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
  private Integer translationType = null;

  public TestStrainKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    sqlMgr = new SQLDataManager();
    String query = "select _translationType_key " +
                   "from mgi_translationType " +
                   "where translationType = 'Strains'";

    ResultsNavigator nav = sqlMgr.executeQuery(query);
    nav.next();
    RowReference row = nav.getRowReference();
    translationType = row.getInt(1);
    sqlMgr.executeUpdate(
        "delete prb_strain where _strain_key = -50"
        );
    sqlMgr.executeUpdate(
        "delete mgi_translation where _translation_key = -50"
        );
    sqlMgr.executeUpdate(
        "insert into prb_strain values (-50, 'CB100', 1, 0, 0, " +
        "getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into mgi_translation values (-50, " + translationType +
        ", -50, 'CB/100 strain', 1, 1000, 1000, getDate(), getDate())"
        );
    strainKeyLookup = new StrainKeyLookup();
  }

  protected void tearDown() throws Exception {
    sqlMgr.executeUpdate(
        "delete prb_strain where _strain_key = -50"
        );
    sqlMgr.executeUpdate(
        "delete mgi_translation where _translation_key = -50"
        );
    strainKeyLookup = null;
    sqlMgr = null;
    translationType = null;
    super.tearDown();
  }

  public void testLookup() throws Exception {
    assertEquals(new Integer(-50), strainKeyLookup.lookup("CB/100 strain"));
    assertEquals(new Integer(-50), strainKeyLookup.lookup("CB100"));
  }

  public void testKeyNotFound() throws Exception {
    try
    {
      strainKeyLookup.lookup("something that we know is not in there");
      assertTrue(false); // we should not get here
    }
    catch (KeyNotFoundException e)
    {
      assertTrue(true);
    }

  }

}
