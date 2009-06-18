package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.dbs.mgd.LogicalDBConstants;
import org.jax.mgi.dbs.mgd.MGITypeConstants;
import org.jax.mgi.dbs.mgd.MGIRefAssocTypeConstants;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.MultiRowInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * @is An object that knows how to look the set PubMed Ids,  associated with an 
 * allele, by allele key
 * @has Nothing
 * @does
 * <UL>
 * <LI> Provides a method to look up a allele key to get a PubMed ID.
 * </UL>
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */

public class PubMedIDLookupByAlleleKey extends FullCachedLookup
{
    
    // provide a static cache so that all instances share one cache
    private static HashMap cache = new HashMap();

    // indicator of whether or not the cache has been initialized
    private static boolean hasBeenInitialized = false;
    
    /**
     * Constructs a PubMedIDLookupByAlleleKey object.
     * @assumes Nothing
     * @effects Nothing
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     * @throws DBException thrown if there is an error accessing the
     * database
     */
    public PubMedIDLookupByAlleleKey ()
        throws CacheException, ConfigException, DBException {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
	if (!hasBeenInitialized) {
	  initCache(cache);
	}
	hasBeenInitialized = true;
    }


    /**
    * Looks up a allele key to find a set of PubMed ID(s).
    * @assumes Nothing
    * @effects Nothing
    * @param key The allele key to look up.
    * @return An HashSet object containing the set ofPubMed ID for the allele key  
    *   If the allele key was not found, return an empty set
    * @throws CacheException thrown if there is an error accessing the cache
    * @throws DBException thrown if there is an error accessing the
    * database
    */
    public HashSet lookup (Integer key) throws DBException, CacheException
    {
        HashSet idSet =  (HashSet)super.lookupNullsOk(key);
	if (idSet ==  null) {
	    idSet = new HashSet();
	}
	return idSet;
    }

    /**
     * Get the query to partially initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @return The query to partially initialize the cache.
     */
    /*
    public String getPartialInitQuery ()
    {
        return null;
    }*/


    /**
     * Get the query to add an object to the database.
     * @assumes Nothing
     * @effects Nothing
     * @param addObject The object to add.
     * @return The query to add an object to the database.
     * @throws Nothing
     */
    public String getFullInitQuery ()
    {
        String stmt = "SELECT ra._Object_key,  aa.accID " +
                      "FROM MGI_Reference_Assoc ra, ACC_Accession aa " +
                      "WHERE ra._MGIType_key = " + MGITypeConstants.ALLELE +
		      " and ra._RefAssocType_key = " + 
			MGIRefAssocTypeConstants.ALLELE_SEQUENCE +
		      " and ra._Refs_key = aa._Object_key " +
		      " and aa._MGIType_key = " + MGITypeConstants.REF +
		      " and aa._LogicalDB_key = " + LogicalDBConstants.PUBMED;
	   
        return stmt;
    }


    /**
     * Get a RowDataInterpreter for creating a KeyValue object from a database
     * used for creating a new cache entry.
     * @assumes nothing
     * @effects nothing
     * @return The RowDataInterpreter object
     */
    public RowDataInterpreter getRowDataInterpreter() {
        class Interpreter implements MultiRowInterpreter
        {
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
		HashSet pubMedIDSet = new HashSet();
		for (Iterator it = v.iterator(); it.hasNext();) {
		    rd = (RowData)it.next();
		    pubMedIDSet.add(rd.pubMedID);
		}
		return new KeyValue(alleleKey, pubMedIDSet);
	    }
        }
        return new Interpreter();
    }
      /**
     * Simple data object representing a row of data from the query
     */
    class RowData {
	protected Integer alleleKey;
        protected String pubMedID;
        public RowData (RowReference row) throws DBException {
            alleleKey = row.getInt(1);
            pubMedID = row.getString(2);
        }
    }
}

