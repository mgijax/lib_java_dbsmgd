package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.mgd.trans.*;
import org.jax.mgi.shr.config.*;

public class TestLibraryKeyLookup
    extends TestCase {
  private LibraryKeyLookup libraryKeyLookup = null;
   private SQLDataManager sqlMgr = null;

  public TestLibraryKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    libraryKeyLookup = new LibraryKeyLookup();
    sqlMgr = new SQLDataManager();
  }

  protected void tearDown() throws Exception {
    libraryKeyLookup = null;
    sqlMgr = null;
    super.tearDown();
  }

  public void testLookup() throws CacheException, DBException,
      TranslationException, ConfigException {
    String sql = "SELECT _source_key " +
        "FROM prb_source " +
        "WHERE name = 'RPCI-22'";
    ResultsNavigator nav = sqlMgr.executeQuery(sql);
    nav.next();
    RowReference row = (RowReference) nav.getCurrent();
    Integer key = row.getInt(1);

    String term = "RPCI-22";
    Integer expectedReturn = key;
    Integer actualReturn = libraryKeyLookup.lookup(term);
    assertEquals("return value", expectedReturn, actualReturn);
  }

}
