//  $Header

package org.jax.mgi.dbs.mgd.lookup;

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
 * @is An object that knows how to look up a MGI_User._User_key
 * given a login
 * @has Nothing
 * @does
 *   <UL>
 *   <LI> Provides a method to look up a MGI_User._User_key.
 *   </UL>
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */

public class MGIUserKeyLookup extends FullCachedLookup {
    /**
     * Constructs a MGIUserKeyLookup object.
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     * @throws DBException thrown if there is an error accessing the
     * database
     */
    public MGIUserKeyLookup ()
        throws CacheException, ConfigException, DBException
    {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    }

    /**
      * Looks up a User login Type to find its key.
      * @param userLogin The user login to lookup.
      * @return An Integer object containing the key for the MGI_User
      * @throws CacheException thrown if there is an error accessing the cache
      * @throws DBException thrown if there is an error accessing the
      * database
      * @throws KeyNotFoundException thrown if the key is not found
      */
     public Integer lookup (String userLogin)
         throws KeyNotFoundException, DBException, CacheException
     {
         return (Integer)super.lookup(userLogin);
     }


     /**
      * Get the query to fully initialize the cache.
      * @assumes Nothing
      * @effects Nothing
      * @return The query to fully initialize the cache.
      */
     public String getFullInitQuery ()
     {
         return new String("SELECT login, _User_key FROM MGI_User");
     }


     /**
      * Get a RowDataInterpreter for creating a KeyValue object from a database
      * used for creating a new cache entry.
      * @assumes nothing
      * @effects nothing
      * @return The RowDataInterpreter object.
      */
     public RowDataInterpreter getRowDataInterpreter()
     {
         class Interpreter implements RowDataInterpreter
         {
             public Object interpret (RowReference row)
                 throws DBException
             {
                 return new KeyValue(row.getString(1), row.getInt(2));
             }
         }
         return new Interpreter();
     }
 }


//  $Log
//
 /**************************************************************************
 *
 * Warranty Disclaimer and Copyright Notice
 *
 *  THE JACKSON LABORATORY MAKES NO REPRESENTATION ABOUT THE SUITABILITY OR
 *  ACCURACY OF THIS SOFTWARE OR DATA FOR ANY PURPOSE, AND MAKES NO WARRANTIES,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING MERCHANTABILITY AND FITNESS FOR A
 *  PARTICULAR PURPOSE OR THAT THE USE OF THIS SOFTWARE OR DATA WILL NOT
 *  INFRINGE ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS, OR OTHER RIGHTS.
 *  THE SOFTWARE AND DATA ARE PROVIDED "AS IS".
 *
 *  This software and data are provided to enhance knowledge and encourage
 *  progress in the scientific community and are to be used only for research
 *  and educational purposes.  Any reproduction or use for commercial purpose
 *  is prohibited without the prior express written permission of The Jackson
 *  Laboratory.
 *
 * Copyright \251 1996, 1999, 2002, 2003 by The Jackson Laboratory
 *
 * All Rights Reserved
 *
 **************************************************************************/
