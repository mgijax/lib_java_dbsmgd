package org.jax.mgi.shr.datafactory;

/*
* $Header$
* $Name$
*/

// imports

import java.util.ArrayList;
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
	    accID, accID) );

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
		Sprintf.sprintf (BASIC_SNP_DATA, snpKey) );

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

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (SNP_FLANK, snpKey));

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
	    REFSNP_ALLELES, snpKey));

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

	DTO item = null;

	DTO subSnps = DTO.getDTO();
	Integer subSnpKey = null;
	ArrayList subSnpOrder = new ArrayList();

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (SUBSNPS, snpKey));

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
	    item.set (DTOConstants.Alleles, DTO.getDTO());

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

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (SUBSNPS_ALLELES,
	    snpKey));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    subSnpKey = rr.getInt(1);
	    strainKey = rr.getInt(2);

	    item = DTO.getDTO();
	    item.set (DTOConstants.SubSnpKey, subSnpKey);
	    item.set (DTOConstants.StrainKey, strainKey);
	    item.set (DTOConstants.Allele, rr.getString(3));
	    item.set (DTOConstants.Population, rr.getString(4));
	    item.set (DTOConstants.SubHandle, rr.getString(5));

	    subSnp = (DTO) subSnps.get (subSnpKey.toString());
	    if (subSnp != null)
	    {
	    alleleCalls = (DTO) subSnp.get (DTOConstants.Alleles);

	    alleleCalls.set (strainKey.toString(), item);
	    calls++;
	    }
	}

	nav.close();

	this.timeStamp (Sprintf.sprintf ("Got %d allele calls for subSnps",
	    calls));

        return data;
    }

    /* -------------------------------------------------------------------- */

    public DTO getMarkers (int snpKey) throws DBException
    {
        DTO data = DTO.getDTO();
	ResultsNavigator nav = null;
	RowReference rr = null;

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (REFSNP_GENES,snpKey));

	ArrayList locations = new ArrayList();
	DTO location = null;
	Integer lastFeature = null;
	Integer feature = null;
	DTO gene = null;
	DTO fcAllele = null;
	ArrayList alleles = null;
	Integer markerKey = null;
	Integer lastMarkerKey = null;
	ArrayList genes = null;
	int markerCount = 0;

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    feature = rr.getInt(1);
	    markerKey = rr.getInt(9);

	    // if this is a whole new feature...
	    if ((lastFeature == null) || (!lastFeature.equals(feature)))
	    {
	        location = DTO.getDTO();
		genes = new ArrayList();

		location.set (DTOConstants.Chromosome, rr.getString(2));
		location.set (DTOConstants.StartCoord, rr.getDouble(3));
		location.set (DTOConstants.Orientation, rr.getString(4));
		location.set (DTOConstants.VariationClass, rr.getString(5));
		location.set (DTOConstants.AccID, rr.getString(7));
		location.set (DTOConstants.LogicalDbKey, rr.getInt(8));
		location.set (DTOConstants.AssociatedGenes, genes);

		locations.add (location);

		lastFeature = feature;
	    }

	    // now need to add gene info to the current 'location'...

	    // build the object with function class & allele info

	    fcAllele = DTO.getDTO();
	    fcAllele.set (DTOConstants.FunctionClass, rr.getString(6));
	    fcAllele.set (DTOConstants.Allele, rr.getString(12));

	    // if we've started on a new gene, then we need to record all data
	    // for that gene

	    if ((lastMarkerKey == null) || (!lastMarkerKey.equals(markerKey)))
	    {
	    	alleles = new ArrayList();
		alleles.add (fcAllele);

		gene = DTO.getDTO();
		gene.set (DTOConstants.FxnAlleles, alleles);
		gene.set (DTOConstants.MarkerKey, rr.getInt(9));
		gene.set (DTOConstants.MarkerSymbol, rr.getString(10));
		gene.set (DTOConstants.MarkerName, rr.getString(11));
		gene.set (DTOConstants.Residue, rr.getString(13));
		gene.set (DTOConstants.AminoAcidPosition, rr.getString(14));
		gene.set (DTOConstants.ReadingFrame, rr.getString(15)); 
		genes.add (gene);

		lastMarkerKey = markerKey;
		markerCount++;
	    }
	    else	// otherwise, just add the allele/fxn info to old gene
	    {
		alleles.add (fcAllele);
	    }
	}

	nav.close();
	data.set (DTOConstants.Markers, locations);

	String msg = Sprintf.sprintf ("Got %d genes", markerCount) +
		Sprintf.sprintf (" in %d locations", locations.size());
	this.timeStamp (msg);

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

	nav = this.sqlDM.executeQuery (OPTIONLIST_STRAINS);
	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    key = rr.getInt(1);
	    value = rr.getString(2);

	    strains.append (Sprintf.sprintf (template, key, value));
	}
	nav.close();
	this.timeStamp ("Got strains");

	nav = this.sqlDM.executeQuery (OPTIONLIST_FUNCTION_CLASSES);
	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    key = rr.getInt(1);
	    value = rr.getString(2);

	    fxnClasses.append (Sprintf.sprintf (template, key, value));
	}
	nav.close();
	this.timeStamp ("Got function classes");

	nav = this.sqlDM.executeQuery (OPTIONLIST_VARIATION_TYPES);
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

    // none yet

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
    ** fill in: consensus snp key (int)
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
	+ "    cc._Feature_key, "
	+ "    cc.iupacCode "
	+ "FROM SNP_Coord_Cache cc, "
	+ "    ACC_Accession aa, "
	+ "    VOC_Term vt "
	+ "WHERE cc._ConsensusSnp_key = %d "
	+ "    AND cc._ConsensusSnp_key = aa._Object_key "
	+ "    AND aa._MGIType_key = " + DBConstants.MGIType_ConsensusSnp
	+ "    AND aa.preferred = 1 "
	+ "    AND aa.private = 0 "
	+ "    AND cc._VarClass_key = vt._Term_key";

    /** retrieve function classes for the SNPs query form's Function Class
    **    selection list
    ** fill in: nothing
    */
    private static String OPTIONLIST_FUNCTION_CLASSES =
        "SELECT DISTINCT vt._Term_key, "
	+ "    vt.term "
	+ "FROM VOC_Term vt, "
	+ "    VOC_Vocab vv "
	+ "WHERE vt._Vocab_key = vv._Vocab_key "
	+ "    AND vv.name = 'SNP Function Class'"
	+ "    AND vt._Term_key IN "
	+ "        (SELECT DISTINCT _Fxn_key FROM SNP_ConsensusSnp_Marker) "
	+ "    AND vt.term != 'reference' "
	+ "ORDER BY vt.term";

    /** retrieve strains for the SNPs query form's Strain selection list
    ** fill in: nothing
    */
    private static String OPTIONLIST_STRAINS =
	"SELECT DISTINCT ps._Strain_key, "
	+ "    ps.strain "
	+ "FROM PRB_Strain ps, "
	+ "    MGI_Set ms, "
	+ "    MGI_SetMember msm "
	+ "WHERE ps._Strain_key = msm._Object_key "
	+ "    AND msm._Set_key = ms._Set_key "
	+ "    AND ms.name = 'SNP Strains' "
	+ "ORDER BY msm.sequenceNum";

    /** retrieve variation types for the SNPs query form's Variation Type
    **    selection list
    ** fill in: nothing
    */
    private static String OPTIONLIST_VARIATION_TYPES =
        "SELECT DISTINCT vt._Term_key, "
	+ "    vt.term "
	+ "FROM VOC_Term vt, "
	+ "    VOC_Vocab vv "
	+ "WHERE vt._Vocab_key = vv._Vocab_key "
	+ "    AND vv.name = 'SNP Variation Class'"
	+ "    AND vt._Term_key IN "
	+ "        (SELECT DISTINCT _VarClass_key FROM SNP_ConsensusSNP) "
	+ "ORDER BY vt.term";

    /** retrieves flank for a given consensus snp, ordered by sequence num
    ** fill in: consensus snp key (int)
    */
    private static String SNP_FLANK = 
	"SELECT flank, "
	+ "    is5Prime, "
	+ "    sequenceNum "
	+ "FROM SNP_Flank "
	+ "WHERE _ConsensusSnp_key = %d "
	+ "ORDER BY sequenceNum";
    
    /** retrieves consensus snp keys matching a given accession ID
    ** fill in: accession ID (string), accession ID (string)
    */
    private static String SNP_KEY_FOR_ID = 
	"SELECT _Object_key "
	+ "FROM ACC_Accession "
	+ "WHERE accID = '%s' "
	+ "    AND _MGIType_key = " + DBConstants.MGIType_ConsensusSnp
	+ " UNION "
	+ "SELECT ss._ConsensusSnp_key "
	+ "FROM ACC_Accession aa, "
	+ "    SNP_SubSnp ss "
	+ "WHERE aa.accID = '%s' "
	+ "    AND aa._MGIType_key = " + DBConstants.MGIType_SubSnp
	+ "    AND aa._Object_key = ss._SubSnp_key ";
   
    /** retrieves markers and their functional classes for a consensus snp
    ** fill in: consensus snp key (int)
    */
    private static String SNP_MARKERS =
	"SELECT mm._Marker_key, "
	+ "    mm.symbol, "
	+ "    vt.term as functionClass, "
	+ "    csm.contig_allele, "
	+ "    csm.residue, "
	+ "    csm.aa_position, "
	+ "    csm.reading_frame, "
	+ "    csm._Feature_key "
	+ "FROM SNP_ConsensusSnp_Marker csm, "
	+ "    MRK_Marker mm, "
	+ "    VOC_Term vt "
	+ "WHERE csm._ConsensusSnp_key = %d "
	+ "    AND csm._Marker_key = mm._Marker_key "
	+ "    AND csm._Fxn_key = vt._Term_key ";

/* --- new -----------------------------------------------------------------*/

    /** gets allele calls for the reference snp, ordered by strain ordering.
    ** fill in: consensus snp key (int)
    */
    private static String REFSNP_ALLELES =
	"SELECT ssa._Strain_key, "
	+ "    ssa.allele, "
	+ "    ssa.isConflict "
	+ "FROM SNP_ConsensusSnp_StrainAllele ssa, "
	+ "    MGI_SetMember msm, "
	+ "    MGI_Set ms "
	+ "WHERE ssa._ConsensusSnp_key = %d "
	+ "    AND ssa._Strain_key = msm._Object_key "
	+ "    AND msm._Set_key = ms._Set_key "
	+ "    AND ms.name = 'SNP Strains' "
	+ "ORDER BY msm.sequenceNum";

    private static String SUBSNPS =
	"SELECT DISTINCT ss._SubSnp_key, "
	+ "    vt1.term AS variationClass, "
	+ "    vt2.term AS submitterHandle, "
	+ "    aa.accID, "
	+ "    aa._LogicalDB_key, "
	+ "    ss.orientation, "
	+ "    ss.isExemplar "
	+ "FROM SNP_SubSnp ss, "
	+ "    ACC_Accession aa, "
	+ "    VOC_Term vt1, "
	+ "    VOC_Term vt2 "
	+ "WHERE ss._ConsensusSnp_key = %d "
	+ "    AND ss._VarClass_key = vt1._Term_key "
	+ "    AND ss._SubHandle_key = vt2._Term_key "
	+ "    AND ss._SubSnp_key = aa._Object_key "
	+ "    AND aa._MGIType_key = " + DBConstants.MGIType_SubSnp
	+ "    AND aa.private = 0 "
	+ "    AND aa.preferred = 1 "
	+ "    AND aa._LogicalDB_key = 74"
	+ "ORDER BY aa.accID";

    private static String SUBSNPS_ALLELES =
	"SELECT DISTINCT ssa._SubSnp_key, "
	+ "    ssa._Strain_key, "
	+ "    ssa.allele, "
	+ "    pop.name, "
	+ "    vt.term AS popSubmitterHandle "
	+ "FROM SNP_SubSnp ss, "
	+ "    SNP_SubSnp_StrainAllele ssa, "
	+ "    SNP_Population pop, "
	+ "    VOC_Term vt "
	+ "WHERE ss._ConsensusSnp_key = %d "
	+ "    AND ss._SubSnp_key = ssa._SubSnp_key "
	+ "    AND ssa._Population_key = pop._Population_key "
	+ "    AND pop._SubHandle_key = vt._Term_key ";

    private static String REFSNP_GENES =
	"SELECT DISTINCT scc._Feature_key, "
	+ "    scc.chromosome, "
	+ "    scc.startCoordinate, "
	+ "    scc.strand AS orientation, "
	+ "    vt1.term AS variationClass, "
	+ "    vt2.term AS functionClass, "
	+ "    aa.accID, "
	+ "    aa._LogicalDB_key, "
	+ "    mm._Marker_key, "
	+ "    mm.symbol, "
	+ "    mm.name, "
	+ "    csm.contig_allele, "
	+ "    csm.residue, "
	+ "    csm.aa_position, "
	+ "    csm.reading_frame "
	+ "FROM SNP_ConsensusSnp_Marker csm, "
	+ "    SNP_Coord_Cache scc, "
	+ "    ACC_Accession aa, "
	+ "    MRK_Marker mm, "
	+ "    VOC_Term vt1, "
	+ "    VOC_Term vt2 "
	+ "WHERE csm._ConsensusSnp_key = %d "
	+ "    AND csm._ConsensusSnp_key = scc._ConsensusSnp_key "
	+ "    AND csm._Feature_key = scc._Feature_key "
	+ "    AND scc._VarClass_key = vt1._Term_key "
	+ "    AND csm._Fxn_key = vt2._Term_key "
	+ "    AND csm._Marker_key = mm._Marker_key "
	+ "    AND csm._ConsensusSnp_Marker_key = aa._Object_key "
	+ "    AND aa.private = 0 "
	+ "    AND aa.preferred = 1 "
	+ "    AND aa._MGIType_key = " + DBConstants.MGIType_ConsensusSnp
	+ "ORDER BY scc.sequenceNum, scc.startCoordinate, mm.symbol ";
}

/*
* $Log$
* $Copyright$
*/
