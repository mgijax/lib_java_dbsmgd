package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;

import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.LookupException;
import org.jax.mgi.shr.types.KeyValue;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.types.Converter;
import org.jax.mgi.dbs.mgd.TranslationTypeConstants;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.exception.MGIException;

/**
 *
 * @is: a RowDataCacheHandler for caching Gender Translation terms
 * @has: a RowDataCacheStrategy of type FULL_CACHE used for creating the
 * cache and performing the cache lookup.
 * @does: provides a lookup method for gender translation terms stored
 * within an in-memory cache.
 * @company: The Jackson Laboratory
 * @author not attributable
 * @version 1.0
 */
public class TranslationTypeLookup extends FullCachedLookup
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
  public TranslationTypeLookup()
  throws CacheException, DBException, ConfigException
  {
    super(SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
    setCache(cache);
  }

  /**
   * lookup a MGI type for the given translation type
   * @param the given translation type
   * @return the MGI type
   */
  public Integer lookup(int translationType)
      throws LookupException
  {
    Object o = lookup(new Integer(translationType));
    return (Integer)o;
  }

  /**
   * get the sql string to use in fully intializing the cache. This method
   * is called by the CacheStrategy class when performing initialization.
   * @return the sql string to use in fully intializing the cache
   */
  public String getFullInitQuery()
  {
    String s = "SELECT _TranslationType_key, _MGIType_key " +
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
       return new KeyValue(new Integer(row.getInt(1)),
                           new Integer(row.getInt(2)));
      }
    }
    return new Interpreter();
  }
}
