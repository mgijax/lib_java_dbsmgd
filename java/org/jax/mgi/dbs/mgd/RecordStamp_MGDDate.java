package org.jax.mgi.dbs.mgd;

import java.sql.Timestamp;
import java.util.Date;

import org.jax.mgi.shr.types.Converter;
import org.jax.mgi.shr.dbutils.RecordStamp;

/**
 * @is a RecordStamp object for tables in the MGD database which
 * contain the fields creation_date, modification_date
 * @has nothing
 * @does creates a string containing the fields required for record
 * stamping each seperated by a designated delimiter
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class RecordStamp_MGDDate implements RecordStamp
{

  /**
   * return the string which represents the fields creation_date and
   * modification_date seperated by the given delimiter
   * @param delimiter the delimiter to use to sepearte the stamp fields
   * @return the record stamp string
   */
  public String getStamp(String delimiter)
  {
    String timestamp =
        Converter.toString(new Timestamp(new Date().getTime()));
    return new String(delimiter + timestamp + delimiter + timestamp);
  }

  /**
   * get the number of fields used for record stamping
   * @return the number of fields used for record stamping
   */
  public int getStampFieldCount()
  {
    return 2;
  }


}
