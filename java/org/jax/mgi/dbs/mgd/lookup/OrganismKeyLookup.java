package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;

import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.cache.RowDataCacheHandler;
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
public class OrganismKeyLookup extends RowDataCacheHandler
{

  private static HashMap cache = new HashMap();

  public OrganismKeyLookup()
      throws CacheException, DBException, ConfigException
  {
    super(RowDataCacheHandler.FULL_CACHE,
          SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
    setCache(cache);
  }
  /**
   * look up the primary key for a Strain term in the PRB_Strain table
   * @param term the term to translate
   * @return
   */
  public Integer lookup(String term) throws CacheException, DBException
  {
    return (Integer)super.cacheStrategy.lookup(term, this.cache);
  }

  public String getFullInitQuery()
  {
    String s = "SELECT _Organism_key, commonName " +
               "FROM MGI_Organism ";
    return s;
  }

  public String getPartialInitQuery()
  {
    throw MGIException.getUnsupportedMethodException();
  }

  public String getAddQuery(Object addObject)
  {
    throw MGIException.getUnsupportedMethodException();
  }

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
