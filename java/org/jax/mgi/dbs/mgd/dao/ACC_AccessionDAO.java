//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

import java.util.Vector;

import org.jax.mgi.dbs.mgd.LogicalDBConstants;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.dao.DAO;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.Table;
import org.jax.mgi.shr.exception.MGIException;

/**
 * @is An object that represents records in the ACC_Accession and
 *     ACC_AccessionReference tables.
 * @has
 *   <UL>
 *   <LI> ACC_AccessionKey object
 *   <LI> ACC_AccessionState object
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

public class ACC_AccessionDAO extends DAO
{
    /////////////////
    //  Variables  //
    /////////////////

    // ACC_AccessionKey object.
    //
    private ACC_AccessionKey key = null;

    // ACC_AccessionState object.
    //
    private ACC_AccessionState state = null;


    /**
     * Constructs a new ACC_AccessionDAO object from a given ACC_AccessionState
     * object and a generated ACC_AccessionKey object.
     * @assumes Nothing
     * @effects Nothing
     * @param pState The ACC_AccessionState object.
     * @throws ConfigException
     * @throws DBException
     */
    public ACC_AccessionDAO (ACC_AccessionState pState)
        throws ConfigException, DBException
    {
        key = new ACC_AccessionKey();
        state = pState;
    }


    /**
     * Constructs a new ACC_AccessionDAO object from given ACC_AccessionKey and
     * ACC_AccessionState objects.
     * @assumes Nothing
     * @effects Nothing
     * @param pKey The ACC_AccessionKey object.
     * @param pState The ACC_AccessionState object.
     * @throws Nothing
     */
    public ACC_AccessionDAO (ACC_AccessionKey pKey, ACC_AccessionState pState)
    {
        key = pKey;
        state = pState;
    }


    /**
     * Get the key attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The key attribute.
     * @throws Nothing
     */
    public ACC_AccessionKey getKey () { return key; }


    /**
     * Get the state attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The state attribute.
     * @throws Nothing
     */
    public ACC_AccessionState getState () { return state; }


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

        // Add each of the table names to the vector.  Accession reference
        // records are not created for MGI IDs.
        //
        v.add("ACC_Accession");
        if (state.getLogicalDBKey().intValue() != LogicalDBConstants.MGI)
            v.add("ACC_AccessionReference");

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
        // bcp record for the ACC_Accession table.
        //
        if (table.getName().equals("ACC_Accession"))
        {
            // Split the accession ID into its prefix and numeric parts.
            //
            Vector vParts = splitAccID(state.getAccID());

            // Add the attributes to the vector.
            //
            v.add(key.getKey());
            v.add(state.getAccID());
            v.add(vParts.get(0));
            v.add((Integer)vParts.get(1));
            v.add(state.getLogicalDBKey());
            v.add(state.getObjectKey());
            v.add(state.getMGITypeKey());
            v.add(state.getPrivateJ());
            v.add(state.getPreferred());
        }

        // Populate the data vector with attributes that are needed to
        // create a bcp record for the ACC_AccessionReference table.
        //
        else if (table.getName().equals("ACC_AccessionReference"))
        {
            // Add the attributes to the vector.
            //
            v.add(key.getKey());
            v.add(state.getRefsKey());
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


    /**
     * Split an accession ID into its prefix and numeric parts.
     * @assumes Nothing
     * @effects Nothing
     * @param accID The accession ID to be split.
     * @return A vector containing the prefix part (String) and numeric
     *         part (Integer) of the accession ID.
     * @throws Nothing
     */
    public static Vector splitAccID (String accID)
    {
        String prefixPart = null;
        Integer numericPart = null;
        Vector v = new Vector();

        // Return null prefix and numeric parts if the accession ID is
        // null or blank.
        //
        if (accID == null || accID.length() == 0)
        {
            v.add(prefixPart);
            v.add(numericPart);
            return v;
        }

        // Determine if the accession ID contains any characters prior to a
        // series of digits that ends the string.
        //
        String[] parts = accID.split("\\d*$");

        // If there is a prefix part, save it and set the numeric part to
        // be any remaining characters.  Otherwise, the entire accession ID
        // is the numeric part.
        //
        if (parts.length > 0)
        {
            prefixPart = parts[0];
            if (accID.length() > prefixPart.length())
                numericPart = Integer.valueOf(accID.substring(prefixPart.length()));

        }
        else
            numericPart = Integer.valueOf(accID);

        // Add the prefix and numeric parts to the vector and return it.
        //
        v.add(prefixPart);
        v.add(numericPart);
        return v;
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
