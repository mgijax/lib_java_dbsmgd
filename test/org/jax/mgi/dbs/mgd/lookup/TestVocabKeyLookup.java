package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.mgd.VocabularyTypeConstants;

public class TestVocabKeyLookup
    extends TestCase {
  private VocabKeyLookup seqKeyLookup = null;
  private VocabKeyLookup genderKeyLookup = null;
  private VocabKeyLookup cellLineKeyLookup = null;
  private SQLDataManager sqlMgr = null;
  private Integer vocGender = null;
  private Integer vocCell = null;
  private Integer vocSeqType = null;
  private Integer transGender = null;
  private Integer transCell = null;
  private Integer transSeqType = null;

  public TestVocabKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    seqKeyLookup = new VocabKeyLookup(VocabularyTypeConstants.SEQUENCETYPE);
    genderKeyLookup = new VocabKeyLookup(VocabularyTypeConstants.GENDER);
    cellLineKeyLookup = new VocabKeyLookup(VocabularyTypeConstants.CELLLINE);
    sqlMgr = new SQLDataManager();
    String query = "select _vocab_key " +
                   "from voc_vocab " +
                   "where name = 'Gender'";

    ResultsNavigator nav = sqlMgr.executeQuery(query);
    nav.next();
    RowReference row = nav.getRowReference();
    vocGender = row.getInt(1);

    String query2 = "select _vocab_key " +
                    "from voc_vocab " +
                    "where name = 'Cell Line'";

    nav = sqlMgr.executeQuery(query2);
    nav.next();
    row = nav.getRowReference();
    vocCell = row.getInt(1);

    String query3 = "select _vocab_key " +
                    "from voc_vocab " +
                    "where name = 'Sequence Type'";

    nav = sqlMgr.executeQuery(query3);
    nav.next();
    row = nav.getRowReference();
    vocSeqType = row.getInt(1);

    String query4 = "select _translationType_key " +
                    "from mgi_translationType " +
                    "where translationType = 'Gender'";

    nav = sqlMgr.executeQuery(query4);
    nav.next();
    row = nav.getRowReference();
    transGender = row.getInt(1);

    String query5 = "select _translationType_key " +
                    "from mgi_translationType " +
                    "where translationType = 'Cell Line'";

    nav = sqlMgr.executeQuery(query5);
    nav.next();
    row = nav.getRowReference();
    transCell = row.getInt(1);

    String query6 = "select _translationType_key " +
                    "from mgi_translationType " +
                    "where translationType = 'Sequence Types'";

    nav = sqlMgr.executeQuery(query6);
    nav.next();
    row = nav.getRowReference();
    transSeqType = row.getInt(1);

    doDeletes();
    doInserts();
  }

  protected void tearDown() throws Exception {
    doDeletes();
    seqKeyLookup = null;
    genderKeyLookup = null;
    sqlMgr = null;
    vocGender = null;
    vocCell = null;
    transGender = null;
    transCell = null;
    vocSeqType = null;
    transSeqType = null;
    super.tearDown();
  }

  public void testGenderLookup() throws Exception {
    assertEquals(new Integer(-50), genderKeyLookup.lookup("she"));
    assertEquals(new Integer(-50), genderKeyLookup.lookup("Female"));
  }

  public void testCellLineLookup() throws Exception {
    assertEquals(new Integer(-60), cellLineKeyLookup.lookup("B-cell"));
    assertEquals(new Integer(-60), cellLineKeyLookup.lookup("Not to B-cell"));
  }


  public void testSeqLookup() throws Exception {
    assertEquals(new Integer(-70),
                 seqKeyLookup.lookup("Deoxyribonucleic Acid"));
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

  private void doDeletes() throws Exception
  {
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
  }

  private void doInserts() throws Exception
  {
    sqlMgr.executeUpdate(
        "insert into voc_term values (-50, " + vocGender + ", 'Female', " +
        "'F', 1, 0, 1200, 1200, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into voc_term values (-60, " + vocCell + ", 'B-cell', " +
        "null, 113, 0, 1200, 1200, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into mgi_translation values (-90, " + transGender +
        ", -50, 'she', 1, 1200, 1200, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into mgi_translation values (-100, " + transCell + ", -60, " +
        "'Not to B-cell', 1, 1200, 1200, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into voc_term values (-70,  " + vocSeqType + ", 'DNA', " +
        "'D', 1, 0, 1200, 1200, getDate(), getDate())"
        );
    sqlMgr.executeUpdate(
        "insert into mgi_translation values (-110, " + transSeqType +
        ", -70, 'Deoxyribonucleic Acid', 1, 1200, 1200, getDate(), getDate())"
        );
  }



}
