//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

/**
 * @is An object with attributes needed to create a new source record in
 *     the PRB_Source table.
 * @has
 *   <UL>
 *   <LI> Probe source attributes
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

public class ProbeSourceState
{
    /////////////////
    //  Variables  //
    /////////////////

    // Probe source attributes.
    //
    private Integer segmentTypeKey = null;
    private Integer vectorKey = null;
    private Integer organismKey = null;
    private Integer strainKey = null;
    private Integer tissueKey = null;
    private Integer genderKey = null;
    private Integer cellLineKey = null;
    private Integer refsKey = null;
    private String name = null;
    private String description = null;
    private String age = null;
    private Float ageMin = null;
    private Float ageMax = null;
    private String isCuratorEdited = null;


    /**
     * Constructs a new ProbeSourceState object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws Nothing
     */
    public ProbeSourceState ()
    {
    }


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
     * Get the vectorKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The vectorKey attribute
     * @throws Nothing
     */
    public Integer getVectorKey () { return vectorKey; }

    /**
     * Get the organismKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The organismKey attribute
     * @throws Nothing
     */
    public Integer getOrganismKey () { return organismKey; }

    /**
     * Get the strainKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The strainKey attribute
     * @throws Nothing
     */
    public Integer getStrainKey () { return strainKey; }

    /**
     * Get the tissueKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The tissueKey attribute
     * @throws Nothing
     */
    public Integer getTissueKey () { return tissueKey; }

    /**
     * Get the genderKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The genderKey attribute
     * @throws Nothing
     */
    public Integer getGenderKey () { return genderKey; }

    /**
     * Get the cellLineKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The cellLineKey attribute
     * @throws Nothing
     */
    public Integer getCellLineKey () { return cellLineKey; }

    /**
     * Get the refsKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The refsKey attribute
     * @throws Nothing
     */
    public Integer getRefsKey () { return refsKey; }

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
     * Get the description attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The description attribute
     * @throws Nothing
     */
    public String getDescription () { return description; }

    /**
     * Get the age attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The age attribute
     * @throws Nothing
     */
    public String getAge () { return age; }

    /**
     * Get the ageMin attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The ageMin attribute
     * @throws Nothing
     */
    public Float getAgeMin () { return ageMin; }

    /**
     * Get the ageMax attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The ageMax attribute
     * @throws Nothing
     */
    public Float getAgeMax () { return ageMax; }

    /**
     * Get the isCuratorEdited attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The isCuratorEdited attribute
     * @throws Nothing
     */
    public String getIsCuratorEdited () { return isCuratorEdited; }

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
     * Set the vectorKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pVectorKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setVectorKey (Integer pVectorKey) { vectorKey = pVectorKey; }

    /**
     * Set the organismKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pOrganismKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setOrganismKey (Integer pOrganismKey) { organismKey = pOrganismKey; }

    /**
     * Set the strainKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pStrainKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setStrainKey (Integer pStrainKey) { strainKey = pStrainKey; }

    /**
     * Set the tissueKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pTissueKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setTissueKey (Integer pTissueKey) { tissueKey = pTissueKey; }

    /**
     * Set the genderKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pGenderKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setGenderKey (Integer pGenderKey) { genderKey = pGenderKey; }

    /**
     * Set the cellLineKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pCellLineKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setCellLineKey (Integer pCellLineKey) { cellLineKey = pCellLineKey; }

    /**
     * Set the refsKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pRefsKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setRefsKey (Integer pRefsKey) { refsKey = pRefsKey; }

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
     * Set the description attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pPrimerSequence1 value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setDescription (String pDescription) { description = pDescription; }

    /**
     * Set the age attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pAge value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setAge (String pAge) { age = pAge; }

    /**
     * Set the ageMin attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pAgeMin value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setAgeMin (Float pAgeMin) { ageMin = pAgeMin; }

    /**
     * Set the ageMax attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pAgeMax value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setAgeMax (Float pAgeMax) { ageMax = pAgeMax; }

    /**
     * Set the isCuratorEdited attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pIsCuratorEdited value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setIsCuratorEdited (String pIsCuratorEdited) { isCuratorEdited = pIsCuratorEdited; }


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
        segmentTypeKey = null;
        vectorKey = null;
        organismKey = null;
        strainKey = null;
        tissueKey = null;
        genderKey = null;
        cellLineKey = null;
        refsKey = null;
        name = null;
        description = null;
        age = null;
        ageMin = null;
        ageMax = null;
        isCuratorEdited = null;
    }
}


//  $Log$
//  Revision 1.2  2003/09/30 16:52:31  dbm
//  Changed Double to Float
//
//  Revision 1.1  2003/09/19 17:43:24  dbm
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
