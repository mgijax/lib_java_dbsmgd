//  $Header
//  $Name

package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.cache.LazyCachedLookup;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.cache.KeyNotFoundException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.dbs.SchemaConstants;

/**
 * @is An object that knows how to look up an accession id for a given MGI Type,
 *     logicalDB, and preferred value to get its _Object_key
 * @has A query. A logicalDB, and MGIType and a preferred value.
 * @does
 *   <UL>
 *   <LI> Provides a method to look up the key for an accession id
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class AccessionLookup extends LazyCachedLookup {
    private int logicalDBKey;
    private int mgiTypeKey;
    private int preferred;

    /**
     * Constructs an AccessionLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @param logicalDB the logical db of the accession id to look up
     * @param mgiType the MGI type of the object key to return
     * @param preferred true (1)if preferred accession ids
     * @throws CacheException
     * @throws ConfigException
     * @throws DBException
     */

    public AccessionLookup(int logicalDBKey, int mgiTypeKey, int preferred)
        throws CacheException, ConfigException, DBException {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
        this.logicalDBKey = logicalDBKey;
        this.mgiTypeKey = mgiTypeKey;
        this.preferred = preferred;
    }

    /**
     * Get the query to partially initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The query to partially initialize the cache.
     * @throws Nothing
     */

    public String getPartialInitQuery() {
         return null;
    }

    /**
     * Get the query to add an object to the cache
     * @assumes Nothing
     * @effects Nothing
     * @param addObject The object to add.
     * @return The query to add an object to the cache
     * @throws Nothing
     */

    public String getAddQuery (Object add)
       {
           String query = "select accID, _Object_key " +
                         "from ACC_Accession " +
                         "where accID = '" + add + "' and " +
                         "_LogicalDB_key = " + logicalDBKey + " and " +
                         "_MGIType_key = " + mgiTypeKey + " and " +
                         "preferred = " + preferred;

           return query;
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

   public RowDataInterpreter getRowDataInterpreter() {
       class Interpreter implements RowDataInterpreter
       {
           public Object interpret (RowReference row)
               throws DBException
           {
               return new KeyValue(row.getString(1), row.getInt(2));
           }
       }
       return new Interpreter();
}

    /**
    * Looks up an accession id to find its object key.
    * @assumes Nothing
    * @effects Nothing
    * @param accid the accession id to look up.
    * @return The _object_key
    * @throws LookupException
    */
    public Integer lookup (String accid)
        throws DBException, CacheException, KeyNotFoundException{
        return (Integer)super.lookupNullsOk(accid);
    }
}

//  $Log

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
