//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;

/**
 * @is An object that knows how create a PRB_SourceDAO object from a row
 *     of data from the PRB_Source table.
 * @has Nothing
 * @does
 *   <UL>
 *   <LI> Provides a method to interpret a row of data.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class PRB_SourceInterpreter implements RowDataInterpreter
{
    /**
    * Interprets a row of data from the PRB_Source table to create a
    * PRB_SourceDAO object.
    * @assumes Nothing
    * @effects Nothing
    * @param row A row of data.
    * @return A PRB_SourceDAO object that contains a row of data.
    * @throws DBException
    */
    public Object interpret (RowReference row)
        throws DBException
    {
        PRB_SourceKey probeSrcKey =
            new PRB_SourceKey(row.getInt("_Source_key"));
        PRB_SourceState probeSrcState = new PRB_SourceState();
        probeSrcState.setSegmentTypeKey(row.getInt("_SegmentType_key"));
        probeSrcState.setVectorKey(row.getInt("_Vector_key"));
        probeSrcState.setProbeSpeciesKey(row.getInt("_ProbeSpecies_key"));
        probeSrcState.setStrainKey(row.getInt("_Strain_key"));
        probeSrcState.setTissueKey(row.getInt("_Tissue_key"));
        probeSrcState.setRefsKey(row.getInt("_Refs_key"));
        probeSrcState.setName(row.getString("name"));
        probeSrcState.setDescription(row.getString("description"));
        probeSrcState.setAge(row.getString("age"));
        probeSrcState.setAgeMin(row.getFloat("ageMin"));
        probeSrcState.setAgeMax(row.getFloat("ageMax"));
        probeSrcState.setAge(row.getString("sex"));
        probeSrcState.setAge(row.getString("cellLine"));
        PRB_SourceDAO probeSrcDAO =
            new PRB_SourceDAO(probeSrcKey, probeSrcState);
        return probeSrcDAO;
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
