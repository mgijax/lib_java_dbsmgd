package org.jax.mgi.dbs.mgd.lookup;

import junit.framework.*;
import org.jax.mgi.shr.cache.*;
import org.jax.mgi.shr.dbutils.*;
import org.jax.mgi.dbs.mgd.trans.*;
import org.jax.mgi.shr.config.*;

public class TestStrainKeyLookup
    extends TestCase {

  private StrainKeyLookup strainKeyLookup = null;

  public TestStrainKeyLookup(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    strainKeyLookup = new StrainKeyLookup();
  }

  protected void tearDown() throws Exception {
    strainKeyLookup = null;
    super.tearDown();
  }

  public void testLookup() throws CacheException, DBException,
      TranslationException, ConfigException {
    String term = "BALB/cJ";
    Integer expectedReturn = new Integer(3);
    Integer actualReturn = strainKeyLookup.lookup(term);
    assertEquals("return value", expectedReturn, actualReturn);
  }

  public void testTranslatedLookup() throws CacheException, DBException,
      TranslationException, ConfigException {
    String term = "BALB/c/J";
    Integer expectedReturn = new Integer(3);
    Integer actualReturn = strainKeyLookup.lookup(term);
    assertEquals("return value", expectedReturn, actualReturn);
  }

}
