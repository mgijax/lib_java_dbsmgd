package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.shr.exception.MGIException;


/**
 * @is An MGIException which represents exceptions occuring during
 * the reading of configuration parameters.
 * @has an exception message, a data related indicator and a parent
 * exception which can be null.
 * @does nothing
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */


public class LookupException extends MGIException {
  public LookupException(String pMessage, boolean pDataRelated) {
    super(pMessage, pDataRelated);
  }


}
