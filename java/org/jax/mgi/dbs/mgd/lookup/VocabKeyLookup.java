package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;

import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.KeyedDataAttribute;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.LazyCachedLookup;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.CacheConstants;
import org.jax.mgi.shr.cache.KeyNotFoundException;
import org.jax.mgi.shr.types.Converter;
import org.jax.mgi.dbs.mgd.TranslationTypeConstants;
import org.jax.mgi.dbs.mgd.trans.Translator;
import org.jax.mgi.dbs.mgd.trans.TranslationException;
import org.jax.mgi.dbs.mgd.MGD;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.exception.MGIException;

/**
 * @is: a RowDataCacheHandler for caching vocabulary terms and their index keys
 * @has: a RowDataCacheStrategy of type LAZY_CACHE used for creating the
 * cache and performing the cache lookup and has a Translator for translating
 * incoming terms before performing the lookup
 * @does: provides a lookup method for obtainning index keys from vocabulary
 * terms and caches the results. Also translates lookup terms to known
 * vocabulary terms.
 * @company: The Jackson Laboratory
 * @author not attributable
 * @version 1.0
 */
public class VocabKeyLookup extends LazyCachedLookup
{
  // the vocabulary type for this instance
  private int vocabType;

  // provide a static cache so that all instances share one cache
  private static HashMap cache = new HashMap();

  // cache the term found during translation so it can be provided on a
  // call to the getTranslatedTerm method
  private String translatedTerm = null;

  // the Translator object shared by all instances of this class
  private static Translator translator = null;

  // the cached mapping of vocabulary types to translation types
  private static HashMap translationTypeCache = new HashMap();

  // a lookup for translation types given a vocabulary type
  private TranslationTypeLookup translationTypeLookup = null;

  // the translation types for this instance which can be null if this
  // vocabulary term does not have translations
  private Integer translationType = null;

  // a boolean indicating whether or not this instance has translations
  private boolean translatable = true;

  /**
   * constructor
   * @throws CacheException thrown if there is an error with the cache
   * @throws DBException thrown if there is an error accessing the db
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file
   */
  public VocabKeyLookup(int vocabType)
      throws CacheException, DBException,
             ConfigException, TranslationException
  {
    super(SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
    this.vocabType = vocabType;
    setCache(cache);
    translationTypeLookup = new TranslationTypeLookup();
    // look up the translation type for this vocabulary type
    translationType = translationTypeLookup.lookup(this.vocabType);
    if (translationType == null) // no translations available
      translatable = false;
    else
      translator = new Translator(translationType.intValue(),
                                  CacheConstants.LAZY_CACHE);
  }

  /**
   * look up the primary key for a Strain term in the PRB_Strain table
   * @param term the term to look up
   * @return the key value
   */
  public Integer lookup(String term) throws CacheException,
      DBException, TranslationException, ConfigException
  {
    Integer key = null;
    if (translatable)  // do a translation first
    {
      // do a translation of the term and expect null if term is not found.
      // if the term is translated then we dont have to look it up in the
      // Voc_Term table since we were given the key already from the translator
      KeyedDataAttribute data = translator.translate(term);
      if (data != null) {
        // a translation was successful so cache the translated term
        this.translatedTerm = data.getValue();
        key = data.getKey();
      }
    }
    if (key == null) // was not found through translation
    {
        this.translatedTerm = term;
        key = (Integer)super.cacheStrategy.lookup(term, cache);
    }
    return key;
  }

  /**
   * return the value of the last term translated.
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
   * get the partial initialization query which is called by the CacheStrategy
   * class when performing cache initialization
   * @assumes nothing
   * @effects nothing
   * @return the full initialization query
   */
  public String getPartialInitQuery()
  {
    // return null to indicate that there should be no partial inititialization
    return null;
  }

  public String getAddQuery(Object o)
  {
    String sql = "SELECT " +
                    MGD.voc_term._term_key + ", " +
                    MGD.voc_term.term +
                 " FROM " +
                    MGD.voc_term._name + " " +
                 " WHERE " +
                    MGD.voc_term._vocab_key + " = " + this.vocabType +
                 " AND " +
                    MGD.voc_term.term + " = '" + (String)o + "'";
    return sql;
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

  /**
   *
   * @is: a FullCachedLookup for caching a mapping between translation types
   * and vocabulary types
   * @has: a RowDataCacheStrategy of type FULL_CACHE used for creating the
   * cache and performing the cache lookup.
   * @does: provides a lookup method for getting a HashMap.
   * @company: The Jackson Laboratory
   * @author not attributable
   * @version 1.0
   */
  private class TranslationTypeLookup
      extends FullCachedLookup {

    /**
     * constructor
     * @assumes nothing
     * @effects nothing
     * @throws CacheException thrown if there is an error
     * @throws DBException
     * @throws ConfigException
     */
    public TranslationTypeLookup() throws CacheException, DBException,
        ConfigException {
      super(SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
      // override the super class instance of the cache with a static one so
      // that all instances of the this class will use the same cache
      setCache(translationTypeCache);
    }

    /**
     * lookup the translation type for the given vocabulary type
     * @param the given vocabulary type
     * @return the translation type for this vocabulary
     */
    public Integer lookup(int vocabularyType)
        throws DBException, CacheException
    {
      Integer transType =
          (Integer)super.lookupNullsOk(new Integer(vocabularyType));
      return transType;
    }

    /**
     * get the sql string to use in fully intializing the cache. This method
     * is called by the CacheStrategy class when performing initialization.
     * @return the sql string to use in fully intializing the cache
     */
    public String getFullInitQuery() {
      String s = "SELECT _vocab_key, " +
          "              _translationType_key " +
          "       FROM MGI_TranslationType";
      return s;
    }

    /**
     * get the RowDataInterpreter for interpreting results from a database
     * query.
     * @return the RowDataInterpreter
     */
    public RowDataInterpreter getRowDataInterpreter() {
      class Interpreter implements RowDataInterpreter {
        public Object interpret(RowReference row) throws DBException {
          return new KeyValue(row.getInt(1), row.getInt(2));
        }
      }
      return new Interpreter();
    }
  }

}
