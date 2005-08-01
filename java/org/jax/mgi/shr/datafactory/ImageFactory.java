package org.jax.mgi.shr.datafactory;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dto.DTOConstants;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.stringutil.Sprintf;
import org.jax.mgi.shr.stringutil.StringLib;
import org.jax.mgi.shr.cache.ExpiringObjectCache;

public class ImageFactory extends AbstractDataFactory
{
    private DataFactoryCfg config = null;
    private SQLDataManager sqlDM = null;

    // -----------------------------------------------------------------------

    /** constructor
    */
    public ImageFactory (DataFactoryCfg config, SQLDataManager sqlDM,
                Logger logger)
    {
        this.config = config;
	this.sqlDM = sqlDM;
	this.logger = logger;
	return;
    }

    // -----------------------------------------------------------------------

    public int getKey (Map parms) throws DBException
    {
	int imageKey = -1;

	// if a 'key' is directly specified in 'parms', then assume it is an
	// image key and grab it

	String keyStr = StringLib.getFirst ((String[]) parms.get("key"));
	if (keyStr != null)
	{
	    this.timeStamp ("used key parameter directly for image key");
	    imageKey = Integer.parseInt(keyStr);
	}

	// otherwise, look for an 'id' parameter for which we can look up the
	// corresponding image key

	else
	{
	    String idStr = StringLib.getFirst ((String[]) parms.get("id"));
	    imageKey = this.getKeyByID (idStr);
	}
	return imageKey;
    }

    // -----------------------------------------------------------------------

    /** get the image key corresponding to the given image ID, or -1 if one
    **    does not exist.
    */
    public int getKeyByID (String accID) throws DBException
    {
       int key = -1;

	Integer imageKey = null;
	ResultsNavigator nav = null;
	RowReference rr = null;

	nav = this.sqlDM.executeQuery (
	    Sprintf.sprintf (IMAGE_KEY_FOR_ID, accID));

	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    imageKey = rr.getInt(1);

	    if (imageKey != null)
	    {
	        key = imageKey.intValue();
	    }
	}
	nav.close();

	this.timeStamp ("Retrieved image key for acc ID: " + accID);
	return key;
    }

    // -----------------------------------------------------------------------

    /** retrieve the full set of data about one image; that is, everything
    *    needed to display an image detail page
    * @param parms set of parameters specifying which image we are seeking.
    *    One key in 'parms' is checked:  'key' which identifies the database
    *    key (_Image_key) of the image to be displayed.
    * @return DTO which defines all image data fields, or null if we cannot
    *    retrieve data
    * @assumes nothing
    * @effects retrieves all image data by querying a database
    * @throws DBException if there is a problem querying the database
    */
    public DTO getFullInfo (Map parms) throws DBException
    {
	return this.getFullInfo (this.getKey (parms));
    }

    // -----------------------------------------------------------------------

    public DTO getFullInfo (int imageKey) throws DBException
    {
	DTO data = this.getBasicInfo (imageKey);
	if (data.isEmpty())	// if found no basic info, can just bail out
	{
	    return data;
	}

	DTO notes = this.getNotes (imageKey);
	data.merge (notes);
	DTO.putDTO (notes);

	// if this is a thumbnail image, then it will have a non-null key for
	// an associated full-size image

	Integer fullsizeKey =
	    (Integer) data.get (DTOConstants.FullSizeImageKey);

	if (fullsizeKey != null)
	{
	    // this is a thumbnail image and we need to retrieve the allele
	    // and genotype associations for its associated full-size image

	    imageKey = fullsizeKey.intValue();
	}

	DTO alleles = this.getAlleles (imageKey);
	data.merge (alleles);
	DTO.putDTO (alleles);

	DTO genotypes = this.getGenotypes (imageKey);
	data.merge (genotypes);
	DTO.putDTO (genotypes);

	return data;
    }

    // -----------------------------------------------------------------------

    public DTO getBasicInfo (int imageKey) throws DBException
    {
        DTO data = DTO.getDTO();	// data for the specified image
	ResultsNavigator nav = null;
	RowReference rr = null;
	Integer thumbnailKey = null;
	int fullsize = -1;

	nav = this.sqlDM.executeQuery (
	    Sprintf.sprintf (IMAGE_BASIC_INFO, imageKey) );

	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    thumbnailKey = rr.getInt(5);

	    if (thumbnailKey == null)		// this is a thumbnail image
	    {
	        fullsize = this.getFullSizeKey (imageKey);
	        data.set (DTOConstants.FullSizeImageKey,
		    new Integer(fullsize));
	    }
	    else				// this is a full-size image
	    {
	        data.set (DTOConstants.FullSizeImageKey, null);
	    }

	    data.set (DTOConstants.ImageKey, rr.getInt(1));
	    data.set (DTOConstants.Width, rr.getInt(2));
	    data.set (DTOConstants.Height, rr.getInt(3));
	    data.set (DTOConstants.FigureLabel, rr.getString(4).trim());
	    data.set (DTOConstants.ThumbnailImageKey, thumbnailKey);
	    data.set (DTOConstants.AccID, rr.getString(6));
	    data.set (DTOConstants.LogicalDbKey, rr.getInt(7));
	    data.set (DTOConstants.NumericPart, rr.getInt(8));
	}
	else
	{
	    data = null;	// failed to find an image, revert to null
	}

	nav.close();
	this.timeStamp (Sprintf.sprintf (
	    "Retrieved basic info for image %d", imageKey) );
	return data;
    }

    // -----------------------------------------------------------------------

    public DTO getNotes (int imageKey) throws DBException
    {
	DTO image = DTO.getDTO();

	ResultsNavigator nav = null;
	RowReference rr = null;
	int notetypeKey = 0;
	String notetype = null;
	String chunk = null;
	String noteSoFar = null;

	nav = this.sqlDM.executeQuery(Sprintf.sprintf(IMAGE_NOTES, imageKey));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    notetype = rr.getString(3);
	    chunk = rr.getString(4);

	    noteSoFar = (String) image.get (notetype);
	    if (noteSoFar == null)
	    {
	        noteSoFar = chunk;
	    }
	    else
	    {
	        noteSoFar = noteSoFar + chunk;
	    }

	    image.set (notetype, noteSoFar.trim());
	}
	nav.close();

	this.timeStamp (Sprintf.sprintf ("retrieved notes for image %d",
	    imageKey) );
	return image;
    }

    // -----------------------------------------------------------------------

    public DTO getAlleles (int imageKey) throws DBException
    {
	ResultsNavigator nav = null;
	RowReference rr = null;

	DTO image = DTO.getDTO();
	DTO allele = null;
	ArrayList alleles = new ArrayList();

	nav = this.sqlDM.executeQuery (
	    Sprintf.sprintf (ALLELES_FOR_IMAGE, imageKey));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    allele = DTO.getDTO();
	    allele.set (DTOConstants.AlleleKey, rr.getInt(1));
	    allele.set (DTOConstants.AlleleSymbol, rr.getString(2));
	    allele.set (DTOConstants.AlleleName, rr.getString(3));
	    allele.set (DTOConstants.MarkerName, rr.getString(4));

	    alleles.add (allele);
        }
	nav.close();

	image.set (DTOConstants.Alleles, alleles);

	this.timeStamp (Sprintf.sprintf (
	    "retrieved alleles associated with image %d", imageKey) );
	return image;
    }

    // -----------------------------------------------------------------------

    public DTO getGenotypes (int imageKey) throws DBException
    {
	DTO image = DTO.getDTO();

	ResultsNavigator nav = null;
	RowReference rr = null;
	String chunk = null;
	String noteSoFar = null;
	Integer genotypeKey = null;

	HashMap combinations = new HashMap();

	nav = this.sqlDM.executeQuery (
	    Sprintf.sprintf (GENOTYPE_ALLELE_COMBINATIONS, imageKey));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    genotypeKey = rr.getInt(1);
	    chunk = rr.getString(2);

	    if (combinations.containsKey (genotypeKey))
	    {
	        noteSoFar = (String) combinations.get(genotypeKey);
		combinations.put (genotypeKey, noteSoFar + chunk);
	    }
	    else
	    {
	        combinations.put (genotypeKey, chunk);
	    }
	}
	nav.close();

	String strain = null;
	ArrayList genotypes = new ArrayList();
	DTO genotype = null;

	nav = this.sqlDM.executeQuery (
	    Sprintf.sprintf (GENOTYPE_STRAINS, imageKey));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    genotypeKey = rr.getInt(1);
	    strain = rr.getString(2).trim();

	    genotype = DTO.getDTO();
	    genotype.set (DTOConstants.Strain, strain);
	    genotype.set (DTOConstants.GenotypeKey, genotypeKey);

	    if (combinations.containsKey (genotypeKey))
	    {
	        genotype.set (DTOConstants.AlleleCombinations,
		    ((String) combinations.get (genotypeKey)).trim() );
	    }
	    else
	    {
	        genotype.set (DTOConstants.AlleleCombinations, null);
	    }

	    genotypes.add (genotype);
	}
	nav.close();

	image.set (DTOConstants.Genotypes, genotypes);
	this.timeStamp (Sprintf.sprintf ("retrieved genotypes for image %d",
	    imageKey) );
	return image;
    }

    // -----------------------------------------------------------------------

    public DTO getPrimaryThumbnailForAllele (int alleleKey) throws DBException
    {
        ResultsNavigator nav = null;
	RowReference rr = null;
	Integer imageKey = null;
	DTO image = null;

	nav = this.sqlDM.executeQuery (
	    Sprintf.sprintf (PRIMARY_THUMBNAIL_ALLELE, alleleKey) );

	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    imageKey = rr.getInt(2);			// thumbnail key

	    if (imageKey != null)
	    {
		this.timeStamp (Sprintf.sprintf (
		    "Found allele's thumbnail image %d", imageKey) );

	        image = this.getFullInfo (imageKey.intValue());
	    }
	}
	nav.close();

	if (imageKey == null)
	{
	    this.timeStamp ("Failed to find thumbnail image for allele");
	}

	return image;
    }
    
    // -----------------------------------------------------------------------

    public DTO getPrimaryThumbnailForGenotype (int genotypeKey)
        throws DBException
    {
        ResultsNavigator nav = null;
	RowReference rr = null;
	Integer imageKey = null;
	DTO image = null;

	nav = this.sqlDM.executeQuery (
	    Sprintf.sprintf (PRIMARY_THUMBNAIL_GENOTYPE, genotypeKey) );

	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    imageKey = rr.getInt(2);			// thumbnail key

	    if (imageKey != null)
	    {
		this.timeStamp (Sprintf.sprintf (
		    "Found genotype's thumbnail image %d", imageKey) );

	        image = this.getFullInfo (imageKey.intValue());
	    }
	}
	nav.close();

	if (imageKey == null)
	{
	    this.timeStamp ("Failed to find thumbnail image for genotype");
	}

	return image;
    }
    
    // -----------------------------------------------------------------------

    /** returns data about all images associated with an allele or with
    *    genotypes involving that allele
    */
    public DTO getImagesForAllele (Map parms) throws DBException
    {
	AlleleFactory alleleFactory = new AlleleFactory (this.config,
	    this.sqlDM, this.logger);
	String keyString = alleleFactory.getKey (parms);

	if (keyString == null)
	{
	    return null;
	}
	int alleleKey = Integer.parseInt (keyString);

        return this.getImagesForAllele (alleleKey);
    }

    // -----------------------------------------------------------------------

    public DTO getImagesForAllele (int alleleKey) throws DBException
    {
	// start with the allele's basic information...

	AlleleFactory alleleFactory = new AlleleFactory (this.config,
	    this.sqlDM, this.logger);
	DTO data = alleleFactory.getBasicInfo (alleleKey);

	if (data.isEmpty())	// unknown allele?  if so, bail out
	{
	    return data;
	}

	this.timeStamp (Sprintf.sprintf (
	    "Got basic info for allele %d", alleleKey) );

	// augment the basic allele data with info about its gene

	DTO markerData = alleleFactory.getGeneInformation (alleleKey);
	data.merge (markerData);
	DTO.putDTO (markerData);	// allow this DTO to be re-used

	this.timeStamp ("Got gene data for allele");

	ResultsNavigator nav = null;
	RowReference rr = null;
	Integer thumbnailKey = null;
	Integer fullsizeKey = null;
	DTO image = null;
	ArrayList images = new ArrayList();

	String keyString = Integer.toString(alleleKey);

	nav = this.sqlDM.executeQuery (
	    Sprintf.sprintf (IMAGES_FOR_ALLELE, keyString, keyString) );

	this.timeStamp ("executed query to find images for allele");

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    fullsizeKey = rr.getInt(1);
	    thumbnailKey = rr.getInt(2);

	    if (thumbnailKey != null)
	    {
	        image = (DTO) this.getFullInfo (thumbnailKey.intValue());

	        // only add images we were able to retrieve

	        if ((image != null) && (!image.isEmpty()))
	        {
	            images.add (image);
	        }
	    }
	}
	nav.close();

	data.set (DTOConstants.Images, images);
	return data;
    }

    // -----------------------------------------------------------------------

    /** returns the key of the full-size image which corresponds to the given
    *    thumbnail key (simply returns the given one if it is for the
    *    full-size)
    */
    public int getFullSizeKey (int thumbnailKey) throws DBException
    {
        return this.getSingleKey (FULLSIZE_KEY, thumbnailKey);
    }

    // -----------------------------------------------------------------------

    /** returns the key of the thumbnail image which corresponds to the given
    *    full-size key (simply returns the given one if it is for the
    *    thumbnail)
    */
    public int getThumbnailKey (int fullsizeKey) throws DBException
    {
        return this.getSingleKey (THUMBNAIL_KEY, fullsizeKey);
    }

    // -----------------------------------------------------------------------

    /** returns the key of the image found by 'query' which corresponding to
    *    the given one (simply returns 'imageKey' if no matches)
    */
    private int getSingleKey (String query, int imageKey) throws DBException
    {
	Integer key = null;
	ResultsNavigator nav = null;
	RowReference rr = null;

	nav = this.sqlDM.executeQuery (Sprintf.sprintf (query, imageKey));

	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    key = rr.getInt(1);

	    if (key == null)
	    {
	        key = new Integer(imageKey);
	    }
	}
	nav.close();

	this.timeStamp ("Retrieved corresponding image key");
	return key.intValue();
    }

    // -----------------------------------------------------------------------

    /* ----------------------------------------------------------------------
    ** class variables -- used to hold standard SQL statements, so we don't
    ** need to re-join the Strings in each thread of the servlet.  For each
    ** one, we note the pieces that need to be filled in using Sprintf.
    ** ----------------------------------------------------------------------
    */

    /* retrieves an image's associated alleles
    ** fill in: image key (int)
    */
    private static String ALLELES_FOR_IMAGE = 
        "SELECT DISTINCT a._Allele_key, "
	+ "    a.symbol, "
	+ "    a.name, "
	+ "    m.name "
	+ "FROM IMG_ImagePane pane, "
	+ "    IMG_ImagePane_Assoc assoc, "
	+ "    ALL_Allele a, "
	+ "    MRK_Marker m "
	+ "WHERE pane._Image_key = %d "
	+ "    AND pane._ImagePane_key = assoc._ImagePane_key "
	+ "    AND assoc._MGIType_key = " + DBConstants.MGIType_Allele
	+ "    AND assoc._Object_key = a._Allele_key "
	+ "    AND a._Marker_key *= m._Marker_key "
	+ "ORDER BY a.symbol, a.name";

    /* returns the key for the full-size image which corresponds to the given
    ** thumbnail image key.  If there is no full-size image, no rows are
    ** returned.
    ** fill in: thumbnail image key (int)
    */
    private static String FULLSIZE_KEY =
        "SELECT _Image_key FROM IMG_Image WHERE _ThumbnailImage_key = %d";

    /** get the allele combinations for each genotype associated with a given
    **    image key
    ** fill in: image key (int)
    */
    private static String GENOTYPE_ALLELE_COMBINATIONS = 
        "SELECT DISTINCT gg._Genotype_key, "
	+ "    mnc.note "
	+ "FROM IMG_ImagePane pane, "
	+ "    IMG_ImagePane_Assoc assoc, "
	+ "    GXD_Genotype gg, "
	+ "    MGI_Note mn, "
	+ "    MGI_NoteChunk mnc, "
	+ "    MGI_NoteType mnt "
	+ "WHERE pane._Image_key = %d "
	+ "    AND pane._ImagePane_key = assoc._ImagePane_key "
	+ "    AND assoc._MGIType_key = " + DBConstants.MGIType_Genotype
	+ "    AND assoc._Object_key = gg._Genotype_key "
	+ "    AND gg._Genotype_key = mn._Object_key "
	+ "    AND mn._MGIType_key = " + DBConstants.MGIType_Genotype
	+ "    AND mn._NoteType_key = mnt._NoteType_key "
	+ "    AND mnt.noteType = 'Combination Type 3' "
	+ "    AND mn._Note_key = mnc._Note_key "
	+ "ORDER BY mnc.sequenceNum";

    /** get the strain for each genotype associated with a given image key
    ** fill in: image key (int)
    */
    private static String GENOTYPE_STRAINS =
        "SELECT DISTINCT gg._Genotype_key, "
	+ "    ps.strain "
	+ "FROM IMG_ImagePane pane, "
	+ "    IMG_ImagePane_Assoc assoc, "
	+ "    GXD_Genotype gg, "
	+ "    PRB_Strain ps "
	+ "WHERE pane._Image_key = %d "
	+ "    AND pane._ImagePane_key = assoc._ImagePane_key "
	+ "    AND assoc._MGIType_key = " + DBConstants.MGIType_Genotype
	+ "    AND assoc._Object_key = gg._Genotype_key "
	+ "    AND gg._Strain_key *= ps._Strain_key ";

    /* retrieves basic information for a single image
    ** fill in: image key (int)
    */
    private static String IMAGE_BASIC_INFO = 
        "SELECT image._Image_key, "
	+ "    image.xDim, "
	+ "    image.yDim, "
	+ "    image.figureLabel, "
	+ "    image._ThumbnailImage_key, "
	+ "    acc.accID, "
	+ "    acc._LogicalDB_key, "
	+ "    acc.numericPart "
	+ " FROM IMG_Image image, "
	+ "    ACC_Accession acc "
	+ " WHERE image._Image_key = %d "
	+ "    AND image._Image_key = acc._Object_key "
	+ "    AND acc._MGIType_key = " + DBConstants.MGIType_Image
	+ "    AND acc.prefixPart = 'PIX:' "
	+ "    AND acc.preferred = 1 ";

    /** retrieve the image key corresponding to the given accession ID
    ** fill in: image ID (String)
    */
    private static String IMAGE_KEY_FOR_ID =
        "SELECT aa._Object_key "
	+ "FROM ACC_Accession aa "
	+ "WHERE aa.accID = '%s' "
	+ "    AND aa._MGIType_key = " + DBConstants.MGIType_Image;

    /* returns all notes associated with a single image.
    ** fill in: image key (integer)
    */
    private static String IMAGE_NOTES =
        "SELECT mn._Object_key, "
	+ "    mnt._NoteType_key, "
	+ "    mnt.noteType, "
	+ "    mnc.note "
	+ "FROM MGI_Note mn, "
	+ "    MGI_NoteChunk mnc, "
	+ "    MGI_NoteType mnt "
	+ "WHERE mn._Object_key = %d "
	+ "    AND mn._MGIType_key = " + DBConstants.MGIType_Image
	+ "    AND mn._NoteType_key = mnt._NoteType_key "
	+ "    AND mn._Note_key = mnc._Note_key "
	+ "ORDER BY mnc.sequenceNum";

    /* returns all image keys associated with a given allele or with genotypes
    **    involving that allele.  The Union will guarantee distinctness.
    ** fill in: allele key (String), allele key (String)
    */
    private static String IMAGES_FOR_ALLELE =
	"SELECT pane._Image_key, image._ThumbnailImage_key "
	+ "FROM IMG_ImagePane_Assoc assoc, "
	+ "    IMG_ImagePane pane, "
	+ "    IMG_Image image "
	+ "WHERE assoc._Object_key = %s "
	+ "    AND assoc._MGIType_key = " + DBConstants.MGIType_Allele
	+ "    AND assoc._ImagePane_key = pane._ImagePane_key "
	+ "    AND pane._Image_key = image._Image_key "
	+ "UNION "
	+ "SELECT pane._Image_key, image._ThumbnailImage_key "
	+ "FROM IMG_ImagePane_Assoc assoc, "
	+ "    IMG_ImagePane pane, "
	+ "    GXD_AlleleGenotype gag, "
	+ "    IMG_Image image "
	+ "WHERE gag._Allele_key = %s "
	+ "    AND gag._Genotype_key = assoc._Object_key "
	+ "    AND assoc._MGIType_key = " + DBConstants.MGIType_Genotype
	+ "    AND pane._Image_key = image._Image_key "
	+ "    AND assoc._ImagePane_key = pane._ImagePane_key";
    
    /* returns the image keys of the primary image and its thumbnail, for
    ** a given allele
    ** fill in: allele key (int)
    */
    private static String PRIMARY_THUMBNAIL_ALLELE =
        "SELECT image._Image_key, "
	+ "    image._ThumbnailImage_key "
	+ "FROM IMG_ImagePane_Assoc assoc, "
	+ "    IMG_ImagePane pane, "
	+ "    IMG_Image image "
	+ "WHERE assoc.isPrimary = 1 "
	+ "    AND assoc._ImagePane_key = pane._ImagePane_key "
	+ "    AND pane._Image_key = image._Image_key "
	+ "    AND assoc._MGIType_key = " + DBConstants.MGIType_Allele
	+ "    AND assoc._Object_key = %d ";

    /* returns the image keys of the primary image and its thumbnail, for
    ** a given genotype
    ** fill in: genotype key (int)
    */
    private static String PRIMARY_THUMBNAIL_GENOTYPE =
        "SELECT image._Image_key, "
	+ "    image._ThumbnailImage_key "
	+ "FROM IMG_ImagePane_Assoc assoc, "
	+ "    IMG_ImagePane pane, "
	+ "    IMG_Image image "
	+ "WHERE assoc.isPrimary = 1 "
	+ "    AND assoc._ImagePane_key = pane._ImagePane_key "
	+ "    AND pane._Image_key = image._Image_key "
	+ "    AND assoc._MGIType_key = " + DBConstants.MGIType_Genotype
	+ "    AND assoc._Object_key = %d ";

    /* returns the key for the thumbnail which corresponds to the given
    ** image key, assuming we are given the key for a full-size image.  If
    ** there is no thumbnail, no rows are returned.
    ** fill in: integer image key
    */
    private static String THUMBNAIL_KEY =
        "SELECT _ThumbnailImage_key FROM IMG_Image WHERE _Image_key = %d";
}
