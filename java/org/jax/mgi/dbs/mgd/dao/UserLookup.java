package org.jax.mgi.dbs.mgd.dao;

import org.jax.mgi.shr.cache.RowDataCacheHandler;
import org.jax.mgi.shr.cache.CacheException;
import org.jax.mgi.shr.cache.KeyValue;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.dbs.mgd.dao.User;
import org.jax.mgi.dbs.mgd.dao.UserInterpreter;


/**
 * @is a RowDataCacheHandler class for looking up userids stored within
 * the MGI_USER table
 * @has a cache for storing database data in memory, a set of sql queries
 * for accessing data in the database and a RowDataInterpreter for creating
 * KeyValue objects necessary for maintaining the cache from the query
 * results.
 * @does gets data from the database and caches it into memory and provides
 * lookup methods for accessing the data
 * @copyright: The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class UserLookup extends RowDataCacheHandler
{

  /**
   * the default constructor
   * @assumes nothing
   * @effects nothing
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file
   * @throws DBException thrown if there is an error accessing the database
   * @throws CacheException thrown if there is an error accessing the cache
   */
  public UserLookup() throws ConfigException, DBException, CacheException
  {
    super(RowDataCacheHandler.FULL_CACHE,
          SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD));
  }

  /**
   * look up a userid for the given user name from the MGI_USER table
   * @param name the name to lookup
   * @assumes nothing
   * @effects nothing
   * @return the userid
   */
  public User lookupByName(String name) throws CacheException, DBException
  {
    return (User)this.cacheStrategy.lookup(name, this.cache);
  }

  /**
   * get the query to partially initialize the cache
   * @assumes nothing
   * @effects nothing
   * @return the query to partially initialize the cache
   */
  public String getPartialInitQuery()
  {
    String message = "Class " + this.getClass().getName() + " does not " +
        "support the method getPartialInitQuery";
    throw new java.lang.UnsupportedOperationException(message);
  }

  /**
   * get the query to fully initialize the cache
   * @assumes nothing
   * @effects nothing
   * @return the query to fully initialize the cache
   */
  public String getFullInitQuery()
  {
    return new String("SELECT * FROM MGI_USER");
  }

  /**
   * get the query to add to cache
   * @param addObject the target obect
   * @return the sql query
   */
  public String getAddQuery(Object addObject)
  {
    String message = "Class " + this.getClass().getName() + " does not " +
        "support the method getAddQuery";
    throw new java.lang.UnsupportedOperationException(message);
  }


  /**
   * get a RowDataInterpreter for creating
   * @return the RowDataInterpreter
   */
  public RowDataInterpreter getRowDataInterpreter()
  {
    return new InnerInterpreter();
  }


  private class InnerInterpreter implements RowDataInterpreter
  {
    private RowDataInterpreter userInterpreter = new UserInterpreter();

    public java.lang.Object interpret(RowReference rowReference)
        throws DBException
    {
      User user = (User)userInterpreter.interpret(rowReference);
      KeyValue keyValue = new KeyValue(user.getState().getName(), user);
      return keyValue;
    }
  }

}