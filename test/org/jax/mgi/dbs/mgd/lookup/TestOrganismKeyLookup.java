package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;

public class TestOrganismKeyLookup
    extends TestCase {
  private OrganismKeyLookup organismKeyLookup = null;
  private SQLDataManager sqlMgr = null;
  private Integer translationType = null;

  public TestOrganismKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    sqlMgr = new SQLDataManager();
    String query = "select _translationType_key " +
                   "from mgi_translationType " +
                   "where translationType = 'Organisms'";

    ResultsNavigator nav = sqlMgr.executeQuery(query);
    nav.next();
    RowReference row = nav.getRowReference();
    translationType = row.getInt(1);

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
        "insert into mgi_translation values (-70, " + translationType +
        ", -50, 'Mus musculus organism', 1, 1200, 1200, getDate(), getDate())"
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
    translationType = null;
    super.tearDown();
  }

  public void testLookup() throws Exception {
    assertEquals(new Integer(-50),
                 organismKeyLookup.lookup("Mus musculus organism"));
    assertEquals(new Integer(-50), organismKeyLookup.lookup("mouse2"));

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
