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
 * @is An object that knows how to look up a chromosome key given a chromosome name.
 * @has Nothing
 * @does
 *   <UL>
 *   <LI> Provides a method to look up a chromosome
 *   </UL>
 * @company The Jackson Laboratory
 * @author sc
 * @version 1.0
 */

public class ChromosomeKeyLookup extends FullCachedLookup {
    Integer organismKey;

    /**
     * Constructs a ChromosomeKeyLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     * @throws DBException thrown if there is an error accessing the
     * database
     */
    public ChromosomeKeyLookup ( String organism )
        throws CacheException, ConfigException, DBException,
            KeyNotFoundException, TranslationException {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
        organismKey = new OrganismKeyLookup().lookup(organism);
    }


    /**
     * Looks up a chromosome to find its key.
     * @assumes Nothing
     * @effects Nothing
     * @param chromosome The chromosome to look up.
     * @return An Integer object containing the key for the chromosome or a null
     *         if the logical DB was not found.
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws DBException thrown if there is an error accessing the
     * database
     * @throws KeyNotFoundException thrown if the key is not found
     */
    public Integer lookup (String chromosome)
        throws KeyNotFoundException, DBException, CacheException {
        return (Integer)super.lookup(chromosome);
    }


    /**
     * Get the query to fully initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @return The query to fully initialize the cache.
     */
    public String getFullInitQuery () {
        return new String(
            "SELECT chromosome, _Chromosome_key FROM MRK_Chromosome " +
                 "where _Organism_key = " + organismKey);
    }


    /**
     * Get a RowDataInterpreter for creating a KeyValue object from a database
     * used for creating a new cache entry.
     * @assumes nothing
     * @effects nothing
     * @return The RowDataInterpreter object.
     */
    public RowDataInterpreter getRowDataInterpreter() {
        class Interpreter implements RowDataInterpreter {
            public Object interpret (RowReference row)
                throws DBException {
                return new KeyValue(row.getString(1), row.getInt(2));
            }
        }
        return new Interpreter();
    }
}

//  $Log
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
