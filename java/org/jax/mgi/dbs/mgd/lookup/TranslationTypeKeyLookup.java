package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;

import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.LookupException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.KeyNotFoundException;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.types.Converter;
import org.jax.mgi.dbs.mgd.TranslationTypeConstants;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.exception.MGIException;

/**
 *
 * @is: a FullCachedLookup for caching translation types and allowing
 * queries on the translation name to get back the record key 
 * @has: a RowDataCacheStrategy of type FULL_CACHE used for creating the
 * cache and performing the cache lookup.
 * @does: provides a lookup method for looking up translation type key by
 * translation name within a cache.
 * @company: The Jackson Laboratory
 * @author not attributable
 * @version 1.0
 */
public class TranslationTypeKeyLookup extends FullCachedLookup
{

  private static HashMap cache = new HashMap();

  /**
   * constructor
   * @assumes nothing
   * @effects nothing
   * @throws CacheException thrown if there is an error
   * @throws DBException
   * @throws ConfigException
   */
  public TranslationTypeKeyLookup()
  throws CacheException, DBException, 
         ConfigException, KeyNotFoundException
  {
    super(SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
    setCache(cache);
  }

  /**
   * lookup a MGI type for the given translation type
   * @param the given translation type
   * @return the MGI type
   */
  public Integer lookup(String translationName)
      throws LookupException
  {
    Object o = lookup(translationName);
    return (Integer)o;
  }

  /**
   * get the sql string to use in fully intializing the cache. This method
   * is called by the CacheStrategy class when performing initialization.
   * @return the sql string to use in fully intializing the cache
   */
  public String getFullInitQuery()
  {
    String s = "SELECT _TranslationType_key, translationType " +
               "FROM MGI_TranslationType";
    return s;
  }

  /**
   * get the RowDataInterpreter for interpreting results from a database query.
   * @return the RowDataInterpreter
   */
  public RowDataInterpreter getRowDataInterpreter()
  {
    class Interpreter implements RowDataInterpreter {
      public Object interpret(RowReference row) throws DBException {
       return new KeyValue(row.getString(2), row.getInt(1));
      }
    }
    return new Interpreter();
  }
}
