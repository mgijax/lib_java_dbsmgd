package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.shr.cache.FullCachedLookup;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.InterpretException;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.dbs.mgd.dao.PRB_SourceDAO;
import org.jax.mgi.dbs.mgd.dao.PRB_SourceInterpreter;
import org.jax.mgi.dbs.mgd.TranslationTypeConstants;
import org.jax.mgi.dbs.mgd.MGD;

/**
 * @is An object that knows how to look up a PRB_Source record for a given
 * source name.
 * @has nothing
 * @does
 *   <UL>
 *   <LI> Provides a method to look up a PRB_Source record.
 *   </UL>
 * @company The Jackson Laboratory
 * @author mbw
 * @version 1.0
 */

public class NamedSourceLookup extends FullCachedLookup
{
    /**
     * Constructs a NamedSourceLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     * @throws DBException thrown if there is an error accessing the
     * database
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
     * @throws DBException thrown if there is an error with the database
     * @throws CacheException thrown if there is an error accessing the cache
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
     * @return The query to fully initialize the cache.
     */
    public String getFullInitQuery()
    {
        String stmt =
            "SELECT " +
               "t." + MGD.mgi_translation.badname + " libName, " +
               "s.* " +
            "FROM " + MGD.mgi_translation._name + " t, " +
                      MGD.prb_source._name + " s " +
            "WHERE " + "s." + MGD.prb_source._source_key + " = " +
                       "t." + MGD.mgi_translation._object_key + " " +
            "AND " + "t." + MGD.mgi_translation._translationtype_key + " = " +
                     TranslationTypeConstants.LIBRARY + " " +
            "UNION " +
            "SELECT " +
               MGD.prb_source.name + " libName, " +
               "* " +
            "FROM " + MGD.prb_source._name + " " +
            "WHERE " + MGD.prb_source.name + " != null";
        return stmt;
    }

    /**
     * Get a RowDataInterpreter for creating a KeyValue object from a
     * database row which is used for creating a new cache entry.
     * @assumes nothing
     * @effects nothing
     * @return The RowDataInterpreter object.
     */
    public RowDataInterpreter getRowDataInterpreter()
    {
        class Interpreter implements RowDataInterpreter
        {
            private PRB_SourceInterpreter probeSrcInterpreter =
                new PRB_SourceInterpreter();

            public Object interpret (RowReference row)
                throws DBException, InterpretException
            {
                PRB_SourceDAO probeSrcDAO =
                    (PRB_SourceDAO) probeSrcInterpreter.interpret(row);
                return new KeyValue(row.getString("libName"), probeSrcDAO);
            }
        }
        return new Interpreter();
    }


}
