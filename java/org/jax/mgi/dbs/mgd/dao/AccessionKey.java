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

public class AccessionKey
{
    /////////////////
    //  Variables  //
    /////////////////

    // The primary key for an Accession object.
    //
    private Integer primaryKey;


    /**
     * Constructs a new AccessionKey object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws ConfigException
     * @throws DBException
     */
    public AccessionKey ()
        throws ConfigException, DBException
    {
        SQLDataManager sqlMgr = SQLDataManagerFactory.getShared("MGD");
        primaryKey = Table.getInstance("ACC_Accession",sqlMgr).getNextKey();
    }


    /**
     * Constructs a new AccessionKey object.
     * @assumes Nothing
     * @effects Nothing
     * @param pKey The value to set the primary key.
     * @throws Nothing
     */
    public AccessionKey (Integer pKey)
    {
        primaryKey = pKey;
    }


    /**
     * Get the primaryKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The primaryKey attribute.
     * @throws Nothing
     */
    public Integer getPrimaryKey () { return primaryKey; }
}


//  $Log$
//  Revision 1.4  2003/10/02 18:49:58  dbm
//  Changed to use getInstance() method on Table class
//
//  Revision 1.3  2003/09/30 17:53:51  dbm
//  Change int to Integer for primary key
//
//  Revision 1.2  2003/09/23 13:23:52  dbm
//  Continued development
//
//  Revision 1.1  2003/09/19 17:43:19  dbm
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
