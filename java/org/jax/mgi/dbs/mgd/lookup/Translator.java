package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;

import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.cache.CachedLookup;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.KeyNotFoundException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.dbs.mgd.MGD;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.config.ConfigException;

/**
 *
 * @is a RowDataCacheHandler for caching translation terms
 * @has a RowDataCacheStrategy which can be set to type FULL_CACHE or
 * LAZY_CACHE.
 * @does provides a lookup method for translation terms stored and caches the
 * results
 * @company The Jackson Laboratory
 * @author not attributable
 * @version 1.0
 */
public class Translator extends CachedLookup
{
  /**
   * the table that stores the term
   */
  public String targetTable = null;
  /**
   * the column that the term is stored in
   */
  public String targetIdentity = null;
  /**
   * the name of the primary key column of the target table
   */
  public String targetPrimaryKey = null;
  /**
   * the translation type for this instance of translator
   */
  public Integer translationType = null;

  /**
   * cache for MGITypeLookup class
   */
  private static HashMap mgiTypeCache = new HashMap();

  /**
   * cache for the MGITypeTableLookup
   */
  private static HashMap mgiTypeTableCache = new HashMap();

  /*
   * the following constant definitions are exceptions thrown by this class
   */
  private static String NoTransTypeKey =
      TranslationExceptionFactory.NoTransTypeKey;
  private static String NoTransType =
      TranslationExceptionFactory.NoTransType;
  private static String NoMGIType =
      TranslationExceptionFactory.NoMGIType;
  private static String LookupErr =
      TranslationExceptionFactory.LookupErr;

  /**
   * constructor
   * @assumes nothing
   * @effects nothing
   * @param translationType the translation type from
   * org.jax.mgi.dbs.mgd.TranslationTypeConstants
   * @param cacheType the cache type from org.jax.mgi.shr.cache.CacheConstants
   * @throws CacheException thrown if there is an error accessing the cache
   * @throws ConfigException thrown if there is an error accessing the
   * configuration
   * @throws DBException thrown if there is an error accessing the
   * database
   * @throws TranslationException thrown if there is an error accessing the
   * translation tables
   */
  public Translator(int translationType, int cacheType)
  throws CacheException, DBException,
         ConfigException, TranslationException
  {
    super(cacheType,
          SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    this.translationType = new Integer(translationType);
    setup();
  }

  /**
   * constructor
   * @assumes nothing
   * @effects nothing
   * @param translationType the translation type from
   * org.jax.mgi.dbs.mgd.TranslationTypeConstants
   * @param cacheType the cache type from org.jax.mgi.shr.cache.CacheConstants
   * @throws CacheException thrown if there is an error accessing cache
   * @throws DBException thrown if there is an error with the db
   * @throws ConfigException thrown if there is an error accessing the
   * configuration
   * @throws CacheException thrown if there is an error accessing the cache
   * @throws ConfigException thrown if there is an error accessing the
   * configuration
   * @throws DBException thrown if there is an error accessing the
   * database
   * @throws TranslationException thrown if there is an error accessing the
   * translation tables
   */
  public Translator(String translationType, int cacheType)
  throws CacheException, DBException,
         ConfigException, TranslationException
  {
    super(cacheType,
          SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    // convert the translation type from a string value to it's db key value
    TranslationTypeKeyLookup ttkLookup = new TranslationTypeKeyLookup();
    try
    {
      this.translationType = ttkLookup.lookup(translationType);
    }
    catch (MGIException e)
    {
      TranslationExceptionFactory eFactory =
          new TranslationExceptionFactory();
      TranslationException e2 =
          (TranslationException) eFactory.getException(NoTransType, e);
      e2.bind(translationType);
      throw e2;
    }
    setup();

  }


  /**
   * translate the given term into the known object key in the MGD database
   * @assumes nothing
   * @effects nothing
   * @param term the given term to translate
   * @return the key in the MGD database for that term
   * @throws CacheException thrown if there is an error accessing the cache
   * @throws DBException thrown if there is an error accessing the
   * database
   * @throws TranslationException thrown if there is an error accessing the
   * translation tables
   */
  public Integer translate(String term)
      throws TranslationException, CacheException, DBException
  {
    return (Integer)super.lookupNullsOk(term);
  }

  /**
   * get the sql string to use in fully intializing the cache. This method
   * is called by the CacheStrategy class when performing initialization.
   * @return the sql string to use in fully intializing the cache
   */
  public String getFullInitQuery()
  {
    String transTable = MGD.mgi_translation._name;
    String object_key = MGD.mgi_translation._object_key;
    String transType = MGD.mgi_translation._translationtype_key;
    String badName = MGD.mgi_translation.badname;

    String s = "SELECT tr." + badName + ", " +
                       this.targetTable + "." + this.targetPrimaryKey + " " +
               " FROM " + this.targetTable + ", " +
               "     " + transTable + " tr " +
               " WHERE tr." + object_key + " = " +
                     this.targetTable + "." + this.targetPrimaryKey +
               " AND tr." + transType + " = " + this.translationType;
    return s;
  }

  /**
   * get the sql string used to add to the cache. This method
   * is called by the CacheStrategy class when performing a lookup
   * that is not in the cache.
   * @param term the term
   * @return the sql string used to add to the cache
   */
  public String getAddQuery(Object term) {
    String transTable = MGD.mgi_translation._name;
    String object_key = MGD.mgi_translation._object_key;
    String transType = MGD.mgi_translation._translationtype_key;
    String badName = MGD.mgi_translation.badname;
    // if query term has a single quote in it, it must be escaped
    String s = term.toString();
    s = s.replaceAll("'","''");
    String sql = "SELECT tr." + badName + ", " +
                       this.targetTable + "." + this.targetPrimaryKey + " " +
               " FROM " + this.targetTable + ", " +
               "     " + transTable + " tr " +
               " WHERE tr." + object_key + " = " +
                     this.targetTable + "." + this.targetPrimaryKey +
               " AND tr." + transType + " = " + this.translationType +
               " AND tr." + badName + " = '" + s + "'";
    return sql;
  }

  /**
   * return the sql to partially fill a cache
   * @return the value of null to indicate that this class does not
   * support partial initialization
   */
  public String getPartialInitQuery() {
    return null;
  }

  /**
   * get the RowDataInterpreter for interpreting results from a database query.
   * @return the RowDataInterpreter
   */
  public RowDataInterpreter getRowDataInterpreter()
  {
    class Interpreter
        implements RowDataInterpreter {
      public Object interpret(RowReference row) throws DBException {
        KeyValue kv = new KeyValue(row.getString(1), row.getInt(2));
        return kv;
      }
    }
    return new Interpreter();
  }

  /**
   * lookup and set the instance variables of this class through database
   * queries
   * @assumes nothing
   * @effects class instance variables will be set
   * @throws TranslationException thrown if there is the given translation
   * type is not found in the database
   * @throws LookupException thrown if there is the mgi type for the given
   * translation type was not found in the database
   * @throws ConfigException thrown if there is an error with lookup
   * configuration
   * @throws DBException thrown if there is an error accessing the database
   * @throws CacheException thrown if there is an error handling the cache
   */
  private void setup()
      throws TranslationException, ConfigException, DBException, CacheException
  {
    /**
     * lookup the MGIType from the database using the inner class MGITypeLookup
     */
    MGITypeLookup lookup = new MGITypeLookup();
    Integer mgiType = null;
    try {
      mgiType = lookup.lookup(translationType.intValue());
    }
    catch (MGIException e) {
      TranslationExceptionFactory eFactory = new TranslationExceptionFactory();
      TranslationException e2 =
          (TranslationException) eFactory.getException(NoMGIType, e);
      e2.bind(translationType.intValue());
      throw e2;
    }

    /**
     * lookup the table information from the ACC_MGIType table which includes
     * the table name, identity column name and the primary key name for
     * a given MGI type.
     */
    MGITypeTableLookup lookup2 = new MGITypeTableLookup();
    HashMap map = null;
    try {
      map = lookup2.lookup(mgiType.intValue());
    }
    catch (MGIException e) {
      TranslationExceptionFactory eFactory = new TranslationExceptionFactory();
      TranslationException e2 =
          (TranslationException) eFactory.getException(NoMGIType, e);
      e2.bind(mgiType.intValue());
      throw e2;
    }
    targetTable = (String) map.get(MGD.acc_mgitype.tablename);
    targetIdentity = (String) map.get(MGD.acc_mgitype.identitycolumnname);
    targetPrimaryKey = (String)map.get(MGD.acc_mgitype.primarykeyname);
  }


  /**
   *
   * @is a RowDataCacheHandler for caching MGI types obtained from a database
   * query
   * @has a RowDataCacheStrategy of type FULL_CACHE used for creating the
   * cache and performing the cache lookup.
   * @does provides a lookup method for gender translation terms stored
   * within an in-memory cache.
   * @company The Jackson Laboratory
   * @author not attributable
   * @version 1.0
   */
  private class MGITypeLookup
      extends FullCachedLookup
  {

    /**
     * constructor
     * @assumes nothing
     * @effects nothing
     * @throws CacheException thrown if there is an error with the cache
     * @throws DBException thrown if there is an error accessing the db
     * @throws ConfigException thrown if there is an error accessing the
     * configuration file
     */
    public MGITypeLookup()
        throws CacheException, DBException, ConfigException
    {
      super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
      // override the super class instance of the cache with a static one so
      // that all instances of the Translator will see the same cache
      initCache(mgiTypeCache);
    }

    /**
     * lookup a MGI type for the given translation type
     * @param translationType the translation type from
     * org.jax.mgi.dbs.mgd.TranslationTypeConstants
     * @return the HashMap containing the following data
     * MGD.mgi_translationtype._mgitype_key
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws DBException thrown if there is an error accessing the
     * database
     * @throws KeyNotFoundException thrown if the key is not found
     */
    public Integer lookup(int translationType) throws KeyNotFoundException,
    DBException, CacheException
    {
      Object o = super.lookup(new Integer(translationType));
      return (Integer) o;
    }

    /**
     * get the sql string to use in fully intializing the cache. This method
     * is called by the CacheStrategy class when performing initialization.
     * @return the sql string to use in fully intializing the cache
     */
    public String getFullInitQuery()
    {
      String s = "SELECT " +
                 MGD.mgi_translationtype._translationtype_key + ", " +
                 MGD.mgi_translationtype._mgitype_key + " " +
                 "FROM " + MGD.mgi_translationtype._name;
      return s;
    }

    /**
     * get the RowDataInterpreter for interpreting results from a database
     * query.
     * @return the RowDataInterpreter
     */
    public RowDataInterpreter getRowDataInterpreter()
    {
      class Interpreter
          implements RowDataInterpreter {
        public Object interpret(RowReference row) throws DBException {
          return new KeyValue(row.getInt(1), row.getInt(2));
        }
      }

      return new Interpreter();
    }
  }

  /**
   *
   * @is a FullCachedLookup for caching table information from the
   * ACC_MGIType table
   * @has a RowDataCacheStrategy of type FULL_CACHE used for creating the
   * cache and performing the cache lookup.
   * @does provides a lookup method for getting a HashMap.
   * @company The Jackson Laboratory
   * @author not attributable
   * @version 1.0
   */
  public class MGITypeTableLookup
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
    public MGITypeTableLookup() throws CacheException, DBException,
        ConfigException
    {
      super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
      // override the super class instance of the cache with a static one so
      // that all instances of the Translator will see the same cache
      initCache(mgiTypeTableCache);
    }

    /**
     * lookup a MGI type for the given translation type
     * @param translationType the translation type from
     * org.jax.mgi.dbs.mgd.TranslationTypeConstants
     * @return the HashMap with the following entries:
     *      MGD.acc_mgitype.primarykeyname
     *		  MGD.acc_mgitype.tablename
     *		  MGD.acc_mgitype.identitycolumnname
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws DBException thrown if there is an error accessing the
     * database
     * @throws KeyNotFoundException thrown if the key is not found
     */
    public HashMap lookup(int translationType)
        throws KeyNotFoundException, DBException, CacheException
    {
      Object o = super.lookup(new Integer(translationType));
      return (HashMap) o;
    }

    /**
     * get the sql string to use in fully intializing the cache. This method
     * is called by the CacheStrategy class when performing initialization.
     * @return the sql string to use in fully intializing the cache
     */
    public String getFullInitQuery()
    {
      String s = "SELECT _mgiType_key, " +
          "       primaryKeyName, " +
          "       tablename, " +
          "       identityColumnName " +
          "FROM ACC_MGIType";
      return s;
    }

    /**
     * get the RowDataInterpreter for interpreting results from a database
     * query.
     * @return the RowDataInterpreter
     */
    public RowDataInterpreter getRowDataInterpreter()
    {
      class Interpreter
          implements RowDataInterpreter {
        public Object interpret(RowReference row) throws DBException {
          HashMap map = new HashMap();
          map.put(MGD.acc_mgitype.primarykeyname, row.getString(2));
          map.put(MGD.acc_mgitype.tablename, row.getString(3));
          map.put(MGD.acc_mgitype.identitycolumnname, row.getString(4));
          return new KeyValue(row.getInt(1), map);
        }
      }

      return new Interpreter();
    }
  }

  /**
   *
   * @is a FullCachedLookup for caching translation types and allowing
   * queries on the translation name to get back the record key
   * @has a RowDataCacheStrategy of type FULL_CACHE used for creating the
   * cache and performing the cache lookup.
   * @does provides a lookup method for looking up translation type key by
   * translation name within a cache.
   * @company The Jackson Laboratory
   * @author not attributable
   * @version 1.0
   */
  public class TranslationTypeKeyLookup
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
    public TranslationTypeKeyLookup() throws CacheException, DBException,
        ConfigException
    {
      super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    }

    /**
     * lookup a MGI type for the given translation type
     * @param translationName the given translation type
     * @return the MGI type
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws DBException thrown if there is an error accessing the
     * database
     * @throws KeyNotFoundException thrown if the key is not found
     */
    public Integer lookup(String translationName)
        throws KeyNotFoundException, DBException, CacheException
    {
      Object o = super.lookup(translationName);
      return (Integer) o;
    }

    /**
     * get the sql string to use in fully intializing the cache. This method
     * is called by the CacheStrategy class when performing initialization.
     * @return the sql string to use in fully intializing the cache
     */
    public String getFullInitQuery()
    {
      String s = "SELECT " +
                 MGD.mgi_translationtype._translationtype_key + ", " +
                 MGD.mgi_translationtype.translationtype + " " +
                 "FROM " + MGD.mgi_translationtype._name;
      return s;
    }

    /**
     * get the RowDataInterpreter for interpreting results from a database
     * query.
     * @return the RowDataInterpreter
     */
    public RowDataInterpreter getRowDataInterpreter()
    {
      class Interpreter
          implements RowDataInterpreter {
        public Object interpret(RowReference row) throws DBException {
          return new KeyValue(row.getString(2), row.getInt(1));
        }
      }

      return new Interpreter();
    }
  }

}





