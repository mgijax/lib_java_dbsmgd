//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

import org.jax.mgi.shr.cache.CacheException;import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.LazyCachedLookup;
import org.jax.mgi.shr.cache.LookupException;
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
        throws CacheException, ConfigException, DBException, LookupException
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

    private class ProbeRefLookup extends LazyCachedLookup
    {
        /**
         * Constructs a ProbeRefLookup object.
         * @assumes Nothing
         * @effects Nothing
         * @param None
         * @throws Nothing
         */
        public ProbeRefLookup()
           throws CacheException, ConfigException, DBException
        {
            super(SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
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
            throws LookupException
        {
            String key = probeKey + "," + refsKey;

            return (ProbeReferenceDAO)super.lookup(key, true);
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
            return null;
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
            class Interpreter implements RowDataInterpreter
            {
                private RowDataInterpreter probeRefInterpreter =
                    new ProbeReferenceInterpreter();

                public Object interpret (RowReference row)
                    throws DBException
                {
                    ProbeReferenceDAO probeRefDAO =
                        (ProbeReferenceDAO)probeRefInterpreter.interpret(row);
                    String key = probeRefDAO.getProbeReferenceState().
                        getProbeKey() + "," +
                        probeRefDAO.getProbeReferenceState().getRefsKey();
                    return new KeyValue(key, probeRefDAO);
                }
            }
            return new Interpreter();
        }
    }
}


//  $Log$
//  Revision 1.5  2003/10/02 18:48:54  dbm
//  Changed to extend subclass of CachedLookup
//
//  Revision 1.4  2003/10/01 14:52:15  dbm
//  Use Strings to represent bit columns in DAO classes
//
//  Revision 1.3  2003/09/30 16:58:09  dbm
//  Use Integer instead of int for attributes
//
//  Revision 1.2  2003/09/25 20:24:21  mbw
//  fixed import for KeyValue
//
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
