//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

import java.sql.Timestamp;

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

public class ACC_AccessionState
{
    /////////////////
    //  Variables  //
    /////////////////

    // ACC_Accession and ACC_AccessionReference attributes.
    //
    private String accID = null;
    private Integer _LogicalDB_key = null;
    private Integer _Object_key = null;
    private Integer _MGIType_key = null;
    private Boolean privateJ = null;
    private Boolean preferred = null;
    private Timestamp creation_date = null;
    private Timestamp modification_date = null;
    private Timestamp release_date = null;
    private Integer _Refs_key = null;


    /**
     * Constructs a new ACC_AccessionState object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws Nothing
     */
    public ACC_AccessionState ()
    {
    }


    /**
     * Get the accID attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The accID attribute.
     * @throws Nothing
     */
    public String getAccID () { return accID; }

    /**
     * Get the _LogicalDB_key attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The _LogicalDB_key attribute.
     * @throws Nothing
     */
    public Integer getLogicalDBKey () { return _LogicalDB_key; }

    /**
     * Get the _Object_key attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The _Object_key attribute.
     * @throws Nothing
     */
    public Integer getObjectKey () { return _Object_key; }

    /**
     * Get the _MGIType_key attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The _MGIType_key attribute.
     * @throws Nothing
     */
    public Integer getMGITypeKey () { return _MGIType_key; }

    /**
     * Get the privateJ attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The privateJ attribute.
     * @throws Nothing
     */
    public Boolean getPrivateJ () { return privateJ; }

    /**
     * Get the preferred attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The preferred attribute.
     * @throws Nothing
     */
    public Boolean getPreferred () { return preferred; }

    /**
     * Get the creation_date attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The creation_date attribute.
     * @throws Nothing
     */
    public Timestamp getCreationDate () { return creation_date; }

    /**
     * Get the modification_date attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The modification_date attribute.
     * @throws Nothing
     */
    public Timestamp getModificationDate () { return modification_date; }

    /**
     * Get the release_date attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The release_date attribute.
     * @throws Nothing
     */
    public Timestamp getReleaseDate () { return release_date; }

    /**
     * Get the _Refs_key attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The _Refs_key attribute.
     * @throws Nothing
     */
    public Integer getRefsKey () { return _Refs_key; }

    /**
     * Set the accID attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param in The value to set the attribute to.
     * @return Nothing
     * @throws Nothing
     */
    public void setAccID (String in) { accID = in; }

    /**
     * Set the _LogicalDB_key attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param in The value to set the attribute to.
     * @return Nothing
     * @throws Nothing
     */
    public void setLogicalDBKey (Integer in) { _LogicalDB_key = in; }

    /**
     * Set the _Object_key attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param in The value to set the attribute to.
     * @return Nothing
     * @throws Nothing
     */
    public void setObjectKey (Integer in) { _Object_key = in; }

    /**
     * Set the _MGIType_key attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param in The value to set the attribute to.
     * @return Nothing
     * @throws Nothing
     */
    public void setMGITypeKey (Integer in) { _MGIType_key = in; }

    /**
     * Set the privateJ attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param in The value to set the attribute to.
     * @return Nothing
     * @throws Nothing
     */
    public void setPrivateJ (Boolean in) { privateJ = in; }

    /**
     * Set the preferred attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param in The value to set the attribute to.
     * @return Nothing
     * @throws Nothing
     */
    public void setPreferred (Boolean in) { preferred = in; }

    /**
     * Set the creation_date attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param in The value to set the attribute to.
     * @return Nothing
     * @throws Nothing
     */
    public void setCreationDate (Timestamp in) { creation_date = in; }

    /**
     * Set the modification_date attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param in The value to set the attribute to.
     * @return Nothing
     * @throws Nothing
     */
    public void setModificationDate (Timestamp in) { modification_date = in; }

    /**
     * Set the release_date attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param in The value to set the attribute to.
     * @return Nothing
     * @throws Nothing
     */
    public void setReleaseDate (Timestamp in) { release_date = in; }

    /**
     * Set the refsKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param in The value to set the attribute to.
     * @return Nothing
     * @throws Nothing
     */
    public void setRefsKey (Integer in) { _Refs_key = in; }


    /**
     * Clear the attributes of this object.
     * @assumes Nothing
     * @effects Clears the attributes of this object.
     * @param None
     * @return Nothing
     * @throws Nothing
     */
    public void clear ()
    {
        accID = null;
        _LogicalDB_key = null;
        _Object_key = null;
        _MGIType_key = null;
        privateJ = null;
        preferred = null;
        creation_date = null;
        modification_date = null;
        release_date = null;
        _Refs_key = null;
    }
}


//  $Log$
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
