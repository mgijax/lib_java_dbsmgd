//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

import org.jax.mgi.dbs.mgd.trans.Translator;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.LazyCachedLookup;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.cache.LookupException;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.KeyedDataAttribute;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;

/**
 * @is An object that knows how to look up a PRB_Source record.
 * @has
 *   <UL>
 *   <LI> Translator object
 *   </UL>
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
    /////////////////
    //  Variables  //
    /////////////////

    // A translator for resolving a source name.
    //
    private Translator trans = null;

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
     * Constructs a ProbeSourceLookup object that will use a Translator to
     * resolve a source name.
     * @assumes Nothing
     * @effects Nothing
     * @param pTrans The Translator to use for resolving a source name.
     * @throws Nothing
     */
    public ProbeSourceLookup(Translator pTrans)
    {
        trans = pTrans;
    }

    /**
     * Finds a PRB_Source record for a given source name. If a Translator object
     * has been set up, it will be used to attempt to resolve the source name
     * before doing a lookup.
     * @assumes Nothing
     * @effects Nothing
     * @param name The source name to look up.
     * @return A ProbeSourceDAO object that represents the PRB_Source record.
     * @throws Nothing
     */
    public ProbeSourceDAO findByName (String name)
        throws CacheException, ConfigException, DBException, LookupException
    {
        if (trans != null)
        {
            KeyedDataAttribute kda = trans.translate(name);
            if (kda != null)
            {
                KeyedSourceLookup keyedSourceLookup = new KeyedSourceLookup();
                return keyedSourceLookup.lookup(kda.getKey());
            }
        }
        NamedSourceLookup namedSourceLookup = new NamedSourceLookup();
        return namedSourceLookup.lookup(name);
    }

    /**
     * Finds a PRB_Source record for a given source key.
     * @assumes Nothing
     * @effects Nothing
     * @param name The source key to look up.
     * @return A ProbeSourceDAO object that represents the PRB_Source record.
     * @throws Nothing
     */
    public ProbeSourceDAO findByKey (Integer key)
        throws CacheException, ConfigException, DBException, LookupException
    {
        KeyedSourceLookup keyedSourceLookup = new KeyedSourceLookup();
        return keyedSourceLookup.lookup(key);
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

    private class NamedSourceLookup extends FullCachedLookup
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
            super(SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
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
            throws LookupException
        {
            return (ProbeSourceDAO)super.lookup(name, true);
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
                private RowDataInterpreter probeSrcInterpreter =
                    new ProbeSourceInterpreter();

                public Object interpret (RowReference row)
                    throws DBException
                {
                    ProbeSourceDAO probeSrcDAO =
                        (ProbeSourceDAO) probeSrcInterpreter.interpret(row);
                    return new KeyValue(probeSrcDAO.getProbeSrcState().getName(),
                                        probeSrcDAO);
                }
            }
            return new Interpreter();
        }
    }

    /**
     * @is An object that knows how to look up a PRB_Source record for a given
     *     source key.
     * @has Nothing
     * @does
     *   <UL>
     *   <LI> Provides a method to look up a PRB_Source record.
     *   </UL>
     * @company The Jackson Laboratory
     * @author dbm
     * @version 1.0
     */

    private class KeyedSourceLookup extends LazyCachedLookup
    {
        /**
         * Constructs a KeyedSourceLookup object.
         * @assumes Nothing
         * @effects Nothing
         * @param None
         * @throws Nothing
         */
        public KeyedSourceLookup()
           throws ConfigException, DBException, CacheException
        {
            super(SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
        }

        /**
         * Looks up a PRB_Source record for a given source key.
         * @assumes Nothing
         * @effects Nothing
         * @param key The source key to look up.
         * @return A ProbeSourceDAO object that represents the PRB_Source record.
         * @throws Nothing
         */
        public ProbeSourceDAO lookup(Integer key)
            throws LookupException
        {
            return (ProbeSourceDAO)super.lookup(key, true);
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
            String stmt = "SELECT * FROM PRB_Source WHERE name is not null";

            return stmt;
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
            String stmt = "SELECT * FROM PRB_Source " +
                          "WHERE _Source_key = "+((Integer)addObject).intValue();

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
                private RowDataInterpreter probeSrcInterpreter =
                    new ProbeSourceInterpreter();

                public Object interpret (RowReference row)
                    throws DBException
                {
                    ProbeSourceDAO probeSrcDAO =
                        (ProbeSourceDAO) probeSrcInterpreter.interpret(row);
                    return new KeyValue(probeSrcDAO.getProbeSrcKey().getPrimaryKey(),
                                        probeSrcDAO);
                }
            }
            return new Interpreter();
        }
    }
}


//  $Log$
//  Revision 1.6  2003/10/03 16:35:25  mbw
//  modified to suit changes to the CachedLookup base class
//
//  Revision 1.5  2003/10/02 18:48:54  dbm
//  Changed to extend subclass of CachedLookup
//
//  Revision 1.4  2003/09/30 16:58:10  dbm
//  Use Integer instead of int for attributes
//
//  Revision 1.3  2003/09/25 20:24:21  mbw
//  fixed import for KeyValue
//
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
