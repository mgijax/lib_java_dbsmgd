package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.mgd.trans.*;
import org.jax.mgi.shr.config.*;

public class TestOrganismKeyLookup
    extends TestCase {
  private OrganismKeyLookup organismKeyLookup = null;
  private SQLDataManager sqlMgr = null;

  public TestOrganismKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    organismKeyLookup = new OrganismKeyLookup();
    sqlMgr = new SQLDataManager();
  }

  protected void tearDown() throws Exception {
    organismKeyLookup = null;
    sqlMgr = null;
    super.tearDown();
  }

  public void testLookup() throws Exception {
    String sql = "SELECT _organism_key " +
        "FROM mgi_organism " +
        "WHERE commonName = 'human'";
    ResultsNavigator nav = sqlMgr.executeQuery(sql);
    nav.next();
    RowReference row = (RowReference) nav.getCurrent();
    Integer key = row.getInt(1);

    String term = "human";
    Integer expectedReturn = key;
    Integer actualReturn = organismKeyLookup.lookup(term);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testTranslatedLookup() throws Exception {
    String sql = "SELECT _organism_key " +
        "FROM mgi_organism " +
        "WHERE commonName = 'mouse, laboratory'";
    ResultsNavigator nav = sqlMgr.executeQuery(sql);
    nav.next();
    RowReference row = (RowReference) nav.getCurrent();
    Integer key = row.getInt(1);

    String term = "Mus musculus";
    Integer expectedReturn = key;
    Integer actualReturn = organismKeyLookup.lookup(term);
    assertEquals("return value", expectedReturn, actualReturn);
  }



}
