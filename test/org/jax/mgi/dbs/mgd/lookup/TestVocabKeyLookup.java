package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.mgd.trans.*;
import org.jax.mgi.shr.config.*;

public class TestVocabKeyLookup
    extends TestCase {
  private VocabKeyLookup vocabKeyLookup = null;

  public TestVocabKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    vocabKeyLookup = new VocabKeyLookup();
  }

  protected void tearDown() throws Exception {
    vocabKeyLookup = null;
    super.tearDown();
  }

  public void testLookup() throws CacheException, DBException,
      TranslationException, ConfigException {
    String term = "Female";
    Integer expectedReturn = new Integer(64555);
    Integer actualReturn = vocabKeyLookup.lookup(term);
    assertEquals("return value", expectedReturn, actualReturn);
  }

}
