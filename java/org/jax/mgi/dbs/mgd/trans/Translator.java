package org.jax.mgi.dbs.mgd.trans;

import java.util.HashMap;

import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.KeyedDataAttribute;
import org.jax.mgi.shr.cache.CachedLookup;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.KeyNotFoundException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.LookupException;
import org.jax.mgi.shr.cache.LookupExceptionFactory;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.dbs.mgd.MGD;
import org.jax.mgi.shr.config.ConfigException;

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
	private static HashMap lookupCache1 = new HashMap();
	
	/**
	 * cache for the MGITypeTableLookup
	 */
	private static HashMap lookupCache2 = new HashMap();
	
	/*
	 * the following constant definitions are exceptions thrown by this class
	 */
	private static String BadTranslationType =
		TranslationExceptionFactory.BadTranslationType;
	private static String KeyNotFound =
		LookupExceptionFactory.KeyNotFound;

  /**
   * constructor
   * @assumes nothing
   * @effects nothing
   * @throws CacheException thrown if there is an error 
   * @throws DBException
   * @throws ConfigException
   */
  public Translator(int translationType, int cacheType)
  throws CacheException, DBException, 
         ConfigException, LookupException, TranslationException
  {
    super(cacheType, 
    			SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
		this.translationType = new Integer(translationType);
		
		/**
		 * lookup the MGIType from the database
		 */
		MGITypeLookup lookup = new MGITypeLookup();
		HashMap map = null;
		try
		{
			map = lookup.lookup(translationType);
		}
		catch(KeyNotFoundException e)
		{
			TranslationExceptionFactory eFactory = new TranslationExceptionFactory();
			TranslationException e2 = 
				(TranslationException)eFactory.getException(BadTranslationType);
			e2.bind(translationType);
			throw e2;
		}
		Integer mgiType = (Integer)map.get(MGD.mgi_translationtype._mgitype_key);

		/**
		 * lookup the table information from the ACC_MGIType table
		 */		
		MGITypeTableLookup lookup2 = new MGITypeTableLookup();
		try
		{
			map = lookup2.lookup(mgiType.intValue());
		}
		catch (KeyNotFoundException e)
		{
			LookupExceptionFactory eFactory = new LookupExceptionFactory();
			throw eFactory.getLookupException(e);
		}
		targetTable = (String)map.get(MGD.acc_mgitype.tablename);
		targetIdentity = (String)map.get(MGD.acc_mgitype.identitycolumnname);
		targetPrimaryKey = (String)map.get(MGD.acc_mgitype.primarykeyname);	
  }

  /**
   * translate the given term into the known vocabulary term and its object
   * key in the MGD database
   * @assumes nothing
   * @effects nothing
   * @param the given term to translate
   * @return the translated term and its key in the MGD database
   */
  protected KeyValue translate(String term)
      throws LookupException
  {
    return (KeyValue)super.lookup(term, true);
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
               "       tg." + this.targetIdentity + ", " +
               "       tg." + this.targetPrimaryKey + " " +
               " FROM " + this.targetTable + " tg, " + 
               "     " + transTable + " tr " +
               " WHERE tr." + object_key + " = tg." + this.targetPrimaryKey +
               " AND tr." + transType + " = " + this.translationType;
    return s;
  }
  
	/**
	 * get the sql string used to add to the cache. This method
	 * is called by the CacheStrategy class when performing a lookup
	 * that is not in the cache.
	 * @return the sql string used to add to the cache
	 */
	public String getAddQuery(Object term)
	{
		String transTable = MGD.mgi_translation._name;
		String object_key = MGD.mgi_translation._object_key;
		String transType = MGD.mgi_translation._translationtype_key;
		String badName = MGD.mgi_translation.badname;
		
		String s = "SELECT tr." + badName + ", " +
               "       tg." + this.targetIdentity + ", " +
               "       tg." + this.targetPrimaryKey + " " +
               " FROM " + this.targetTable + " tg, " + 
               "     " + transTable + " tr " +
               " WHERE tr." + object_key + " = tg." + this.targetPrimaryKey +
               " AND tr." + transType + " = " + this.translationType +
               " AND tr." + badName + " = " + term.toString();
		return s;
	}
	/**
	 * return the sql to partially fill a cache 
	 * @return the value of null to indicate that this class does not 
	 * support partial initialization
	 */
	public String getPartialInitQuery()
	{
		return null;
	}

  /**
   * get the RowDataInterpreter for interpreting results from a database query.
   * @return the RowDataInterpreter
   */
  public RowDataInterpreter getRowDataInterpreter()
  {
    class Interpreter implements RowDataInterpreter {
      public Object interpret(RowReference row) throws DBException 
      {
				KeyedDataAttribute kda = 
				  new KeyedDataAttribute(row.getInt(3), row.getString(2));
				KeyValue kv = new KeyValue(row.getString(1), kda);
				return kda;
      }
    }
		return new Interpreter();
  }
  
  
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
	public class MGITypeLookup extends FullCachedLookup
	{

		/**
		 * constructor
		 * @assumes nothing
		 * @effects nothing
		 * @throws CacheException thrown if there is an error
		 * @throws DBException
		 * @throws ConfigException
		 */
		public MGITypeLookup()
		throws CacheException, DBException, ConfigException
		{
			super(SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
			setCache(lookupCache1);
		}

		/**
		 * lookup a MGI type for the given translation type
		 * @param the given translation type
		 * @return the HashMap containing the following data
		 * MGD.mgi_translationtype._mgitype_key 
		 */
		public HashMap lookup(int translationType)
				throws LookupException, KeyNotFoundException
		{
			Object o = lookup(new Integer(translationType));
			return (HashMap)o;
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
					HashMap map = new HashMap();
					map.put(MGD.mgi_translationtype._mgitype_key, row.getInt(2));
				 return new KeyValue(row.getInt(1), map);
				}
			}
			return new Interpreter();
		}
	}
	
	/**
	 *
	 * @is: a FullCachedLookup for caching table information from the
	 * ACC_MGIType table
	 * @has: a RowDataCacheStrategy of type FULL_CACHE used for creating the
	 * cache and performing the cache lookup.
	 * @does: provides a lookup method for getting a HashMap.
	 * @company: The Jackson Laboratory
	 * @author not attributable
	 * @version 1.0
	 */
	public class MGITypeTableLookup extends FullCachedLookup
	{

		/**
		 * constructor
		 * @assumes nothing
		 * @effects nothing
		 * @throws CacheException thrown if there is an error
		 * @throws DBException
		 * @throws ConfigException
		 */
		public MGITypeTableLookup()
		throws CacheException, DBException, ConfigException
		{
			super(SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
			setCache(lookupCache2);
		}

		/**
		 * lookup a MGI type for the given translation type
		 * @param the given translation type
		 * @return the HashMap with the following entries:
		 *      MGD.acc_mgitype.primarykeyname
		 *		  MGD.acc_mgitype.tablename
		 *		  MGD.acc_mgitype.identitycolumnname    
		 */
		public HashMap lookup(int translationType)
				throws KeyNotFoundException, LookupException
		{
			Object o = lookup(new Integer(translationType));
			return (HashMap)o;
		}

		/**
		 * get the sql string to use in fully intializing the cache. This method
		 * is called by the CacheStrategy class when performing initialization.
		 * @return the sql string to use in fully intializing the cache
		 */
		public String getFullInitQuery()
		{
			String s = "SELECT _translationType_key, " +				         "       primaryKeyName, " +
			           "       tablename, " +			           "       identityColumn " +
								 "FROM ACC_MGIType";
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
					HashMap map = new HashMap();
					map.put(MGD.acc_mgitype.primarykeyname, row.getString(2));
					map.put(MGD.acc_mgitype.tablename, row.getInt(3));
					map.put(MGD.acc_mgitype.identitycolumnname, row.getInt(4));
				 return new KeyValue(row.getInt(1), map);
				}
			}
			return new Interpreter();
		}
	} 
}





