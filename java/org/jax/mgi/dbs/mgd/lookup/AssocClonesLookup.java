package org.jax.mgi.dbs.mgd.lookup;

import java.util.Vector;

import org.jax.mgi.shr.cache.CachedLookup;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.CacheConstants;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.MultiRowInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.dbs.SchemaConstants;
import org.jax.mgi.dbs.mgd.dao.PRB_SourceInterpreter;
import org.jax.mgi.dbs.mgd.MGITypeConstants;
import org.jax.mgi.dbs.mgd.LogicalDBConstants;
import org.jax.mgi.dbs.mgd.SegmentTypeConstants;
import org.jax.mgi.dbs.mgd.MGD;

/**
 * @is An object that knows how to look up a clones associated with a given
 * sequence
 * @has a internal cache
 * @does
 *   <UL>
 *   <LI> Provides a method to look up associated clones for a given sequence
 *   </UL>
 * @company The Jackson Laboratory
 * @author mbw
 * @version 1.0
 */

public class AssocClonesLookup extends CachedLookup
{

  public static final String DELIMITER = ":";

    /**
     * Constructor
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws CacheException
     * @throws ConfigException
     * @throws DBException
     */
    public AssocClonesLookup()
       throws CacheException, ConfigException, DBException
    {
        super(CacheConstants.FULL_CACHE,
              SQLDataManagerFactory.getShared(SchemaConstants.MGD));
    }

    /**
     * Looks up associated clone names for a given accid and if there are
     * 1 to manys then the names are concatenated together using a '^' as a
     * delimiter
     * @assumes Nothing
     * @effects Nothing
     * @param accid the accid to search on
     * @return clone names associated with the given accid
     * @throws DBException thrown if there is an error with the database
     * @throws CacheException thrown if there is an error accessing the cache
     */
    public String lookup(String accid)
    throws DBException, CacheException
    {
        return (String)super.lookupNullsOk(accid);
    }

    /**
     * Get the query to fully initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The query to fully initialize the cache.
     */
    public String getFullInitQuery()
    {
        String stmt =
            "SELECT " +
               "acc." + MGD.acc_accession.accid + ", " +
               "src." + MGD.prb_source.name + " " +
            "FROM " +
                MGD.acc_accession._name + " acc, " +
                MGD.prb_probe._name + " prb, " +
                MGD.prb_source._name + " src " +
            "WHERE " +
                "acc." + MGD.acc_accession._logicaldb_key + " = " +
                    LogicalDBConstants.SEQUENCE + " " +
            "AND " +
                "acc." + MGD.acc_accession._mgitype_key + " = " +
                    MGITypeConstants.CLONE + " " +
            "AND " +
                "acc." + MGD.acc_accession._object_key + " = " +
                "prb." + MGD.prb_probe._probe_key + " " +
            "AND " +
                "prb." + MGD.prb_probe._source_key + " = " +
                "src." + MGD.prb_source._source_key + " " +
            "AND (" +
                "prb." + MGD.prb_probe._segmenttype_key + " = " +
                   SegmentTypeConstants.CDNA + " " +
                "OR " +
                "prb." + MGD.prb_probe._segmenttype_key + " = " +
                   SegmentTypeConstants.GENOMIC + " " +
                "OR " +
                "prb." + MGD.prb_probe._segmenttype_key + " = " +
                   SegmentTypeConstants.NOT_SPECIFIED + " " +
                ")" +
            "AND " +
                "src." + MGD.prb_source.name + " != null";
        return stmt;
    }

    /**
     * Get the query to partially initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The query to partially initialize the cache.
     */
    public String getPartialInitQuery()
    {
      return null;
    }

    /**
     * Get the query to fully initialize the cache.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The query to fully initialize the cache.
     */
    public String getAddQuery(Object o)
    {
        String accid = (String)o;
        String stmt =
            "SELECT " +
               "acc." + MGD.acc_accession.accid + ", " +
               "src." + MGD.prb_source.name + " " +
            "FROM " +
                MGD.acc_accession._name + " acc, " +
                MGD.prb_probe._name + " prb, " +
                MGD.prb_source._name + " src " +
            "WHERE " +
                "acc." + MGD.acc_accession.accid + " = '" + accid + "' " +
            "AND " +
                "acc." + MGD.acc_accession._object_key + " = " +
                "prb." + MGD.prb_probe._probe_key + " " +
            "AND " +
                "prb." + MGD.prb_probe._source_key + " = " +
                "src." + MGD.prb_source._source_key + " " +
            "AND (" +
                "prb." + MGD.prb_probe._segmenttype_key + " = " +
                   SegmentTypeConstants.CDNA + " " +
                "OR " +
                "prb." + MGD.prb_probe._segmenttype_key + " = " +
                   SegmentTypeConstants.GENOMIC + " " +
                "OR " +
                "prb." + MGD.prb_probe._segmenttype_key + " = " +
                   SegmentTypeConstants.NOT_SPECIFIED + " " +
                ")" +
            "AND " +
                "src." + MGD.prb_source.name + " != null";
        return stmt;
    }



    /**
     * Get a MultiRowInterpreter for creating a KeyValue object from a
     * set of database rows which is used by the FullCacheStrategy for creating
     * a new cache entry. Since this method is an implementation for an
     * abstract method, the return type must be RowDataInterpreter. The
     * FullCacheStategy class will see that the return type is actually a
     * MultiRowInterpreter and cast it appropriately.
     * @assumes nothing
     * @effects nothing
     * @param None
     * @return The MultiRowInterpreter object.
     */
    public RowDataInterpreter getRowDataInterpreter() {
      class Interpreter
          implements MultiRowInterpreter {
        private PRB_SourceInterpreter probeSrcInterpreter =
            new PRB_SourceInterpreter();

        public Object interpret(RowReference row) throws DBException {
          return new KeyValue(row.getString(1), row.getString(2));
        }

        public Object interpretKey(RowReference row) throws DBException {
          return row.getString(1);
        }

        public Object interpretRows(Vector v) {
          KeyValue keyValue = (KeyValue)v.get(0);
          String accid = (String)keyValue.getKey();
          String clones = (String)keyValue.getValue();
          for (int i = 1; i < v.size(); i++)
          {
            String nextClone = (String)((KeyValue)v.get(i)).getValue();
            clones = clones.concat(DELIMITER + nextClone);
          }
          return new KeyValue(accid, clones);
        }
      }

      return new Interpreter();
    }


}
