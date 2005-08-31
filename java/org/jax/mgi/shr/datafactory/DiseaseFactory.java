package org.jax.mgi.shr.datafactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.net.MalformedURLException;
import java.io.IOException;
import org.jax.mgi.shr.stringutil.Sprintf;
import org.jax.mgi.shr.stringutil.StringLib;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dto.DTOConstants;
import org.jax.mgi.shr.timing.TimeStamper;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.exception.MGIException;


public class DiseaseFactory {
    /////////////////////
    // instance variables
    /////////////////////

    // provides access to the database
    private SQLDataManager sqlDM = null;

    // provides logging capability for info, error, debug logs
    private Logger logger = null;

    // provides parameters needed to configure a DiseaseFactory
    private DataFactoryCfg config = null;

    // used to log profiling information (timings for sections of code)
    private TimeStamper timer = null;


    ///////////////
    // Constructors
    ///////////////
    /** constructor; instantiates and initializes new DiseaseFactory.
     * @param config provides parameters needed to configure factory,
     * @param sqlDM provides access to a database
     * @param logger provides logging capability
     * @assumes nothing
     * @effects nothing
     * @throws nothing
     */
    public DiseaseFactory (DataFactoryCfg config, 
                           SQLDataManager sqlDM,
                           Logger logger)
    {
        this.config = config;
        this.sqlDM = sqlDM;
        this.logger = logger;
        return;

    }

    /** find a unique key identifying the vocab subset specified by the given
     *    'parms'.
     * @param parms set of parameters specifying which subset we are seeking.
     *    Only one key in 'parms' is checked as we only cache based on
     *    subsets.  The key is:  "key"  This represents a _Term_key
     * @return String a unique key identifying the mp term object specified in
     *    the given set of 'parms', or null if none can be found
     * @assumes nothing
     * @effects nothing
     * @throws DBException There is no db interaction so exception not thrown
     */
    public String getKey (Map parms) throws DBException
    {
        // if a 'key' is directly specified in the 'parms', then assume it is
        // a term key, and return it...

        String keyStr = StringLib.getFirst ((String[]) parms.get ("key"));
        if (keyStr != null) {
            return keyStr;
        }

        return null;		// no key could be found
    }


    /* ------------------------------------------------------------------- */


    //////////////////////////////////////////////////////
    // methods to retrieve sets of sections of information
    //////////////////////////////////////////////////////

    /** retrieve the Human Disease and Mouse Model Detail information.
     * @param parms This will contain parameters to get detail information
     *    for human disease/mouse model data.
     * @return DTO which defines all detail about given disease term
     * @assumes nothing
     * @effects nothing 
     * @throws DBException if there is a problem querying the database or
     *    processing the results
     * @throws MalformedURLException if any URLs are invalid
     * @throws IOException if there are problems reading from URLs via HTTP
     */
    public DTO getDiseaseInfo (Map parms)
        throws DBException, MalformedURLException, IOException, MGIException
    {
        
        logger.logDebug("Getting Disease Info");

        // all disease info for term key submitted
        DTO disease = DTO.getDTO();

        // data for a particular section, to merge with 'disease'
        DTO section = null;

        // term key as a String
        String keyStr = getKey (parms);

        // if we could not find a term key based on 'parms', then bail out
        // before bothering with anything else
        if (keyStr == null) {
            
            logger.logError ("No valid 'key' parameter was passed!");
            throw new MGIException("No valid 'key' parameter was provided!");
        }

        // term key as an integer
        int key = Integer.parseInt (keyStr);

        // get data for individual sections.  For the sake of efficiency, we
        // make sure to always return the 'section' to the pool of available
        // DTOs once we are done with it.
        
        section = this.getBasicInfo (key);
        disease.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved basic term info");
        
        section = this.getSynonyms (key);
        disease.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved synonyms");
        
        section = 
            this.getGeneDetails(key);
        disease.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved gene details");

        section = this.getTransgeneDetails (key);
        disease.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved transgene details");

        section = this.getMouseModels (key,
                                (String)disease.get(DTOConstants.PrimaryAccID),
                                (String)disease.get(DTOConstants.Term));
        disease.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved mouse models");
                    
        return disease;
    }
    /* ------------------------------------------------------------------- */

    /////////////////////////////////////////////////////////
    // methods to retrieve individual sections of information
    /////////////////////////////////////////////////////////

    /** retrieve basic information about the disease term with the given 'key'.
     * @param key the term key of the disease term whose data we seek
     * @return DTO contains information retrieved from the database.  See
     *    notes.  If no data for the specified term is found, then returns
     *    an empty DTO.
     * @assumes nothing
     * @effects queries the database
     * @throws DBException if there are problems querying the database or
     *    stepping through the results.
     * @notes The following constants from DTOConstants are included as
     *    fieldnames in the returned DTO:
     *    <OL>
     *    <LI> TermKey : Integer
     *    <LI> Term : String
     *    <LI> PrimaryAccID : String
     *    <LI> DatabaseDate : String
     *    </OL>
     */
    public DTO getBasicInfo (int key) throws DBException
    {
        logger.logDebug("Getting Disease Basic Info");
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        DTO disease = DTO.getDTO();	// start with an empty DTO

        // get the most basic disease data.  If we find no results, then just
        // return an empty DTO.
        String cmd = Sprintf.sprintf (BASIC_TERM_DATA, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.
            executeQuery ( cmd );

        if (!nav.next()) {
            nav.close();
            nav = null;
            rr = null;
            return disease;
        }

        // otherwise collect the basic data fields

        rr = (RowReference) nav.getCurrent();

        disease.set (DTOConstants.TermKey, rr.getInt(1));
        disease.set (DTOConstants.Term, rr.getString(2));
        disease.set (DTOConstants.PrimaryAccID, rr.getString(3));
        disease.set (DTOConstants.DatabaseDate, rr.getString(4));


        nav.close();
        nav = null;
        rr = null;
        
        return disease;
    }


    /* -------------------------------------------------------------------- */

    /** retrieve any synonyms for the term with the given 'key'.
    * @param key the term key of the term whose data we seek
    * @return DTO with the DTOConstants.Synonyms field mapped to a Vector of
    *    Strings, each of which is one synonym.  If there are no synonyms for
    *    the given term, then an empty DTO is returned.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getSynonyms (int key) throws DBException
    {
        logger.logDebug("Getting Synonyms");
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        Vector synonyms = null;		// has one String per synonym
        DTO disease = DTO.getDTO();  // start with an empty DTO

        // get the disease synonyms.  If we find no results, then just
        // return an empty DTO.
        String cmd = Sprintf.sprintf (SYNONYMS, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.
            executeQuery ( cmd );

        if (nav.next()) {
            // collect all synonyms returned by the query, then add them to
            // the 'disease' DTO

            synonyms = new Vector();
            do {
                rr = (RowReference) nav.getCurrent();
                synonyms.add (rr.getString(1));

            } while (nav.next());

            disease.set (DTOConstants.Synonyms, synonyms);
        }
        nav.close();
        nav = null;
        rr = null;
            
        return disease;
    }

    /* -------------------------------------------------------------------- */

    /** retrieve any gene details for the disease term with the given 'key'.
    * @param key the term key of the term whose data we seek
    * @return DTO with several attributes added to support three concepts:
    *    <ul>
    *    <li> Mutations in both the mouse and orthologous human gene associated
    *         with this human disease.
    *    <li> Mutations in mouse gene associated with this human disease.  No 
    *         mutations in orthologous human gene are known to be associated
    *         with disease.
    *    <li> Mutations in human gene are associated with this disease. No 
    *         mutations in orthologous mouse gene are known to be associated 
    *         with human disease.
    *    </ul>
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getGeneDetails (int key) 
        throws DBException
    {
        logger.logDebug("Getting Gene Info");
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        DTO disease = DTO.getDTO();

        // get the "both": mouse and human gene associated with disease (no 
        //                 transgenes and other mutations). 
        HashMap genes = null;        // the pair of mouse and human genes
        ArrayList both = new ArrayList(); // has a hashmap per pair of genes
        HashSet bothUsed = new HashSet();  // only want the mouse/human pair
                                           // once.
        String cmd = Sprintf.sprintf (GENE_BOTH, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );

        while (nav.next()) {
            rr = (RowReference)nav.getCurrent();

            int species = (rr.getInt(5)).intValue();

            genes = new HashMap();
            Integer mouseKey = null;
            Integer humanKey = null;
            if (species == DBConstants.Species_Mouse) {
                mouseKey = rr.getInt(1);
                humanKey = rr.getInt(3);
                genes.put("mouseKey", mouseKey);
                genes.put("mouseSymbol", rr.getString(2));
                genes.put("humanKey", humanKey);
                genes.put("humanSymbol", rr.getString(4));
            }
            else {
                mouseKey = rr.getInt(3);
                humanKey = rr.getInt(1);
                genes.put("mouseKey", mouseKey);
                genes.put("mouseSymbol", rr.getString(4));
                genes.put("humanKey", humanKey);
                genes.put("humanSymbol", rr.getString(2));
            }    
            String lookup = "" + mouseKey + ":" + humanKey;
            System.out.println("Both => " + lookup);
            if ( ! bothUsed.contains(lookup) ) {
                bothUsed.add(lookup);
                both.add(genes);
            }
            else {
                System.out.println("Already added => " + lookup);
            }
        }
        nav.close();
        nav = null;
        rr = null;

        if (both.size() > 0) {
            disease.set("both", both);
        }

        // get the "mouse": mouse associated with disease. No human. 
        //                  no transgene and other mutations
        ArrayList mouse = new ArrayList();	// has a hashmap per pair of genes
        cmd = Sprintf.sprintf (GENE_MOUSE, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );

        while (nav.next()) {
            rr = (RowReference)nav.getCurrent();

            genes = new HashMap();
            genes.put("mouseKey", rr.getInt(1));
            genes.put("mouseSymbol", rr.getString(2));
            genes.put("humanKey", rr.getInt(3));
            String hsym = rr.getString(4);
            if (hsym == null) {
                hsym = "none identified";
            }
            genes.put("humanSymbol", hsym);
            mouse.add(genes);
        }
        nav.close();
        nav = null;
        rr = null;

        if (mouse.size() > 0) {
            disease.set("mouse", mouse);
        }

        // get the "human": human associated with disease. No mouse. 
        //                  no transgene and other mutations
        ArrayList human = new ArrayList();	// has a hashmap per pair of genes
        cmd = Sprintf.sprintf (GENE_HUMAN, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );

        while (nav.next()) {
            rr = (RowReference)nav.getCurrent();

            genes = new HashMap();
            genes.put("mouseKey", rr.getInt(1));
            String msym = rr.getString(2);
            if (msym == null) {
                msym = "none identified";
            }
            genes.put("mouseSymbol", msym);
            genes.put("humanKey", rr.getInt(3));
            genes.put("humanSymbol", rr.getString(4));
            human.add(genes);
        }
        nav.close();
        nav = null;
        rr = null;

        if (human.size() > 0) {
            disease.set("human", human);
        }

        return disease;
    }

    /* -------------------------------------------------------------------- */

    /** retrieve any transgene details for disease term with the given 'key'.
    * @param key the term key of the term whose data we seek
    * @return DTO with an array of hashmaps where each hashmap represents
    *         the gene key and symbol for a transgene or other mutation
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getTransgeneDetails (int key) 
        throws DBException
    {
        logger.logDebug("Getting transgene Info");
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        DTO disease = DTO.getDTO();

        // get the "trans": transgenes and other muts associated with disease. 
        HashMap gene = null;        // the mouse gene (key and symbol)
        ArrayList trans = new ArrayList();		// has a hashmap per gene
        String cmd = Sprintf.sprintf (GENE_TRANSGENE, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );

        while (nav.next()) {
            rr = (RowReference)nav.getCurrent();
            
            gene = new HashMap();
            gene.put("mouseKey", rr.getInt(1));
            gene.put("mouseSymbol", rr.getString(2));
            trans.add(gene);
        }
        nav.close();
        nav = null;
        rr = null;

        if (trans.size() > 0) {
            disease.set("transgenes", trans);
        }

        return disease;
    }

    /* -------------------------------------------------------------------- */

    /** retrieve the mouse modelss for the disease term with the given 'key'.
    * @param key the term key of the term whose data we seek
    * @param ID is the omim id for this term
    * @param term is the term value for this disease
    * @return DTO with several attributes added to support five concepts:
    *    <ul>
    *    <li> category1 = Models with phenotypic similarity to human disease 
    *                     and etiologies involving orthologs.
    *    <li> category2 = Models with phenotypic similarity to human disease 
    *                     but distinct etiologies
    *    <li> category3 = Models with phenotypic similarity to human disease 
    *                     with unknown etiology or involving genes where 
    *                     ortholog is unknown
    *    <li> category4 = No similarity to expected human disease phenotype 
    *                     was found
    *    <li> category5 = Models involving transgenes or other mutation types
    *    </ul>
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getMouseModels (int key, String ID, String term) 
        throws DBException
    {
        logger.logDebug("Getting Mouse Models Info");
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        DTO disease = DTO.getDTO();

        //  The sets of phenotypes for this term, by category
        HashMap cat1 = new HashMap(); // involving orthologs
        HashMap cat2 = new HashMap(); // distinct etiologies
        HashMap cat3 = new HashMap(); // unknown etiology or ortholog
        HashMap cat4 = new HashMap(); // No similarity
        HashMap cat5 = new HashMap(); // transgenes and other mutations
        
        Phenotype p = null;
        Integer genoKey = null;
        Integer genoOrder = new Integer(0);
        String allelicComp = null;
        String background = null;
        Integer ref = null;
        String jnum = null;
        int category = 0;
        //  Iterate through the rows and add the genotypes to
        //  the set of phenotypes.
        //  There is an assumption that certain rules have been dealt with
        //  at the database level.  such as, if a genotype is "category 4"
        //  no similarity, it can't be part of another category, or a genotype
        //  can belong to no more that 2 categories.
        String cmd = Sprintf.sprintf (MOUSE_MODEL, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );
        while (nav.next()) {
            rr = (RowReference)nav.getCurrent();
            
            Integer tmpGeno = rr.getInt(1);
            int tmpCat = rr.getInt(6).intValue();
            // Null genotypes and category -1 are irrelevant
            if (tmpGeno == null || tmpCat == -1) {
                continue;
            }

            //  If this is our first time through, just set values.
            if (genoKey == null) {
                genoKey = tmpGeno;
                allelicComp = rr.getString(2);
                background = rr.getString(3);
                ref = rr.getInt(4);
                jnum = rr.getString(5);
                category = tmpCat;
                p = new Phenotype(genoKey, genoOrder, allelicComp, background);
                p.addAnnotTerm(new Integer(key), ID, term, new Integer(0));
                p.addAnnotEvidence(new Integer(key), new Integer(0), ref,  
                                   jnum, new String(""), new String(""));
            }
            //  In this case we should only be adding a reference
            else if (genoKey.equals(tmpGeno) && category == tmpCat) {
                ref = rr.getInt(4);
                jnum = rr.getString(5);
                p.addAnnotEvidence(new Integer(key), new Integer(0), ref,  
                                   jnum, new String(""), new String(""));
            }
            //  Treat a change in category for a phenotype like a new
            //  phenotype as it's stored with a seperate category with
            //  different references.
            else {
                //  Put the phenotype object in the propery category hashset
                switch (category)  {
                case 1:
                    cat1.put(genoKey,p);
                    break;
                case 2:
                    cat2.put(genoKey,p);
                    break;
                case 3:
                    cat3.put(genoKey,p);
                    break;
                case 4:
                    cat4.put(genoKey,p);
                    break;
                case 5:
                    cat5.put(genoKey,p);
                    break;
                default:
                    // not a valid option
                    break;
                }
                //  Now reset all of the variables
                genoKey = tmpGeno;
                allelicComp = rr.getString(2);
                background = rr.getString(3);
                ref = rr.getInt(4);
                jnum = rr.getString(5);
                category = tmpCat;
                p = new Phenotype(genoKey, genoOrder, allelicComp, background);
                p.addAnnotTerm(new Integer(key), ID, term, new Integer(0));
                p.addAnnotEvidence(new Integer(key), new Integer(0), ref, 
                                   jnum, new String(""), new String(""));

            }
                    
        }
        //  Now put the last phenotype in the proper hashset, assuming there
        //  were any results at all.
        if (genoKey != null) {
            //  Put the phenotype object in the propery category hashset
            switch (category)  {
            case 1:
                cat1.put(genoKey,p);
                break;
            case 2:
                cat2.put(genoKey,p);
                break;
            case 3:
                cat3.put(genoKey,p);
                break;
            case 4:
                cat4.put(genoKey,p);
                break;
            case 5:
                cat5.put(genoKey,p);
                break;
            default:
                // not a valid option
                break;
            }
        }
        nav.close();
        nav = null;
        rr = null;

        //  Populate the dto with all of our categories
        ArrayList values = new ArrayList(cat1.values());
        Collections.sort(values, 
                         new PhenotypicComparator(PhenotypicComparator.ALPHA));
        disease.set("category1", values);
        values = new ArrayList(cat2.values());
        Collections.sort(values, 
                         new PhenotypicComparator(PhenotypicComparator.ALPHA));
        disease.set("category2", values);
        values = new ArrayList(cat3.values());
        Collections.sort(values, 
                         new PhenotypicComparator(PhenotypicComparator.ALPHA));
        disease.set("category3", values);
        values = new ArrayList(cat4.values());
        Collections.sort(values, 
                         new PhenotypicComparator(PhenotypicComparator.ALPHA));
        disease.set("category4", values);
        values = new ArrayList(cat5.values());
        Collections.sort(values, 
                         new PhenotypicComparator(PhenotypicComparator.ALPHA));
        disease.set("category5", values);

        return disease;
    }

    /* -------------------------------------------------------------------- */

    /** add the given 'entry' to the TimeStamper in this AlleleFactory, if
     *    one was added using setTimeStamper().
     * @param entry the entry to write to the TimeStamper
     * @return nothing
     * @assumes nothing
     * @effects nothing
     * @throws nothing
     */
    private void timeStamp (String entry)
    {
        if (this.timer != null)
            {
                this.timer.record (entry);
            }
        return;
    }

    /* -------------------------------------------------------------------- */

    /** log the given 'entry' to the informational log within 'this.logger'
     * @param entry the entry to write to 'log'
     * @return nothing
     * @assumes nothing
     * @effects writes 'entry' to the informational log
     * @throws nothing
     */
    private void logInfo (String entry)
    {
        if (this.logger != null)
            {
                logger.logInfo (entry);
            }
        return;
    }

    //////////////////
    // class variables
    //////////////////

    /* --------------------------------------------------------------------
    ** class variables -- used to hold standard SQL statements, so we don't
    ** need to re-join the Strings in each thread of the servlet.  For each
    ** one, we note the pieces that need to be filled in using Sprintf.
    ** --------------------------------------------------------------------
    */

    // find the Term that is associated with the given key.
    // fill in: term key (int)
    private static final String BASIC_TERM_DATA =
		"select vt._Term_key, vt.term, ac.accID,  "
		+    " convert(varchar, db.lastdump_date, 101) "
        + "from VOC_Term vt, ACC_Accession ac, MGI_dbInfo db "
		+ "where vt._Term_key = %d "
        + "and vt._Term_key = ac._Object_key "
        + "and ac._MGIType_key =  " + DBConstants.MGIType_VocTerm
        + " and ac._LogicalDB_key = " + DBConstants.LogicalDB_OMIM
        + " and ac.preferred = 1 ";

    // get synonyms
    // fill in: term key (int)
    private static final String SYNONYMS =
		  "select distinct synonym"
		+ " from MGI_Synonym"
		+ " where _Object_key = %d"
		+ " and _MGIType_key = " + DBConstants.MGIType_VocTerm
		+ " order by synonym";

    // get genes where both mouse and human gene are associated with human
    // disease.
    // fill in: term key (int)
    private static final String GENE_BOTH =
		  "select distinct o._Marker_key, o.markerSymbol, "
        + " o._OrthologMarker_key, o.orthologSymbol, o._Organism_key "
		+ " from MRK_OMIM_Cache o "
		+ " where _Term_key = %d "
        + " and omimCategory2 = 1 "
        + " and o._Marker_Type_key = 1"; // only genes

    // get genes where mouse with human disease.  Human gene not known to be.
    // fill in: term key (int)
    private static final String GENE_MOUSE =
		  "select distinct o._Marker_key, o.markerSymbol, "
        + " o._OrthologMarker_key, o.orthologSymbol "
		+ " from MRK_OMIM_Cache o "
		+ " where _Term_key = %d "
		+ " and _Organism_key = " + DBConstants.Species_Mouse
        + " and omimCategory2 = 2 "
        + " and _Marker_Type_key = 1"; // only genes

    // get genes where human with human disease.  Mouse gene not known to be.
    // fill in: term key (int)
    private static final String GENE_HUMAN =
		  "select distinct o._OrthologMarker_key, o.orthologSymbol, "
        + " o._Marker_key, o.markerSymbol "
		+ " from MRK_OMIM_Cache o "
		+ " where _Term_key = %d "
		+ " and _Organism_key = " + DBConstants.Species_Human
        + " and omimCategory2 = 3 "
        + " and _Marker_Type_key = 1 "; // only genes


    // get mouse transgenes associated with human disease
    // fill in: term key (int)
    private static final String GENE_TRANSGENE =
        "select distinct o._Marker_key, o.markerSymbol "
        + " from MRK_OMIM_Cache o "
        + " where _Term_key = %d "
        + " and _Organism_key = " + DBConstants.Species_Mouse
        + " and omimCategory2 in (1, 2, 3) "
        + " and _Marker_Type_key != 1";

    // get all mouse model data for a given term
    // Order is relevant for building phenotype structure.
    // fill in: term key (int)
    private static final String MOUSE_MODEL =
        "select distinct o._Genotype_key, o.genotypeDisplay, o.strain, "
        + " o._Refs_key, o.jnumID, o.omimCategory3, o.isNot "
        + " from MRK_OMIM_Cache o " 
        + " where o._Term_key = %d " 
        + " order by o._Genotype_key, o.omimCategory3, o._Refs_key";

}
