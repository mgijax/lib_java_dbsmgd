package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.mgd.trans.*;
import org.jax.mgi.dbs.mgd.VocabularyTypeConstants;
import org.jax.mgi.shr.config.*;

public class TestVocabKeyLookup
    extends TestCase {
  private VocabKeyLookup vocabKeyLookup = null;
  private SQLDataManager sqlMgr = null;

  public TestVocabKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    vocabKeyLookup = new VocabKeyLookup(VocabularyTypeConstants.GENDER);
    sqlMgr = new SQLDataManager();
  }

  protected void tearDown() throws Exception {
    vocabKeyLookup = null;
    sqlMgr = null;
    super.tearDown();
  }

  public void testLookup() throws Exception {
    String sql = "SELECT _term_key " +
                 "FROM voc_term " +
                 "WHERE _vocab_key = 15 and term = 'Female'";
    ResultsNavigator nav = sqlMgr.executeQuery(sql);
    nav.next();
    RowReference row = (RowReference)nav.getCurrent();
    Integer key = row.getInt(1);
    String term = "Female";
    Integer expectedReturn = key;
    Integer actualReturn = vocabKeyLookup.lookup(term);
    assertEquals("return value", expectedReturn, actualReturn);
  }

}
