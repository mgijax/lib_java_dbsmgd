//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.lookup;

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
 * @is An object that knows how to look up a term name for a given term key
 * in the voc_term table.
 * @has Nothing
 * @does
 *   <UL>
 *   <LI> Provides a method to look up a term name.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class VocabTermLookup extends LazyCachedLookup
{
    /**
     * Constructs a TermNameLookup object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws CacheException
     * @throws ConfigException
     * @throws DBException
     */
    public VocabTermLookup ()
        throws  CacheException, ConfigException, DBException
    {
        super(SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    }


    /**
    * Looks up a term name for a term key.
    * @assumes Nothing
    * @effects Nothing
    * @param termKey The term key to look up.
    * @return The term name or a null if the term key was not found.
    * @throws LookupException
    */
    public String lookup (Integer termKey)
    throws CacheException, DBException
    {
        return (String)super.lookupNullsOk(termKey);
    }


    /**
     * Get the query to partially initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The query to partially initialize the cache.
     * @throws Nothing
     */
    public String getPartialInitQuery ()
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
    public String getAddQuery (Object addObject)
    {
        return "SELECT _Term_key, term FROM VOC_Term";
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
            public Object interpret (RowReference row)
                throws DBException
            {
                return new KeyValue(row.getInt(1), row.getString(2));
            }
        }
        return new Interpreter();
    }
}


//  $Log$
//  Revision 1.2  2004/02/17 01:27:36  mbw
//  renamed TermNameLookup.java  to VocabTermLookup.java
//
//  Revision 1.1.2.1  2004/02/16 23:36:36  mbw
//  ranamed TermNameLookup.java  to VocabTermLookup.java
//
//  Revision 1.1.2.1  2003/11/04 20:51:27  mbw
//  modified to suit the now defunct LookupException class
//
//  Revision 1.1  2003/10/20 19:01:54  dbm
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
