package org.jax.mgi.shr.datafactory;

import java.util.Map;
import java.util.Vector;
import java.util.TreeMap;
import java.util.Iterator;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dto.DTOConstants;
import org.jax.mgi.shr.config.RcdFile;
import org.jax.mgi.shr.config.RcdFile.Rcd;
import java.io.IOException;
import java.io.FileNotFoundException;

//////////////////////////////////////////////////////////////////////////////
/**
* @module GlossaryContainer.java
* @author pf
*/

/**  The GlossaryContainer class provides a presistent, in-memory container
*   for MGI glossary data.  It contains the methods for accessing
*   the glossary.rcd data file, parsing of this file, and accessing local
*   storage variables containing the data.
*
* @is a singleton for holding glossary related information
* @has all glossary related data
* @does reads in a record file, stores the data locally,
*   and provides access methods to this data
*
*    Public methods include:  (parameters not listed here)
*    <OL>
*    <LI> getGlossaryInstance() -- get reference to singleton instance
*    <LI> reload() -- reload the glossary RCD file
*    <LI> getGlossaryMapping() -- return the glossary mapping
*    </OL>
*/
public class GlossaryContainer
{
    /////////////////
    //Singleton setup
    /////////////////

    // Create the ONLY instance of this class
    private static final GlossaryContainer _theInstance =
        new GlossaryContainer();

    // private constructor to avoid outside instantiation
    private GlossaryContainer(){}

    /////////////////////
    // class variables
    /////////////////////
    private static TreeMap  glossaryMapping     = new TreeMap();
    private static RcdFile  glossaryRcd         = null;
    private static String   RcdFilePath         = null;
    private static boolean  reloadNeeded        = true;

    /////////////////////////////
    // Singleton retrieval method
    /////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    /** retrieve a reference to the glossaryContainer singleton instance
    * @param cfgFilePath path to glossary rcd file
    * @return a reference to the glossaryContainer object
    * @assumes nothing
    * @effects may modify class variable "reloadNeeded"
    * @throws IOException, FileNotFoundException
    */
    public static GlossaryContainer getGlossaryInstance(String cfgFilePath)
        throws IOException, FileNotFoundException
    {
        // if this is the first call, or if rcd file path has changed,
        // we need to reload the glossary.rcd file
        if (cfgFilePath != RcdFilePath)
        {
            RcdFilePath  = cfgFilePath;
            setReloadNeeded();
        }

        if (reloadNeeded)
        {
            reload(RcdFilePath);
            reloadNeeded = false;
            System.out.println("Glossary Reloaded"); // to catalina.out
        }

        return _theInstance;
    }

    /////////////////////////
    // Data Retrieval Methods
    /////////////////////////

    //////////////////////////////////////////////////////////////////////////
    /** force reloading of the glossary rcd file
    * @assumes nothing
    * @effects nothing
    */
    public static void setReloadNeeded()
    {
        reloadNeeded = true;

        return;
    }

    //////////////////////////////////////////////////////////////////////////
    /** returns the glossary mapping
    * @return TreeMap representing
    * @assumes nothing
    * @effects nothing
    */
     public static TreeMap getGlossaryMapping()
    {
        return glossaryMapping;
    }


    /////////////////
    //private methods
    /////////////////

    //////////////////////////////////////////////////////////////////////////
    /** read the glossary.rcd file, and reloads the glossary mapping
    * @param string RcdFilePath; path to the glossary RCD file
    * @returns nothing
    * @assumes nothing
    * @effects reads from file system
    * @throws IOException, FileNotFoundException
    */
    private static void reload(String RcdFilePath)
        throws IOException, FileNotFoundException
    {
        glossaryRcd = new RcdFile (RcdFilePath, "key");

        makeGlossaryMapping();

        return;
    }

    //////////////////////////////////////////////////////////////////////////
    /** fills glossaryMapping {key:String[]} from the glossary RCD file
    * key = glossary rcd key
    * String[0] = displayName for the term
    * String[1] = definition for the term
    * @param nothing
    * @returns nothing
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    private static void makeGlossaryMapping()
    {
        Rcd         currentRecord       = null;
        String      currentKey          = "";
        Iterator    rcdKeyIterator      = null;

        // Clear existing glossary mapping
        glossaryMapping.clear();

        // for each record in the glossary.rcd file...
        rcdKeyIterator = glossaryRcd.keys();
        while (rcdKeyIterator.hasNext())
        {
            String[] termInfo = new String[2];

            // information retrieval and storage into glossary mapping
            currentKey      = (String) rcdKeyIterator.next();
            currentRecord   = glossaryRcd.get(currentKey);
            termInfo[0] = currentRecord.getString("displayName");
            termInfo[1] = currentRecord.getString("definition");
            glossaryMapping.put(currentKey, termInfo);
        }

        return;
    }

}

