package org.jax.mgi.shr.datafactory;

/*
* $Header$
* $Name$
*/

/**
* @module PrivateRefSet.java
* @author jsb
*/

import java.util.HashMap;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;

/** The PrivateRefSet class represents a set of references (J-numbers) which
* are to be de-emphasized in the web interface.  That is, they should not be
* highlighted on a marker detail page.  This set of references is built using
* a set of queries against the database.
*/
public class PrivateRefSet
{
    /* -------------------------------------------------------------------- */

    /////////////////////////////
    // Private Instance Variables
    /////////////////////////////

    // set of J: numbers which are to be de-emphasized.  Each String J# maps
    // to a dummy value.
    private HashMap myJnums = null;

    /* -------------------------------------------------------------------- */

    /////////////////
    // Public methods
    /////////////////

    /** constructor
    * @param sqlDM used to query the database
    * @assumes nothing
    * @effects queries the database using 'sqlDM'
    * @throws nothing
    */
    public PrivateRefSet (SQLDataManager sqlDM)
    {
        this.myJnums = new HashMap();
	this.load (sqlDM);
	return;
    }

    /* -------------------------------------------------------------------- */

    /** determines whether the given 'jnum' is contained in this set of
    *    private references
    * @param jnum the J: number to determine whether it is in the set
    * @return boolean true if the given 'jnum' is in the set, false if not
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    public boolean contains (String jnum)
    {
	return this.myJnums.containsKey(jnum);
    }

    /* -------------------------------------------------------------------- */

    //////////////////
    // Private methods
    //////////////////

    /** load the set of J: numbers, using 'sqlDM' to access the database.
    * @param sqlDM used to query the database
    * @return nothing
    * @assumes All queries in QUERIES will return J: numbers in the first
    *    column returned.
    * @effects queries the database using 'sqlDM'
    */
    private void load (SQLDataManager sqlDM)
    {
	ResultsNavigator nav = null;		// set of query results
	RowReference rr = null;			// one row in 'nav'

        // run each of the set of 'QUERIES', adding each resulting J: number
	// to our set of 'this.myJnums'

	for (int i = 0; i < QUERIES.length; i++)
	{
	    try
	    {
	        nav = sqlDM.executeQuery (QUERIES[i]);
	        while (nav.next())
	        {
	            rr = (RowReference) nav.getCurrent();
	            this.myJnums.put (rr.getString(1), ""); 
	        }
	        nav.close();
	    }
	    catch (DBException dbExc)
	    {
		// just ignore this query if it caused problems
	    }
	}
	return;
    }

    /* -------------------------------------------------------------------- */

    //////////////////////////
    // Private class constants
    //////////////////////////

    /* basic query for use in building other queries (we just append more
    * 'where' clauses to generate the actual queries used).
    */
    private static final String BASIC_QUERY =
    	"select acc.accID "
	+ " from BIB_Refs br, "
	+ "    ACC_Accession acc "
	+ " where br._Refs_key = acc._Object_key "
	+ "    and acc._MGIType_key = " + DBConstants.MGIType_Reference
	+ "    and acc._LogicalDB_key = " + DBConstants.LogicalDB_MGI
	+ "    and acc.prefixPart = 'J:'";

    // query to retrieve J: numbers for database loads
    private static final String LOADS = BASIC_QUERY
    	+ "    and br.journal like 'database%'";

    // query to retrieve J: numbers for personal communications
    private static final String PERSONAL = BASIC_QUERY
	+ "    and br.journal like 'personal%'";

    // query to retrieve J: numbers for genbank submissions
    private static final String GENBANK = BASIC_QUERY
	+ "    and br.journal = 'Genbank Submission'";

    // query to retrieve J: numbers for books
    private static final String BOOKS = BASIC_QUERY
	+ "    and br.refType = 'BOOK'";

    // query to retrieve J: numbers for MGI direct data submissions
    private static final String SUBMISSIONS = BASIC_QUERY
	+ "    and br.journal like '%Data Submission%'";

    // query to retrieve J: numbers for curatorial references
    private static final String CURATORIAL = BASIC_QUERY
	+ "    and br.journal = null "
	+ "    and br._ReviewStatus_key = " + DBConstants.ReviewStatus_MGI;

    // all queries to use in determining the 'this.myJnums' set...
    private static final String[] QUERIES = new String[] {
							LOADS,
							PERSONAL,
							GENBANK,
							BOOKS,
							SUBMISSIONS,
							CURATORIAL
							};
}

/*
* $Log$
* Revision 1.1  2003/12/30 16:28:30  mbw
* initial import into this product
*
* Revision 1.2  2003/12/01 13:13:53  jsb
* Updated to use ExpiringObjectCache, simplifying code
*
* Revision 1.1  2003/07/03 17:37:14  jsb
* initial addition for use by JSAM WI
*
* $Copyright$
*/
