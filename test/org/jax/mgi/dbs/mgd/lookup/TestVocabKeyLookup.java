package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.mgd.trans.*;
import org.jax.mgi.dbs.mgd.VocabularyTypeConstants;
import org.jax.mgi.shr.config.*;

public class TestVocabKeyLookup
    extends TestCase {
  private VocabKeyLookup seqKeyLookup = null;
  private VocabKeyLookup genderKeyLookup = null;
  private VocabKeyLookup cellLineKeyLookup = null;
  private SQLDataManager sqlMgr = null;

  public TestVocabKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    seqKeyLookup = new VocabKeyLookup(VocabularyTypeConstants.SEQUENCETYPE);
    genderKeyLookup = new VocabKeyLookup(VocabularyTypeConstants.GENDER);
    cellLineKeyLookup = new VocabKeyLookup(VocabularyTypeConstants.CELLLINE);
    sqlMgr = new SQLDataManager();
    sqlMgr.executeUpdate(
        "delete from voc_term where _term_key = -50"
        );
    sqlMgr.executeUpdate(
        "delete from mgi_translation where _translation_key = -90"
        );
    sqlMgr.executeUpdate(
        "delete from voc_term where _term_key = -60"
        );
    sqlMgr.executeUpdate(
        "delete from mgi_translation where _translation_key = -100"
        );
    sqlMgr.executeUpdate(
        "delete from voc_term where _term_key = -70"
        );
    sqlMgr.executeUpdate(
        "delete from mgi_translation where _translation_key = -110"
        );

    sqlMgr.executeUpdate(
        "insert into voc_term values (-50, 15, 'Female', 'F', 1, 0, 1200, " +
        "1200, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into voc_term values (-60, 17, 'B-cell', null, 113, 0, " +
        "1200, 1200, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into mgi_translation values (-90, 1000, -50, 'she', 1, " +
        "1200, 1200, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into mgi_translation values (-100, 1004, -60, " +
        "'Not to B-cell', 1, 1200, 1200, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into voc_term values (-70,  21, 'DNA', 'D', 1, 0, 1200, " +
        "1200, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into mgi_translation values (-110, 1002, -70, " +
        "'Deoxyribonucleic Acid', 1, 1200, 1200, getDate(), getDate())"
        );
  }

  protected void tearDown() throws Exception {
    sqlMgr.executeUpdate(
        "delete from voc_term where _term_key = -50"
        );
    sqlMgr.executeUpdate(
        "delete from mgi_translation where _translation_key = -90"
        );
    sqlMgr.executeUpdate(
        "delete from voc_term where _term_key = -60"
        );
    sqlMgr.executeUpdate(
        "delete from mgi_translation where _translation_key = -100"
        );
    sqlMgr.executeUpdate(
        "delete from voc_term where _term_key = -70"
        );
    sqlMgr.executeUpdate(
        "delete from mgi_translation where _translation_key = -110"
        );
    seqKeyLookup = null;
    genderKeyLookup = null;
    sqlMgr = null;
    super.tearDown();
  }

  public void testGenderLookup() throws Exception {
    assertNull(genderKeyLookup.getTranslatedTerm());
    assertEquals(new Integer(-50), genderKeyLookup.lookup("she"));
    assertEquals("Female", this.genderKeyLookup.getTranslatedTerm());
    assertEquals(new Integer(-50), genderKeyLookup.lookup("Female"));
    assertEquals("Female", this.genderKeyLookup.getTranslatedTerm());
  }

  public void testCellLineLookup() throws Exception {
    assertEquals(new Integer(-60), cellLineKeyLookup.lookup("B-cell"));
    assertEquals("B-cell", cellLineKeyLookup.getTranslatedTerm());
    assertEquals(new Integer(-60), cellLineKeyLookup.lookup("Not to B-cell"));
    assertEquals("B-cell", cellLineKeyLookup.getTranslatedTerm());
  }


  public void testSeqLookup() throws Exception {
    assertEquals(new Integer(-70),
                 seqKeyLookup.lookup("Deoxyribonucleic Acid"));
    assertEquals("DNA", seqKeyLookup.getTranslatedTerm());
  }

  public void testKeyNotFound() throws Exception
  {
    try
    {
      seqKeyLookup.lookup("something that we know is not in there");
      assertTrue(false); // we should not get here
    }
    catch (KeyNotFoundException e)
    {
      assertTrue(true);
    }
  }



}
