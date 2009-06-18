package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.dbs.mgd.LogicalDBConstants;
import org.jax.mgi.dbs.mgd.MGITypeConstants;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.LazyCachedLookup;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

import java.util.HashMap;

/**
 * @is An object that knows how to look up a reference key to get
 * a J-Number
 * @has Nothing
 * @does
 * <UL>
 * <LI> Provides a method to look up a reference key to get a J-Number.
 * </UL>
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */

public class JNumberLookupByKey extends LazyCachedLookup
{
    
    // provide a static cache so that all instances share one cache
    private static HashMap cache = new HashMap();

    // indicator of whether or not the cache has been initialized
    private static boolean hasBeenInitialized = false;
    
    /**
     * Constructs a JNumberLookupByKey object.
     * @assumes Nothing
     * @effects Nothing
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     * @throws DBException thrown if there is an error accessing the
     * database
     */
    public JNumberLookupByKey ()
        throws CacheException, ConfigException, DBException {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
	if (!hasBeenInitialized) {
	  initCache(cache);
	}
	hasBeenInitialized = true;
    }


    /**
    * Looks up a reference key to find a J-Number.
    * @assumes Nothing
    * @effects Nothing
    * @param key The reference key to look up.
    * @return An String object containing the J-Number for the reference key or
    * a null if the reference key was not found.
    * @throws CacheException thrown if there is an error accessing the cache
    * @throws DBException thrown if there is an error accessing the
    * database
    */
    public String lookup (Integer key) throws DBException, CacheException
    {
        return (String)super.lookupNullsOk(key);
    }

    /**
     * Get the query to partially initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @return The query to partially initialize the cache.
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
        String stmt = "SELECT _Object_key, accID " +
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
     * @return The RowDataInterpreter object
     */
    public RowDataInterpreter getRowDataInterpreter()
    {
        class Interpreter implements RowDataInterpreter
        {
            public Object interpret (RowReference row)
                throws DBException
            {
                return new KeyValue(row.getInt(1), row.getString(2));
            }
        }
        return new Interpreter();
    }
}
