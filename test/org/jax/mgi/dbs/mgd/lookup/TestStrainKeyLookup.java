package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.mgd.trans.*;
import org.jax.mgi.shr.config.*;

public class TestStrainKeyLookup
    extends TestCase {

  private StrainKeyLookup strainKeyLookup = null;
  private SQLDataManager sqlMgr = null;

  public TestStrainKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    strainKeyLookup = new StrainKeyLookup();
    sqlMgr = new SQLDataManager();
  }

  protected void tearDown() throws Exception {
    strainKeyLookup = null;
    sqlMgr = null;
    super.tearDown();
  }

  public void testLookup() throws CacheException, DBException,
      TranslationException, ConfigException {
    String sql = "SELECT _strain_key " +
        "FROM prb_strain " +
        "WHERE strain = 'BALB/cJ'";
    ResultsNavigator nav = sqlMgr.executeQuery(sql);
    nav.next();
    RowReference row = (RowReference) nav.getCurrent();
    Integer key = row.getInt(1);

    String term = "BALB/cJ";
    Integer expectedReturn = key;
    Integer actualReturn = strainKeyLookup.lookup(term);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testTranslatedLookup() throws CacheException, DBException,
      TranslationException, ConfigException {
    String sql = "SELECT _strain_key " +
        "FROM prb_strain " +
        "WHERE strain = 'BALB/cJ'";
    ResultsNavigator nav = sqlMgr.executeQuery(sql);
    nav.next();
    RowReference row = (RowReference) nav.getCurrent();
    Integer key = row.getInt(1);

    String term = "BALB/c/J";
    Integer expectedReturn = key;
    Integer actualReturn = strainKeyLookup.lookup(term);
    assertEquals("return value", expectedReturn, actualReturn);
  }

}
