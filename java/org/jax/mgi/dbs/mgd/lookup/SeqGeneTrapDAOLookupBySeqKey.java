package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;

import org.jax.mgi.dbs.mgd.dao.SEQ_GeneTrapDAO;
import org.jax.mgi.dbs.mgd.dao.SEQ_GeneTrapState;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

/**
 * @is a FullCachedLookup for caching SEQ_GeneTrapDAOs by their 
 * Sequence keys
 * @has a RowDataCacheStrategy of type FULL_CACHE used for creating the
 *      cache and performing the cache lookup
 * @does provides a lookup method to return a SEQ_GeneTrapDAO given a sequence
 *       key
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */

public class SeqGeneTrapDAOLookupBySeqKey extends FullCachedLookup {
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
	public SeqGeneTrapDAOLookupBySeqKey()
		  throws CacheException, DBException, ConfigException {
		super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
		// since cache is static make sure you do not reinit
		if (!hasBeenInitialized) {
			initCache(cache);
		}
		hasBeenInitialized = true;
	}

	/**
	* look up SEQ_GeneTrapDAO for a sequence key
	* @param seqKey the seqKey to look up
	* @return SEQ_GeneTrapDAO for seqKey
	* @throws CacheException thrown if there is an error accessing the cache
	* @throws ConfigException thrown if there is an error accessing the
	* configuration
	* @throws DBException thrown if there is an error accessing the
	* database
	*/
	public SEQ_GeneTrapDAO lookup(Integer seqKey) throws CacheException,
			DBException, ConfigException {
		return (SEQ_GeneTrapDAO)super.lookupNullsOk(seqKey);
	}

	/**
	* get the full initialization query which is called by the CacheStrategy
	* class when performing cache initialization
	* @assumes nothing
	* @effects nothing
	* @return the full initialization query
	*/
	public String getFullInitQuery() {
		return new String("SELECT _Sequence_key, _TagMethod_key, _VectorEnd_key, "  +
			"_ReverseComp_key, goodHitCount, pointCoordinate, " +
			"_CreatedBy_key, _ModifiedBy_key, creation_date, " +
			"modification_date " +
			"FROM SEQ_GeneTrap");
	}

	/**
	* get the RowDataInterpreter which is required by the CacheStrategy to
	* read the results of a database query.
	* @assumes nothing
	* @effects nothing
	* @return the partial initialization query
	*/
	public RowDataInterpreter getRowDataInterpreter() {
		class Interpreter implements RowDataInterpreter {
		  public Object interpret(RowReference row) throws DBException {
			  Integer key = row.getInt(1);
			  SEQ_GeneTrapState state = new SEQ_GeneTrapState();
			  state.setSequenceKey(key);
			  state.setTagMethodKey(row.getInt(2));
			  state.setVectorEndKey(row.getInt(3));
			  state.setReverseCompKey(row.getInt(4));
			  state.setGoodHitCount(row.getInt(5));
			  state.setPointCoordinate(row.getDouble(6));
			  state.setCreatedByKey(row.getInt(7));
			  state.setModifiedByKey(row.getInt(8));
			  state.setCreationDate(row.getTimestamp(9));
			  state.setModificationDate(row.getTimestamp(10));

			  // not this table does not have a single primary key
			  SEQ_GeneTrapDAO dao = new SEQ_GeneTrapDAO(state);
				  return new KeyValue(key, dao);
		  }
		}
		return new Interpreter();
	}
}
