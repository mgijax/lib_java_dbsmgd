package org.jax.mgi.dbs.mgd;

import java.sql.Timestamp;
import java.util.Date;

import org.jax.mgi.shr.config.BCPManagerCfg;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.types.Converter;
import org.jax.mgi.shr.dbutils.BCPException;
import org.jax.mgi.shr.dbutils.BCPExceptionFactory;
import org.jax.mgi.shr.dbutils.RecordStamp;

/**
 * @is a RecordStamp object for tables in the MGD database which
 * contain the fields createdBy, modifiedBy, creation_date,
 * modification_date
 * @has a BCPManagerCfg for looking up the values of MODIFIED_BY and
 * CREATED_BY
 * @does creates a string containing the fields required for record
 * stamping each seperated by a designated delimiter
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class RecordStamp_MGDOrg implements RecordStamp
{

  /**
   * return the string which represents the fields createdBy,
   * modifiedBy, creation_date and modification_date seperated by the
   * given delimiter
   * @param delimiter the delimiter to use to sepearte the stamp fields
   * @return the record stamp string
   */
  public String getStamp(String delimiter) throws BCPException
  {
    BCPManagerCfg cfgReader = null;
    String createdBy = null;
    String modifiedBy = null;
    try
    {
      cfgReader = new BCPManagerCfg();
      createdBy = cfgReader.getCreatedBy();
      modifiedBy = cfgReader.getModifiedBy();
    }
    catch (ConfigException e)
    {
      BCPExceptionFactory eFactory = new BCPExceptionFactory();
      BCPException e2 = (BCPException)
          eFactory.getException(BCPExceptionFactory.UserNotFound);
      e2.bind(createdBy);
      throw e2;
    }
    String timestamp =
        Converter.toString(new Timestamp(new Date().getTime()));
    String stamp = new String(delimiter + createdBy + delimiter + modifiedBy +
       delimiter + timestamp + delimiter + timestamp);
    return stamp;
  }

  /**
   * get the number of fields used for record stamping
   * @return the number of fields used for record stamping
   */
  public int getStampFieldCount()
  {
    return 4;
  }

}
