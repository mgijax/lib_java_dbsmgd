package org.jax.mgi.shr.datafactory;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import org.jax.mgi.shr.stringutil.Sprintf;
import org.jax.mgi.shr.stringutil.StringLib;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dto.DTOConstants;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
* @module GlossaryFactory.java
* @author pf
*/

/**  The GlossaryFactory class contains many methods which encapsulate
*   knowledge of the MGI glossary.  They allow for  easy retrieval of
*   various aspects of glossary terms.
*
* @is a factory for retrieving information related to glossary terms.
* @has all data available for the MGI glossary
* @does retrieves glossary data for the glossary index or a specific term
*
*    Retrieval methods always return a new <tt>DTO</tt>.
*    Public methods include:  (parameters not listed here)
*    <OL>
*    <LI> getGlossaryIndex() -- return a mapping of glossary data for
*       glossaryIndex.jsp
*    <LI> getGlossaryTerm() -- find the displayed name and definition
        for a given term
*    </OL>
*/
public class GlossaryFactory
{

    /////////////////////
    // instance variables
    /////////////////////

    // provides logging capability for info, error, debug logs
    private Logger logger = null;

    // provides parameters needed to configure a MarkerFactory
    private DataFactoryCfg config = null;

    // reference to glossaryContainer singleton
    private GlossaryContainer glossaryContainer = null;

    // location of the glossary data file
    private String glossarySource = null;

    ///////////////
    // Constructors
    ///////////////

    /** constructor; instantiates and initializes a new GlossaryFactory.
    * @param config provides parameters needed to configure a GlossaryFactory
    * @param sqlDM provides access to a database (currently unused)
    * @param logger provides logging capability
    * @assumes GLOSSARY_SOURCE is define in config file
    * @effects nothing
    * @throws IOException, FileNotFoundException
    */
    public GlossaryFactory (DataFactoryCfg config,
                            SQLDataManager sqlDM,
                            Logger logger)
        throws IOException, FileNotFoundException
    {
        this.config             = config;
        this.logger             = logger;
        this.glossarySource     = this.config.get ("GLOSSARY_SOURCE");

        // retrieve reference to glossaryContainer singleton instance
        this.glossaryContainer  =
            GlossaryContainer.getGlossaryInstance(glossarySource);

        return;
    }

    /////////////////////////
    // Data Retrieval Methods
    /////////////////////////

    /** retrieve glossary data for the glossaryIndex.jsp
    * @param parms set of passed parameters; we check for the reload parm,
    *   and get another glossaryContainer as needed
    * @return DTO with term information, and how to link to glossary terms
    * @assumes nothing
    * @effects glossaryContainer
    * @throws IOException, FileNotFoundException
    */
    public DTO getGlossaryIndex (Map parms)
        throws IOException, FileNotFoundException
    {
        // if we have a reload parm, we need set the reload parameter
        // of the glossary singleton, and get another singleton reference.
        String reload = StringLib.getFirst((String[]) parms.get ("reload"));
        if (reload != null){
            if (reload.equals("true"))
            {
                this.glossaryContainer.setReloadNeeded();
                this.glossaryContainer =
                    GlossaryContainer.getGlossaryInstance(glossarySource);
            }
        }

        // since there are a limited number of DTOs, we need to claim a
        // DTO for use
        DTO glossaryDTO = DTO.getDTO();

        glossaryDTO.set
           (DTOConstants.GlossaryKey, glossaryContainer.getGlossaryMapping());

        return glossaryDTO;

    }

    /** retrieve glossary data (single mapping of requested key and a string
    * array containing displayName and definition for the glossaryTerm.jsp
    * @param parms set of passed parameters; we check for the reload parm,
    *   and get another glossaryContainer as needed
    * @return DTO with term information, and how to link to glossary terms
    * @assumes nothing
    * @effects glossaryContainer
    * @throws IOException, FileNotFoundException
    */
    public DTO getGlossaryTerm (Map parms)
        throws IOException, FileNotFoundException
    {
        TreeMap     glossaryMapping     = null;
        String[]    termInfo            = new String[2];

        // if we have a reload parm, we need set the reload parameter
        // of the glossary singleton, and get another singleton reference.
        String reload = StringLib.getFirst((String[]) parms.get ("reload"));
        if (reload != null){
            if (reload.equals("true"))
            {
                glossaryContainer.setReloadNeeded();
                glossaryContainer =
                    GlossaryContainer.getGlossaryInstance(glossarySource);
            }
        }

        // retrive the 'key' parameter value indicating the requested term
        String glossaryKey = StringLib.getFirst((String[]) parms.get ("key"));

        // sence there are a limited number of DTOs, we need to claim a
        // DTO for use
        DTO glossaryDTO = DTO.getDTO();

        // get the information for requested term, and load the DTO
        glossaryMapping = glossaryContainer.getGlossaryMapping();
        termInfo        = (String[])glossaryMapping.get(glossaryKey);
        glossaryDTO.set(DTOConstants.GlossaryKey, termInfo);

        return glossaryDTO;
    }
}
