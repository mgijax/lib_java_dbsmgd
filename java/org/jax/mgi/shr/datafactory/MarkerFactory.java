package org.jax.mgi.shr.datafactory;

/*
* $Header$
* $Name$
*/

import java.util.Map;
import java.util.Vector;
import java.util.ArrayList;
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
* @module MarkerFactory.java
* @author jsb
*/

/** The MarkerFactory class contains many methods which encapsulate
*    knowledge of the marker-related portions of the schema.  They allow for
*    easy retrieval of various aspects of a marker's associated data.
* @is a factory for retrieving information related to markers.
* @has all data available for markers, retrieved from a database
* @does queries a database to retrieve subsets of information about markers.
*    Retrieval methods always return a new <tt>DTO</tt>.  These DTO objects
*    may then be merged if needed.  Public methods include:  (parameters not
*    listed here)
*    <OL>
*    <LI> getKeyForSymbol() -- find the marker key for a given marker symbol
*    <LI> getKeyForID() -- find the marker key for a given MGI ID
*    <LI> getFullInfo() -- get all available information for a marker
*    <LI> getBasicInfo() -- get a minimal set of available information for a
*        marker
*    <LI> <I>Many other methods exist for individual data sections.  See below
*        for details.</I>
*    </OL>
*/
public class MarkerFactory
{
    /////////////////////
    // instance variables
    /////////////////////

    // provides access to the database
    private SQLDataManager sqlDM = null;

    // provides logging capability for info, error, debug logs
    private Logger logger = null;

    // provides parameters needed to configure a MarkerFactory
    private DataFactoryCfg config = null;

    // used to log profiling information (timings for sections of code)
    private TimeStamper timer = null;

    ///////////////
    // Constructors
    ///////////////

    /** constructor; instantiates and initializes a new MarkerFactory.
    * @param config provides parameters needed to configure a MarkerFactory,
    *    including values for "MINIMAP_URL" and "GENE_FAMILY_MAP_URL" if you
    *    want to retrieve those attributes of each marker.
    * @param sqlDM provides access to a database
    * @param logger provides logging capability
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    public MarkerFactory (DataFactoryCfg config, SQLDataManager sqlDM,
        Logger logger)
    {
        this.config = config;
	this.sqlDM = sqlDM;
	this.logger = logger;
	return;
    }

    //////////////////////////
    // public instance methods
    //////////////////////////

    /** adds the given <tt>TimeStamper</tt> to the MarkerFactory and begins
    *    collection of timing data for MarkerFactory code
    * @param timer used to collect profiling data for sections of code
    * @return nothing
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    public void setTimeStamper (TimeStamper timer)
    {
        this.timer = timer;
	this.timeStamp ("TimeStamper added to MarkerFactory");
	return;
    }

    /////////////////////////////////////
    // methods for retrieving marker keys
    /////////////////////////////////////

    /** find a unique key identifying the marker specified by the given
    *    'parms'.
    * @param parms set of parameters specifying which marker we are seeking.
    *    Three keys in 'parms' are checked, in order of preference:  "key"
    *    (marker key as a String), "id" (marker accession ID), and "symbol"
    *    (marker symbol).
    * @return String a unique key identifying the marker object specified in
    *    the given set of 'parms', or null if none can be found
    * @assumes nothing
    * @effects queries the database using 'sqlDM'
    * @throws DBException if there is a problem while attempting to query the
    *    database using 'sqlDM'
    */
    public String getKey (Map parms) throws DBException
    {
	// if a 'key' is directly specified in the 'parms', then assume it is
	// a marker key, and return it...

        String keyStr = StringLib.getFirst ((String[]) parms.get ("key"));
	if (keyStr != null)
	{
	    this.timeStamp ("Used key parameter directly for marker key");
	    return keyStr;
	}

	// otherwise, our second choice is a marker's accession ID...

	int key = -1;

	String accID = StringLib.getFirst ((String[]) parms.get ("id"));
        if (accID != null)
	{
	    key = this.getKeyForID (accID);
	}

	// and, finally, we check for 'symbol' as a last resort...

	if (key == -1)
	{
	    String symbol = StringLib.getFirst (
					(String[]) parms.get ("symbol") );
	    if (symbol != null)
	    {
	        key = this.getKeyForSymbol (symbol);
	    }
	}

	// if we found an integer key, then convert it to a String for return

	if (key != -1)
	{
	    this.timeStamp ("Retrieved marker key from database");
	    return Integer.toString(key);
	}
	return null;		// no key could be found
    }

    /* -------------------------------------------------------------------- */

    /** find the marker key that corresponds to the given mouse 'symbol'.  If
    *    multiple mouse markers have the same symbol, then we return the key
    *    for the one which is the current symbol (if any).  If none are
    *    current, then we arbitrarily choose one of the keys to return.
    * @param symbol the marker symbol (for mouse)
    * @return int the marker key corresponding to 'symbol'; will be -1 if
    *    no marker key could be found
    * @assumes Only one current mouse marker can have the same mouse symbol.
    *    (Duplicate symbols are allowed, but only one can be considered to be
    *    current nomenclature.)
    * @effects nothing
    * @throws DBException if there is a problem querying the database or
    *    processing the results.
    */
    public int getKeyForSymbol (String symbol) throws DBException
    {
	int markerKey = -1;		// the marker key we will return
	int thisStatus = -1;		// the marker status key of this row
	int thisKey = -1;		// the marker key of this row
	ResultsNavigator nav = null;	// set of query results
	RowReference rr = null;		// one row in 'nav'

	nav = this.sqlDM.executeQuery (
	        Sprintf.sprintf (KEY_FOR_SYMBOL, symbol) );

	while (nav.next())		// step through all returned rows
	{
	    rr = (RowReference) nav.getCurrent();
	    thisKey = rr.getInt(1).intValue();
	    thisStatus = rr.getInt(2).intValue();

	    // If this row's marker is current, then it overrides any key
	    // seen so far.  Otherwise, only keep this key if we've not yet
	    // seen one.

	    if (thisStatus == DBConstants.Marker_Approved)
	    {
		// once we've found the approved marker, then we can bail
		// out of the loop

	        markerKey = thisKey;
		break;
	    }
	    else if (markerKey == -1)
	    {
	        markerKey = thisKey;
	    }
	}
	nav.close();
        return markerKey;
    }

    /* -------------------------------------------------------------------- */

    /** find the marker key that corresponds to the given mouse 'accID'.  If
    *    multiple mouse markers have the same 'accID', then we arbitrarily
    *    choose one of the keys to return.
    * @param accID the accession ID for which we seek an associated marker
    * @return int the marker key corresponding to 'accID'; will be -1 if
    *    no marker key could be found
    * @assumes That returning any marker associated with that accession ID is
    *    good enough.  In the case where 'accID' is an object's primary MGI
    *    ID, this is valid.
    * @effects nothing
    * @throws DBException if there is a problem querying the database or
    *    processing the results.
    */
    public int getKeyForID (String accID) throws DBException
    {
	int markerKey = -1;		// the marker key we will return
	ResultsNavigator nav = null;	// set of query results
	RowReference rr = null;		// one row in 'nav'

        nav = this.sqlDM.executeQuery (Sprintf.sprintf (KEY_FOR_ID, accID) );

        if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    markerKey = rr.getInt(1).intValue();
	}
	nav.close();
        return markerKey;
    }

    /* -------------------------------------------------------------------- */

    //////////////////////////////////////////////////////
    // methods to retrieve sets of sections of information
    //////////////////////////////////////////////////////

    /** retrieve the full suite of data available for the marker specified in
    *    'parms'.
    * @param parms set of parameters specifying which marker we are seeking.
    *    Three keys in 'parms' are checked, in order of preference:  "key"
    *    (marker key as a String), "id" (marker accession ID), and "symbol"
    *    (marker symbol).
    * @return DTO which defines all marker data fields
    * @assumes nothing
    * @effects retrieves all marker data by quering a database and retrieving
    *    data via HTTP as needed
    * @throws DBException if there is a problem querying the database or
    *    processing the results
    * @throws MalformedURLException if any URLs are invalid
    * @throws IOException if there are problems reading from URLs via HTTP
    */
    public DTO getFullInfo (Map parms)
            throws DBException, MalformedURLException, IOException
    {
	// URL used to retrieve minimap information for a marker
	String minimapUrl = this.config.get ("MINIMAP_URL");

	// URL used to retrieve gene family information for markers
	String geneFamiliesUrl = this.config.get ("GENE_FAMILY_MAP_URL");

        // used to read data from 'minimapUrl'
	BufferedReader minimapReader = null;

	// all data for the marker with the given 'key'
        DTO marker = DTO.getDTO();

	// data for a particular section, to merge with 'marker'
	DTO section = null;

	// marker key as a String
	String keyStr = getKey (parms);

	// if we could not find a marker key based on 'parms', then bail out
	// before bothering with anything else
	if (keyStr == null)
	{
	    this.logInfo ("Could not find marker");
	    return marker;
	}

	// marker key as an integer
	int key = Integer.parseInt (keyStr);

	// If we have a URL for getting the minimap, then go ahead and open
	// the reader now.  We'll collect the information later on, to allow
	// for processing of other sections while the minimap is being built.

	if (minimapUrl != null)
	{
            minimapReader = this.openMinimapCgi (key, minimapUrl);
	    this.timeStamp ("Opened minimap URL to start building the minimap");
        }

	// get data for individual sections.  For the sake of efficiency, we
	// make sure to always return the 'section' to the pool of available
	// DTOs once we are done with it.
	
	section = this.getBasicInfo (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved basic marker info");

        section = this.getMapPosition (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved map position");

	section = this.getSynonyms (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved synonyms");

	section = this.getNotes (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved marker notes");

	section = this.getMLCIndicator (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Determined whether an MLC record exists");

	section = this.getMappingCount (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved count of mapping experiments");

	section = this.getGXDIndexCount (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved count of GXD Index entries");

	section = this.getPhenoCount (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved count of phenotype classifications");

        section = this.getGOAnnotations (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved GO annotations");

        section = this.getMGIIDs (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved MGI IDs");

        section = this.getPolymorphismCounts (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved polymorphism counts");

        section = this.getAlleleCounts (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved allele counts");

        section = this.getAntibodyCount (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved antibody count");

        section = this.getProbeCounts (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved probe counts");

        section = this.getExpressionResultCounts (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved expression result counts");

        section = this.getExpressionAssayCounts (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved expression assay counts");

        section = this.getAliases (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved aliases");

        section = this.getcDNACount (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved cDNA source count");

        section = this.getTissueCount (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved tissue count");

        section = this.getOrthologies (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved orthologies");

        section = this.getTheilerStages (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved Theiler Stages");

        section = this.getOtherIDs (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved non-MGI, non-sequence acc IDs");

        section = this.getSeqIDs (key);
	marker.merge (section);
	DTO.putDTO (section);
	this.timeStamp ("Retrieved sequence acc IDs");

// HERE -- omitted for now:
//        section = this.getReferenceInfo (key);
//	marker.merge (section);
//	DTO.putDTO (section);
//        this.timeStamp ("Retrieved reference data");

	// collect data from the minimap reader, then close it.

	if (minimapReader != null)
	{
	    section = this.readAndCloseMinimapCgi (minimapReader);
	    marker.merge (section);
	    DTO.putDTO (section);
	    this.timeStamp ("Retrieved URL for finished minimap");
	}

	if (geneFamiliesUrl != null)
	{
	    String mgiId = (String) marker.get (DTOConstants.PrimaryAccID);

	    section = this.getGeneFamilyURL (geneFamiliesUrl, mgiId);
	    marker.merge (section);
	    DTO.putDTO (section);
	    this.timeStamp ("Retrieved URL for gene family page");
	}

	return marker;
    }

    /* -------------------------------------------------------------------- */

    /////////////////////////////////////////////////////////
    // methods to retrieve individual sections of information
    /////////////////////////////////////////////////////////

    /** retrieve basic information about the marker with the given 'key'.
    * @param key the marker key of the marker whose data we seek
    * @return DTO contains information retrieved from the database.  See
    *    notes.  If no data for the specified marker is found, then returns
    *    an empty DTO.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results.
    * @notes The following constants from DTOConstants are included as 
    *    fieldnames in the returned DTO:
    *    <OL>
    *    <LI> MarkerKey : Integer
    *    <LI> MarkerSymbol : String
    *    <LI> MarkerName : String
    *    <LI> MarkerType : String
    *    <LI> Chromosome : String
    *    <LI> DatabaseDate : String
    *    <LI> MarkerIsWithdrawn : Boolean
    *    <LI> CytogeneticOffset (optional) : String
    *    <LI> PrimaryAccID (optional) : String
    *    <LI> MarkerWithdrawnTo (optional): Vector of DTO objects.  Each
    *        defines MarkerKey and MarkerSymbol for a marker to which this one
    *        was withdrawn.
    *    </OL>
    */
    public DTO getBasicInfo (int key) throws DBException
    {
	ResultsNavigator nav = null;	// set of query results
	RowReference rr = null;		// one row in 'nav'
	DTO marker = DTO.getDTO();	// start with an empty DTO

	// get the most basic marker data.  If we find no results, then just
	// return an empty DTO.

        nav = this.sqlDM.executeQuery (
	        Sprintf.sprintf (BASIC_MARKER_DATA, key));

        if (!nav.next())
	{
	    return marker;
	}
	
	// otherwise collect the basic data fields

	rr = (RowReference) nav.getCurrent();

	marker.set (DTOConstants.MarkerKey, new Integer(key));
	marker.set (DTOConstants.MarkerSymbol, rr.getString(2));
	marker.set (DTOConstants.MarkerName, rr.getString(3));
	marker.set (DTOConstants.MarkerType, rr.getString(4));
	marker.set (DTOConstants.Chromosome, rr.getString(7));
	marker.set (DTOConstants.DatabaseDate, rr.getString(6));

	// only define the cytogenetic offset if it is non-null

	if (rr.getString(8) != null)
	{
	    marker.set (DTOConstants.CytogeneticOffset, rr.getString(8));
	}

	// If the marker is approved, then we need to get the primary MGI ID.
	// If not, then we need to get info about the markers to which it was
	// withdrawn.

	if (rr.getInt(5).intValue() == DBConstants.Marker_Approved)
	{
	    marker.set (DTOConstants.MarkerIsWithdrawn, Boolean.FALSE);
	    nav.close();

	    nav = this.sqlDM.executeQuery (
	            Sprintf.sprintf (PRIMARY_MGI_ID, key));
	    if (nav.next())
	    {
	        rr = (RowReference) nav.getCurrent();
	        marker.set (DTOConstants.PrimaryAccID, rr.getString(1));
	    }
	}
	else
	{
	    marker.set (DTOConstants.MarkerIsWithdrawn, Boolean.TRUE);
	    nav.close();

	    // one marker to which this one was withdrawn
	    DTO newMarker = null;

	    // set of all 'newMarker' objects, one per marker to which this
	    // one was withdrawn.  (may be more than one in case of splits)
	    Vector withdrawnList = new Vector();

	    nav = this.sqlDM.executeQuery (
	            Sprintf.sprintf (WITHDRAWN_TO, key));
	    while (nav.next())
	    {
		rr = (RowReference) nav.getCurrent();

	        newMarker = DTO.getDTO();
		newMarker.set (DTOConstants.MarkerKey, rr.getInt(1) );
	        newMarker.set (DTOConstants.MarkerSymbol, rr.getString(2));
		withdrawnList.add (newMarker);
	    }

	    // if we found any markers to which this one was withdrawn, then
	    // make sure to add them to the 'marker'.

	    if (withdrawnList.size() > 0)
	    {
	        marker.set (DTOConstants.MarkerWithdrawnTo, withdrawnList);
	    }
	}
	nav.close();
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** retrieve the map position (cM location) for the given marker.
    * @param key the marker key of the marker whose data we seek
    * @return DTO where the DTOConstants.MapPosition field is associated with
    *    a Float value.  If no cM location is defined for the given marker,
    *    then an empty DTO is returned.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getMapPosition (int key) throws DBException
    {
	ResultsNavigator nav = null;	// set of query results
	RowReference rr = null;		// one row in 'nav'
	DTO marker = DTO.getDTO();	// start with an empty DTO

        nav = this.sqlDM.executeQuery (Sprintf.sprintf (MAP_POSITION, key));
	
	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    marker.set (DTOConstants.MapPosition, rr.getFloat(1) );
	}
	
	nav.close();
        return marker;
    }

    /* -------------------------------------------------------------------- */

    /** retrieve any synonyms for the marker with the given 'key'.
    * @param key the marker key of the marker whose data we seek
    * @return DTO with the DTOConstants.Synonyms field mapped to a Vector of
    *    Strings, each of which is one synonym.  If there are no synonyms for
    *    the given marker, then an empty DTO is returned.
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
	DTO marker = DTO.getDTO();	// start with an empty DTO

        nav = this.sqlDM.executeQuery (Sprintf.sprintf (SYNONYMS, key));
	if (nav.next())
	{
	    // collect all synonyms returned by the query, then add them to
	    // the 'marker' DTO

	    synonyms = new Vector();
            do
	    {
		rr = (RowReference) nav.getCurrent();
	        synonyms.add (rr.getString(1));

	    } while (nav.next());

	    marker.set (DTOConstants.Synonyms, synonyms);
	}
	nav.close();
        return marker;
    }

    /* -------------------------------------------------------------------- */

    /** retrieve any notes for the marker with the given 'key'.
    * @param key the marker key of the marker whose data we seek
    * @return DTO with the DTOConstants.Notes field mapped to a String of
    *    notes.  If there are no notes for the marker with the given
    *    'key', returns an empty DTO.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getNotes (int key) throws DBException
    {
	ResultsNavigator nav = null;		    // set of query results
	RowReference rr = null;			    // one row in 'nav'
        StringBuffer notes = new StringBuffer();    // marker notes so far
	DTO marker = DTO.getDTO();		    // start with a new DTO

	// collect all note chunks in 'notes'.  (Marker notes are stored in
	// multiple 256-byte varchar fields)

        nav = this.sqlDM.executeQuery (Sprintf.sprintf (NOTES, key));
	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    notes.append (rr.getString(1));
	}
	nav.close();

	// if we didn't find any notes, then don't bother adding to 'marker'

	if (notes.length() > 0)
	{
	    marker.set (DTOConstants.Notes, notes.toString());
	}
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** determine whether the marker with the given 'key' has an MLC record.
    * @param key the marker key of the marker whose data we seek
    * @return DTO which maps DTOConstants.HasMLC to the Integer 1 if the
    *    specified marker has an MLC record.  If not, then an empty DTO is
    *    returned.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getMLCIndicator (int key) throws DBException
    {
	ResultsNavigator nav = null;		    // set of query results
	RowReference rr = null;			    // one row in 'nav'
	DTO marker = DTO.getDTO();		    // start with a new DTO

        nav = this.sqlDM.executeQuery (Sprintf.sprintf (MLC_COUNT, key));

	// if no rows are returned, or if the count returned in the row is 0,
	// then do not add the field to 'marker'

	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    if (rr.getInt(1).intValue() > 0)
	    {
	        marker.set (DTOConstants.HasMLC, new Integer(1));
	    }
	}
	nav.close();
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get a count of mapping experiments for the given marker.
    * @param key the marker key of the marker whose data we seek
    * @return DTO with the DTOConstants.MappingCount mapped to an Integer.  If
    *    there are no mapping experiments for the marker with the given 'key',
    *    returns an empty DTO.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes 
    *    This method should probably be moved into a MappingFactory which
    *    knows how to retrieve mapping data.  This method could become
    *    getMappingCountForMarker() or something similar.
    */
    public DTO getMappingCount (int key) throws DBException
    {
	ResultsNavigator nav = null;		    // set of query results
	RowReference rr = null;			    // one row in 'nav'
	DTO marker = DTO.getDTO();		    // start with a new DTO

        nav = this.sqlDM.executeQuery (Sprintf.sprintf (MAPPING_COUNT, key));
	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    marker.set (DTOConstants.MappingCount, rr.getInt(1));
	}
	nav.close();
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get a count of GXD Index entries for the marker with the given 'key'.
    * @param key the marker key of the marker whose data we seek
    * @return DTO which maps the DTOConstants.GXDIndexCount field to an
    *    Integer count.  If there are no GXD Index entries for the marker with
    *    the given 'key', an empty DTO is returned.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes
    *    This method should probably move into a GXDIndexFactory which knows
    *    how to retrieve information about GXD Index entries.  This method
    *    could become something like getGXDIndexCountForMarker() or something
    *    similar.
    */
    public DTO getGXDIndexCount (int key) throws DBException
    {
	ResultsNavigator nav = null;		    // set of query results
	RowReference rr = null;			    // one row in 'nav'
	DTO marker = DTO.getDTO();		    // start with a new DTO

        nav = this.sqlDM.executeQuery(Sprintf.sprintf (GXD_INDEX_COUNT, key));
	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    marker.set (DTOConstants.GXDIndexCount, rr.getInt(1));
	}
	nav.close();
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get a count of phenotype classifications for the given marker.
    * @param key the marker key of the marker whose data we seek
    * @return DTO which maps DTOConstants.PhenoCount to an Integer count.  If
    *    there are no phenotype classifications for the marker with
    *    the given 'key', returns 'marker' as-is.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getPhenoCount (int key) throws DBException
    {
	ResultsNavigator nav = null;    // set of query results
	int count = 0;			// pheno classifications seen so far
	DTO marker = DTO.getDTO();	// start with a new DTO

	// for this query, we simply count the rows returned

        nav = this.sqlDM.executeQuery (
	        Sprintf.sprintf (PHENO_CLASS_COUNT, key));
	while (nav.next())
	{
	    count++;
	}
	nav.close();

	if (count > 0)
	{
	    marker.set (DTOConstants.PhenoCount, new Integer(count));
	}
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get a minimal set of reference data for the given marker.
    * @param key the marker key of the marker whose data we seek
    * @return DTO see the getReferenceCount(), getFirstReference(), and
    *    getLastReference() methods to see which fieldnames are included.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes
    *    This method should probably be moved into a ReferenceFactory class
    *    which knows how to retrieve data about references.
    */
    public DTO getReferenceInfo (int key) throws DBException
    {
        DTO refCount = getReferenceCount (key);
	DTO firstRef = getFirstReference (key);
	DTO lastRef = getLastReference (key);

	// combine the DTOs to return them as one; free the extras for re-use

	refCount.merge (firstRef);
	refCount.merge (lastRef);

	DTO.putDTO (firstRef);
	DTO.putDTO (lastRef);

	return refCount;
    }

    /* -------------------------------------------------------------------- */

    /** get a count of references for the marker with the given 'key'.
    * @param key the marker key of the marker whose data we seek
    * @return DTO with DTOConstants.ReferenceCount mapped to an Integer count,
    *    if there are any references for the marker.  If not, an empty DTO is
    *    returned.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes This method should probably be moved into a ReferenceFactory
    *    class which knows how to retrieve data about references.
    */
    public DTO getReferenceCount (int key) throws DBException
    {
	ResultsNavigator nav = null;		    // set of query results
	RowReference rr = null;			    // one row in 'nav'
	DTO marker = DTO.getDTO();		    // start with a new DTO

	nav = this.sqlDM.executeQuery (
	        Sprintf.sprintf (REFERENCE_COUNT, key) );
	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    marker.set (DTOConstants.ReferenceCount, rr.getInt(1) );
	}
	nav.close();
        return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get the first reference associated with the marker with the given
    *    'key', excluding those in the <tt>PrivateRefSet</tt>.
    * @param key the marker key of the marker whose data we seek
    * @return DTO with the DTOConstants.FirstReference field mapped to a DTO
    *    which has fields as outlined in the notes below.  If there is no
    *    suitable reference, then just an empty DTO is returned.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes
    *    If defined, the FirstReference DTO contains the following fields
    *    (defined in DTOConstants):
    *    <OL>
    *    <LI> RefsKey : Integer
    *    <LI> Jnum : String
    *    <LI> Citation : String
    *    <LI> RefsTitle : String
    *    <LI> Authors : ArrayList (may be null if no authors)
    *    </OL>
    *    <P>This method should probably be moved into a ReferenceFactory class
    *    which knows how to retrieve data about references.
    */
    public DTO getFirstReference (int key) throws DBException
    {
        return handleSelectedReference (key, REFERENCE_FIRST,
	            DTOConstants.FirstReference);
    }

    /* -------------------------------------------------------------------- */

    /** same as <tt>getLastReference (int)</tt>, except that this gets the
    *    last reference and fills in DTOConstants.LastReference in the DTO
    *    returned.<P>
    *    This method should probably be moved into a ReferenceFactory class
    *    which knows how to retrieve data about references.
    */
    public DTO getLastReference (int key) throws DBException
    {
        return handleSelectedReference (key, REFERENCE_LAST,
	            DTOConstants.LastReference);
    }

    /* -------------------------------------------------------------------- */

    /** get the Gene Ontology (GO) annotations for the given marker.
    * @param key the marker key of the marker whose data we seek
    * @return DTO with the DTOConstants.GOAnnotationsCount field mapped to
    *    an Integer count and the DTOConstants.GOAnnotations field mapped to
    *    a DTO.  This DTO maps the DAG abbreviations to a Vector of DTOs, one
    *    per annotation.  These DTOs are defined in the notes below.  If
    *    there are no GO annotations for the marker with the given 'key',
    *    this method simply returns an empty DTO.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes
    *    Each included DTO defines seven fields for a GO association:
    *    <OL>
    *    <LI> GOID : String
    *    <LI> GOTerm : String
    *    <LI> IsNot : Integer
    *    <LI> TermKey : Integer
    *    <LI> EvidenceCode : String
    *    <LI> InferredFrom : String
    *    <LI> RefsKey : Integer
    *    </OL>
    */
    public DTO getGOAnnotations (int key) throws DBException
    {
	ResultsNavigator nav = null;    // set of query results
	RowReference rr = null;	        // one row in 'nav'
	DTO marker = DTO.getDTO();	// start with a new DTO

        int count = 0;			// count of all GO annotations
	String abbrev = null;		// GO ontology abbreviation - this row
	String lastAbbrev = "";		// GO ontology abbreviation - last row

	// a single GO annotation, including fields GOID, GOTerm, and IsNot
	// from the DTOConstants class
	DTO annotation = null;

	// all 'annotation' DTOs for one DAG
        Vector dagAnnotations = null;

	// top-level DTO.  Each fieldname is the abbreviation for one GO DAG.
	// The value for each fieldname is one 'dagAnnotations' Vector.
        DTO annotations = DTO.getDTO();

	// having finally defined all variables, we are now ready to go...

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (GO_ANNOTATIONS, key));
	while (nav.next())
	{
	    count++;
	    rr = (RowReference) nav.getCurrent();

	    // the abbreviation is a 'char' field rather than a 'varchar', so
	    // we need to trim any blanks here...

	    abbrev = rr.getString(1).trim();

	    // create a record for this particular annotation

            annotation = DTO.getDTO();
	    annotation.set (DTOConstants.GOTerm, rr.getString(2));
	    annotation.set (DTOConstants.GOID, rr.getString(3));
	    annotation.set (DTOConstants.EvidenceCode, rr.getString(4));
	    annotation.set (DTOConstants.InferredFrom, rr.getString(5));
	    annotation.set (DTOConstants.IsNot, rr.getInt(6) );
	    annotation.set (DTOConstants.TermKey, rr.getInt(7) );
	    annotation.set (DTOConstants.RefsKey, rr.getInt(8) );

            // if is a different DAG, then start a new list for the new DAG.

	    if (!lastAbbrev.equals(abbrev))
	    {
	        dagAnnotations = new Vector();
		dagAnnotations.add (annotation);
		annotations.set (abbrev, dagAnnotations);
		lastAbbrev = abbrev;
	    }
	    else		// just add to the current DAG's annotations
	    {
	        dagAnnotations.add (annotation);
	    }
	}
	nav.close();

	if (count > 0)
	{
	    marker.set (DTOConstants.GOAnnotationCount, new Integer(count));
	    marker.set (DTOConstants.GOAnnotations, annotations);
	}
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get all MGI IDs (including the primary) for the given marker.
    * @param key the marker key of the marker whose data we seek
    * @return DTO which maps DTOConstants.PrimaryAccID to a String which is
    *    the marker's primary accession ID, and DTOConstants.OtherMGIIDs to
    *    a Vectory of Strings, each of which is a secondary MGI ID.  If there
    *    are no MGI IDs for a marker with the given 'key', an empty DTO is
    *    returned.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getMGIIDs (int key) throws DBException
    {
	ResultsNavigator nav = null;    // set of query results
	RowReference rr = null;	        // one row in 'nav'
	Vector ids = new Vector();	// non-primary MGI IDs
	DTO marker = DTO.getDTO();	// start with a new DTO

        nav = this.sqlDM.executeQuery (Sprintf.sprintf (MGI_IDS, key));

	while (nav.next())
	{
	    // if this MGI ID is marked 'preferred', then it is the primary
	    // MGI ID for the marker.  Otherwise, it is just a secondary ID.

	    rr = (RowReference) nav.getCurrent();
	    if (rr.getInt(2).intValue() == 1)		// is preferred?
	    {
	        marker.set (DTOConstants.PrimaryAccID, rr.getString(1));
	    }
	    else				// non-preferred MGI ID
	    {
	        ids.add (rr.getString(1));
	    }
	}
	nav.close();

	if (ids.size() > 0)
	{
	    marker.set (DTOConstants.OtherMGIIDs, ids);
	}
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get counts of polymorphisms involving the given marker.
    * @param key the marker key of the marker whose data we seek
    * @return DTO which maps DTOConstants.PcrCount to an Integer count and
    *    DTOConstants.RflpCount to an Integer count.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes This method should probably be moved into a PolymorphismFactory
    *    class which knows how to retrieve data about polymorphisms.
    */
    public DTO getPolymorphismCounts (int key) throws DBException
    {
	ResultsNavigator nav = null;    // set of query results
	RowReference rr = null;	        // one row in 'nav'
	DTO marker = DTO.getDTO();	// start with a new DTO

	// counts of the two polymorphism types...

        int pcr = 0;			// DNA type == primer
	int rflp = 0;   		// DNA type != primer

	nav = sqlDM.executeQuery (Sprintf.sprintf (POLYMORPHISM_COUNTS, key));
	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    if ("primer".equals (rr.getString(1)))
	    {
	        pcr = pcr + rr.getInt(2).intValue();
	    }
	    else
	    {
	        rflp = rflp + rr.getInt(2).intValue();
	    }
	}
	nav.close();

	// always set these fields, even if the counts are 0

	marker.set (DTOConstants.PcrCount, new Integer(pcr));
	marker.set (DTOConstants.RflpCount, new Integer(rflp));
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get counts of various allele types for the given marker.  Wild-type
    *    alleles are excluded from the counts.
    * @param key the marker key of the marker whose data we seek
    * @return DTO which maps DTOConstants.AlleleCounts to a DTO.  That DTO has
    *    lowercase allele types as keys, each mapped to an Integer count.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes This method should probably be moved into a AllelesFactory
    *    class which knows how to retrieve data about alleles.
    */
    public DTO getAlleleCounts (int key) throws DBException
    {
	ResultsNavigator nav = null;    	// set of query results
	RowReference rr = null;	        	// one row in 'nav'
	DTO marker = DTO.getDTO();		// start with a new DTO
        DTO alleleCounts = DTO.getDTO();	// as described in notes above

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (ALLELE_COUNTS, key));
	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    alleleCounts.set (rr.getString(1).toLowerCase(), rr.getInt(2) );
	}
	nav.close();

	// always define this field, even if no allele counts

	marker.set (DTOConstants.AlleleCounts, alleleCounts);
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get a count of antibodies associated with the given marker.
    * @param key the marker key of the marker whose data we seek
    * @return DTO which maps DTOConstants.AntibodyCount to an Integer.  If the
    *    marker has no associated antibodies, then this DTO will be empty.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes This method should probably be moved into an ExpressionFactory
    *    which knows how to retrieve expression-related data.
    */
    public DTO getAntibodyCount (int key) throws DBException
    {
	ResultsNavigator nav = null;    	// set of query results
	RowReference rr = null;	        	// one row in 'nav'
	DTO marker = DTO.getDTO();		// start with a new DTO

	nav = sqlDM.executeQuery (Sprintf.sprintf (ANTIBODY_COUNT, key) );
        if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    marker.set(DTOConstants.AntibodyCount, rr.getInt(1));
	}
	nav.close();

        return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get counts of various probe types for the given marker.
    * @param key the marker key of the marker whose data we seek
    * @return DTO which maps DTOConstants.ProbeCounts to a DTO.  That DTO has
    *    probe types as keys, each mapped to an Integer count.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes This method should probably be moved into an ProbeFactory
    *    which knows how to retrieve probe data.
    */
    public DTO getProbeCounts (int key) throws DBException
    {
	ResultsNavigator nav = null;    	// set of query results
	RowReference rr = null;	        	// one row in 'nav'
        DTO probeCounts = DTO.getDTO();		// as defined in notes above
	DTO marker = DTO.getDTO();		// start with a new DTO

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (PROBE_COUNTS, key));
	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    probeCounts.set (rr.getString(1), rr.getInt(2));
	}
	nav.close();

	// always set this field, even if probe counts are 0

	marker.set (DTOConstants.ProbeCounts, probeCounts);
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get counts of expression results for the marker with the given 'key',
    *    grouped by assay type.
    * @param key the marker key of the marker whose data we seek
    * @return DTO which maps DTOConstants.ExpressionResultCounts to a DTO.
    *    That DTO has assay types as keys, each mapped to a DTO.  Each of
    *    those inner-most DTOs defines two fields:  DTOConstants.AssayTypeKey
    *    maps to an Integer key, and DTOConstants.ExpressionResultCount maps
    *    to an Integer count.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes This method should probably be moved into an ExpressionFactory
    *    which knows how to retrieve expression-related data.
    */
    public DTO getExpressionResultCounts (int key) throws DBException
    {
	ResultsNavigator nav = null;   	// set of query results
	RowReference rr = null;	       	// one row in 'nav'
	DTO marker = DTO.getDTO();	// start with a new DTO
	DTO assayTypes = DTO.getDTO();	// main DTO, as defined above in notes
	DTO typeInfo = null;		// sub DTO, as defined above in notes

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (
	                                EXPRESSION_RESULT_COUNTS, key) );
	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    // remember the key and count for this assay type...

	    typeInfo = DTO.getDTO();
	    typeInfo.set (DTOConstants.AssayTypeKey, rr.getInt(1) );
            typeInfo.set (DTOConstants.ExpressionResultCount, rr.getInt(2) );

	    // and add it using the assay type string

	    assayTypes.set (rr.getString(3), typeInfo);
	}
	nav.close();

	marker.set (DTOConstants.ExpressionResultCounts, assayTypes);
        return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get counts of expression assays for the marker with the given 'key',
    *    grouped by assay type.
    * @param key the marker key of the marker whose data we seek
    * @return DTO which maps DTOConstants.ExpressionAssayCounts to a DTO.
    *    That DTO has assay types as keys, each mapped to a DTO.  Each of
    *    those inner-most DTOs defines two fields:  DTOConstants.AssayTypeKey
    *    maps to an Integer key, and DTOConstants.ExpressionAssayCount maps
    *    to an Integer count.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes This method should probably be moved into an ExpressionFactory
    *    which knows how to retrieve expression-related data.
    */
    public DTO getExpressionAssayCounts (int key) throws DBException
    {
	ResultsNavigator nav = null;   	// set of query results
	RowReference rr = null;	       	// one row in 'nav'
	DTO marker = DTO.getDTO();	// start with a new DTO
	DTO assayTypes = DTO.getDTO();	// main DTO, as defined above in notes
	DTO typeInfo = null;		// sub DTO, as defined above in notes

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (
	                                EXPRESSION_ASSAY_COUNTS, key) );
	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    // remember the key and count for this assay type...

	    typeInfo = DTO.getDTO();
	    typeInfo.set (DTOConstants.AssayTypeKey, rr.getInt(1) );
            typeInfo.set (DTOConstants.ExpressionAssayCount, rr.getInt(2) );

	    // and add it using the assay type string

	    assayTypes.set (rr.getString(3), typeInfo);
	}
	nav.close();

	marker.set (DTOConstants.ExpressionAssayCounts, assayTypes);
        return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get the aliases for the marker with the given 'key'.
    * @param key the marker key of the marker whose data we seek
    * @return DTO maps DTOConstants.Aliases to a Vector of DTOs.  Each of
    *    those DTOs defines two fields:  DTOConstants.MarkerKey maps to an
    *    Integer key, and DTOConstants.MarkerSymbol maps to a String symbol.
    *    If the given marker has no aliases, the returned DTO is empty.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getAliases (int key)
            throws DBException
    {
	ResultsNavigator nav = null;   	// set of query results
	RowReference rr = null;	       	// one row in 'nav'
	DTO marker = DTO.getDTO();	// start with a new DTO
	DTO alias = null;		// one alias for given marker
	Vector aliases = new Vector();	// all aliases for the marker

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (ALIASES, key));
	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    // we need the key and symbol for each alias

	    alias = DTO.getDTO();
	    alias.set (DTOConstants.MarkerKey, rr.getInt(1));
	    alias.set (DTOConstants.MarkerSymbol, rr.getString(2));

	    aliases.add (alias);
	}
	nav.close();

	if (aliases.size() > 0)
	{
	    marker.set (DTOConstants.Aliases, aliases);
	}
        return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get a count of cDNAs associated with the marker with the given 'key'.
    *    We only count those associated with an "encodes" or "putative"
    *    relationship.
    * @param key the marker key of the marker whose data we seek
    * @return DTO maps DTOConstants.cDNACount to the Integer count.  If
    *    there are no cDNAs associated with the given marker, an emtpy DTO
    *    is returned.
    * @assumes nothing
    * @effects queries the database using 'sqlDM'
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes This method should probably be moved into an ExpressionFactory
    *    or ProbeFactory which knows how to retrieve expression-related data.
    *    (cDNAs are in both the probe and expression camps)
    */
    public DTO getcDNACount (int key) throws DBException
    {
	ResultsNavigator nav = null;   	// set of query results
	RowReference rr = null;	       	// one row in 'nav'
	DTO marker = DTO.getDTO();	// start with a new DTO

        nav = this.sqlDM.executeQuery (Sprintf.sprintf (CDNA_COUNT, key));
	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    marker.set (DTOConstants.cDNACount, rr.getInt(1));
	}
	nav.close();

	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get a count of tissues from expression results associated with the
    *    marker with the given 'key'.
    * @param key the marker key of the marker whose data we seek
    * @return DTO maps DTOConstants.TissueCount to an Integer count.  If there
    *    are no associated tissues, then an empty DTO is returned.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes This method should probably be moved into an ExpressionFactory
    *    which knows how to retrieve expression-related data.
    */
    public DTO getTissueCount (int key) throws DBException
    {
	ResultsNavigator nav = null;   	// set of query results
	RowReference rr = null;	       	// one row in 'nav'
	DTO marker = DTO.getDTO();	// start with a new DTO

        nav = this.sqlDM.executeQuery (
	         Sprintf.sprintf (EXPRESSION_TISSUES, key));
	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    marker.set (DTOConstants.TissueCount, rr.getInt(1));
	}
	nav.close();

	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get orthology data related to the marker with the given 'key'.
    * @param key the marker key of the marker whose data we seek
    * @return DTO which maps two fields:  DTOConstants.HasHumanOrthology maps
    *    to a Boolean TRUE if the marker has a human ortholog (and is absent
    *    if not); and DTOConstants.OrthologousSpecies maps to a Vector of
    *    Strings, each of which is a species with a marker orthologous to the
    *    given marker.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getOrthologies (int key) throws DBException
    {
	ResultsNavigator nav = null;		// set of query results
	RowReference rr = null;	   	   	// one row in 'nav'
	Vector orthologousSpecies = null;	// has one String per species
	DTO marker = DTO.getDTO();		// start with a new DTO

        nav = this.sqlDM.executeQuery (
	        Sprintf.sprintf (ORTHOLOGOUS_SPECIES, key));
	if (nav.next())
	{
	    orthologousSpecies = new Vector();
	    do
	    {
		// add this orthologous species to the list we're compiling

		rr = (RowReference) nav.getCurrent();
	        orthologousSpecies.add (rr.getString(1));

		// for human orthologies, we also need to set a special flag

		if (rr.getInt(3).intValue() == DBConstants.Species_Human)
		{
		    marker.set (DTOConstants.HasHumanOrthology, Boolean.TRUE);
		}
	    } while (nav.next());

	    marker.set (DTOConstants.OrthologousSpecies, orthologousSpecies);
	}
	nav.close();
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get the Theiler Stages where we have expression data for the marker
    *    with the given 'key'.
    * @param key the marker key of the marker whose data we seek
    * @return DTO maps DTOConstants.TheilerStages to a Vector of DTOs.  Each
    *    of those DTOs defines two fields:  DTOConstants.Stage is an Integer
    *    stage number, and DTOConstants.StageKey is an Integer stage key.  If
    *    there are no stages associated, then an empty DTO is returned.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes This method should probably be moved into an ExpressionFactory
    *    which knows how to retrieve expression-related data.
    */
    public DTO getTheilerStages (int key) throws DBException
    {
	ResultsNavigator nav = null;	// set of query results
	RowReference rr = null;	   	// one row in 'nav'
	DTO marker = DTO.getDTO();	// start with a new DTO
	DTO stage = null;		// data for one stage: Stage, StageKey
	Vector stages = null;		// all 'stage' DTOs for this marker

	nav = this.sqlDM.executeQuery (
	        Sprintf.sprintf (EXPRESSION_STAGES, key));
	if (nav.next())
	{
	    stages = new Vector();
	    do
	    {
		// for each row returned, collect its stage data in a DTO and
		// add it to the list of DTOs we're building

		rr = (RowReference) nav.getCurrent();

	        stage = DTO.getDTO();
	        stage.set (DTOConstants.Stage, rr.getInt(1));
	        stage.set (DTOConstants.StageKey, rr.getInt(2));

	        stages.add (stage);

	    } while (nav.next());

	    marker.set (DTOConstants.TheilerStages, stages);
	}
	nav.close();
        return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get "other accession IDs" for the marker with the given 'key'.  These
    *    IDs are not from MGI, RefSeq, sequence databases, or Swiss-prot.
    * @param key the marker key of the marker whose data we seek
    * @return DTO maps DTOConstants.OtherIDs to a DTO.  That DTO maps logical
    *    database names to DTOs.  Each of those DTOs defines three fields:
    *    DTOConstants.LogicalDbKey (an Integer), DTOConstants.URL (a String),
    *    DTOConstants.AccIDs (a Vector of Strings, each of which is an
    *    accession ID).  If there are no "other accession IDs", then an empty
    *    DTO is returned.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    */
    public DTO getOtherIDs (int key) throws DBException
    {
	ResultsNavigator nav = null;	// set of query results
	RowReference rr = null;	   	// one row in 'nav'

	DTO marker = DTO.getDTO();	// start with a new DTO
        DTO allLdbs = null;		// main DTO as defined above in notes
	DTO thisLdb = null;		// sub DTO as defined above in notes
	Vector ids = null;		// all accIDs for one 'thisLdb'

	int prevLdbKey = -1;		// key of previous logical database
	int ldbKey = -1;		// key of this logical database
	String ldbName = null;		// name of this logical database
	
	nav = this.sqlDM.executeQuery (Sprintf.sprintf (ACC_IDS, key));
	if (nav.next())
	{
	    allLdbs = DTO.getDTO();
	    do
	    {
		rr = (RowReference) nav.getCurrent();

		// We assume that the query sorts rows from the same logical
		// database together.  If this row is from a logical database
		// different from the previous one, then we need to set up
		// data structures to capture its information.

                ldbKey = rr.getInt(4).intValue();
		if (ldbKey != prevLdbKey)
		{
		    ids = new Vector();

		    // capture basic info for this logical database

		    thisLdb = DTO.getDTO();
		    thisLdb.set (DTOConstants.URL, rr.getString(3));
		    thisLdb.set (DTOConstants.LogicalDbKey,
		        new Integer(ldbKey));
		    thisLdb.set (DTOConstants.AccIDs, ids);

		    // and associate that info with the name of the logical db

		    ldbName = rr.getString(2).trim();
                    allLdbs.set (ldbName, thisLdb);

		    // finally, note that we're now working with this
		    // logical db

		    prevLdbKey = ldbKey;
		}

		// 'ids' refers to the Vector of accession IDs for the current
		// logical database, so just add this ID to it

	        ids.add (rr.getString(1));		// acc ID for this row

	    } while (nav.next());
	    marker.set (DTOConstants.OtherIDs, allLdbs);
	}
        nav.close();
        return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get "sequence IDs" associated with the marker with the given 'key'.
    * @param key the marker key of the marker whose data we seek
    * @return DTO which defines two fields:  DTOConstants.ProteinSeqIDs and
    *    DTOConstants.NucleotideSeqIDs, each of which maps to a Vector of
    *    DTOs.  The contents of each of those DTOs is defined in the notes
    *    section.  If the marker has no associated seq IDs, then an empty
    *    DTO is returned.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes
    *    Each DTO contained in one of the Vectors above defines three fields:
    *    DTOConstants.LogicalDbKey (an Integer), DTOConstants.URL (a String),
    *    and DTOConstants.SeqID (a String).
    *    <P>This method should probably be moved into an SequenceFactory
    *    which knows how to retrieve sequence data.
    */
    public DTO getSeqIDs (int key) throws DBException
    {
	DTO marker = DTO.getDTO();	// start with a new DTO

	ResultsNavigator nav = null;	// set of query results
	RowReference rr = null;	   	// one row in 'nav'

	Vector nucSeqs = null;		// nucleotide sequences; preferred at
					// the beginning of the 'nucSeqs'

	Vector proSeqs = null;		// protein sequences; preferred at the
					// beginning of 'proSeqs'

	DTO seqInfo = null;		// seq ID, logical db key, URL for
					// the current sequence

	int ldbKey = -1;		// logical database key for this seqID

	String id = null;		// the seq ID we are examining

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (SEQ_IDS, key));
	if (nav.next())
	{
	    nucSeqs = new Vector();
	    proSeqs = new Vector();

	    do
	    {
	        rr = (RowReference) nav.getCurrent();
		
		ldbKey = rr.getInt(4).intValue();

		// collect data for this sequence

		id = rr.getString(1);

		seqInfo = DTO.getDTO();
		seqInfo.set (DTOConstants.SeqID, id);
		seqInfo.set (DTOConstants.LogicalDbKey, new Integer (ldbKey));
		seqInfo.set (DTOConstants.URL, rr.getString(3));

		// Determine whether this is a protein or nucleotide sequence
		// and whether or not it is preferred.  Preferred IDs go to
		// the beginning of their respective Vectors.  Non-preferred
		// IDs go to the end.

                switch (ldbKey)
		{
		    case DBConstants.LogicalDB_SequenceDB:
		    case DBConstants.LogicalDB_DoTS:
		    case DBConstants.LogicalDB_TIGR:
			    // non-preferred nucleotide sequence ID --> end
			    nucSeqs.add (seqInfo);
		            break;

		    case DBConstants.LogicalDB_RefSeq:
			    // preferred nucleotide sequence ID --> beginning
			    nucSeqs.add (0, seqInfo);
		            break;

		    case DBConstants.LogicalDB_TrEMBL:
			    // non-preferred protein sequence ID --> end
			    proSeqs.add (seqInfo);
		            break;

		    case DBConstants.LogicalDB_SwissProt:
		            // Swiss-Prot defines both non-preferred and
			    // preferred protein IDs, so we need to determine
			    // which this is...
			    if (isPreferredProteinID (id))
			    {
				proSeqs.add (0, seqInfo);    // --> end
			    }
			    else
			    {
				proSeqs.add (seqInfo);	     // --> beginning
			    }
		            break;
		}

	    } while (nav.next());

	    // finally add the completed Vectors to the 'marker' DTO

	    marker.set (DTOConstants.ProteinSeqIDs, proSeqs);
	    marker.set (DTOConstants.NucleotideSeqIDs, nucSeqs);
	}
	nav.close();
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get the URL to a minimap for the marker with the given 'key'
    * @param key the marker key of the marker whose data we seek
    * @param url the non-null URL which can be used to generate a marker's
    *    minimap and which will return a URL to that minimap image
    * @return DTO which maps the DTOConstants.MinimapURL field to a String
    *    value -- the URL to the minimap image.  If there is no minimap for
    *    the given marker, an empty DTO is returned.
    * @assumes the minimap url (in this.config) is formed such that 'key' may
    *    be appended to it to specify the marker in which we are interested
    * @effects opens, reads from, and closes a connection to 'url' via HTTP
    * @throws MalformedURLException if there is a problem with 'url'
    * @throws IOException if we cannot open, read from, or close a connection
    *    to 'url'
    */
    public DTO getMinimapURL (int key, String url)
            throws MalformedURLException, IOException
    {
        BufferedReader minimapReader = openMinimapCgi (key, url);
	DTO marker = readAndCloseMinimapCgi (minimapReader);
	return marker;
    }

    /* -------------------------------------------------------------------- */

    /** get the URL for the gene family map associated with the given marker.
    * @param url the URL to the script/page which provides the gene family
    *    mappings for markers
    * @param mgiId the primary MGI ID of this number.
    * @return DTO which maps DTOConstants.GeneFamilyURL to a String, which is
    *    the URL to the marker's gene family page.  If the marker is not
    *    associated with a gene family page, then an empty DTO is returned.
    * @assumes nothing
    * @effects reaches out to 'url' via HTTP, if needed
    * @throws nothing
    */
    public DTO getGeneFamilyURL (String url, String mgiId)
    {
	DTO marker = DTO.getDTO();	// start with a new DTO

	// the URL to the gene family page for this marker
	String result = null;

        if (mgiId != null)
	{
	    GeneFamilyMap gfm = this.getGeneFamilyMap (url);
	    if (gfm != null)
	    {
	        result = gfm.getUrl (mgiId);

	        if (result != null)
	        {
	            marker.set (DTOConstants.GeneFamilyURL, result);
	        }
	    }
	}
	return marker;
    }

    /* -------------------------------------------------------------------- */

    ///////////////////////////
    // private instance methods
    ///////////////////////////

    /** gets the GeneFamilyMap corresponding to the given 'url'.
    * @param url the web-accessible script or page which yields the mappings
    *    from gene accession IDs to the URLs of their corresponding gene
    *    family pages
    * @assumes nothing
    * @effects reads via HTTP from 'url'; adds entry to shared object cache
    * @throws nothing
    */
    private GeneFamilyMap getGeneFamilyMap (String url)
    {
	// unique key for storing/retrieving url's GeneFamilyMap object from
	// the shared cache of objects
        String gfmKey = "org.jax.mgi.shr.datafactory.MarkerFactory." + url;

	// cache of already-generated objects
	ExpiringObjectCache cache = ExpiringObjectCache.getSharedCache();

	// attempt to get an already-instantiated GeneFamilyMap from the
	// cache of objects.  If successful, then we can just return it.

	GeneFamilyMap gfm = (GeneFamilyMap) cache.get (gfmKey);

	if (gfm != null)
	{
	    return gfm;
	}

	// otherwise, we need to build the GeneFamilyMap afresh and store it
	// in the cache.  Since the gene family map data changes so
	// infrequently, a four hour timeout is more than adequate.

	gfm = new GeneFamilyMap (url);
	cache.put (gfmKey, gfm, 4 * 60 * 60);
	return gfm;
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
        String prsKey = "org.jax.mgi.shr.datafactory.MarkerFactory.PRS";

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
	return prs;
    }

    /* -------------------------------------------------------------------- */

    /** fill 'key' into the given 'sql'; execute the resulting 'sql' query;
    *    pick the first reference returned that is acceptable for public
    *    display and return its data in a DTO as the given 'fieldname'.
    * @param key the marker key of the marker whose data we seek
    * @param sql the SQL statement to be used to retrieve references, with one
    *    field to be filled in by 'key'
    * @param fieldname specifies what field should be defined in the DTO that
    *    is to be returned
    * @return DTO maps the given 'fieldname' to a DTO.  That DTO's fields are
    *    defined in makeReferenceDTO().  If no reference exists, then an empty
    *    DTO is returned.
    * @assumes that 'sql' returns fields required by makeReferenceDTO(), which
    *    receives one row of results from the query
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results
    * @notes Fields defined in the reference's DTO are detailed in the
    *    comments for makeReferenceDTO().
    */
    private DTO handleSelectedReference (int key,String sql, String fieldname)
            throws DBException
    {
	ResultsNavigator nav = null;	// set of query results
	RowReference rr = null;	   	// current row in 'nav'
	boolean rowExists = false;	// has an acceptable row been found?
	DTO marker = DTO.getDTO();	// start with a new DTO

	// set of de-emphasized references (not to be highlighted on the
	// marker detail page)
	PrivateRefSet refSet = getPrivateRefSet ();

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (sql, key) );

	// if at least one reference was returned by the query, then we need
	// to walk through them to find the first one that is not in the
	// set of references to de-emphasize.  (The set contains those which
	// should NOT appear on a marker detail page.)

	if (nav.next())
	{
	    rowExists = true;	// assume this row is acceptable

	    // if the set of internal-only references contains the one from
	    // this row, then we need to move on to check the next row

	    rr = (RowReference) nav.getCurrent();
	    while (rowExists && refSet.contains (rr.getString(2)))
	    {
	        rowExists = nav.next();
	    }
	}

	// if we have a valid row at this point, then we need to add it

        if (rowExists)
	{
	    marker.set (fieldname, this.makeReferenceDTO(rr) );
	}
	nav.close();
        return marker;
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

    /** make a request of 'url' to get the URL of a minimap for the marker
    *    with the given 'key'.
    * @param key the marker key of the marker whose data we seek
    * @param url the URL to a script or servlet that will generate a minimap
    *    and return the URL to a web-accessible image of it
    * @return BufferedReader that can be used to read the resulting URL
    * @assumes 'url' is formed such that 'key' may be appended to it to
    *    specify the marker in which we are interested
    * @effects opens a connection to 'url' via HTTP
    * @throws MalformedURLException if there is a problem with 'url'
    * @throws IOException if we cannot open a connection to 'url' and
    *    establish a reader for it
    */
    private BufferedReader openMinimapCgi (int key, String url)
            throws MalformedURLException, IOException
    {
        URL minimapURL = new URL (url + key);
	BufferedReader minimapReader = new BufferedReader (
	                                   new InputStreamReader (
					       minimapURL.openStream() ));
        return minimapReader;
    }

    /* -------------------------------------------------------------------- */

    /** get the minimap's URL from 'reader' and return it in a DTO.  Then
    *   close the 'reader'.
    * @param reader as established by 'openMinimapCgi()' method
    * @return DTO which defines the DTOConstants.MinimapURL field to be a
    *    String (the URL to the minimap).  If no minimap exists for the
    *    marker, returns an empty DTO.
    * @assumes nothing
    * @effects reads from 'reader' and closes it
    * @throws IOException if there are problems reading from 'reader' or
    *    closing it
    */
    private DTO readAndCloseMinimapCgi (BufferedReader reader)
            throws IOException
    {
        DTO marker = DTO.getDTO();		// start with a new DTO

        String result = reader.readLine();
	reader.close();

	// if a marker has no map location, then the minimap CGI should
	// return "None".

	if ("None".equals(result))
	{
	    return marker;
	}

	// if there was a problem creating the minimap, then the minimap CGI
	// should return an error string.

	if ("ERROR".equals (result.substring (0,5)))
	{
	    return marker;
	}

	marker.set (DTOConstants.MinimapURL, result);
	return marker;
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

    /** add the given 'entry' to the TimeStamper in this MarkerFactory, if
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

    /** determine if 'id' is a preferred protein sequence ID.  Preferred IDs
    *    are those we want to display and link primarily.  Non-preferred IDs
    *    are to be displayed less prominently.
    * @param id a protein sequence ID
    * @return boolean true if it is preferred, false if not
    * @assumes 'id' is not null; all letters in 'id' are uppercase
    * @effects nothing
    * @throws nothing
    * @notes A preferred protein ID will begin with a single letter and then
    *    have one or more digits.
    */
    private static boolean isPreferredProteinID (String id)
    {
        char c = ' ';			// current character being examined
	int idLen = id.length();	// count of characters in 'id'

	// A preferred ID cannot have less than two characters (one letter and
	// one digit, so bail out if less.

	if (idLen < 2)
	{
	    return false;	// must have one letter, 1+ digits
	}

	// if the first character is not a letter, then bail out now...

	c = id.charAt(0);
	if ((c < 'A') || (c > 'Z'))
	{
	    return false;
	}

	// now we need to ensure that all other characters are numeric

	for (int i = 1; i < idLen; i++)
	{
	    c = id.charAt(i);
	    if ((c < '0') || (c > '9'))
	    {
	        return false;
	    }
	}
	return true;
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

    // get all accession IDs associated with a marker
    // fill in:  marker key (int)
    private static final String ACC_IDS = 
		"select a.accID, ldb.name, adb.url, ldb._LogicalDB_key "
		+ "from ACC_Accession a, ACC_LogicalDB ldb, ACC_ActualDB adb "
		+ "where a._LogicalDB_key = ldb._LogicalDB_key "
		+    "and ldb._LogicalDB_key = adb._LogicalDB_key "
		+    "and a._Object_key = %d "
		+    "and a.private = 0 "
		+    "and a._MGIType_key = " + DBConstants.MGIType_Marker
		+    " and a._LogicalDB_key not in (" +
			    DBConstants.LogicalDB_MGI + ", " +
			    DBConstants.LogicalDB_RefSeq + ", " +
			    DBConstants.LogicalDB_SwissProt + ", " +
			    DBConstants.LogicalDB_SequenceDB + ") "
		+ " order by ldb.name, a.accID";

    // get aliases
    // fill in: marker key (int)
    private static final String ALIASES =
		"select _Alias_key, alias"
		+ " from MRK_Alias_View"
		+ " where _Marker_key = %d"
		+ " order by alias";

    // get counts of the marker's alleles for each type of allele, excluding
    //	wild types
    // fill in: marker key (int)
    private static final String ALLELE_COUNTS =
		"select ty.alleleType, count(1) "
		+ "from ALL_Allele aa, ALL_Type ty "
		+ "where aa._Marker_key = %d "
		+    "and aa._Allele_Type_key = ty._Allele_Type_key "
		+    "and aa.name != 'wild type' "
		+ "group by ty.alleleType";

    // get a count of the antibodies associated with the marker
    // fill in: marker key (int)
    private static final String ANTIBODY_COUNT =
		"select count (distinct _Antibody_key) "
		+ "from GXD_AntibodyMarker "
		+ "where _Marker_key = %d";

    // get the basic information for page header for marker info.  for use by
    // the getBasicMarkerInfo() method.
    // fill in:  marker key (int)
    protected static final String BASIC_MARKER_DATA =
    		"select m._Marker_key, m.symbol, m.name, mt.name, "
		+    "m._Marker_Status_key, "
		+    "convert(varchar, db.lastdump_date, 101), "
		+    "m.chromosome, m.cytogeneticOffset"
		+ " from MRK_Marker m"
		+    ", MRK_Types mt"
		+    ", MGI_dbInfo db"
		+ " where m._Marker_key = %d"
		+    " and m._Marker_Type_key = mt._Marker_Type_key";

    // get the basic information for page header for marker info
    // (symbol, name, accID for a given marker key).  Used by other Marker*
    // servlets.
    // fill in:  marker key (int)
    protected static final String BASIC_MARKER_INFO =
    		"select m.symbol, m.name, a.accID, mt.name, "
		+    "convert(varchar, db.lastdump_date, 101)"
		+ " from MRK_Marker m"
		+    ", ACC_Accession a"
		+    ", MRK_Types mt"
		+    ", MGI_dbInfo db"
		+ " where m._Marker_key = %d"
		+    " and m._Marker_key = a._Object_key"
		+    " and m._Marker_Type_key = mt._Marker_Type_key"
		+    " and a._MGIType_key = " + DBConstants.MGIType_Marker
		+    " and a.preferred = 1"
		+    " and a.private = 0"
		+    " and a._LogicalDB_key = " + DBConstants.LogicalDB_MGI;

    // get a count of cDNA sources
    // fill in:  marker key (int)
    private static final String CDNA_COUNT = 
		"select count (distinct pp._Probe_key)"
		+ " from PRB_Probe pp,"
		+ "    PRB_Source ps,"
		+ "    PRB_Marker pm"
		+ " where pp.DNAType = 'cDNA'"
		+ "    and pp._Source_key = ps._Source_key"
		+ "    and ps._ProbeSpecies_key = 1"
		+ "    and pp._Probe_key = pm._Probe_key"
		+ "    and pm.relationship in ('E', 'P')"
		+ "    and pm._Marker_key = %d"
		+ "    and ( (ps.cellLine is null) )";

    // get a count of expression assays for the marker by assay type
    // fill in: marker key (int)
    private static final String EXPRESSION_ASSAY_COUNTS =
		"select gat._AssayType_key,"
		+ "    count (distinct ge._Assay_key),"
		+ "    gat.assayType"
		+ " from GXD_Expression ge,"
		+ "    GXD_AssayType gat"
		+ " where _Marker_key = %d"
		+ "    and ge._AssayType_key = gat._AssayType_key"
		+ " group by assayType";

    // get a count of expression results for the marker by assay type
    // fill in: marker key (int)
    private static final String EXPRESSION_RESULT_COUNTS =
		"select gat._AssayType_key,"
		+ "    count (distinct ge._Expression_key),"
		+ "    gat.assayType"
		+ " from GXD_Expression ge,"
		+ "    GXD_AssayType gat"
		+ " where _Marker_key = %d"
		+ "    and ge._AssayType_key = gat._AssayType_key"
		+ " group by assayType";

    // get a count of tissues for the marker
    // fill in: marker key (int)
    private static final String EXPRESSION_STAGES =
		"select distinct ts.stage, ts._Stage_key"
		+ " from GXD_Expression ge,"
		+ "    GXD_Structure gs,"
		+ "    GXD_TheilerStage ts"
		+ " where ge._Marker_key = %d"
		+ "    and ge._Structure_key = gs._Structure_key"
		+ "    and gs._Stage_key = ts._Stage_key"
		+ " order by stage";

    // get a count of tissues for the marker
    // fill in: marker key (int)
    private static final String EXPRESSION_TISSUES =
		"select count (distinct ge._Structure_key)"
		+ " from GXD_Expression ge"
		+ " where ge._Marker_key = %d";

    // retrieve unique set of GO annotations.  uniqueness is defined by having
    // the same term, the same evidence, and the same 'inferred from' field.
    // fill in:  marker key (int)
    private static final String GO_ANNOTATIONS =
    		"select distinct dd.abbreviation, "
		+    "vt1.term, acc.accID, "
		+    "evidenceCode = vt2.abbreviation, ve.inferredFrom, "
		+    "va.isNot, va._Term_key, ve._Refs_key "
		+ "from VOC_Term vt1, "
		+    "ACC_Accession acc, "
		+    "VOC_Evidence ve, "
		+    "VOC_Term vt2, "
		+    "DAG_DAG dd, "
		+    "DAG_Node dn, "
		+    "VOC_Annot va, "
		+    "VOC_GOMarker_AnnotType_View gm "
		+ "where va._Object_key = %s "
		+    "and va._AnnotType_key = gm._AnnotType_key "
		+    "and va._Term_key = vt1._Term_key "
		+    "and vt1._Term_key = acc._Object_key "
		+    "and acc._MGIType_key = " + DBConstants.MGIType_VocTerm
		+    " and acc.preferred = 1 "
		+    "and va._Annot_key = ve._Annot_key "
		+    "and ve._EvidenceTerm_key = vt2._Term_key "
		+    "and va._Term_key = dn._Object_key "
		+    "and dn._DAG_key = dd._DAG_key "
		+    "and dd._MGIType_key = " + DBConstants.MGIType_VocTerm
		+ " order by dd.abbreviation, vt1.term";

    // count GXD index entries which are associated with the marker
    // fill in: marker key (int)
    private static final String GXD_INDEX_COUNT =
		"select count(1) "
		+ "from GXD_Index "
		+ "where _Marker_key = %d";

    // find the marker key associated with the given accession id.
    // fill in: accession ID (String)
    private static final String KEY_FOR_ID =
		"select _Object_key "
		+ "from ACC_Accession "
		+ "where _MGIType_key = " + DBConstants.MGIType_Marker + " "
		+    "and accID = '%s'";
    
    // find the marker key associated with the given mouse symbol.
    // fill in: marker symbol (String)
    private static final String KEY_FOR_SYMBOL =
		"select _Marker_key, _Marker_Status_key "
		+ "from MRK_Marker "
		+ "where _Species_key = " + DBConstants.Species_Mouse + " "
		+    "and symbol = '%s'";
    
    // get MGI map position (in centimorgans)
    // fill in: marker key (int)
    private static final String MAP_POSITION =
       		"select offset "
		+ "from MRK_Offset "
		+ "where _Marker_key = %d "
		+    "and source = " + DBConstants.OffsetSource_MGI;
	
    // determine how many mapping experiments are associated with the marker
    // fill in: marker key (int)
    private static final String MAPPING_COUNT =
		"select count (distinct _Expt_key) "
		+ "from MLD_Expt_Marker "
		+ "where _Marker_key = %d";

    // get all MGI accession IDs associated with a marker
    // fill in:  marker key (int)
    private static final String MGI_IDS = 
		"select a.accID, a.preferred "
		+ "from ACC_Accession a "
		+ "where a._LogicalDB_key = " + DBConstants.LogicalDB_MGI
		+    "and a._Object_key = %d "
		+    "and a._MGIType_key = " + DBConstants.MGIType_Marker
		+ " order by a.accID";

    // determine if the marker has an MLC text record
    // fill in: marker key (int)
    private static final String MLC_COUNT =
		"select count(1) "
		+ "from MLC_Text "
		+ "where _Marker_key = %d";

    // get marker note chunks (only mouse markers, as other species' notes
    //	are private)
    // fill in: marker key (int)
    private static final String NOTES =
		"select mn.note "
		+ "from MRK_Notes mn, MRK_Marker mm "
		+ "where mn._Marker_key = %d "
		+ "  and mn._Marker_key = mm._Marker_key "
		+ "  and mm._Species_key = " + DBConstants.Species_Mouse + " "
		+ "order by mn.sequenceNum";

    // get orthologous species
    // fill in: marker key (int)
    private static final String ORTHOLOGOUS_SPECIES =
    		"select distinct ms.name, ms.species, ms._Species_key"
		+ " from MRK_Species ms,"
		+ "    MRK_Marker mm,"
		+ "    HMD_Homology hh1,"
		+ "    HMD_Homology_Marker hm1,"
		+ "    HMD_Homology hh2,"
		+ "    HMD_Homology_Marker hm2"
		+ " where hm1._Marker_key = %d"
		+ "    and hm1._Homology_key = hh1._Homology_key"
		+ "    and hh1._Class_key = hh2._Class_key"
		+ "    and hh2._Homology_key = hm2._Homology_key"
		+ "    and hm2._Marker_key = mm._Marker_key"
		+ "    and mm._Species_key = ms._Species_key"
		+ "    and ms._Species_key != " + DBConstants.Species_Mouse;

    // count phenotypic classifications (phenoslim annotations)
    // fill in: marker key (int)
    private static final String PHENO_CLASS_COUNT =
		"select distinct va._Term_key, geno._Genotype_key "
		+ "from GXD_AllelePair gap, GXD_Genotype geno, "
		+    "VOC_Annot va, VOC_PSGenotype_AnnotType_View atv "
		+ "where atv._AnnotType_key = va._AnnotType_key "
		+    "and va._Object_key = geno._Genotype_key "
		+    "and geno._Genotype_key = gap._Genotype_key "
		+    "and gap._Marker_key = %d";

    // get counts of polymorphisms by type
    // fill in: marker key (int)
    private static final String POLYMORPHISM_COUNTS =
		"select pp.DNAType, count (pr._Reference_key) "
		+ "from PRB_Probe pp, PRB_RFLV pr, PRB_Reference ref "
		+ "where pr._Marker_key = %d "
		+    "and pr._Reference_key = ref._Reference_key "
		+    "and ref._Probe_key = pp._Probe_key "
		+ "group by pp.DNAtype";

    // get the primary MGI ID for a given marker.
    // fill in:  marker key (int)
    protected static final String PRIMARY_MGI_ID = 
    		"select accID"
		+ " from ACC_Accession"
		+ " where _Object_key = %d"
		+    " and _MGIType_key = " + DBConstants.MGIType_Marker
		+    " and preferred = 1"
		+    " and private = 0"
		+    " and _LogicalDB_key = " + DBConstants.LogicalDB_MGI;

    // find how many probes are associatd with this marker, by type 
    // fill in: marker key (int)
    private static final String PROBE_COUNTS =
		"select pp.DNAType, count (distinct pp._Probe_key) "
		+ "from PRB_Marker pm, PRB_Probe pp "
		+ "where pm._Marker_key = %d "
		+    "and pm._Probe_key = pp._Probe_key "
		+ "group by pp.DNAtype";

    // get a count of references for the marker
    // fill in: marker key (int)
    private static final String REFERENCE_COUNT =
		"select count (1) "
		+ "from MRK_Reference "
		+ "where _Marker_key = %d";

    // get info about the marker's oldest reference, defined as the one
    // with the lowest J: number for the oldest year (which is NOT a load
    // reference)
    // fill in:  marker key (int)
    private static final String REFERENCE_FIRST =
    		"select bv._Refs_key, bv.jnumID, bv.authors, bv.authors2,"
		+ "    bv.title, bv.title2, bv.citation"
		+ " from BIB_View bv, MRK_Reference mr"
		+ " where bv._Refs_key = mr._Refs_key"
		+ "    and mr._Marker_key = %d"
		+ " order by bv.year, bv.jnum";

    // get info about the marker's most recent reference, defined as the one
    // with the highest J: number for the most recent year (which is NOT a
    // load reference)
    // fill in:  marker key (int)
    private static final String REFERENCE_LAST =
    		"select bv._Refs_key, bv.jnumID, bv.authors, bv.authors2,"
		+ "    bv.title, bv.title2, bv.citation"
		+ " from BIB_View bv, MRK_Reference mr"
		+ " where bv._Refs_key = mr._Refs_key"
		+ "    and mr._Marker_key = %d"
		+ " order by bv.year desc, bv.jnum desc";

    // get all sequence IDs associated with a marker
    // fill in:  marker key (int)
    private static final String SEQ_IDS = 
		"select a.accID, ldb.name, adb.url, ldb._LogicalDB_key "
		+ "from ACC_Accession a, ACC_LogicalDB ldb, ACC_ActualDB adb "
		+ "where a._LogicalDB_key = ldb._LogicalDB_key "
		+    "and ldb._LogicalDB_key = adb._LogicalDB_key "
		+    "and adb.name in ('GenBank', 'Swiss-Prot (SIB)', "
		+    "    'RefSeq') "
		+    "and a._Object_key = %d "
		+    "and a.private = 0 "
		+    "and a._MGIType_key = " + DBConstants.MGIType_Marker
		+    " and a._LogicalDB_key in (" +
			    DBConstants.LogicalDB_RefSeq + ", " +
			    DBConstants.LogicalDB_SwissProt + ", " +
			    DBConstants.LogicalDB_SequenceDB + ") "
		+ " order by a.accID";

    // get synonyms
    // fill in: marker key (int)
    private static final String SYNONYMS =
		"select name "
		+ "from MRK_Other "
		+ "where _Marker_key = %d "
		+ "order by name";

    // get the marker key and symbol for the marker(s) to which a specified
    // marker was withdrawn.  There may be multiple rows returned if a marker
    // has been split into multiple ones.
    // fill in: marker key (int)
    protected static final String WITHDRAWN_TO =
		"select mm._Marker_key, mm.symbol"
		+ " from MRK_Marker mm, MRK_Current mc"
		+ " where mc._Marker_key = %d"
		+    " and mc._Current_key = mm._Marker_key"
		+ " order by mm.symbol";
}

/*
* $Log$
* Revision 1.1  2003/12/30 16:28:29  mbw
* initial import into this product
*
* Revision 1.2  2003/12/01 13:14:06  jsb
* Many code review changes
*
* Revision 1.1  2003/07/03 17:37:13  jsb
* initial addition for use by JSAM WI
*
* $Copyright$
*/
