package org.jax.mgi.dbs.mgd.lookup;

import org.jax.mgi.dbs.mgd.VocabularyTypeConstants;
import org.jax.mgi.shr.cache.CacheConstants;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.config.ConfigException;

/**
 * @is: a VocabKeyLookup for the Cell Line vocabulary
 * @has: a Translator for translating incoming  cell line terms before
 * lookup.
 * @does: provides a lookup method for cell line terms stored within a cache
 * and provides translation of incoming terms.
 * @company: The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */


public class CellLineLookup extends VocabKeyLookup {
  public CellLineLookup()
      throws CacheException, DBException,
             ConfigException,
             TranslationException

  {
    super(VocabularyTypeConstants.CELLLINE, CacheConstants.FULL_CACHE,
          CacheConstants.FULL_CACHE);
  }

}