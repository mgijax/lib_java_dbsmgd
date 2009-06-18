package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;

import org.jax.mgi.dbs.mgd.MGITypeConstants;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.KeyNotFoundException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

/**
 * @is a FullCachedLookup for caching sequence Keys by their
 * sequence IDs
 * @has a RowDataCacheStrategy of type FULL_CACHE used for creating the
 * cache and performing the cache lookup 
 * @does provides a lookup method to return a sequence key given a sequence
 *       ID
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */
public class SequenceKeyLookupBySeqID extends FullCachedLookup
{
  // provide a static cache so that all instances share one cache
  private static HashMap cache = new HashMap();

  // indicator of whether or not the cache has been initialized
  private static boolean hasBeenInitialized = false;
  
  // logicalDB of the sequences to lookup
  private Integer ldbKey;
  
  // division of sequences, if applicable
  private String division = null;
  
  /**
   * constructor
   * @throws CacheException thrown if there is an error with the cache
   * @throws DBException thrown if there is an error accessing the db
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file
   */
  private SequenceKeyLookupBySeqID()
      throws CacheException, DBException, ConfigException
  {
    super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
  }
  
  /** 
   * constructor
   * constructs the cache for a given logical DB
   */
   public SequenceKeyLookupBySeqID(int ldbKey)
        throws CacheException, DBException, ConfigException
   {
       // call default constructor
       this();
       this.ldbKey = new Integer(ldbKey);
       initialize();
	   
   }
  /** 
   * constructor
   * constructs the cache with a given logicalDB key and  
   * division (for GenBank, RefSeq)
   */
   public SequenceKeyLookupBySeqID(int ldbKey, String division)
        throws CacheException, DBException, ConfigException
   {
       // call default constructor
       this();
       this.ldbKey = new Integer(ldbKey);
       this.division = division;
       initialize();
	   
   }

  /**
   * look up the sequence key for a seqID 
   * @param seqID the seqID to look up
   * @return the key value or null if not found
   * @throws CacheException thrown if there is an error accessing the cache
   * @throws ConfigException thrown if there is an error accessing the
   * configuration
   * @throws DBException thrown if there is an error accessing the
   * database
   * @throws TranslationException thrown if there is an error accessing the
   * translation tables
   * @throws KeyNotFoundException not actually thrown, returns null if not found
   */
  public Integer lookup(String seqID) throws CacheException,
      DBException, TranslationException, ConfigException
  {
      return (Integer)super.lookupNullsOk(seqID);
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
    String s = "SELECT a.accid, s._Sequence_key " +
	    "FROM ACC_Accession a, SEQ_Sequence s " +
	    "WHERE a._MGIType_key = " + MGITypeConstants.SEQUENCE + " " +
	    "AND a._LogicalDB_key = " + ldbKey.toString() + " " +
	    "AND a.preferred = 1 " +
	    "AND a._Object_key = s._Sequence_key "; // +
	    //"AND a.accid in ('AB000096', 'CZ258610', 'CW020141', 'CZ168783', 'CZ179568','CZ191407', 'CW020143', 'CW512050', 'CG409415', 'CL982786', 'AB187228', 'CG472644', 'ER896300', 'AY685649', 'AB195217', 'ER896299', 'CL631880', 'EF806820')";
    if (division == null) {
	return s;
    }
    else {
	return s + " AND division = " + "'" + division + "'";
    }
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
       return new KeyValue(row.getString(1), row.getInt(2));
      }
    }
    return new Interpreter();
  }
  /**
   * initialize the cache if not already initialized
   * @throws CacheException thrown if there is an error with the cache
   * @throws DBException thrown if there is an error accessing the db
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file
   */
  private void initialize()
      throws CacheException, DBException, ConfigException
  {
    // since cache is static make sure you do not reinit
    if (!hasBeenInitialized)
      initCache(cache);
    hasBeenInitialized = true;

  }
}
