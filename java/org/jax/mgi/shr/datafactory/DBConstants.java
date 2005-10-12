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
    public static final int LogicalDB_OMIM = 15;
    public static final int LogicalDB_RefSeq = 27;
    public static final int LogicalDB_SequenceDB = 9;
    public static final int LogicalDB_Interpro = 28;
    public static final int LogicalDB_TIGR = 35;
    public static final int LogicalDB_DoTS = 36;
    public static final int LogicalDB_TrEMBL = 41;
    public static final int LogicalDB_NIA = 53;
    public static final int LogicalDB_NCBI = 59;
    public static final int LogicalDB_Ensembl = 60;
    public static final int LogicalDB_PIRSF = 78;

    // from ACC_ActualDB

    public static final int ActualDB_RefSeq = 35;
    public static final int ActualDB_GenBank = 12;
    public static final int ActualDB_SwissProt = 20;

    // sources for MRK_Offset

    public static final int OffsetSource_MGI = 0;

    // from MRK_Species

    public static final int Species_Mouse = 1;
    public static final int Species_Human = 2;
    public static final int Species_Rat = 40;

    // from ACC_MGIType

    public static final int MGIType_Reference = 1;
    public static final int MGIType_Marker = 2;
    public static final int MGIType_Probe = 3;
    public static final int MGIType_MappingExpt = 4;
    public static final int MGIType_Antibody = 6;
    public static final int MGIType_Antigen = 7;
    public static final int MGIType_Assay = 8;
    public static final int MGIType_Image = 9;
    public static final int MGIType_Allele = 11;
    public static final int MGIType_Genotype = 12;
    public static final int MGIType_VocTerm = 13;
    public static final int MGIType_Sequence = 19;
    public static final int MGIType_ConsensusSnp = 30;
    public static final int MGIType_SubSnp = 31;

    // from MRK_Status

    public static final int Marker_Approved = 1;
    public static final int Marker_Withdrawn = 2;
    public static final int Marker_Interim = 3;

    // from BIB_ReviewStatus

    public static final int ReviewStatus_MGI = 2;

    // from ALL_Status
    // For reasons that are unclear to me, this is changing back and forth...
    //public static final int Allele_Approved = 765354;
    public static final String Allele_Approved = "Approved";


    // from MGI_NoteType
    public static final int MGINoteType_AlleleGeneral = 1020;
    public static final int MGINoteType_AlleleMolecular = 1021;
    public static final int MGINoteType_Nomenclature = 1022;

    // from VOC_AnnotType
    public static final int VOCAnnotType_MP = 1002;
    public static final int VOCAnnotType_OMIM = 1005;
    public static final int VOCAnnotType_PIRSF = 1007;

    // from MGI_RefAssocType
    public static final int MGIRefAssocType_ALLOriginal = 1011;

    // from SEQ_Marker_Cache - Qualifier
    public static final int SEQMarker_Polypeptide = 615421;
}

/*
* $Log$
* Revision 1.10.6.3  2005/10/04 19:34:28  dow
* Changes for PIRSF Detail page.
*
* Revision 1.10.6.2  2005/10/03 20:10:09  jsb
* added SNPs
*
* Revision 1.10.6.1  2005/09/28 16:46:53  dow
* Changes for PRISF Vocab Browser capability.
*
* Revision 1.10  2005/08/25 15:28:00  dow
* lib_java_dbsmgd-3-3-0-0
*
* Revision 1.9  2005/08/05 17:49:14  mbw
* merged code from branch lib_java_dbsmgd-tr6046-1
*
* Revision 1.8.4.1  2005/07/15 20:31:57  dow
* Changes and additions for OMIM Release.
*
* Revision 1.8  2005/04/19 19:27:20  dow
* Added constant for MGI_RefAssocType
*
* Revision 1.5.10.1  2005/08/02 21:33:23  mbw
* merged tag lib_java_dbsmgd-tr1560-BP onto branch
*
* Revision 1.5.6.1  2005/04/10 16:27:45  dow
* Additions for mpr release.
*
* Revision 1.5  2004/10/21 17:50:39  jw
* Changes for the 3.1 assembly release
*
* Revision 1.4  2004/05/24 15:15:05  jsb
* added Marker_Interim status
*
* Revision 1.3  2004/04/06 16:50:59  jsb
* added LogicalDB_NIA
*
* Revision 1.2  2004/02/10 17:48:50  jsb
* Added more MGIType definitions
*
* Revision 1.1  2003/12/30 16:38:48  mbw
* initial import into this product
*
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
