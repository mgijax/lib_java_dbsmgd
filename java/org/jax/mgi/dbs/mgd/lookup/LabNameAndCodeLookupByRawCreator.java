package org.jax.mgi.dbs.mgd.lookup;

import java.util.HashMap;

import org.jax.mgi.dbs.mgd.TranslationTypeConstants;
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
 * @is An object that knows how to look up a lab code term and abbreviation given
 *  a raw creator name
 * @has Nothing
 * @does
 * <UL>
 * <LI> Provides a method to lookup a raw creator term to find its lab code and
 *      abbreviaton
 * <LI> Uses the lab code translation to map raw creator (badName) to term
 *      and abbreviation
 * </UL>
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */

public class LabNameAndCodeLookupByRawCreator extends FullCachedLookup {
    // provide a static cache so that all instances share one cache
    private static HashMap cache = new HashMap();

    // indicator of whether or not the cache has been initialized
    private static boolean hasBeenInitialized = false;
    
    /**
     * Constructs a LabNameAndCodeLookupByCreator object.
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     * @throws DBException thrown if there is an error accessing the
     * database
     */
    public LabNameAndCodeLookupByRawCreator ()
        throws CacheException, ConfigException, DBException {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
		// since cache is static make sure you do not reinit
		if (!hasBeenInitialized) {
			initCache(cache);
		}
		hasBeenInitialized = true;
    }

    /**
    * Looks up raw
    * @assumes Nothing
    * @effects Nothing
    * @param key The reference key to look up.
    * @return A KeyValue object where key is labName, value is labCode
    * @throws CacheException thrown if there is an error accessing the cache
    * @throws DBException thrown if there is an error accessing the
    * database
    */
    public KeyValue lookup (String rawCreator) throws DBException, 
			CacheException {
        return (KeyValue)super.lookupNullsOk(rawCreator);
    }

    /**
     * Get the query to add an object to the database.
     * @assumes Nothing
     * @effects Nothing
     * @param addObject The object to add.
     * @return The query to add an object to the database.
     * @throws Nothing
     */
    public String getFullInitQuery () {
        String stmt = "SELECT mt.badName, labName=vt.term, " +
	    "labCode=vt.abbreviation " +
	    "FROM MGI_Translation mt, VOC_Term vt " +
	    "WHERE mt._TranslationType_Key = " + 
	    TranslationTypeConstants.CELL_LINE_LABCODE + " " +
	    "AND mt._Object_key = vt._Term_key";    

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
        class Interpreter implements RowDataInterpreter {
            public Object interpret (RowReference row)
                throws DBException {
                String rawCreator = row.getString(1);
				String labName = row.getString(2);
				String labCode = row.getString(3);
				//System.out.println("creator: " + rawCreator + " labName: " + labName + " labCode: " + labCode);
				KeyValue labNameCode = new KeyValue(labName, labCode);
				return new KeyValue(rawCreator, labNameCode);
            }
        }
        return new Interpreter();
    }
}
