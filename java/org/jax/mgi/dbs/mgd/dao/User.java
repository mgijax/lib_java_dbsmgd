package org.jax.mgi.dbs.mgd.dao;

import java.util.Vector;
import org.jax.mgi.shr.dbutils.dao.DataInstance;
import org.jax.mgi.shr.dbutils.Table;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.exception.ExceptionFactory;

/**
 * @is a DataInstance which represents records from the MGI_USER table
 * @has a UserState and a UserKey
 * @does provides SQLTranslatable and BCPTranslatable implementations for
 * the MGI_USER table
 */

public class User extends DataInstance
{
  private UserKey userKey = null;
  private UserState userState = null;

  /*
   * the following constant definitions are exceptions thrown by this class
   */
  private static String MethodNotSupported =
      ExceptionFactory.MethodNotSupported;

  /**
   * the constructor which sets both the UserState and UserKey
   * @param userKey the UserKey
   * @param userState the UserState
   */
  public User(UserKey userKey, UserState userState)
  {
    this.userKey = userKey;
    this.userState = userState;
  }

  /**
   * the constructor which accepts a UserState and generates a new UserKey
   * @param userState the UserState
   */
  public User(UserState userState) throws DBException, ConfigException
  {
    this.userKey = new UserKey();
    this.userState = userState;
  }

  /**
   * get the UserKey
   * @return the UserKey
   * @assumes nothing
   * @effects nothing
   */
  public UserKey getKey()
  {
    return userKey;
  }

  /**
   * get the UserState
   * @return the UserState
   * @assumes nothing
   * @effects nothing
   */
  public UserState getState()
  {
    return userState;
  }

  /**
   * get the sql for inserting a record into the MGI_User table
   * @return the sql string for inserting a record into the MGI_User table
   * @assumes nothing
   * @effects nothing
   */
  public String getInsertSQL()
  {
    String message = "Class " + this.getClass().getName() + " does not " +
    "support the method getInsertSQL";
    throw new java.lang.UnsupportedOperationException(message);
  }

  /**
   * get the sql for deleting a record from the MGI_User table
   * @return the sql for deleting a record from the MGI_User table
   * @assumes nothing
   * @effects nothing
   */
  public String getDeleteSQL()
  {
    String message = "Class " + this.getClass().getName() + " does not " +
    "support the method getDeleteSQL";
    throw new java.lang.UnsupportedOperationException(message);
  }

  /**
   * get the sql for updating a record in the MGI_User table
   * @assumes nothing
   * @effects nothing
   * @return the sql for updating a record in the MGI_User table
   */
  public String getUpdateSQL()
  {
    String message = "Class " + this.getClass().getName() + " does not " +
    "support the method getUpdateSQL";
    throw new java.lang.UnsupportedOperationException(message);
  }

  /**
   * get the list of table names that this object will write to for bcping
   * @assumes nothing
   * @effects nothing
   * @return the list of table names that this object will write to for bcping
   */
  public Vector getBCPSupportedTables()
  {
    String message = "Class " + this.getClass().getName() + " does not " +
    "support the method getBCPSupportedTables";
    throw new java.lang.UnsupportedOperationException(message);
  }

  /**
   * get the vector of values for writing to the given table using bcp
   * @param table the Table class which is to be written to
   * @assumes nothing
   * @effects nothing
   * @return the vector of values for writing to the given table using bcp
   */
  public Vector getBCPVector(Table table)
  {
    String message = "Class " + this.getClass().getName() + " does not " +
    "support the method getBCPVector";
    throw new java.lang.UnsupportedOperationException(message);
  }


}