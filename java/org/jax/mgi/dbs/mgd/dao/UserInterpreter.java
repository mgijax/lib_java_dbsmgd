package org.jax.mgi.dbs.mgd.dao;

import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.DBException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UserInterpreter implements RowDataInterpreter
{

  public Object interpret(RowReference rowReference) throws DBException
  {
    UserKey key = new UserKey(rowReference.getInt("_User_key"));
    UserState state = new UserState();
    state.setUserStatusKey(rowReference.getInt("_UserStatus_key"));
    state.setUserTypeKey(rowReference.getInt("_UserType_key"));
    state.setLogin(rowReference.getString("login"));
    state.setName(rowReference.getString("name"));
    UserDAO user = new UserDAO(key, state);
    return user;
  }

}