//  $Header$
//  $Name$

package org.jax.mgi.dbs.mgd.dao;

/**
 * @is An object with attributes needed to create a new probe reference record
 *     in the PRB_Reference table.
 * @has
 *   <UL>
 *   <LI> Probe reference attributes
 *   </UL>
 * @does
 *   <UL>
 *   <LI> Provides get/set methods for its attribute(s).
 *   <LI> Provides a method to clear its attribute(s).
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class ProbeReferenceState
{
    /////////////////
    //  Variables  //
    /////////////////

    // Probe reference attributes.
    //
    private Integer probeKey = null;
    private Integer refsKey = null;
    private String holder = null;
    private String hasRmap = null;
    private String hasSequence = null;


    /**
     * Constructs a new ProbeReferenceState object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws Nothing
     */
    public ProbeReferenceState ()
    {
    }


    /**
     * Get the probeKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The probeKey attribute
     * @throws Nothing
     */
    public Integer getProbeKey () { return probeKey; }

    /**
     * Get the refsKey attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The refsKey attribute
     * @throws Nothing
     */
    public Integer getRefsKey () { return refsKey; }

    /**
     * Get the holder attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The holder attribute
     * @throws Nothing
     */
    public String getHolder () { return holder; }

    /**
     * Get the hasRmap attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The hasRmap attribute
     * @throws Nothing
     */
    public String getHasRmap () { return hasRmap; }

    /**
     * Get the hasSequence attribute from this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The hasSequence attribute
     * @throws Nothing
     */
    public String getHasSequence () { return hasSequence; }

    /**
     * Set the probeKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pProbeKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setProbeKey (Integer pProbeKey) { probeKey = pProbeKey; }

    /**
     * Set the refsKey attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pRefsKey value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setRefsKey (Integer pRefsKey) { refsKey = pRefsKey; }

    /**
     * Set the holder attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pHolder value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setHolder (String pHolder) { holder = pHolder; }

    /**
     * Set the hasRmap attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pHasRmap value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setHasRmap (String pHasRmap) { hasRmap = pHasRmap; }

    /**
     * Set the hasSequence attribute of this object to the given value.
     * @assumes Nothing
     * @effects Nothing
     * @param pHasSequence value to set the attribute to
     * @return Nothing
     * @throws Nothing
     */
    public void setHasSequence (String pHasSequence) { hasSequence = pHasSequence; }


    /**
     * Clear the attributes of this object.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return Nothing
     * @throws Nothing
     */
    public void clear ()
    {
        probeKey = null;
        refsKey = null;
        holder = null;
        hasRmap = null;
        hasSequence = null;
    }
}


//  $Log$
//  Revision 1.1  2003/09/18 13:37:22  dbm
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
