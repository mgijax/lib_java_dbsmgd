package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.dbs.mgd.VocabularyTypeConstants;
import org.jax.mgi.shr.cache.CacheConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.config.ConfigException;

/**
 * @is: a VocabKeyLookup for the Vector Type vocabulary
 * @has: a Translator for translating incoming vector type terms before
 * lookup.
 * @does: provides a lookup method for vector type terms stored within a cache
 * and provides translation of incoming terms.
 * @company: The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */


public class VectorKeyLookup extends VocabKeyLookup {
  public VectorKeyLookup()
      throws CacheException, DBException,
             ConfigException,
             TranslationException

  {
    super(VocabularyTypeConstants.VECTORTYPE, CacheConstants.FULL_CACHE,
          CacheConstants.FULL_CACHE);
  }

}
