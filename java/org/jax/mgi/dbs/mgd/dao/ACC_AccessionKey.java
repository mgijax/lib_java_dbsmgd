//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.Table;

/**
 * @is An object that has the primary key for an Accession object.
 * @has
 *   <UL>
 *   <LI> The primary key for an Accession object
 *   </UL>
 * @does
 *   <UL>
 *   <LI> Provides a get method for its key.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class ACC_AccessionKey
{
    /////////////////
    //  Variables  //
    /////////////////

    // The primary key for an Accession object.
    //
    private Integer key;


    /**
     * Constructs a new ACC_AccessionKey object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws ConfigException
     * @throws DBException
     */
    public ACC_AccessionKey ()
        throws ConfigException, DBException
    {
        SQLDataManager sqlMgr = SQLDataManagerFactory.getShared("MGD");
        key = Table.getInstance("ACC_Accession",sqlMgr).getNextKey();
    }


    /**
     * Constructs a new ACC_AccessionKey object.
     * @assumes Nothing
     * @effects Nothing
     * @param pKey The value to set the primary key.
     * @throws Nothing
     */
    public ACC_AccessionKey (Integer pKey)
    {
        key = pKey;
    }


    /**
     * Get the key attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The key attribute.
     * @throws Nothing
     */
    public Integer getKey () { return key; }
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
