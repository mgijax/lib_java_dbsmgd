
package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.dbs.mgd.LogicalDBConstants;
import org.jax.mgi.dbs.mgd.MGITypeConstants;
import org.jax.mgi.dbs.mgd.dao.MAP_Coord_FeatureState;
import org.jax.mgi.dbs.mgd.dao.MAP_Coord_FeatureDAO;
import org.jax.mgi.dbs.mgd.dao.MAP_Coord_FeatureKey;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

/**
 * @is An object that knows how to look up a J-Number to find its
 *     reference key.
 * @has Nothing
 * @does
 *   <UL>
 *   <LI> Provides a method to look up a J-Number.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class CoordMapFeatureLookup extends FullCachedLookup
{
    private String collectionName;
    private Integer mgiTypeKey;
    /**
     * Constructs a JNumberLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     * @throws DBException thrown if there is an error accessing the
     * database
     */
    public CoordMapFeatureLookup (String collectionName, Integer mgiTypeKey)
        throws  CacheException, ConfigException, DBException
    {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
	this.collectionName = collectionName;
	this.mgiTypeKey = mgiTypeKey;
    }

 /**
     * Get the query to fully initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @return The query to fully initialize the cache.
     */
    public String getFullInitQuery ()
    {

	return new String("SELECT f._Feature_key, f._Map_key, " +
		"f._MGIType_key, f._Object_key, f.startCoordinate, " +
		"f.endCoordinate, f.strand, f._CreatedBy_key, " +
		"f._ModifiedBy_key, f.creation_date, f.modification_date " +
		"FROM MAP_Coord_Feature f, " +
		"MAP_Coordinate c, MAP_Coord_Collection cc " +
		"WHERE cc.name = '" + collectionName  + "'" +
		" AND cc._Collection_key = c._Collection_key " +
		" AND c._Map_key = f._Map_key " +
		" AND f._MGIType_key = " + mgiTypeKey );
    }
    /**
    * Looks up a J-Number to find its reference key.
    * @assumes Nothing
    * @effects Nothing
    * @param jNumber The J-Number to look up.
    * @return An Integer object containing the reference key for the J-Number or
    *         a null if the J-Number was not found.
    * @throws CacheException thrown if there is an error accessing the cache
    * @throws DBException thrown if there is an error accessing the
    * database
    */
    public MAP_Coord_FeatureDAO lookup (Integer key) throws DBException, CacheException
    {
        return (MAP_Coord_FeatureDAO)super.lookupNullsOk(key);
    }


    /**
     * Get the query to partially initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @return The query to partially initialize the cache.
     */
    public String getPartialInitQuery ()
    {
        return null;
    }


    /**`
     * Get a RowDataInterpreter for creating a KeyValue object from a database
     * used for creating a new cache entry.
     * @assumes nothing
     * @effects nothing
     * @return The RowDataInterpreter object
     */
    public RowDataInterpreter getRowDataInterpreter()
    {
        class Interpreter implements RowDataInterpreter
        {
            public Object interpret (RowReference row)
                throws DBException
            {
		Integer key = row.getInt(1);
		MAP_Coord_FeatureState state = new MAP_Coord_FeatureState();
		state.setMapKey(row.getInt(2));
		state.setMGITypeKey(row.getInt(3));
		state.setObjectKey(row.getInt(4));
		state.setStartCoordinate(row.getDouble(5));
		state.setEndCoordinate(row.getDouble(6));
		state.setStrand(row.getString(7));
		state.setCreatedByKey(row.getInt(8));
		state.setModifiedByKey(row.getInt(9));
		state.setCreationDate(row.getTimestamp(10));
		state.setModificationDate(row.getTimestamp(11));
		
		MAP_Coord_FeatureDAO dao = new MAP_Coord_FeatureDAO(new MAP_Coord_FeatureKey(key), state);
                return new KeyValue(key, dao);
            }
        }
        return new Interpreter();
    }
}
