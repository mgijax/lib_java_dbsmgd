//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;

/**
 * @is An object that knows how create a PRB_ReferenceDAO object from a row
 *     of data from the PRB_Reference table.
 * @has Nothing
 * @does
 *   <UL>
 *   <LI> Provides a method to interpret a row of data.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class PRB_ReferenceInterpreter implements RowDataInterpreter
{
    /**
    * Interprets a row of data from the PRB_Reference table to create a
    * PRB_ReferenceDAO object.
    * @assumes Nothing
    * @effects Nothing
    * @param row A row of data.
    * @return A PRB_ReferenceDAO object that contains a row of data.
    * @throws DBException
    */
    public Object interpret (RowReference row)
        throws DBException
    {
        PRB_ReferenceKey probeRefKey =
            new PRB_ReferenceKey(row.getInt("_Reference_key"));
        PRB_ReferenceState probeRefState = new PRB_ReferenceState();
        probeRefState.setProbeKey(row.getInt("_Probe_key"));
        probeRefState.setRefsKey(row.getInt("_Refs_key"));
        probeRefState.setHolder(row.getString("holder"));
        probeRefState.setHasRmap(row.getBoolean("hasRmap"));
        probeRefState.setHasSequence(row.getBoolean("hasSequence"));
        PRB_ReferenceDAO probeRefDAO =
            new PRB_ReferenceDAO(probeRefKey, probeRefState);
        return probeRefDAO;
    }
}


//  $Log$
//
/**************************************************************************
*
* Warranty Disclaimer and Copyright Notice
*
*  THE JACKSON LABORATORY MAKES NO REPRESENTATION ABOUT THE SUITABILITY OR
*  ACCURACY OF THIS SOFTWARE OR DATA FOR ANY PURPOSE, AND MAKES NO WARRANTIES,
*  EITHER EXPRESS OR IMPLIED, INCLUDING MERCHANTABILITY AND FITNESS FOR A
*  PARTICULAR PURPOSE OR THAT THE USE OF THIS SOFTWARE OR DATA WILL NOT
*  INFRINGE ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS, OR OTHER RIGHTS.
*  THE SOFTWARE AND DATA ARE PROVIDED "AS IS".
*
*  This software and data are provided to enhance knowledge and encourage
*  progress in the scientific community and are to be used only for research
*  and educational purposes.  Any reproduction or use for commercial purpose
*  is prohibited without the prior express written permission of The Jackson
*  Laboratory.
*
* Copyright \251 1996, 1999, 2002, 2003 by The Jackson Laboratory
*
* All Rights Reserved
*
**************************************************************************/
