//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

import java.util.Vector;

import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.dao.DAO;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.Table;
import org.jax.mgi.shr.exception.MGIException;

/**
 * @is An object that represents a record in the PRB_Probe table.
 * @has
 *   <UL>
 *   <LI> ProbeKey object
 *   <LI> ProbeState object
 *   </UL>
 * @does
 *   <UL>
 *   <LI> Provides get methods for its attribute(s).
 *   <LI> Provides BCPTranslatable and SQLTranslatable implementations.
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class ProbeDAO extends DAO
{
    /////////////////
    //  Variables  //
    /////////////////

    // ProbeKey object.
    //
    private ProbeKey probeKey = null;

    // ProbeState object.
    //
    private ProbeState probeState = null;


    /**
     * Constructs a new ProbeDAO object from a given ProbeState object and
     * a generated ProbeKey object.
     * @assumes Nothing
     * @effects Nothing
     * @param pState The ProbeState object.
     * @throws ConfigException
     * @throws DBException
     */
    public ProbeDAO (ProbeState pState)
        throws ConfigException, DBException
    {
        probeKey = new ProbeKey();
        probeState = pState;
    }


    /**
     * Constructs a new ProbeDAO object from given ProbeKey and ProbeState
     * objects.
     * @assumes Nothing
     * @effects Nothing
     * @param pKey The ProbeKey object.
     * @param pState The ProbeState object.
     * @throws Nothing
     */
    public ProbeDAO (ProbeKey pKey, ProbeState pState)
    {
        probeKey = pKey;
        probeState = pState;
    }


    /**
     * Get the probeKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The probeKey attribute.
     * @throws Nothing
     */
    public ProbeKey getProbeKey () { return probeKey; }


    /**
     * Get the probeState attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The probeState attribute.
     * @throws Nothing
     */
    public ProbeState getProbeState () { return probeState; }


    /**
     * Get a vector of table names that are supported by this class.
     * The BCPWriter will call the getBCPVector() method for each
     * of the tables that are provided by this method.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return A vector of table names support by this class.
     * @throws Nothing
     */
    public Vector getBCPSupportedTables()
    {
        // Create a new vector.
        //
        Vector v = new Vector();

        // Add each of the table names to the vector.
        //
        v.add("PRB_Probe");

        // Return the vector.
        //
        return v;
    }


    /**
     * Get a vector from this object that represents one row of data to be
     * written to the bcp file.  The order of the attributes in the vector
     * needs to correspond to the order of the columns in the given table.
     * @assumes Nothing
     * @effects Nothing
     * @param table The name of the database table that the vector of
     *        attributes is being targeted for.
     * @return A vector for the bcp record to be written.
     * @throws Nothing
     */
    public Vector getBCPVector (Table table)
    {
        // Create a new vector to hold the attributes for one record of a
        // bcp file.
        //
        Vector v = new Vector();

        // Populate the vector with attributes that are needed to create a
        // bcp record for the PRB_Probe table.
        //
        if (table.getName().equals("PRB_Probe"))
        {
            // Add the attributes to the vector.
            //
            v.add(probeKey.getPrimaryKey());
            v.add(probeState.getName());
            v.add(probeState.getDerivedFrom());
            v.add(probeState.getSourceKey());
            v.add(probeState.getVectorKey());
            v.add(probeState.getSegmentTypeKey());
            v.add(probeState.getPrimerSequence1());
            v.add(probeState.getPrimerSequence2());
            v.add(probeState.getRegionCovered());
            v.add(probeState.getRegionCovered2());
            v.add(probeState.getInsertSite());
            v.add(probeState.getInsertSize());
            v.add(probeState.getRepeatUnit());
            v.add(probeState.getProductSize());
            v.add(probeState.getMoreProduct());
        }

        // Return the vector.
        //
        return v;
    }


    /**
     * Build the SQL statements needed to insert records into the database
     * for this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return A string representing the SQL statement.
     * @throws MGIException
     */
    public String getInsertSQL()
    {
        throw MGIException.getUnsupportedMethodException();
    }


    /**
     * Build the SQL statements needed to update records in the database
     * for this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return A string representing the SQL statement.
     * @throws MGIException
     */
    public String getUpdateSQL()
    {
        throw MGIException.getUnsupportedMethodException();
    }


    /**
     * Build the SQL statements needed to delete records from the database
     * for this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return A string representing the SQL statement.
     * @throws MGIException
     */
    public String getDeleteSQL()
    {
        throw MGIException.getUnsupportedMethodException();
    }
}


//  $Log$
//  Revision 1.4  2003/09/30 16:58:07  dbm
//  Use Integer instead of int for attributes
//
//  Revision 1.3  2003/09/24 15:12:56  dbm
//  Changed DataInstance to DAO
//
//  Revision 1.2  2003/09/23 13:23:52  dbm
//  Continued development
//
//  Revision 1.1  2003/09/19 17:43:20  dbm
//  Initial version
//
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
