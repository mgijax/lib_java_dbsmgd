//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.dbs.mgd.dao.PRB_SourceDAO;
import org.jax.mgi.dbs.mgd.dao.PRB_SourceKey;
import org.jax.mgi.dbs.mgd.dao.PRB_SourceState;
import org.jax.mgi.dbs.mgd.trans.Translator;
import org.jax.mgi.dbs.mgd.trans.TranslationException;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.LazyCachedLookup;
import org.jax.mgi.shr.cache.KeyValue;
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
 *   <LI> Lookup object for each inner lookup class
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

    // An object used to look up a source by name.
    //
    private NamedSourceLookup namedSourceLookup = null;

    // An object used to look up a source by key.
    //
    private KeyedSourceLookup keyedSourceLookup = null;

    /**
     * Constructs a ProbeSourceLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws CacheException
     * @throws ConfigException
     * @throws DBException
     */
    public ProbeSourceLookup()
        throws CacheException, ConfigException, DBException
    {
        namedSourceLookup = new NamedSourceLookup();
        keyedSourceLookup = new KeyedSourceLookup();
    }

    /**
     * Constructs a ProbeSourceLookup object that will use a Translator to
     * resolve a source name.
     * @assumes Nothing
     * @effects Nothing
     * @param pTrans The Translator to use for resolving a source name.
     * @throws CacheException
     * @throws ConfigException
     * @throws DBException
     */
    public ProbeSourceLookup(Translator pTrans)
        throws CacheException, ConfigException, DBException
    {
        trans = pTrans;
        namedSourceLookup = new NamedSourceLookup();
        keyedSourceLookup = new KeyedSourceLookup();
    }

    /**
     * Finds a PRB_Source record for a given source name. If a Translator object
     * has been set up, it will be used to attempt to resolve the source name
     * before doing a lookup.
     * @assumes Nothing
     * @effects Nothing
     * @param name The source name to look up.
     * @return A PRB_SourceDAO object that represents the PRB_Source record.
     * @throws LookupException
     */
    public PRB_SourceDAO findByName (String name)
      throws DBException, CacheException, TranslationException
    {
        if (trans != null)
        {
            KeyedDataAttribute kda = trans.translate(name);
            if (kda != null)
            {
                return keyedSourceLookup.lookup(kda.getKey());
            }
        }
        return namedSourceLookup.lookup(name);
    }

    /**
     * Finds a PRB_Source record for a given source key.
     * @assumes Nothing
     * @effects Nothing
     * @param name The source key to look up.
     * @return A PRB_SourceDAO object that represents the PRB_Source record.
     * @throws CacheException
     * @throws ConfigException
     * @throws DBException
     * @throws LookupException
     */
    public PRB_SourceDAO findByKey (Integer key)
        throws CacheException, ConfigException, DBException
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
         * @throws CacheException
         * @throws ConfigException
         * @throws DBException
         */
        public NamedSourceLookup()
           throws CacheException, ConfigException, DBException
        {
            super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
        }

        /**
         * Looks up a PRB_Source record for a given source name.
         * @assumes Nothing
         * @effects Nothing
         * @param name The source name to look up.
         * @return A PRB_SourceDAO object that represents the PRB_Source record.
         * @throws LookupException
         */
        public PRB_SourceDAO lookup(String name)
        throws DBException, CacheException
        {
            return (PRB_SourceDAO)super.lookupNullsOk(name);
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
         * @return The RowDataInterpreter object.
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
                    PRB_SourceDAO probeSrcDAO =
                        (PRB_SourceDAO) probeSrcInterpreter.interpret(row);
                    return new KeyValue(probeSrcDAO.getState().getName(),
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
         * @throws CacheException
         * @throws ConfigException
         * @throws DBException
         */
        public KeyedSourceLookup()
           throws CacheException, ConfigException, DBException
        {
            super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
        }

        /**
         * Looks up a PRB_Source record for a given source key.
         * @assumes Nothing
         * @effects Nothing
         * @param key The source key to look up.
         * @return A PRB_SourceDAO object that represents the PRB_Source record.
         * @throws LookupException
         */
        public PRB_SourceDAO lookup(Integer key)
          throws DBException, CacheException
        {
            return (PRB_SourceDAO)super.lookupNullsOk(key);
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
         * @return The RowDataInterpreter object.
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
                    PRB_SourceDAO probeSrcDAO =
                        (PRB_SourceDAO) probeSrcInterpreter.interpret(row);
                    return new KeyValue(probeSrcDAO.getKey().getKey(),
                                        probeSrcDAO);
                }
            }
            return new Interpreter();
        }
    }

    /**
     * @is An object that knows how create a PRB_SourceDAO object from a row
     *     of data from the PRB_Source table.
     * @has Nothing
     * @does
     *   <UL>
     *   <LI> Provides a method to interpret a row of data.
     *   </UL>
     * @company The Jackson Laboratory
     * @author dbm
     * @version 1.0
     */

    private class ProbeSourceInterpreter implements RowDataInterpreter
    {
        /**
        * Interprets a row of data from the PRB_Source table to create a
        * PRB_SourceDAO object.
        * @assumes Nothing
        * @effects Nothing
        * @param row A row of data.
        * @return A PRB_SourceDAO object that contains a row of data.
        * @throws DBException
        */
        public Object interpret (RowReference row)
            throws DBException
        {
            PRB_SourceKey probeSrcKey =
                new PRB_SourceKey(row.getInt("_Source_key"));
            PRB_SourceState probeSrcState = new PRB_SourceState();
            probeSrcState.setSegmentTypeKey(row.getInt("_SegmentType_key"));
            probeSrcState.setVectorKey(row.getInt("_Vector_key"));
            probeSrcState.setProbeSpeciesKey(row.getInt("_ProbeSpecies_key"));
            probeSrcState.setStrainKey(row.getInt("_Strain_key"));
            probeSrcState.setTissueKey(row.getInt("_Tissue_key"));
            probeSrcState.setRefsKey(row.getInt("_Refs_key"));
            probeSrcState.setName(row.getString("name"));
            probeSrcState.setDescription(row.getString("description"));
            probeSrcState.setAge(row.getString("age"));
            probeSrcState.setAgeMin(row.getFloat("ageMin"));
            probeSrcState.setAgeMax(row.getFloat("ageMax"));
            probeSrcState.setAge(row.getString("sex"));
            probeSrcState.setAge(row.getString("cellLine"));
            PRB_SourceDAO probeSrcDAO =
                new PRB_SourceDAO(probeSrcKey, probeSrcState);
            return probeSrcDAO;
        }
    }
}


//  $Log$
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
