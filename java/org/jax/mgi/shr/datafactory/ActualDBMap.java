package org.jax.mgi.shr.datafactory;

/*
* $Header$
* $Name$
*/

import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.config.RcdFile;
import org.jax.mgi.shr.config.RcdFile.Rcd;
import org.jax.mgi.shr.stringutil.Sprintf;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
* @module ActualDBMap.java
* @author jsb
*/

/** This ActualDBMap class provides an easy mechanism for generating HTML
*    links when given a logical database (name or key) and an accession ID.
*    It draws its information about logical and actual databases from the
*    database, and optionally allows for a file of overrides.
* @is a mapping from a logical database through its actual databases to links
*    generated from their URLs.
* @has a set of logical databases and a set of actual database data,
*    including keys, names, short names, urls, and ordering info.
* @does takes a logical database and an accession ID and gives back HTML
*    links
* @notes This class does no caching of data across object instantiations; each
*    time you instantiate a new ActualDBMap, it hits the database again and
*    re-reads the data file (if one is specified).  For the sake of
*    performance, you should probably cache one of these ActualDBMap objects
*    for a certain amount of time, reusing it as needed.
*/
public class ActualDBMap
{
    /* -------------------------------------------------------------------- */

    ///////////////
    // constructors
    ///////////////

    /** constructor; instantiates an ActualDBMap using only information from
    *    the database accessed via 'sqlDM'.
    * @param sqlDM provides access to a database
    * @assumes nothing
    * @effects queries the database
    * @throws nothing
    */
    public ActualDBMap (SQLDataManager sqlDM)
    throws DBException, IOException, FileNotFoundException
    {
        this.initialize (sqlDM, null);
    return;
    }

    /* -------------------------------------------------------------------- */

    /** constructor; instantiates an ActualDBMap using information from the
    *    database, with overrides processed from 'updateFile'.
    * @param sqlDM provides access to a database
    * @param updateFile an RcdFile which provides updated ActualDB information
    *    including removals, additions, or modifications
    * @assumes nothing
    * @effects queries the database and reads from 'updateFile' in the file
    *    system
    * @throws nothing
    */
    public ActualDBMap (SQLDataManager sqlDM, String updateFile)
    throws DBException, IOException, FileNotFoundException
    {
        this.initialize (sqlDM, updateFile);
    return;
    }

    /* -------------------------------------------------------------------- */

    //////////////////////////
    // public instance methods
    //////////////////////////

    /** get the first link from the given List of 'links'
    * @param List of Strings as generated by any of the get*Links() methods
    *    of this class
    * @return String an HTML link, or null if 'links' is null or empty
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes This method is provided as a convenience to pick an arbitrary
    *    link when a logical database has multiple associated actual databases
    *    and we don't care which we link to.
    */
    public String getFirstLink (List links)
    {
        if ((links != null) && (links.size() > 0))
    {
        return (String) links.get(0);
    }
    return null;
    }

    /* -------------------------------------------------------------------- */

    /** get a List of links for 'accID', one per actual database for the
    *    given 'logicalDb'.
    * @param logicalDb name of logical database
    * @param accID the accession ID for the item to retrieve
    * @return List of Strings; each String is an '<A HREF...>...</A>' HTML
    *    tag linking to an actual database for 'accID'.  The name of the
    *    actual database is used for the link text displayed.
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes will return an empty List if 'logicalDb' is not known
    */
    public List getLinks (String logicalDb, String accID)
    {
        if (!this.ldbNameToKey.containsKey(logicalDb))
    {
        return new ArrayList(0);
    }
    return this.getLinks(
            (Integer) this.ldbNameToKey.get(logicalDb), accID);
    }

    /* -------------------------------------------------------------------- */

    /** get a List of links for 'accID', one per actual database for the
    *    given 'logicalDb'.
    * @param logicalDb name of logical database
    * @param accID the accession ID for the item to retrieve
    * @return List of Strings; each String is an '<A HREF...>...</A>' HTML
    *    tag linking to an actual database for 'accID'.  The short name of the
    *    actual database is used for the link text displayed.  If no short
    *    name is defined, the full name is used.
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes will return an empty List if 'logicalDb' is not known
    */
    public List getShortLinks (String logicalDb, String accID)
    {
        if (!this.ldbNameToKey.containsKey(logicalDb))
    {
        return new ArrayList(0);
    }
    return this.getShortLinks(
            (Integer) this.ldbNameToKey.get(logicalDb), accID);
    }

    /* -------------------------------------------------------------------- */

    /** get a List of links for 'accID', one per actual database for the
    *    given 'logicalDb'.
    * @param logicalDb name of logical database
    * @param accID the accession ID for the item to retrieve
    * @return List of Strings; each String is an '<A HREF...>...</A>' HTML
    *    tag linking to an actual database for 'accID'.  The given 'accID'
    *    is used for the link text displayed.
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes will return an empty List if 'logicalDb' is not known
    */
    public List getIDLinks (String logicalDb, String accID)
    {
    if (!this.ldbNameToKey.containsKey(logicalDb))
    {
        return new ArrayList(0);
    }
        return this.getIDLinks (
        (Integer) this.ldbNameToKey.get(logicalDb), accID);
    }

    /* -------------------------------------------------------------------- */

    /** get a List of links for 'accID', one per actual database for the
    *    given 'logicalDbKey'.
    * @param logicalDbKey database key for the logical database
    * @param accID the accession ID for the item to retrieve
    * @return List of Strings; each String is an '<A HREF...>...</A>' HTML
    *    tag linking to an actual database for 'accID'.  The name of the
    *    actual database is used for the link text displayed.
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes will return an empty List if 'logicalDbKey' is not known
    */
    public List getLinks (Integer logicalDbKey, String accID)
    {
        return this.getLinksPrivate (logicalDbKey, accID, ADB_NAME);
    }

    /* -------------------------------------------------------------------- */

    /** get a List of links for 'accID', one per actual database for the
    *    given 'logicalDbKey'.
    * @param logicalDbKey database key for the logical database
    * @param accID the accession ID for the item to retrieve
    * @return List of Strings; each String is an '<A HREF...>...</A>' HTML
    *    tag linking to an actual database for 'accID'.  The short name of the
    *    actual database is used for the link text displayed.  If no short
    *    name is defined, the full name is used.
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes will return an empty List if 'logicalDbKey' is not known
    */
    public List getShortLinks (Integer logicalDbKey, String accID)
    {
        return this.getLinksPrivate (logicalDbKey, accID, ADB_SHORT_NAME);
    }

    /* -------------------------------------------------------------------- */

    /** get a List of links for 'accID', one per actual database for the
    *    given 'logicalDbKey'.
    * @param logicalDbKey database key for the logical database
    * @param accID the accession ID for the item to retrieve
    * @return List of Strings; each String is an '<A HREF...>...</A>' HTML
    *    tag linking to an actual database for 'accID'.  The given 'accID'
    *    is used for the link text displayed.
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes will return an empty List if 'logicalDbKey' is not known
    */
    public List getIDLinks (Integer logicalDbKey, String accID)
    {
        return this.getLinksPrivate (logicalDbKey, accID, null);
    }

    /* -------------------------------------------------------------------- */

    /** get a List of URLs, one per actual database for the
    *    given 'logicalDbKey'.
    * @param logicalDbKey database key for the logical database
    * @return List of Strings; each String is URL
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes will return an empty List if 'logicalDbKey' is not known
    */
    public ArrayList getActualDBUrl(Integer logicalDbKey) {

        return this.getActualDBUrlPrivate(logicalDbKey);
    }


    /* -------------------------------------------------------------------- */

    ///////////////////////////
    // private instance methods
    ///////////////////////////

    /** get a List of links for 'accID', one per actual database for the
    *    given 'logicalDbKey'.
    * @param logicalDbKey database key for the logical database
    * @param accID the accession ID for the item to retrieve
    * @param fieldname identifies which field in each actual database entry
    *    should be used as the text of the link; if null, uses the ID as the
    *    link text
    * @return List of Strings; each String is an '<A HREF...>...</A>' HTML
    *    tag linking to an actual database for 'accID'.
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes will return an empty List if 'logicalDbKey' is not known
    */
    private List getLinksPrivate (Integer logicalDbKey, String accID,
        String fieldname)
    {
    // List of links we will build to return
    ArrayList links = new ArrayList();

    // List of actual database info for the given 'logicalDbKey'
    List actualDbs = null;

    // number of items in 'actualDbs'
    int adbCount = -1;

    // current item being used in 'actualDbs'
    HashMap actualDb = null;

    // URL stored in 'actualDb'
    String url = null;

    // template for building a link
    String template = "<A HREF='%s'>%s</A>";

    // what the clickable text should be
    String linkText = null;

    // We should always have a valid 'logicalDbKey', but we check just to
    // make sure, and return an empty List otherwise.

    if (this.ldbKeyToAdb.containsKey(logicalDbKey))
    {
        actualDbs = (List) this.ldbKeyToAdb.get(logicalDbKey);
        adbCount = actualDbs.size();

        // walk through each actual database for this logical database,
        // generating a link for each

        for (int i = 0; i < adbCount; i++)
        {
            actualDb = (HashMap) actualDbs.get(i);

        // if we have a fieldname, then we lookup the value of that
        // field to use as the link text.  if not, we just use the
        // given accession ID.

        if (fieldname != null)
        {
            linkText = (String) actualDb.get(fieldname);
        }
        else
        {
            linkText = accID;
        }

        url = (String) actualDb.get(ADB_URL);

        if ((linkText != null) && (url != null))
        {
            // to correctly form the link, we need to insert the accID
            url = url.replaceFirst ("@@@@", accID);

            links.add (Sprintf.sprintf (template, url, linkText));
        }
        }
    }
    return links;
    }

    /* -------------------------------------------------------------------- */

    /** initialize this object, getting data from the database and updating it
    *    using 'updateFile' if specified; to be called by the constructors
    * @param sqlDM provides access to a database
    * @param updateFile an RcdFile which provides updated ActualDB information
    *    including removals, additions, or modifications
    * @return nothing
    * @assumes nothing
    * @effects queries the database and reads from 'updateFile' in the file
    *    system
    * @throws nothing
    */
    private void initialize (SQLDataManager sqlDM, String updateFile)
    throws DBException, IOException, FileNotFoundException
    {
    // initialize the HashMaps used for storing data in this object

        this.ldbNameToKey = new HashMap();
    this.ldbKeyToAdb = new HashMap();
    this.adbKeyToLdbKey = new HashMap();

    // first get info from the database, then optionally update it from
    // the 'updateFile'

    this.getDbInfo(sqlDM);
    if (updateFile != null)
    {
        this.getFileInfo(updateFile);
    }
    return;
    }

    /* -------------------------------------------------------------------- */

    /** initialize this object with data from the database.
    * @param sqlDM provides access to a database
    * @return nothing
    * @assumes nothing
    * @effects queries the database to get info about logical and actual dbs
    * @throws nothing
    */
    private void getDbInfo (SQLDataManager sqlDM)
    throws DBException
    {
        ResultsNavigator nav = null;    // set of results from query
    RowReference rr = null;     // one row in 'nav'
    int counter = 0;        // counts rows processed so far

    String[] queries = new String[] {
            MSM_LDB_ADB_INFO, OTHER_LDB_ADB_INFO };

    // retrieve the data from the database

    //nav = sqlDM.executeQuery (MSM_LDB_ADB_INFO);
    for (int i = 0; i < queries.length; i++)
    {
        nav = sqlDM.executeQuery (queries[i]);

        // if we got at least one row, then we can go ahead and process
        // them

        if (nav.next())
        {
            rr = (RowReference) nav.getCurrent();

            String ldbName = null;  // name of logical db for current row
            Integer ldbKey = null;  // key of logical db for current row
            Integer adbKey = null;  // key of actual db for current row
            HashMap adbInfo = null; // table of info for one actual db

            ArrayList adbList = null;   // list of 'adbInfo' objects
                            // ...for a single logical db
            do
            {
            // get attributes to be used multiple times, and increment
            // the counter of rows.

            ldbKey = rr.getInt(1);
            ldbName = rr.getString(2);
            adbKey = rr.getInt(3);
            counter++;

            // keep mapping from each logical db name to its key

            if (!this.ldbNameToKey.containsKey (ldbName))
            {
            this.ldbNameToKey.put (ldbName, ldbKey);
            }

            // keep mapping from each actual db key to its logical db
            // key

            this.adbKeyToLdbKey.put (adbKey, ldbKey);

            // finally, create an entry for this row

            adbInfo = createActualDB (adbKey, rr.getString(4),
                rr.getString(4), new Integer(counter),
            rr.getString(5) );

            // if we've not already seen this logical database, then
            // we need to create a new list to store our 'adbInfo'
            // objects

            if (!this.ldbKeyToAdb.containsKey(ldbKey))
            {
                adbList = new ArrayList();
                this.ldbKeyToAdb.put (ldbKey, adbList);
            }
            else
            {
                // otherwise, just get a reference to the existing
            // List

                adbList = (ArrayList) this.ldbKeyToAdb.get(ldbKey);
            }

            addActualDB (adbList, adbInfo); // and add this entry

            } while (nav.next());       // process all remaining rows

        }   // end of 'if' statement that checks for any results

        nav.close();

    } // end of 'for' loop that iterates through queries
    }

    /* -------------------------------------------------------------------- */

    /** process the given 'updateFile' containing updates for actual database
    *    information retrieved from the database.
    * @param updateFile path to the RcdFile of updates (in the file system)
    * @return nothing
    * @assumes nothing
    * @effects reads 'updateFile' from the file system
    * @throws IOException if there are problems reading from 'updateFile'
    * @throws FileNotFoundException if 'updateFile' does not exist
    */
    private void getFileInfo (String updateFile)
        throws IOException, FileNotFoundException
    {
    // set of all updates from the given 'updateFile'
    RcdFile entries = new RcdFile (updateFile, ENTRY_KEY);

    // the update currently being processed
    Rcd entry = null;

    // interates through all the given updates
    Iterator it = entries.getRcds();

    // these three fields are required for all entries:

    String op = null;       // operation specified in this 'entry'
    Integer actualDbKey = null; // key of this entry's actual database
    String entryKey = null;     // unique identifier for this entry

    // these fields are optional, depending on the type of 'op':

    String logicalDbKeyStr = null;  // logical db key as a String
    Integer logicalDbKey = null;    // logical db key as an Integer
    String name = null;     // name of the actual database
    String shortName = null;    // shorter name of the actual database
    String orderStr = null;     // ordering value as a String
    Integer order = null;       // ordering value as an Integer
    String url = null;      // URL for the actual database

    // these variables are used for processing, but are not part of an
    // individual entry

    List actualDbs = null;      // list of actual dbs for this log.db
    HashMap actualDb = null;    // actual database to be updated
    int pos = -1;           // index into 'actualDbs'
    boolean orderChanged = false;   // did ADB_ORDER in 'actualDb' change?

    // step through all entries...

    while (it.hasNext())
    {
        entry = (Rcd) it.next();

        // use this 'entryKey' to help report error messages
        entryKey = entry.getString(ENTRY_KEY);

        // for now, we just ignore 'entry' if it omits either of the
        // other required fields.  In reality, this should be an error.

        if (!entry.containsKey(OPERATION))
        {
            continue;
        }
        if (!entry.containsKey(ADB_KEY))
        {
            continue;
        }

        // these values are now guaranteed to exist, though valueOf()
        // could throw an exception

        op = entry.getString(OPERATION);
        actualDbKey = Integer.valueOf(entry.getString(ADB_KEY));

        logicalDbKey = null;    // reset for this 'entry'

        // any or all of these four fields may be present in the 'entry'

        name = null;
        shortName = null;
        orderStr = null;
        url = null;

        if (entry.containsKey(ADB_NAME))
        {
            name = entry.getString(ADB_NAME);
        }
        if (entry.containsKey(ADB_SHORT_NAME))
        {
            shortName = entry.getString(ADB_SHORT_NAME);
        }
        if (entry.containsKey(ADB_ORDER))
        {
            orderStr = entry.getString(ADB_ORDER);
        }
        if (entry.containsKey(ADB_URL))
        {
            url = entry.getString(ADB_URL);
        }

        // if we have an 'orderStr', then get its Integer value

        if ((orderStr != null) && (orderStr.length() > 0))
        {
            order = Integer.valueOf(orderStr);
        }
        else
        {
            order = null;
        }

        // now we do the actual processing of the 'entry', based on its
        // specified type of operation...

        if ("remove".equals(op))    // remove the actual database?
        {
        // find the logical database associated with the specified
        // actual database.  If valid, remove the actual database
        // from its List of associated ones.

            logicalDbKey = (Integer) this.adbKeyToLdbKey.get(actualDbKey);
        if (this.ldbKeyToAdb.containsKey(logicalDbKey))
        {
            actualDbs = (List) this.ldbKeyToAdb.get(logicalDbKey);
            pos = findActualDB (actualDbs, actualDbKey);
            if (pos >= 0)
            {
                actualDbs.remove(pos);
            }
        }
        }   // operation is remove

        else if ("modify".equals(op))   // modify actual database?
        {
        // find the logical database associated with this actual
        // database.  If valid, look up the actual database and
        // update its contents.

            logicalDbKey = (Integer) this.adbKeyToLdbKey.get(actualDbKey);
        if (this.ldbKeyToAdb.containsKey(logicalDbKey))
        {
            actualDbs = (List) this.ldbKeyToAdb.get(logicalDbKey);
            pos = findActualDB (actualDbs, actualDbKey);

            if (pos >= 0)
            {
                actualDb = (HashMap) actualDbs.get(pos);
            orderChanged = updateActualDB (actualDb, name,
                shortName, order, url);

            // if the ADB_ORDER attribute of the actual database
            // changed, we remove it and re-add it to ensure that
            // it is stored in the proper order

            if (orderChanged)
            {
                actualDbs.remove(pos);
                addActualDB (actualDbs, actualDb);
            }
            }
        }
        }   // operation is modify

        else if ("add".equals(op))      // add a new actual database
        {
        // entries to be added must specify the logical database
        // to which they should be added

            logicalDbKeyStr = entry.getString(LDB_KEY);
        if ((logicalDbKeyStr != null) && (logicalDbKeyStr.length() > 0))
        {
            logicalDbKey = Integer.valueOf(logicalDbKeyStr);

            if (shortName == null)
            {
                shortName = name;
            }

            if (order == null)
            {
                order = Integer.valueOf(entryKey);
            }

            if ((name == null) || (url == null))
            {
            // this should really give an exception
                continue;
            }

                actualDb = createActualDB (actualDbKey, name, shortName,
                order, url);
        }

        // if this logical database has no other active entries, then
        // we must create a new List.

        if (!this.ldbKeyToAdb.containsKey(logicalDbKey))
        {
            actualDbs = new ArrayList();
            this.ldbKeyToAdb.put (logicalDbKey, actualDbs);
        }
        else    // otherwise, retrieve the List for this logical db
        {
            actualDbs = (ArrayList) this.ldbKeyToAdb.get(
                logicalDbKey);
        }

        addActualDB (actualDbs, actualDb);

        }   // operation is add

        else
        {
            // should give an exception here, for an invalid operation
        }
    }

        return;
    }

    /* -------------------------------------------------------------------- */

    /////////////////////////
    // private static methods
    /////////////////////////

    /** look in 'actualDbs' to find the actual database with the given
    *    'actualDbKey'.
    * @param actualDbs List of HashMaps, each defining one actual database
    * @param actualDbKey the Integer actual database key we are seeking
    * @return int the index in 'actualDbs' of the entry having the given
    *    'actualDbKey', or -1 if no matching one could be found
    * @assumes all items in 'actualDbs' can be cast to a 'HashMap'
    * @effects nothing
    * @throws nothing
    */
    private static int findActualDB (List actualDbs, Integer actualDbKey)
    {
        HashMap actualDb = null;        // actual db being examined
    int listLength = actualDbs.size();  // length of 'actualDbs'
    Integer thisDbKey = null;       // actual db key in 'actualDb'

    // walk through the list until we find the matching actual database
    // entry.  If we don't find it, then we return -1.

    for (int i = 0; i < listLength; i++)
    {
        actualDb = (HashMap) actualDbs.get(i);
        thisDbKey = (Integer) actualDb.get(ADB_KEY);

        if (thisDbKey.equals(actualDbKey))
        {
            return i;
        }
    }
    return -1;
    }

    /* -------------------------------------------------------------------- */

    /** add the given 'actualDb' entry to the List of 'actualDbs', in the
    *    proper order as determined by the ADB_ORDER attribute in 'actualDb'.
    * @param actualDbs List of HashMaps, each defining one actual database
    * @param actualDb the actual database to be added to 'actualDbs'
    * @return nothing
    * @assumes 'actualDb' contains an ADB_ORDER attribute
    * @effects adds 'actualDb' to 'actualDbs'
    * @throws nothing
    */
    private static void addActualDB (List actualDbs, HashMap actualDb)
    {
    boolean added = false;          // did we add 'actualDb' yet?
        int listLength = actualDbs.size();  // # of items in 'actualDbs'
    HashMap current = null;         // item in 'actualDbs'
    Integer currentOrder = null;        // ADB_ORDER within 'current'

    // ADB_ORDER of the element we are inserting
    Integer actualDbOrder = (Integer) actualDb.get(ADB_ORDER);

    for (int i = 0; i < listLength; i++)
    {
        // as we walk the list, we look for the first element with an
        // ADB_ORDER that comes after the ADB_ORDER of the element we want
        // to insert.  If we find it, then we insert the new element
        // before it.

        current = (HashMap) actualDbs.get(i);
        currentOrder = (Integer) current.get(ADB_ORDER);

        if (!added && (currentOrder.compareTo(actualDbOrder) > 0))
        {
            actualDbs.add (i, actualDb);
        added = true;
        break;
        }
    }

    if (!added) // if all elements preceded this one, add to the end
    {
        actualDbs.add (actualDb);
    }
    return;
    }

    /* -------------------------------------------------------------------- */

    /** create a HashMap representing the actual database described in the
    *   parameters.
    * @param actualDbKey unique key for the actual database
    * @param name the name of the actual database
    * @param shortName a shorter name for the actual database
    * @param order used for ordering multiple actual databases for the same
    *    logical database
    * @param url URL for linking to an accession ID using this actual database
    * @return HashMap which defines ADB_KEY, ADB_NAME, ADB_SHORT_NAME,
    *    ADB_ORDER, and ADB_URL.
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    private static HashMap createActualDB (Integer actualDbKey,
        String name, String shortName, Integer order, String url)
    {
        HashMap actualDB = new HashMap();

    actualDB.put (ADB_KEY, actualDbKey);
    actualDB.put (ADB_NAME, name);
    actualDB.put (ADB_SHORT_NAME, shortName);
    actualDB.put (ADB_ORDER, order);
    actualDB.put (ADB_URL, url);

    return actualDB;
    }

    /* -------------------------------------------------------------------- */

    /** update the given 'actualDb' to reflect the new values specified in
    *    the other parameters
    * @param actualDb the existing actual database information which we need
    *    to change
    * @param name the name of the actual database (if non-empty)
    * @param shortName abbreviated name of the actual database (if non-empty)
    * @param order ordering value, used to sort actual databases for the
    *    same logical database (if non-null)
    * @param url the URL for linking to an accession ID for this actual
    *    database
    * @return boolean true if the ADB_ORDER attribute of 'actualDb' changed,
    *    meaning that we will need to adjust the sorting of actual databases
    * @assumes nothing
    * @effects alters 'actualDb'
    * @throws nothing
    * @notes If any of the String parameters are null or are an empty String,
    *    then their respective values in 'actualDb' are not changed.  If
    *    ADB_ORDER is null, then the ADB_ORDER value in 'actualDb' will not be
    *    changed.
    */
    private static boolean updateActualDB (HashMap actualDB, String name,
        String shortName, Integer order, String url)
    {
        boolean orderChanged = false;   // did the ordering attribute change?

        if ((name != null) && (name.length() > 0))
    {
        actualDB.put (ADB_NAME, name);
    }

        if ((shortName != null) && (shortName.length() > 0))
    {
        actualDB.put (ADB_SHORT_NAME, shortName);
    }

        if (order != null)
    {
        actualDB.put (ADB_ORDER, order);
        orderChanged = true;
    }

        if ((url != null) && (url.length() > 0))
    {
        actualDB.put (ADB_URL, url);
    }

    return orderChanged;
    }

    /* -------------------------------------------------------------------- */

    /** get a List of URLs, one per actual database for the
    *    given 'logicalDbKey'.
    * @param logicalDbKey database key for the logical database
    * @return List of Strings; each String is URL
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    * @notes will return an empty List if 'logicalDbKey' is not known
    */
   private ArrayList getActualDBUrlPrivate(Integer logicalDbKey) {

        ArrayList urls = new ArrayList();
        List actualDbs;
        HashMap actualDb;

        if (this.ldbKeyToAdb.containsKey(logicalDbKey)) {
            actualDbs = (List) this.ldbKeyToAdb.get(logicalDbKey);

            for(int i=0;i<actualDbs.size();i++) {
                actualDb = (HashMap) actualDbs.get(0);
                urls.add((String) actualDb.get(ADB_URL));
            }

        }
        return urls;
    }

    /* -------------------------------------------------------------------- */

    /////////////////////
    // instance variables
    /////////////////////

    // These three HashMaps track all the logical and actual database info
    // in this object.  We map logical database keys to their names.  We
    // map actual database keys to their respective logical database keys.
    // And, we have a mapping from each logical database key to its List
    // of actual databases.

    private HashMap ldbNameToKey = null;    // maps ldb name to ldb key

    private HashMap adbKeyToLdbKey = null;  // maps Integer adbKey to its
                            // ...Integer ldbKey

    private HashMap ldbKeyToAdb = null;     // maps Integer ldbKey to
                            // ...List of adb data

    /* -------------------------------------------------------------------- */

    ///////////////////
    // static variables
    ///////////////////

    // SQL statement to retrieve logical and actual database information from
    // the database.  Use the actual database's sequence number (from the
    // 'Actual DB' MGI Set) for ordering the actual databases.  This query
    // only returns actual databases associated with sequence IDs, as those
    // are the only ones in the MGI Set named 'Actual DB'.

    private static String MSM_LDB_ADB_INFO =
        "SELECT ldb._LogicalDB_key, ldb.name, adb._ActualDB_key, adb.name"
    +   ", adb.url, msm.sequenceNum"
    + " FROM ACC_LogicalDB ldb, ACC_ActualDB adb"
    +   ", MGI_Set ms, MGI_SetMember msm"
    + " WHERE ldb._LogicalDB_key = adb._LogicalDB_key"
    +   " AND adb._ActualDB_key = msm._Object_key"
    +   " AND msm._Set_key = ms._Set_key"
    +   " AND ms.name = 'Actual DB'"
    + " ORDER BY msm.sequenceNum, adb.name";

    // SQL statement to retrieve logical and actual database information from
    // the database.  This includes only those actual databases not in the
    // MGI Set named 'Actual DB' -- thus, including all non-sequence actual
    // databases.  We simply order these ones by name.

    private static String OTHER_LDB_ADB_INFO =
    "SELECT ldb._LogicalDB_key, ldb.name, adb._ActualDB_key, adb.name"
    +   ", adb.url"
    + " FROM ACC_LogicalDB ldb, ACC_ActualDB adb"
    + " WHERE ldb._LogicalDB_key = adb._LogicalDB_key"
    +   " AND adb._ActualDB_key not in ("
    +       " SELECT msm._Object_key"
    +       " FROM MGI_Set ms, MGI_SetMember msm"
    +       " WHERE ms._Set_key = msm._Set_key"
    +           " AND ms.name = 'Actual DB')"
    +       " ORDER BY adb.name";

    // These two values identify attributes required in each Rcd in the
    // update file.

    private static String ENTRY_KEY = "entry";      // unique key for Rcd
    private static String OPERATION = "operation";  // type of update

    // The final six String values identify attributes in each actual db's
    // HashMap.  (These are the keys.)  They also are used as attributes in
    // the update file's Rcds.

    private static String LDB_KEY = "_LogicalDB_key";   // logical db key
    private static String ADB_KEY = "_ActualDB_key";    // actual db key
    private static String ADB_NAME = "name";        // actual db name
    private static String ADB_SHORT_NAME = "shortName"; // shorter adb name
    private static String ADB_URL = "url";      // url for actual db
    private static String ADB_ORDER = "order";      // sort order for adb
}

/*
* $Log$
* Revision 1.1  2004/04/09 14:34:09  jsb
* initial addition
*
* $Copyright$
*/
