package org.jax.mgi.dbs.mgd.dao;

import org.jax.mgi.shr.dbutils.Table;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.SQLDataManagerFactory;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.config.ConfigException;

/**
 * @is a key class for the User DataInstance
 * @has a int key value
 * @does can get the next key value when constructing a new instance and
 * provides an accessor method for the key value
 * @company The Jackson Laboratory
 * @author not attributable
 * @version 1.0
 */

public class UserKey
{
  /**
   * the key value
   */
  private int key;

  /**
   * the default constructor which will obtain the next key value
   * automatically
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file when obtaining database connection parameters
   * @throws DBException thrown if there is an error accessing the database
   */
  public UserKey() throws ConfigException, DBException
  {
    SQLDataManager sqlMgr =
        SQLDataManagerFactory.getShared(SQLDataManagerFactory.MGD);
    Table userTable =
        new Table("MGI_USER", sqlMgr);
    int key = userTable.getNextKey();
    this.key = key;
  }

  /**
   * the constructor which accepts the key value
   * @param key the key value
   */
  public UserKey(int key)
  {
    this.key = key;
  }

  /**
   * get the key value
   * @return the key value
   */
  public int getKey()
  {
    return key;
  }

}
