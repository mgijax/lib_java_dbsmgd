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
 * @is An object that knows how to look up a PRB_Reference record.
 * @has Nothing
 * @does
 *   <UL>
 *   <LI> Provides a method to look up a PRB_Reference record for a given
 *        probe key and refs key.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class ProbeReferenceLookup
{
    /**
     * Constructs a ProbeReferenceLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws Nothing
     */
    public ProbeReferenceLookup()
    {
    }

    /**
     * Finds a PRB_Reference record for a given probe key and refs key.
     * @assumes Nothing
     * @effects Nothing
     * @param probeKey The probe key to look up.
     * @param refsKey The refs key to look up.
     * @return A ProbeReferenceDAO object that represents the PRB_Reference
     *         record.
     * @throws Nothing
     */
    public ProbeReferenceDAO findByPrbRef (Integer probeKey, Integer refsKey)
        throws ConfigException, DBException, CacheException
    {
        ProbeRefLookup prbRefLookup = new ProbeRefLookup();
        return prbRefLookup.lookup(probeKey, refsKey);
    }

    /**
     * @is An object that knows how to look up a PRB_Reference record for a
     *     given probe key and refs key.
     * @has Nothing
     * @does
     *   <UL>
     *   <LI> Provides a method to look up a PRB_Reference record.
     *   </UL>
     * @company The Jackson Laboratory
     * @author dbm
     * @version 1.0
     */

    private class ProbeRefLookup extends RowDataCacheHandler
    {
        /**
         * Constructs a ProbeRefLookup object.
         * @assumes Nothing
         * @effects Nothing
         * @param None
         * @throws Nothing
         */
        public ProbeRefLookup()
           throws ConfigException, DBException, CacheException
        {
            super(LAZY_CACHE,
                SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
        }

        /**
         * Looks up a PRB_Reference record for a given probe key and refs key.
         * @assumes Nothing
         * @effects Nothing
         * @param probeKey The probe key to look up.
         * @param refsKey The refs key to look up.
         * @return A ProbeReferenceDAO object that represents the PRB_Reference
         *         record.
         * @throws Nothing
         */
        public ProbeReferenceDAO lookup(Integer probeKey, Integer refsKey)
            throws DBException, CacheException
        {
            String key = probeKey + "," + refsKey;

            return (ProbeReferenceDAO) cacheStrategy.lookup(key, cache);
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
            String message = "Class " + this.getClass().getName() +
                " does not support the method getFullInitQuery";
            throw new java.lang.UnsupportedOperationException(message);
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
            String[] keyParts = addObject.toString().split(",");

            String stmt = "SELECT * FROM PRB_Reference " +
                          "WHERE _Probe_key = " + keyParts[0] + " and " +
                                "_Refs_key = " + keyParts[1];

            return stmt;
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
            private RowDataInterpreter probeRefInterpreter =
                new ProbeReferenceInterpreter();

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
                ProbeReferenceDAO probeRefDAO =
                    (ProbeReferenceDAO) probeRefInterpreter.interpret(row);
                String key = probeRefDAO.getProbeReferenceState().getProbeKey() + "," +
                             probeRefDAO.getProbeReferenceState().getRefsKey();
                KeyValue keyValue = new KeyValue(key, probeRefDAO);

                return keyValue;
            }
        }
    }
}


//  $Log$
//  Revision 1.1  2003/09/25 17:51:54  dbm
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
