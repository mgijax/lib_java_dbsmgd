package org.jax.mgi.shr.datafactory;

import java.util.Map;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.timing.TimeStamper;
import org.jax.mgi.shr.dbutils.DBException;

public abstract class AbstractDataFactory
{
    protected TimeStamper timer = null;
    protected Logger logger = null;

    // constructors should take three arguments:  DataFactoryCfg,
    //    SQLDataManager, and Logger

    public abstract DTO getFullInfo (Map parms) throws DBException;

    public void setTimeStamper (TimeStamper timer)
    {
        this.timer = timer;
	this.timeStamp ("TimeStamper added to factory");
	return;
    }

    protected void timeStamp (String entry)
    {
        if ((this.timer != null) && (entry != null))
	{
	    this.timer.record (entry);
	}
	return;
    }

    protected void logInfo (String entry)
    {
        if ((this.logger != null) && (entry != null))
	{
	    this.logger.logInfo (entry);
	}
	return;
    }
}
