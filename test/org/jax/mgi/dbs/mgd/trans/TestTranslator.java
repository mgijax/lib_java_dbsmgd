package org.jax.mgi.dbs.mgd.trans;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.*;
import org.jax.mgi.dbs.mgd.*;

public class TestTranslator
    extends TestCase {
  private Translator translator = null;
  private SQLDataManager sqlMgr = null;

  public TestTranslator(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    translator = new Translator(MGD.translationTypes.Gender,
                                CacheConstants.FULL_CACHE);
    sqlMgr = new SQLDataManager();
    doDeletes();
    doInserts();
  }

  protected void tearDown() throws Exception {
    doDeletes();
    translator = null;
    sqlMgr = null;
    super.tearDown();
  }

  public void testTranslate() throws Exception {
    String term = "f";
    KeyedDataAttribute actualReturn = translator.translate(term);
    assertEquals("Female", actualReturn.getValue());
    assertEquals(-100, actualReturn.getKey().intValue());
  }

  public void testStringConstructor() throws Exception {
    Translator translator2 = new Translator("Gender",
                                            CacheConstants.FULL_CACHE);
    String term = "f";
    KeyedDataAttribute actualReturn = translator.translate(term);
    assertEquals("Female", actualReturn.getValue());
    assertEquals(-100, actualReturn.getKey().intValue());
  }

  public void testSingleQuote() throws Exception {
    Translator translator2 = new Translator("Gender",
                                            CacheConstants.LAZY_CACHE);
    String term = "f'";
    KeyedDataAttribute actualReturn = translator2.translate(term);
    assertEquals("Female", actualReturn.getValue());
    assertEquals(-100, actualReturn.getKey().intValue());
  }

  private void doInserts() throws Exception
  {
    sqlMgr.executeUpdate("insert into voc_term values (-100, 15, " +
                         "'Female', 'F', 1, 0, 1200, 1200, getDate(), " +
                         "getDate())");
    sqlMgr.executeUpdate("insert into mgi_translation values (-100, " +
                         "1000, -100, 'f', 1, 1200, 1200, getDate(), " +
                         "getDate())");
  }

  private void doDeletes() throws Exception
  {
    sqlMgr.executeUpdate("delete voc_term where _term_key = -100");
    sqlMgr.executeUpdate("delete mgi_translation where " +
                         "_translation_key = -100");
  }


}
