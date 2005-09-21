package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;


/**
 * <p>@is </p>
 * <p>@has </p>
 * <p>@does </p>
 * <p>@company The Jackson Laboratory</p>
 * @author not attributable
 *
 */

public class VocVocabLookup extends LazyCachedLookup
{
    public VocVocabLookup(SQLDataManager sqlMgr)
    throws CacheException
    {
        super(sqlMgr);
    }
    public String getPartialInitQuery()
    {
        return null;
    }
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


    public String getAddQuery(Object addObject)
    {
        String name = (String)addObject;
        return "select  v._vocab_key, v._refs_key,  " +
               "        v._logicaldb_key,  v.name,  b.jnumID, " +
               "        l.name as 'logicaldb', v.isSimple, v.isPrivate " +
               "from VOC_Vocab v, acc_logicaldb l, bib_view b " +
               "where v.name = 'PIR Superfamily' " +
               "and v._refs_key = b._refs_key " +
               "and v._logicaldb_key = l._logicaldb_key ";
    }

    public VocVocab lookup(String name)
    throws DBException, CacheException, KeyNotFoundException
    {
        return (VocVocab)super.lookup(name);
    }


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