package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.shr.exception.ExceptionFactory;
import org.jax.mgi.dbs.mgd.MGD;

/**
 * @is An ExceptionFactory.
 * @has a hashmap of predefined TranslationExceptions stored by a name key
 * @does looks up TranslationExceptions by name
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class TranslationExceptionFactory extends ExceptionFactory {

  /**
   * Failed during lookup
   */
  public static final String LookupErr =
      "org.jax.mgi.dbs.mgd.trans.LookupErr";
  static {
    exceptionsMap.put(LookupErr, new TranslationException(
        "Failed during a lookup of term ??", false));
  }

  /**
   * Could not find given translation type key
   */
  public static final String NoTransTypeKey =
      "org.jax.mgi.dbs.mgd.trans.NoTransTypeKey";
  static {
    exceptionsMap.put(NoTransTypeKey, new TranslationException(
        "Could not find record in " + MGD.mgi_translationtype._name + " " +
        "for translationType key of ??", false));
  }

  /**
   * Could not find given translation type name
   */
  public static final String NoTransType =
      "org.jax.mgi.dbs.mgd.trans.NoTransType";
  static {
    exceptionsMap.put(NoTransType, new TranslationException(
        "Could not find record in " + MGD.mgi_translationtype._name + " " +
        "for translationType name of ??", false));
  }

  /**
   * Could not find given mgi type
   */
  public static final String NoMGIType =
      "org.jax.mgi.dbs.mgd.trans.NoMGIType";
  static {
    exceptionsMap.put(NoMGIType, new TranslationException(
        "Could not find record in " + MGD.acc_mgitype.name + " " +
        "for mgi type of ??", false));
  }



}
