//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

/**
 * @is An object with attributes needed to create a new accession record in
 *     the ACC_Accession table and a new accession reference record in the
 *     ACC_AccessionReference table.
 * @has
 *   <UL>
 *   <LI> Accession and accession reference attributes
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

public class AccessionState
{
    /////////////////
    //  Variables  //
    /////////////////

    // Accession attributes.
    //
    private String accID = null;
    private Integer logicalDBKey = null;
    private Integer objectKey = null;
    private Integer mgiTypeKey = null;
    private Boolean privateAcc = null;
    private Boolean preferred = null;

    // Accession reference attributes.
    //
    private Integer refsKey = null;


    /**
     * Constructs a new AccessionState object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws Nothing
     */
    public AccessionState ()
    {
    }


    /**
     * Get the accID attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The accID attribute
     * @throws Nothing
     */
    public String getAccID () { return accID; }

    /**
     * Get the logicalDBKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The logicalDBKey attribute
     * @throws Nothing
     */
    public Integer getLogicalDBKey () { return logicalDBKey; }

    /**
     * Get the objectKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The objectKey attribute
     * @throws Nothing
     */
    public Integer getObjectKey () { return objectKey; }

    /**
     * Get the mgiTypeKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The mgiTypeKey attribute
     * @throws Nothing
     */
    public Integer getMGITypeKey () { return mgiTypeKey; }

    /**
     * Get the privateAcc attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The privateAcc attribute
     * @throws Nothing
     */
    public Boolean getPrivateAcc () { return privateAcc; }

    /**
     * Get the preferred attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The preferred attribute
     * @throws Nothing
     */
    public Boolean getPreferred () { return preferred; }

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
     * Set the accID attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pAccID value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setAccID (String pAccID) { accID = pAccID; }

    /**
     * Set the logicalDBKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pLogicalDBKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setLogicalDBKey (Integer pLogicalDBKey) { logicalDBKey = pLogicalDBKey; }

    /**
     * Set the objectKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pObjectKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setObjectKey (Integer pObjectKey) { objectKey = pObjectKey; }

    /**
     * Set the mgiTypeKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pMGITypeKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setMGITypeKey (Integer pMGITypeKey) { mgiTypeKey = pMGITypeKey; }

    /**
     * Set the privateAcc attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pPrivateAcc value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setPrivateAcc (Boolean pPrivateAcc) { privateAcc = pPrivateAcc; }

    /**
     * Set the preferred attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pPreferred value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setPreferred (Boolean pPreferred) { preferred = pPreferred; }

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
     * Clear the attributes of this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return Nothing
     * @throws Nothing
     */
    public void clear ()
    {
        accID = null;
        logicalDBKey = null;
        objectKey = null;
        mgiTypeKey = null;
        privateAcc = null;
        preferred = null;
        refsKey = null;
    }
}


//  $Log$
//  Revision 1.2  2003/10/01 14:52:14  dbm
//  Use Strings to represent bit columns in DAO classes
//
//  Revision 1.1  2003/09/18 13:37:21  dbm
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
