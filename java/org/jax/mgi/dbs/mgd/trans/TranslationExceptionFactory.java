package org.jax.mgi.dbs.mgd.trans;

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
  public static final String NoTransTypeName =
      "org.jax.mgi.dbs.mgd.trans.NoTransTypeName";
  static {
    exceptionsMap.put(NoTransTypeName, new TranslationException(
        "Could not find record in " + MGD.mgi_translationtype._name + " " +
        "for translationType name of ??", false));
  }


}
