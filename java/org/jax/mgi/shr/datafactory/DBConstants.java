package org.jax.mgi.shr.datafactory;

/*
* $Header$
* $Name$
*/

/**
* @module DBConstants.java
* @author jsb
*/

/** gives names to the keys for common database entries, to make code more
*    readable and maintainable.
*/
public class DBConstants
{
    // from ACC_LogicalDB

    public static final int LogicalDB_MGI = 1;
    public static final int LogicalDB_SwissProt = 13;
    public static final int LogicalDB_RefSeq = 27;
    public static final int LogicalDB_SequenceDB = 9;
    public static final int LogicalDB_Interpro = 28;
    public static final int LogicalDB_TIGR = 35;
    public static final int LogicalDB_DoTS = 36;
    public static final int LogicalDB_TrEMBL = 41;

    // from ACC_ActualDB

    public static final int ActualDB_RefSeq = 35;
    public static final int ActualDB_GenBank = 12;
    public static final int ActualDB_SwissProt = 20;

    // sources for MRK_Offset

    public static final int OffsetSource_MGI = 0;

    // from MRK_Species

    public static final int Species_Mouse = 1;
    public static final int Species_Human = 2;

    // from ACC_MGIType

    public static final int MGIType_Reference = 1;
    public static final int MGIType_Marker = 2;
    public static final int MGIType_VocTerm = 13;

    // from MRK_Status

    public static final int Marker_Approved = 1;
    public static final int Marker_Withdrawn = 2;

    // from BIB_ReviewStatus

    public static final int ReviewStatus_MGI = 2;
}

/*
* $Log$
* Revision 1.1  2003/12/30 16:28:27  mbw
* initial import into this product
*
* Revision 1.2  2003/12/01 13:12:27  jsb
* Changed ActualDB strings to use keys, per code review
*
* Revision 1.1  2003/07/03 17:37:11  jsb
* initial addition for use by JSAM WI
*
* $Copyright$
*/
