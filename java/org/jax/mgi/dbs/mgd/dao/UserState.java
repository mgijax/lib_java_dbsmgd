package org.jax.mgi.dbs.mgd.dao;

/**
 * @is a class for holding attributes for a record in the MGI_User table
 * @has data attributes that are required for representing a record in the
 * MGI_User table
 * @does provides accessor methods for its data attributes
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class UserState
{
  public int userTypeKey;
  public int userStatusKey;
  public String login;
  public String name;

  /**
   * get the user type key
   * @return the user type key
   */
  public int getUserTypeKey()
  {
    return userTypeKey;
  }

  /**
   * get the user status key
   * @return the user status key
   */
  public int getUserStatusKey()
  {
    return userStatusKey;
  }

  /**
   * get the login
   * @return the login
   */
  public String getLogin()
  {
    return login;
  }

  /**
   * get the name
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * set the user type key
   * @param userTypeKey the user type key
   */
  public void setUserTypeKey(int userTypeKey)
  {
    this.userTypeKey = userTypeKey;
  }

  /**
   * set the user status key
   * @param userStatusKey the user status key
   */
  public void setUserStatusKey(int userStatusKey)
  {
    this.userStatusKey = userStatusKey;
  }

  /**
   * set the login
   * @param login the login
   */
  public void setLogin(String login)
  {
    this.login = login;
  }

  /**
   * set the name
   * @param name the name
   */
  public void setName(String name)
  {
    this.name = name;
  }
}