 package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.jax.mgi.dbs.mgd.TranslationTypeConstants;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.cache.CacheConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.KeyNotFoundException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.MultiRowInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

/**
 * @is a FullCachedLookup for caching parent cell line keys by their 
 * cell line names
 * @has a RowDataCacheStrategy of type FULL_CACHE used for creating the
 * cache and performing the cache lookup and has a Translator for translating
 * incoming names before performing the lookup
 * @does provides a lookup method to return a parent cell line key given a 
 *       parent cell line name
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */
public class ParentCellLineKeyLookupByParent extends FullCachedLookup {
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
	public ParentCellLineKeyLookupByParent()
	  throws CacheException, DBException, ConfigException {
		super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
		// since cache is static make sure you do not reinit
		if (!hasBeenInitialized) {
			initCache(cache);
			hasBeenInitialized = true;
		}
	}
	/**
	* look up the primary key for a parent cell line name
	* @param name the cell line name to lookup
	* @return the key value
	* @throws CacheException thrown if there is an error accessing the cache
	* @throws ConfigException thrown if there is an error accessing the
	* configuration
	* @throws DBException thrown if there is an error accessing the
	* database
	* @throws TranslationException thrown if there is an error accessing the
	* translation tables
	* @throws KeyNotFoundException thrown if the key is not found
	*/
	public HashSet lookup(String name) throws CacheException,
	  DBException, TranslationException, ConfigException,
	  KeyNotFoundException {
		// since a Translator is not obtained until this method is called,
		// we need to check for null
		if (translator == null) {
			translator = new Translator(TranslationTypeConstants.PARENT_CELL_LINE,
									  CacheConstants.FULL_CACHE);
		}
		// do a translation of the name and expect null if term is not found.
		// if the name is translated to a key then we don't have to use the cache
		Integer key = translator.translate(name);
		if (key != null) {
			  HashSet set =  new HashSet();
			  set.add(key);
			  return set;
		}
		// no translation, so look in the cache
		else {
			return (HashSet)super.lookup(name);
		}
	}

	/**
	* get the full initialization query which is called by the CacheStrategy
	* class when performing cache initialization
	* @assumes nothing
	* @effects nothing
	* @return the full initialization query
	*/
	public String getFullInitQuery() {
		String s = "SELECT distinct cellLine, _CellLine_key " +
				   "FROM ALL_CellLine " +
				   "WHERE isMutant = 0";
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
				return ref.getString(1);
			}

			public Object interpretRows(Vector v) {
				RowData rd = (RowData)v.get(0);
				String parentName = rd.parentName;
				HashSet parentKeySet = new HashSet();
				for (Iterator i = v.iterator(); i.hasNext();) {
					rd = (RowData)i.next();
					parentKeySet.add(rd.parentKey);
				}
				return new KeyValue(parentName, parentKeySet);
			}
		}
		return new Interpreter();
		}
	 /**
	 * Simple data object representing a row of data from the query
	 */
	class RowData {
		protected String parentName;
		protected Integer parentKey;
		public RowData (RowReference row) throws DBException {
			parentName = row.getString(1);
			parentKey = row.getInt(2);
		}
	}
}
