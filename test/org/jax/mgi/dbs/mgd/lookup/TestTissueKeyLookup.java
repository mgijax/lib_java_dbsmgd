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
    sqlMgr = new SQLDataManager();
    sqlMgr.executeUpdate(
        "insert into prb_tissue values (-50, 'placenta 21', " +
        "1, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into mgi_translation values (-50, 1003, -50, " +
        "'placenta day 21', 1, 1200, 1200, getDate(), getDate())"
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
    super.tearDown();
  }

  public void testLookup() throws Exception {
    assertNull(tissueKeyLookup.getTranslatedTerm());
    assertEquals(new Integer(-50), tissueKeyLookup.lookup("placenta day 21"));
    assertEquals("placenta 21", tissueKeyLookup.getTranslatedTerm());
    assertEquals(new Integer(-50), tissueKeyLookup.lookup("placenta 21"));
    assertEquals("placenta 21", tissueKeyLookup.getTranslatedTerm());
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

