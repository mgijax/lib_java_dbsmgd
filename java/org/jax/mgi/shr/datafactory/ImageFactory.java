package org.jax.mgi.shr.datafactory;

/*
* $Header$
* $Name$
*/

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

/**
* @module ImageFactory.java
* @author jsb
*/

/** The ImageFactory class contains methods which encapsulate knowledge of
*    the image-related portions of the schema.  These  allow for easy
*    retrieval of an image's attributes and related database objects.
* @is a factory for retrieving information related to images
* @has all data available for images, retrieved from a database
* @does queries a database to retrieve subsets of information about images.
*    Retrieval methods will always return a new <tt>DTO</tt>.  These DTO
*    objects may then be merged if needed.
*/
public class ImageFactory extends AbstractDataFactory
{
    /////////////////////
    // instance variables
    /////////////////////

    // provides parameters needed to configure an ImageFactory
    private DataFactoryCfg config = null;

    // provides access to the database
    private SQLDataManager sqlDM = null;

    ///////////////
    // Constructors
    ///////////////

    /** constructor; instantiates and initializes a new ImageFactory.
    * @param config provides parameters needed to configure an ImageFactory
    * @param sqlDM provides access to a database
    * @param logger provides logging capability
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    public ImageFactory (DataFactoryCfg config, SQLDataManager sqlDM,
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

    /** find a unique key identifying the image specified by the given 'parms'
    * @param parms set of parameters specifying which image we are seeking.
    *    Two keys in 'parms' are checked, in order of preference: "key" (image
    *    key as a String) and "id" (image accession ID).  Each key refers to
    *    an array of String values, with the desired value as the first String
    *    in the array.
    * @return int the unique _Image_key for the image identified in 'parms',
    *    or -1 if one cannot be found
    * @assumes nothing
    * @effects queries the database using 'sqlDM'
    * @throws DBException if there is a problem while attempting to query the
    *    database using 'sqlDM'
    */
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
    *    does not exist.
    * @param accID the accession ID for the desired image
    * @return int the unique _Image_key for the image identified by 'accID',
    *    or -1 if one cannot be found
    * @assumes nothing
    * @effects queries the database using 'sqlDM'
    * @throws DBException if there is a problem while attempting to query the
    *    database using 'sqlDM'
    */
    public int getKeyByID (String accID) throws DBException
    {
       int key = -1;			// image key to be returned to caller

	Integer imageKey = null;		// image key found in database
	ResultsNavigator nav = null;		// result set from db query
	RowReference rr = null;			// single row in 'nav'

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
    *    See <tt>getKey()</tt> method for description.
    * @return DTO which defines all image data fields, or an empty DTO if we
    *    cannot find the specified image in the database
    * @assumes nothing
    * @effects retrieves all image data by querying a database
    * @throws DBException if there is a problem querying the database
    * @notes defines in the DTO all fields defined by the getBasicInfo(),
    *    getNotes(), getAlleles(), and getGenotypes() methods.
    */
    public DTO getFullInfo (Map parms) throws DBException
    {
	return this.getFullInfo (this.getKey (parms));
    }

    // -----------------------------------------------------------------------

    /** retrieve the full set of data about one image; that is, everything
    *    needed to display an image detail page
    * @param imageKey unique _Image_key to find image in the database
    * @return DTO which defines all image data fields, or an empty DTO if we
    *    cannot find the specified image in the database
    * @assumes nothing
    * @effects retrieves all image data by querying a database
    * @throws DBException if there is a problem querying the database
    * @notes defines in the DTO all fields defined by the getBasicInfo(),
    *    getNotes(), getAlleles(), and getGenotypes() methods.
    */
    public DTO getFullInfo (int imageKey) throws DBException
    {
	DTO data = this.getBasicInfo (imageKey);

	// if we found no basic information for the specified key, then we
	// can just bail out without adding the other image data later

	if (data.isEmpty())
	{
	    return data;
	}

	// get the notes for this image, including caption and copyright info

	DTO notes = this.getNotes (imageKey);
	data.merge (notes);
	DTO.putDTO (notes);

	// if this is a thumbnail image, then it will have a non-null key to
	// an associated full-size image.  We would then need to get the
	// genotype and allele associations for that full-size image (as only
	// full-size images are included in associations).

	Integer fullsizeKey =
	    (Integer) data.get (DTOConstants.FullSizeImageKey);

	if (fullsizeKey != null)
	{
	    imageKey = fullsizeKey.intValue();
	}

	// get allele and genotype associations for this image (or for its
	// associated full-size version, if this is a thumbnail)...

	DTO alleles = this.getAlleles (imageKey);
	data.merge (alleles);
	DTO.putDTO (alleles);

	DTO genotypes = this.getGenotypes (imageKey);
	data.merge (genotypes);
	DTO.putDTO (genotypes);

	return data;
    }

    // -----------------------------------------------------------------------

    /** retrieve the set of basic data about one image
    * @param imageKey unique _Image_key to find image in the database
    * @return DTO which defines basic image data fields, or an empty DTO if we
    *    cannot find the specified image in the database
    * @assumes nothing
    * @effects retrieves basic image data by querying a database
    * @throws DBException if there is a problem querying the database
    * @notes defines in the returned DTO fields for the following items from
    *    DTOConstants:  FullSizeImageKey, ImageKey, Width, Height,
    *    FigureLabel, ThumbnailImageKey, AccID, LogicalDbKey, NumericPart.
    */
    public DTO getBasicInfo (int imageKey) throws DBException
    {
        DTO data = DTO.getDTO();	// data collected to be returned
	ResultsNavigator nav = null;	// result set for a query
	RowReference rr = null;		// one row in 'nav'
	Integer thumbnailKey = null;	// field _ThumbnailImage_key in 'rr'

	// key of full-size image associated with this image if it is a 
	// thumbnail image
	int fullsize = -1;

	nav = this.sqlDM.executeQuery (
	    Sprintf.sprintf (IMAGE_BASIC_INFO, imageKey) );

	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    thumbnailKey = rr.getInt(5);

	    // if this is a thumbnail image, then it will have a null value
	    // in the _ThumbnailImage_key field in the database.  If that is
	    // the case, then we also need to look up its corresponding
	    // full size image's _Image_key.

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

	nav.close();
	this.timeStamp (Sprintf.sprintf (
	    "Retrieved basic info for image %d", imageKey) );
	return data;
    }

    // -----------------------------------------------------------------------

    /** retrieve the set of notes for one image
    * @param imageKey unique _Image_key to find image in the database
    * @return DTO which defines note fields for the identified image, or an
    *    empty DTO if we cannot find the specified image in the database (or
    *    if the image has no notes)
    * @assumes nothing
    * @effects retrieves an image's notes by querying a database
    * @throws DBException if there is a problem querying the database
    * @notes defines in the returned DTO fields named according to the note
    *    types identified in the database.
    */
    public DTO getNotes (int imageKey) throws DBException
    {
	DTO image = DTO.getDTO();	// data to be returned

	ResultsNavigator nav = null;	// result set of query
	RowReference rr = null;		// one row in 'nav'
	String notetype = null;		// type of note in 'rr'
	String chunk = null;		// piece of note in 'rr'
	String noteSoFar = null;	// all chunks for current note type

	nav = this.sqlDM.executeQuery(Sprintf.sprintf(IMAGE_NOTES, imageKey));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    notetype = rr.getString(3);
	    chunk = rr.getString(4);

	    // add this 'chunk' to any others already retrieved for this
	    // 'notetype'

	    noteSoFar = (String) image.get (notetype);
	    if (noteSoFar == null)
	    {
	        noteSoFar = chunk;
	    }
	    else
	    {
	        noteSoFar = noteSoFar + chunk;
	    }

	    image.set (notetype, noteSoFar);
	}
	nav.close();

	this.timeStamp (Sprintf.sprintf ("retrieved notes for image %d",
	    imageKey) );
	return image;
    }

    // -----------------------------------------------------------------------

    /** retrieve the associated alleles for one image
    * @param imageKey unique _Image_key to find image in the database
    * @return DTO which defines associated alleles, or an empty DTO if we
    *    cannot find the specified image in the database
    * @assumes nothing
    * @effects retrieves associated alleles by querying a database
    * @throws DBException if there is a problem querying the database
    * @notes defines in the returned DTO fields for the following item from
    *    DTOConstants:  Alleles.  Its associated value is a List of DTO
    *    objects, each of which defines one allele with four fields from
    *    DTOConstants:  AlleleKey, AlleleSymbol, AlleleName, MarkerName.  The
    *    List will be empty if there are no associated alleles.
    */
    public DTO getAlleles (int imageKey) throws DBException
    {
	ResultsNavigator nav = null;		// result set for query
	RowReference rr = null;			// one row in 'nav' 
	DTO image = DTO.getDTO();		// data to be returned
	DTO allele = null;			// data for one allele
	ArrayList alleles = new ArrayList();	// list of associated alleles

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

    /** retrieve the associated genotypes for one image
    * @param imageKey unique _Image_key to find image in the database
    * @return DTO which defines associated genotypes, or an empty DTO if we
    *    cannot find the specified image in the database
    * @assumes nothing
    * @effects retrieves associated genotypes by querying a database
    * @throws DBException if there is a problem querying the database
    * @notes defines in the returned DTO fields for the following item from
    *    DTOConstants:  Genotypes.  Its associated value is a List of DTO
    *    objects, each of which defines one genotype with three fields from
    *    DTOConstants:  GenotypeKey, AlleleCombinations, and Strain.  The
    *    List will be empty if there are no associated genotypes.
    */
    public DTO getGenotypes (int imageKey) throws DBException
    {
	DTO image = DTO.getDTO();	// data to be returned 
	ResultsNavigator nav = null;	// result set for a query
	RowReference rr = null;		// one row in 'nav'
	String chunk = null;		// one piece of an allele combination
	String noteSoFar = null;	// all 'chunk's for an allele combo
	Integer genotypeKey = null;	// value of _Genotype_key

	// maps from a _Genotype_key to its allele combination String
	HashMap combinations = new HashMap();

	// our first query is to collect the allele combinations for each
	// genotype from the MGI_Note tables

	nav = this.sqlDM.executeQuery (
	    Sprintf.sprintf (GENOTYPE_ALLELE_COMBINATIONS, imageKey));

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();

	    genotypeKey = rr.getInt(1);
	    chunk = rr.getString(2);

	    // if we've already collected a chunk for this genotype, then
	    // append this one to it

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

	// now, our second query is to find all the genotype and strain
	// records.  This is where we collect the list of genotypes and
	// merge in the data from the 'combinations' retrieved above.

	String strain = null;			// strain for this genotype
	ArrayList genotypes = new ArrayList();	// list of genotypes
	DTO genotype = null;			// data for one genotype

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

	    genotypes.add (genotype);	// add to list of collected genotypes
	}
	nav.close();

	image.set (DTOConstants.Genotypes, genotypes);
	this.timeStamp (Sprintf.sprintf ("retrieved genotypes for image %d",
	    imageKey) );
	return image;
    }

    // -----------------------------------------------------------------------

    /** retrieve the set of basic data about an allele's primary image's
    *    thumbnail version
    * @param alleleKey unique _Allele_key to identify the allele in the
    *    database
    * @return DTO which defines all image data fields, or an empty DTO if the
    *    allele has no primary image
    * @assumes nothing
    * @effects retrieves image data by querying a database
    * @throws DBException if there is a problem querying the database
    * @notes defines in the returned DTO all fields from the getFullInfo()
    *    method
    */
    public DTO getPrimaryThumbnailForAllele (int alleleKey) throws DBException
    {
        ResultsNavigator nav = null;	// result set for a query
	RowReference rr = null;		// one row in 'nav'
	Integer imageKey = null;	// key of the primary image
	DTO image = null;		// data to be returned

	// look for the associated primary image's thumbnail key.  If we find
	// it, then go ahead and look up all the data for that image.

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
	    image = DTO.getDTO();
	}

	return image;
    }
    
    // -----------------------------------------------------------------------

    /** retrieve the set of basic data about an genotype's primary image's
    *    thumbnail version
    * @param genotypeKey unique _Genotype_key to identify the genotype in the
    *    database
    * @return DTO which defines all image data fields, or an empty DTO if the
    *    genotype has no primary image
    * @assumes nothing
    * @effects retrieves image data by querying a database
    * @throws DBException if there is a problem querying the database
    * @notes defines in the returned DTO all fields from the getFullInfo()
    *    method
    */
    public DTO getPrimaryThumbnailForGenotype (int genotypeKey)
        throws DBException
    {
        ResultsNavigator nav = null;	// set of results for query
	RowReference rr = null;		// one row in 'nav'
	Integer imageKey = null;	// key of associated image's thumbnail
	DTO image = null;		// data to be returned

	// look for the associated primary image's thumbnail key.  If we find
	// it, then go ahead and look up all the data for that image.

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
	    image = DTO.getDTO();
	}

	return image;
    }
    
    // -----------------------------------------------------------------------

    /** retrieve all thumbnail image data for the allele specified in 'parms',
    *    and for genotypes involving that allele.
    * @param parms set of parameters specifying which image we are seeking.
    *    Three keys in 'parms' are checked: "key" (allele key as a String),
    *    "id" (allele accession ID), and "symbol" (allele symbol).  Each key
    *    refers to an array of String values, with the desired value as the
    *    first String in the array.
    * @return DTO with information about the allele and its associated images,
    *    or an empty DTO if we cannot find the allele in the database
    * @assumes nothing
    * @effects queries the database using 'sqlDM'
    * @throws DBException if there is a problem while attempting to query the
    *    database using 'sqlDM'
    * @notes The DTO defines the fields set up by the AlleleFactory's
    *    getBasicInfo() and getGeneInformation() methods, as well as the
    *    Images field from DTOConstants.  The Images field points to a List
    *    of DTO objects, where each DTO describes a thumbnail image by
    *    defining all the fields noted for the getFullInfo() method.  The List
    *    will be empty if the allele has no associated images.
    */
    public DTO getImagesForAllele (Map parms) throws DBException
    {
	// use an allele factory to find the allele key for the given 'parms'

	AlleleFactory alleleFactory = new AlleleFactory (this.config,
	    this.sqlDM, this.logger);
	String keyString = alleleFactory.getKey (parms);

	// if we couldn't find an allele for those parameters, return an
	// empty DTO object

	if (keyString == null)
	{
	    return DTO.getDTO();
	}
	
	// otherwise, go ahead and return the images for this allele

        return this.getImagesForAllele (Integer.parseInt (keyString));
    }

    // -----------------------------------------------------------------------

    /** retrieve all thumbnail image data for the allele specified by
    *    'alleleKey', and for genotypes involving that allele.
    * @param alleleKey the _Allele_key uniquely identifying the desired allele
    * @return DTO with information about the allele and its associated images,
    *    or an empty DTO if we cannot find the allele in the database
    * @assumes nothing
    * @effects queries the database using 'sqlDM'
    * @throws DBException if there is a problem while attempting to query the
    *    database using 'sqlDM'
    * @notes The DTO defines the fields set up by the AlleleFactory's
    *    getBasicInfo() and getGeneInformation() methods, as well as the
    *    Images field from DTOConstants.  The Images field points to a List
    *    of DTO objects, where each DTO describes a thumbnail image by
    *    defining all the fields noted for the getFullInfo() method.  The List
    *    will be empty if the allele has no associated images.
    */
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

	// now, we can go ahead and query for images associated with that
	// allele or with genotypes involving that allele

	ResultsNavigator nav = null;	// result set for a query
	RowReference rr = null;		// one row in 'nav'
	Integer thumbnailKey = null;	// _ThumbnailImage_key in 'rr'
	Integer fullsizeKey = null;	// _Image_key in 'rr'
	DTO image = null;		// data for one associated image

	ArrayList images = new ArrayList();	// List of 'image' objects

	// due to limitations in Sprintf, we need to convert this 'alleleKey'
	// int to a String so we can insert it in two places in the SQL query

	String keyString = Integer.toString(alleleKey);
	nav = this.sqlDM.executeQuery (
	    Sprintf.sprintf (IMAGES_FOR_ALLELE, keyString, keyString) );

	this.timeStamp ("executed query to find images for allele");

	while (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    fullsizeKey = rr.getInt(1);
	    thumbnailKey = rr.getInt(2);

	    // if this associated (full-size) image has a corresponding
	    // thumbnail, then retrieve data for that thumbnail and add it
	    // to the list of associated images

	    if (thumbnailKey != null)
	    {
	        image = (DTO) this.getFullInfo (thumbnailKey.intValue());

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
    *    thumbnail key (simply returns the given one if it already is for the
    *    full-size)
    * @param thumbnailKey the _Image_key for a thumbnail image
    * @return int the _Image_key for the full-size image corresponding to the
    *    given 'thumbnailKey'
    * @assumes nothing
    * @effects queries the database using 'sqlDM'
    * @throws DBException if there are problems retrieving the data
    */
    public int getFullSizeKey (int thumbnailKey) throws DBException
    {
        return this.getSingleKey (FULLSIZE_KEY, thumbnailKey);
    }

    // -----------------------------------------------------------------------

    /** returns the key of the thumbnail image which corresponds to the given
    *    full-size key (simply returns the given one if it already is for the
    *    thumbnail)
    * @param fullsizeKey the _Image_key for a full-size image
    * @return int the _Image_key for the thumbnail image corresponding to the
    *    given 'fullsizeKey'
    * @assumes nothing
    * @effects queries the database using 'sqlDM'
    * @throws DBException if there are problems retrieving the data
    */
    public int getThumbnailKey (int fullsizeKey) throws DBException
    {
        return this.getSingleKey (THUMBNAIL_KEY, fullsizeKey);
    }

    ///////////////////////////
    // private instance methods
    ///////////////////////////

    /** returns the key of the image found by 'query' which corresponds to
    *    the given 'imageKey' (simply returns 'imageKey' if no matches)
    * @param query the SQL statement needed to find the corresponding key
    * @param imageKey the _Image_key for which we want to find its
    *    corresponding one
    * @return int the _Image_key for the image corresponding to the given
    *    'imageKey'
    * @assumes nothing
    * @effects queries the database using 'sqlDM'
    * @throws DBException if there are problems retrieving the data
    */
    private int getSingleKey (String query, int imageKey) throws DBException
    {
	Integer key = null;		// key found by query
	ResultsNavigator nav = null;	// result set for query
	RowReference rr = null;		// one row in 'nav'

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

    //////////////////////////
    // private class variables
    //////////////////////////

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

/*
* $Log$
* Revision 1.1.2.2  2005/08/02 18:56:04  jsb
* documentation and cleanup
*
* $Copyright$
*/
