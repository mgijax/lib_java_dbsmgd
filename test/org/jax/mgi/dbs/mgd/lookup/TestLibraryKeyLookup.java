package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.shr.cache.*;

public class TestLibraryKeyLookup
    extends TestCase {
  private LibraryKeyLookup libraryKeyLookup = null;
   private SQLDataManager sqlMgr = null;
   private Integer translationType = null;

  public TestLibraryKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    sqlMgr = new SQLDataManager();
    String query = "select _translationType_key " +
                   "from mgi_translationType " +
                   "where translationType = 'Library'";

    ResultsNavigator nav = sqlMgr.executeQuery(query);
    nav.next();
    RowReference row = nav.getRowReference();
    translationType = row.getInt(1);

    sqlMgr.executeUpdate(
        "delete from prb_source where _source_key = -50"
        );
    sqlMgr.executeUpdate(
        "delete from mgi_translation where _translation_key = -80"
        );
    sqlMgr.executeUpdate(
        "insert into prb_source values (-50, 63470, 76017, 1, 11966, -1, " +
        "74831, 75982, 64221, 'RPCI-2', null, 'Not Specified', -1.0, " +
        "-1.0, 1, 1060, 1060, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into mgi_translation values (-80, " + translationType +
        ", -50, 'RPCI/22 Clone Set', 1, 1200, 1200, getDate(), getDate())"
        );
    libraryKeyLookup = new LibraryKeyLookup();
  }

  protected void tearDown() throws Exception {
    sqlMgr.executeUpdate(
        "delete from prb_source where _source_key = -50"
        );
    sqlMgr.executeUpdate(
        "delete from mgi_translation where _translation_key = -80"
        );
    libraryKeyLookup = null;
    sqlMgr = null;
    super.tearDown();
  }

  public void testLookup() throws Exception
  {
    assertEquals(new Integer(-50),
                 libraryKeyLookup.lookup("RPCI/22 Clone Set"));
    assertEquals(new Integer(-50),
                 libraryKeyLookup.lookup("RPCI-2"));
  }

  public void testKeyNotFound() throws Exception {
    try
    {
      libraryKeyLookup.lookup("something that we know is not in there");
      assertTrue(false); // we should not get here
    }
    catch (KeyNotFoundException e)
    {
      assertTrue(true);
    }
  }


}
