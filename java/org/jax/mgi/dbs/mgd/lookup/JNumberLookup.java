//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.dbs.mgd.LogicalDBConstants;
import org.jax.mgi.dbs.mgd.MGITypeConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.types.KeyValue;
import org.jax.mgi.shr.cache.RowDataCacheHandler;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.exception.MGIException;

/**
 * @is An object that knows how to look up a J-Number to find its
 *     reference key.
 * @has Nothing
 * @does
 *   <UL>
 *   <LI> Provides a method to look up a J-Number.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class JNumberLookup extends RowDataCacheHandler
{
    /**
     * Constructs a JNumberLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws Nothing
     */
    public JNumberLookup ()
        throws ConfigException, DBException, CacheException
    {
        super(LAZY_CACHE, SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
    }


    /**
    * Looks up a J-Number to find its reference key.
    * @assumes Nothing
    * @effects Nothing
    * @param jNumber The J-Number to look up.
    * @return An Integer object containing the reference key for the J-Number or
    *         a null if the J-Number was not found.
    * @throws Nothing
    */
    public Integer lookup (String jNumber)
        throws DBException, CacheException
    {
        Object obj = cacheStrategy.lookup(jNumber, cache);
        if (obj != null)
            return (Integer)obj;
        else
            return null;
    }


    /**
     * Get the query to fully initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The query to fully initialize the cache.
     * @throws Nothing
     */
    public String getFullInitQuery ()
    {
        throw MGIException.getUnsupportedMethodException();
    }


    /**
     * Get the query to partially initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The query to partially initialize the cache.
     * @throws Nothing
     */
    public String getPartialInitQuery ()
    {
        return null;
    }


    /**
     * Get the query to add an object to the database.
     * @assumes Nothing
     * @effects Nothing
     * @param addObject The object to add.
     * @return The query to add an object to the database.
     * @throws Nothing
     */
    public String getAddQuery (Object addObject)
    {
        String stmt = "SELECT accID, _Object_key " +
                      "FROM ACC_Accession " +
                      "WHERE accID = '" + addObject + "' and " +
                            "_LogicalDB_key = " + LogicalDBConstants.MGI + " and " +
                            "_MGIType_key = " + MGITypeConstants.REF + " and " +
                            "prefixPart = 'J:' and " +
                            "preferred = 1";

        return stmt;
    }


    /**
     * Get a RowDataInterpreter for creating a KeyValue object from a database
     * used for creating a new cache entry.
     * @assumes nothing
     * @effects nothing
     * @param None
     * @return The RowDataInterpreter object
     * @throws Nothing
     */
    public RowDataInterpreter getRowDataInterpreter()
    {
        return new InnerInterpreter();
    }


    /**
     * @is An object that knows how to create a KeyValue object for a row of
     *     data retrieved by this lookup.
     * @has Nothing
     * @does
     *   <UL>
     *   <LI> Provides a RowDataInterpreter implementation.
     *   </UL>
     * @company The Jackson Laboratory
     * @author dbm
     * @version 1.0
     */

    private class InnerInterpreter implements RowDataInterpreter
    {
        /**
         * Create a KeyValue object from a row of data.
         * @assumes nothing
         * @effects nothing
         * @param row The row reference.
         * @return The KeyValue object.
         * @throws Nothing
         */
        public Object interpret (RowReference row)
            throws DBException
        {
            String key = row.getString(1);
            Integer value = row.getInt(2);

            KeyValue keyValue = new KeyValue(key, value);
            return keyValue;
        }
    }
}


//  $Log$
//  Revision 1.4  2003/09/25 20:23:43  mbw
//  fixed imports for KeyValue
//
//  Revision 1.3  2003/09/25 17:55:29  dbm
//  Continued development
//
//  Revision 1.2  2003/09/23 13:13:01  dbm
//  Continued development
//
//  Revision 1.1  2003/09/18 14:26:12  dbm
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
