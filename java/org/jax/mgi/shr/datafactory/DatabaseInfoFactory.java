package org.jax.mgi.shr.datafactory;

import java.util.Map;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dto.DTOConstants;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.cache.ExpiringObjectCache;

public class DatabaseInfoFactory extends AbstractDataFactory
{
    private DataFactoryCfg config = null;
    private SQLDataManager sqlDM = null;

    public DatabaseInfoFactory (DataFactoryCfg config, SQLDataManager sqlDM,
                Logger logger)
    {
        this.config = config;
	this.sqlDM = sqlDM;
	this.logger = logger;
	return;
    }

    public DTO getFullInfo (Map parms) throws DBException
    {
        DTO data = null;
	String cacheKey = "DatabaseInfoFactory." + this.sqlDM.getServer() +
	    "." + this.sqlDM.getDatabase();
	ExpiringObjectCache cache = ExpiringObjectCache.getSharedCache();

	data = (DTO) cache.get(cacheKey);
	if (data != null)
	{
	    this.timeStamp ("Retrieved database date and version from cache");
	    DTO newDTO = DTO.getDTO();
	    newDTO.merge (data);
	    return newDTO;
	}

	ResultsNavigator nav = null;
	RowReference rr = null;
        data = DTO.getDTO();

	nav = this.sqlDM.executeQuery (DB_INFO);
	if (nav.next())
	{
	    rr = (RowReference) nav.getCurrent();
	    data.set (DTOConstants.DatabaseDate, rr.getString(1));
	    data.set (DTOConstants.DatabaseVersion, rr.getString(2));
	}
	cache.put (cacheKey, (DTO) data.clone(), 5 * 60);
	nav.close();

	this.timeStamp ("Retrieved database date and version from db");

	return data;
    }

    private static String DB_INFO = 
        "SELECT CONVERT(varchar, lastdump_date, 101), public_version "
	+ " FROM MGI_dbInfo";
}
