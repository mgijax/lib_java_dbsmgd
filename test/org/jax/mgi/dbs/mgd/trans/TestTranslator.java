package org.jax.mgi.dbs.mgd.trans;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.*;
import org.jax.mgi.dbs.mgd.*;

public class TestTranslator
    extends TestCase {
  private Translator translator = null;

  public TestTranslator(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    translator = new Translator(MGD.translationTypes.Gender,
                                CacheConstants.FULL_CACHE);
  }

  protected void tearDown() throws Exception {
    translator = null;
    super.tearDown();
  }

  public void testTranslate() throws Exception {
    String term = "f";
    KeyedDataAttribute actualReturn = translator.translate(term);
    assertEquals("Female", actualReturn.getValue());
    assertEquals(64555, actualReturn.getKey().intValue());
  }

  public void testStringConstructor() throws Exception {
    Translator translator2 = new Translator("Gender",
                                            CacheConstants.FULL_CACHE);
    String term = "f";
    KeyedDataAttribute actualReturn = translator.translate(term);
    assertEquals("Female", actualReturn.getValue());
    assertEquals(64555, actualReturn.getKey().intValue());
  }


}
