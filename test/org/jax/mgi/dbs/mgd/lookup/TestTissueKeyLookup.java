package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.mgd.trans.*;
import org.jax.mgi.shr.config.*;

public class TestTissueKeyLookup
    extends TestCase {
  private TissueKeyLookup tissueKeyLookup = null;
   private SQLDataManager sqlMgr = null;

  public TestTissueKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    tissueKeyLookup = new TissueKeyLookup();
    sqlMgr = new SQLDataManager();
  }

  protected void tearDown() throws Exception {
    tissueKeyLookup = null;
    sqlMgr = null;
    super.tearDown();
  }

  public void testLookup() throws CacheException, DBException,
      TranslationException, ConfigException {
    String sql = "SELECT _tissue_key " +
        "FROM prb_tissue " +
        "WHERE tissue = 'brain'";
    ResultsNavigator nav = sqlMgr.executeQuery(sql);
    nav.next();
    RowReference row = (RowReference) nav.getCurrent();
    Integer key = row.getInt(1);

    String term = "brain";
    Integer expectedReturn = key;
    Integer actualReturn = tissueKeyLookup.lookup(term);
    assertEquals("return value", expectedReturn, actualReturn);
  }

}

