//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.LazyCachedLookup;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;


/**
 * @is An object that knows how to look up a PRB_Reference record.
 * @has
 *   <UL>
 *   <LI> Lookup object for each inner lookup class
 *   </UL>
 * @does
 *   <UL>
 *   <LI> Provides a method to look up a PRB_Reference record for a given
 *        probe key and refs key.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class PRB_ReferenceLookup
{
    /////////////////
    //  Variables  //
    /////////////////

    // An object used to look up a probe reference.
    //
    private ProbeRefLookup prbRefLookup = null;

    /**
     * Constructs a PRB_ReferenceLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws CacheException
     * @throws ConfigException
     * @throws DBException
     */
    public PRB_ReferenceLookup()
        throws CacheException, ConfigException, DBException
    {
        prbRefLookup = new ProbeRefLookup();
    }

    /**
     * Finds a PRB_Reference record for a given probe key and refs key.
     * @assumes Nothing
     * @effects Nothing
     * @param probeKey The probe key to look up.
     * @param refsKey The refs key to look up.
     * @return A PRB_ReferenceDAO object that represents the PRB_Reference
     *         record.
     * @throws LookupException
     */
    public PRB_ReferenceDAO findByPrbRef (Integer probeKey, Integer refsKey)
    throws DBException, CacheException
    {
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
         * @throws CacheException
         * @throws ConfigException
         * @throws DBException
         */
        public ProbeRefLookup()
           throws CacheException, ConfigException, DBException
        {
            super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
        }

        /**
         * Looks up a PRB_Reference record for a given probe key and refs key.
         * @assumes Nothing
         * @effects Nothing
         * @param probeKey The probe key to look up.
         * @param refsKey The refs key to look up.
         * @return A PRB_ReferenceDAO object that represents the PRB_Reference
         *         record.
         * @throws LookupException
         */
        public PRB_ReferenceDAO lookup(Integer probeKey, Integer refsKey)
        throws CacheException, DBException
        {
            String key = probeKey + "," + refsKey;

            return (PRB_ReferenceDAO)super.lookupNullsOk(key);
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
                    new PRB_ReferenceInterpreter();

                public Object interpret (RowReference row)
                    throws DBException
                {
                    PRB_ReferenceDAO probeRefDAO =
                        (PRB_ReferenceDAO)probeRefInterpreter.interpret(row);
                    String key = probeRefDAO.getState().getProbeKey() + "," +
                        probeRefDAO.getState().getRefsKey();
                    return new KeyValue(key, probeRefDAO);
                }
            }
            return new Interpreter();
        }
    }
}


//  $Log$
//  Revision 1.2  2003/11/05 15:44:20  mbw
//  modified to suit the now defunct LookupException
//
//  Revision 1.1  2003/10/23 11:44:44  dbm
//  Converted to work with code generated DAO classes
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
