//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.KeyNotFoundException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.LookupException;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

/**
 * @is An object that knows how to look up a logical DB.
 * @has Nothing
 * @does
 *   <UL>
 *   <LI> Provides a method to look up a logical DB.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class LogicalDBLookup extends FullCachedLookup
{
    /**
     * Constructs a LogicalDBLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws CacheException
     * @throws ConfigException
     * @throws DBException
     */
    public LogicalDBLookup ()
        throws CacheException, ConfigException, DBException
    {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    }


    /**
     * Looks up a logical DB to find its key.
     * @assumes Nothing
     * @effects Nothing
     * @param logicalDB The logicalDB to look up.
     * @return An Integer object containing the key for the logical DB or a null
     *         if the logical DB was not found.
     * @throws KeyNotFoundException
     * @throws LookupException
     */
    public Integer lookup (String logicalDB)
        throws KeyNotFoundException, LookupException
    {
        return (Integer)super.lookup(logicalDB);
    }


    /**
     * Get the query to fully initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The query to fully initialize the cache.
     * @throws Nothing
     */
    public String getFullInitQuery ()
    {
        return new String("SELECT name, _LogicalDB_key FROM ACC_LogicalDB");
    }


    /**
     * Get a RowDataInterpreter for creating a KeyValue object from a database
     * used for creating a new cache entry.
     * @assumes nothing
     * @effects nothing
     * @param None
     * @return The RowDataInterpreter object.
     * @throws Nothing
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


//  $Log$
//  Revision 1.8  2003/10/10 15:30:15  dbm
//  Update javadocs
//
//  Revision 1.7  2003/10/06 19:43:07  dbm
//  Support changes to frameworks classes
//
//  Revision 1.6  2003/10/03 16:38:37  mbw
//  changed to suit the new CachedLookup base class
//
//  Revision 1.5  2003/10/02 18:47:20  dbm
//  Changed to extends subclass of CachedLookup
//
//  Revision 1.4  2003/09/30 17:02:43  dbm
//  Use Integer instead of int for values
//
//  Revision 1.3  2003/09/25 20:23:43  mbw
//  fixed imports for KeyValue
//
//  Revision 1.2  2003/09/23 13:13:01  dbm
//  Continued development
//
//  Revision 1.1  2003/09/18 14:26:12  dbm
//  Initial version
//
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
