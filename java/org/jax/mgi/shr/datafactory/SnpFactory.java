package org.jax.mgi.shr.datafactory;

/*
* $Header$
* $Name$
*/

// imports

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dto.DTOConstants;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.stringutil.StringLib;
import org.jax.mgi.shr.stringutil.Sprintf;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.cache.ExpiringObjectCache;

/**
* @module SnpFactory.java
* @author jsb
*/

public class SnpFactory extends AbstractDataFactory
{
    /* -------------------------------------------------------------------- */

    /////////////////////
    // instance variables
    /////////////////////

    private DataFactoryCfg config = null;

    private SQLDataManager sqlDM = null;

    /* -------------------------------------------------------------------- */

    ///////////////
    // Constructors
    ///////////////

    public SnpFactory (DataFactoryCfg config, SQLDataManager sqlDM,
    	Logger logger)
    {
        this.config = config;
	this.sqlDM = sqlDM;
	this.logger = logger;
	return;
    }

    /* -------------------------------------------------------------------- */

    //////////////////////////
    // public instance methods
    //////////////////////////

    public int getKey (Map parms) throws DBException
    {
        int snpKey = -1;

	String keyStr = StringLib.getFirst( (String[]) parms.get("key"));
	if (keyStr != null)
	{
	    this.timeStamp ("used key parameter directly for snp key");
	    snpKey = Integer.parseInt(keyStr);
	}
	else
	{
	    String idStr = StringLib.getFirst( (String[]) parms.get("id"));
	    snpKey = this.getKeyByID (idStr);
	}
	return snpKey;
    }

    /* -------------------------------------------------------------------- */

    public int getKeyByID (String accID) throws DBException
    {
        int key = -1;

	Integer snpKey = null;
	ResultsNavigator nav = null;
	RowReference rr = null;

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (SNP_KEY_FOR_ID,
	    this.snpDatabaseName, accID, this.snpDatabaseName,
	    this.snpDatabaseName, accID) );

	// just takes the first, even if multiple are returned

	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    snpKey = rr.getInt(1);

	    if (snpKey != null)
	    {
	        key = snpKey.intValue();
	    }
	}
	nav.close();

	this.timeStamp ("Got SNP key for acc ID: " + accID);
	return key;
    }

    /* -------------------------------------------------------------------- */

    public DTO getFullInfo (Map parms) throws DBException
    {
        return this.getFullInfo (this.getKey (parms));
    }

    /* -------------------------------------------------------------------- */

    public DTO getFullInfo (int snpKey) throws DBException
    {
        DTO data = this.getBasicInfo (snpKey);
	DTO alleles = this.getAlleles (snpKey);
	DTO flank = this.getFlank (snpKey);
	DTO markers = this.getMarkers (snpKey);
	DTO subSnps = this.getSubSnps (snpKey);

	data.merge (alleles);
	data.merge (flank);
	data.merge (markers);
	data.merge (subSnps);

	DTO.putDTO (alleles);
	DTO.putDTO (flank);
	DTO.putDTO (markers);
	DTO.putDTO (subSnps);

	this.timeStamp ("merged snp data & recycled DTOs");

	return data;
    }

    /* -------------------------------------------------------------------- */

    public DTO getBasicInfo (int snpKey) throws DBException
    {
        DTO data = DTO.getDTO();
	ResultsNavigator nav = null;
	RowReference rr = null;
	ArrayList locations = null;
	DTO location = null;

	nav = this.sqlDM.executeQuery (
		Sprintf.sprintf (BASIC_SNP_DATA, this.snpDatabaseName,
		    this.snpDatabaseName, Integer.toString(snpKey)) );

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    if (data.isEmpty())		// is this the first row returned?
	    {
		locations = new ArrayList();

	        data.set (DTOConstants.ConsensusSnpKey, rr.getInt(1));
		data.set (DTOConstants.AlleleSummary, rr.getString(3));
		data.set (DTOConstants.VariationClass, rr.getString(4));
		data.set (DTOConstants.AccID, rr.getString(7));
		data.set (DTOConstants.LogicalDbKey, rr.getInt(8));
		data.set (DTOConstants.Locations, locations);
		data.set (DTOConstants.IupacCode, rr.getString(10));
	    }

	    location = DTO.getDTO();
	    location.set (DTOConstants.Chromosome, rr.getString(5));
	    location.set (DTOConstants.StartCoord, rr.getDouble(6));
	    location.set (DTOConstants.Orientation, rr.getString(2));

	    locations.add (location);
	}

	nav.close();
	this.timeStamp ("Got basic data for snp " + snpKey);

	return data;
    }

    /* -------------------------------------------------------------------- */

    public DTO getFlank (int snpKey) throws DBException
    {
        DTO data = DTO.getDTO();
	ResultsNavigator nav = null;
	RowReference rr = null;

	String fivePrime = "";
	String threePrime = "";

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (SNP_FLANK,
	    this.snpDatabaseName, Integer.toString(snpKey) ));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    if (rr.getInt(2).intValue() == 1)	// is5Prime == true
	    {
		fivePrime = fivePrime + rr.getString(1);
	    }
	    else
	    {
		threePrime = threePrime + rr.getString(1);
	    }
	}
	nav.close();

	data.set (DTOConstants.Flank5Prime, fivePrime.trim());
	data.set (DTOConstants.Flank3Prime, threePrime.trim());

	this.timeStamp ("Got flanking segments");

        return data;
    }

    /* -------------------------------------------------------------------- */

    public DTO getAlleles (int snpKey) throws DBException
    {
        DTO data = DTO.getDTO();
	ResultsNavigator nav = null;
	RowReference rr = null;

	DTO item = null;

	DTO alleles = DTO.getDTO();
	ArrayList strains = new ArrayList();
	Integer strainKey = null;

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (
	    REFSNP_ALLELES, this.snpDatabaseName, this.snpDatabaseName,
	    Integer.toString(snpKey)));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    strainKey = rr.getInt(1);

	    item = DTO.getDTO();
	    item.set (DTOConstants.StrainKey, strainKey);
	    item.set (DTOConstants.Allele, rr.getString(2));
	    item.set (DTOConstants.IsConflict, rr.getInt(3));

	    alleles.set (strainKey.toString(), item);	// strain key -> info
	    strains.add (strainKey);
	}
	nav.close();

	data.set (DTOConstants.Alleles, alleles);
	data.set (DTOConstants.Strains, strains);

	this.timeStamp (Sprintf.sprintf ("Got %d alleles for snp",
	    alleles.size() ) );

        return data;
    }

    /* -------------------------------------------------------------------- */

    public DTO getSubSnps (int snpKey) throws DBException
    {
        DTO data = DTO.getDTO();
	ResultsNavigator nav = null;
	RowReference rr = null;

	Integer subSnpKey = null;
	HashMap submitterIDs = new HashMap();

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (SUBSNP_SUBMITTER_IDS,
	    this.snpDatabaseName, this.snpDatabaseName,
	    Integer.toString(snpKey) ));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    subSnpKey = rr.getInt(1);

	    submitterIDs.put (subSnpKey, rr.getString(2));
	}
	nav.close();

	DTO item = null;

	DTO subSnps = DTO.getDTO();
	ArrayList subSnpOrder = new ArrayList();
	DTO populations = null;

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (SUBSNPS,
	    this.snpDatabaseName, this.snpDatabaseName,
	    Integer.toString(snpKey) ));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    subSnpKey = rr.getInt(1);

	    item = DTO.getDTO();
	    item.set (DTOConstants.SubSnpKey, subSnpKey);
	    item.set (DTOConstants.VariationClass, rr.getString(2));
	    item.set (DTOConstants.SubHandle, rr.getString(3));
	    item.set (DTOConstants.AccID, rr.getString(4));
	    item.set (DTOConstants.LogicalDbKey, rr.getInt(5));
	    item.set (DTOConstants.Orientation, rr.getString(6));
	    item.set (DTOConstants.IsExemplar, rr.getInt(7));
	    item.set (DTOConstants.AlleleSummary, rr.getString(8));
	    item.set (DTOConstants.Populations, DTO.getDTO());

	    if (submitterIDs.containsKey (subSnpKey))
	    {
	        item.set (DTOConstants.SubmitterID,
		    submitterIDs.get(subSnpKey));
	    }

	    subSnps.set (subSnpKey.toString(), item);
	    subSnpOrder.add (subSnpKey);
	}
	nav.close();

	data.set (DTOConstants.SubSnps, subSnps);
	data.set (DTOConstants.SubSnpOrder, subSnpOrder);

	this.timeStamp (Sprintf.sprintf ("Got %d subSnps for snp",
	    subSnps.size() ) );

	// now go back and get the allele calls for those subsnps

	int calls = 0;
	Integer strainKey = null;
	DTO alleleCalls = null;
	DTO subSnp = null;
	DTO population = null;
	String popStr = null;
	String popID = null;
	Integer popKey = null;
	boolean anyInsertions = false;
	String allele = null;

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (SUBSNPS_ALLELES,
	    this.snpDatabaseName, this.snpDatabaseName, this.snpDatabaseName,
	    this.snpDatabaseName, Integer.toString(snpKey) ));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    subSnpKey = rr.getInt(1);
	    strainKey = rr.getInt(2);
	    allele = rr.getString(3);
	    popStr = rr.getString(4);

	    item = DTO.getDTO();
	    item.set (DTOConstants.StrainKey, strainKey);
	    if (allele != null)
	    {
	        item.set (DTOConstants.Allele, allele);
	        if (!anyInsertions && (allele.length() > 1))
		{
		    anyInsertions = true;
		}
	    }

	    subSnp = (DTO) subSnps.get (subSnpKey.toString());
	    if (subSnp != null)
	    {
	        populations = (DTO) subSnp.get (DTOConstants.Populations);
		population = (DTO) populations.get (popStr);

		if (population == null)
		{
		    population = DTO.getDTO();
		    alleleCalls = DTO.getDTO();

		    population.set (DTOConstants.SubHandle, rr.getString(5));
		    population.set (DTOConstants.Alleles, alleleCalls);

		    popKey = rr.getInt(6);
		    popID = getPopulationID (popKey);
		    if (popID != null)
		    {
		        population.set (DTOConstants.AccID, popID);
		    }

		    populations.set (popStr, population);
		}
		else
		{
	    	    alleleCalls = (DTO) population.get (DTOConstants.Alleles);
		}

	    	alleleCalls.set (strainKey.toString(), item);
	    	calls++;
	    }
	}

	nav.close();

	data.set (DTOConstants.AnyInsertions, new Boolean(anyInsertions));

	this.timeStamp (Sprintf.sprintf ("Got %d allele calls for subSnps",
	    calls));

        return data;
    }

    /* -------------------------------------------------------------------- */

    public DTO getMarkers (int snpKey) throws DBException
    {
	ResultsNavigator nav = null;
	RowReference rr = null;

	// info from each data row

	String chromosome = null;
	Double startCoordinate = null;
	Integer consensusSnpMarkerKey = null;
	String symbol = null;
	String accID = null;
	String functionClass = null;
	String orientation = null;
	Integer featureKey = null;
	Integer functionKey = null;
	String contigAllele = null;
	String residue = null;
	String aaPosition = null;
	String readingFrame = null;
	Integer markerKey = null;
	Integer consensusSnpKey = null;
	String markerName = null;

	Integer lastFeatureKey = null;
	Integer lastMarkerKey = null;
	Integer lastCsmKey = null;

	int subRowCount = 0;

        DTO data = DTO.getDTO();
	ArrayList locations = new ArrayList();
	data.set (DTOConstants.Markers, locations);

	DTO location = null;
	ArrayList genes = null;
	DTO gene = null;
	ArrayList subrows = null;
	DTO subrow = null;

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (REFSNP_GENES,
	    this.snpDatabaseName, this.snpDatabaseName, this.snpDatabaseName,
	    Integer.toString(snpKey) ));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    chromosome = rr.getString(1);		//
	    startCoordinate = rr.getDouble(2);		//
	    consensusSnpMarkerKey = rr.getInt(3);	//
	    symbol = rr.getString(4);			//
	    accID = rr.getString(5);			//
	    functionClass = rr.getString(6);		//
	    orientation = rr.getString(7);		//
	    featureKey = rr.getInt(8);			//
	    functionKey = rr.getInt(9);
	    contigAllele = rr.getString(10);		//
	    residue = rr.getString(11);			//
	    aaPosition = rr.getString(12);		//
	    readingFrame = rr.getString(13);		//
	    markerKey = rr.getInt(14);			//
	    consensusSnpKey = rr.getInt(15);
	    markerName = rr.getString(16);		//

	    // still need:  orientation, NCBI build #,

	    if ((lastFeatureKey == null) ||
	        (!lastFeatureKey.equals(featureKey)) )
	    {
		location = DTO.getDTO();
		location.set (DTOConstants.Chromosome, chromosome);
		location.set (DTOConstants.StartCoord, startCoordinate);
		location.set (DTOConstants.Orientation, orientation);

		genes = new ArrayList();
		location.set (DTOConstants.AssociatedGenes, genes);

		locations.add (location);
		lastFeatureKey = featureKey;

		lastMarkerKey = null;
		lastCsmKey = null;
	    }
	    // else -- just use the 'location' from last time

	    if ((lastMarkerKey == null) ||
	        (!lastMarkerKey.equals (markerKey)) )
	    {
		gene = DTO.getDTO();
		gene.set (DTOConstants.MarkerSymbol, symbol);
		gene.set (DTOConstants.MarkerName, markerName);
		gene.set (DTOConstants.MarkerKey, markerKey);
		subrows = new ArrayList();
		gene.set (DTOConstants.FxnAlleles, subrows);
		genes.add (gene);
		lastMarkerKey = markerKey;
		lastCsmKey = null;
	    }
	    // else -- just use the 'gene' from last time

	    if ((lastCsmKey == null) ||
	        (!lastCsmKey.equals (consensusSnpMarkerKey)) )
	    {
		subrow = DTO.getDTO();
		subrow.set (DTOConstants.FunctionClass, functionClass);
		subrow.set (DTOConstants.Residue, residue);
		subrow.set (DTOConstants.AminoAcidPosition, aaPosition);
		subrow.set (DTOConstants.ReadingFrame, readingFrame);
		subrow.set (DTOConstants.Allele, contigAllele);

		subrows.add (subrow);
		lastCsmKey = consensusSnpMarkerKey;
		subRowCount++;
	    }
	    // else -- just use the 'subrow' from last time

	    if (accID != null)
	    {
		if (accID.startsWith ("NM"))
		{
		    subrow.set (DTOConstants.RepRNASeq, accID);
		}
		else if (accID.startsWith ("NP"))
		{
		    subrow.set (DTOConstants.RepProteinSeq, accID);
		}
	    }

	} // end -- while

	nav.close();

	this.timeStamp (Sprintf.sprintf ("got %d subrows for markers",
	    subRowCount) );
	return data;
    }

    /* -------------------------------------------------------------------- */

    public DTO getQueryFormData (Map parms) throws DBException
    {
        DTO data = null;
	String cacheKey = "SnpFactory." + this.sqlDM.getServer() + "." +
	    this.sqlDM.getDatabase() + ".SnpQueryForm";
	ExpiringObjectCache cache = ExpiringObjectCache.getSharedCache();

	data = (DTO) cache.get(cacheKey);
	if (data != null)
	{
	    this.timeStamp ("Got query form data from cache");
	    DTO newDTO = DTO.getDTO();
	    newDTO.merge (data);
	    return newDTO;
	}

	ResultsNavigator nav = null;
	RowReference rr = null;
	data = DTO.getDTO();

	StringBuffer strains = new StringBuffer();
	StringBuffer fxnClasses = new StringBuffer();
	StringBuffer varTypes = new StringBuffer();

	Integer key = null;
	String value = null;

	String template = "<OPTION VALUE='%d'>%s</OPTION>\n";

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (OPTIONLIST_STRAINS,
	    this.snpDatabaseName) );
	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    key = rr.getInt(1);
	    value = rr.getString(2);

	    try
	    {
	        value = value.replaceAll ("<", "&lt;");
		value = value.replaceAll (">", "&gt;");
	    }
	    catch (Exception e)
	    {}

	    strains.append (Sprintf.sprintf (template, key, value));
	}
	nav.close();
	this.timeStamp ("Got strains");

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (
	    OPTIONLIST_FUNCTION_CLASSES, this.snpDatabaseName) );
	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    key = rr.getInt(1);
	    value = rr.getString(2);

	    fxnClasses.append (Sprintf.sprintf (template, key, value));
	}
	nav.close();
	this.timeStamp ("Got function classes");

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (
	    OPTIONLIST_VARIATION_TYPES, this.snpDatabaseName) );
	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    key = rr.getInt(1);
	    value = rr.getString(2);

	    varTypes.append (Sprintf.sprintf (template, key, value));
	}
	nav.close();
	this.timeStamp ("Got variation types");

	data.set ("Strains", strains.toString());
	data.set ("VariationTypes", varTypes.toString());
	data.set ("FunctionClasses", fxnClasses.toString());

	cache.put (cacheKey, (DTO) data.clone(), 240 * 60);	// 4 hours

	this.timeStamp ("Got query form data from db");
	return data;
    }

    /* -------------------------------------------------------------------- */

    ///////////////////////////
    // private instance methods
    ///////////////////////////

    private String getPopulationID (Integer populationKey) throws DBException
    {
	ExpiringObjectCache cache = ExpiringObjectCache.getSharedCache();
	String cacheKey = "SnpFactory.PopulationMapping";

	DTO mapping = (DTO) cache.get(cacheKey);
	if (mapping == null)
	{
	    RowReference rr = null;
	    Integer popKey = null;
	    String accID = null;
	    ResultsNavigator nav = this.sqlDM.executeQuery (
	        Sprintf.sprintf (POPULATIONS, this.snpDatabaseName,
			this.snpDatabaseName) );

	    mapping = DTO.getDTO();
	    while (nav.next())
	    {
	        rr = (RowReference) nav.getCurrent();

	        popKey = rr.getInt(1);
		accID = rr.getString(2);

		if (popKey != null)
		{
		    mapping.set (popKey.toString(), accID);
	    	}
	    }

	    cache.put (cacheKey, (DTO) mapping.clone(), 480 * 60);  // 8 hours
	    this.timeStamp ("Got population IDs");
	}

	return (String) mapping.get (populationKey.toString());
    }

    /* -------------------------------------------------------------------- */

    //////////////////////////
    // private class variables
    //////////////////////////

    /* -----------------------------------------------------------------------
    ** class variables -- used to hold standard SQL statements, so we don't
    ** need to re-join the Strings in each thread of the servlet.  For each
    ** one, we note the pieces that need to be filled in using Sprintf.
    ** -----------------------------------------------------------------------
    */

    /* retrieves basic data for a SNP, including multiple positions as
    **    multiple rows returned
    ** fill in: SNP db (String), SNP db (String), consensus snp key (String)
    */
    private static String BASIC_SNP_DATA =
    	"SELECT DISTINCT cc._ConsensusSnp_key, "
	+ "    cc.strand as orientation, "
	+ "    cc.alleleSummary, "
	+ "    vt.term as variationClass, "
	+ "    cc.chromosome, "
	+ "    cc.startCoordinate, "
	+ "    aa.accID, "
	+ "    aa._LogicalDB_key, "
	+ "    cc._Coord_Cache_key, "
	+ "    cc.iupacCode "
	+ "FROM %s..SNP_Coord_Cache cc, "
	+ "    %s..SNP_Accession aa, "
	+ "    VOC_Term vt "
	+ "WHERE cc._ConsensusSnp_key = %s "
	+ "    AND cc._ConsensusSnp_key = aa._Object_key "
	+ "    AND aa._MGIType_key = " + DBConstants.MGIType_ConsensusSnp
	+ "    AND cc._VarClass_key = vt._Term_key";

    /** retrieve function classes for the SNPs query form's Function Class
    **    selection list
    ** fill in: SNP db (String)
    */
    private static String OPTIONLIST_FUNCTION_CLASSES =
        "SELECT DISTINCT vt._Term_key, "
	+ "    vt.term "
	+ "FROM VOC_Term vt, "
	+ "    VOC_Vocab vv "
	+ "WHERE vt._Vocab_key = vv._Vocab_key "
	+ "    AND vv.name = 'SNP Function Class'"
	+ "    AND ( (vt._Term_key IN "
	+ "          (SELECT DISTINCT _Fxn_key "
	+ "		FROM %s..SNP_ConsensusSnp_Marker))"
	+ "	    OR "
	+ "	    (vt.term = 'Locus-Region') ) "
	+ "    AND vt.term != 'Contig-Reference' "
	+ "    AND vt.term NOT LIKE 'within%%' "
	+ "    AND vt.term NOT LIKE '%%stream)' "
	+ "ORDER BY vt.term";

    /** retrieve strains for the SNPs query form's Strain selection list
    ** fill in: SNP db (String)
    */
    private static String OPTIONLIST_STRAINS =
    	"SELECT _mgdStrain_key, strain "
	+ " FROM %s..SNP_Strain "
	+ " ORDER BY sequenceNum";

    /** retrieve variation types for the SNPs query form's Variation Type
    **    selection list
    ** fill in: SNP db (String)
    */
    private static String OPTIONLIST_VARIATION_TYPES =
        "SELECT DISTINCT vt._Term_key, "
	+ "    vt.term "
	+ "FROM VOC_Term vt, "
	+ "    VOC_Vocab vv "
	+ "WHERE vt._Vocab_key = vv._Vocab_key "
	+ "    AND vv.name = 'SNP Variation Class'"
	+ "    AND vt._Term_key IN "
	+ "        (SELECT DISTINCT _VarClass_key "
	+ "		FROM %s..SNP_ConsensusSNP) "
	+ "ORDER BY vt.sequenceNum";

    /** retrieves flank for a given consensus snp, ordered by sequence num
    ** fill in: SNP db (String), consensus snp key (String)
    */
    private static String SNP_FLANK =
	"SELECT flank, "
	+ "    is5Prime, "
	+ "    sequenceNum "
	+ "FROM %s..SNP_Flank "
	+ "WHERE _ConsensusSnp_key = %s "
	+ "ORDER BY sequenceNum";

    /** retrieves consensus snp keys matching a given accession ID
    ** fill in: SNP db (String), accession ID (string), SNP db (String),
    **    SNP db (String), accession ID (string)
    */
    private static String SNP_KEY_FOR_ID =
	"SELECT _Object_key "
	+ "FROM %s..SNP_Accession "
	+ "WHERE accID = '%s' "
	+ "    AND _MGIType_key = " + DBConstants.MGIType_ConsensusSnp
	+ " UNION "
	+ "SELECT ss._ConsensusSnp_key "
	+ "FROM %s..SNP_Accession aa, "
	+ "    %s..SNP_SubSnp ss "
	+ "WHERE aa.accID = '%s' "
	+ "    AND aa._MGIType_key = " + DBConstants.MGIType_SubSnp
	+ "    AND aa._Object_key = ss._SubSnp_key ";

    /** gets the set of populations and their IDs
    ** fill in: SNP db (String), SNP db (String)
    */
    private static String POPULATIONS =
    	"SELECT DISTINCT p._Population_key, "
	+ "	aa.accID "
	+ "FROM %s..SNP_Accession aa, "
	+ "	%s..SNP_Population p "
	+ "WHERE aa._Object_key = p._Population_key "
	+ "	AND aa._MGIType_key = " + DBConstants.MGIType_Population;

    /** gets allele calls for the reference snp, ordered by strain ordering.
    ** fill in: SNP db (String), SNP db (String), consensus snp key (String)
    */
    private static String REFSNP_ALLELES =
	"SELECT ssa._mgdStrain_key, "
	+ "    ssa.allele, "
	+ "    ssa.isConflict "
	+ "FROM %s..SNP_ConsensusSnp_StrainAllele ssa, "
	+ "    %s..SNP_Strain ss "
	+ "WHERE ssa._ConsensusSnp_key = %s "
	+ "    AND ssa._mgdStrain_key = ss._mgdStrain_key "
	+ "ORDER BY ss.sequenceNum";

    /** retrieves SubSnps for a given consensus SNP key
    ** fill in:  SNP db (String), SNP db (String), consensus SNP key (String)
    */
    private static String SUBSNPS =
	"SELECT DISTINCT ss._SubSnp_key, "
	+ "    vt1.term AS variationClass, "
	+ "    vt2.term AS submitterHandle, "
	+ "    aa.accID, "
	+ "    aa._LogicalDB_key, "
	+ "    ss.orientation, "
	+ "    ss.isExemplar, "
	+ "    ss.alleleSummary "
	+ "FROM %s..SNP_SubSnp ss, "
	+ "    %s..SNP_Accession aa, "
	+ "    VOC_Term vt1, "
	+ "    VOC_Term vt2 "
	+ "WHERE ss._ConsensusSnp_key = %s "
	+ "    AND ss._VarClass_key = vt1._Term_key "
	+ "    AND ss._SubHandle_key = vt2._Term_key "
	+ "    AND ss._SubSnp_key = aa._Object_key "
	+ "    AND aa._MGIType_key = " + DBConstants.MGIType_SubSnp
	+ "    AND aa._LogicalDB_key = 74"
	+ "ORDER BY aa.accID";

    /** retrieve allele calls for SubSnps of a given consensus SNP key
    ** fill in: SNP db (String), SNP db (String), SNP db (String),
    **    SNP db (String), consensus snp key (String)
    */
    private static String SUBSNPS_ALLELES =
	"SELECT DISTINCT ssa._SubSnp_key, "
	+ "    ssa._mgdStrain_key, "
	+ "    ssa.allele, "
	+ "    pop.name, "
	+ "    vt.term AS popSubmitterHandle, "
	+ "    pop._Population_key "
	+ "FROM %s..SNP_SubSnp ss, "
	+ "    %s..SNP_SubSnp_StrainAllele ssa, "
	+ "    %s..SNP_Population pop, "
	+ "    VOC_Term vt, "
	+ "    %s..SNP_Accession aa "
	+ "WHERE ss._ConsensusSnp_key = %s "
	+ "    AND ss._SubSnp_key = ssa._SubSnp_key "
	+ "    AND ssa._Population_key = pop._Population_key "
	+ "    AND pop._Population_key = aa._Object_key "
	+ "    AND aa._MGIType_key = " + DBConstants.MGIType_Population
	+ "    AND pop._SubHandle_key = vt._Term_key ";

    /** gets gene/function class associations for this SNP, with associated
    *  transcript and protein IDs.
    * fill in: SNP db (String), SNP db (String), SNP db (String),
    *    refSNP ID (String)
    */
    private static String REFSNP_GENES =
	"SELECT scc.chromosome, "
	+ "    scc.startCoordinate, "
	+ "    csm._ConsensusSnp_Marker_key, "
	+ "    mm.symbol, "
	+ "    aa.accID, "
	+ "    vt1.term as functionClass, "
	+ "    scc.strand, "
	+ "    csm._Coord_Cache_key, "
	+ "    csm._Fxn_key, "
	+ "    csm.contig_allele, "
	+ "    csm.residue, "
	+ "    csm.aa_position, "
	+ "    csm.reading_frame, "
	+ "    csm._Marker_key, "
	+ "    csm._ConsensusSnp_key, "
	+ "    mm.name "
	+ "FROM %s..SNP_ConsensusSnp_Marker csm, "
	+ "    %s..SNP_Coord_Cache scc, "
	+ "    MRK_Marker mm, "
	+ "    VOC_Term vt1, "
	+ "    %s..SNP_Accession aa, "
	+ "    DAG_Closure dc, "
	+ "    VOC_Term vt2 "
	+ "WHERE csm._ConsensusSnp_key = %s "
	+ "    AND csm._ConsensusSnp_key = scc._ConsensusSnp_key "
	+ "    AND csm._Coord_Cache_key = scc._Coord_Cache_key "
	+ "    AND csm._Marker_key = mm._Marker_key "
	+ "    AND csm._Fxn_key = vt1._Term_key "
	+ "    AND csm._ConsensusSnp_Marker_key *= aa._Object_key "
	+ "    AND aa._MGIType_key = 32 "
	+ "    AND csm._Fxn_key = dc._DescendentObject_key "
	+ "    AND dc._AncestorObject_key = vt2._Term_key "
//	+ "    AND vt2.term = 'dbSNP Function Class' "
	+ "    AND vt2.term = 'within 2 kb of' "
	+ "ORDER BY scc.sequenceNum, "
	+ "    scc.startCoordinate, "
	+ "    mm.symbol, "
	+ "    vt1.term";

    /** get the submitter ID for each SubSnp of a given consensus SNP
    ** fill in: SNP db (String), SNP db (String), refsnp key (String)
    */
    private static String SUBSNP_SUBMITTER_IDS =
	"SELECT DISTINCT ss._SubSnp_key, "
	+ "    aa.accID, "
	+ "    aa._LogicalDB_key "
	+ "FROM %s..SNP_SubSnp ss, "
	+ "    %s..SNP_Accession aa "
	+ "WHERE ss._SubSnp_key = aa._Object_key "
	+ "    AND ss._ConsensusSnp_key = %s "
	+ "    AND aa._MGIType_key = 31 "
	+ "    AND aa._LogicalDB_key = 75 ";
}

/*
* $Log$
* Revision 1.11  2006/02/10 13:26:05  sc
* merge of tr7392 to trunk tag lib_java_dbsmgd-3-4-3-1
*
* Revision 1.10.4.2  2006/02/06 20:18:57  jsb
* updated query for retrieving SNP/gene associations
*
* Revision 1.10.4.1  2006/01/26 17:35:36  jsb
* updated SNP QF function class lookup query
*
* Revision 1.10.2.2  2006/03/24 17:51:09  jsb
* updated for new schema in separate SNPs database
*
* Revision 1.10.2.1  2006/02/14 18:27:22  sc
* MGI3.44 - merged branch tr7392 (MGI3.43) into tr7203 branch
*
* Revision 1.10  2005/12/02 16:04:31  pf
* 3.41 maint7119 branch merge to trunk
*
* Revision 1.9.2.1  2005/11/09 00:08:27  jw
* lib_java_dbsmgd-3-4-1-0
*
* Revision 1.9  2005/10/28 10:28:58  jsb
* added code to look for insertions
*
* Revision 1.8  2005/10/27 14:49:18  jsb
* fixed retrieval of associated markers
*
* Revision 1.7  2005/10/26 17:04:26  jsb
* updated 'reference' to be 'Contig-Reference' in vocab term
*
* Revision 1.6  2005/10/25 11:23:11  jsb
* fixes for alpha 2
*
* Revision 1.5  2005/10/21 14:04:50  jsb
* Escaped < and > characters in strain names
*
* Revision 1.4  2005/10/19 10:11:27  jsb
* now gets population IDs and subSNP's allele summary
*
* Revision 1.3  2005/10/13 11:50:22  jsb
* updated function class picklist query
*
* Revision 1.2  2005/10/12 18:13:09  jsb
* lib_java_dbsmgd-3-4-0-0
*
* Revision 1.1.2.2  2005/10/06 19:03:46  jsb
* various data retrieval updates
*
* Revision 1.1.2.1  2005/10/03 20:11:24  jsb
* initial addition
*
* $Copyright$
*/
