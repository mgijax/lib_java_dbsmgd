
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
 * Map Feature key for a given collection
 * @has a database query with which to load all Features for a given collection
 * @does provides a lookup method to lookup the feature key for a Map Feature
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */

public class CoordMapFeatureKeyLookup extends FullCachedLookup{

    public static final String DELIMITER = ":::";
    private String collectionName;
    private Integer mgiTypeKey;

    /**
     * Constructs a CoordMapFeatureKeyLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     * @throws DBException thrown if there is an error accessing the
     * database
     */
    public CoordMapFeatureKeyLookup (String collectionName, Integer mgiTypeKey) 
        throws CacheException, ConfigException, DBException {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
	this.collectionName = collectionName;
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
		"FROM ACC_Accession a, MAP_Coord_Feature f, " +
		"MAP_Coordinate c, MAP_Coord_Collection cc " + 
		"WHERE cc.name = '" + collectionName  + "'" +
		" AND cc._Collection_key = c._Collection_key " +
		" AND c._Map_key = f._Map_key " +
		" AND f._MGIType_key = " + mgiTypeKey +
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


