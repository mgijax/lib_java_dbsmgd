package org.jax.mgi.shr.datafactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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


public class PhenotypeFactory {
    /////////////////////
    // instance variables
    /////////////////////
    public static final String CLOSURE_CACHE_KEY = "phenotypeClosureCache";
    public static final long CLOSURE_LIFE = 86400;  // 24hrs in seconds


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
    /** constructor; instantiates and initializes a new PhenotypeFactory.
     * @param config provides parameters needed to configure a AlleleFactory,
     * @param sqlDM provides access to a database
     * @param logger provides logging capability
     * @assumes nothing
     * @effects nothing
     * @throws nothing
     */
    public PhenotypeFactory (DataFactoryCfg config, SQLDataManager sqlDM,
        Logger logger)
    {
        this.config = config;
        this.sqlDM = sqlDM;
        this.logger = logger;

        ExpiringObjectCache eoc = ExpiringObjectCache.getSharedCache();
        Object closureDag = eoc.get(PhenotypeFactory.CLOSURE_CACHE_KEY);
        if (closureDag == null) {
            try {
                MPClosureDAG cdag = new MPClosureDAG(sqlDM);
                eoc.put(CLOSURE_CACHE_KEY, cdag, CLOSURE_LIFE);
            }
            catch (DBException dbe) {
                logger.logError("Could not populate the Closure DAG : " +
                                dbe.getMessage());
            }
        }
        return;
    }

    /** find a unique key identifying the mp term specified by the given
     *    'parms'.
     * @param parms set of parameters specifying which allele we are seeking.
     *    Three keys in 'parms' are checked, in order of preference:  "key"
     *    (term key as a String), or "id" (mp accession ID.
     * @return String a unique key identifying the mp term object specified in
     *    the given set of 'parms', or null if none can be found
     * @assumes nothing
     * @effects queries the database using 'sqlDM'
     * @throws DBException if there is a problem while attempting to query the
     *    database using 'sqlDM'
     */
    public String getKey (Map parms) throws DBException
    {
        // if a 'key' is directly specified in the 'parms', then assume it is
        // a term key, and return it...

        String keyStr = StringLib.getFirst ((String[]) parms.get ("key"));
        if (keyStr != null) {
            this.timeStamp ("Used key parameter directly for term key");
            return keyStr;
        }

        // otherwise, our second choice is a mp term's accession ID...

        int key = -1;

        String accID = StringLib.getFirst ((String[]) parms.get ("id"));
        if (accID != null){
            key = this.getKeyForID (accID);
        }
        // if we found an integer key, then convert it to a String for return

        if (key != -1){
            this.timeStamp ("Retrieved allele key from database");
            return Integer.toString(key);
        }

        return null;		// no key could be found
    }

    /* ------------------------------------------------------------------- */

    /** find the term key that corresponds to the given term 'accID'.  If
     *    multiple terms have the same 'accID', then we arbitrarily
     *    choose one of the keys to return.
     * @param accID the accession ID for which we seek an associated mp term
     * @return int the mp term key corresponding to 'accID'; will be -1 if
     *    no mp term key could be found
     * @assumes That returning any mp term associated with that accession ID is
     *    good enough.  In the case where 'accID' is an object's primary MGI
     *    ID, this is valid.
     * @effects nothing
     * @throws DBException if there is a problem querying the database or
     *    processing the results.
     */
    public int getKeyForID (String accID) throws DBException
    {
        int termKey = -1;		// the term key we will return
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'

        nav = this.sqlDM.executeQuery (Sprintf.sprintf (KEY_FOR_ID, accID) );

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            termKey = rr.getInt(1).intValue();
        }
        nav.close();
        return termKey;
    }

    /* ------------------------------------------------------------------- */


    //////////////////////////////////////////////////////
    // methods to retrieve sets of sections of information
    //////////////////////////////////////////////////////

    /** retrieve the all of the phenotype data for a given allele.
     * @param parms alleleKey is the database of the key for which we want to
     *        load phenotype date.
     * @return DTO which defines all an alleles phenotype data
     * @assumes nothing
     * @effects retrieves all phenotype data by quering a database 
     * @throws DBException if there is a problem querying the database or
     *    processing the results
     * @throws MalformedURLException if any URLs are invalid
     * @throws IOException if there are problems reading from URLs via HTTP
     */
    public DTO getPhenotypes (int alleleKey)
            throws DBException, MalformedURLException, IOException
    {
        logger.logDebug("Getting Phenotype Info");

        // all phenotype data for the allele with the given 'key'
        DTO phenotypes = DTO.getDTO();

        //  This holds the set of Phenotype objects we are building keyed on 
        //  on the genotype key.
        HashMap phenoHash = getBasicPhenotypes(BASIC_GENOTYPE_DATA, alleleKey);

        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'

        //  Get phenotype headers and add them to hash
        String cmd = Sprintf.sprintf (GENOTYPE_HEADER_TERMS, alleleKey);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );

        Phenotype p = null;
        while (nav.next()) {
            rr = (RowReference)nav.getCurrent();
            p = (Phenotype)phenoHash.get(rr.getInt(1));
            p.addHeaderTerm(rr.getInt(2), rr.getString(3), rr.getString(4), 
                            rr.getInt(5));
        }
        nav.close();

        //  Get phenotype annotations and add them to hash
        cmd = Sprintf.sprintf (GENOTYPE_ANNOT_TERMS, alleleKey);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );

        while (nav.next()) {
            rr = (RowReference)nav.getCurrent();
            p = (Phenotype)phenoHash.get(rr.getInt(1));
            p.addAnnotTerm(rr.getInt(2),  rr.getString(3), rr.getString(4),
                           rr.getInt(5));
        }
        nav.close();

        
        //  Get phenotype annotation, notes and references and add them to hash
        cmd = Sprintf.sprintf (GENOTYPE_EVIDENCE_NOTES, alleleKey);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );
        
        Integer genoKey = null;
        Integer annotTermKey = null;
        Integer refKey = null;
        String  jNum = null;
        Integer evidenceKey = null;
        Integer noteKey = null;
        String  note = null;
        String  noteType = null;
        while (nav.next()) {
            rr = (RowReference)nav.getCurrent();

            // If this is our first time through, just set values
            if (evidenceKey == null) {
                genoKey = rr.getInt(1);
                annotTermKey = rr.getInt(2);
                refKey = rr.getInt(3);
                jNum = rr.getString(4);
                evidenceKey = rr.getInt(5);
                noteKey = rr.getInt(6);
                note = rr.getString(7);
                noteType = rr.getString(9);
            }
            //  If this is the second time through with the same evidence
            //  just add to the note
            else if (noteKey != null && noteKey.equals(rr.getInt(6))) {
                note += rr.getString(7);
            }
            //  We have a new evidence code, so we need to store the last one
            else {
                p = (Phenotype)phenoHash.get(genoKey);
                p.addAnnotEvidence(annotTermKey, evidenceKey, refKey, jNum, 
                                   note, noteType);

                genoKey = rr.getInt(1);
                annotTermKey = rr.getInt(2);
                refKey = rr.getInt(3);
                jNum = rr.getString(4);
                evidenceKey = rr.getInt(5);
                noteKey = rr.getInt(6);
                note = rr.getString(7);
                noteType = rr.getString(9);
            }
        
        }
        //  add the lastone.
        if (evidenceKey != null) {
            p = (Phenotype)phenoHash.get(genoKey);
            p.addAnnotEvidence(annotTermKey, evidenceKey, refKey, jNum, note,
                               noteType);
        }
        nav.close();

        //  Now I need to purge the Phenotypes that don't have headers or
        //  annotations.  We're not interested in these for display.
        for (Iterator i = phenoHash.values().iterator(); i.hasNext(); ) {
            p = (Phenotype) i.next();
            if (! p.hasHeaders() && ! p.hasAnnotations()) {
                Integer key = p.getGenotypeKey();
                i.remove();
            }
        }

        //  Organize the headers and annotations under our phenotypes
        for (Iterator i = phenoHash.values().iterator(); i.hasNext(); ) {
            p = (Phenotype) i.next();
            p.organizeAnnotations();
        }
        
        ArrayList values = new ArrayList(phenoHash.values());
        Collections.sort(values, new PhenotypicComparator(PhenotypicComparator.CUSTOM));
        
        phenotypes.set(DTOConstants.Phenotypes, values);

        return phenotypes;
    }

    /** retrieve the all of the MGI Genotype data annotated to a given mp 
     *  annotation id or it's descendents.
     * @param parms mp annotation id.
     * @return DTO which defines all genotype data for an annotation id
     * @assumes nothing
     * @effects retrieves all phenotype data by quering a database 
     * @throws DBException if there is a problem querying the database or
     *    processing the results
     * @throws MalformedURLException if any URLs are invalid
     * @throws IOException if there are problems reading from URLs via HTTP
     */
    public DTO getPhenotypeAnnotations (Map parms)
            throws DBException, MalformedURLException, IOException
    {
        
        logger.logDebug("Getting Phenotype Annotations Info");

        // all phenotype data for the MP Annotation ID submitted
        DTO phenotypes = DTO.getDTO();

        // term key as a String
        String keyStr = getKey (parms);

        // if we could not find a term key based on 'parms', then bail out
        // before bothering with anything else
        if (keyStr == null) {
            this.logInfo ("Could not find mp term");
            return phenotypes;
        }

        // mp term key as an integer
        int key = Integer.parseInt (keyStr);

        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'

        String mpid = "";
        //  Get the term for this mpid
        String cmd = Sprintf.sprintf(MP_TERM, key);
        logger.logDebug( cmd );
        nav = this.sqlDM.executeQuery ( cmd );

        if (nav.next()) {
            rr = (RowReference)nav.getCurrent();
            phenotypes.set(DTOConstants.MPTerm, rr.getString(1));
            mpid = rr.getString(2);
            phenotypes.set(DTOConstants.MPId, mpid);
            phenotypes.set (DTOConstants.DatabaseDate, rr.getString(3));

        }
        else {
            //  If a term could not be found, this is not likely a valid
            //  MP ID
            return phenotypes;
        }

        //  Create genotype temp table
        cmd = CREATE_GENOTYPESTMP;
        logger.logDebug(cmd);
        int res = this.sqlDM.executeUpdate ( cmd );


        //  Get genotypes, and genotype key for the term itself
        cmd = Sprintf.sprintf (ADD_TERMGENOS_TO_TMP, mpid);
        logger.logDebug(cmd);
        res = this.sqlDM.executeUpdate ( cmd );

        //  Get genotypes, and genotype key for the term descendents
        cmd = Sprintf.sprintf (ADD_DESCENDENTGENOS_TO_TMP, mpid);
        logger.logDebug(cmd);
        res = this.sqlDM.executeUpdate ( cmd );


        //  Get the count of genotype/term relationships.
        cmd = COUNT_GENOTYPES_IN_TMP;
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );
        if (nav.next()) {
            rr = (RowReference)nav.getCurrent();
            phenotypes.set(DTOConstants.GenotypeCount, rr.getInt(1));
        }

        //  Get all genotypes associated with genotype temp table, and
        //  their allelic compositions and background strains.
        cmd = GENOTYPE_DATA_FROM_TMP;
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery( cmd );

        //  The set of Phenotype classes, keyed by genotype key
        HashMap phenoHash = getBasicPhenotypes(GENOTYPE_DATA_FROM_TMP, 0);

        //  Get phenotype annotations and add them to hash
        cmd = GENOTYPE_ANNOT_TERMS_FROM_TMP;
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );

        Phenotype p = null;
        // debug print boolean
        boolean atleastone = false;
        while (nav.next()) {
            rr = (RowReference)nav.getCurrent();
            if (! atleastone) { 
                logger.logDebug("at least one annotation -> " + rr.getString(3) + " " + rr.getString(4));
                atleastone = true;
            }
            p = (Phenotype)phenoHash.get(rr.getInt(1));
            p.addAnnotTerm(rr.getInt(2),  rr.getString(3), rr.getString(4),
                           rr.getInt(5));
        }
        nav.close();

        //  Get phenotype annotation, notes and references and add them to hash
        cmd = GENOTYPE_EVIDENCE_FROM_TMP;
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );
        
        Integer genoKey = null;
        Integer annotTermKey = null;
        Integer refKey = null;
        String  jNum = null;
        Integer evidenceKey = null;
        while (nav.next()) {
            rr = (RowReference)nav.getCurrent();

            genoKey = rr.getInt(1);
            annotTermKey = rr.getInt(2);
            refKey = rr.getInt(3);
            jNum = rr.getString(4);
            evidenceKey = rr.getInt(5);
            p = (Phenotype)phenoHash.get(genoKey);
            p.addAnnotEvidence(annotTermKey, evidenceKey, refKey, jNum, 
                               new String(""), new String(""));        
        }
        nav.close();

        ArrayList values = new ArrayList(phenoHash.values());
        Collections.sort(values, new PhenotypicComparator(PhenotypicComparator.ALPHA));
        
        phenotypes.set(DTOConstants.Phenotypes, values);

        return phenotypes;
    }
    /* ------------------------------------------------------------------- */

    ///////////////////////////
    // private instance methods
    ///////////////////////////

    public HashMap getBasicPhenotypes ( String dbCmd, int alleleKey)
            throws DBException, MalformedURLException, IOException
    {
        logger.logDebug("Getting Basic Phenotype Info");

        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'

        // get the most basic allele data.  If we find no results, then just
        // return an empty DTO.
        String cmd = "";
        if (alleleKey > 0) {
            cmd = Sprintf.sprintf (dbCmd, alleleKey);
        }
        else {
            cmd = dbCmd;
        }
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );

        //  This holds the set of Phenotype objects we are building keyed on 
        //  on the genotype key.
        HashMap phenoHash = new HashMap();
        
        Phenotype p = null;
        Integer genoKey = null;
        Integer genoOrder = null;
        String allelicComp = null;
        String background = null;
        while (nav.next()) {
            rr = (RowReference)nav.getCurrent();
           
            //  If this is our first time through, just set values.
            if (genoKey == null) {
                genoKey = rr.getInt(1);
                genoOrder = rr.getInt(2);
                allelicComp = rr.getString(3);
                background = rr.getString(5);

            }
            //  If this is the second time through with the same genotype
            //  just add to the allelic composition
            else if (genoKey.equals(rr.getInt(1))) {
                allelicComp += rr.getString(3);
            }
            //  We have a new genotype, so we need to store the last one
            else {
                p = new Phenotype(genoKey, genoOrder, 
                                  allelicComp, background);
                phenoHash.put(genoKey, p);
                genoKey = rr.getInt(1);
                genoOrder = rr.getInt(2);
                allelicComp = rr.getString(3);
                background = rr.getString(5);
                
            }
        }
        if (genoKey != null) {
            //  Add the last genotype
            p = new Phenotype(genoKey, genoOrder, allelicComp, background);
            phenoHash.put(genoKey, p);
        }

        return phenoHash;
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

    // find the allele key associated with the given accession id.
    // fill in: accession ID (String)
    private static final String KEY_FOR_ID =
		"select ac._Object_key "
		+ "from ACC_Accession ac, VOC_Term vt "
		+ "where _MGIType_key = " + DBConstants.MGIType_VocTerm + " "
		+    "and accID = '%s'"
        + " and preferred = 1 "
        + " and ac._Object_key = vt._Term_key "
        + " and vt._Vocab_key = 5";

    // find the genotypes, their allelic compositions and background strains.
    // fill in: allele key (int)
    private static final String BASIC_GENOTYPE_DATA =
		"select gag._Genotype_key, gag.sequenceNum, "
        + "nc.note as alleleCombination, nc.sequenceNum, "
        + "s.strain as background "
        + "from GXD_AlleleGenotype gag, MGI_Note n, MGI_NoteType nt, "
        + "MGI_NoteChunk nc, GXD_Genotype g, PRB_Strain s "
		+ "where gag._Allele_key = %d "
        + "and gag._Genotype_key = n._Object_key "
        + "and n._MGIType_key =  " + DBConstants.MGIType_Genotype
        + " and nt.noteType = 'Combination Type 2' "
        + "and nt._NoteType_key = n._NoteType_key "
        + "and n._Note_key = nc._Note_key "
        + "and gag._Genotype_key = g._Genotype_key "
        + "and g._Strain_key = s._Strain_key "
        + "order by gag.sequenceNum, nc.sequenceNum";

    // find the mp header terms for a given allele key.
    // fill in: allele key (int)
    private static final String GENOTYPE_HEADER_TERMS =
		"select gag._Genotype_key, vt._Term_key, ac.accID, "
        + "s.synonym as header, vah.sequenceNum as headerOrder "
        + "from GXD_AlleleGenotype gag, VOC_AnnotHeader vah, VOC_Term vt, "
        + "MGI_Synonym s, MGI_SynonymType st, ACC_Accession ac "
        + "where gag._Allele_key = %d "
        + "and gag._Genotype_key = vah._Object_key "
        + "and vah._AnnotType_key = " + DBConstants.VOCAnnotType_MP
        + " and vah._Term_key = vt._Term_key "
        + " and vt._Term_key = s._Object_key "
        + " and s._MGIType_key = " + DBConstants.MGIType_VocTerm
        + " and s._SynonymType_key = st._SynonymType_key "
        + " and st.synonymType = 'Synonym Type 1' "
        + "and vt._Term_key = ac._Object_key "
        + "and ac._MGIType_key = " + DBConstants.MGIType_VocTerm
        + " and ac.prefixPart = 'MP:' "
        + "and ac.preferred = 1 "
        + "order by gag.sequenceNum, gag._Genotype_key, vah.sequenceNum";

    // find the mp annotation terms for a given allele key.
    // fill in: allele key (int)
    private static final String GENOTYPE_ANNOT_TERMS =
		"select  gag._Genotype_key, vt._Term_key, ac.accID, vt.term as annot, "
        + "vt.sequenceNum as annotOrder "
        + "from GXD_AlleleGenotype gag, VOC_Annot va, VOC_Term vt, "
        + "ACC_Accession ac "
        + "where gag._Allele_key = %d "
        + "and gag._Genotype_key = va._Object_key "
        + "and va._AnnotType_key = " + DBConstants.VOCAnnotType_MP
        + " and va._Term_key = vt._Term_key "
        + "and vt._Term_key = ac._Object_key "
        + "and ac._MGIType_key = " + DBConstants.MGIType_VocTerm
        + " and ac.prefixPart = 'MP:' "
        + "and ac.preferred = 1 "
        + "order by gag.sequenceNum, gag._Genotype_key";

    // Get all of the evidence and Notes for the given allele key.
    // fill in: allele key (int)
    private static final String GENOTYPE_EVIDENCE_NOTES =
        "select gag._Genotype_key, vt._Term_key as annot, "
        + " ve._Refs_key, ac.accID as jnum, ve._AnnotEvidence_key, "
        + " nc._Note_key, nc.note, "
        + " nc.sequenceNum, nt.noteType "
        + " from GXD_AlleleGenotype gag, VOC_Annot va, VOC_Evidence ve,  "
        + " VOC_Term vt, MGI_Note n, MGI_NoteChunk nc, ACC_Accession ac, "
        + " MGI_NoteType nt "
        + " where gag._Allele_key = %d "
        + " and gag._Genotype_key = va._Object_key "
        + " and va._AnnotType_key = " + DBConstants.VOCAnnotType_MP
        + " and va._Term_key = vt._Term_key "
        + " and va._Annot_key = ve._Annot_key "
        + " and ve._Refs_key = ac._Object_key "
        + " and ac._MGIType_key = " + DBConstants.MGIType_Reference
        + " and ac.prefixPart = 'J:' "
        + " and ac.preferred = 1 "
        + " and ve._AnnotEvidence_key *= n._Object_key "
        + " and n._MGIType_key = 25 "
        + " and n._NoteType_key *= nt._NoteType_key "
        + " and n._Note_key *= nc._Note_key "
        + " order by gag._Genotype_key, nt.noteType, ve._AnnotEvidence_key, "
        + " nc._Note_key, "
        + " nc.sequenceNum";

    //  Get the MP Term for a given MP Id
    //  fill in: mp id (String)
    public static final String MP_TERM =
        "select vt.term, ac.accID, "
		+    " convert(varchar, db.lastdump_date, 101) "
        + " from VOC_Term vt, ACC_Accession ac, MGI_dbInfo db "
        + " where ac._Object_key = %d " 
        + " and ac._Object_key = vt._Term_key"
        + " and vt._Vocab_key = 5 "
        + " and ac._MGIType_key = " + DBConstants.MGIType_VocTerm
        + " and ac.prefixPart = 'MP:' "
        + " and ac.preferred = 1";

    //  Create a temp table for holding the genotype/term relationships
    //  for a given mp term and it's descendents.
    //  It was necessary to add an orderVal column, as sometimes a genotyp
    //  will have to sequenceNum values.  I want the lowest for sorting.
    //  fill in: nothing
    private static final String CREATE_GENOTYPESTMP =
        "create table #genotypes "
        + "(_Genotype_key int, "
        + " _Term_key int,  "
        + " MPID varchar(50), "
        + " orderVal int)";

    //  Add all genotypes to temptable for the given term
    //  It was necessary to add an orderVal column, as sometimes a genotyp
    //  will have to sequenceNum values.  I want the lowest for sorting.
    //  fill in:  mp term id (String)
    private static final String ADD_TERMGENOS_TO_TMP =
        "insert into #genotypes"
        + " select distinct gag._Genotype_key, vt._Term_key, ac.accID, "
        + " min(gag.sequenceNum) as orderVal "
        + " from ACC_Accession ac, VOC_Term vt, VOC_Annot va, "
        + " GXD_AlleleGenotype gag"
        + " where ac.accID = '%s'"
        + " and ac._Object_key = vt._Term_key"
        + " and ac._MGIType_key = " + DBConstants.MGIType_VocTerm
        + " and ac.preferred = 1"
        + " and vt._Term_key = va._Term_key"
        + " and va._AnnotType_key = " + DBConstants.VOCAnnotType_MP
        + " and va._Object_key = gag._Genotype_key "
        + " group by gag._Genotype_key, vt._Term_key, ac.accID";

    // Add all genotypes to temptable for descendents of term
    //  It was necessary to add an orderVal column, as sometimes a genotyp
    //  will have to sequenceNum values.  I want the lowest for sorting.
    // fill in:  mp term id (String)
    private static final String ADD_DESCENDENTGENOS_TO_TMP =
        "insert into #genotypes"
        + " select distinct gag._Genotype_key, vt2._Term_key, ac2.accID, "
        + " min(gag.sequenceNum) as orderVal "
        + " from ACC_Accession ac, VOC_Term vt, DAG_Node dn1, DAG_Closure dc," 
        + " DAG_Node dn2, VOC_Term vt2, VOC_Annot va, GXD_AlleleGenotype gag, "
        + " ACC_Accession ac2 "
        + " where ac.accID = '%s'"
        + " and ac._Object_key = vt._Term_key"
        + " and ac._MGIType_key = " + DBConstants.MGIType_VocTerm
        + " and ac.preferred = 1"
        + " and vt._Vocab_key = 5"
        + " and vt._Term_key = dn1._Object_key"
        + " and dn1._Node_key = dc._Ancestor_key"
        + " and dc._Descendent_key = dn2._Node_key"
        + " and dn2._Object_key = vt2._Term_key"
        + " and vt2._Vocab_key = 5" 
        + " and vt2._Term_key = va._Term_key"
        + " and va._AnnotType_key = " + DBConstants.VOCAnnotType_MP
        + " and va._Object_key = gag._Genotype_key "
        + " and vt2._Term_key = ac2._Object_key "
        + " and ac2._MGIType_key = " + DBConstants.MGIType_VocTerm
        + " and ac2.preferred = 1 "
        + " and not exists (select 1 from #genotypes" 
        + "                 where _Genotype_key = gag._Genotype_key"
        + "                 and _Term_key = vt2._Term_key) "
        + " group by gag._Genotype_key, vt2._Term_key, ac2.accID";

    //  returns the number of records in the genotype tmp table.  This 
    //  represents the unique set of genotype/term associations.
    //  fill in:  nothing
    private static final String COUNT_GENOTYPES_IN_TMP =
        "select count(1) from #genotypes";

    // find the allelic compositions and background strains for the
    // genotype keys in the #genotypes tmp table.
    //  It was necessary to add an orderVal column, as sometimes a genotyp
    //  will have to sequenceNum values.  I want the lowest for sorting.
    // fill in: nothing
    private static final String GENOTYPE_DATA_FROM_TMP =
		"select distinct gt._Genotype_key, gt.orderVal, "
        + "nc.note as alleleCombination, nc.sequenceNum, "
        + "s.strain as background "
        + "from #genotypes gt, GXD_AlleleGenotype gag, "
        + "MGI_Note n, "
        + "MGI_NoteType nt, MGI_NoteChunk nc, GXD_Genotype g, PRB_Strain s "
		+ "where gt._Genotype_key = gag._Genotype_key "
        + "and gag._Genotype_key = n._Object_key "
        + "and n._MGIType_key =  " + DBConstants.MGIType_Genotype
        + " and nt.noteType = 'Combination Type 3' "
        + "and nt._NoteType_key = n._NoteType_key "
        + "and n._Note_key = nc._Note_key "
        + "and gag._Genotype_key = g._Genotype_key "
        + "and g._Strain_key = s._Strain_key "
        + "order by gt.orderVal, nc.sequenceNum";


    // find the mp annotation terms for the genotype/term key combinations
    // in the #genotypes temp table.
    // fill in: none
    private static final String GENOTYPE_ANNOT_TERMS_FROM_TMP =
		"select  distinct gt._Genotype_key, vt._Term_key, gt.mpid, "
        + "vt.term as annot, vt.sequenceNum as annotOrder "
        + "from #genotypes gt, VOC_Annot va, VOC_Term vt "
        + "where gt._Genotype_key = va._Object_key "
        + "and va._AnnotType_key = " + DBConstants.VOCAnnotType_MP
        + " and va._Term_key = gt._Term_key "
        + " and va._Term_key = vt._Term_key ";

    // Get all of the evidence for the genotype/annot terms in the
    // #genotypes tmp table.
    // fill in: nothing
    private static final String GENOTYPE_EVIDENCE_FROM_TMP =
        "select distinct gag._Genotype_key, gt._Term_key as annot, "
        + " ve._Refs_key, ac.accID as jnum, ve._AnnotEvidence_key "
        + " from #genotypes gt, GXD_AlleleGenotype gag, VOC_Annot va, "
        + " VOC_Evidence ve, ACC_Accession ac "
        + " where gt._Genotype_key = gag._Genotype_key "
        + " and gag._Genotype_key = va._Object_key "
        + " and va._AnnotType_key = " + DBConstants.VOCAnnotType_MP
        + " and va._Term_key = gt._Term_key "
        + " and va._Annot_key = ve._Annot_key "
        + " and ve._Refs_key = ac._Object_key "
        + " and ac._MGIType_key = " + DBConstants.MGIType_Reference
        + " and ac.prefixPart = 'J:' "
        + " and ac.preferred = 1 ";


}
