package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.dbs.mgd.VocabularyTypeConstants;
import org.jax.mgi.shr.cache.CacheConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.config.ConfigException;

/**
 * @is a VocabKeyLookup for the Sequence Provider vocabulary
 * @has a Translator for translating incoming sequence provider terms before
 * lookup.
 * @does provides a lookup method for sequence provider terms stored within a
 * cache and provides translation of incoming terms.
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */


public class SequenceProviderKeyLookup extends VocabKeyLookup {
  public SequenceProviderKeyLookup()
      throws CacheException, DBException,
             ConfigException,
             TranslationException

  {
    super(VocabularyTypeConstants.SEQUENCEPROVIDER, CacheConstants.FULL_CACHE,
          CacheConstants.FULL_CACHE);
  }

}
