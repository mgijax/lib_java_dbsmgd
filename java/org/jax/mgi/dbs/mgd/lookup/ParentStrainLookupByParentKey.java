
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
 * @is a FullCachedLookup for caching parent cell line keys by their
 * cell line names
 * @has a RowDataCacheStrategy of type FULL_CACHE used for creating the
 * cache and performing the cache lookup and has a Translator for translating
 * incoming names before performing the lookup
 * @does provides a lookup method to return a parent cell line key given a
 *       parent cell line name
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */
public class ParentStrainLookupByParentKey extends FullCachedLookup {
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
  public ParentStrainLookupByParentKey()
      throws CacheException, DBException, ConfigException {
    super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    // since cache is static make sure you do not reinit
    if (!hasBeenInitialized) {
      initCache(cache);
    }
    hasBeenInitialized = true;
  }
  /**
   * look up the parent strain name given a parent cell line key
   * @param parentKey the parent cell line key to lookup
   * @return parent strain
   * @throws CacheException thrown if there is an error accessing the cache
   * @throws ConfigException thrown if there is an error accessing the
   * configuration
   * @throws DBException thrown if there is an error accessing the
   * database
   * @throws KeyNotFoundException thrown if the key is not found
   */
  public String lookup(Integer parentKey) throws CacheException,
      DBException, ConfigException {

      return (String)super.lookupNullsOk(parentKey);
  }

  /**
   * get the full initialization query which is called by the CacheStrategy
   * class when performing cache initialization
   * @assumes nothing
   * @effects nothing
   * @return the full initialization query
   */
  public String getFullInitQuery() {
    String s = "SELECT distinct c._CellLine_key, s.strain " +
               "FROM ALL_CellLine c, PRB_Strain s " +
               "WHERE c.isMutant = 0 " +
			   "and c._Strain_key = s._Strain_key";
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

