package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
    
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.MultiRowInterpreter;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

/**
 * @is a FullCachedLookup for caching mutation terms  by their
 * allele key associations
 * @has a RowDataCacheStrategy of type FULL_CACHE used for creating the
 * cache and performing the cache lookup
 * @does provides a lookup method for getting a set of mutation terms 
 * given an allele key
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */
public class AlleleMutationLookupByAlleleKey extends FullCachedLookup
{
  // provide a static cache so that all instances share one cache
  private static HashMap cache = new HashMap();

  // indicator of whether or not the cache has been initialized
  private static boolean hasBeenInitialized = false;


   /**
   * constructor
   * @throws CacheException thrown if there is an error with the cache
   * @throws DBException thrown if there is an error accessing the db
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file
   */
  public AlleleMutationLookupByAlleleKey()
      throws CacheException, DBException, ConfigException {
    super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
  }

  /**
   * look up the set of mutations terms for an allele
   * @param alleleKey the allele key to look up
   * @return Set of mutation terms (Strings)
   * @throws CacheException thrown if there is an error accessing the cache
   * @throws ConfigException thrown if there is an error accessing the
   * configuration
   * @throws DBException thrown if there is an error accessing the
   * database
   * @throws TranslationException thrown if there is an error accessing the
   * translation tables
   */
  public HashSet lookup(Integer alleleKey) throws CacheException,
      DBException
  {
      return (HashSet)super.lookupNullsOk(alleleKey);
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
    String s = "SELECT a._Allele_key, t.term as mutation " +
	    "FROM ALL_Allele_Mutation a, VOC_Term t " + 
	    "WHERE a._Mutation_key = t._Term_key " +
	    "order by a._Allele_key";
    return s;
  }


  /**
   * get the RowDataInterpreter which is required by the CacheStrategy to
   * read the results of a database query.
   * @assumes nothing
   * @effects nothing
   * @return the partial initialization query
   */
  public RowDataInterpreter getRowDataInterpreter() {
    class Interpreter implements MultiRowInterpreter {
	public Object interpret(RowReference row) throws DBException {
	   //return new KeyValue(row.getString(1), row.getInt(2));
	      return new RowData(row);
	}
	public Object interpretKey(RowReference ref) throws DBException {
	    return ref.getInt(1);
	}

	public Object interpretRows(Vector v) {
	    RowData rd = (RowData)v.get(0);
	    Integer alleleKey = rd.alleleKey;
	    HashSet mutationTermSet = new HashSet();
	    for (Iterator it = v.iterator(); it.hasNext();) {
		rd = (RowData)it.next();
		mutationTermSet.add(rd.mutationTerm);
	    }
	    return new KeyValue(alleleKey, mutationTermSet);
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
   /**
     * Simple data object representing a row of data from the query
     */
    class RowData {
	protected Integer alleleKey;
        protected String mutationTerm;
        public RowData (RowReference row) throws DBException {
            alleleKey = row.getInt(1);
            mutationTerm = row.getString(2);
        }
    }
}

