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
 * @is An object that knows how to look up an MGI ID to find its
 *     sequences from a given logicalDB
 * @has sequence logicalDBKey
 * @does
 *   <UL>
 *   <LI> Provides a method to look sequences, for the given logicalDB, by 
 *        MGI ID
 *   </UL>
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */

public class SeqIdsByMarkerIdLookup extends FullCachedLookup {
    private int seqLdbKey;
    /**
     * Constructor
     * @throws DBException thrown if there is an error accessing the database
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown of there is an error accessing the
     * configuration
     */

    public SeqIdsByMarkerIdLookup(int seqLdbKey)
        throws DBException,
        ConfigException,
        CacheException {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
	this.seqLdbKey = seqLdbKey;
    }

    /**
     * lookup associated sequences for a given marker MGI ID
     * @assumes nothing
     * @effects if the cache has not been initialized then the query will be
     * executed and the cache will be loaded. Queries a database.
     * @param mgiID the mgiID of a marker
     * @return a set of seqIDs
     * @throws CacheException thrown if there is an error accessing the
     * caches
     * @throws DBException thrown if there is an error accessing the database
     */
    public HashSet lookup(String mgiID)
    throws CacheException, DBException {
        return (HashSet)super.lookupNullsOk(mgiID);
    }

    /**
     * Get the query to fully initialize the cache.
     * @return The query to fully initialize the cache.
     */
    public String getFullInitQuery() {

	// select mgiIds of markers and their seqId associations of type "seqLdbKey"

        String sql =
            "select markerID = ma.accID, sequenceID = c.accID " +
	    "from SEQ_Marker_Cache c, ACC_Accession ma " +
	    "where c._Marker_key = ma._Object_key " +
	    "and ma._MGIType_key = 2 " +
	    "and ma._LogicalDB_key = 1 " +
	    "and ma.prefixPart = 'MGI:' " +
	    "and ma.preferred = 1 " +
	    "and c._LogicalDB_key =  " + seqLdbKey + " " +
	    "order by ma.accID";

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
		    return ref.getString(1);
		}

		public Object interpretRows(Vector v)
		{
		    RowData rd = (RowData)v.get(0);
		    String mgiID = rd.mgiID;
		    HashSet seqIDSet = new HashSet();
		    for (Iterator it = v.iterator(); it.hasNext();)
		    { 
			rd = (RowData)it.next();
			seqIDSet.add(rd.seqID);
		    }
		    return new KeyValue(mgiID, seqIDSet);
		}
	    }
	    
        return new Interpreter();
    }
    /**
     * Simple data object representing a row of data from the query
     */
    class RowData  {
	protected String mgiID;
	protected String seqID;
	public RowData (RowReference row) throws DBException {
	    mgiID = row.getString(1);
	    seqID = row.getString(2);
	}
    }
}
