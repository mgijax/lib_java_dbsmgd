package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
    
import org.jax.mgi.dbs.mgd.MGITypeConstants;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.KeyNotFoundException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.MultiRowInterpreter;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

/**
 * @is a FullCachedLookup for caching marker keys  by their
 * sequence key associations
 * @has a RowDataCacheStrategy of type FULL_CACHE used for creating the
 * cache and performing the cache lookup
 * @does provides a lookup method for getting marker keys associated
 *  with a given sequence key
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */
public class MarkerKeyLookupBySeqKey extends FullCachedLookup {
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
	private MarkerKeyLookupBySeqKey()
	  throws CacheException, DBException, ConfigException {
		super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
	}

	/**
	* constructor
	* constructs the cache for a given sequence logical DB
	*/
	public MarkerKeyLookupBySeqKey(int ldbKey)
		throws CacheException, DBException, ConfigException {
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
	public MarkerKeyLookupBySeqKey(int ldbKey, String division)
		throws CacheException, DBException, ConfigException {
	   // call default constructor
	   this();
	   this.ldbKey = new Integer(ldbKey);
	   this.division = division;
	   initialize();

	}

	/**
	* look up the marker keys associated with a  sequence key
	* @param seqKey the seqKey to look up
	* @return Set of Integer marker keys
	* @throws CacheException thrown if there is an error accessing the cache
	* @throws ConfigException thrown if there is an error accessing the
	* configuration
	* @throws DBException thrown if there is an error accessing the
	* database
	* @throws TranslationException thrown if there is an error accessing the
	* translation tables
	* @throws KeyNotFoundException thrown if the key is not found
	*/
	public HashSet lookup(Integer seqKey) throws CacheException,
			DBException, TranslationException, ConfigException,
				KeyNotFoundException {
		return (HashSet)super.lookupNullsOk(seqKey);
	}

	/**
	* get the full initialization query which is called by the CacheStrategy
	* class when performing cache initialization
	* @assumes nothing
	* @effects nothing
	* @return the full initialization query
	*/
	public String getFullInitQuery() {
		String s = "SELECT distinct a2._Object_key as _Sequence_key, " +
				"a1._Object_key as_Marker_key  " +
			"FROM ACC_Accession a1, ACC_Accession a2, SEQ_Sequence s " +
			"WHERE a1._MGIType_key = " + MGITypeConstants.MARKER + " " +
			"AND a1._LogicalDB_key = " + ldbKey + " " +
			"AND a1.accid = a2.accid " +
			"AND a2._MGIType_key = " +  MGITypeConstants.SEQUENCE + " " +
			"AND a2._LogicalDB_key = " + ldbKey.toString() +
			"AND a2._Object_key = s._Sequence_key";
		if (division == null) {
			return s + " ORDER BY a2._Object_key";
		}
		else {
			return s + " AND s.division = " + "'" + division + "'" +
			   " ORDER BY a2._Object_key";
		}
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
				Integer sequenceKey = rd.sequenceKey;
				HashSet markerKeySet = new HashSet();
				for (Iterator i = v.iterator(); i.hasNext();) {
					rd = (RowData)i.next();
					markerKeySet.add(rd.markerKey);
				}
				return new KeyValue(sequenceKey, markerKeySet);
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
	  throws CacheException, DBException, ConfigException {
		// since cache is static make sure you do not reinit
		if (!hasBeenInitialized)
			initCache(cache);
			hasBeenInitialized = true;
	}
	/**
	 * Simple data object representing a row of data from the query
	 */
	class RowData {
		protected Integer sequenceKey;
		protected Integer markerKey;
		public RowData (RowReference row) throws DBException {
			sequenceKey = row.getInt(1);
			markerKey = row.getInt(2);
		}
	}
}
