package org.jax.mgi.dbs.mgd.trans;

import org.jax.mgi.shr.exception.MGIException;

/**
 * @is An MGIException which represents an error in translation
 * @has an exception message, a data related indicator and a parent
 * exception which can be null.
 * @does nothing
 * @author M Walker
 * @version 1.0
 */

public class TranslationException extends MGIException {
  public TranslationException(String pMessage, boolean pDataRelated) {
    super(pMessage, pDataRelated);
  }


}
