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
    sqlMgr.executeUpdate(
        "delete from mgi_organism where _organism_key = -50"
        );
    sqlMgr.executeUpdate(
        "delete from mgi_translation where _translation_key = -70"
        );
    sqlMgr.executeUpdate(
        "insert into mgi_organism values (-50, 'mouse2', 'Mus musculus', " +
        "1200, 1200, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into mgi_translation values (-70, 1001, -50, " +
        "'Mus musculus organism', 1, 1200, 1200, getDate(), getDate())"
        );
    organismKeyLookup = new OrganismKeyLookup();
  }

  protected void tearDown() throws Exception {
    sqlMgr.executeUpdate(
        "delete from mgi_organism where _organism_key = -50"
        );
    sqlMgr.executeUpdate(
        "delete from mgi_translation where _translation_key = -70"
        );
    organismKeyLookup = null;
    sqlMgr = null;
    super.tearDown();
  }

  public void testLookup() throws Exception {
    assertNull(organismKeyLookup.getTranslatedTerm());
    assertEquals(new Integer(-50),
                 organismKeyLookup.lookup("Mus musculus organism"));
    assertEquals("mouse2", this.organismKeyLookup.getTranslatedTerm());
    assertEquals(new Integer(-50), organismKeyLookup.lookup("mouse2"));
    assertEquals("mouse2", this.organismKeyLookup.getTranslatedTerm());
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

  public void testKeyNotFound() throws Exception {
    try
    {
      organismKeyLookup.lookup("something that we know is not in there");
      assertTrue(false); // we should not get here
    }
    catch (KeyNotFoundException e)
    {
      assertTrue(true);
    }
  }



}
