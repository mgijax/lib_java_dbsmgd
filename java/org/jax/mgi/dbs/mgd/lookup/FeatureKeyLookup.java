
package org.jax.mgi.dbs.mgd.lookup;

import java.util.Vector;

import org.jax.mgi.shr.dbutils.MultiRowInterpreter;
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
 * @is an object that knows how to look up a Map Feature objectId to get a
 * Map Feature key for a given MGI Type
 * @has a database query with which to load all Features for a given MGI Type
 * @does provides a lookup method to lookup the feature key for a Map Feature
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */

public class FeatureKeyLookup extends FullCachedLookup{

    public static final String DELIMITER = ":::";
    private Integer mgiTypeKey;

    /**
     * Constructs a FeatureKeyLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     * @throws DBException thrown if there is an error accessing the
     * database
     */
    public FeatureKeyLookup (Integer mgiTypeKey) 
        throws CacheException, ConfigException, DBException {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
	this.mgiTypeKey = mgiTypeKey;
    }

    /**
     * Looks up a Map Feature to find its key.
     * @assumes Nothing
     * @effects Nothing
     * @param featureObjectId  The feature Object Id to look up.
     * @return An Integer object containing the feature key 
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws DBException thrown if there is an error accessing the
     * database
     */

    public Integer[] lookup (String featureObjectId)
        throws DBException, CacheException {
	String list = (String)super.lookupNullsOk(featureObjectId);
	if (list ==  null) {
	    return null;
	}
	String[] featureKeys = list.split(DELIMITER);
	Integer[] featureKeysInt = new Integer[featureKeys.length];
	for (int i = 0; i < featureKeys.length; i++) {
	    featureKeysInt[i] = new Integer(featureKeys[i]);
	}
        return featureKeysInt;
    }

    /**
     * Get the query to fully initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @return The query to fully initialize the cache.
     */
    public String getFullInitQuery ()
    {
        return new String("SELECT a.accid, f._Feature_key " +
		"FROM ACC_Accession a, MAP_Coord_Feature f" +
		" WHERE f._MGIType_key = " + mgiTypeKey +
		" AND f._MGIType_key = a._MGIType_key " +
		" AND f._Object_key = a._Object_key");
    }

    /**
     * Get a RowDataInterpreter for creating a KeyValue object from a database
     * used for creating a new cache entry.
     * @assumes nothing
     * @effects nothing
     * @return The RowDataInterpreter object.
     */
    public RowDataInterpreter getRowDataInterpreter() {
        class Interpreter implements MultiRowInterpreter
        {
            public Object interpret (RowReference row)
                throws DBException
            {
                return new KeyValue(row.getString(1), row.getInt(2));
            }

	    public Object interpretKey(RowReference row) throws DBException {
               return row.getString(1);
            }
	    public Object interpretRows(Vector v) {
		KeyValue keyValue = (KeyValue)v.get(0);
		String featureObjectId = (String)keyValue.getKey();
		String featureKeys = ((Integer)keyValue.getValue()).toString();
		for (int i = 1; i < v.size(); i++) {
		    String nextFeatureKey = ((Integer)((KeyValue)v.get(i)).getValue()).toString();
		    featureKeys = featureKeys.concat(DELIMITER + nextFeatureKey);
		}
		return new KeyValue(featureObjectId, featureKeys);
        }

        }
        return new Interpreter();
    }
}


