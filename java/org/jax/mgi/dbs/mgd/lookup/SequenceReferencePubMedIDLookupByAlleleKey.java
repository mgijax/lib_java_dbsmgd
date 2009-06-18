package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.jax.mgi.dbs.mgd.LogicalDBConstants;
import org.jax.mgi.dbs.mgd.MGIRefAssocTypeConstants;
import org.jax.mgi.dbs.mgd.MGITypeConstants;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.InterpretException;
import org.jax.mgi.shr.dbutils.MultiRowInterpreter;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

/**
 * @is a FullCachedLookup for caching pubmed reference ids 
 *  associated with an allele with type 'sequence'
 * @has RowDataCacheStrategy of type FULL_CACHE used for creating the
 *      cache and performing the cache lookup
 * @does provides a lookup method for getting a set of reference ids by their 
 * associated allele key
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */

public class SequenceReferencePubMedIDLookupByAlleleKey extends FullCachedLookup
{
    
    // provide a static cache so that all instances share one cache
    private static HashMap cache = new HashMap();

    // indicator of whether or not the cache has been initialized
    private static boolean hasBeenInitialized = false;
  
    /**
     * Constructs a SequenceReferencePubMedIDLookupByAlleleKey object.
     * @assumes Nothing
     * @effects Nothing
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     * @throws DBException thrown if there is an error accessing the
     * database
     */
    public SequenceReferencePubMedIDLookupByAlleleKey ()
        throws CacheException, ConfigException, DBException {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
	// since cache is static make sure you do not reinit
	if (!hasBeenInitialized) {
	  initCache(cache);
	}
	hasBeenInitialized = true;
    }


    /**
    * Looks up a allele key to find a set of reference ids
    * @assumes Nothing
    * @effects Nothing
    * @param key The allele key to look up.
    * @return A set of reference ids (J-Number and PubMed) associated with an
    * allele (allele key) or null if no reference found
    * @throws CacheException thrown if there is an error accessing the cache
    * @throws DBException thrown if there is an error accessing the database
    */
    
    public HashSet lookup (Integer key) throws DBException, CacheException
    {
        return (HashSet)super.lookupNullsOk(key);
    }

    /**
     * Get the query to fully initialize the cache.
     * @return The query to fully initialize the cache.
     */
    public String getFullInitQuery ()
    {
        String stmt = "SELECT ra._Object_key, a.accid " +
	    "FROM MGI_Reference_assoc ra, ACC_Accession a " +
	    "WHERE ra._MGIType_key = " + MGITypeConstants.ALLELE + " " +
	    "and ra._RefAssocType_key =  " + 
	    MGIRefAssocTypeConstants.ALLELE_SEQUENCE + " " +
	    "and ra._Refs_key = a._Object_key " +
	    "and a._MGIType_key = " + MGITypeConstants.REF + " " +
	    "and _LogicalDB_key = " + LogicalDBConstants.PUBMED + " " +
	    "order by ra._Object_key";

        return stmt;
    }


    /**
     * Get a RowDataInterpreter for creating a KeyValue object from a database
     * used for creating a new cache entry.
     * @assumes nothing
     * @effects nothing
     * @return The RowDataInterpreter object
     */
    public RowDataInterpreter getRowDataInterpreter()
    {
	
        class Interpreter implements MultiRowInterpreter
        {
	    public Object interpret(RowReference row)
                throws DBException {
                    return new RowData(row);
                }

                public Object interpretKey(RowReference row) 
			throws DBException {
                    return row.getInt(1);
                }

                public Object interpretRows(Vector v) throws InterpretException {
		    HashSet refIDSet = new HashSet();
		    Integer alleleKey = ((RowData)v.get(0)).alleleKey;
		    for (Iterator i = v.iterator();i.hasNext(); ) {			
			RowData row = (RowData)i.next();
			String currentRefID = row.refID;
			refIDSet.add(currentRefID);
		    }
		   return new KeyValue(alleleKey, refIDSet);
		}
        }
        return new Interpreter();
    }
    				
		/**
		 * an object that represents a row of data from the query we are
		 * interpreting
		 * @has attributes representing each column selected in the 
		 *      query
		 * @does assigns its attributes from a RowReference object
		 * @company The Jackson Laboratory
		 * @author sc
		 * @version 1.0
		 */
		 class RowData {
		    protected Integer alleleKey;
		    protected String refID;
		    		    
		    /**
		     * Constructs a RowData object from a RowReference
		     * @param row a RowReference
		     * @throws DBException if error accessing RowReference 
		     *         methods
		     */

		    public RowData(RowReference row) throws DBException {
			alleleKey = row.getInt(1);
			refID = row.getString(2);
		    }
		}
}
