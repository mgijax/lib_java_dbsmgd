package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;

/**
 * is a lazy cached lookup for vocabulary definitions in the database
 * @has an internal cache
 * @does provides a lookup for accessing vocabulary definitions
 * @Company: The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class VocVocabLookup extends LazyCachedLookup
{
    /**
     * constructor
     * @param sqlMgr the SQLDataManager to use
     * @throws CacheException thrown if there is an error creating the cache
     */
    public VocVocabLookup(SQLDataManager sqlMgr)
    throws CacheException
    {
        super(sqlMgr);
    }
    /**
     * get the query to partially initialize the cache which returns null so
     * no initialization is performed
     * @return the partial initialization query which is null
     */
    public String getPartialInitQuery()
    {
        return null;
    }
    /**
     * get a RowDataInterpreter for interpreting query results
     * @return the RowDataInterpreter
     */
    public RowDataInterpreter getRowDataInterpreter()
    {
        class Interpreter implements RowDataInterpreter
        {
            public Object interpret(RowReference row)
            throws DBException
            {
                VocVocab voc = new VocVocab();
                voc.vocabKey = row.getInt(1).intValue();
                voc.refsKey = row.getInt(2).intValue();
                voc.logicaldbKey = row.getInt(3).intValue();
                voc.name = row.getString(4);
                voc.jnumber = row.getString(5);
                voc.logicaldb = row.getString(6);
                voc.isSimple = row.getBoolean(7).booleanValue();
                voc.isPrivate = row.getBoolean(8).booleanValue();
                return new KeyValue(voc.name, voc);
            }
        }
        return new Interpreter();
    }

    /**
     * get the query to use when adding ne entries tothe cache
     * @param addObject the lookup object
     * @return
     */
    public String getAddQuery(Object addObject)
    {
        String name = (String)addObject;
        return "select  v._vocab_key, v._refs_key,  " +
               "        v._logicaldb_key,  v.name,  b.jnumID, " +
               "        l.name as 'logicaldb', v.isSimple, v.isPrivate " +
               "from VOC_Vocab v, acc_logicaldb l, bib_view b " +
               "where v.name = '" + name + "' " +
               "and v._refs_key = b._refs_key " +
               "and v._logicaldb_key = l._logicaldb_key ";
    }
    /**
     * look up vocabulary definition by vocabulary name
     * @param name the name of th evocabulary as defined in the database
     * @return the vocabulary definition as a VocVocab object
     * @throws DBException thrown if there is an error accessing the database
     * @throws CacheException thrown if there is an error accessing the cache
     * @throws KeyNotFoundException thrown if the vocabulary is not found
     */
    public VocVocab lookup(String name)
    throws DBException, CacheException, KeyNotFoundException
    {
        return (VocVocab)super.lookup(name);
    }

    /**
     * is a plain old java object for storing vocabulary information
     * @has vocabulary attributes
     * @does nothing
     * @Company: The Jackson Laboratory
     * @author M Walker
     * @version 1.0
     */
    public class VocVocab
    {
        public String name = null;
        public String jnumber = null;
        public String logicaldb = null;
        public int vocabKey;
        public int refsKey;
        public int logicaldbKey;
        public boolean isSimple;
        public boolean isPrivate;
    }
}
