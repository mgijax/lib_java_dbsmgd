package org.jax.mgi.shr.datafactory;

import java.util.Map;
import java.util.Vector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import org.jax.mgi.shr.stringutil.Sprintf;
import org.jax.mgi.shr.stringutil.StringLib;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dto.DTOConstants;
import org.jax.mgi.shr.cache.ExpiringObjectCache;
import org.jax.mgi.shr.timing.TimeStamper;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.DBException;

/**
 * @module AlleleFactory.java
 * @author dow
 */

/** The AlleleFactory class contains many methods which encapsulate
*    knowledge of the allele-related portions of the schema.  They allow for
*    easy retrieval of various aspects of a allele's associated data.
* @is a factory for retrieving information related to alleles.
* @has all data available for alleles, retrieved from a database
* @does queries a database to retrieve subsets of information about alleles.
*    Retrieval methods always return a new <tt>DTO</tt>.  These DTO objects
*    may then be merged if needed.  Public methods include:  (parameters not
*    listed here)
*    <OL>
*    <LI> getKeyForSymbol() -- find the allele key for a given allele symbol
*    <LI> getKeyForID() -- find the allele key for a given MGI ID
*    <LI> getFullInfo() -- get all available information for a allele
*    <LI> getBasicInfo() -- get a minimal set of available information for a
*        allele
*    <LI> <I>Many other methods exist for individual data sections.  See below
*        for details.</I>
*    </OL>
*/
public class AlleleFactory
{
    /////////////////////
    // instance variables
    /////////////////////

    // provides access to the database
    private SQLDataManager sqlDM = null;

    // provides logging capability for info, error, debug logs
    private Logger logger = null;

    // provides parameters needed to configure a AlleleFactory
    private DataFactoryCfg config = null;

    // used to log profiling information (timings for sections of code)
    private TimeStamper timer = null;

    ///////////////
    // Constructors
    ///////////////

    /** constructor; instantiates and initializes a new AlleleFactory.
     * @param config provides parameters needed to configure a AlleleFactory,
     * @param sqlDM provides access to a database
     * @param logger provides logging capability
     * @assumes nothing
     * @effects nothing
     * @throws nothing
     */
    public AlleleFactory (DataFactoryCfg config, SQLDataManager sqlDM,
        Logger logger)
    {
        this.config = config;
        this.sqlDM = sqlDM;
        this.logger = logger;
        return;
    }


    /////////////////////////////////////
    // methods for retrieving allele keys
    /////////////////////////////////////

    /** find a unique key identifying the allele specified by the given
     *    'parms'.
     * @param parms set of parameters specifying which allele we are seeking.
     *    Three keys in 'parms' are checked, in order of preference:  "key"
     *    (allele key as a String), "id" (allele accession ID), and "symbol"
     *    (allele symbol).
     * @return String a unique key identifying the allele object specified in
     *    the given set of 'parms', or null if none can be found
     * @assumes nothing
     * @effects queries the database using 'sqlDM'
     * @throws DBException if there is a problem while attempting to query the
     *    database using 'sqlDM'
     */
    public String getKey (Map parms) throws DBException
    {
        // if a 'key' is directly specified in the 'parms', then assume it is
        // a allele key, and return it...

        String keyStr = StringLib.getFirst ((String[]) parms.get ("key"));
        if (keyStr != null) {
            this.timeStamp ("Used key parameter directly for allele key");
            return keyStr;
        }

        // otherwise, our second choice is a allele's accession ID...

        int key = -1;

        String accID = StringLib.getFirst ((String[]) parms.get ("id"));
        if (accID != null){
            key = this.getKeyForID (accID);
        }

        // and, finally, we check for 'symbol' as a last resort...

        if (key == -1){
            String symbol = StringLib.getFirst ((String[]) parms.
                                                get ("symbol") );
            if (symbol != null){
                key = this.getKeyForSymbol (symbol);
            }
        }

        // if we found an integer key, then convert it to a String for return

        if (key != -1){
            this.timeStamp ("Retrieved allele key from database");
            return Integer.toString(key);
        }
        return null;		// no key could be found
    }

    /* ------------------------------------------------------------------- */

    /** find the allele key that corresponds to the given allele 'symbol'.  If
     *    multiple alleles have the same symbol, then we return the key
     *    for the one which is the current symbol (if any).  If none are
     *    current, then we arbitrarily choose one of the keys to return.
     * @param symbol the allele symbol (for mouse)
     * @return int the allele key corresponding to 'symbol'; will be -1 if
     *    no allele key could be found
     * @assumes Only one current allele can have the same symbol.
     * @effects nothing
     * @throws DBException if there is a problem querying the database or
     *    processing the results.
     */
    public int getKeyForSymbol (String symbol) throws DBException
    {
        int alleleKey = -1;		    // the allele key we will return
        int thisStatus = -1;		// the allele status key of this row
        int thisKey = -1;	  	    // the allele key of this row
        ResultsNavigator nav = null;// set of query results
        RowReference rr = null;		// one row in 'nav'

        nav = this.sqlDM.executeQuery ( Sprintf.sprintf (KEY_FOR_SYMBOL, 
                                                         symbol) );

        while (nav.next())	{	// step through all returned rows
            rr = (RowReference) nav.getCurrent();
            thisKey = rr.getInt(1).intValue();


            alleleKey = thisKey;
        }
        nav.close();
        return alleleKey;
    }

    /* ------------------------------------------------------------------- */

    /** find the allele key that corresponds to the given allele 'accID'.  If
     *    multiple alleles have the same 'accID', then we arbitrarily
     *    choose one of the keys to return.
     * @param accID the accession ID for which we seek an associated allele
     * @return int the allele key corresponding to 'accID'; will be -1 if
     *    no allele key could be found
     * @assumes That returning any allele associated with that accession ID is
     *    good enough.  In the case where 'accID' is an object's primary MGI
     *    ID, this is valid.
     * @effects nothing
     * @throws DBException if there is a problem querying the database or
     *    processing the results.
     */
    public int getKeyForID (String accID) throws DBException
    {
        int alleleKey = -1;		// the allele key we will return
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'

        nav = this.sqlDM.executeQuery (Sprintf.sprintf (KEY_FOR_ID, accID) );

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            alleleKey = rr.getInt(1).intValue();
        }
        nav.close();
        return alleleKey;
    }

    /* ------------------------------------------------------------------- */


    //////////////////////////////////////////////////////
    // methods to retrieve sets of sections of information
    //////////////////////////////////////////////////////

    /** retrieve the full suite of data available for the allele specified in
    *    'parms'.
    * @param parms set of parameters specifying which allele we are seeking.
    *    Three keys in 'parms' are checked, in order of preference:  "key"
    *    (allele key as a String), "id" (allele accession ID), and "symbol"
    *    (allele symbol).
    * @return DTO which defines all allele data fields
    * @assumes nothing
    * @effects retrieves all allele data by quering a database and retrieving
    *    data via HTTP as needed
    * @throws DBException if there is a problem querying the database or
    *    processing the results
    * @throws MalformedURLException if any URLs are invalid
    * @throws IOException if there are problems reading from URLs via HTTP
    */
    public DTO getFullInfo (Map parms)
            throws DBException, MalformedURLException, IOException
    {
        logger.logDebug("Getting Full Allele Info");

        // all data for the allele with the given 'key'
        DTO allele = DTO.getDTO();

        // data for a particular section, to merge with 'allele'
        DTO section = null;

        // allele key as a String
        String keyStr = getKey (parms);

        // if we could not find a allele key based on 'parms', then bail out
        // before bothering with anything else
        if (keyStr == null) {
            this.logInfo ("Could not find allele");
            return allele;
        }

        // allele key as an integer
        int key = Integer.parseInt (keyStr);

        // get data for individual sections.  For the sake of efficiency, we
        // make sure to always return the 'section' to the pool of available
        // DTOs once we are done with it.

        section = this.getBasicInfo (key);
        allele.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved basic allele info");

        section = this.getSynonyms (key);
        allele.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved synonyms");

        section = this.getDetails (key);
        allele.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved allele details");

        section = this.getExpressionResultCounts (key);
        allele.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved expression result counts");

        section = this.getGeneInformation (key);
        allele.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved gene information");

        section = this.getPhenotypes (key);
        allele.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved allele phenotype data");

        section = this.getNotes (key);
        allele.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved allele notes");

        section = this.getQtlExpts (key);
        allele.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved qtl experiments");

        section = this.getReferenceInfo (key);
        allele.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved reference data");

        return allele;
    }
    /* ------------------------------------------------------------------- */

    /////////////////////////////////////////////////////////
    // methods to retrieve individual sections of information
    /////////////////////////////////////////////////////////

    /** retrieve basic information about the allele with the given 'key'.
     * @param key the allele key of the allele whose data we seek
     * @return DTO contains information retrieved from the database.  See
     *    notes.  If no data for the specified allele is found, then returns
     *    an empty DTO.
     * @assumes nothing
     * @effects queries the database
     * @throws DBException if there are problems querying the database or
     *    stepping through the results.
     * @notes The following constants from DTOConstants are included as
     *    fieldnames in the returned DTO:
     *    <OL>
     *    <LI> AlleleKey : Integer
     *    <LI> AlleleSymbol : String
     *    <LI> AlleleName : String
     *    <LI> StrainOfOrigin : String
     *    <LI> InheritanceMode : String
     *    <LI> AlleleType : String
     *    <LI> ESCellLine : String
     *    <LI> ESCellLineStrain : String
     *    <LI> MutatntESCellLine : String
     *    <LI> CellLineProvider : String
     *    <LI> DatabaseDate : String
     *    <LI> PrimaryAccID (optional) : String
     *    </OL>
     */
    public DTO getBasicInfo (int key) throws DBException
    {
        logger.logDebug("Getting Allele Basic Info");
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        DTO allele = DTO.getDTO();	// start with an empty DTO

        // get the most basic allele data.  If we find no results, then just
        // return an empty DTO.
        String cmd = Sprintf.sprintf (BASIC_ALLELE_DATA, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.
            executeQuery ( cmd );

        if (!nav.next()) {
            return allele;
        }

        // otherwise collect the basic data fields

        rr = (RowReference) nav.getCurrent();

        allele.set (DTOConstants.AlleleKey, new Integer(key));
        allele.set (DTOConstants.AlleleSymbol, rr.getString(1));
        allele.set (DTOConstants.AlleleName, rr.getString(2));
        allele.set (DTOConstants.StrainOfOrigin, rr.getString(3));
        allele.set (DTOConstants.InheritanceMode, rr.getString(4));
        allele.set (DTOConstants.AlleleType, rr.getString(5));
        allele.set (DTOConstants.ESCellLine, rr.getString(7));
        allele.set (DTOConstants.ESCellLineStrain, rr.getString(8));
        allele.set (DTOConstants.MutantESCellLine, rr.getString(9));
        allele.set (DTOConstants.CellLineProvider, rr.getString(10));
        allele.set (DTOConstants.DatabaseDate, rr.getString(11));
        allele.set (DTOConstants.ApprovalDate, rr.getString(12));
        allele.set (DTOConstants.SubmittedBy, rr.getString(13));


        // If the allele is approved, then we need to get the primary MGI ID.

        String alleleStatus = rr.getString(6);
        allele.set (DTOConstants.AlleleStatus, alleleStatus);

        if (alleleStatus.equals(DBConstants.Allele_Approved)) {
            nav.close();

            nav = this.sqlDM.
                executeQuery (Sprintf.sprintf (PRIMARY_MGI_ID, key));
            if (nav.next()) {
                rr = (RowReference) nav.getCurrent();
                allele.set (DTOConstants.PrimaryAccID, rr.getString(1));
            }

        }
        nav.close();

        //  Get the nomenclature notes.
        StringBuffer notes = new StringBuffer();  // nomenclature notes
        cmd = Sprintf.sprintf (NOMEN_NOTE, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );
        while (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            notes.append(rr.getString(1));
        }

        if (notes.length() > 0) {
            allele.set(DTOConstants.NomenNotes, notes.toString());
        }
        nav.close();

        
        return allele;
    }
    /* -------------------------------------------------------------------- */

    /** retrieve any synonyms for the allele with the given 'key'.
    * @param key the allele key of the allele whose data we seek
    * @return DTO with the DTOConstants.Synonyms field mapped to a Vector of
    *    Strings, each of which is one synonym.  If there are no synonyms for
    *    the given allele, then an empty DTO is returned.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getSynonyms (int key) throws DBException
    {
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        Vector synonyms = null;		// has one String per synonym
        DTO allele = DTO.getDTO();  // start with an empty DTO

        // get the allele synonyms.  If we find no results, then just
        // return an empty DTO.
        String cmd = Sprintf.sprintf (SYNONYMS, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.
            executeQuery ( cmd );

        if (nav.next()) {
            // collect all synonyms returned by the query, then add them to
            // the 'marker' DTO

            synonyms = new Vector();
            do {
                rr = (RowReference) nav.getCurrent();
                synonyms.add (rr.getString(1));

            } while (nav.next());

            allele.set (DTOConstants.Synonyms, synonyms);
        }
        nav.close();
            
        return allele;
    }

    public DTO getDetails (int key) throws DBException
    {
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        DTO allele = DTO.getDTO();

        // get the mutations.  
        Vector mutations = null;		// has one String per mutation
        String cmd = Sprintf.sprintf (MUTATIONS, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.
            executeQuery ( cmd );

        if (nav.next()) {
            // collect all synonyms returned by the query, then add them to
            // the 'allele' DTO

            mutations = new Vector();
            do {
                rr = (RowReference) nav.getCurrent();
                mutations.add (rr.getString(1));

            } while (nav.next());

            allele.set (DTOConstants.Mutations, mutations);
        }
        nav.close();

        //  Get the molecular notes.
        StringBuffer notes = new StringBuffer();  // molecular notes
        cmd = Sprintf.sprintf (MOLECULARNOTES, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );
        while (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            notes.append(rr.getString(1));
        }

        if (notes.length() > 0) {
            allele.set(DTOConstants.MolecularNotes, notes.toString());
        }
        nav.close();


        //  Get the molecular references.
        ArrayList refs = new ArrayList();  // molecular refs
        cmd = Sprintf.sprintf (MOLECULARREFS, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );
        while (nav.next()) {
            HashMap hm = new HashMap();
            rr = (RowReference) nav.getCurrent();
            hm.put("jnum", rr.getString(1));
            hm.put("key", rr.getInt(2));
            refs.add(hm);
        }

        if (refs.size() > 0) {
            allele.set(DTOConstants.MolecularJNums, refs);
        }
        nav.close();

        /*  This section of code is used to determine if the marker 
            associated with the given allele is in IMSR.  Unfortunately
            this code assumes there is an IMSR db in the same server as
            the MGD database.  For now we'll just return true in all cases.
        cmd = Sprintf.sprintf(IMSR, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );
        if (nav.next()) {
            rr = (RowReference)nav.getCurrent();
            allele.set(DTOConstants.InIMSR, new Boolean(true));
        }
        else {
            allele.set(DTOConstants.InIMSR, new Boolean(false));
        }
        */
        allele.set(DTOConstants.InIMSR, new Boolean(true));
        return allele;
    }

    public DTO getExpressionResultCounts (int key) throws DBException
    {
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        DTO allele = DTO.getDTO();	// start with an empty DTO

        // get the gxd count.  If we find no results, then just
        // return an empty DTO.
        String cmd = Sprintf.sprintf (GENEEXPRESSION, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.
            executeQuery ( cmd );

        if (!nav.next()) {
            return allele;
        }

        // otherwise collect the basic data fields

        rr = (RowReference) nav.getCurrent();

        allele.set (DTOConstants.ExpressionAssayCounts, rr.getInt(1));

        return allele;
    }

    public DTO getGeneInformation (int key) throws DBException
    {
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        DTO allele = DTO.getDTO();	// start with an empty DTO

        // get the marker data associated with this allele.  
        // If we find no results, then just
        // return an empty DTO.
        String cmd = Sprintf.sprintf (MARKER, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.
            executeQuery ( cmd );

        if (!nav.next()) {
            return allele;
        }

        // otherwise collect the basic data fields

        rr = (RowReference) nav.getCurrent();

        Integer markerKey = rr.getInt(1);
        allele.set (DTOConstants.MarkerKey, markerKey);
        allele.set (DTOConstants.MarkerSymbol, rr.getString(2));
        allele.set (DTOConstants.MarkerName, rr.getString(3));
        allele.set (DTOConstants.Chromosome, rr.getString(4));
        allele.set (DTOConstants.MarkerType, rr.getString(6));

        // only define the cytogenetic offset if it is non-null

        if (rr.getString(5) != null) {
            allele.set (DTOConstants.CytogeneticOffset, rr.getString(5));
        }
        nav.close();

        if (markerKey != null) {
            nav = this.sqlDM.
                executeQuery(Sprintf.sprintf(MAP_POSITION, 
                                             markerKey.intValue()));

            if (nav.next()) {
                rr = (RowReference) nav.getCurrent();
                allele.set (DTOConstants.MapPosition, rr.getDouble(1));
            }
            nav.close();
        
            DTO seq = null;
            nav = this.sqlDM.
                executeQuery (Sprintf.sprintf (REPRESENTIVE_SEQUENCE_KEYS,
                                               markerKey.intValue()));
            if (nav.next()) {
                SequenceFactory seqFact = new SequenceFactory(config,
                                                              sqlDM,
                                                              logger);
                String seqType;
                do  {
                    rr = (RowReference) nav.getCurrent();
                    seq = seqFact.getBasicInfo(rr.getInt(1).intValue());
                    seqType = rr.getString(2);

                    if(seqType.equals("genomic")) {
                        allele.set (DTOConstants.RepDNASeq,seq);
                    } else if (seqType.equals("transcript")) {
                        allele.set (DTOConstants.RepRNASeq,seq);
                    } else if (seqType.equals("polypeptide")) {
                        allele.set (DTOConstants.RepProteinSeq,seq);
                    }
                } while (nav.next());
            }
            nav.close();
        }
        
        return allele;
    }

    public DTO getPhenotypes (int key) throws DBException
    {
        DTO allele = DTO.getDTO();
        PhenotypeFactory pf  = new PhenotypeFactory(config, sqlDM, logger);
        try {
            allele = pf.getPhenotypes(key);
        }
        catch (Exception e) {
            logger.logError("Problem getting phenotype data");
            e.printStackTrace();
        }
        return allele;
    }

    public DTO getNotes (int key) throws DBException
    {
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        DTO allele = DTO.getDTO();

        //  Get the general notes.
        StringBuffer notes = new StringBuffer();  // general notes
        String cmd = Sprintf.sprintf (GENERALNOTES, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );
        while (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            notes.append(rr.getString(2));
        }

        if (notes.length() > 0) {
            allele.set(DTOConstants.Notes, notes.toString());
        }
        nav.close();
        
        return allele;
    }

    public DTO getQtlExpts (int key) throws DBException
    {
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        DTO allele = DTO.getDTO();

        //  Get the qtl expt notes.
        ArrayList expts = new ArrayList();
        Integer exptKey = null;
        Integer refsKey = null;
        String jnum = null;
        String notes = null;  // expt notes with jnum link prepended
        String cmd = Sprintf.sprintf (QTL_EXPTS, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );
        while (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            
            //  If this is our first time through, just set values
            if (exptKey == null) {
                exptKey = rr.getInt(1);
                refsKey = rr.getInt(2);
                jnum    = rr.getString(3);
                notes   = "(<a href=\"REFURL" + refsKey + "\"><i>" + jnum +
                    "</i></a>)<br>" + rr.getString(4);
            }
            else if (exptKey.equals(rr.getInt(1))) {
                notes += rr.getString(4);
            }
            else {
                expts.add(notes);

                exptKey = rr.getInt(1);
                refsKey = rr.getInt(2);
                jnum    = rr.getString(3);
                notes   = "(<a href=\"REFURL" + refsKey + "\"><i>" + jnum +
                    "</i></a>)<br>" + rr.getString(4);
            }                
        }

        if (notes != null) {
            expts.add(notes);
        }

        if (expts.size() > 0) {
            allele.set(DTOConstants.QtlExpts, (List)expts);
        }
        nav.close();
        
        return allele;
    }
    public DTO getReferenceInfo (int key) throws DBException
    {

        DTO refCount = getReferenceCount(key);
        DTO firstRef = getFirstReference(key);
        refCount.merge(firstRef);
        DTO.putDTO (firstRef);

        return refCount;
    }

    /* -------------------------------------------------------------------- */

    /** get a count of references for the allele with the given 'key'.
     * @param key the allele key of the allele whose data we seek
     * @return DTO with DTOConstants.ReferenceCount mapped to an Integer count,
     *    if there are any references for the allele.  If not, an empty DTO is
     *    returned.
     * @assumes nothing
     * @effects queries the database
     * @throws DBException if there are problems querying the database or
     *    stepping through the results
     * @notes This method should probably be moved into a ReferenceFactory
     *    class which knows how to retrieve data about references.  This method
     *    excludes from the count references which are listed in four different
     *    MGI Sets:  Load References, Personal References, Genbank References,
     *    and Curatorial References.
     */
    public DTO getReferenceCount (int key) throws DBException
    {
        ResultsNavigator nav = null;		    // set of query results
        RowReference rr = null;			    // one row in 'nav'
        DTO allele = DTO.getDTO();		    // start with a new DTO

        // cache of already-generated objects
        ExpiringObjectCache cache = ExpiringObjectCache.getSharedCache();

        // key for 'excludedKeys' in 'cache'
        String cacheKey = "org.jax.mgi.shr.datafactory.AlleleFactory.RC";

        // comma-delimited String of _Set_keys for sets of references to not
        // be displayed by the "All" link for references
        String excludedKeys = (String) cache.get(cacheKey);

        // if the cached value has expired (or not yet been generated), then
        // we need to query the database to build it

        if (excludedKeys == null)
            {
                Integer setKey = null;		// MGI_Set._Set_key
                String name = null;			// MGI_Set.name
                ArrayList list = new ArrayList(4);	// List of keys to be excluded

                nav = this.sqlDM.executeQuery (REFERENCE_NOT_ALL);
                while (nav.next())
                    {
                        rr = (RowReference) nav.getCurrent();
                        name = rr.getString(2);

                        // there are four sets of references to be excluded for the
                        // "All" link...

                        if (name.equals("Load References") ||
                            name.equals("Personal References") ||
                            name.equals("Genbank References") ||
                            name.equals("Curatorial References") )
                            {
                                setKey = rr.getInt(1);
                                list.add (setKey.toString());
                            }
                    }
                nav.close();

                // if we found some sets, then make them a comma-delimited String

                if (list.size() > 0)
                    {
                        excludedKeys = StringLib.join (list, ",");
                    }
                else
                    {
                        // otherwise, use a dummy value (so no sets are excluded), and
                        // we'll just check again when the cache times out

                        excludedKeys = "-1";
                    }

                // store the new value in the cache for one hour

                cache.put (cacheKey, excludedKeys, 60 * 60);
            }

        // finally, do the actual query to get the count of references,
        // excluding those as-needed

        String cmd = Sprintf.sprintf (REFERENCE_COUNT, 
                                           Integer.toString(key),
                                      excludedKeys);
        logger.logDebug(cmd);
        nav = this.sqlDM.
            executeQuery ( cmd );
        if (nav.next())
            {
                rr = (RowReference) nav.getCurrent();
                allele.set (DTOConstants.ReferenceCount, rr.getInt(1) );
            }
        nav.close();
        return allele;
    }


    public DTO getFirstReference (int key) throws DBException
    {
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;	   	// current row in 'nav'
        boolean rowExists = false;	// has an acceptable row been found?
        DTO allele = DTO.getDTO();

        // set of de-emphasized references (not to be highlighted on the
        // allele detail page)
        PrivateRefSet refSet = getPrivateRefSet ();

        this.sqlDM.setScrollable(true);
        nav = this.sqlDM.executeQuery (Sprintf.sprintf(REFERENCE_FIRST, key));
        
        if (nav.next()) {
            rowExists = true;	// assume this row is acceptable

            // if the set of internal-only references contains the one from
            // this row, then we need to move on to check the next row --
            // until we find an acceptable one or we run out of rows.

            rr = (RowReference) nav.getCurrent();
            while (rowExists && refSet.contains (rr.getString(2))) {
                rowExists = nav.next();
            }

            // if we found an acceptable row, then we build a DTO for it as
            // the first reference.  We also know that there's at least one
            // acceptable reference, so we can proceed to finding the last
            // one as well without worrying about keeping track of 'rowExists'
            // for that loop.  (At worst, we'll just find the same one as the
            // first one)

            if (rowExists)  {
                allele.set (DTOConstants.FirstReference,
                            this.makeReferenceDTO(rr) );
            }
        }
        this.sqlDM.setScrollable(false);
        nav.close();

        return allele;
    }

    /* ------------------------------------------------------------------- */

    ///////////////////////////
    // private instance methods
    ///////////////////////////

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

    /* -------------------------------------------------------------------- */

    /** gets the PrivateRefSet using the given 'sqlDM' if needed
     * @param sqlDM provides access to the database for use in generating the
     *    PrivateRefSet
     * @assumes nothing
     * @effects queries the database; adds entry to shared object cache
     * @throws nothing
     */
    private PrivateRefSet getPrivateRefSet ()
    {
        // unique key for storing/retrieving the PrivateRefSet object from
        // the shared cache of objects
        String prsKey = "org.jax.mgi.shr.datafactory.AlleleFactory.PRS";

        // cache of already-generated objects
        ExpiringObjectCache cache = ExpiringObjectCache.getSharedCache();

        // attempt to get an already-instantiated PrivateRefSet from the
        // cache of objects.  If successful, then we can just return it.

        PrivateRefSet prs = (PrivateRefSet) cache.get (prsKey);

        if (prs != null)
            {
                return prs;
            }

        // otherwise, we need to build the PrivateRefSet afresh and store it
        // in the cache.  Since the reference data changes so infrequently, a
        // four hour timeout is more than adequate.

        prs = new PrivateRefSet (this.sqlDM);
        cache.put (prsKey, prs, 4 * 60 * 60);
        this.timeStamp ("retrieved and cached new PrivateRefSet");
        return prs;
    }

    /* -------------------------------------------------------------------- */

    /** create a DTO describing the reference identified in 'rr'.
     * @param rr a row from a database query containing at least these fields:
     *    <OL>
     *    <LI> _Refs_key : integer
     *    <LI> J number : string
     *    <LI> authors part 1 : string
     *    <LI> authors part 2 : string
     *    <LI> title part 1 : string
     *    <LI> title part 2 : string
     *    <LI> citation : string
     *    </OL>
     * @return DTO a data transfer object describing this reference.  The
     *    fields (defined in DTOConstants) included are:
     *    <OL>
     *    <LI> RefsKey : Integer
     *    <LI> Jnum : String
     *    <LI> Citation : String
     *    <LI> RefsTitle : String
     *    <LI> Authors : ArrayList of Strings, each the name of an author;
     *        null if there are no authors
     *    </OL>
     * @assumes nothing
     * @effects nothing
     * @throws DBException if there are problems retrieving the fields from
     *    'rr'
     */
    private DTO makeReferenceDTO (RowReference rr)
        throws DBException
    {
        DTO ref = DTO.getDTO();
        ref.set (DTOConstants.RefsKey, rr.getInt(1));
        ref.set (DTOConstants.Jnum, rr.getString(2));
        ref.set (DTOConstants.Citation, rr.getString(7));

        // title is stored in two database fields since varchar fields are
        // limited to 256 characters each

        ref.set (DTOConstants.RefsTitle,
                 combine (rr.getString(5), rr.getString(6)) );

        // authors are stored in two database fields since varchar fields are
        // limited to 256 characters each.  A semi-colon is used to delimit
        // the lists of authors.

        String authors = combine (rr.getString(3), rr.getString(4));
        if (authors != null)
            {
                ref.set (DTOConstants.Authors,
                         (ArrayList) StringLib.split (authors, ";") );
            }
        else
            {
                ref.set (DTOConstants.Authors, null);
            }
        return ref;
    }

    /* -------------------------------------------------------------------- */

    /////////////////////////
    // private static methods
    /////////////////////////

    /** combine 's' and 't' into one String, gracefully handling nulls.
     * @param s String to be at the left of the result String
     * @param t String to be at the right of the result String
     * @return String 's' + 't', will be null if both 's' and 't' are null
     * @assumes nothing
     * @effects nothing
     * @throws nothing
     */
    private static String combine (String s, String t)
    {
        // if 's' is null, then we can just return 't' whether null or not

        if (s == null)
            {
                return t;
            }

        // we know that 's' is not null, so if 't' is null, just return 's'

        else if (t == null)
            {
                return s;
            }

        // otherwise, 's' and 't' are both non-null, so we use both

        return s + t;
    }

    /* -------------------------------------------------------------------- */

    //////////////////
    // class variables
    //////////////////

    /* --------------------------------------------------------------------
    ** class variables -- used to hold standard SQL statements, so we don't
    ** need to re-join the Strings in each thread of the servlet.  For each
    ** one, we note the pieces that need to be filled in using Sprintf.
    ** --------------------------------------------------------------------
    */

    // find the allele key associated with the given mouse symbol.
    // fill in: allele symbol (String)
    private static final String KEY_FOR_SYMBOL =
		"select _Allele_key "
		+ "from ALL_Allele "
		+ "where symbol = '%s'";

    // find the allele key associated with the given accession id.
    // fill in: accession ID (String)
    private static final String KEY_FOR_ID =
		"select _Object_key "
		+ "from ACC_Accession "
		+ "where _MGIType_key = " + DBConstants.MGIType_Allele + " "
		+    "and accID = '%s'";

    // get the basic data associated with the given allele key.
    // if this does not perform well break out all the joins to VOC_Term
    // into seperate queries
    // fill in: allele key (int)
    private static final String BASIC_ALLELE_DATA =
		"select a.symbol, a.name, p1.strain, imvt.term as inheritanceMode, "
        + " atvt.term as alleleType, asvt.term as alleleStatus, "
        + " c.cellLine as esCellLine, p2.strain as cellLineStrain, "
        + " mc.cellLine as MutatntESCellLine, mc.provider,  "
		+    " convert(varchar, db.lastdump_date, 101), "
        + " a.approval_date, mu.login as submittedBy "
		+ " from ALL_Allele a, PRB_Strain p1, VOC_Term imvt, VOC_Term atvt, "
        + " VOC_Term asvt, ALL_CellLine c, PRB_Strain p2, ALL_CellLine mc, "
        + " MGI_dbInfo db, MGI_User mu "
		+ " where _Allele_key = %d "
        + " and a._Strain_key *= p1._Strain_key "
        + " and p1._Strain_key > 0 "
        + " and a._Mode_key *= imvt._Term_key "
        + " and imvt._Vocab_key = 35 "
        + " and imvt.term ! = 'Not Applicable' "
        + " and a._Allele_Type_key = atvt._Term_key "
        + " and atvt._Vocab_key = 38 "
        + " and a._Allele_Status_key = asvt._Term_key "
        + " and asvt._Vocab_key = 37 "
        + " and a._ESCellLine_key *= c._CellLine_key "
        + " and c._CellLine_key > 0 "
        + " and c._Strain_key *= p2._Strain_key "
        + " and p2._Strain_key > 0 "
        + " and a._MutantESCellLine_key *= mc._CellLine_key "
        + " and mc._CellLine_key > 0 " 
        + " and a._CreatedBy_key *= mu._User_key ";

    //  Nomenclature notes should only be displayed in the production version
    // fill in: allele key (int)
    private static final String NOMEN_NOTE =
        "select note "
        + " from MGI_Note_Allele_View "
        + " where _Object_key = %d " 
        + " and _NoteType_key = " + DBConstants.MGINoteType_Nomenclature
        + " and _MGIType_key = " + DBConstants.MGIType_Allele
        + " order by sequenceNum";

    // get the primary MGI ID for a given allele.
    // fill in:  allle key (int)
    protected static final String PRIMARY_MGI_ID =
        "select accID"
		+ " from ACC_Accession"
		+ " where _Object_key = %d"
		+ " and _MGIType_key = " + DBConstants.MGIType_Allele
		+ " and preferred = 1"
		+ " and private = 0"
		+ " and _LogicalDB_key = " + DBConstants.LogicalDB_MGI;

    // get synonyms
    // fill in: allele key (int)
    private static final String SYNONYMS =
		  "select synonym"
		+ " from MGI_Synonym"
		+ " where _Object_key = %d"
		+ " and _MGIType_key = " + DBConstants.MGIType_Allele
		+ " order by synonym";

    // get mutations
    // fill in: allele key (int)
    private static final String MUTATIONS =
        "select vt.term as mutation "
        + " from ALL_Allele_Mutation am, VOC_Term vt "
        + " where am._Allele_key = %d "
        + " and am._Mutation_key = vt._Term_key "
        + " and vt._Vocab_key = 36";

    // get allele molecular note chunks 
    // fill in: allele key (int)
    private static final String MOLECULARNOTES =
		"select nc.note "
		+ " from MGI_Note n, MGI_NoteChunk nc "
		+ " where n._Object_key = %d "
		+ "  and n._MGIType_key = " + DBConstants.MGIType_Allele
		+ "  and n._NoteType_key =  " + DBConstants.MGINoteType_AlleleMolecular
        + "  and n._Note_key = nc._Note_key "
		+ " order by nc.sequenceNum";

    // get allele molecular references 
    // fill in: allele key (int)
    private static final String MOLECULARREFS =
		"select r.jnumID, r._Refs_key "
		+ " from MGI_RefAssocType rat, MGI_Reference_Assoc ra, BIB_View r "
		+ " where ra._Object_key = %d "
		+ "  and ra._MGIType_key = " + DBConstants.MGIType_Allele
		+ "  and ra._RefAssocType_key = rat._RefAssocType_key "
        + "  and ra._MGIType_key = rat._MGIType_key "
        + "  and rat.assocType = 'Molecular'"
        + "  and ra._Refs_key = r._Refs_key";

    // get allele general note chunks 
    // fill in: allele key (int)
    private static final String GENERALNOTES =
		"select nc._Note_key, nc.note "
		+ " from MGI_Note n, MGI_NoteChunk nc "
		+ " where n._Object_key = %d "
		+ "  and n._MGIType_key = " + DBConstants.MGIType_Allele
		+ "  and n._NoteType_key =  " + DBConstants.MGINoteType_AlleleGeneral
        + "  and n._Note_key = nc._Note_key "
		+ " order by nc._Note_key, nc.sequenceNum";

    // get gene expression count for allele
    // fill in: allele key (int)
    private static final String GENEEXPRESSION =
        "select count(distinct _Assay_key) "
        + " from GXD_AlleleGenotype gag, GXD_Expression ge "
        + " where gag._Allele_key = %d "
        + " and gag._Genotype_key = ge._Genotype_key";

    // get marker information for allele
    // fill in: allele key (int)
    private static final String MARKER =
        "select a._Marker_key, isnull(m.symbol,a.nomenSymbol), m.name, " 
        + " m.chromosome, m.cytogeneticOffset, mt.name as type "
        + " from ALL_Allele a, MRK_Marker m, MRK_Types mt "
        + " where a._Allele_key = %d "
        + " and a._Marker_key *= m._Marker_key "
        + " and m._Marker_Type_key *= mt._Marker_Type_key";


    // get MGI map position (in centimorgans)
    // fill in: marker key (int)
    private static final String MAP_POSITION =
        "select offset "
		+ " from MRK_Offset "
		+ " where _Marker_key = %d "
		+    " and source = " + DBConstants.OffsetSource_MGI;

    // get the sequence keys for the representitive sequence for a marker
    // fill in: marker key (int)
    private static final String REPRESENTIVE_SEQUENCE_KEYS =
        "SELECT smc._Sequence_key,type = vt.term"
        + " FROM SEQ_Marker_Cache smc"
        + 	", VOC_Term vt"
		+ " WHERE smc._Marker_key = %d"
		+	" AND vt._Term_key = smc._Qualifier_key"
		+	" AND vt.term != 'Not Specified'";

    // determines if marker associated with allele is in IMSR.
    // fill in: Allele key (int)
    private static final String IMSR =
        "select 1 "
        + " from ALL_Allele a, ACC_Accession ac, imsr..Accession ia "
        + " where a._Allele_key = %d "
        + " and a._Marker_key = ac._Object_key "
        + " and ac._MGIType_key = " + DBConstants.MGIType_Marker
        + " and ac.preferred = 1 "
        + " and ac._LogicalDB_key = 1 "
        + " and ac.accID = ia.accID ";

    // get info about the allele's oldest reference, defined as the one
    // with the lowest J: number for the oldest year (which is NOT a load
    // reference).  For the sake of efficiency, we avoid the use of BIB_View
    // here and compute 'citation' directly.  (This drops the query's runtime
    // from 1.19 seconds to 0.46 seconds, a 62% drop.)
    // fill in:  allele key (int)
    private static final String REFERENCE_FIRST =
        "SELECT br._Refs_key, aa.accID, br.authors, br.authors2,"
        + "  br.title, br.title2, "
        + "  citation = br.journal + ' ' + br.date + ';' + br.vol "
		+ "+ '(' + br.issue + '):' + br.pgs "
        + " FROM BIB_Refs br, ACC_Accession aa, MGI_Reference_Assoc mr "
        + " WHERE mr._Object_key = %d "
        + "  AND mr._MGIType_key = " + DBConstants.MGIType_Allele
        + "  AND mr._Refs_key = br._Refs_key "
        + "  AND br._Refs_key = aa._Object_key "
        + "  AND aa._MGIType_key = 1 "
        + "  AND aa._LogicalDB_key = 1 "
        + "  AND aa.preferred = 1 "
        + "  AND aa.prefixPart = 'J:' "
        + " ORDER BY br.year, aa.numericPart";

    // get the _Set_key values for references which are excluded from display
    // via the "All" link for references
    private static final String REFERENCE_NOT_ALL =
        "SELECT _Set_key, name "
        + " FROM MGI_Set "
        + " WHERE name LIKE '%References'";

    // get a count of references for the marker
    // fill in: allele key (int)
    private static final String REFERENCE_COUNT =
		"select count (distinct _Refs_key) "
		+ " from MGI_Reference_Assoc "
		+ " where _Object_key = %s "
        +    " AND _MGIType_key = " + DBConstants.MGIType_Allele
                + " and _RefAssocType_key != " 
                + DBConstants.MGIRefAssocType_ALLOriginal
		+    " AND _Refs_key NOT IN "
		+	" (SELECT _Object_key "
		+	" FROM MGI_SetMember "
		+	" WHERE _Set_key IN (%s) )";


    // for qtl alleles, get the TEXT-QTL experiment notes for the associated
    // marker.
    // fill in: allele key (int)
    private static final String QTL_EXPTS =
        "select me._Expt_key, me._Refs_key, ac.accID, men.note "
        + " from MLD_Expt_Marker mem, MLD_Expts me, MLD_Expt_Notes men, "
        + " ACC_Accession ac, ALL_Allele a, VOC_Term atvt "
        + " where a._Allele_key = %d "
        + " and a._Allele_Type_key = atvt._Term_key "
        + " and atvt._Vocab_key = 38 "
        + " and atvt.term = 'QTL' "
        + " and a._Marker_key = mem._Marker_key " 
        + " and mem._Expt_key = me._Expt_key "
        + " and me._Expt_key = men._Expt_key "
        + " and me.exptType = 'TEXT-QTL' "
        + " and me._Refs_key = ac._Object_key  "
        + " and ac._MGIType_key = " + DBConstants.MGIType_Reference
        + " and ac.prefixPart = 'J:'  "
        + " and ac.preferred = 1  "
        + " order by mem.sequenceNum, me._Expt_key, men.sequenceNum ";

}
