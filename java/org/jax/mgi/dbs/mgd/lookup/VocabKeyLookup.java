package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;

import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.KeyedDataAttribute;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.CachedLookup;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.CacheConstants;
import org.jax.mgi.shr.cache.KeyNotFoundException;
import org.jax.mgi.dbs.mgd.MGD;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.config.ConfigException;

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
public class VocabKeyLookup extends CachedLookup
{
  // the vocabulary type for this instance
  private int vocabType;

  // the Translator object shared by all instances of this class
  private Translator translator = null;

  // a lookup for translation types given a vocabulary type
  private TranslationTypeLookup translationTypeLookup = null;

  // the translation types for this instance which can be null if this
  // vocabulary term does not have translations
  private Integer translationType = null;

  // a boolean indicating whether or not this instance has translations
  private boolean translatable = true;

  /**
   * constructor which creates by default a lazy caching strategy for the
   * vocabulary terms and a lazy caching strategy for the translations
   * @param vocabType the type of vocabulary which are listed in
   * org.jax.mgi.dbs.mgd.VocabularyTypeConstants
   * @throws CacheException thrown if there is an error with the cache
   * @throws DBException thrown if there is an error accessing the db
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file
   */
  public VocabKeyLookup(int vocabType)
      throws CacheException, DBException,
             ConfigException,
             TranslationException
  {
    super(CacheConstants.LAZY_CACHE,
          SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    this.vocabType = vocabType;
    translationTypeLookup = new TranslationTypeLookup();
    // look up the translation type for this vocabulary type
    translationType = translationTypeLookup.lookup(this.vocabType);
    if (translationType == null) // no translations available
      translatable = false;
    else
    {
      translator = new Translator(translationType.intValue(),
                                  CacheConstants.LAZY_CACHE);
      // make the translation and vocabulary cache the same
      translator.setCache(getCache());
    }
  }

  /**
   * constructor which accepts the type of caching strategy from
   * org.jax.mgi.shr.cache.CacheConstants for caching vocabulary terms and
   * for caching the translations
   * @param vocabType the type of vocabulary chosen from the list in
   * org.jax.mgi.dbs.mgd.VocabularyTypeConstants
   * @param vocabCacheType the type of cache chosen from the list in
   * org.jax.mgi.shr.cache.CacheConstants for the vocabulary lookup
   * @param translationCacheType the type of caching strategy chosen from
   * the list in org.jax.mgi.shr.cache.CacheConstants
   * @throws CacheException thrown if there is an error with the cache
   * @throws DBException thrown if there is an error accessing the db
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file
   */
  public VocabKeyLookup(int vocabType,
                        int vocabCacheType, int translationCacheType)
      throws CacheException, DBException,
             ConfigException,
             TranslationException
  {
    super(vocabCacheType, SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    this.vocabType = vocabType;
    //setCache(cache);
    translationTypeLookup = new TranslationTypeLookup();
    // look up the translation type for this vocabulary type
    translationType = translationTypeLookup.lookup(this.vocabType);
    if (translationType == null) // no translations available
      translatable = false;
    else
    {
      translator = new Translator(translationType.intValue(),
                                  translationCacheType);
      // make the translation and vocabulary cache the same
      translator.setCache(getCache());
    }
  }



  /**
   * look up the primary key for a Strain term in the PRB_Strain table
   * @param term the term to look up
   * @return the key value
   */
  public Integer lookup(String term) throws CacheException,
      DBException, TranslationException, ConfigException, KeyNotFoundException
  {
    Integer key = null;
    if (translatable)  // do a translation first
    {
      // do a translation of the term and expect null if term is not found.
      // if the term is translated then we dont have to look it up in the
      // Voc_Term table since we were given the key already from the translator
      key = (Integer)translator.translate(term);
    }
    if (key == null) // was not found through translation
    {
        key = (Integer)super.lookup(term);
    }
    return key;
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
    String sql = "SELECT " +
                    MGD.voc_term._term_key + ", " +
                    MGD.voc_term.term +
                 "FROM " + MGD.voc_term._name + " " +
                 "WHERE " +
                    MGD.voc_term._vocab_key + " = " + this.vocabType;
    return sql;
  }

  /**
   * get the partial initialization query which is called by the CacheStrategy
   * class when performing cache initialization
   * @assumes nothing
   * @effects nothing
   * @return the partial initialization query
   */
  public String getPartialInitQuery()
  {
    // return null to indicate that there should be no partial inititialization
    // when implementing a lazy cache
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
      extends FullCachedLookup
  {

    /**
     * constructor
     * @assumes nothing
     * @effects nothing
     * @throws CacheException thrown if there is an error
     * @throws DBException
     * @throws ConfigException
     */
    public TranslationTypeLookup() throws CacheException, DBException,
        ConfigException
    {
      super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
      // override the super class instance of the cache with a static one so
      // that all instances of the this class will use the same cache
      //setCache(translationTypeCache);
    }

    /**
     * lookup the translation type for the given vocabulary type
     * @param the given vocabulary type
     * @return the translation type for this vocabulary
     */
    public Integer lookup(int vocabularyType) throws DBException,
        ConfigException, CacheException
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
    public String getFullInitQuery()
    {
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
    public RowDataInterpreter getRowDataInterpreter()
    {
      class Interpreter implements RowDataInterpreter {
        public Object interpret(RowReference row) throws DBException {
          return new KeyValue(row.getInt(1), row.getInt(2));
        }
      }
      return new Interpreter();
    }
  }

}
