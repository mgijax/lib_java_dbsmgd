package org.jax.mgi.shr.datafactory;

/*
* $Header$
* $Name$
*/

/**
* @module DataFactoryCfg.java
* @author jsb
*/

/** provides configuration information needed for a DataFactory.
* @is a configurator for a DataFactory
* @has various attributes needed to configure data factories
* @does provides name-based lookups for those attributes
* @notes Each data factory must be able to gracefully handle the case where a
*    requested attribute is undefined (returns a null value).
*/
public interface DataFactoryCfg
{
    /* -------------------------------------------------------------------- */

    /** retrieve the attribute which corresponds to the given 'attributeName'.
    * @return String the attribute which corresponds to the given
    *    'attributeName', or returns null if there is no corresponding
    *    attribute
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    public String get (String attributeName);
}

/*
* $Log$
* Revision 1.1  2003/12/30 16:28:28  mbw
* initial import into this product
*
* Revision 1.1  2003/12/01 13:14:47  jsb
* Added per JSAM code review
*
* $Copyright$
*/
