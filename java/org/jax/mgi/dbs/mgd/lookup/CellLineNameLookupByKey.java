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
 * @is a FullCachedLookup for caching cell line names by their 
 *  database keys
 * @has a RowDataCacheStrategy of type FULL_CACHE used for creating the
 * cache and performing the cache lookup 
 * @does provides a lookup method to return a cell line name (ALL_CellLine.
 * cellLine) given a cell line key
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */
public class CellLineNameLookupByKey extends FullCachedLookup {
	// provide a static cache so that all instances share one cache
	private static HashMap cache = new HashMap();

	// indicator of whether or not the cache has been initialized
	private static boolean hasBeenInitialized = false;

	/**
	* constructor
	* @throws CacheException thrown if there is an error with the cache
	* @throws DBException thrown if there is an error accessing the db
	* @throws ConfigException thrown if there is an error accessing the
	* configuration file
	*/
	public CellLineNameLookupByKey()
	  throws CacheException, DBException, ConfigException {
		super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
		// since cache is static make sure you do not reinit
		if (!hasBeenInitialized) {
			initCache(cache);
		}
		hasBeenInitialized = true;
	}
	/**
	* look up the cell line name given a cell line database key
	* @param key the key to lookup
	* @return the cell line name
	* @throws CacheException thrown if there is an error accessing the cache
	* @throws ConfigException thrown if there is an error accessing the
	* configuration
	* @throws DBException thrown if there is an error accessing the
	* database
	* @throws KeyNotFoundException thrown if the key is not found
	*/
	public String lookup(Integer key) throws CacheException,
		  DBException, ConfigException, KeyNotFoundException {
		return (String)super.lookup(key);

	}

	/**
	* get the full initialization query which is called by the CacheStrategy
	* class when performing cache initialization
	* @assumes nothing
	* @effects nothing
	* @return the full initialization query
	*/
	public String getFullInitQuery() {
		String s = "SELECT " +
				   "c._CellLine_key, c.cellLine " +
				   "FROM ALL_CellLine c ";
		return s;
	}


	/**
	* get the RowDataInterpreter which is required by the CacheStrategy to
	* read the results of a database query.
	* @assumes nothing
	* @effects nothing
	* @return the partial initialization query
	*/
	public RowDataInterpreter getRowDataInterpreter() {
		class Interpreter implements RowDataInterpreter {
			public Object interpret(RowReference row) throws DBException {
				return new KeyValue(row.getInt(1), row.getString(2));
			}
		}
		return new Interpreter();
	}
}
