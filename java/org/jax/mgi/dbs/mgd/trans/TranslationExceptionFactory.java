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
   * Could not find given translation type
   */
  public static final String BadTranslationType =
      "org.jax.mgi.dbs.mgd.trans.BadTranslationType";
  static {
    exceptionsMap.put(BadTranslationType, new TranslationException(
        "Could not find record in " + MGD.mgi_translationtype._name + " " +
        "for translationType key of ??", false));
  }

}
