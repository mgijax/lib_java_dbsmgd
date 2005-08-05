package org.jax.mgi.shr.datafactory;

/*
* $Header$
* $Name$
*/

/**
* @module GeneFamilyMap.java
* @author jsb
*/

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import org.jax.mgi.shr.stringutil.StringLib;

/** The GeneFamilyMap class stores the mapping from a gene's MGI accession ID
* to the URL for its Gene Family page.  This is a temporary mechanism until
* the gene family information is stored in the database.  For now, this class
* takes a URL in the constructor.  This should be the URL to a Python CGI
* (the eGUL, currently in MGI Home) which will return the mapping of gene
* accession IDs to the URLs for their corresponding gene family page.
*/
public class GeneFamilyMap
{
    /* -------------------------------------------------------------------- */

    /////////////////////////////
    // private instance variables
    /////////////////////////////

    // the URL to the eGUL -- to use when retrieving the data for building
    // the 'pages' data set
    private String myURL = null;

    // maps from accession ID to URL for a gene family web page
    private HashMap myPages = null;

    /* -------------------------------------------------------------------- */

    /////////////////
    // Public methods
    /////////////////

    /* -------------------------------------------------------------------- */

    /** constructor
    * @param myUrl URL to the script that provides the mapping between
    *    gene MGI IDs and the URLs for their respective gene family pages.
    * @assumes nothing
    * @effects reads from 'egulUrl' via HTTP
    * @throws nothing
    */
    public GeneFamilyMap (String myUrl)
    {
	this.myURL = myUrl;
        this.myPages = new HashMap();
	this.load();
	return;
    }

    /* -------------------------------------------------------------------- */

    /** retrieve the URL for the gene family page associated with the given
    *    MGI accession 'id'.
    * @param id a mouse gene's MGI accession ID
    * @return String the URL for the gene family page, or null if there is
    *    none for that 'id'
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    public String getUrl (String id)
    {
	return (String) this.myPages.get (id);
    }

    /* -------------------------------------------------------------------- */

    ///////////////////////////
    // private instance methods
    ///////////////////////////

    /** finish the initialization of this object by loading the gene family
    *    mappings from the URL provided to the constructor.
    * @return nothing
    * @assumes nothing
    * @effects reads from 'this.myURL' via HTTP
    * @throws nothing
    */
    private void load ()
    {
	// used to read from 'this.myURL'
	BufferedReader reader = null;

	// is 'reader' currently open?  (initially, it is not)
	boolean isOpen = false;
	
	// attempt to retrieve the mappings from 'this.myURL' via HTTP

	try
	{
	    // URL to the script which provides the mappings
	    URL egulURL = new URL(this.myURL);

	    // one line read from 'reader'; must be tab-delimited with at
	    // least three columns
	    String line = null;

	    // set of values extracted from 'line'
	    ArrayList items = null;

	    // Finally, open 'reader' and begin processing one 'line' at a
	    // time...

	    reader = new BufferedReader (new InputStreamReader (
					        egulURL.openStream() ));

	    isOpen = true;	// succeeded in opening the 'reader'

	    line = reader.readLine();
	    while (line != null)
	    {
	        items = StringLib.split (line);
	        if (items.size() >= 3)
	        {
		    // MGI ID --> gene family URL

		    this.myPages.put (
		        (String) items.get(0),
			(String) items.get(2) );
		}
		else
		{
		    throw new Exception ("Bad line format");
		}
	        line = reader.readLine();
	    }
	}
	catch (Exception e)
	{
	    // If we get here, then an unexpected exception occurred, which
	    // we just ignore.  We opt to go on with whatever we had already
	    // retrieved for 'this.myPages'.
	    ;
	}

	// we close the reader out here, since we could have exited the 'if'
	// and the 'while' statements (above) because of an exception.

	if (isOpen)
	{
	    try
	    {
	        reader.close();
	    }
	    catch (IOException exc)
	    {
		// if we couldn't close the reader, just move on...
	    }
	}
	return;
    }
}

/*
* $Log$
* Revision 1.2.16.2  2005/08/02 21:33:24  mbw
* merged tag lib_java_dbsmgd-tr1560-BP onto branch
*
* Revision 1.2.16.1  2005/08/02 21:14:17  mbw
* fixed javadocs error
*
* Revision 1.2  2004/02/25 21:01:44  mbw
* fixed to eliminate compiler warnings
*
* Revision 1.1  2003/12/30 16:38:49  mbw
* initial import into this product
*
* Revision 1.1  2003/12/30 16:28:29  mbw
* initial import into this product
*
* Revision 1.2  2003/12/01 13:13:24  jsb
* Updated to use ExpiringObjectCache, simplifying code
*
* Revision 1.1  2003/07/03 17:37:12  jsb
* initial addition for use by JSAM WI
*
* $Copyright$
*/
