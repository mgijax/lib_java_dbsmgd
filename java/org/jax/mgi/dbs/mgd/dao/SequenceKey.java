package org.jax.mgi.dbs.mgd.dao;

import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.Table;

/**
 * @is An object that has the primary key for a Probe object.
 * @has
 *   <UL>
 *   <LI> The primary key for a Probe object
 *   </UL>
 * @does
 *   <UL>
 *   <LI> Provides a get method for its key.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class SequenceKey
{

    // The primary key for a Probe object.
    //
    private int primaryKey;


    /**
     * Constructs a new ProbeKey object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws Nothing
     */
    public SequenceKey ()
        throws ConfigException, DBException
    {
        SQLDataManager sqlMgr =
            SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD);
        primaryKey = new Table("SEQ_Sequence",sqlMgr).getNextKey();
    }


    /**
     * Constructs a new SequenceKey object.
     * @assumes Nothing
     * @effects Nothing
     * @param pKey value to set the primary key
     * @throws Nothing
     */
    public SequenceKey (int pKey)
    {
        primaryKey = pKey;
    }


    /**
     * Get the primaryKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The primaryKey attribute
     * @throws Nothing
     */
    public int getPrimaryKey () { return primaryKey; }
}
