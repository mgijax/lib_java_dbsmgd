//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.types.KeyValue;
import org.jax.mgi.shr.cache.RowDataCacheHandler;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

/**
 * @is An object that knows how to look up a PRB_Source record.
 * @has Nothing
 * @does
 *   <UL>
 *   <LI> Provides a method to look up a PRB_Source record for a given source
 *        name.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class ProbeSourceLookup
{
    /**
     * Constructs a ProbeSourceLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws Nothing
     */
    public ProbeSourceLookup()
    {
    }

    /**
     * Finds a PRB_Source record for a given source name.
     * @assumes Nothing
     * @effects Nothing
     * @param name The source name to look up.
     * @return A ProbeSourceDAO object that represents the PRB_Source record.
     * @throws Nothing
     */
    public ProbeSourceDAO findByName (String name)
        throws ConfigException, DBException, CacheException
    {
        NamedSourceLookup namedSourceLookup = new NamedSourceLookup();
        return namedSourceLookup.lookup(name);
    }

    /**
     * @is An object that knows how to look up a PRB_Source record for a given
     *     source name.
     * @has Nothing
     * @does
     *   <UL>
     *   <LI> Provides a method to look up a PRB_Source record.
     *   </UL>
     * @company The Jackson Laboratory
     * @author dbm
     * @version 1.0
     */

    private class NamedSourceLookup extends RowDataCacheHandler
    {
        /**
         * Constructs a NamedSourceLookup object.
         * @assumes Nothing
         * @effects Nothing
         * @param None
         * @throws Nothing
         */
        public NamedSourceLookup()
           throws ConfigException, DBException, CacheException
        {
            super(FULL_CACHE,
                SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
        }

        /**
         * Looks up a PRB_Source record for a given source name.
         * @assumes Nothing
         * @effects Nothing
         * @param name The source name to look up.
         * @return A ProbeSourceDAO object that represents the PRB_Source record.
         * @throws Nothing
         */
        public ProbeSourceDAO lookup(String name)
            throws DBException, CacheException
        {
            return (ProbeSourceDAO) cacheStrategy.lookup(name, cache);
        }

        /**
         * Get the query to fully initialize the cache.
         * @assumes Nothing
         * @effects Nothing
         * @param None
         * @return The query to fully initialize the cache.
         * @throws Nothing
         */
        public String getFullInitQuery()
        {
            String stmt = "SELECT * FROM PRB_Source WHERE name is not null";

            return stmt;
        }

        /**
         * Get the query to partially initialize the cache.
         * @assumes Nothing
         * @effects Nothing
         * @param None
         * @return The query to partially initialize the cache.
         * @throws Nothing
         */
        public String getPartialInitQuery()
        {
            String message = "Class " + this.getClass().getName() +
                " does not support the method getPartialInitQuery";
            throw new java.lang.UnsupportedOperationException(message);
        }

        /**
         * Get the query to add an object to the database.
         * @assumes Nothing
         * @effects Nothing
         * @param addObject The object to add.
         * @return The query to add an object to the database.
         * @throws Nothing
         */
        public String getAddQuery(Object addObject)
        {
            String message = "Class " + this.getClass().getName() +
                " does not support the method getAddQuery";
            throw new java.lang.UnsupportedOperationException(message);
        }

        /**
         * Get a RowDataInterpreter for creating a KeyValue object from a
         * database used for creating a new cache entry.
         * @assumes nothing
         * @effects nothing
         * @param None
         * @return The RowDataInterpreter object
         * @throws Nothing
         */
        public RowDataInterpreter getRowDataInterpreter()
        {
            return new InnerInterpreter();
        }

        /**
         * @is An object that knows how to create a KeyValue object for a row of
         *     data retrieved by this lookup.
         * @has Nothing
         * @does
         *   <UL>
         *   <LI> Provides a RowDataInterpreter implementation.
         *   </UL>
         * @company The Jackson Laboratory
         * @author dbm
         * @version 1.0
         */

        private class InnerInterpreter implements RowDataInterpreter
        {
            private RowDataInterpreter probeSrcInterpreter =
                new ProbeSourceInterpreter();

            /**
             * Create a KeyValue object from a row of data.
             * @assumes nothing
             * @effects nothing
             * @param row The row reference.
             * @return The KeyValue object.
             * @throws Nothing
             */
            public Object interpret(RowReference row)
                throws DBException
            {
                ProbeSourceDAO probeSrcDAO =
                    (ProbeSourceDAO) probeSrcInterpreter.interpret(row);
                KeyValue keyValue =
                    new KeyValue(probeSrcDAO.getProbeSrcState().getName(), probeSrcDAO);

                return keyValue;
            }
        }
    }
}


//  $Log$
//  Revision 1.2  2003/09/25 17:54:11  dbm
//  Continued development
//
//  Revision 1.1  2003/09/23 13:16:22  dbm
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
