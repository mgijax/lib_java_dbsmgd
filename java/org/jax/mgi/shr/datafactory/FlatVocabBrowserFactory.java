package org.jax.mgi.shr.datafactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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


public class FlatVocabBrowserFactory {
    /////////////////////
    // instance variables
    /////////////////////

    // provides access to the database
    private SQLDataManager sqlDM = null;

    // provides logging capability for info, error, debug logs
    private Logger logger = null;

    // provides parameters needed to configure a FlatVocabBrowserFactory
    private DataFactoryCfg config = null;

    // used to log profiling information (timings for sections of code)
    private TimeStamper timer = null;

    ////////////////////////////////////////////////////
    // configurable attributes, specific to Flat Vocabs. 
    ////////////////////////////////////////////////////

    //  The ordered list of valid subsets to use in the vocabulary
    private List subsets = null;

    //  A lookup for the valid subsets
    private HashSet subsetHS = null;

    //  The script run to execute the text search
    private String searchScript = null;

    //  parameters that should come from config that are being passed in
    //  DTO.
    private String pageTitle = "Human Disease";
    private String titleSupportText = "The current vocabulary contains human disease, syndrome, and condition terms from Online Mendelian Inheritance in Man <a href=\"http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=OMIM\" target=\"new\">(OMIM database)</a>.";
    private String accessionType = "OMIM";
    private String subsetUrl = "WIFetch?page=omimBrowser&subset=";
    private String subsetHead = "Human Diseases/Syndromes";
    private String subsetNote = "To see all annotations for a disease, click the disease name.";
    private String idHeader = "OMIM ID";
    private String termHeader = "Human Disease";
    private String annotCountText = "mouse model";
    private String idUrl = "http://www.ncbi.nlm.nih.gov/entrez/dispomim.cgi?id=";
    private String termUrl = "WIFetch?page=humanDisease&key=";
    

    ///////////////
    // Constructors
    ///////////////
    /** constructor; instantiates and initializes new FlatVocabBrowserFactory.
     * @param config provides parameters needed to configure factory,
     * @param sqlDM provides access to a database
     * @param logger provides logging capability
     * @assumes nothing
     * @effects nothing
     * @throws nothing
     */
    public FlatVocabBrowserFactory (DataFactoryCfg config, 
                                    SQLDataManager sqlDM,
                                    Logger logger)
    {
        this.config = config;
        this.sqlDM = sqlDM;
        this.logger = logger;

        //  These attributes represent items that should be configurable.
        //  For now, we are assuming this is an OMIM vocabulary browser.
        String[] l = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N",
                      "O","P","Q","R","S","T","U","V","W","X","Y","Z","0-9"};
        subsets = Arrays.asList(l);
        subsetHS = new HashSet(subsets);

        searchScript = this.config.get ("TEXT_SEARCH_WRAPPER");

    }

    /** find a unique key identifying the vocab subset specified by the given
     *    'parms'.
     * @param parms set of parameters specifying which subset we are seeking.
     *    Only one key in 'parms' is checked as we only cache based on
     *    subsets.  The key is:  "subset"
     * @return String a unique key identifying the subset specified in
     *    the given set of 'parms', or null if none can be found
     * @assumes nothing
     * @effects nothing
     * @throws DBException There is no db interaction so exception not thrown
     */
    public String getKey (Map parms) throws DBException
    {
        // if a 'key' is directly specified in the 'parms', then assume it is
        // a term key, and return it...

        String keyStr = StringLib.getFirst ((String[]) parms.get ("subset"));
        if (keyStr != null) {
            if ( subsetHS.contains(keyStr) ) {
                this.timeStamp ("Used subset parameter directly for key");
                return keyStr;
            }
            else {
                logger.logError("Invalid subset entered: '" + keyStr + 
                                "'  cannot use as key!");
            }
        }

        return null;		// no key could be found
    }


    /* ------------------------------------------------------------------- */


    //////////////////////////////////////////////////////
    // methods to retrieve sets of sections of information
    //////////////////////////////////////////////////////

    /** retrieve the subset of a flat vocabulary based on parameters.
     *  current incarnation assumes OMIM, needs to be abstracted through config
     *  parameters to allow other vocabularies.
     * @param parms This will contain parameters to get either a
     *     simple subset or a subset based on a text search.
     * @return DTO which defines all vocab data for the parameters
     * @assumes configuration defines what vocabulary we are searching
     * @effects retrieves data for a vocabulary using text searching and
     *    database queries. 
     * @throws DBException if there is a problem querying the database or
     *    processing the results
     * @throws MalformedURLException if any URLs are invalid
     * @throws IOException if there are problems reading from URLs via HTTP
     */
    public DTO getFlatVocab (Map parms)
        throws DBException, MalformedURLException, IOException, MGIException
    {
        
        logger.logDebug("Getting Flat Vocabulary Info");

        // all vocabulary data for either the subset or search terms submitted
        DTO vocabulary = DTO.getDTO();

        //  All of the following values are passed to the Caller for
        //  display purposes and should come from a config of some sort
        //  for a generic vocab browser.  Current assumption = OMIM.
        //  Also values should be added to DTOConstants for all of these.
        vocabulary.set("pageTitle", this.pageTitle);
        vocabulary.set("subsets", this.subsets);
        vocabulary.set("titleSupportText",this.titleSupportText);
        vocabulary.set("accessionType",this.accessionType);
        vocabulary.set("subsetUrl",this.subsetUrl);
        vocabulary.set("subsetHead",this.subsetHead);
        vocabulary.set("subsetNote",this.subsetNote);
        vocabulary.set("idHeader",this.idHeader);
        vocabulary.set("termHeader",this.termHeader);
        vocabulary.set("annotCountText",this.annotCountText);
        vocabulary.set("idUrl",this.idUrl);
        vocabulary.set("termUrl",this.termUrl);

        // data for a particular section, to merch with 'vocabulary'
        DTO section = null;

        // subset "key"  as a String
        String keyStr = getKey (parms);
        String searchStr = null;

        //  If there is a key value, then do the query to get
        //  a subset of data (no text search).
        if (keyStr != null) {
            // Need to add to DTOConstants
            //vocabulary.set(DTOConstants.VocabSubset, keyStr);
            vocabulary.set("vocabSubset", keyStr);
           
            section = getSubset(keyStr);
            vocabulary.merge(section);
            DTO.putDTO(section);
            this.timeStamp ("Retrieved subset of vocabulary.");
        }

        // If we don't have a subset we're searching, then we must have a
        // search string.
        else  {
            logger.logDebug("fetching search string");
            searchStr = StringLib.getFirst ((String[]) parms.get ("query"));
            //searchStr = (String) parms.get("query");
            logger.logDebug("Have search string " + searchStr);
        }
        //  If there is a search string, go ahead and do textsearch,
        //  and associated query to bring back results.

        if (searchStr != null) {
            // Need to add to DTOConstants
            //vocabulary.set(DTOConstants.QueryResults, searchStr);
            vocabulary.set("queryResults", searchStr);
            TextSearchHandler tsh = new TextSearchHandler (this.searchScript);

            //try {
            HashMap results = 
                tsh.search(TextSearchHandler.OMIM, searchStr);
            section = getSearchResults((String[])results.get("from"),
                                       (String[])results.get("where"));
            if (section != null) {
                vocabulary.merge(section);
                DTO.putDTO(section);
                this.timeStamp ("Retrieved vocab based on search terms");
            }
            //}
            ///catch (Exception e) {
            //    throw e;
            //}
                
        }


        return vocabulary;
    }
    /* ------------------------------------------------------------------- */

    ///////////////////////////
    // private instance methods
    ///////////////////////////

    //  This method currently assumes OMIM.  It should be abstracted either
    //  through configurable sql, or a class which can be extended to support
    //  other vocabularies.
    private DTO getSubset ( String subset )
            throws DBException, MalformedURLException, IOException
    {
        logger.logDebug("Getting Subset " + subset);

        //  The object to return.  Represents the subsection of vocabulary.
        DTO vocabulary = null;

        //  The list of terms, this will be a list of HashMaps, each HashMap
        //  containing an accession ID
        ArrayList terms = new ArrayList();

        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'

        //  If the subset is only one character long, it is the first char
        //  of the terms in the subset.
        ArrayList subsetsToFetch = new ArrayList();
        if (subset.length() == 1) {
            subsetsToFetch.add(subset);
        }
        //  Currently assumes that if a subset is not a single character that
        //  it is the range 0-9.
        else if(subset.equals("0-9")){
            subsetsToFetch.add("0");
            subsetsToFetch.add("1");
            subsetsToFetch.add("2");
            subsetsToFetch.add("3");
            subsetsToFetch.add("4");
            subsetsToFetch.add("5");
            subsetsToFetch.add("6");
            subsetsToFetch.add("7");
            subsetsToFetch.add("8");
            subsetsToFetch.add("9");
        }
        
        //  For each subset we need fetch the terms and add them to our
        //  array list.
        for (int i = 0; i < subsetsToFetch.size(); i++) {
            String ss = (String)subsetsToFetch.get(i);
            
            //  Get the query that will get this subset
            String cmd = Sprintf.sprintf(SUBSET, ss);
            logger.logDebug(cmd);
            nav = this.sqlDM.executeQuery( cmd );
           
            while (nav.next()) {
                rr = (RowReference)nav.getCurrent();
            
                terms.add(processTerm(rr));
            }
        }

        nav.close();

        //  If there were any terms found for this subset, add to the DTO
        if (terms.size() > 0) {
            vocabulary = DTO.getDTO();
            //  Need to add to DTOConstants
            //vocabulary.set(DTOConstants.TermList, terms);
            vocabulary.set("termList", terms);
        }

        return vocabulary;
    }


    //  This method currently assumes OMIM.  It should be abstracted either
    //  through configurable sql, or a class which can be extended to support
    //  other vocabularies.
    private DTO getSearchResults ( String[] fromClause, String[] whereClause)
            throws DBException, MalformedURLException, IOException
    {
        logger.logDebug("Building Results Info");

        //  The object to return.  Represents the subsection of vocabulary.
        DTO vocabulary = null;

        //  The list of terms, this will be a list of HashMaps, each HashMap
        //  containing an accession ID
        ArrayList terms = new ArrayList();

        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'

        String fromStr = "";
        String whereStr = "";
        
        // Turn the arrays of from and where clauses into simple strings
        for (int i = 0; i < fromClause.length; i++) {
            fromStr = fromStr + ", " + fromClause[i];
        }
        boolean firstWhere = true;
        for (int i = 0; i < whereClause.length; i++) {
            if (firstWhere) {
                firstWhere = false;
            }
            else {
                whereStr = whereStr + " and ";
            }
            whereStr = whereStr + whereClause[i];
        } 

        String cmd = Sprintf.sprintf (OMIM_TERMS, fromStr, whereStr);
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery ( cmd );
        while (nav.next()) {
            rr = (RowReference)nav.getCurrent();
                
            terms.add(processTerm(rr));
        }
          
        nav.close();

        //  If there were any terms found for this subset, add to the DTO
        if (terms.size() > 0) {
            vocabulary = DTO.getDTO();
            //  Need to add to DTOConstants
            //vocabulary.set(DTOConstants.TermList, terms);
            vocabulary.set("termList", terms);
        }

        return vocabulary;
    }

    /* -------------------------------------------------------------------- */

    private HashMap processTerm ( RowReference rr ) 
        throws DBException
    {
        Integer termKey = rr.getInt(1);
        String term = rr.getString(2);
        String omimId = rr.getString(3);

        HashMap hm = new HashMap();
        hm.put("term", term);
        hm.put("id", omimId);
        hm.put("key",termKey);

        ResultsNavigator nav2 = null;
        RowReference rr2 = null;

        //  Now get the count of genotype annotations for this term.
        //  This should probably be done as a temp table in the long 
        //  haul.
        String cmd = Sprintf.sprintf(ANNOTATIONS, termKey.intValue());
        //logger.logDebug(cmd);
        nav2 = this.sqlDM.executeQuery( cmd );
        if (nav2.next()) {
            rr2 = (RowReference)nav2.getCurrent();
            hm.put("models", rr2.getInt(1));
        }
        nav2.close();
        return hm;
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

     // find the all OMIM Terms that begin with given subset character.
    // fill in: beginning character of term (String)
    private static final String SUBSET =
		"select vt._Term_key, vt.term, ac.accID  "
        + "from VOC_Term vt, ACC_Accession ac "
		+ "where vt.term like '%s%' "
        + "and vt._Term_key = ac._Object_key "
        + "and ac._MGIType_key =  " + DBConstants.MGIType_VocTerm
        + " and ac._LogicalDB_key = " + DBConstants.LogicalDB_OMIM
        + " and ac.preferred = 1 "
        + "order by vt.term";

    // find the OMIM Terms that correspond with associated from and where
    // clause.
    // fill in: portion of from clause (String) 
    //          portion of where clause (String)
    private static final String OMIM_TERMS =
		"select vt._Term_key, vt.term, ac.accID "
        + "from VOC_Term vt, ACC_Accession ac %s "
        + "where %s "
        + "and vt._Term_key = ac._Object_key "
        + "and ac._MGIType_key =  " + DBConstants.MGIType_VocTerm
        + " and ac._LogicalDB_key = " + DBConstants.LogicalDB_OMIM
        + " and ac.preferred = 1 "
        + "order by term";

    //  find the count of genotypes annotated with this omim term
    //  fill in: term key (int)
    private static final String ANNOTATIONS = 
        "select count(distinct va._Object_key) "
        + "from VOC_Annot va "
        + "where va._Term_key = %d "
        + "and _AnnotType_key = " + DBConstants.VOCAnnotType_OMIM;
}
