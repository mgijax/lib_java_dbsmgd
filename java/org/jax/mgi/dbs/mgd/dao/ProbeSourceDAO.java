//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

import java.util.Vector;

import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.dao.DataInstance;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.Table;

/**
 * @is An object that represents a record in the PRB_Source table.
 * @has
 *   <UL>
 *   <LI> ProbeSourceKey object
 *   <LI> ProbeSourceState object
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

public class ProbeSourceDAO extends DataInstance
{
    /////////////////
    //  Variables  //
    /////////////////

    // ProbeSourceKey object.
    //
    private ProbeSourceKey probeSrcKey = null;

    // ProbeSourceState object.
    //
    private ProbeSourceState probeSrcState = null;


    /**
     * Constructs a new ProbeSourceDAO object from a given ProbeSourceState
     * object and a generated ProbeSourceKey object.
     * @assumes Nothing
     * @effects Nothing
     * @param pState The ProbeSourceState object
     * @throws Nothing
     */
    public ProbeSourceDAO (ProbeSourceState pState)
        throws ConfigException, DBException
    {
        probeSrcKey = new ProbeSourceKey();
        probeSrcState = pState;
    }


    /**
     * Constructs a new ProbeSourceDAO object from given ProbeSourceKey and
     * ProbeSourceState objects.
     * @assumes Nothing
     * @effects Nothing
     * @param pKey The ProbeSourceKey object
     * @param pState The ProbeSourceState object
     * @throws Nothing
     */
    public ProbeSourceDAO (ProbeSourceKey pKey, ProbeSourceState pState)
    {
        probeSrcKey = pKey;
        probeSrcState = pState;
    }


    /**
     * Get the probeSrcKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The probeSrcKey attribute
     * @throws Nothing
     */
    public ProbeSourceKey getProbeSrcKey () { return probeSrcKey; }


    /**
     * Get the probeSrcState attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The probeSrcState attribute
     * @throws Nothing
     */
    public ProbeSourceState getProbeSrcState () { return probeSrcState; }


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
        v.add("PRB_Source");

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
     *        attributes is being targeted for
     * @return A vector for the bcp record to be written
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
        if (table.getName().equals("PRB_Source"))
        {
            // Add the attributes to the vector.
            //
            v.add(new Integer(probeSrcKey.getPrimaryKey()));
            v.add(probeSrcState.getSegmentTypeKey());
            v.add(probeSrcState.getVectorKey());
            v.add(probeSrcState.getOrganismKey());
            v.add(probeSrcState.getStrainKey());
            v.add(probeSrcState.getTissueKey());
            v.add(probeSrcState.getGenderKey());
            v.add(probeSrcState.getCellLineKey());
            v.add(probeSrcState.getRefsKey());
            v.add(probeSrcState.getName());
            v.add(probeSrcState.getDescription());
            v.add(probeSrcState.getAge());
            v.add(probeSrcState.getAgeMin());
            v.add(probeSrcState.getAgeMax());
            v.add(probeSrcState.getIsCuratorEdited());
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
     * @throws Nothing
     */
    public String getInsertSQL()
    {
        String message = "Class " + this.getClass().getName() +
                         " does not support the method getInsertSQL";
        throw new java.lang.UnsupportedOperationException(message);
    }


    /**
     * Build the SQL statements needed to update records in the database
     * for this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return A string representing the SQL statement.
     * @throws Nothing
     */
    public String getUpdateSQL()
    {
        String message = "Class " + this.getClass().getName() +
                         " does not support the method getUpdateSQL";
        throw new java.lang.UnsupportedOperationException(message);
    }


    /**
     * Build the SQL statements needed to delete records from the database
     * for this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return A string representing the SQL statement.
     * @throws Nothing
     */
    public String getDeleteSQL()
    {
        String message = "Class " + this.getClass().getName() +
                         " does not support the method getDeleteSQL";
        throw new java.lang.UnsupportedOperationException(message);
    }
}


//  $Log$
//  Revision 1.1  2003/09/19 17:43:23  dbm
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
