package org.jax.mgi.dbs.mgd.lookup;


import org.jax.mgi.shr.exception.ExceptionFactory;

/**
 * @is An ExceptionFactory for database lookup exceptions.
 * @has a hashmap of predefined LookupExceptions stored by a name key
 * @does looks up and returns a LookupExceptions by name
 * @author M Walker
 * @version 1.0
 */

public class LookupExceptionFactory extends ExceptionFactory {

  /**
   * a key was not found on lookup
   */
  public static final String KeyNotFound =
      "org.jax.mgi.shr.cache.KeyNotFound";
  static {
    exceptionsMap.put(KeyNotFound, new LookupException(
      "The key ?? was not found on a call to lookup " +
      "on class name ??", false));
  }

  /**
   * a lookup failed due to a database or cache exception
   */
  public static final String ResourceErr =
      "org.jax.mgi.shr.cache.ResourceErr";
  static {
    exceptionsMap.put(ResourceErr, new LookupException(
      "The lookup failed due to a nested exception within the " +
      "database resource or the caching resource", false));
  }

}
