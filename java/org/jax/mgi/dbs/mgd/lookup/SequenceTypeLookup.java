package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.dbs.mgd.VocabularyTypeConstants;
import org.jax.mgi.shr.cache.CacheConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.config.ConfigException;

/**
 * @is: a VocabKeyLookup for the Sequence Type vocabulary
 * @has: a Translator for translating incoming sequence type terms before
 * lookup.
 * @does: provides a lookup method for sequence type terms stored within a cache
 * and provides translation of incoming terms.
 * @company: The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */


public class SequenceTypeLookup extends VocabKeyLookup {
  public SequenceTypeLookup()
      throws CacheException, DBException,
             ConfigException,
             TranslationException

  {
    super(VocabularyTypeConstants.SEQUENCETYPE, CacheConstants.FULL_CACHE,
          CacheConstants.FULL_CACHE);
  }

}
