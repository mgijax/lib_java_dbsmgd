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
import org.jax.mgi.shr.cache.KeyNotFoundException;
import org.jax.mgi.dbs.mgd.TranslationTypeConstants;
import org.jax.mgi.dbs.mgd.MGD;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.config.ConfigException;

/**
 * @is a RowDataCacheHandler for caching molecular source terms and their
 * database key values
 * @has a RowDataCacheStrategy of type FULL_CACHE used for creating the
 * cache and performing the cache lookup and has a Translator for translating
 * incoming terms before performing the lookup
 * @does provides a lookup method for molecular source terms and caches the
 * results. Also translates lookup names to known vocabulary terms
 * @company The Jackson Laboratory
 * @author not attributable
 * @version 1.0
 */
public class LibraryKeyLookup extends FullCachedLookup
{
  // provide a static cache so that all instances share one cache
  private static HashMap cache = new HashMap();

  // the Translator object shared by all instances of this class
  private static Translator translator = null;

  // indicator of whether or not the cache has been initialized
  private static boolean hasBeenInitialized = false;

  /**
   * constructor
   * @throws CacheException thrown if there is an error with the cache
   * @throws DBException thrown if there is an error accessing the db
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file
   */
  public LibraryKeyLookup()
      throws CacheException, DBException, ConfigException
  {
    super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    // since cache is static make sure you do not reinit
    if (!hasBeenInitialized)
      initCache(cache);
    hasBeenInitialized = true;
  }
  /**
   * look up the primary key for a Library name in the PRB_Source table
   * @param term the term to look up
   * @return the key value
   */
  public Integer lookup(String term) throws CacheException,
      DBException, TranslationException, ConfigException,
      KeyNotFoundException
  {
    // since a Translator is not obtained until this method is called,
    // we need to check for null
    if (translator == null)
    {
      translator = new Translator(TranslationTypeConstants.LIBRARY,
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
    else  // no translation found so lookup in PRB_Strain
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
               MGD.prb_source._source_key + ", " +
               MGD.prb_source.name + " " +
               "FROM " + MGD.prb_source._name + " " +
               "WHERE " + MGD.prb_source.name + " != null";
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
