package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.mgd.trans.*;
import org.jax.mgi.shr.config.*;

public class TestOrganismKeyLookup
    extends TestCase {
  private OrganismKeyLookup organismKeyLookup = null;

  public TestOrganismKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    organismKeyLookup = new OrganismKeyLookup();
  }

  protected void tearDown() throws Exception {
    organismKeyLookup = null;
    super.tearDown();
  }

  public void testLookup() throws CacheException, DBException,
      TranslationException, ConfigException {
    String term = "human";
    Integer expectedReturn = new Integer(2);
    Integer actualReturn = organismKeyLookup.lookup(term);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testTranslatedLookup() throws CacheException, DBException,
      TranslationException, ConfigException {
    String term = "Mus musculus";
    Integer expectedReturn = new Integer(1);
    Integer actualReturn = organismKeyLookup.lookup(term);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testCachedTranslatedTerm() throws CacheException, DBException,
      TranslationException, ConfigException {
    String term = "Mus musculus";
    String expectedReturn = "mouse, laboratory";
    organismKeyLookup.lookup(term);
    String actualReturn = organismKeyLookup.getTranslatedTerm();
    assertEquals("return value", expectedReturn, actualReturn);
  }



}
