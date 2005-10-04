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
import org.jax.mgi.shr.dbutils.BatchProcessor;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.BatchException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.exception.MGIException;


public class PIRSFFactory {
    /////////////////////
    // instance variables
    /////////////////////

    // provides access to the database
    private SQLDataManager sqlDM = null;

    // provides logging capability for info, error, debug logs
    private Logger logger = null;

    // provides parameters needed to configure a PIRSFFactory
    private DataFactoryCfg config = null;

    // used to log profiling information (timings for sections of code)
    private TimeStamper timer = null;


    ///////////////
    // Constructors
    ///////////////
    /** constructor; instantiates and initializes new PIRSFFactory.
     * @param config provides parameters needed to configure factory,
     * @param sqlDM provides access to a database
     * @param logger provides logging capability
     * @assumes nothing
     * @effects nothing
     * @throws nothing
     */
    public PIRSFFactory (DataFactoryCfg config, 
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

    /** retrieve the PIRSF Detail information.
     * @param parms This will contain parameters to get detail information
     *    for pirsf term data.
     * @return DTO which defines all detail about given pirsf term
     * @assumes nothing
     * @effects nothing 
     * @throws DBException if there is a problem querying the database or
     *    processing the results
     * @throws MalformedURLException if any URLs are invalid
     * @throws IOException if there are problems reading from URLs via HTTP
     */
    public DTO getPIRSFInfo (Map parms)
        throws DBException, MalformedURLException, IOException, MGIException
    {
        
        logger.logDebug("Getting PIRSF Info");

        // all pirsf info for term key submitted
        DTO pirsf = DTO.getDTO();

        // data for a particular section, to merge with 'pirsf'
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
        pirsf.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved basic term info");
                
        section = 
            this.getMemberDetails(key);
        pirsf.merge (section);
        DTO.putDTO (section);
        this.timeStamp ("Retrieved Family Member details");

                    
        return pirsf;
    }
    /* ------------------------------------------------------------------- */

    /////////////////////////////////////////////////////////
    // methods to retrieve individual sections of information
    /////////////////////////////////////////////////////////

    /** retrieve basic information about the pirsf term with the given 'key'.
     * @param key the term key of the pirsf term whose data we seek
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
        logger.logDebug("Getting PIRSF Basic Info");
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        DTO pirsf = DTO.getDTO();	// start with an empty DTO

        // get the most basic pirsf data.  If we find no results, then just
        // return an empty DTO.
        String cmd = Sprintf.sprintf (BASIC_TERM_DATA, key);
        logger.logDebug(cmd);
        nav = this.sqlDM.
            executeQuery ( cmd );

        if (!nav.next()) {
            nav.close();
            nav = null;
            rr = null;
            return pirsf;
        }

        // otherwise collect the basic data fields

        rr = (RowReference) nav.getCurrent();

        pirsf.set (DTOConstants.TermKey, rr.getInt(1));
        pirsf.set (DTOConstants.Term, rr.getString(2));
        pirsf.set (DTOConstants.PrimaryAccID, rr.getString(3));
        pirsf.set (DTOConstants.DatabaseDate, rr.getString(4));


        nav.close();
        nav = null;
        rr = null;
        
        return pirsf;
    }


    /* -------------------------------------------------------------------- */

    /** retrieve details about the members of the pir super family 
     * @param key the term key of the pirsf term whose data we seek
     * @return DTO contains information retrieved from the database.  
     *    If no data for the specified term is found, then returns
     *    an empty DTO.
     * @assumes nothing
     * @effects queries the database
     * @throws DBException if there are problems querying the database or
     *    stepping through the results.
     * @throws BatchException if there is a problem running the batch that
     *    create the temp tables.
     * @notes The following constants from DTOConstants are included as
     *    fieldnames in the returned DTO:
     *    <OL>
     *    <LI> members - the List of members of the superfamily, where
     *     each member is a SFMember object
     *    </OL>
     */
    public DTO getMemberDetails (int key) throws DBException, BatchException
    {
        logger.logDebug("Getting Super family member details");
        ResultsNavigator nav = null;	// set of query results
        RowReference rr = null;		// one row in 'nav'
        DTO pirsf = DTO.getDTO();	// start with an empty DTO
        ArrayList members = new ArrayList();  // the list of members

        // Get a batch processor to submit the queries to generate
        // temp tables.
        BatchProcessor bp = sqlDM.getBatchProcessor();

        // Populate a temp table with the mouse genes annotated to
        // the PIRSF term.
        String cmd = Sprintf.sprintf (MOUSE_MEMBERS, key);
        logger.logDebug(cmd);
        bp.addBatch(cmd);

        // Populate another temp table with the Human Genes homologous
        // to the mouse genes.
        cmd = HUMAN_GENES;
        logger.logDebug(cmd);
        bp.addBatch(cmd);

        // Populate one more temp table with the Rat Genes homologous to
        // the mouse genes
        cmd = RAT_GENES;
        logger.logDebug(cmd);
        bp.addBatch(cmd);

        int[] res = bp.executeBatch();

        // Now pull back the set of mouse genes with their homologous human
        // and rat genes (if they exist).
        cmd = GET_MEMBER_RESULTS;
        logger.logDebug(cmd);
        nav = this.sqlDM.executeQuery( cmd );

        SequenceFactory seqFac = new SequenceFactory( config, sqlDM, logger);

        //  Build SFMember objects and add them to the list
        while (nav.next()) {

            rr = (RowReference) nav.getCurrent();
            
            //  Construct a new super family member
            DTO sfm = DTO.getDTO();
            //  Add mouse genes annotated to pirsf term
            sfm.put("markerKey",rr.getInt(1));
            sfm.put("mouseSymbol", rr.getString(2));
            sfm.put("mouseChromosome", rr.getString(3));
            Integer seqKey = rr.getInt(4);
            sfm.put("mouseSeqKey", seqKey);
            if (seqKey != null) {
                DTO seqDTO = seqFac.getBasicInfo(seqKey.intValue());
                sfm.put("sequence", seqDTO);
            }
            sfm.put("mouseSeqId",rr.getString(5));
            sfm.put("mouseDbName", rr.getString(6));

            //  Add human and rat homology info if they exist
            sfm.put("humanSymbol", rr.getString(7));
            sfm.put("humanChromosome", rr.getString(8));
            seqKey = rr.getInt(9);
            sfm.put("humanSeqKey", seqKey);
            sfm.put("humanSeqId",rr.getString(10));
            sfm.put("humanDbName", rr.getString(11));
            sfm.put("ratSymbol", rr.getString(12));
            sfm.put("ratChromosome", rr.getString(13));
            seqKey = rr.getInt(14);
            sfm.put("ratSeqKey", seqKey);
            sfm.put("ratSeqId",rr.getString(15));
            sfm.put("ratDbName", rr.getString(16));

            //  Add member to list.
            members.add(sfm);
        }
        bp.close();
        nav.close();
        bp = null;
        nav = null;
        rr = null;
        
        // add list to DTO
        if (members.size() > 0) {
            pirsf.set ("members", members);
        }
        
        return pirsf;
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
		"select vt._Term_key, vt.term, ac.accID,  \n"
		+    " convert(varchar, db.lastdump_date, 101) \n"
        + "from VOC_Term vt, ACC_Accession ac, MGI_dbInfo db "
		+ "where vt._Term_key = %d \n"
        + "and vt._Term_key = ac._Object_key \n"
        + "and ac._MGIType_key =  " + DBConstants.MGIType_VocTerm
        + " and ac._LogicalDB_key = " + DBConstants.LogicalDB_PIRSF
        + " and ac.preferred = 1 ";

    // get mouse genes annotated to the PIRSF term.  Puts them in temp table.
    // fill in: term key (int)
    private static final String MOUSE_MEMBERS =
		  "select distinct m._Marker_key, " 
        + " m.symbol, \n"
        + "  m.chromosome,  \n"
        + "  smc1._Sequence_key, \n"
        + "  acmouse.accID, \n"
        + "  ldb1.name \n"
        + " into #mouseGenes \n"
        + " from VOC_Annot va, \n"
        + "  MRK_Marker m, \n"
        + "  SEQ_Marker_Cache smc1,  \n"
        + "  ACC_Accession acmouse, \n"
        + "  ACC_LogicalDB ldb1  \n"
        + " where va._Term_key = %d \n"
        + "  and va._Object_key = m._Marker_key  \n"
        + "  and va._AnnotType_key =  " + DBConstants.VOCAnnotType_PIRSF
        + "  and m._Marker_key = smc1._Marker_key  \n"
        + "  and m._Organism_key = " + DBConstants.Species_Mouse
        + "  and smc1._Qualifier_key = " + DBConstants.SEQMarker_Polypeptide
        + "  and smc1._Sequence_key = acmouse._Object_key  \n"
        + "  and smc1._LogicalDB_key = acmouse._LogicalDB_key \n"
        + "  and acmouse._MGIType_key =  " + DBConstants.MGIType_Sequence
        + "  and acmouse.preferred = 1 \n"
        + "  and acmouse._LogicalDB_key = ldb1._LogicalDB_key";

    // get human genes homologous to the marker key in mouseGene temp table,
    // that was annotated to the PIRSF term.  Puts them in temp table.
    // fill in: nothing
    // assumes: that the #mouseGenes temp table has already been generated
    private static final String HUMAN_GENES =
        "select distinct m._Marker_key, \n"
        + " mh.symbol, \n"
        + " mh.chromosome, \n"
        + " smc2._Sequence_key, \n"
        + " achuman.accID, \n"
        + " ldb2.name \n"
        + "into #humanGenes \n"
        + "from #mouseGenes m, \n"
        + " HMD_Homology_Marker hmm1, \n"
        + " HMD_Homology h1, \n"
        + " HMD_Homology h11, \n"
        + " HMD_Homology_Marker hmh, \n"
        + " MRK_Marker mh, \n"
        + " SEQ_Marker_Cache smc2, \n"
        + " ACC_Accession achuman,   \n"
        + " ACC_LogicalDB ldb2 \n"
        + "where  m._Marker_key = hmm1._Marker_key  \n"
        + " and hmm1._Homology_key = h1._Homology_key  \n"
        + " and h1._Class_key = h11._Class_key \n"
        + " and h11._Homology_key = hmh._Homology_key  \n"
        + " and hmh._Marker_key = mh._Marker_key  \n"
        + " and mh._Organism_key =  " + DBConstants.Species_Human
        + " and mh._Marker_key *= smc2._Marker_key  \n"
        + " and smc2._Qualifier_key = " + DBConstants.SEQMarker_Polypeptide
        + " and smc2._Sequence_key *= achuman._Object_key  \n"
        + " and smc2._LogicalDB_key *= achuman._LogicalDB_key \n"
        + " and achuman._MGIType_key = " + DBConstants.MGIType_Sequence
        + " and achuman.preferred = 1 \n"
        + " and achuman._LogicalDB_key *= ldb2._LogicalDB_key ";

    // get rat genes homologous to the marker key in mouseGene temp table,
    // that was annotated to the PIRSF term.  Puts them in temp table.
    // fill in: nothing
    // assumes: that the #mouseGenes temp table has already been generated
    private static final String RAT_GENES =
        "select distinct m._Marker_key, \n"
        + " mr.symbol, \n"
        + " mr.chromosome,  \n"
        + " smc3._Sequence_key, \n"
        + " acrat.accID,  \n"
        + " ldb3.name \n"
        + "into #ratGenes \n"
        + "from #mouseGenes m, \n"
        + " HMD_Homology_Marker hmm2, \n"
        + " HMD_Homology h2,  \n"
        + " HMD_Homology h22,  \n"
        + " HMD_Homology_Marker hmr, \n"
        + " MRK_Marker mr, \n"
        + " SEQ_Marker_Cache smc3,  \n"
        + " ACC_Accession acrat,  \n"
        + " ACC_LogicalDB ldb3  \n"
        + "where m._Marker_key = hmm2._Marker_key  \n"
        + " and hmm2._Homology_key = h2._Homology_key  \n"
        + " and h2._Class_key = h22._Class_key \n"
        + " and h22._Homology_key = hmr._Homology_key  \n"
        + " and hmr._Marker_key = mr._Marker_key  \n"
        + " and mr._Organism_key =  " + DBConstants.Species_Rat
        + " and mr._Marker_key *= smc3._Marker_key  \n"
        + " and smc3._Qualifier_key = " + DBConstants.SEQMarker_Polypeptide
        + " and smc3._Sequence_key *= acrat._Object_key  \n"
        + " and smc3._LogicalDB_key *= acrat._LogicalDB_key \n"
        + " and acrat._MGIType_key = " + DBConstants.MGIType_Sequence
        + " and acrat.preferred = 1 \n"
        + " and acrat._LogicalDB_key *= ldb3._LogicalDB_key ";

    // fetch the mouse genes annotated to the PIRSF term, from a temp table,
    // and also return the homologous human and rat genes if they exist.
    // fill in: nothing
    // assumes: that the #mouseGenes, #humanGenes, and #ratGenes have all
    // been generated.
    private static final String GET_MEMBER_RESULTS =
        "select distinct m._Marker_key, \n"
        + " m.symbol, \n"
        + " m.chromosome,  \n"
        + " m._Sequence_key, \n"
        + " m.accID, \n"
        + " m.name, \n"
        + " h.symbol, \n"
        + " h.chromosome, \n"
        + " h._Sequence_key, \n"
        + " h.accID, \n"
        + " h.name, \n"
        + " r.symbol, \n"
        + " r.chromosome,  \n"
        + " r._Sequence_key, \n"
        + " r.accID,  \n"
        + " r.name \n"
        + "from #mouseGenes m, #humanGenes h, #ratGenes r \n"
        + "where m._Marker_key *= h._Marker_key \n"
        + "and m._Marker_key *= r._Marker_key ";
}
