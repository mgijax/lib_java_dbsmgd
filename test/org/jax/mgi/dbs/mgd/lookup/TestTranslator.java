package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.mgd.*;

public class TestTranslator
    extends TestCase {
  private Translator translator = null;
  private SQLDataManager sqlMgr = null;
  private Integer translationType = null;
  private Integer vocabType = null;

  public TestTranslator(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    translator = new Translator(TranslationTypeConstants.GENDER,
                                CacheConstants.FULL_CACHE);
    sqlMgr = new SQLDataManager();
    String query = "select _translationType_key " +
                   "from mgi_translationType " +
                   "where translationType = 'Gender'";

    ResultsNavigator nav = sqlMgr.executeQuery(query);
    nav.next();
    RowReference row = nav.getRowReference();
    translationType = row.getInt(1);
    String query2 = "select _vocab_key " +
                    "from voc_vocab " +
                    "where name = 'Gender'";

    nav = sqlMgr.executeQuery(query2);
    nav.next();
    row = nav.getRowReference();
    vocabType = row.getInt(1);
    doDeletes();
    doInserts();
  }

  protected void tearDown() throws Exception {
    doDeletes();
    translator = null;
    sqlMgr = null;
    translationType = null;
    vocabType = null;
    super.tearDown();
  }

  public void testTranslate() throws Exception {
    String term = "f";
    Integer actualReturn = translator.translate(term);
    assertEquals(-100, actualReturn.intValue());
  }

  public void testStringConstructor() throws Exception {
    Translator translator2 = new Translator("Gender",
                                            CacheConstants.FULL_CACHE);
    String term = "f";
    Integer actualReturn = translator.translate(term);
    assertEquals(-100, actualReturn.intValue());
  }

  public void testLazyCache() throws Exception {
    Translator translator2 = new Translator("Gender",
                                            CacheConstants.LAZY_CACHE);
    String term = "f";
    Integer actualReturn = translator.translate(term);
    assertEquals(-100, actualReturn.intValue());
  }


  public void testSingleQuote() throws Exception {
    Translator translator2 = new Translator("Gender",
                                            CacheConstants.LAZY_CACHE);
    String term = "f'";
    Integer actualReturn = translator2.translate(term);
    assertEquals(-100, actualReturn.intValue());
  }

  private void doInserts() throws Exception
  {
    sqlMgr.executeUpdate("insert into voc_term values (-100, " +
                         vocabType + ", 'Female', 'F', 1, 0, " +
                         "1200, 1200, getDate(), getDate())");
    sqlMgr.executeUpdate("insert into mgi_translation values (-100, " +
                         translationType + ", -100, 'f', 1, 1200, 1200, " +
                         "getDate(), getDate())");
    sqlMgr.executeUpdate("insert into mgi_translation values (-200, " +
                         translationType + ", -100, 'f''', 1, 1200, 1200, " +
                         "getDate(), getDate())");
  }

  private void doDeletes() throws Exception
  {
    sqlMgr.executeUpdate("delete voc_term where _term_key = -100");
    sqlMgr.executeUpdate("delete mgi_translation where " +
                         "_translation_key = -100");
    sqlMgr.executeUpdate("delete mgi_translation where " +
                         "_translation_key = -200");
  }


}
