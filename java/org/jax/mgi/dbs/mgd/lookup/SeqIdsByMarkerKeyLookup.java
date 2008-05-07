package org.jax.mgi.dbs.mgd.lookup;

import java.util.Vector;
import java.util.Iterator;
import java.util.HashSet;

import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.MultiRowInterpreter;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.dbs.SchemaConstants;

/**
 * @is An object that knows how to look up a MGI marker key to find its
 *     sequences from a given logicalDB
 * @has sequence logicalDBKey
 * @does
 *   <UL>
 *   <LI> Provides a method to look sequences, for the given logicalDB, by 
 *        marker key
 *   </UL>
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */

public class SeqIdsByMarkerKeyLookup extends FullCachedLookup {
    private String seqLdbKey;
    /**
     * Constructor
     * @throws DBException thrown if there is an error accessing the database
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown of there is an error accessing the
     * configuration
     */

    public SeqIdsByMarkerKeyLookup(int seqLdbKey)
        throws DBException,
        ConfigException,
        CacheException {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
	this.seqLdbKey = (new Integer(seqLdbKey)).toString();
    }

    /**
     * lookup associated sequences for a given marker key
     * @assumes nothing
     * @effects if the cache has not been initialized then the query will be
     * executed and the cache will be loaded. Queries a database.
     * @param MGI marker Key 
     * @return a set of seqIDs
     * @throws CacheException thrown if there is an error accessing the
     * caches
     * @throws DBException thrown if there is an error accessing the database
     */
    public HashSet lookup(Integer markerKey)
    throws CacheException, DBException {
        return (HashSet)super.lookupNullsOk(markerKey);
    }

    /**
     * Get the query to fully initialize the cache.
     * @return The query to fully initialize the cache.
     */
    public String getFullInitQuery() {

	// select marker keys and their seqId associations of type "seqLdbKey"

        String sql =
            "select c._Marker_key, sequenceID = c.accID " +
	    "from SEQ_Marker_Cache c " +
	    "where c._LogicalDB_key =  " + seqLdbKey + " " +
	    "order by c._Marker_key";

        return sql;
    }

    /**
     * return the RowDataInterpreter for creating  KeyValue objects from the query results
     * @return the RowDataInterpreter for this query
     */
    public RowDataInterpreter getRowDataInterpreter() {
           class Interpreter implements MultiRowInterpreter {
            
		public Object interpret(RowReference ref)
		throws DBException
		{
		    return new RowData(ref);
		}

		public Object interpretKey(RowReference ref) throws DBException
		{
		    return ref.getInt(1);
		}

		public Object interpretRows(Vector v)
		{
		    RowData rd = (RowData)v.get(0);
		    Integer markerKey = rd.markerKey;
		    HashSet seqIDSet = new HashSet();
		    for (Iterator it = v.iterator(); it.hasNext();)
		    { 
			rd = (RowData)it.next();
			seqIDSet.add(rd.seqID);
		    }
		    return new KeyValue(markerKey, seqIDSet);
		}
	    }
	    
        return new Interpreter();
    }
    /**
     * Simple data object representing a row of data from the query
     */
    class RowData  {
	protected Integer markerKey;
	protected String seqID;
	public RowData (RowReference row) throws DBException {
	    markerKey = row.getInt(1);
	    seqID = row.getString(2);
	}
    }
}
