package org.jax.mgi.shr.datafactory;

import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayList;
import org.jax.mgi.shr.stringutil.Sprintf;
import org.jax.mgi.shr.stringutil.StringLib;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dto.DTOConstants;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.ListHash;

/**
* @module SequenceFactory.java
* @author jw
*/

/** The SequenceFactory class contains many methods which encapsulate
*    knowledge of the sequence-related portions of the schema.  They allow for
*    easy retrieval of various aspects of a sequence's associated data.
* @is a factory for retrieving information related to sequences.
* @has all data available for sequences, retrieved from a database
* @does queries a database to retrieve subsets of information about sequences.
*    Retrieval methods always return a new <tt>DTO</tt>.  These DTO objects
*    may then be merged if needed.  Public methods include:  (parameters not
*    listed here)
*    <OL>
*    <LI> getKey -- find from the CGI parameters the sequence key
*    <LI> getKeyForID() -- find the sequence key for a given accID
*    <LI> getFullInfo() -- get all available information for a sequence
*    <LI> getBasicInfo() -- get a minimal set of available information for a
*        sequence
*    <LI> <I>Many other methods exist for individual data sections.  See below
*        for details.</I>
*    </OL>
*/
public class SequenceFactory extends Factory {


    public final static String NOT_SPECIFIED = "Not Specified";


    /** constructor; instantiates and initializes a new SequenceFactory.
    * @param config provides parameters needed to configure a SequenceFactory.
    * @param sqlDM provides access to a database
    * @param logger provides logging capability
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    public SequenceFactory (DataFactoryCfg config,
                            SQLDataManager sqlDM,
                            Logger logger) {
        super(config,sqlDM,logger,"SequenceFactory");

    }


    /////////////////////////////////////
    // methods for retrieving sequence keys
    /////////////////////////////////////

    /** find a unique key identifying the sequence specified by the given
    *    'parms'.
    * @param parms set of parameters specifying which sequence we are seeking.
    *    Two keys in 'parms' are checked, first, "key" (sequeunce key as a
    *    String), then "id" (sequence accession ID).
    * @return String a unique key identifying the sequence object specified in
    *    the given set of 'parms', or null if none can be found
    * @assumes nothing
    * @effects queries the database using 'sqlDM'
    * @throws DBException if there is a problem while attempting to query the
    *    database using 'sqlDM'
    */
    public String getKey(Map parms) throws DBException {

        // if a 'key' is directly specified in the 'parms', then assume it is
        // a sequence key, and return it...

        String keyStr = StringLib.getFirst((String[]) parms.get ("key"));
        if (keyStr != null) {
            this.timeStamp ("Used key parameter directly for sequence key");
            return keyStr;
        }

        // otherwise, our second choice is a seqID...
        int key = -1;
        String accID = StringLib.getFirst ((String[]) parms.get ("id"));
        if (accID != null) {
            key = this.getKeyForID (accID);
            if (key != -1) {
                this.timeStamp ("Retrieved sequence key from database");
                return Integer.toString(key);
            }
        }
        return null;        // no key could be found
    }

    /* -------------------------------------------------------------------- */

    /** find the sequence key that corresponds to the given 'accID'.  If
    *    multiple sequences have the same 'accID', then we arbitrarily
    *    choose one of the keys to return.
    * @param accID the accession ID for which we seek an associated sequence
    * @return int the sequence key corresponding to 'accID'; will be -1 if
    *    no sequence key could be found
    * @assumes That returning any sequence associated with that accID is
    *    good enough.
    * @effects nothing
    * @throws DBException if there is a problem querying the database or
    *    processing the results.
    */
    public int getKeyForID (String accID) throws DBException {
        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        int key = -1;

        nav = this.sqlDM.executeQuery(Sprintf.sprintf(SEQ_KEY, accID));
        if(nav.next()) {
            rr = (RowReference)nav.getCurrent();
            key = rr.getInt(1).intValue();
        }

        return key;
    }


    /* -------------------------------------------------------------------- */

    //////////////////////////////////////////////////////
    // methods to retrieve sets of sections of information
    //////////////////////////////////////////////////////

    /** retrieves the full suite of data available for the sequence specified in
    *    'parms'.
    * @param parms set of parameters specifying which sequence we are seeking.
    *    Two keys in 'parms' are checked, first, "key" (sequeunce key as a
    *    String), then "id" (sequence accession ID).
    * @return DTO which defines all sequence data fields, note if there is no
    *		      sequence specified by the values in the map, an empty DTO
    *			  will be returned.
    * @assumes nothing
    * @effects retrieves all sequence data by quering a database and retrieving
    *    data via HTTP as needed
    * @throws DBException if there is a problem querying the database or
    *    processing the results
    */
    public DTO getFullInfo (Map parms) throws DBException {
        // sequence key as a String
        String keyStr = getKey (parms);

        // if we could not find a sequence key based on 'parms', then bail out
        // before bothering with anything else
        if (keyStr == null)
        {
            this.logger.logInfo ("Could not find sequence");
            return DTO.getDTO();
        }
        // sequence key as an int
        int key = Integer.parseInt (keyStr);
		return getFullInfo(key);
	}


    /** retrieves the full suite of data available for the sequence specified in
    *    'parms'.
    * @param int key The sequence_key for the sequence you wish to retrieve
    * @return DTO which defines all sequence data fields
    * @assumes nothing
    * @effects retrieves all sequence data by quering a database and retrieving
    *    data via HTTP as needed
    * @throws DBException if there is a problem querying the database or
    *    processing the results
    */
	public DTO getFullInfo (int key) throws DBException {

		// As each chunk of data is gathered, section will capture it from
		// the functions and then be merged into sequence, which will hold
		// all the information about this sequence
        DTO section;
        DTO sequence = DTO.getDTO();

        //All the sequence attributes and source information
        section = getBasicInfo(key);
        sequence.merge (section);
        DTO.putDTO (section);
        this.timeStamp("returned basic info");

        //All of the markers associated with this sequence
        section = getMarkerInfo(key);
        sequence.merge (section);
        DTO.putDTO (section);
        this.timeStamp("returned marker info");

        //all the references associated with this sequence
        section = getReferenceInfo(key);
        sequence.merge (section);
        DTO.putDTO (section);
        this.timeStamp("returned reference info");

        //all the probes associated with this sequence
        section = getProbes(key);
        sequence.merge (section);
        DTO.putDTO (section);
        this.timeStamp("returned probe info");



        return sequence;

    }

    /** retrieves the basic info avaliable for the sequence specified in
    *    'parms'.
    * @param parms set of parameters specifying which sequence we are seeking.
    *    Two keys in 'parms' are checked, first, "key" (sequeunce key as a
    *    String), then "id" (sequence accession ID).
    * @return DTO which defines all sequence attributes and source.
    * @assumes That key represents a valid sequence key
    * @effects retrieves all sequence attributes and source data by
    * quering a database
    * @throws DBException if there is a problem querying the database or
    *    processing the results
    */
    public DTO getBasicInfo (int key) throws DBException {
        DTO section;
        DTO sequence = DTO.getDTO();

        sequence.set(DTOConstants.SequenceKey, Integer.toString(key));
        this.timeStamp("stored sequence key info");

        section = getACCIDs(key);
        sequence.merge(section);
        DTO.putDTO(section);
        this.timeStamp("returned accIDs info");

        section = getSequenceVersion(key);
        sequence.merge (section);
        DTO.putDTO (section);
        this.timeStamp("returned version");

        section = getSequenceDescription(key);
        sequence.merge (section);
        DTO.putDTO (section);
        this.timeStamp("returned description");

        section = getSequenceProvider(key);
        sequence.merge (section);
        DTO.putDTO (section);
        this.timeStamp("returned provider");

        section = getSequenceStatus(key);
        sequence.merge (section);
        DTO.putDTO (section);
        this.timeStamp("returned status");

        section = getSequenceType(key);
        sequence.merge (section);
        DTO.putDTO (section);
        this.timeStamp("returned type");

        section = getSequenceLength(key);
        sequence.merge (section);
        DTO.putDTO (section);
        this.timeStamp("returned length");

        section = getSequenceSource(key);
        sequence.merge (section);
        DTO.putDTO (section);
        this.timeStamp("returned source");

        //The chromosome of the associated markers.  Even on a BAC, there
        //should be at most one.
        section = getChromosome(key);
        sequence.merge (section);
        DTO.putDTO (section);
        this.timeStamp("returned chromosome info");

        //the assembly coordinates of this sequence (if any)
        section = getAssemblyCoords(key);
        sequence.merge(section);
        DTO.putDTO(section);
        this.timeStamp("returned assembly coords");

        return sequence;
    }


    /** retrieves all the accIDs associated with this sequence.
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO where the DTOConstants.AccIDs field is associated with
    *    a hashmap of all the accessionIDs and their actualdb information.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getACCIDs(int key) throws DBException {
        // set of query results
        ResultsNavigator nav = null;
        // one row in 'nav'
        RowReference rr = null;
        // start with a new DTO
        DTO sequence = DTO.getDTO();
        //the accID of the current row
        String id;
        Integer ldbKey;
        //1 if the accID of this row is preferred.
        Integer pref;
        //a list accession IDs assoicated with this sequence
        ArrayList ids = new ArrayList();

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(ACC_IDS, key));

        while(nav.next()) {
            rr = (RowReference) nav.getCurrent();
            id = rr.getString(1);
            pref = rr.getInt(2);
            ldbKey = rr.getInt(3);

            if(pref.intValue() == 1) {
                sequence.set(DTOConstants.AccID, id);
                sequence.set(DTOConstants.LogicalDbKey, ldbKey);
            }
            ids.add(id);
        }
        sequence.set(DTOConstants.AccIDs, ids);
        nav.close();
        return sequence;
    }

    /** retrieves the sequence version information of this sequence.
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO where the DTOConstants.SequenceVersion field is
    * associated with a string containing the version of the sequence,
    * the DTOConstants.SequenceRecordDate field is associated with a
    * string containing the date of the last annotation of this sequence,
    * and the DTOConstants.SequenceDate field is associated with a string
    * containing the date of the last update of this sequence.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getSequenceVersion(int key) throws DBException {
        ResultsNavigator nav = null;
        RowReference rr = null;         // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_VER, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            sequence.set(DTOConstants.SequenceVersion, rr.getString(1));
            sequence.set(DTOConstants.SequenceRecordDate, rr.getString(2));
            sequence.set(DTOConstants.SequenceDate, rr.getString(3));

        }
        nav.close();

        return sequence;
    }

    /** retrieves the description of this sequence.
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO where the DTOConstants.SequenceDescription field is
    * associated with a string containing the description of this
    * sequence.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getSequenceDescription(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String desc;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_DESCRIPTION, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            desc = rr.getString(1);
            sequence.set(DTOConstants.SequenceDescription, desc);
        }
        nav.close();

        return sequence;
    }

    /** retrieves the provider, (Genbank:Rod, Trembl, etc) of this sequence.
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO where the DTOConstants.SequenceProvider field is
    * associated with a string containing the provider of this
    * sequence.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getSequenceProvider(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String provider;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_PROVIDER, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            provider = rr.getString(1);
            sequence.set(DTOConstants.SequenceProvider, provider);
        }
        nav.close();

        return sequence;
    }

    /** retrieves the status of this sequence.
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO where the DTOConstants.SequenceStatus field is
    * associated with a string containing the status of this
    * sequence.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getSequenceStatus(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String status;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_STATUS, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            status = rr.getString(1);
            sequence.set(DTOConstants.SequenceStatus, status);
        }
        nav.close();

        return sequence;
    }

    /** retrieves the type (DNA,RNA,etc) of this sequence.
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO where the DTOConstants.SequenceType field is
    * associated with a string containing the type of this
    * sequence.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getSequenceType(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String type;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_TYPE, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            type = rr.getString(1);
            sequence.set(DTOConstants.SequenceType, type);
        }
        nav.close();

        return sequence;
    }

    /** retrieves the length of this sequence.
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO where the DTOConstants.SequenceLength field is
    * associated with a string containing the length of this
    * sequence.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getSequenceLength(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;         // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        Integer len;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_LENGTH, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            sequence.set(DTOConstants.SequenceLength, rr.getInt(1));
        }
        nav.close();

        return sequence;
    }

    /** retrieves the source, either raw or resolved, of this sequence.
    *if any of the resolved source values return a "Not Resolved"
    * they will be overwritten with the raw value.  If there is no
    * raw value to be found, they will be replaced with a "Not Specified"
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO where
    *   DTOConstants.Age is associated with a string containing the age of
    *   the source of this sequence.
    *   DTOConstants.CellLine is associated with a string containing the
    *   cell line of the source of this sequence.
    *   DTOConstants.Gender is associated with a string containing the
    *   gender of the source of this sequence.
    *   DTOConstants.Library is associated with a string containing the
    *   library of the source of this sequence.
    *   DTOConstants.Organism is associated with a string containing the
    *   organism of the source of this sequence.
    *   DTOConstants.Strain is associated with a string containing the
    *   strain of the source of this sequence.
    *   DTOConstants.Tissue is associated with a string containing the
    *   tissue of the source of this sequence.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getSequenceSource(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String age, cellLine, gender, library, organism, strain, tissue;

        this.sqlDM.execute(Sprintf.sprintf(SEQ_SOURCE_TABLE, key));
        this.timeStamp("resolved source loaded into table");
        this.sqlDM.execute(RAW_LIBRARY);
        this.timeStamp("raw library");
        this.sqlDM.execute(RAW_ORGANISM);
        this.timeStamp("raw organism");
        this.sqlDM.execute(RAW_STRAIN);
        this.timeStamp("raw strain");
        this.sqlDM.execute(RAW_TISSUE);
        this.timeStamp("raw tissue");
        this.sqlDM.execute(RAW_AGE);
        this.timeStamp("raw age");
        this.sqlDM.execute(RAW_SEX);
        this.timeStamp("raw sex");
        this.sqlDM.execute(RAW_CELLLINE);
        this.timeStamp("raw cell line");
        this.timeStamp("gathered raw source");
        nav = this.sqlDM.executeQuery(SEQ_SOURCE);
        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            library = rr.getString(1);
            if(library.equals("*"))
                library = NOT_SPECIFIED;
            organism = rr.getString(2);
            if(organism.equals("*"))
                organism = NOT_SPECIFIED;
            strain = rr.getString(3);
            if(strain.equals("*"))
                strain = NOT_SPECIFIED;
            tissue = rr.getString(4);
            if(tissue.equals("*"))
                tissue = NOT_SPECIFIED;
            age = rr.getString(5);
            if(age.equals("*"))
                age = NOT_SPECIFIED;
            gender = rr.getString(6);
            if(gender.equals("*"))
                gender = NOT_SPECIFIED;
            cellLine = rr.getString(7);
            if(cellLine.equals("*"))
                cellLine = NOT_SPECIFIED;
            sequence.set(DTOConstants.Age, age);
            sequence.set(DTOConstants.CellLine, cellLine);
            sequence.set(DTOConstants.Gender, gender);
            sequence.set(DTOConstants.Library, library);
            sequence.set(DTOConstants.Organism, organism);
            sequence.set(DTOConstants.Strain, strain);
            sequence.set(DTOConstants.Tissue, tissue);
        }
        nav.close();
		this.sqlDM.execute(SEQ_SOURCE_TABLE_DROP);
        return sequence;
    }

    /** retrieve the chromosome of an arbitrary marker associated with this
    * sequence.
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO where the DTOConstants.Chromosome field is
    * associated with a string containing the provider of this
    * sequence.
    * @assumes That all markers associated with this sequence reside on the
    * same chromosome.
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getChromosome(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(CHROMOSOME, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            sequence.set(DTOConstants.Chromosome, rr.getString(1));
        }
        nav.close();

        return sequence;
    }

    /** retrieve assembly coordinates associated with this sequence.
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO where:
    * the DTOConstants.startCoord field is a string containing the floating
    * point value (in bp) of the start of this sequence on the assembly
    * the DTOConstants.stopCoord field is a string containing the floating
    * point value (in bp) of the end of this sequence on the assembly
    * the DTOConstants.strand field is either + or -, depending on which
    * strand of the helix this sequence came from.
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getAssemblyCoords(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        Boolean isAssembly = new Boolean(false);

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(ASSEMBLY_COORDS, key));

        if (nav.next()) {
			isAssembly = new Boolean(true);
            rr = (RowReference) nav.getCurrent();
            sequence.set(DTOConstants.StartCoord,rr.getFloat(1));
            sequence.set(DTOConstants.StopCoord,rr.getFloat(2));
            sequence.set(DTOConstants.Strand,rr.getString(3));
        }
        nav.close();

      	sequence.set(DTOConstants.IsAssembly,isAssembly);

        return sequence;
    }


    /** retrieves a host of information about all markers associated
    * with this sequence
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO DTOConstants.Markers refers to a List of DTOs each
    *			  consisting of the information about 1 marker
    *			  associated with this sequence.  The details of these
    *			  DTOs is as follows:
    *
    *   DTOConstants.MarkerKey is associated with an integer containing
    *   marker key of this marker.
    *   DTOConstants.MarkerSymbol is associated with a string containing the
    *   marker symbol of this marker.
    *   DTOConstants.MarkerName is associated with a string containing the
    *   marker name of this marker.
    *   DTOConstants.MarkerType is associated with a string containing the
    *   marker type of this marker.
    *   DTOConstants.PhenoCount is associated with a string containing the
    *   number of phenotype classification (PhenoSlim) terms associated
    *   with this marker.
    *   DTOConstants.ExpressionAssayCount is associated with a string
    *   containing the number of assays associated with this marker.
    *   DTOConstants.GOAnnotationCount is associated with a string containing
    *   the number of Gene Ontology terms associated with this marker.
    *   DTOConstants.OrthologCount is associated with a string containing the
    *   number of orthologous markers associated with this marker
    *   DTOConstants.RefsKey is associated with a string containing the
    *   reference key for the association between this marker and the
    *   sequence.
    *   DTOConstants.RefID is associated with a string containing the
    *   reference ID (J number) for the association between this marker and
    *   the sequence.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getMarkerInfo(int key) throws DBException {
        ResultsNavigator nav = null;    // set of query results
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        ArrayList allMarkers = new ArrayList();
        DTO marker;

        this.sqlDM.execute(MARKER_TABLE);
        this.sqlDM.execute(Sprintf.sprintf(MARKER_NOMENCLATURE, key));
        this.sqlDM.execute(MARKER_GO_COUNT);
        this.sqlDM.execute(MARKER_GO_UPDATE);
        this.sqlDM.execute(MARKER_ASSAY_COUNT);
        this.sqlDM.execute(MARKER_ASSAY_UPDATE);
        this.sqlDM.execute(MARKER_ORTHOLOGY_COUNT);
        this.sqlDM.execute(MARKER_ORTHOLOGY_UPDATE);
        this.sqlDM.execute(MARKER_ALLELE_COUNT);
        this.sqlDM.execute(MARKER_ALLELE_UPDATE);
        this.sqlDM.execute(Sprintf.sprintf(MARKER_REF,key));
        nav = this.sqlDM.executeQuery(MARKER_INFO);
        while (nav.next()) {
            marker = DTO.getDTO();
            rr = (RowReference) nav.getCurrent();
            marker.set(DTOConstants.MarkerKey,rr.getInt(1));
            marker.set(DTOConstants.MarkerSymbol,rr.getString(2));
            marker.set(DTOConstants.MarkerName,rr.getString(3));
            marker.set(DTOConstants.MarkerType,rr.getString(4));
            marker.set(DTOConstants.PhenoCount,rr.getString(5));
            marker.set(DTOConstants.ExpressionAssayCount, rr.getString(6));
            marker.set(DTOConstants.GOAnnotationCount, rr.getString(7));
            marker.set(DTOConstants.OrthologCount, rr.getString(8));
            marker.set(DTOConstants.RefsKey, rr.getInt(9));
            marker.set(DTOConstants.RefID, rr.getString(10));
            allMarkers.add(marker);
        }
        sequence.set(DTOConstants.Markers,allMarkers);
        return sequence;
    }

    /** retrieves all references associated with this sequence.
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO DTOConstants.References refers to a List of DTOs each
    *             consisting of the information about 1 reference
    *             associated with this sequence.  See makeReferenceDTO
    *             for details about these inner DTOs.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getReferenceInfo(int key) throws DBException {
        ResultsNavigator nav = null;
        RowReference rr = null;
        DTO sequence = DTO.getDTO();
        ArrayList allRefs = new ArrayList();
        DTO ref;

        nav = this.sqlDM.executeQuery(Sprintf.sprintf(REFS, key));
        while (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            ref = makeReferenceDTO(rr);
            allRefs.add(ref);
        }
        sequence.set(DTOConstants.References, allRefs);
        return sequence;
    }

    /** create a DTO describing the reference identified in 'rr'.
    * NOTE: THIS WAS TAKEN VERBATIM FROM THE COPY IN MarkerFactory
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
    protected DTO makeReferenceDTO (RowReference rr)
            throws DBException {

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
        if (authors != null) {
            ref.set (DTOConstants.Authors,
            (ArrayList) StringLib.split (authors, ";") );
        } else {
            ref.set (DTOConstants.Authors, null);
        }
        return ref;
    }

    /** retrieves a host of information about all probes associated
    * with this sequence
    * @param key the sequence key of the sequence whose data we seek
    * @return DTO DTOConstants.Probes refers to a List of DTOs each
    *             consisting of the information about 1 probe
    *             associated with this sequence.  The details of these
    *			  DTOs is as follows:
    *
    *   DTOConstants.ProbeKey is associated with an integer containing
    *   the _Probe_key of this probe.
    *   DTOConstants.ProbeName is associated with a string containing the
    *   name of this probe.
    *   DTOConstants.AccID is associated with a string containing the
    *   MGI accession ID of this probe.
    *   DTOConstants.CloneID is associated with a string containing the
    *   provider's accessionID for this probe.
    *   DTOConstants.SegmentType is associated with a string containing the
    *   type of this probe.
    *   DTOConstants.CloneCollection is associated with a List
    *   containing the names of the LogicalDB whose clone collection this
    *   probe is a member of.  If the probe is part of no collection, then
    *   this value will be an empty arraylist.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getProbes(int key) throws DBException {
        ResultsNavigator nav = null;
        RowReference rr = null;
        DTO sequence = DTO.getDTO();
        ArrayList allProbes = new ArrayList();
        DTO probe;
        HashMap collections = new HashMap();
        String probeKey;
        ArrayList collectionList = new ArrayList();
        String curKey = "";
        String lastKey = null;

        this.sqlDM.execute(PROBE_TABLE);
        this.sqlDM.execute(Sprintf.sprintf(PROBES, key));
        this.sqlDM.execute(CLONE_COLLECTION_TABLE);
        this.sqlDM.execute(CLONE_ACC_ID);
        nav = this.sqlDM.executeQuery(CLONE_INFO);
        while(nav.next()) {
            rr = (RowReference) nav.getCurrent();
            curKey = rr.getString(1);
            if(curKey.equals(lastKey)) {
                collectionList.add(rr.getString(2));
            } else {
                if(lastKey != null)
                    collections.put(lastKey,collectionList);
                collectionList = new ArrayList();
                collectionList.add(rr.getString(2));
            }
            lastKey = curKey;
        }
        collections.put(curKey,collectionList);
        nav = this.sqlDM.executeQuery(PROBE_INFO);

        while (nav.next()) {
            probe = DTO.getDTO();
            rr = (RowReference) nav.getCurrent();
            probeKey = rr.getString(1);
            probe.set(DTOConstants.ProbeKey, probeKey);
            probe.set(DTOConstants.ProbeName, rr.getString(2));
            probe.set(DTOConstants.AccID, rr.getString(3));
            probe.set(DTOConstants.CloneID, rr.getString(4));
            probe.set(DTOConstants.SegmentType, rr.getString(5));
            probe.set(DTOConstants.CloneCollection,
                      (ArrayList)collections.get(probeKey));
            allProbes.add(probe);
        }
        sequence.set(DTOConstants.Probes, allProbes);
        return sequence;
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

    // get the sequence key
    // fill in: accession ID of the sequence (string)
    private static final String SEQ_KEY =
            "select aa._Object_key\n"+
            "from ACC_Accession aa\n"+
            "where aa.accID = '%s'\n"+
            "and aa._MGIType_key = 19";

    // get all accession IDs associated with this sequence key
    // fill in: sequence key (int)
    private static final String ACC_IDS =
            "select distinct aa.accID, aa.preferred, aa._LogicalDB_key\n"+
            "from ACC_Accession aa, ACC_ActualDB adb, \n"+
            "MGI_Set ms, MGI_SetMember msm\n"+
            "where aa._Object_key = %d\n"+
            "and aa._MGIType_key = 19\n"+
            "and aa._LogicalDB_key = adb._LogicalDB_key\n"+
            "and msm._Object_key = adb._ActualDB_key\n"+
            "and msm._Set_key = ms._Set_key\n"+
            "and ms.name = 'Actual DB'\n"+
            "order by aa.accID";

    // get the version of the sequence
    // fill in: sequence key (int)
    private static final String SEQ_VER =
            "select version, seqrecord_date, sequence_date\n"+
            "from SEQ_Sequence\n"+
            "where _Sequence_key = %d";

    // get the description of the sequence
    // fill in: sequence key (int)
    private static final String SEQ_DESCRIPTION =
            "select description\n" +
            "from SEQ_Sequence\n" +
            "where _Sequence_key = %d";

    // get the name of the sequence provider
    // fill in: sequence key (int)
    private static final String SEQ_PROVIDER =
            "select vt.term\n"+
            "from VOC_Term vt, SEQ_Sequence ss\n"+
            "where ss._Sequence_key = %d\n"+
            "and vt._Term_key = ss._SequenceProvider_key";

    // get the status of the sequence
    // fill in: sequence key (int)
    private static final String SEQ_STATUS =
            "select vt.term\n"+
            "from VOC_Term vt, SEQ_Sequence ss\n"+
            "where ss._Sequence_key = %d\n"+
            "and vt._Term_key = ss._SequenceStatus_key";

    // get the type(DNA,RNA,etc) of the sequence
    // fill in: sequence key (int)
    private static final String SEQ_TYPE =
            "select vt.term\n"+
            "from VOC_Term vt, SEQ_Sequence ss\n"+
            "where ss._Sequence_key = %d\n"+
            "and vt._Term_key = ss._SequenceType_key";

    // get the length of the sequence
    // fill in: sequence key (int)
    private static final String SEQ_LENGTH =
            "select length\n"+
            "from SEQ_Sequence\n"+
            "where _Sequence_key = %d";

    // loads #seqSource with all the resolved source values for this
    // sequence
    // fill in: sequence key (int)
    private static final String SEQ_SOURCE_TABLE =
             "select _Sequence_key,\n"+
            "    library = ps.name,\n"+
            "    organism = mo.commonName,\n"+
            "    strain = pstr.strain,\n"+
            "    tissue = pt.tissue,\n"+
            "    age = ps.age,\n"+
            "    sex = vtG.term,\n"+
            "    cellLine = vtC.term\n"+
            "into #seqSource\n"+
            "from PRB_Source ps, SEQ_Source_Assoc ssa,\n"+
            "MGI_Organism mo, PRB_Strain pstr, PRB_Tissue pt, \n"+
            "VOC_Term vtG, VOC_Term vtC\n"+
            "where ssa._Sequence_key = %d\n"+
            "and ssa._Source_key = ps._Source_key\n"+
            "and ps._Organism_key = mo._Organism_key\n"+
            "and ps._Strain_key = pstr._Strain_key\n"+
            "and ps._Tissue_key = pt._Tissue_key\n"+
            "and ps._Gender_key = vtG._Term_key\n"+
            "and ps._CellLine_key = vtC._Term_key\n";

	private static final String SEQ_SOURCE_TABLE_DROP =
			"drop table #seqSource";


    // loads #seqSource with the raw library name if there was no
    // resolved value
    private static final String RAW_LIBRARY =
            "update #seqSource\n"+
            "set library = ss.rawLibrary + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and (s.library = 'Not Resolved' \n"+
            "     or s.library = null)";

    // loads #seqSource with the raw organism if there was no
    // resolved value
    private static final String RAW_ORGANISM =
            "update #seqSource\n"+
            "set organism = ss.rawOrganism + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.organism = 'Not Resolved'";

    // loads #seqSource with the raw strain if there was no
    // resolved value
    private static final String RAW_STRAIN =
            "update #seqSource\n"+
            "set strain = ss.rawStrain + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.strain = 'Not Resolved'";

    // loads #seqSource with the raw tissue if there was no
    // resolved value
    private static final String RAW_TISSUE =
            "update #seqSource\n"+
            "set tissue = ss.rawTissue + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.tissue = 'Not Resolved'";

    // loads #seqSource with the raw age if there was no
    // resolved value
    private static final String RAW_AGE =
            "update #seqSource\n"+
            "set age = ss.rawAge + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.age = 'Not Resolved'";

    // loads #seqSource with the raw sex if there was no
    // resolved value
    private static final String RAW_SEX =
            "update #seqSource\n"+
            "set sex = ss.rawSex + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.sex = 'Not Resolved'";

    // loads #seqSource with the raw cell line if there was no
    // resolved value
    private static final String RAW_CELLLINE =
            "update #seqSource\n"+
            "set cellLine = ss.rawCellLine + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.cellLine = 'Not Resolved'";

    // gets all the source information for this sequence.  If there is a
    // raw value, it will be followed by a *
    private static final String SEQ_SOURCE =
            "select library,organism,strain,tissue,age,sex,cellLine\n"+
            "from #seqSource";

    // get the chromosomes of all markers associated with this sequence
    // fill in: sequence key (int)
    private static final String CHROMOSOME =
            "select mm.chromosome\n"+
            "from MRK_Marker mm, SEQ_Marker_Cache smc\n"+
            "where smc._Sequence_key = %d\n"+
            "and mm._Marker_key = smc._Marker_key";

    // get the assembly coordinate and strand information for this sequence
    // (if any)
    // fill in: sequence key (int)
    private static final String ASSEMBLY_COORDS =
            "select mcf.startCoordinate, mcf.endCoordinate, mcf.strand\n"+
            "from VOC_Term vt, MAP_Coordinate mc, MAP_Coord_Feature mcf,\n"+
            "SEQ_Sequence ss\n"+
            "where ss._Sequence_key = mcf._Object_key\n"+
            "and mcf._MGIType_key = 19\n"+
            "and mcf._Map_key = mc._Map_key\n"+
            "and vt.term = 'Assembly'\n"+
            "and mc._MapType_key = vt._Term_key\n"+
            "and ss._Sequence_key = %d";

    // sets up a temp table, #mrk to hold all the information about all
    //  markers associated with this sequence
    private static final String MARKER_TABLE =
            "CREATE TABLE #mrk(\n"+
            "_Marker_key  int NOT NULL,\n"+
            "markerType   varchar(255)  NOT NULL,\n"+
            "assayCount   int default 0 NULL,\n"+
            "alleleCount  int default 0 NULL,\n"+
            "GOCount      int default 0 NULL,\n"+
            "orthoCount   int default 0 NULL,\n"+
            "name         varchar(255)  NOT NULL,\n"+
            "symbol       varchar(50)   NOT NULL,\n"+
            "_Refs_key    int NULL,\n"+
            "refID        varchar(255) NULL\n"+
            ")";

    // loads #mrk with the _Marker_key, name, symbol, and markerType of all
    // markers associated with this sequence
    // fill in: sequence key (int)
    private static final String MARKER_NOMENCLATURE =
            "insert #mrk(_Marker_key,name,symbol,markerType)\n"+
            "select distinct mm._Marker_key, mm.name, mm.symbol, mt.name\n"+
            "from MRK_Marker mm, SEQ_Marker_Cache smc, MRK_Types mt\n"+
            "where smc._Sequence_key = %s\n"+
            "and mm._Marker_key = smc._Marker_key\n"+
            "and mt._Marker_Type_key = mm._Marker_Type_key";

    // loads #goCnt with all _Marker_keys from #mrk and the count of
    // Gene Ontology terms associated with each of those markers
    private static final String MARKER_GO_COUNT =
            "SELECT goCount = count(distinct va._Term_key),\n"+
            "m._Marker_key\n"+
            "into #goCnt\n"+
            "FROM VOC_Annot va, VOC_GOMarker_AnnotType_View gm,\n"+
            "#mrk m\n"+
            "WHERE va._Object_key = m._Marker_key\n"+
            "AND va._AnnotType_key = gm._AnnotType_key\n"+
            "group by m._Marker_key";

    // updates #mrk, associating the count of GO terms with their marker keys
    private static final String MARKER_GO_UPDATE =
            "update #mrk\n"+
            "set goCount = gc.goCount\n"+
            "from #mrk m, #goCnt gc\n"+
            "where m._Marker_key = gc._Marker_key\n";

    // loads #assayCount with all _Marker_keys from #mrk and the count of
    // Gene Expression Assays associated with each of those markers
    private static final String MARKER_ASSAY_COUNT =
            "select assayCount = count(ga._Assay_key), m._Marker_key\n"+
            "into #assayCnt\n"+
            "from #mrk m, GXD_Assay ga\n"+
            "where m._Marker_key = ga._Marker_key\n"+
            "group by m._Marker_key";

    // updates #mrk, associating the count of assays with their marker keys
    private static final String MARKER_ASSAY_UPDATE =
            "update #mrk\n"+
            "set assayCount = ac.assayCount\n"+
            "from #mrk m, #assayCnt ac\n"+
            "where m._Marker_key = ac._Marker_key\n";


    // get orthologous species
    // fill in: marker key (int)
    private static final String MARKER_ORTHOLOGY_COUNT =
        "SELECT orthoCount = count(distinct mm._Organism_key),\n"+
        "        m._Marker_key\n"+
        "into #orthoCnt\n"+
        "FROM MRK_Marker mm,\n"+
        "HMD_Homology hh1,\n"+
        "HMD_Homology_Marker hm1,\n"+
        "HMD_Homology hh2,\n"+
        "HMD_Homology_Marker hm2,\n"+
        "#mrk m\n"+
        "where hm1._Marker_key = m._Marker_key\n"+
        "and hm1._Homology_key = hh1._Homology_key\n"+
        "and hh1._Class_key = hh2._Class_key\n"+
        "and hh2._Homology_key = hm2._Homology_key\n"+
        "and hm2._Marker_key = mm._Marker_key\n"+
        "and mm._Organism_key != 1\n"+
        "group by m._Marker_key\n";

    // updates #mrk, associating the count of orthologs with their marker keys
    private static final String MARKER_ORTHOLOGY_UPDATE =
            "update #mrk\n"+
            "set orthoCount = oc.orthoCount\n"+
            "from #mrk m, #orthoCnt oc\n"+
            "where m._Marker_key = oc._Marker_key\n";

    // loads #alleleCnt with all _Marker_keys from #mrk and the count of
    // alleles associated with each of those markers
    private static final String MARKER_ALLELE_COUNT =
            "select alleleCount = count(al._Allele_key), m._Marker_key\n"+
            "into #alleleCnt\n"+
            "from #mrk m, ALL_Allele al\n"+
            "where m._Marker_key = al._Marker_key\n"+
            "and al.name !='wild type'\n"+
            "group by m._Marker_key";

    // updates #mrk, associating the count of alleles with their marker keys
    private static final String MARKER_ALLELE_UPDATE =
            "update #mrk\n"+
            "set alleleCount = ac.alleleCount\n"+
            "from #mrk m, #alleleCnt ac\n"+
            "where m._Marker_key = ac._Marker_key\n";

    // updates #mrk with the information for the reference associating the
    // sequence and this marker
    // fill in: sequence key (int)
    private static final String MARKER_REF =
            "update #mrk\n"+
            "set _Refs_key = smc._Refs_key, refID = aa.accID\n"+
            "from SEQ_Marker_Cache smc, ACC_Accession aa, #mrk m\n"+
            "where smc._Refs_key = aa._Object_key\n"+
            "and m._Marker_key = smc._Marker_key\n"+
            "and aa._MGIType_key = 1\n"+
            "and aa.prefixPart = 'J:'\n"+
            "and smc._Sequence_key = %d\n"+
            "and aa.preferred = 1";

    // gets the marker information, the counts, and the association reference
    // information for all markers associated with this sequence
    private static final String MARKER_INFO =
            "select _Marker_key, symbol, name, markerType,\n"+
            "alleleCount,assayCount,GOCount,orthoCount,\n"+
            "_Refs_key, refID\n"+
            "from #mrk\n"+
            "order by symbol";

    // gets all references associated with this sequence.
    // this includes the _Refs_key, J:, author, title, and a short citation.
    // fill in: sequence key (int)
    private static final String REFS =
            "SELECT br._Refs_key, aa.accID, br.authors, br.authors2,\n"+
            "br.title, br.title2,\n"+
            "citation = br.journal + ' ' + br.date + ';' + br.vol "+
            "+ '(' + br.issue + '):' + br.pgs \n"+
            "from MGI_Reference_Assoc mra, BIB_Refs br, ACC_Accession aa\n"+
            "where mra._Object_key = %d\n"+
            "and mra._MGIType_key = 19\n"+
            "and br._Refs_key = mra._Refs_key\n"+
            "and aa._MGIType_key = 1\n"+
            "and aa.prefixPart = 'J:'\n"+
            "and aa._Object_key = br._Refs_key";

    // sets up a temp table, #prbs to hold all the information about all
    // the probes associated with this sequence
    private static final String PROBE_TABLE =
            "CREATE TABLE #prbs(\n"+
            "_Probe_key   int NOT NULL,\n"+
            "name         varchar(40)  NOT NULL,\n"+
            "mgiID        varchar(30)  NOT NULL,\n"+
            "accID        varchar(30)  NULL,\n"+
            "segmentType  varchar(255) NULL,\n"+
            ")";

    // loads #prbs with the name, _Probe_key, type, and mgiID of all the
    // probes associated with this sequence
    // fill in: sequence key (int)
    private static final String PROBES =
            "insert #prbs(name,_Probe_key,segmentType, mgiID)\n"+
            "select distinct pp.name, pp._Probe_key, vt.term, aa.accID\n"+
            "from PRB_Probe pp, SEQ_Probe_Cache sqc, VOC_Term vt, ACC_Accession aa\n"+
            "where sqc._Sequence_key = %d\n"+
            "and pp._Probe_key = sqc._Probe_key\n"+
            "and aa._Object_key = pp._Probe_key\n"+
            "and aa._MGIType_key = 3\n"+
            "and aa.preferred = 1\n"+
            "and aa._LogicalDB_key = 1\n"+
            "and vt._Term_key = pp._SegmentType_key";

    // loads #prbs with the logicaldb name of the collection this probe
    // belongs to, if any and the providers accID for the probe.
    private static final String CLONE_COLLECTION_TABLE =
            "select distinct collection = msclv.name, p._Probe_key\n"+
            "into #collection\n"+
            "from #prbs p, PRB_Probe pp,\n"+
            "MGI_SetMember msm, MGI_Set_CloneLibrary_View msclv\n"+
            "where p._Probe_key = pp._Probe_key\n"+
            "and pp._Source_key = msm._Object_key\n"+
            "and msm._Set_key = msclv._Set_key";

    private static final String CLONE_ACC_ID =
            "update #prbs\n"+
            "set accID = aa.accID\n"+
            "from ACC_Accession aa, #prbs p, #collection c, ACC_LogicalDB ldb\n"+
            "where aa._Object_key = p._Probe_key\n"+
            "and aa._MGIType_key = 3\n"+
            "and p._Probe_key = c._Probe_key\n"+
            "and c.collection = ldb.name\n"+
            "and ldb._LogicalDB_key = aa._LogicalDB_key";

    private static final String CLONE_INFO =
            "select _Probe_key, collection\n"+
            "from #collection\n"+
            "order by _Probe_key";

    // gets the probe information from #prbs
    private static final String PROBE_INFO =
            "select _Probe_key, name, mgiID, accID,segmentType\n"+
            "from #prbs";
}
