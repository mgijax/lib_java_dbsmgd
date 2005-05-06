package org.jax.mgi.shr.datafactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dto.DTO;

public interface DetailPageFactory {
    /** find a unique key identifying the database object specified by
    *   the given 'parms'(object dependant on the implementing class)
    * @param parms set of parameters specifying which database object
    *        we are seeking.  Parameters are dependant on the implementing
    *        class.
    * @return String a unique key identifying the database object specified
    *         in the given set of 'parms', or null if none can be found
    * @assumes nothing
    * @effects queries the database using 'sqlDM'
    * @throws DBException if there is a problem while attempting to query the
    *    database using 'sqlDM'
    */
    public abstract String getKey(Map parms) throws DBException;

    /** find the _Object_key that corresponds to the given mouse 'accID'
    *   for the _MGIType_key associated with database object type of the
    *   implementing factory.  If multiple objects of the appropritate type
    *   have the same 'accID', then we arbitrarily choose one of the keys
    *   to return.
    * @param accID the accession ID for which we seek an associated object
    * @return int the _Object_key corresponding to 'accID'; will be -1 if
    *    no _Object_key could be found
    * @assumes That returning any object associated with that accession ID is
    *    good enough.  In the case where 'accID' is an object's primary MGI
    *    ID, this is valid.
    * @effects nothing
    * @throws DBException if there is a problem querying the database or
    *    processing the results.
    */
    public abstract int getKeyForID (String accID) throws DBException;


    //////////////////////////////////////////////////////
    // methods to retrieve sets of sections of information
    //////////////////////////////////////////////////////

    /** retrieve the full suite of data available for the database object
    *   specified in 'parms' (object dependant on the implementing class).
    * @param parms set of parameters specifying which object we are seeking.
    * @return DTO which defines all object's data fields
    * @assumes nothing
    * @effects retrieves all the object's data by quering a database and
    *          retrieving data via HTTP as needed
    * @throws DBException if there is a problem querying the database or
    *    processing the results
    * @throws MalformedURLException if any URLs are invalid
    * @throws IOException if there are problems reading from URLs via HTTP
    */
    public abstract DTO getFullInfo (Map parms) throws DBException,
                                                       MalformedURLException,
                                                       IOException;

    /** retrieve basic information about the object with the given 'key'.
    * @param key the primary key of the object whose data we seek
    * @return DTO contains information retrieved from the database.  See
    *    notes of the implementing class.  If no data for the specified
    *    object is found, then returns an empty DTO.
    * @assumes nothing
    * @effects queries the database
    * @throws DBException if there are problems querying the database or
    *    stepping through the results.
    */
    public abstract DTO getBasicInfo (int key) throws DBException;

}