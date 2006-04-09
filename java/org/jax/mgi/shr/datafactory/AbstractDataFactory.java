package org.jax.mgi.shr.datafactory;

import java.util.Map;
import java.io.IOException;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.timing.TimeStamper;
import org.jax.mgi.shr.dbutils.DBException;

public abstract class AbstractDataFactory
{
    protected TimeStamper timer = null;
    protected Logger logger = null;
    protected String snpDatabaseName = null;

    // constructors should take three arguments:  DataFactoryCfg,
    //    SQLDataManager, and Logger

    public abstract DTO getFullInfo (Map parms) throws DBException,
	IOException;

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

    public void setSnpDatabaseName (String snpDb)
    {
        this.snpDatabaseName = snpDb;
	this.timeStamp ("Set SNP db = " + snpDb);
    }
}
