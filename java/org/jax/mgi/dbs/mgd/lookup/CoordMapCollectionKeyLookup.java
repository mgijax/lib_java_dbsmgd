//  $Header
//  $Name
package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;

import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.KeyNotFoundException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

/**
 * @is an object that knows how to look up a Map Collection name to get a
 * Map Collection key
 * @has a database query with which to load a full cache of Map Collection name
 * keys with Map Collection key values
 * @does provides a lookup method to lookup the key for a Map Collection names
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */

public class CoordMapCollectionKeyLookup extends FullCachedLookup{
    /**
     * Constructs a CoordMapCollectionKeyLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     * @throws DBException thrown if there is an error accessing the
     * database
     */
    public CoordMapCollectionKeyLookup ()
        throws CacheException, ConfigException, DBException {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    }

    /**
     * Looks up a Map Collection to find its key.
     * @assumes Nothing
     * @effects Nothing
     * @param mapCollection  The map collection to look up.
     * @return An Integer object containing the key for the map collection or a null
     *         if the map collection was not found.
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws DBException thrown if there is an error accessing the
     * database
     * @throws KeyNotFoundException thrown if the key is not found
     */
    public Integer lookup (String mapCollection)
        throws KeyNotFoundException, DBException, CacheException {
        return (Integer)super.lookup(mapCollection);
    }

    /**
     * Get the query to fully initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @return The query to fully initialize the cache.
     */
    public String getFullInitQuery ()
    {
        return new String("SELECT name, _Collection_Key FROM MAP_Coord_Collection");
    }


    /**
     * Get a RowDataInterpreter for creating a KeyValue object from a database
     * used for creating a new cache entry.
     * @assumes nothing
     * @effects nothing
     * @return The RowDataInterpreter object.
     */
    public RowDataInterpreter getRowDataInterpreter()
    {
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
}
// $Log
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





