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
   private Integer translationType = null;

  public TestTissueKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    sqlMgr = new SQLDataManager();
    String query = "select _translationType_key " +
                   "from mgi_translationType " +
                   "where translationType = 'Tissues'";

    ResultsNavigator nav = sqlMgr.executeQuery(query);
    nav.next();
    RowReference row = nav.getRowReference();
    translationType = row.getInt(1);

    sqlMgr.executeUpdate(
        "delete prb_tissue where _tissue_key = -50"
        );
    sqlMgr.executeUpdate(
        "delete mgi_translation where _translation_key = -50"
        );
    sqlMgr.executeUpdate(
        "insert into prb_tissue values (-50, 'placenta 21', " +
        "1, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into mgi_translation values (-50, " + translationType +
        ", -50, 'placenta day 21', 1, 1200, 1200, getDate(), getDate())"
        );
    tissueKeyLookup = new TissueKeyLookup();
  }

  protected void tearDown() throws Exception {
    sqlMgr.executeUpdate(
        "delete prb_tissue where _tissue_key = -50"
        );
    sqlMgr.executeUpdate(
        "delete mgi_translation where _translation_key = -50"
        );
    tissueKeyLookup = null;
    sqlMgr = null;
    translationType = null;
    super.tearDown();
  }

  public void testLookup() throws Exception {
    assertEquals(new Integer(-50), tissueKeyLookup.lookup("placenta day 21"));
    assertEquals(new Integer(-50), tissueKeyLookup.lookup("placenta 21"));
  }

  public void testKeyNotFound() throws Exception
  {
    try
    {
      tissueKeyLookup.lookup("something that we know is not in there");
      assertTrue(false); // we should not get here
    }
    catch (KeyNotFoundException e)
    {
      assertTrue(true);
    }
  }

}

