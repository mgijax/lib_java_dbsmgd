//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

/**
 * @is An object with attributes needed to create a new probe record in
 *     the PRB_Probe table.
 * @has
 *   <UL>
 *   <LI> Probe attributes
 *   </UL>
 * @does
 *   <UL>
 *   <LI> Provides get/set methods for its attribute(s).
 *   <LI> Provides a method to clear its attribute(s).
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class ProbeState
{
    /////////////////
    //  Variables  //
    /////////////////

    // Probe attributes.
    //
    private String name = null;
    private Integer derivedFrom = null;
    private Integer sourceKey = null;
    private Integer vectorKey = null;
    private Integer segmentTypeKey = null;
    private String primerSequence1 = null;
    private String primerSequence2 = null;
    private String regionCovered = null;
    private String regionCovered2 = null;
    private String insertSite = null;
    private String insertSize = null;
    private String repeatUnit = null;
    private String productSize = null;
    private Boolean moreProduct = null;


    /**
     * Constructs a new ProbeState object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws Nothing
     */
    public ProbeState ()
    {
    }


    /**
     * Get the name attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The name attribute
     * @throws Nothing
     */
    public String getName () { return name; }

    /**
     * Get the derivedFrom attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The derivedFrom attribute
     * @throws Nothing
     */
    public Integer getDerivedFrom () { return derivedFrom; }

    /**
     * Get the sourceKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The sourceKey attribute
     * @throws Nothing
     */
    public Integer getSourceKey () { return sourceKey; }

    /**
     * Get the vectorKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The vectorKey attribute
     * @throws Nothing
     */
    public Integer getVectorKey () { return vectorKey; }

    /**
     * Get the segmentTypeKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The segmentTypeKey attribute
     * @throws Nothing
     */
    public Integer getSegmentTypeKey () { return segmentTypeKey; }

    /**
     * Get the primerSequence1 attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The primerSequence1 attribute
     * @throws Nothing
     */
    public String getPrimerSequence1 () { return primerSequence1; }

    /**
     * Get the primerSequence2 attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The primerSequence2 attribute
     * @throws Nothing
     */
    public String getPrimerSequence2 () { return primerSequence2; }

    /**
     * Get the regionCovered attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The regionCovered attribute
     * @throws Nothing
     */
    public String getRegionCovered () { return regionCovered; }

    /**
     * Get the regionCovered2 attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The regionCovered2 attribute
     * @throws Nothing
     */
    public String getRegionCovered2 () { return regionCovered2; }

    /**
     * Get the insertSite attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The insertSite attribute
     * @throws Nothing
     */
    public String getInsertSite () { return insertSite; }

    /**
     * Get the insertSize attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The insertSize attribute
     * @throws Nothing
     */
    public String getInsertSize () { return insertSize; }

    /**
     * Get the repeatUnit attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The repeatUnit attribute
     * @throws Nothing
     */
    public String getRepeatUnit () { return repeatUnit; }

    /**
     * Get the productSize attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The productSize attribute
     * @throws Nothing
     */
    public String getProductSize () { return productSize; }

    /**
     * Get the moreProduct attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The moreProduct attribute
     * @throws Nothing
     */
    public Boolean getMoreProduct () { return moreProduct; }

    /**
     * Set the name attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pName value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setName (String pName) { name = pName; }

    /**
     * Set the derivedFrom attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pDerivedFrom value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setDerivedFrom (Integer pDerivedFrom) { derivedFrom = pDerivedFrom; }

    /**
     * Set the sourceKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pSourceKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setSourceKey (Integer pSourceKey) { sourceKey = pSourceKey; }

    /**
     * Set the vectorKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pVectorKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setVectorKey (Integer pVectorKey) { vectorKey = pVectorKey; }

    /**
     * Set the segmentTypeKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pSegmentTypeKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setSegmentTypeKey (Integer pSegmentTypeKey) { segmentTypeKey = pSegmentTypeKey; }

    /**
     * Set the primerSequence1 attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pPrimerSequence1 value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setPrimerSequence1 (String pPrimerSequence1) { primerSequence1 = pPrimerSequence1; }

    /**
     * Set the primerSequence2 attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pPrimerSequence2 value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setPrimerSequence2 (String pPrimerSequence2) { primerSequence2 = pPrimerSequence2; }

    /**
     * Set the regionCovered attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pRegionCovered value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setRegionCovered (String pRegionCovered) { regionCovered = pRegionCovered; }

    /**
     * Set the regionCovered2 attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pRegionCovered2 value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setRegionCovered2 (String pRegionCovered2) { regionCovered2 = pRegionCovered2; }

    /**
     * Set the insertSite attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pInsertSite value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setInsertSite (String pInsertSite) { insertSite = pInsertSite; }

    /**
     * Set the insertSize attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pInsertSize value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setInsertSize (String pInsertSize) { insertSize = pInsertSize; }

    /**
     * Set the repeatUnit attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pRepeatUnit value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setRepeatUnit (String pRepeatUnit) { repeatUnit = pRepeatUnit; }

    /**
     * Set the productSize attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pProductSize value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setProductSize (String pProductSize) { productSize = pProductSize; }

    /**
     * Set the moreProduct attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pMoreProduct value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setMoreProduct (Boolean pMoreProduct) { moreProduct = pMoreProduct; }


    /**
     * Clear the attributes of this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return Nothing
     * @throws Nothing
     */
    public void clear ()
    {
        name = null;
        derivedFrom = null;
        sourceKey = null;
        vectorKey = null;
        segmentTypeKey = null;
        primerSequence1 = null;
        primerSequence2 = null;
        regionCovered = null;
        regionCovered2 = null;
        insertSite = null;
        insertSize = null;
        repeatUnit = null;
        productSize = null;
        moreProduct = null;
    }
}


//  $Log$
//  Revision 1.2  2003/10/01 14:52:17  dbm
//  Use Strings to represent bit columns in DAO classes
//
//  Revision 1.1  2003/09/18 13:37:23  dbm
//  Initial version
//
//
/**************************************************************************
*
* Warranty Disclaimer and Copyright Notice
*
*  THE JACKSON LABORATORY MAKES NO REPRESENTATION ABOUT THE SUITABILITY OR
*  ACCURACY OF THIS SOFTWARE OR DATA FOR ANY PURPOSE, AND MAKES NO WARRANTIES,
*  EITHER EXPRESS OR IMPLIED, INCLUDING MERCHANTABILITY AND FITNESS FOR A
*  PARTICULAR PURPOSE OR THAT THE USE OF THIS SOFTWARE OR DATA WILL NOT
*  INFRINGE ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS, OR OTHER RIGHTS.
*  THE SOFTWARE AND DATA ARE PROVIDED "AS IS".
*
*  This software and data are provided to enhance knowledge and encourage
*  progress in the scientific community and are to be used only for research
*  and educational purposes.  Any reproduction or use for commercial purpose
*  is prohibited without the prior express written permission of The Jackson
*  Laboratory.
*
* Copyright \251 1996, 1999, 2002, 2003 by The Jackson Laboratory
*
* All Rights Reserved
*
**************************************************************************/
