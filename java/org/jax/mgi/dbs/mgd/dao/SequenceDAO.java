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
 * @is An object that represents a record in the PRB_Sequence table.
 * @has
 *   <UL>
 *   <LI> SequenceeKey object
 *   <LI> SequenceState object
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

public class SequenceDAO extends DAO
{

    // SequenceKey object.
    //
    private SequenceKey sequenceKey = null;

    // SequenceState object.
    //
    private SequenceState sequenceState = null;


    /**
     * Constructs a new SequenceDAO object from a given SequenceState object
     * and a generated SequenceKey object.
     * @assumes Nothing
     * @effects Nothing
     * @param pState The SequenceState object
     * @throws Nothing
     */
    public SequenceDAO (SequenceState pState)
        throws ConfigException, DBException
    {
        sequenceKey = new SequenceKey();
        sequenceState = pState;
    }


    /**
     * Constructs a new SequenceDAO object from given SequenceKey and
     * SequenceState objects.
     * @assumes Nothing
     * @effects Nothing
     * @param pKey The SequenceKey object
     * @param pState The SequenceState object
     * @throws Nothing
     */
    public SequenceDAO (SequenceKey pKey, SequenceState pState)
    {
        sequenceKey = pKey;
        sequenceState = pState;
    }


    /**
     * Get the sequenceKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The sequenceKey attribute
     * @throws Nothing
     */
    public SequenceKey getSequenceKey () { return sequenceKey; }


    /**
     * Get the sequenceState attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The sequenceState attribute
     * @throws Nothing
     */
    public SequenceState getSequenceState () { return sequenceState; }


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
        v.add("SEQ_SEQUENCE");

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
        if (table.getName().equals("SEQ_SEQUENCE"))
        {
            // Add the attributes to the vector.
            //
            v.add(sequenceKey.getPrimaryKey());
            v.add(sequenceState.getSequenceQualityKey());
            v.add(sequenceState.getSequenceStatusKey());
            v.add(sequenceState.getSegmentTypeKey());
            v.add(sequenceState.getSequenceProviderKey());
            v.add(sequenceState.getSegmentTypeKey());
            v.add(sequenceState.getLength());
            v.add(sequenceState.getDescription());
            v.add(sequenceState.getVersion());
            v.add(sequenceState.getDivision());
            v.add(sequenceState.getVirtual());
            v.add(sequenceState.getRawType());
            v.add(sequenceState.getRawLibrary());
            v.add(sequenceState.getRawOrganism());
            v.add(sequenceState.getRawStrain());
            v.add(sequenceState.getRawTissue());
            v.add(sequenceState.getRawAge());
            v.add(sequenceState.getRawSex());
            v.add(sequenceState.getRawCellLine());
            v.add(sequenceState.getNumberOfOrganisms());
            v.add(sequenceState.getSeqrecordDate());
            v.add(sequenceState.getSequenceDate());
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
        throw MGIException.getUnsupportedMethodException();
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
        throw MGIException.getUnsupportedMethodException();
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
        throw MGIException.getUnsupportedMethodException();
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
