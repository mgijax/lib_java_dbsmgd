//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.RowDataInterpreter;
import org.jax.mgi.shr.dbutils.RowReference;

/**
 * @is An object that knows how create a ProbeSourceState object from a row
 *     of data from the PRB_Source table.
 * @has Nothing
 * @does
 *   <UL>
 *   <LI> Provides a method to interpret a row of source data.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class ProbeSourceInterpreter implements RowDataInterpreter
{
    /**
    * Interprets a row of source data to create a ProbeSourceState object.
    * @assumes Nothing
    * @effects Nothing
    * @param row A row of source data.
    * @return A ProbeSourceState object that contains a row of source data.
    * @throws DBException
    */
    public Object interpret (RowReference row)
        throws DBException
    {
        ProbeSourceKey probeSrcKey =
            new ProbeSourceKey(row.getInt("_Source_key"));
        ProbeSourceState probeSrcState = new ProbeSourceState();
        probeSrcState.setSegmentTypeKey(new Integer(row.getInt("_SegmentType_key")));
        probeSrcState.setVectorKey(new Integer(row.getInt("_Vector_key")));
        probeSrcState.setOrganismKey(new Integer(row.getInt("_Organism_key")));
        probeSrcState.setStrainKey(new Integer(row.getInt("_Strain_key")));
        probeSrcState.setTissueKey(new Integer(row.getInt("_Tissue_key")));
        probeSrcState.setGenderKey(new Integer(row.getInt("_Gender_key")));
        probeSrcState.setCellLineKey(new Integer(row.getInt("_CellLine_key")));
        probeSrcState.setRefsKey(new Integer(row.getInt("_Refs_key")));
        probeSrcState.setName(row.getString("name"));
        probeSrcState.setDescription(row.getString("description"));
        probeSrcState.setAge(row.getString("age"));
        probeSrcState.setAgeMin(new Double(row.getFloat("ageMin")));
        probeSrcState.setAgeMax(new Double(row.getFloat("ageMax")));
        probeSrcState.setIsCuratorEdited(new Integer(row.getInt("isCuratorEdited")));
        ProbeSourceDAO probeSrcDAO = new ProbeSourceDAO(probeSrcKey, probeSrcState);
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
