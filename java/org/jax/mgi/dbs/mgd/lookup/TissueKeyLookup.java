package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;

import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.KeyedDataAttribute;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.CacheConstants;
import org.jax.mgi.dbs.mgd.TranslationTypeConstants;
import org.jax.mgi.dbs.mgd.MGD;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.cache.KeyNotFoundException;

/**
 * @is a RowDataCacheHandler for caching tissue terms and their
 * database key values
 * @has a RowDataCacheStrategy of type FULL_CACHE used for creating the
 * cache and performing the cache lookup and has a Translator for translating
 * incoming terms before performing the lookup
 * @does provides a lookup method for tissue terms and caches the results.
 * Also translates lookup names to known vocabulary terms
 * @company The Jackson Laboratory
 * @author not attributable
 * @version 1.0
 */
public class TissueKeyLookup extends FullCachedLookup
{
  // provide a static cache so that all instances share one cache
  private static HashMap cache = new HashMap();

  // the Translator object shared by all instances of this class
  private static Translator translator = null;

  /**
   * constructor
   * @throws CacheException thrown if there is an error with the cache
   * @throws DBException thrown if there is an error accessing the db
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file
   */
  public TissueKeyLookup()
      throws CacheException, DBException, ConfigException
  {
    super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    setCache(cache);
  }
  /**
   * look up the primary key for a Tissue term in the PRB_Tissue table
   * @param term the term to look up
   * @return the key value
   * @throws DBException thrown if there is an error accessing the database
   * @throws TranslationException thrown if there is an error using the
   * Translator
   * @throws ConfigException thrown if there is an error accessing the
   * configuration
   * @throws KeyNotFoundException thrown if the lookup did not find the value
   */
  public Integer lookup(String term) throws CacheException,
      DBException, TranslationException, ConfigException, KeyNotFoundException
  {
    if (translator == null)
    {
      translator = new Translator(TranslationTypeConstants.TISSUE,
                                  CacheConstants.FULL_CACHE);
    }
    // do a translation of the term and expect null if term is not found.
    // if the term is translated then we dont have to look it up in the
    // PRB_Source table since we were given it from the translator
    Integer key = translator.translate(term);
    if (key != null)
    {
      return key;
    }
    else  // no translation found so lookup directly in PRB_Tissue
    {
      return (Integer)super.lookup(term);
    }
  }

  /**
   * get the full initialization query which is called by the CacheStrategy
   * class when performing cache initialization
   * @assumes nothing
   * @effects nothing
   * @return the full initialization query
   */
  public String getFullInitQuery()
  {
    String s = "SELECT " +
               MGD.prb_tissue._tissue_key + ", " +
               MGD.prb_tissue.tissue + " " +
               "FROM " + MGD.prb_tissue._name;
    return s;
  }


  /**
   * get the RowDataInterpreter which is required by the CacheStrategy to
   * read the results of a database query.
   * @assumes nothing
   * @effects nothing
   * @return the partial initialization query
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
