package org.jax.mgi.shr.dto;

/*
* $Header$
* $Name$
*/

/**
* @module DTOConstants.java
* @author jsb
*/

/** contains common constants for use in constructing <tt>DTO</tt>s.
*  a set of static constants to use for fieldnames when building DTOs.
*    (no instances of this class are to be created)
* @has a set of static constants.
* @does simply provides public access to these constants.
*/
public class DTOConstants
{
    ///////////////
    // constructors
    ///////////////

    /** hidden default constructor.  We do not want any instances of this
    *    class to be created.
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    private DTOConstants()
    {}

    /* -------------------------------------------------------------------- */

    ///////////////////
    // common constants
    ///////////////////

    // markers -- constants dealing with marker information

    public static String MarkerKey = "markerKey";
    public static String MarkerSymbol = "symbol";
    public static String MarkerName = "name";
    public static String MarkerType = "markerType";
    public static String MarkerIsWithdrawn = "isWithdrawn";
    public static String MarkerWithdrawnTo = "withdrawnTo";

    public static String Synonyms = "synonyms";
    public static String Aliases = "aliases";

    public static String NomenEvents = "nomenEvents";
    public static String OldName = "oldName";
    public static String NewName = "newName";
    public static String OldSymbol = "oldSymbol";
    public static String Event = "event";
    public static String EventDate = "eventDate";

    public static String Markers = "markers";
    public static String OrthologCount = "orthologCount";

    public static String SuperFamilyKey = "superFamilyKey";
    public static String SuperFamilyName = "superFamilyName";
    public static String SnpCount = "snpCount";

    // alleles -- constants dealing with allele information

    public static String AlleleCounts = "alleleCounts";
    public static String AlleleTypeName = "alleleTypeName";
    public static String AlleleTypeKey = "alleleTypeKey";
    public static String AlleleTypeCount = "alleleTypeCount";
    public static String AlleleKey = "alleleKey";
    public static String AlleleSymbol = "alleleSymbol";
    public static String AlleleName = "alleleName";
    public static String AlleleType = "alleleType";
    public static String AlleleStatus = "alleleStatus";
    public static String InheritanceMode = "inheritanceMode";
    public static String StrainOfOrigin = "strainOfOrigin";
    public static String ESCellLine = "eSCellLine";
    public static String ESCellLineStrain = "eSCellLineStrain";
    public static String MutantESCellLine = "mutantESCellLine";
    public static String CellLineProvider = "cellLineProvider";
    public static String ProviderURL = "providerURL";
    public static String ApprovalDate = "approvalDate";
    public static String SubmittedBy = "submittedBy";
    public static String NomenNotes = "nomenNotes";
    public static String MolecularNotes = "molecularNotes";
    public static String MolecularJNums = "molecularJNums";
    public static String InIMSR = "inIMSR";
    public static String GenomeCoordinates = "genomeCoordinates";
    public static String Phenotypes = "phenotypes";
    public static String Mutations = "mutations";
    public static String QtlExpts = "qtlExpts";
    public static String DiseaseCount = "diseaseCount";
    public static String IsWildType = "isWildType";

    // mapping -- constants dealing with mapping information

    public static String Chromosome = "chromosome";
    public static String CytogeneticOffset = "cytogeneticOffset";
    public static String MappingCount = "mappingCount";
    public static String MapPosition = "offset";

    // phenotypes -- constants dealing with phenotype information

    public static String HasMLC = "hasMLC";
    public static String PhenoCount = "phenoCount";

    // orthologies -- constants dealing with orthology information

    public static String HasHumanOrthology = "hasHumanOrthology";
    public static String OrthologousSpecies = "orthologousSpecies";
    public static String OrthologKey = "orthologKey";
    public static String OrthologSymbol = "orthologSymbol";

    // polymorphisms -- constants dealing with polymorphism

    public static String PcrCount = "pcrCount";
    public static String RflpCount = "rflpCount";

    // references -- constants dealing with references

    public static String Authors = "authors";
    public static String Citation = "citation";
    public static String FirstReference = "firstRef";
    public static String Jnum = "jnum";
    public static String LastReference = "lastRef";
    public static String ReferenceCount = "refCount";
    public static String RefsKey = "refsKey";
    public static String RefsKeys = "refsKeys";
    public static String RefsTitle = "refsTitle";
    public static String References = "references";
    public static String RefID = "referenceID";

    // gene ontology -- constants dealing with GO data

    public static String GOAnnotationCount = "goCount";
    public static String GOAnnotations = "goAnnotations";
    public static String GOID = "goID";
    public static String GOTerm = "goTerm";

    // gene expression -- constants dealing with expression data

    public static String AntibodyCount = "antibodyCount";
    public static String AssayTypeKey = "assayTypeKey";
    public static String ExpressionAssayCount = "expressionAssayCount";
    public static String ExpressionAssayCounts = "expressionAssayCounts";
    public static String ExpressionResultCount = "expressionResultCount";
    public static String ExpressionResultCounts = "expressionResultCounts";
    public static String ExpressionTissues = "expressionTissues";
    public static String GXDIndexCount = "gxdIndexCount";
    public static String NegativeExpressionCount = "minusExpressionCount";
    public static String PositiveExpressionCount = "plusExpressionCount";
    public static String ProbeCounts = "probeCounts";
    public static String cDNACount = "cDNACount";
    public static String Stage = "stage";
    public static String StageKey = "stageKey";
    public static String Structure = "structure";
    public static String StructureKey = "structureKey";
    public static String TheilerStages = "theilerStages";
    public static String TissueCount = "tissueCount";
    public static String Tissue = "tissue";

    // probes -- constants dealing with probes and clones

    public static String ProbeName = "probeName";
    public static String ProbeKey = "probeKey";
    public static String Probes = "probes";
    public static String CloneCollection = "cloneCollection";
    public static String CloneID = "cloneID";
    public static String SegmentType = "segmentType";

    // accession IDs -- constants dealing with various types of accession IDs

    public static String AccID = "accID";
    public static String AccIDs = "accIDs";
    public static String NucleotideSeqIDs = "nucleotideSequences";
    public static String OtherIDs = "otherIDs";
    public static String OtherMGIIDs = "otherMgiIDs";
    public static String PrimaryAccID = "primaryID";
    public static String ProteinSeqIDs = "proteinSequences";
    public static String SeqID = "seqID";
    public static String SeqIDs = "seqIDs";
    public static String LogicalDbKey = "logicalDbKey";
    public static String InterProTerms = "interProTerms";
    public static String ActualDB = "actualDB";
    public static String ActualDBs = "actualDBs";
    public static String NumericPart = "numericPart";

    // Sequences -- constants dealing with sequence data

    public static String Sequences = "sequences";
    public static String SequenceKey = "sequenceKey";
    public static String SequenceLength = "length";
    public static String SequenceType = "seqType";
    public static String SequenceRecordDate = "sequenceRecordDate";
    public static String SequenceDate = "sequenceDate";
    public static String Strain = "strain";
    public static String RawStrain = "rawStrain";
    public static String SequenceProvider = "sequenceProvider";
    public static String SequenceStatus = "sequenceStatus";
    public static String SequenceVersion = "sequenceVersion";
    public static String SequenceDescription = "sequenceDescription";
    public static String Age = "age";
    public static String CellLine = "cellLine";
    public static String Gender = "gender";
    public static String Organism = "organism";
    public static String Library = "library";
    public static String LastAnnotationUpdate = "lastAnnotationUpdate";
    public static String LastSequenceUpdate = "lastSequenceUpdate";
    public static String SequenceCount = "sequenceCount";
    public static String RepDNASeq = "repDNASeq";
    public static String RepRNASeq = "repRNASeq";
    public static String RepProteinSeq = "repProteinSeq";


	// Assembly Coordinates -- constants for dealing with the assembly
	public static String IsAssembly = "isAssembly";
	public static String Strand = "strand";
	public static String StartCoord = "startCoord";
	public static String StopCoord = "stopCoord";

    // URLs -- constants dealing with URLs

    public static String GeneFamilyURL = "geneFamilyUrl";
    public static String MinimapURL = "minimapUrl";
    public static String URL = "url";

    // vocab -- constants dealing with standard vocabularies and annotations

    public static String IsNot = "isNot";
    public static String EvidenceCode = "evidenceCode";
    public static String InferredFrom = "inferredFrom";
    public static String TermKey = "_Term_key";
    public static String Term = "term";

    // images -- constants dealing with images

    public static String ImageKey = "_Image_key";
    public static String Width = "xDim";
    public static String Height = "yDim";
    public static String FigureLabel = "figureLabel";
    public static String ThumbnailImageKey = "_ThumbnailImage_key";
    public static String FullSizeImageKey = "_FullSizeImage_key";
    public static String ObjectKey = "_Object_key";
    public static String Genotypes = "genotypes";
    public static String Alleles = "alleles";
    public static String Images = "images";
    public static String AllelePrimaryImage = "allelePrimaryImage";

    // miscellaneous -- constants which fit none of the other sections

    public static String DatabaseVersion = "databaseVersion";
    public static String DatabaseDate = "databaseDate";
    public static String Notes = "notes";
    public static String SearchToolResults = "searchToolResults";
    public static String GlossaryKey = "glossaryKey";
    public static String GenotypeKey = "_Genotype_key";
    public static String AlleleCombinations = "alleleCombinations";
    public static String Diseases = "diseases";

    // MP Term related

    public static String MPId = "mPId";
    public static String MPTerm = "mPTerm";
    public static String GenotypeCount = "genotypeCount";

    // imsr  --	 constants used by the IMSR product

    public static String Chromosomes = "chromosomes";
    public static String StrainTypes = "strainTypes";

    // SNP related

    public static String ConsensusSnpKey = "_ConsensusSnp_key";
    public static String Orientation = "orientation";
    public static String AlleleSummary = "alleleSummary";
    public static String VariationClass = "variationClass";
    public static String IupacCode = "iupacCode";
    public static String Locations = "locations";
    public static String Strains = "strains";
    public static String StrainKey = "_Strain_key";
    public static String Allele = "alleleCall";
    public static String FxnAlleles = "functionClasses+Alleles";
    public static String IsConflict = "isConflict";
    public static String FunctionClass = "functionClass";
    public static String ContigAllele = "contigAllele";
    public static String Residue = "residue";
    public static String AssociatedGenes = "associatedGenes";
    public static String AminoAcidPosition = "aminoAcidPosition";
    public static String ReadingFrame = "readingFrame";
    public static String SubSnpKey = "_SubSnp_key";
    public static String IsExemplar = "isExemplar";
    public static String SubHandle = "submitterHandle";
    public static String SubSnps = "subSnps";
    public static String SubSnpOrder = "subSnpOrder";
    public static String Flank5Prime = "flank5'";
    public static String Flank3Prime = "flank3'";
    public static String Population = "population";
    public static String Populations = "populations";
    public static String Provider = "provider";
    public static String MapUnits = "mapUnits";
    public static String SubmitterID = "submitterID";
    public static String Version = "version";
}

/*
* $Log$
* Revision 1.3.6.1  2005/11/15 02:27:58  jw
* Adding missed files from merge
*
* Revision 1.2  2005/10/12 18:13:10  jsb
* lib_java_dbsmgd-3-4-0-0
*
* Revision 1.1.2.3  2005/10/11 16:09:29  dow
* Changes for PIRSF and SNP count links.
*
* Revision 1.1.2.2  2005/10/06 19:03:08  jsb
* added Populations and SubmitterID
*
* Revision 1.1.2.1  2005/10/04 14:09:13  jsb
* moved from core
*
* Revision 1.1.2.1  2005/10/03 20:10:58  jsb
* moved from dbsmgd product
*
* $Copyright$
*/
