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
 *   <LI> AccessionKey object
 *   <LI> AccessionState object
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

public class AccessionDAO extends DAO
{
    /////////////////
    //  Variables  //
    /////////////////

    // AccessionKey object.
    //
    private AccessionKey accessionKey = null;

    // AccessionState object.
    //
    private AccessionState accessionState = null;


    /**
     * Constructs a new AccessionDAO object from a given AccessionState object
     * and a generated AccessionKey object.
     * @assumes Nothing
     * @effects Nothing
     * @param pState The AccessionState object
     * @throws Nothing
     */
    public AccessionDAO (AccessionState pState)
        throws ConfigException, DBException
    {
        accessionKey = new AccessionKey();
        accessionState = pState;
    }


    /**
     * Constructs a new AccessionDAO object from given AccessionKey and
     * AccessionState objects.
     * @assumes Nothing
     * @effects Nothing
     * @param pKey The AccessionKey object
     * @param pState The AccessionState object
     * @throws Nothing
     */
    public AccessionDAO (AccessionKey pKey, AccessionState pState)
    {
        accessionKey = pKey;
        accessionState = pState;
    }


    /**
     * Get the accessionState attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The accessionState attribute
     * @throws Nothing
     */
    public AccessionState getAccessionState () { return accessionState; }


    /**
     * Get the accessionKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The accessionKey attribute
     * @throws Nothing
     */
    public AccessionKey getAccessionKey () { return accessionKey; }


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
        if (accessionState.getLogicalDBKey().intValue() != LogicalDBConstants.MGI)
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
        // bcp record for the ACC_Accession table.
        //
        if (table.getName().equals("ACC_Accession"))
        {
            // Split the accession ID into its prefix and numeric parts.
            //
            Vector vParts = splitAccID(accessionState.getAccID());

            // Add the attributes to the vector.
            //
            v.add(accessionKey.getPrimaryKey());
            v.add(accessionState.getAccID());
            v.add(vParts.get(0));
            v.add((Integer)vParts.get(1));
            v.add(accessionState.getLogicalDBKey());
            v.add(accessionState.getObjectKey());
            v.add(accessionState.getMGITypeKey());
            v.add(accessionState.getPrivateAcc());
            v.add(accessionState.getPreferred());
        }

        // Populate the data vector with attributes that are needed to
        // create a bcp record for the ACC_AccessionReference table.
        //
        else if (table.getName().equals("ACC_AccessionReference"))
        {
            // Add the attributes to the vector.
            //
            v.add(accessionKey.getPrimaryKey());
            v.add(accessionState.getRefsKey());
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


    /**
     * Split an accession ID into its prefix and numeric parts.
     * @assumes Nothing
     * @effects Nothing
     * @param accID The accession ID to be split
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
//  Revision 1.5  2003/09/30 16:58:06  dbm
//  Use Integer instead of int for attributes
//
//  Revision 1.4  2003/09/25 17:52:55  dbm
//  Continued development
//
//  Revision 1.3  2003/09/24 15:12:56  dbm
//  Changed DataInstance to DAO
//
//  Revision 1.2  2003/09/23 13:23:51  dbm
//  Continued development
//
//  Revision 1.1  2003/09/19 17:43:18  dbm
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
