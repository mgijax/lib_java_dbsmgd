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
import org.jax.mgi.dbs.mgd.trans.Translator;
import org.jax.mgi.dbs.mgd.trans.TranslationException;
import org.jax.mgi.dbs.mgd.MGD;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.config.ConfigException;

/**
 * @is: a RowDataCacheHandler for caching strain terms
 * @has: a RowDataCacheStrategy of type FULL_CACHE used for creating the
 * cache and performing the cache lookup and has a Translator for translating
 * incoming terms before performing the lookup
 * @does: provides a lookup method for strain terms stored and caches the
 * results. Also translates lookup names to known vocabulary terms
 * @company: The Jackson Laboratory
 * @author not attributable
 * @version 1.0
 */
public class StrainKeyLookup extends FullCachedLookup
{
  // provide a static cache so that all instances share one cache
  private static HashMap cache = new HashMap();

  // cache the term found during translation so it can be provided on a
  // call to the getTranslatedTerm method
  private String translatedTerm = null;

  // the Translator object shared by all instances of this class
  private static Translator translator = null;

  /**
   * constructor
   * @throws CacheException thrown if there is an error with the cache
   * @throws DBException thrown if there is an error accessing the db
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file
   */
  public StrainKeyLookup()
      throws CacheException, DBException, ConfigException
  {
    super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    setCache(cache);
  }
  /**
   * look up the primary key for a Strain term in the PRB_Strain table
   * @param term the term to look up
   * @return the key value
   */
  public Integer lookup(String term) throws CacheException,
      DBException, TranslationException, ConfigException, KeyNotFoundException
  {
    if (translator == null)
    {
      translator = new Translator(TranslationTypeConstants.STRAIN,
                                  CacheConstants.FULL_CACHE);
    }
    // do a translation of the term and expect null if term is not found.
    // if the term is translated then we dont have to look it up in the
    // PRB_Strain table since we were given it from the translator
    KeyedDataAttribute data = translator.translate(term);
    if (data != null)
    {
      // a translation was successful so cache the translated term
      this.translatedTerm = data.getValue();
      return data.getKey();
    }
    else  // no translation found so lookup in PRB_Strain
    {
      this.translatedTerm = term;
      return (Integer)super.lookup(term);
    }
  }

  /**
   * return the value of the last term which was translated.
   * @assumes nothing
   * @effects nothing
   * @return the last translated term or if the term could not be
   * translated then the lookup term will be returned
   */
  public String getTranslatedTerm()
  {
    return translatedTerm;
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
               MGD.prb_strain._strain_key + ", " +
               MGD.prb_strain.strain + " " +
               "FROM " + MGD.prb_strain._name;
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
