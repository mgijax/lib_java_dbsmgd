package org.jax.mgi.shr.datafactory;

import java.util.Map;
import java.io.IOException;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dbutils.DBException;


public interface SummaryReportFactory {

	public DTO getResults(Map QueryParameters) throws DBException, IOException;
}