package org.jax.mgi.dbs.mgd;

public class MGD
{
	public class acc_accession
	{
		public static final String _name = "ACC_Accession";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String privateJ = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class acc_accessionmax
	{
		public static final String _name = "ACC_AccessionMax";
		public static final String prefixpart = "prefixPart";
		public static final String maxnumericpart = "maxNumericPart";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class acc_accessionreference
	{
		public static final String _name = "ACC_AccessionReference";
		public static final String _accession_key = "_Accession_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class acc_actualdb
	{
		public static final String _name = "ACC_ActualDB";
		public static final String _actualdb_key = "_ActualDB_key";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String name = "name";
		public static final String active = "active";
		public static final String url = "url";
		public static final String allowsmultiple = "allowsMultiple";
		public static final String delimiter = "delimiter";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class acc_logicaldb
	{
		public static final String _name = "ACC_LogicalDB";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String name = "name";
		public static final String description = "description";
		public static final String _organism_key = "_Organism_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class acc_mgitype
	{
		public static final String _name = "ACC_MGIType";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String name = "name";
		public static final String tablename = "tableName";
		public static final String primarykeyname = "primaryKeyName";
		public static final String identitycolumnname = "identityColumnName";
		public static final String dbview = "dbView";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class all_allele
	{
		public static final String _name = "ALL_Allele";
		public static final String _allele_key = "_Allele_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _strain_key = "_Strain_key";
		public static final String _mode_key = "_Mode_key";
		public static final String _allele_type_key = "_Allele_Type_key";
		public static final String _cellline_key = "_CellLine_key";
		public static final String _allele_status_key = "_Allele_Status_key";
		public static final String symbol = "symbol";
		public static final String name = "name";
		public static final String nomensymbol = "nomenSymbol";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String _approvedby_key = "_ApprovedBy_key";
		public static final String approval_date = "approval_date";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class all_allele_mutation
	{
		public static final String _name = "ALL_Allele_Mutation";
		public static final String _allele_key = "_Allele_key";
		public static final String _mutation_key = "_Mutation_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class all_cellline
	{
		public static final String _name = "ALL_CellLine";
		public static final String _cellline_key = "_CellLine_key";
		public static final String cellline = "cellLine";
		public static final String _strain_key = "_Strain_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class all_inheritance_mode
	{
		public static final String _name = "ALL_Inheritance_Mode";
		public static final String _mode_key = "_Mode_key";
		public static final String mode = "mode";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class all_molecular_mutation
	{
		public static final String _name = "ALL_Molecular_Mutation";
		public static final String _mutation_key = "_Mutation_key";
		public static final String mutation = "mutation";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class all_note
	{
		public static final String _name = "ALL_Note";
		public static final String _allele_key = "_Allele_key";
		public static final String _notetype_key = "_NoteType_key";
		public static final String sequencenum = "sequenceNum";
		public static final String privateJ = "private";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class all_notetype
	{
		public static final String _name = "ALL_NoteType";
		public static final String _notetype_key = "_NoteType_key";
		public static final String notetype = "noteType";
		public static final String privateJ = "private";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class all_reference
	{
		public static final String _name = "ALL_Reference";
		public static final String _allele_key = "_Allele_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _refstype_key = "_RefsType_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class all_referencetype
	{
		public static final String _name = "ALL_ReferenceType";
		public static final String _refstype_key = "_RefsType_key";
		public static final String referencetype = "referenceType";
		public static final String allowonlyone = "allowOnlyOne";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class all_status
	{
		public static final String _name = "ALL_Status";
		public static final String _allele_status_key = "_Allele_Status_key";
		public static final String status = "status";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class all_synonym
	{
		public static final String _name = "ALL_Synonym";
		public static final String _synonym_key = "_Synonym_key";
		public static final String _allele_key = "_Allele_key";
		public static final String _refs_key = "_Refs_key";
		public static final String synonym = "synonym";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class all_type
	{
		public static final String _name = "ALL_Type";
		public static final String _allele_type_key = "_Allele_Type_key";
		public static final String alleletype = "alleleType";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class bib_books
	{
		public static final String _name = "BIB_Books";
		public static final String _refs_key = "_Refs_key";
		public static final String book_au = "book_au";
		public static final String book_title = "book_title";
		public static final String place = "place";
		public static final String publisher = "publisher";
		public static final String series_ed = "series_ed";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class bib_notes
	{
		public static final String _name = "BIB_Notes";
		public static final String _refs_key = "_Refs_key";
		public static final String sequencenum = "sequenceNum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class bib_refs
	{
		public static final String _name = "BIB_Refs";
		public static final String _refs_key = "_Refs_key";
		public static final String _reviewstatus_key = "_ReviewStatus_key";
		public static final String reftype = "refType";
		public static final String authors = "authors";
		public static final String authors2 = "authors2";
		public static final String _primary = "_primary";
		public static final String title = "title";
		public static final String title2 = "title2";
		public static final String journal = "journal";
		public static final String vol = "vol";
		public static final String issue = "issue";
		public static final String date = "date";
		public static final String year = "year";
		public static final String pgs = "pgs";
		public static final String dbs = "dbs";
		public static final String nlmstatus = "NLMstatus";
		public static final String abstract = "abstract";
		public static final String isreviewarticle = "isReviewArticle";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class bib_reviewstatus
	{
		public static final String _name = "BIB_ReviewStatus";
		public static final String _reviewstatus_key = "_ReviewStatus_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class crs_cross
	{
		public static final String _name = "CRS_Cross";
		public static final String _cross_key = "_Cross_key";
		public static final String type = "type";
		public static final String _femalestrain_key = "_femaleStrain_key";
		public static final String femaleallele1 = "femaleAllele1";
		public static final String femaleallele2 = "femaleAllele2";
		public static final String _malestrain_key = "_maleStrain_key";
		public static final String maleallele1 = "maleAllele1";
		public static final String maleallele2 = "maleAllele2";
		public static final String abbrevho = "abbrevHO";
		public static final String _strainho_key = "_StrainHO_key";
		public static final String abbrevht = "abbrevHT";
		public static final String _strainht_key = "_StrainHT_key";
		public static final String whosecross = "whoseCross";
		public static final String allelefromsegparent = "alleleFromSegParent";
		public static final String f1directionknown = "F1DirectionKnown";
		public static final String nprogeny = "nProgeny";
		public static final String displayed = "displayed";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class crs_matrix
	{
		public static final String _name = "CRS_Matrix";
		public static final String _cross_key = "_Cross_key";
		public static final String _marker_key = "_Marker_key";
		public static final String othersymbol = "otherSymbol";
		public static final String chromosome = "chromosome";
		public static final String rownumber = "rowNumber";
		public static final String notes = "notes";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class crs_progeny
	{
		public static final String _name = "CRS_Progeny";
		public static final String _cross_key = "_Cross_key";
		public static final String sequencenum = "sequenceNum";
		public static final String name = "name";
		public static final String sex = "sex";
		public static final String notes = "notes";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class crs_references
	{
		public static final String _name = "CRS_References";
		public static final String _cross_key = "_Cross_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _refs_key = "_Refs_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class crs_typings
	{
		public static final String _name = "CRS_Typings";
		public static final String _cross_key = "_Cross_key";
		public static final String rownumber = "rowNumber";
		public static final String colnumber = "colNumber";
		public static final String data = "data";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class dag_closure
	{
		public static final String _name = "DAG_Closure";
		public static final String _dag_key = "_DAG_key";
		public static final String _ancestor_key = "_Ancestor_key";
		public static final String _descendent_key = "_Descendent_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class dag_dag
	{
		public static final String _name = "DAG_DAG";
		public static final String _dag_key = "_DAG_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String name = "name";
		public static final String abbreviation = "abbreviation";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class dag_edge
	{
		public static final String _name = "DAG_Edge";
		public static final String _edge_key = "_Edge_key";
		public static final String _dag_key = "_DAG_key";
		public static final String _parent_key = "_Parent_key";
		public static final String _child_key = "_Child_key";
		public static final String _label_key = "_Label_key";
		public static final String sequencenum = "sequenceNum";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class dag_label
	{
		public static final String _name = "DAG_Label";
		public static final String _label_key = "_Label_key";
		public static final String label = "label";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class dag_node
	{
		public static final String _name = "DAG_Node";
		public static final String _node_key = "_Node_key";
		public static final String _dag_key = "_DAG_key";
		public static final String _object_key = "_Object_key";
		public static final String _label_key = "_Label_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_allelegenotype
	{
		public static final String _name = "GXD_AlleleGenotype";
		public static final String _genotype_key = "_Genotype_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _allele_key = "_Allele_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_allelepair
	{
		public static final String _name = "GXD_AllelePair";
		public static final String _allelepair_key = "_AllelePair_key";
		public static final String _genotype_key = "_Genotype_key";
		public static final String sequencenum = "sequenceNum";
		public static final String _allele_key_1 = "_Allele_key_1";
		public static final String _allele_key_2 = "_Allele_key_2";
		public static final String _marker_key = "_Marker_key";
		public static final String isunknown = "isUnknown";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_antibody
	{
		public static final String _name = "GXD_Antibody";
		public static final String _antibody_key = "_Antibody_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _antibodyclass_key = "_AntibodyClass_key";
		public static final String _antibodytype_key = "_AntibodyType_key";
		public static final String _organism_key = "_Organism_key";
		public static final String _antigen_key = "_Antigen_key";
		public static final String antibodyname = "antibodyName";
		public static final String antibodynote = "antibodyNote";
		public static final String recogwestern = "recogWestern";
		public static final String recogimmunprecip = "recogImmunPrecip";
		public static final String recognote = "recogNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_antibodyalias
	{
		public static final String _name = "GXD_AntibodyAlias";
		public static final String _antibodyalias_key = "_AntibodyAlias_key";
		public static final String _antibody_key = "_Antibody_key";
		public static final String _refs_key = "_Refs_key";
		public static final String alias = "alias";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_antibodyclass
	{
		public static final String _name = "GXD_AntibodyClass";
		public static final String _antibodyclass_key = "_AntibodyClass_key";
		public static final String class = "class";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_antibodymarker
	{
		public static final String _name = "GXD_AntibodyMarker";
		public static final String _antibody_key = "_Antibody_key";
		public static final String _marker_key = "_Marker_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_antibodyprep
	{
		public static final String _name = "GXD_AntibodyPrep";
		public static final String _antibodyprep_key = "_AntibodyPrep_key";
		public static final String _antibody_key = "_Antibody_key";
		public static final String _secondary_key = "_Secondary_key";
		public static final String _label_key = "_Label_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_antibodytype
	{
		public static final String _name = "GXD_AntibodyType";
		public static final String _antibodytype_key = "_AntibodyType_key";
		public static final String antibodytype = "antibodyType";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_antigen
	{
		public static final String _name = "GXD_Antigen";
		public static final String _antigen_key = "_Antigen_key";
		public static final String _source_key = "_Source_key";
		public static final String antigenname = "antigenName";
		public static final String regioncovered = "regionCovered";
		public static final String antigennote = "antigenNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_assay
	{
		public static final String _name = "GXD_Assay";
		public static final String _assay_key = "_Assay_key";
		public static final String _assaytype_key = "_AssayType_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _probeprep_key = "_ProbePrep_key";
		public static final String _antibodyprep_key = "_AntibodyPrep_key";
		public static final String _imagepane_key = "_ImagePane_key";
		public static final String _reportergene_key = "_ReporterGene_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_assaynote
	{
		public static final String _name = "GXD_AssayNote";
		public static final String _assay_key = "_Assay_key";
		public static final String sequencenum = "sequenceNum";
		public static final String assaynote = "assayNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_assaytype
	{
		public static final String _name = "GXD_AssayType";
		public static final String _assaytype_key = "_AssayType_key";
		public static final String assaytype = "assayType";
		public static final String isrnaassay = "isRNAAssay";
		public static final String isgelassay = "isGelAssay";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_embeddingmethod
	{
		public static final String _name = "GXD_EmbeddingMethod";
		public static final String _embedding_key = "_Embedding_key";
		public static final String embeddingmethod = "embeddingMethod";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_expression
	{
		public static final String _name = "GXD_Expression";
		public static final String _expression_key = "_Expression_key";
		public static final String _assay_key = "_Assay_key";
		public static final String _assaytype_key = "_AssayType_key";
		public static final String _genotype_key = "_Genotype_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _structure_key = "_Structure_key";
		public static final String expressed = "expressed";
		public static final String age = "age";
		public static final String agemin = "ageMin";
		public static final String agemax = "ageMax";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_fixationmethod
	{
		public static final String _name = "GXD_FixationMethod";
		public static final String _fixation_key = "_Fixation_key";
		public static final String fixation = "fixation";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_gelband
	{
		public static final String _name = "GXD_GelBand";
		public static final String _gelband_key = "_GelBand_key";
		public static final String _gellane_key = "_GelLane_key";
		public static final String _gelrow_key = "_GelRow_key";
		public static final String _strength_key = "_Strength_key";
		public static final String bandnote = "bandNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_gelcontrol
	{
		public static final String _name = "GXD_GelControl";
		public static final String _gelcontrol_key = "_GelControl_key";
		public static final String gellanecontent = "gelLaneContent";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_gellane
	{
		public static final String _name = "GXD_GelLane";
		public static final String _gellane_key = "_GelLane_key";
		public static final String _assay_key = "_Assay_key";
		public static final String _genotype_key = "_Genotype_key";
		public static final String _gelrnatype_key = "_GelRNAType_key";
		public static final String _gelcontrol_key = "_GelControl_key";
		public static final String sequencenum = "sequenceNum";
		public static final String lanelabel = "laneLabel";
		public static final String sampleamount = "sampleAmount";
		public static final String sex = "sex";
		public static final String age = "age";
		public static final String agemin = "ageMin";
		public static final String agemax = "ageMax";
		public static final String agenote = "ageNote";
		public static final String lanenote = "laneNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_gellanestructure
	{
		public static final String _name = "GXD_GelLaneStructure";
		public static final String _gellane_key = "_GelLane_key";
		public static final String _structure_key = "_Structure_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_gelrnatype
	{
		public static final String _name = "GXD_GelRNAType";
		public static final String _gelrnatype_key = "_GelRNAType_key";
		public static final String rnatype = "rnaType";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_gelrow
	{
		public static final String _name = "GXD_GelRow";
		public static final String _gelrow_key = "_GelRow_key";
		public static final String _assay_key = "_Assay_key";
		public static final String _gelunits_key = "_GelUnits_key";
		public static final String sequencenum = "sequenceNum";
		public static final String size = "size";
		public static final String rownote = "rowNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_gelunits
	{
		public static final String _name = "GXD_GelUnits";
		public static final String _gelunits_key = "_GelUnits_key";
		public static final String units = "units";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_genotype
	{
		public static final String _name = "GXD_Genotype";
		public static final String _genotype_key = "_Genotype_key";
		public static final String _strain_key = "_Strain_key";
		public static final String isconditional = "isConditional";
		public static final String note = "note";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_index
	{
		public static final String _name = "GXD_Index";
		public static final String _index_key = "_Index_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _priority_key = "_Priority_key";
		public static final String comments = "comments";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_index_stages
	{
		public static final String _name = "GXD_Index_Stages";
		public static final String _index_key = "_Index_key";
		public static final String _indexassay_key = "_IndexAssay_key";
		public static final String _stageid_key = "_StageID_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_insituresult
	{
		public static final String _name = "GXD_InSituResult";
		public static final String _result_key = "_Result_key";
		public static final String _specimen_key = "_Specimen_key";
		public static final String _strength_key = "_Strength_key";
		public static final String _pattern_key = "_Pattern_key";
		public static final String sequencenum = "sequenceNum";
		public static final String resultnote = "resultNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_insituresultimage
	{
		public static final String _name = "GXD_InSituResultImage";
		public static final String _result_key = "_Result_key";
		public static final String _imagepane_key = "_ImagePane_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_isresultstructure
	{
		public static final String _name = "GXD_ISResultStructure";
		public static final String _result_key = "_Result_key";
		public static final String _structure_key = "_Structure_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_label
	{
		public static final String _name = "GXD_Label";
		public static final String _label_key = "_Label_key";
		public static final String label = "label";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_labelcoverage
	{
		public static final String _name = "GXD_LabelCoverage";
		public static final String _coverage_key = "_Coverage_key";
		public static final String coverage = "coverage";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_pattern
	{
		public static final String _name = "GXD_Pattern";
		public static final String _pattern_key = "_Pattern_key";
		public static final String pattern = "pattern";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_probeprep
	{
		public static final String _name = "GXD_ProbePrep";
		public static final String _probeprep_key = "_ProbePrep_key";
		public static final String _probe_key = "_Probe_key";
		public static final String _sense_key = "_Sense_key";
		public static final String _label_key = "_Label_key";
		public static final String _coverage_key = "_Coverage_key";
		public static final String _visualization_key = "_Visualization_key";
		public static final String type = "type";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_probesense
	{
		public static final String _name = "GXD_ProbeSense";
		public static final String _sense_key = "_Sense_key";
		public static final String sense = "sense";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_secondary
	{
		public static final String _name = "GXD_Secondary";
		public static final String _secondary_key = "_Secondary_key";
		public static final String secondary = "secondary";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_specimen
	{
		public static final String _name = "GXD_Specimen";
		public static final String _specimen_key = "_Specimen_key";
		public static final String _assay_key = "_Assay_key";
		public static final String _embedding_key = "_Embedding_key";
		public static final String _fixation_key = "_Fixation_key";
		public static final String _genotype_key = "_Genotype_key";
		public static final String sequencenum = "sequenceNum";
		public static final String specimenlabel = "specimenLabel";
		public static final String sex = "sex";
		public static final String age = "age";
		public static final String agemin = "ageMin";
		public static final String agemax = "ageMax";
		public static final String agenote = "ageNote";
		public static final String hybridization = "hybridization";
		public static final String specimennote = "specimenNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_strength
	{
		public static final String _name = "GXD_Strength";
		public static final String _strength_key = "_Strength_key";
		public static final String strength = "strength";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_structure
	{
		public static final String _name = "GXD_Structure";
		public static final String _structure_key = "_Structure_key";
		public static final String _parent_key = "_Parent_key";
		public static final String _structurename_key = "_StructureName_key";
		public static final String _stage_key = "_Stage_key";
		public static final String edinburghkey = "edinburghKey";
		public static final String printname = "printName";
		public static final String treedepth = "treeDepth";
		public static final String printstop = "printStop";
		public static final String toposort = "topoSort";
		public static final String structurenote = "structureNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_structureclosure
	{
		public static final String _name = "GXD_StructureClosure";
		public static final String _structure_key = "_Structure_key";
		public static final String _descendent_key = "_Descendent_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_structurename
	{
		public static final String _name = "GXD_StructureName";
		public static final String _structurename_key = "_StructureName_key";
		public static final String _structure_key = "_Structure_key";
		public static final String structure = "structure";
		public static final String mgiadded = "mgiAdded";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_theilerstage
	{
		public static final String _name = "GXD_TheilerStage";
		public static final String _stage_key = "_Stage_key";
		public static final String stage = "stage";
		public static final String description = "description";
		public static final String dpcmin = "dpcMin";
		public static final String dpcmax = "dpcMax";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_visualizationmethod
	{
		public static final String _name = "GXD_VisualizationMethod";
		public static final String _visualization_key = "_Visualization_key";
		public static final String visualization = "visualization";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class hmd_assay
	{
		public static final String _name = "HMD_Assay";
		public static final String _assay_key = "_Assay_key";
		public static final String assay = "assay";
		public static final String abbrev = "abbrev";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class hmd_class
	{
		public static final String _name = "HMD_Class";
		public static final String _class_key = "_Class_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class hmd_homology
	{
		public static final String _name = "HMD_Homology";
		public static final String _homology_key = "_Homology_key";
		public static final String _class_key = "_Class_key";
		public static final String _refs_key = "_Refs_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class hmd_homology_assay
	{
		public static final String _name = "HMD_Homology_Assay";
		public static final String _homology_key = "_Homology_key";
		public static final String _assay_key = "_Assay_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class hmd_homology_marker
	{
		public static final String _name = "HMD_Homology_Marker";
		public static final String _homology_key = "_Homology_key";
		public static final String _marker_key = "_Marker_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class hmd_notes
	{
		public static final String _name = "HMD_Notes";
		public static final String _homology_key = "_Homology_key";
		public static final String sequencenum = "sequenceNum";
		public static final String notes = "notes";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class img_fieldtype
	{
		public static final String _name = "IMG_FieldType";
		public static final String _fieldtype_key = "_FieldType_key";
		public static final String fieldtype = "fieldType";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class img_image
	{
		public static final String _name = "IMG_Image";
		public static final String _image_key = "_Image_key";
		public static final String _refs_key = "_Refs_key";
		public static final String xdim = "xDim";
		public static final String ydim = "yDim";
		public static final String figurelabel = "figureLabel";
		public static final String copyrightnote = "copyrightNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class img_imagenote
	{
		public static final String _name = "IMG_ImageNote";
		public static final String _image_key = "_Image_key";
		public static final String sequencenum = "sequenceNum";
		public static final String imagenote = "imageNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class img_imagepane
	{
		public static final String _name = "IMG_ImagePane";
		public static final String _imagepane_key = "_ImagePane_key";
		public static final String _image_key = "_Image_key";
		public static final String _fieldtype_key = "_FieldType_key";
		public static final String panelabel = "paneLabel";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_attributehistory
	{
		public static final String _name = "MGI_AttributeHistory";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String columnname = "columnName";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_columns
	{
		public static final String _name = "MGI_Columns";
		public static final String table_name = "table_name";
		public static final String column_name = "column_name";
		public static final String description = "description";
		public static final String example = "example";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_dbinfo
	{
		public static final String _name = "MGI_dbinfo";
		public static final String public_version = "public_version";
		public static final String product_name = "product_name";
		public static final String schema_version = "schema_version";
		public static final String lastdump_date = "lastdump_date";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_deletedobject
	{
		public static final String _name = "MGI_DeletedObject";
		public static final String _deletedobject_key = "_DeletedObject_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String deletion_date = "deletion_date";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_fantom2
	{
		public static final String _name = "MGI_Fantom2";
		public static final String _fantom2_key = "_Fantom2_key";
		public static final String riken_seqid = "riken_seqid";
		public static final String riken_cloneid = "riken_cloneid";
		public static final String riken_locusid = "riken_locusid";
		public static final String riken_cluster = "riken_cluster";
		public static final String final_cluster = "final_cluster";
		public static final String genbank_id = "genbank_id";
		public static final String fantom1_clone = "fantom1_clone";
		public static final String fantom2_clone = "fantom2_clone";
		public static final String tiger_tc = "tiger_tc";
		public static final String unigene_id = "unigene_id";
		public static final String seq_length = "seq_length";
		public static final String seq_note = "seq_note";
		public static final String seq_quality = "seq_quality";
		public static final String riken_locusstatus = "riken_locusStatus";
		public static final String mgi_statuscode = "mgi_statusCode";
		public static final String mgi_numbercode = "mgi_numberCode";
		public static final String riken_numbercode = "riken_numberCode";
		public static final String cds_category = "cds_category";
		public static final String cluster_analysis = "cluster_analysis";
		public static final String gene_name_curation = "gene_name_curation";
		public static final String cds_go_curation = "cds_go_curation";
		public static final String blast_groupid = "blast_groupID";
		public static final String blast_mgiids = "blast_mgiIDs";
		public static final String final_mgiid = "final_mgiID";
		public static final String final_symbol1 = "final_symbol1";
		public static final String final_name1 = "final_name1";
		public static final String final_symbol2 = "final_symbol2";
		public static final String final_name2 = "final_name2";
		public static final String nomen_event = "nomen_event";
		public static final String cluster_evidence = "cluster_evidence";
		public static final String load_mgiid = "load_mgiid";
		public static final String nonmgi_rep = "nonmgi_rep";
		public static final String approved_symbol = "approved_symbol";
		public static final String approved_name = "approved_name";
		public static final String chromosome = "chromosome";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_fantom2cache
	{
		public static final String _name = "MGI_Fantom2Cache";
		public static final String _fantom2_key = "_Fantom2_key";
		public static final String gba_mgiid = "gba_mgiID";
		public static final String gba_symbol = "gba_symbol";
		public static final String gba_name = "gba_name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_fantom2notes
	{
		public static final String _name = "MGI_Fantom2Notes";
		public static final String _fantom2_key = "_Fantom2_key";
		public static final String notetype = "noteType";
		public static final String sequencenum = "sequenceNum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_note
	{
		public static final String _name = "MGI_Note";
		public static final String _note_key = "_Note_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _notetype_key = "_NoteType_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_notechunk
	{
		public static final String _name = "MGI_NoteChunk";
		public static final String _note_key = "_Note_key";
		public static final String sequencenum = "sequenceNum";
		public static final String note = "note";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_notetype
	{
		public static final String _name = "MGI_NoteType";
		public static final String _notetype_key = "_NoteType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String notetype = "noteType";
		public static final String private = "private";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_organism
	{
		public static final String _name = "MGI_Organism";
		public static final String _organism_key = "_Organism_key";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_organism_mgitype
	{
		public static final String _name = "MGI_Organism_MGIType";
		public static final String _organism_key = "_Organism_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_refassoctype
	{
		public static final String _name = "MGI_RefAssocType";
		public static final String _refassoctype_key = "_RefAssocType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String assoctype = "assocType";
		public static final String allowonlyone = "allowOnlyOne";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_reference_assoc
	{
		public static final String _name = "MGI_Reference_Assoc";
		public static final String _assoc_key = "_Assoc_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _refassoctype_key = "_RefAssocType_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_set
	{
		public static final String _name = "MGI_Set";
		public static final String _set_key = "_Set_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String name = "name";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_setmember
	{
		public static final String _name = "MGI_SetMember";
		public static final String _setmember_key = "_SetMember_key";
		public static final String _set_key = "_Set_key";
		public static final String _object_key = "_Object_key";
		public static final String sequencenum = "sequenceNum";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_tables
	{
		public static final String _name = "MGI_Tables";
		public static final String table_name = "table_name";
		public static final String description = "description";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_translation
	{
		public static final String _name = "MGI_Translation";
		public static final String _translation_key = "_Translation_key";
		public static final String _translationtype_key = "_TranslationType_key";
		public static final String _object_key = "_Object_key";
		public static final String badname = "badName";
		public static final String sequencenum = "sequenceNum";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_translationtype
	{
		public static final String _name = "MGI_TranslationType";
		public static final String _translationtype_key = "_TranslationType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String translationtype = "translationType";
		public static final String compressionchars = "compressionChars";
		public static final String regularexpression = "regularExpression";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_user
	{
		public static final String _name = "MGI_User";
		public static final String _user_key = "_User_key";
		public static final String _usertype_key = "_UserType_key";
		public static final String _userstatus_key = "_UserStatus_key";
		public static final String login = "login";
		public static final String name = "name";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mlc_lock
	{
		public static final String _name = "MLC_Lock";
		public static final String time = "time";
		public static final String _marker_key = "_Marker_key";
		public static final String checkedout = "checkedOut";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mlc_marker
	{
		public static final String _name = "MLC_Marker";
		public static final String _marker_key = "_Marker_key";
		public static final String tag = "tag";
		public static final String _marker_key_2 = "_Marker_key_2";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mlc_reference
	{
		public static final String _name = "MLC_Reference";
		public static final String _marker_key = "_Marker_key";
		public static final String _refs_key = "_Refs_key";
		public static final String tag = "tag";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mlc_text
	{
		public static final String _name = "MLC_Text";
		public static final String _marker_key = "_Marker_key";
		public static final String mode = "mode";
		public static final String description = "description";
		public static final String userid = "userID";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_assay_types
	{
		public static final String _name = "MLD_Assay_Types";
		public static final String _assay_type_key = "_Assay_Type_key";
		public static final String description = "description";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_concordance
	{
		public static final String _name = "MLD_Concordance";
		public static final String _expt_key = "_Expt_key";
		public static final String sequencenum = "sequenceNum";
		public static final String _marker_key = "_Marker_key";
		public static final String chromosome = "chromosome";
		public static final String cpp = "cpp";
		public static final String cpn = "cpn";
		public static final String cnp = "cnp";
		public static final String cnn = "cnn";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_contig
	{
		public static final String _name = "MLD_Contig";
		public static final String _contig_key = "_Contig_key";
		public static final String _expt_key = "_Expt_key";
		public static final String mincm = "mincm";
		public static final String maxcm = "maxcm";
		public static final String name = "name";
		public static final String minlink = "minLink";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_contigprobe
	{
		public static final String _name = "MLD_ContigProbe";
		public static final String _contig_key = "_Contig_key";
		public static final String sequencenum = "sequenceNum";
		public static final String _probe_key = "_Probe_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_distance
	{
		public static final String _name = "MLD_Distance";
		public static final String _expt_key = "_Expt_key";
		public static final String _marker_key_1 = "_Marker_key_1";
		public static final String _marker_key_2 = "_Marker_key_2";
		public static final String sequencenum = "sequenceNum";
		public static final String estdistance = "estDistance";
		public static final String endonuclease = "endonuclease";
		public static final String minfrag = "minFrag";
		public static final String notes = "notes";
		public static final String relativearrangecharstr = "relativeArrangeCharStr";
		public static final String units = "units";
		public static final String realisticdist = "realisticDist";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_expt_marker
	{
		public static final String _name = "MLD_Expt_Marker";
		public static final String _expt_key = "_Expt_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _allele_key = "_Allele_key";
		public static final String _assay_type_key = "_Assay_Type_key";
		public static final String sequencenum = "sequenceNum";
		public static final String gene = "gene";
		public static final String description = "description";
		public static final String matrixdata = "matrixData";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_expt_notes
	{
		public static final String _name = "MLD_Expt_Notes";
		public static final String _expt_key = "_Expt_key";
		public static final String sequencenum = "sequenceNum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_expts
	{
		public static final String _name = "MLD_Expts";
		public static final String _expt_key = "_Expt_key";
		public static final String _refs_key = "_Refs_key";
		public static final String expttype = "exptType";
		public static final String tag = "tag";
		public static final String chromosome = "chromosome";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_fish
	{
		public static final String _name = "MLD_FISH";
		public static final String _expt_key = "_Expt_key";
		public static final String band = "band";
		public static final String _strain_key = "_Strain_key";
		public static final String cellorigin = "cellOrigin";
		public static final String karyotype = "karyotype";
		public static final String robertsonians = "robertsonians";
		public static final String label = "label";
		public static final String nummetaphase = "numMetaphase";
		public static final String totalsingle = "totalSingle";
		public static final String totaldouble = "totalDouble";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_fish_region
	{
		public static final String _name = "MLD_FISH_Region";
		public static final String _expt_key = "_Expt_key";
		public static final String sequencenum = "sequenceNum";
		public static final String region = "region";
		public static final String totalsingle = "totalSingle";
		public static final String totaldouble = "totalDouble";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_hit
	{
		public static final String _name = "MLD_Hit";
		public static final String _expt_key = "_Expt_key";
		public static final String _probe_key = "_Probe_key";
		public static final String _target_key = "_Target_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_hybrid
	{
		public static final String _name = "MLD_Hybrid";
		public static final String _expt_key = "_Expt_key";
		public static final String chrsorgenes = "chrsOrGenes";
		public static final String band = "band";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_insitu
	{
		public static final String _name = "MLD_InSitu";
		public static final String _expt_key = "_Expt_key";
		public static final String band = "band";
		public static final String _strain_key = "_Strain_key";
		public static final String cellorigin = "cellOrigin";
		public static final String karyotype = "karyotype";
		public static final String robertsonians = "robertsonians";
		public static final String nummetaphase = "numMetaphase";
		public static final String totalgrains = "totalGrains";
		public static final String grainsonchrom = "grainsOnChrom";
		public static final String grainsotherchrom = "grainsOtherChrom";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_isregion
	{
		public static final String _name = "MLD_ISRegion";
		public static final String _expt_key = "_Expt_key";
		public static final String sequencenum = "sequenceNum";
		public static final String region = "region";
		public static final String graincount = "grainCount";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_marker
	{
		public static final String _name = "MLD_Marker";
		public static final String _refs_key = "_Refs_key";
		public static final String _marker_key = "_Marker_key";
		public static final String sequencenum = "sequenceNum";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_matrix
	{
		public static final String _name = "MLD_Matrix";
		public static final String _expt_key = "_Expt_key";
		public static final String _cross_key = "_Cross_key";
		public static final String female = "female";
		public static final String female2 = "female2";
		public static final String male = "male";
		public static final String male2 = "male2";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_mc2point
	{
		public static final String _name = "MLD_MC2point";
		public static final String _expt_key = "_Expt_key";
		public static final String _marker_key_1 = "_Marker_key_1";
		public static final String _marker_key_2 = "_Marker_key_2";
		public static final String sequencenum = "sequenceNum";
		public static final String numrecombinants = "numRecombinants";
		public static final String numparentals = "numParentals";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_mcdatalist
	{
		public static final String _name = "MLD_MCDataList";
		public static final String _expt_key = "_Expt_key";
		public static final String sequencenum = "sequenceNum";
		public static final String alleleline = "alleleLine";
		public static final String offspringnmbr = "offspringNmbr";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_notes
	{
		public static final String _name = "MLD_Notes";
		public static final String _refs_key = "_Refs_key";
		public static final String sequencenum = "sequenceNum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_physmap
	{
		public static final String _name = "MLD_PhysMap";
		public static final String _expt_key = "_Expt_key";
		public static final String definitiveorder = "definitiveOrder";
		public static final String geneorder = "geneOrder";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_ri
	{
		public static final String _name = "MLD_RI";
		public static final String _expt_key = "_Expt_key";
		public static final String ri_idlist = "RI_IdList";
		public static final String _riset_key = "_RISet_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_ri2point
	{
		public static final String _name = "MLD_RI2Point";
		public static final String _expt_key = "_Expt_key";
		public static final String _marker_key_1 = "_Marker_key_1";
		public static final String _marker_key_2 = "_Marker_key_2";
		public static final String sequencenum = "sequenceNum";
		public static final String numrecombinants = "numRecombinants";
		public static final String numtotal = "numTotal";
		public static final String ri_lines = "RI_Lines";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_ridata
	{
		public static final String _name = "MLD_RIData";
		public static final String _expt_key = "_Expt_key";
		public static final String _marker_key = "_Marker_key";
		public static final String sequencenum = "sequenceNum";
		public static final String alleleline = "alleleLine";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mld_statistics
	{
		public static final String _name = "MLD_Statistics";
		public static final String _expt_key = "_Expt_key";
		public static final String sequencenum = "sequenceNum";
		public static final String _marker_key_1 = "_Marker_key_1";
		public static final String _marker_key_2 = "_Marker_key_2";
		public static final String recomb = "recomb";
		public static final String total = "total";
		public static final String pcntrecomb = "pcntrecomb";
		public static final String stderr = "stderr";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mlp_extra
	{
		public static final String _name = "MLP_Extra";
		public static final String _strain_key = "_Strain_key";
		public static final String reference = "reference";
		public static final String dataset = "dataset";
		public static final String note1 = "note1";
		public static final String note2 = "note2";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mlp_notes
	{
		public static final String _name = "MLP_Notes";
		public static final String _strain_key = "_Strain_key";
		public static final String sequencenum = "sequenceNum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mlp_species
	{
		public static final String _name = "MLP_Species";
		public static final String _species_key = "_Species_key";
		public static final String species = "species";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mlp_strain
	{
		public static final String _name = "MLP_Strain";
		public static final String _strain_key = "_Strain_key";
		public static final String _species_key = "_Species_key";
		public static final String userdefined1 = "userDefined1";
		public static final String userdefined2 = "userDefined2";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mlp_straintype
	{
		public static final String _name = "MLP_StrainType";
		public static final String _straintype_key = "_StrainType_key";
		public static final String straintype = "strainType";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mlp_straintypes
	{
		public static final String _name = "MLP_StrainTypes";
		public static final String _strain_key = "_Strain_key";
		public static final String _straintype_key = "_StrainType_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_alias
	{
		public static final String _name = "MRK_Alias";
		public static final String _alias_key = "_Alias_key";
		public static final String _marker_key = "_Marker_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_anchors
	{
		public static final String _name = "MRK_Anchors";
		public static final String chromosome = "chromosome";
		public static final String _marker_key = "_Marker_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_chromosome
	{
		public static final String _name = "MRK_Chromosome";
		public static final String _organism_key = "_Organism_key";
		public static final String chromosome = "chromosome";
		public static final String sequencenum = "sequenceNum";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_class
	{
		public static final String _name = "MRK_Class";
		public static final String _class_key = "_Class_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_classes
	{
		public static final String _name = "MRK_Classes";
		public static final String _marker_key = "_Marker_key";
		public static final String _class_key = "_Class_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_current
	{
		public static final String _name = "MRK_Current";
		public static final String _current_key = "_Current_key";
		public static final String _marker_key = "_Marker_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_event
	{
		public static final String _name = "MRK_Event";
		public static final String _marker_event_key = "_Marker_Event_key";
		public static final String event = "event";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_eventreason
	{
		public static final String _name = "MRK_EventReason";
		public static final String _marker_eventreason_key = "_Marker_EventReason_key";
		public static final String eventreason = "eventReason";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_history
	{
		public static final String _name = "MRK_History";
		public static final String _marker_key = "_Marker_key";
		public static final String _marker_event_key = "_Marker_Event_key";
		public static final String _marker_eventreason_key = "_Marker_EventReason_key";
		public static final String _history_key = "_History_key";
		public static final String _refs_key = "_Refs_key";
		public static final String sequencenum = "sequenceNum";
		public static final String name = "name";
		public static final String event_date = "event_date";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_label
	{
		public static final String _name = "MRK_Label";
		public static final String _marker_key = "_Marker_key";
		public static final String _label_status_key = "_Label_Status_key";
		public static final String _organism_key = "_Organism_key";
		public static final String _orthologorganism_key = "_OrthologOrganism_key";
		public static final String priority = "priority";
		public static final String label = "label";
		public static final String labeltype = "labelType";
		public static final String labeltypename = "labelTypeName";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_marker
	{
		public static final String _name = "MRK_Marker";
		public static final String _marker_key = "_Marker_key";
		public static final String _organism_key = "_Organism_key";
		public static final String _marker_status_key = "_Marker_Status_key";
		public static final String _marker_type_key = "_Marker_Type_key";
		public static final String _curationstate_key = "_CurationState_key";
		public static final String symbol = "symbol";
		public static final String name = "name";
		public static final String chromosome = "chromosome";
		public static final String cytogeneticoffset = "cytogeneticOffset";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_notes
	{
		public static final String _name = "MRK_Notes";
		public static final String _marker_key = "_Marker_key";
		public static final String sequencenum = "sequenceNum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_offset
	{
		public static final String _name = "MRK_Offset";
		public static final String _marker_key = "_Marker_key";
		public static final String source = "source";
		public static final String offset = "offset";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_other
	{
		public static final String _name = "MRK_Other";
		public static final String _other_key = "_Other_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _refs_key = "_Refs_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_reference
	{
		public static final String _name = "MRK_Reference";
		public static final String _marker_key = "_Marker_key";
		public static final String _refs_key = "_Refs_key";
		public static final String auto = "auto";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_status
	{
		public static final String _name = "MRK_Status";
		public static final String _marker_status_key = "_Marker_Status_key";
		public static final String status = "status";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mrk_types
	{
		public static final String _name = "MRK_Types";
		public static final String _marker_type_key = "_Marker_Type_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class nom_genefamily
	{
		public static final String _name = "NOM_GeneFamily";
		public static final String _nomen_key = "_Nomen_key";
		public static final String _genefamily_key = "_GeneFamily_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class nom_marker
	{
		public static final String _name = "NOM_Marker";
		public static final String _nomen_key = "_Nomen_key";
		public static final String _marker_type_key = "_Marker_Type_key";
		public static final String _nomenstatus_key = "_NomenStatus_key";
		public static final String _marker_event_key = "_Marker_Event_key";
		public static final String _marker_eventreason_key = "_Marker_EventReason_key";
		public static final String _curationstate_key = "_CurationState_key";
		public static final String symbol = "symbol";
		public static final String name = "name";
		public static final String chromosome = "chromosome";
		public static final String humansymbol = "humanSymbol";
		public static final String statusnote = "statusNote";
		public static final String broadcast_date = "broadcast_date";
		public static final String _broadcastby_key = "_BroadcastBy_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class nom_synonym
	{
		public static final String _name = "NOM_Synonym";
		public static final String _synonym_key = "_Synonym_key";
		public static final String _nomen_key = "_Nomen_key";
		public static final String _refs_key = "_Refs_key";
		public static final String name = "name";
		public static final String isauthor = "isAuthor";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_alias
	{
		public static final String _name = "PRB_Alias";
		public static final String _alias_key = "_Alias_key";
		public static final String _reference_key = "_Reference_key";
		public static final String alias = "alias";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_allele
	{
		public static final String _name = "PRB_Allele";
		public static final String _allele_key = "_Allele_key";
		public static final String _rflv_key = "_RFLV_key";
		public static final String allele = "allele";
		public static final String fragments = "fragments";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_allele_strain
	{
		public static final String _name = "PRB_Allele_Strain";
		public static final String _allele_key = "_Allele_key";
		public static final String _strain_key = "_Strain_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_marker
	{
		public static final String _name = "PRB_Marker";
		public static final String _probe_key = "_Probe_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _refs_key = "_Refs_key";
		public static final String relationship = "relationship";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_notes
	{
		public static final String _name = "PRB_Notes";
		public static final String _probe_key = "_Probe_key";
		public static final String sequencenum = "sequenceNum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_probe
	{
		public static final String _name = "PRB_Probe";
		public static final String _probe_key = "_Probe_key";
		public static final String name = "name";
		public static final String derivedfrom = "derivedFrom";
		public static final String _source_key = "_Source_key";
		public static final String _vector_key = "_Vector_key";
		public static final String _segmenttype_key = "_SegmentType_key";
		public static final String primer1sequence = "primer1sequence";
		public static final String primer2sequence = "primer2sequence";
		public static final String regioncovered = "regionCovered";
		public static final String regioncovered2 = "regionCovered2";
		public static final String insertsite = "insertSite";
		public static final String insertsize = "insertSize";
		public static final String repeatunit = "repeatUnit";
		public static final String productsize = "productSize";
		public static final String moreproduct = "moreProduct";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_ref_notes
	{
		public static final String _name = "PRB_Ref_Notes";
		public static final String _reference_key = "_Reference_key";
		public static final String sequencenum = "sequenceNum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_reference
	{
		public static final String _name = "PRB_Reference";
		public static final String _reference_key = "_Reference_key";
		public static final String _probe_key = "_Probe_key";
		public static final String _refs_key = "_Refs_key";
		public static final String holder = "holder";
		public static final String hasrmap = "hasRmap";
		public static final String hassequence = "hasSequence";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_rflv
	{
		public static final String _name = "PRB_RFLV";
		public static final String _rflv_key = "_RFLV_key";
		public static final String _reference_key = "_Reference_key";
		public static final String _marker_key = "_Marker_key";
		public static final String endonuclease = "endonuclease";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_source
	{
		public static final String _name = "PRB_Source";
		public static final String _source_key = "_Source_key";
		public static final String _segmenttype_key = "_SegmentType_key";
		public static final String _vector_key = "_Vector_key";
		public static final String _organism_key = "_Organism_key";
		public static final String _strain_key = "_Strain_key";
		public static final String _tissue_key = "_Tissue_key";
		public static final String _gender_key = "_Gender_key";
		public static final String _cellline_key = "_CellLine_key";
		public static final String _refs_key = "_Refs_key";
		public static final String name = "name";
		public static final String description = "description";
		public static final String age = "age";
		public static final String agemin = "ageMin";
		public static final String agemax = "ageMax";
		public static final String iscuratoredited = "isCuratorEdited";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_strain
	{
		public static final String _name = "PRB_Strain";
		public static final String _strain_key = "_Strain_key";
		public static final String strain = "strain";
		public static final String standard = "standard";
		public static final String needsreview = "needsReview";
		public static final String private = "private";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_strain_marker
	{
		public static final String _name = "PRB_Strain_Marker";
		public static final String _strainmarker_key = "_StrainMarker_key";
		public static final String _strain_key = "_Strain_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _allele_key = "_Allele_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_strain_synonym
	{
		public static final String _name = "PRB_Strain_Synonym";
		public static final String _synonym_key = "_Synonym_key";
		public static final String _strain_key = "_Strain_key";
		public static final String synonym = "synonym";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class prb_tissue
	{
		public static final String _name = "PRB_Tissue";
		public static final String _tissue_key = "_Tissue_key";
		public static final String tissue = "tissue";
		public static final String standard = "standard";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class ri_riset
	{
		public static final String _name = "RI_RISet";
		public static final String _riset_key = "_RISet_key";
		public static final String _strain_key_1 = "_Strain_key_1";
		public static final String _strain_key_2 = "_Strain_key_2";
		public static final String designation = "designation";
		public static final String abbrev1 = "abbrev1";
		public static final String abbrev2 = "abbrev2";
		public static final String ri_idlist = "RI_IdList";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class ri_summary
	{
		public static final String _name = "RI_Summary";
		public static final String _risummary_key = "_RISummary_key";
		public static final String _riset_key = "_RISet_key";
		public static final String _marker_key = "_Marker_key";
		public static final String summary = "summary";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class ri_summary_expt_ref
	{
		public static final String _name = "RI_Summary_Expt_Ref";
		public static final String _risummary_key = "_RISummary_key";
		public static final String _expt_key = "_Expt_key";
		public static final String _refs_key = "_Refs_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class seq_markercache
	{
		public static final String _name = "SEQ_MarkerCache";
		public static final String _sequence_key = "_Sequence_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _cachetype_key = "_CacheType_key";
		public static final String _markertype_key = "_MarkerType_key";
		public static final String _organism_key = "_Organism_key";
		public static final String _source_key = "_Source_key";
		public static final String _accession_key = "_Accession_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String seqrecord_date = "seqrecord_date";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class seq_markerset_assoc
	{
		public static final String _name = "SEQ_MarkerSet_Assoc";
		public static final String _assoc_key = "_Assoc_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _sequenceset_key = "_SequenceSet_key";
		public static final String _curationstate_key = "_CurationState_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class seq_nomen_assoc
	{
		public static final String _name = "SEQ_Nomen_Assoc";
		public static final String _assoc_key = "_Assoc_key";
		public static final String _nomen_key = "_Nomen_key";
		public static final String _sequence_key = "_Sequence_key";
		public static final String _seqsetassocrefs_key = "_SeqSetAssocRefs_key";
		public static final String _seqsetassoccuration_key = "_SeqSetAssocCuration_key";
		public static final String _seqsetrefs_key = "_SeqSetRefs_key";
		public static final String _seqsetcuration_key = "_SeqSetCuration_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class seq_probe_assoc
	{
		public static final String _name = "SEQ_Probe_Assoc";
		public static final String _assoc_key = "_Assoc_key";
		public static final String _probe_key = "_Probe_key";
		public static final String _sequence_key = "_Sequence_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class seq_seqset_assoc
	{
		public static final String _name = "SEQ_SeqSet_Assoc";
		public static final String _sequenceset_key = "_SequenceSet_key";
		public static final String _sequence_key = "_Sequence_key";
		public static final String isspecial = "isSpecial";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class seq_sequence
	{
		public static final String _name = "SEQ_Sequence";
		public static final String _sequence_key = "_Sequence_key";
		public static final String _sequencetype_key = "_SequenceType_key";
		public static final String _sequencequality_key = "_SequenceQuality_key";
		public static final String _sequencestatus_key = "_SequenceStatus_key";
		public static final String _sequenceprovider_key = "_SequenceProvider_key";
		public static final String length = "length";
		public static final String description = "description";
		public static final String version = "version";
		public static final String division = "division";
		public static final String virtual = "virtual";
		public static final String rawtype = "rawType";
		public static final String rawlibrary = "rawLibrary";
		public static final String raworganism = "rawOrganism";
		public static final String rawstrain = "rawStrain";
		public static final String rawtissue = "rawTissue";
		public static final String rawage = "rawAge";
		public static final String rawsex = "rawSex";
		public static final String rawcellline = "rawCellLine";
		public static final String numberoforganisms = "numberOfOrganisms";
		public static final String seqrecord_date = "seqrecord_date";
		public static final String sequence_date = "sequence_date";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class seq_sequenceset
	{
		public static final String _name = "SEQ_SequenceSet";
		public static final String _sequenceset_key = "_SequenceSet_key";
		public static final String _curationstate_key = "_CurationState_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class seq_source_assoc
	{
		public static final String _name = "SEQ_Source_Assoc";
		public static final String _assoc_key = "_Assoc_key";
		public static final String _sequence_key = "_Sequence_key";
		public static final String _source_key = "_Source_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class test_dbstamped_mgd
	{
		public static final String _name = "TEST_DBstamped_MGD";
		public static final String columna = "columnA";
		public static final String columnb = "columnB";
		public static final String _createdby_key = "_createdBy_key";
		public static final String _modifiedby_key = "_modifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class test_dbstamped_mgdorg
	{
		public static final String _name = "TEST_DBstamped_MGDOrg";
		public static final String columna = "columnA";
		public static final String columnb = "columnB";
		public static final String columnc = "columnC";
		public static final String columnd = "columnD";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_annot
	{
		public static final String _name = "VOC_Annot";
		public static final String _annot_key = "_Annot_key";
		public static final String _annottype_key = "_AnnotType_key";
		public static final String _object_key = "_Object_key";
		public static final String _term_key = "_Term_key";
		public static final String isnot = "isNot";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_annottype
	{
		public static final String _name = "VOC_AnnotType";
		public static final String _annottype_key = "_AnnotType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String _evidencevocab_key = "_EvidenceVocab_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_evidence
	{
		public static final String _name = "VOC_Evidence";
		public static final String _annotevidence_key = "_AnnotEvidence_key";
		public static final String _annot_key = "_Annot_key";
		public static final String _evidenceterm_key = "_EvidenceTerm_key";
		public static final String _refs_key = "_Refs_key";
		public static final String inferredfrom = "inferredFrom";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_synonym
	{
		public static final String _name = "VOC_Synonym";
		public static final String _synonym_key = "_Synonym_key";
		public static final String _term_key = "_Term_key";
		public static final String synonym = "synonym";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term
	{
		public static final String _name = "VOC_Term";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_text
	{
		public static final String _name = "VOC_Text";
		public static final String _term_key = "_Term_key";
		public static final String sequencenum = "sequenceNum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_vocab
	{
		public static final String _name = "VOC_Vocab";
		public static final String _vocab_key = "_Vocab_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String issimple = "isSimple";
		public static final String isprivate = "isPrivate";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_vocabdag
	{
		public static final String _name = "VOC_VocabDAG";
		public static final String _vocab_key = "_Vocab_key";
		public static final String _dag_key = "_DAG_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class wks_rosetta
	{
		public static final String _name = "WKS_Rosetta";
		public static final String _rosetta_key = "_Rosetta_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _allele_key = "_Allele_key";
		public static final String wks_markersymbol = "wks_markerSymbol";
		public static final String wks_markername = "wks_markerName";
		public static final String wks_allelesymbol = "wks_alleleSymbol";
		public static final String wks_allelename = "wks_alleleName";
		public static final String wks_markerurl = "wks_markerurl";
		public static final String wks_alleleurl = "wks_alleleurl";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class acc_logicaldb_view
	{
		public static final String _name = "ACC_LogicalDB_View";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String name = "name";
		public static final String description = "description";
		public static final String _organism_key = "_Organism_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String organism = "organism";
		
		}
	public class acc_reference_view
	{
		public static final String _name = "ACC_Reference_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgitype = "MGIType";
		public static final String logicaldb = "LogicalDB";
		public static final String description = "description";
		public static final String _organism_key = "_Organism_key";
		public static final String actualdb = "ActualDB";
		public static final String url = "url";
		public static final String allowsmultiple = "allowsMultiple";
		public static final String delimiter = "delimiter";
		public static final String _refs_key = "_Refs_key";
		public static final String jnum = "jnum";
		public static final String jnumid = "jnumID";
		public static final String short_citation = "short_citation";
		
		}
	public class acc_view
	{
		public static final String _name = "ACC_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgitype = "MGIType";
		public static final String logicaldb = "LogicalDB";
		public static final String description = "description";
		public static final String _organism_key = "_Organism_key";
		public static final String actualdb = "ActualDB";
		public static final String url = "url";
		public static final String allowsmultiple = "allowsMultiple";
		public static final String delimiter = "delimiter";
		
		}
	public class all_acc_view
	{
		public static final String _name = "ALL_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class all_allele_mutation_view
	{
		public static final String _name = "ALL_Allele_Mutation_View";
		public static final String _allele_key = "_Allele_key";
		public static final String _mutation_key = "_Mutation_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mutation = "mutation";
		
		}
	public class all_allele_view
	{
		public static final String _name = "ALL_Allele_View";
		public static final String _allele_key = "_Allele_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _strain_key = "_Strain_key";
		public static final String _mode_key = "_Mode_key";
		public static final String _allele_type_key = "_Allele_Type_key";
		public static final String _cellline_key = "_CellLine_key";
		public static final String _allele_status_key = "_Allele_Status_key";
		public static final String symbol = "symbol";
		public static final String name = "name";
		public static final String nomensymbol = "nomenSymbol";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String _approvedby_key = "_ApprovedBy_key";
		public static final String approval_date = "approval_date";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String markersymbol = "markerSymbol";
		public static final String strain = "strain";
		public static final String mode = "mode";
		public static final String alleletype = "alleleType";
		public static final String cellline = "cellLine";
		public static final String celllinestrain = "cellLineStrain";
		public static final String status = "status";
		public static final String markername = "markerName";
		public static final String mgiid = "mgiID";
		public static final String approval_date_str = "approval_date_str";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		public static final String approvedby = "approvedBy";
		
		}
	public class all_cellline_view
	{
		public static final String _name = "ALL_CellLine_View";
		public static final String _cellline_key = "_CellLine_key";
		public static final String cellline = "cellLine";
		public static final String _strain_key = "_Strain_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String celllinestrain = "cellLineStrain";
		
		}
	public class all_note_general_view
	{
		public static final String _name = "ALL_Note_General_View";
		public static final String _allele_key = "_Allele_key";
		public static final String _notetype_key = "_NoteType_key";
		public static final String sequencenum = "sequenceNum";
		public static final String private = "private";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String notetype = "noteType";
		public static final String notetypeprivate = "noteTypePrivate";
		
		}
	public class all_note_molecular_view
	{
		public static final String _name = "ALL_Note_Molecular_View";
		public static final String _allele_key = "_Allele_key";
		public static final String _notetype_key = "_NoteType_key";
		public static final String sequencenum = "sequenceNum";
		public static final String private = "private";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String notetype = "noteType";
		public static final String notetypeprivate = "noteTypePrivate";
		
		}
	public class all_note_nomenclature_view
	{
		public static final String _name = "ALL_Note_Nomenclature_View";
		public static final String _allele_key = "_Allele_key";
		public static final String _notetype_key = "_NoteType_key";
		public static final String sequencenum = "sequenceNum";
		public static final String private = "private";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String notetype = "noteType";
		public static final String notetypeprivate = "noteTypePrivate";
		
		}
	public class all_note_promoter_view
	{
		public static final String _name = "ALL_Note_Promoter_View";
		public static final String _allele_key = "_Allele_key";
		public static final String _notetype_key = "_NoteType_key";
		public static final String sequencenum = "sequenceNum";
		public static final String private = "private";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String notetype = "noteType";
		public static final String notetypeprivate = "noteTypePrivate";
		
		}
	public class all_note_view
	{
		public static final String _name = "ALL_Note_View";
		public static final String _allele_key = "_Allele_key";
		public static final String _notetype_key = "_NoteType_key";
		public static final String sequencenum = "sequenceNum";
		public static final String private = "private";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String notetype = "noteType";
		public static final String notetypeprivate = "noteTypePrivate";
		
		}
	public class all_reference_additional_view
	{
		public static final String _name = "ALL_Reference_Additional_View";
		public static final String _allele_key = "_Allele_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _refstype_key = "_RefsType_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String referencetype = "referenceType";
		public static final String allowonlyone = "allowOnlyOne";
		public static final String jnum = "jnum";
		public static final String jnumid = "jnumID";
		public static final String short_citation = "short_citation";
		public static final String citation = "citation";
		public static final String authors = "authors";
		public static final String title = "title";
		
		}
	public class all_reference_molecular_view
	{
		public static final String _name = "ALL_Reference_Molecular_View";
		public static final String _allele_key = "_Allele_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _refstype_key = "_RefsType_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String referencetype = "referenceType";
		public static final String allowonlyone = "allowOnlyOne";
		public static final String jnum = "jnum";
		public static final String jnumid = "jnumID";
		public static final String short_citation = "short_citation";
		public static final String citation = "citation";
		public static final String authors = "authors";
		public static final String title = "title";
		
		}
	public class all_reference_original_view
	{
		public static final String _name = "ALL_Reference_Original_View";
		public static final String _allele_key = "_Allele_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _refstype_key = "_RefsType_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String referencetype = "referenceType";
		public static final String allowonlyone = "allowOnlyOne";
		public static final String jnum = "jnum";
		public static final String jnumid = "jnumID";
		public static final String short_citation = "short_citation";
		public static final String citation = "citation";
		public static final String authors = "authors";
		public static final String title = "title";
		
		}
	public class all_reference_view
	{
		public static final String _name = "ALL_Reference_View";
		public static final String _allele_key = "_Allele_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _refstype_key = "_RefsType_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String referencetype = "referenceType";
		public static final String allowonlyone = "allowOnlyOne";
		public static final String jnum = "jnum";
		public static final String jnumid = "jnumID";
		public static final String short_citation = "short_citation";
		public static final String citation = "citation";
		public static final String authors = "authors";
		public static final String title = "title";
		
		}
	public class all_summary_view
	{
		public static final String _name = "ALL_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class all_synonym_view
	{
		public static final String _name = "ALL_Synonym_View";
		public static final String _synonym_key = "_Synonym_key";
		public static final String _allele_key = "_Allele_key";
		public static final String _refs_key = "_Refs_key";
		public static final String synonym = "synonym";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String jnum = "jnum";
		public static final String jnumid = "jnumID";
		public static final String short_citation = "short_citation";
		public static final String citation = "citation";
		public static final String authors = "authors";
		public static final String title = "title";
		public static final String _marker_key = "_Marker_key";
		public static final String symbol = "symbol";
		
		}
	public class bib_acc_view
	{
		public static final String _name = "BIB_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class bib_all_view
	{
		public static final String _name = "BIB_All_View";
		public static final String _refs_key = "_Refs_key";
		public static final String _reviewstatus_key = "_ReviewStatus_key";
		public static final String reftype = "refType";
		public static final String authors = "authors";
		public static final String authors2 = "authors2";
		public static final String _primary = "_primary";
		public static final String title = "title";
		public static final String title2 = "title2";
		public static final String journal = "journal";
		public static final String vol = "vol";
		public static final String issue = "issue";
		public static final String date = "date";
		public static final String year = "year";
		public static final String pgs = "pgs";
		public static final String dbs = "dbs";
		public static final String nlmstatus = "NLMstatus";
		public static final String abstract = "abstract";
		public static final String isreviewarticle = "isReviewArticle";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String reviewstatus = "reviewStatus";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String citation = "citation";
		public static final String short_citation = "short_citation";
		
		}
	public class bib_goxref_view
	{
		public static final String _name = "BIB_GOXRef_View";
		public static final String _refs_key = "_Refs_key";
		public static final String _reviewstatus_key = "_ReviewStatus_key";
		public static final String reftype = "refType";
		public static final String authors = "authors";
		public static final String authors2 = "authors2";
		public static final String _primary = "_primary";
		public static final String title = "title";
		public static final String title2 = "title2";
		public static final String journal = "journal";
		public static final String vol = "vol";
		public static final String issue = "issue";
		public static final String date = "date";
		public static final String year = "year";
		public static final String pgs = "pgs";
		public static final String dbs = "dbs";
		public static final String nlmstatus = "NLMstatus";
		public static final String abstract = "abstract";
		public static final String isreviewarticle = "isReviewArticle";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String reviewstatus = "reviewStatus";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String citation = "citation";
		public static final String short_citation = "short_citation";
		public static final String _marker_key = "_Marker_key";
		
		}
	public class bib_summary_all_view
	{
		public static final String _name = "BIB_Summary_All_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		
		}
	public class bib_summary_view
	{
		public static final String _name = "BIB_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class bib_view
	{
		public static final String _name = "BIB_View";
		public static final String _refs_key = "_Refs_key";
		public static final String _reviewstatus_key = "_ReviewStatus_key";
		public static final String reftype = "refType";
		public static final String authors = "authors";
		public static final String authors2 = "authors2";
		public static final String _primary = "_primary";
		public static final String title = "title";
		public static final String title2 = "title2";
		public static final String journal = "journal";
		public static final String vol = "vol";
		public static final String issue = "issue";
		public static final String date = "date";
		public static final String year = "year";
		public static final String pgs = "pgs";
		public static final String dbs = "dbs";
		public static final String nlmstatus = "NLMstatus";
		public static final String abstract = "abstract";
		public static final String isreviewarticle = "isReviewArticle";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String reviewstatus = "reviewStatus";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String citation = "citation";
		public static final String short_citation = "short_citation";
		
		}
	public class crs_cross_view
	{
		public static final String _name = "CRS_Cross_View";
		public static final String _cross_key = "_Cross_key";
		public static final String type = "type";
		public static final String _femalestrain_key = "_femaleStrain_key";
		public static final String femaleallele1 = "femaleAllele1";
		public static final String femaleallele2 = "femaleAllele2";
		public static final String _malestrain_key = "_maleStrain_key";
		public static final String maleallele1 = "maleAllele1";
		public static final String maleallele2 = "maleAllele2";
		public static final String abbrevho = "abbrevHO";
		public static final String _strainho_key = "_StrainHO_key";
		public static final String abbrevht = "abbrevHT";
		public static final String _strainht_key = "_StrainHT_key";
		public static final String whosecross = "whoseCross";
		public static final String allelefromsegparent = "alleleFromSegParent";
		public static final String f1directionknown = "F1DirectionKnown";
		public static final String nprogeny = "nProgeny";
		public static final String displayed = "displayed";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String display = "display";
		public static final String femalestrain = "femaleStrain";
		public static final String malestrain = "maleStrain";
		public static final String strainho = "strainHO";
		public static final String strainht = "strainHT";
		
		}
	public class dag_node_view
	{
		public static final String _name = "DAG_Node_View";
		public static final String _node_key = "_Node_key";
		public static final String _dag_key = "_DAG_key";
		public static final String _object_key = "_Object_key";
		public static final String _label_key = "_Label_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String dag = "dag";
		public static final String dagabbrev = "dagAbbrev";
		public static final String _vocab_key = "_Vocab_key";
		
		}
	public class gxd_allelegenotype_view
	{
		public static final String _name = "GXD_AlleleGenotype_View";
		public static final String _genotype_key = "_Genotype_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _allele_key = "_Allele_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String marker = "marker";
		public static final String allele = "allele";
		public static final String strain = "strain";
		
		}
	public class gxd_allelepair_view
	{
		public static final String _name = "GXD_AllelePair_View";
		public static final String _allelepair_key = "_AllelePair_key";
		public static final String _genotype_key = "_Genotype_key";
		public static final String sequencenum = "sequenceNum";
		public static final String _allele_key_1 = "_Allele_key_1";
		public static final String _allele_key_2 = "_Allele_key_2";
		public static final String _marker_key = "_Marker_key";
		public static final String isunknown = "isUnknown";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol = "symbol";
		public static final String allele1 = "allele1";
		public static final String allele2 = "allele2";
		public static final String _strain_key = "_Strain_key";
		public static final String strain = "strain";
		public static final String allelestate = "alleleState";
		public static final String mgiid = "mgiID";
		
		}
	public class gxd_antibody_acc_view
	{
		public static final String _name = "GXD_Antibody_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class gxd_antibody_summary_view
	{
		public static final String _name = "GXD_Antibody_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class gxd_antibody_view
	{
		public static final String _name = "GXD_Antibody_View";
		public static final String _antibody_key = "_Antibody_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _antibodyclass_key = "_AntibodyClass_key";
		public static final String _antibodytype_key = "_AntibodyType_key";
		public static final String _organism_key = "_Organism_key";
		public static final String _antigen_key = "_Antigen_key";
		public static final String antibodyname = "antibodyName";
		public static final String antibodynote = "antibodyNote";
		public static final String recogwestern = "recogWestern";
		public static final String recogimmunprecip = "recogImmunPrecip";
		public static final String recognote = "recogNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String class = "class";
		public static final String antibodytype = "antibodyType";
		public static final String antibodyspecies = "antibodySpecies";
		public static final String antigenname = "antigenName";
		
		}
	public class gxd_antibodyalias_view
	{
		public static final String _name = "GXD_AntibodyAlias_View";
		public static final String antibodyname = "antibodyName";
		public static final String _antibodyalias_key = "_AntibodyAlias_key";
		public static final String _antibody_key = "_Antibody_key";
		public static final String _refs_key = "_Refs_key";
		public static final String alias = "alias";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class gxd_antibodyaliasref_view
	{
		public static final String _name = "GXD_AntibodyAliasRef_View";
		public static final String antibodyname = "antibodyName";
		public static final String _antibodyalias_key = "_AntibodyAlias_key";
		public static final String _antibody_key = "_Antibody_key";
		public static final String _refs_key = "_Refs_key";
		public static final String alias = "alias";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		
		}
	public class gxd_antibodyantigen_view
	{
		public static final String _name = "GXD_AntibodyAntigen_View";
		public static final String _antibody_key = "_Antibody_key";
		public static final String antibodyname = "antibodyName";
		public static final String _antigen_key = "_Antigen_key";
		public static final String _source_key = "_Source_key";
		public static final String antigenname = "antigenName";
		public static final String regioncovered = "regionCovered";
		public static final String antigennote = "antigenNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String organism = "organism";
		public static final String age = "age";
		public static final String gender = "gender";
		public static final String cellline = "cellLine";
		public static final String library = "library";
		public static final String description = "description";
		public static final String _refs_key = "_Refs_key";
		public static final String _strain_key = "_Strain_key";
		public static final String strain = "strain";
		public static final String _tissue_key = "_Tissue_key";
		public static final String tissue = "tissue";
		
		}
	public class gxd_antibodymarker_view
	{
		public static final String _name = "GXD_AntibodyMarker_View";
		public static final String _antibody_key = "_Antibody_key";
		public static final String antibodyname = "antibodyName";
		public static final String _marker_key = "_Marker_key";
		public static final String symbol = "symbol";
		public static final String chromosome = "chromosome";
		
		}
	public class gxd_antibodyprep_view
	{
		public static final String _name = "GXD_AntibodyPrep_View";
		public static final String _assay_key = "_Assay_key";
		public static final String _antibodyprep_key = "_AntibodyPrep_key";
		public static final String _antibody_key = "_Antibody_key";
		public static final String _secondary_key = "_Secondary_key";
		public static final String _label_key = "_Label_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String secondary = "secondary";
		public static final String label = "label";
		public static final String antibodyname = "antibodyName";
		public static final String accid = "accID";
		
		}
	public class gxd_antibodyref_view
	{
		public static final String _name = "GXD_AntibodyRef_View";
		public static final String _antibody_key = "_Antibody_key";
		public static final String _refs_key = "_Refs_key";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		
		}
	public class gxd_antigen_acc_view
	{
		public static final String _name = "GXD_Antigen_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class gxd_antigen_summary_view
	{
		public static final String _name = "GXD_Antigen_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class gxd_antigen_view
	{
		public static final String _name = "GXD_Antigen_View";
		public static final String _antigen_key = "_Antigen_key";
		public static final String _source_key = "_Source_key";
		public static final String antigenname = "antigenName";
		public static final String regioncovered = "regionCovered";
		public static final String antigennote = "antigenNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String organism = "organism";
		public static final String age = "age";
		public static final String gender = "gender";
		public static final String cellline = "cellLine";
		public static final String library = "library";
		public static final String description = "description";
		public static final String _refs_key = "_Refs_key";
		public static final String _strain_key = "_Strain_key";
		public static final String strain = "strain";
		public static final String _tissue_key = "_Tissue_key";
		public static final String tissue = "tissue";
		
		}
	public class gxd_assay_acc_view
	{
		public static final String _name = "GXD_Assay_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class gxd_assay_summary_view
	{
		public static final String _name = "GXD_Assay_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class gxd_assay_view
	{
		public static final String _name = "GXD_Assay_View";
		public static final String _assay_key = "_Assay_key";
		public static final String _assaytype_key = "_AssayType_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _probeprep_key = "_ProbePrep_key";
		public static final String _antibodyprep_key = "_AntibodyPrep_key";
		public static final String _imagepane_key = "_ImagePane_key";
		public static final String _reportergene_key = "_ReporterGene_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String assaytype = "assayType";
		public static final String isrnaassay = "isRNAAssay";
		public static final String isgelassay = "isGelAssay";
		public static final String symbol = "symbol";
		public static final String chromosome = "chromosome";
		public static final String name = "name";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class gxd_expression_view
	{
		public static final String _name = "GXD_Expression_View";
		public static final String _expression_key = "_Expression_key";
		public static final String _assay_key = "_Assay_key";
		public static final String _assaytype_key = "_AssayType_key";
		public static final String _genotype_key = "_Genotype_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _structure_key = "_Structure_key";
		public static final String expressed = "expressed";
		public static final String age = "age";
		public static final String agemin = "ageMin";
		public static final String agemax = "ageMax";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String _refs_key = "_Refs_key";
		public static final String mgiid = "mgiID";
		public static final String assaytype = "assayType";
		public static final String isrnaassay = "isRNAAssay";
		public static final String isgelassay = "isGelAssay";
		public static final String strain = "strain";
		public static final String strainid = "strainID";
		public static final String symbol = "symbol";
		public static final String chromosome = "chromosome";
		public static final String name = "name";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		
		}
	public class gxd_gelband_view
	{
		public static final String _name = "GXD_GelBand_View";
		public static final String _gelband_key = "_GelBand_key";
		public static final String _gellane_key = "_GelLane_key";
		public static final String _gelrow_key = "_GelRow_key";
		public static final String _strength_key = "_Strength_key";
		public static final String bandnote = "bandNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String strength = "strength";
		public static final String _assay_key = "_Assay_key";
		public static final String lanenum = "laneNum";
		public static final String rownum = "rowNum";
		
		}
	public class gxd_gellane_view
	{
		public static final String _name = "GXD_GelLane_View";
		public static final String _gellane_key = "_GelLane_key";
		public static final String _assay_key = "_Assay_key";
		public static final String _genotype_key = "_Genotype_key";
		public static final String _gelrnatype_key = "_GelRNAType_key";
		public static final String _gelcontrol_key = "_GelControl_key";
		public static final String sequencenum = "sequenceNum";
		public static final String lanelabel = "laneLabel";
		public static final String sampleamount = "sampleAmount";
		public static final String sex = "sex";
		public static final String age = "age";
		public static final String agemin = "ageMin";
		public static final String agemax = "ageMax";
		public static final String agenote = "ageNote";
		public static final String lanenote = "laneNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String rnatype = "rnaType";
		public static final String strain = "strain";
		public static final String gellanecontent = "gelLaneContent";
		public static final String mgiid = "mgiID";
		
		}
	public class gxd_gellanestructure_view
	{
		public static final String _name = "GXD_GelLaneStructure_View";
		public static final String _assay_key = "_Assay_key";
		public static final String sequencenum = "sequenceNum";
		public static final String _gellane_key = "_GelLane_key";
		public static final String _structure_key = "_Structure_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String printname = "printName";
		public static final String stage = "stage";
		public static final String _stage_key = "_Stage_key";
		public static final String dbname = "dbName";
		
		}
	public class gxd_gelrow_view
	{
		public static final String _name = "GXD_GelRow_View";
		public static final String _gelrow_key = "_GelRow_key";
		public static final String _assay_key = "_Assay_key";
		public static final String _gelunits_key = "_GelUnits_key";
		public static final String sequencenum = "sequenceNum";
		public static final String size = "size";
		public static final String rownote = "rowNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String size_str = "size_str";
		public static final String units = "units";
		
		}
	public class gxd_genotype_acc_view
	{
		public static final String _name = "GXD_Genotype_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class gxd_genotype_summary_view
	{
		public static final String _name = "GXD_Genotype_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class gxd_genotype_view
	{
		public static final String _name = "GXD_Genotype_View";
		public static final String _genotype_key = "_Genotype_key";
		public static final String _strain_key = "_Strain_key";
		public static final String isconditional = "isConditional";
		public static final String note = "note";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String strain = "strain";
		public static final String mgiid = "mgiID";
		public static final String dbname = "dbName";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class gxd_index_view
	{
		public static final String _name = "GXD_Index_View";
		public static final String _index_key = "_Index_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _priority_key = "_Priority_key";
		public static final String comments = "comments";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol = "symbol";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class gxd_insituresult_view
	{
		public static final String _name = "GXD_InSituResult_View";
		public static final String _result_key = "_Result_key";
		public static final String _specimen_key = "_Specimen_key";
		public static final String _strength_key = "_Strength_key";
		public static final String _pattern_key = "_Pattern_key";
		public static final String sequencenum = "sequenceNum";
		public static final String resultnote = "resultNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String strength = "strength";
		public static final String pattern = "pattern";
		
		}
	public class gxd_isresultimage_view
	{
		public static final String _name = "GXD_ISResultImage_View";
		public static final String _specimen_key = "_Specimen_key";
		public static final String sequencenum = "sequenceNum";
		public static final String _result_key = "_Result_key";
		public static final String _imagepane_key = "_ImagePane_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String figurepanelabel = "figurepaneLabel";
		public static final String _image_key = "_Image_key";
		public static final String panelabel = "paneLabel";
		public static final String figurelabel = "figureLabel";
		public static final String xdim = "xDim";
		public static final String ydim = "yDim";
		
		}
	public class gxd_isresultstructure_view
	{
		public static final String _name = "GXD_ISResultStructure_View";
		public static final String _specimen_key = "_Specimen_key";
		public static final String sequencenum = "sequenceNum";
		public static final String _result_key = "_Result_key";
		public static final String _structure_key = "_Structure_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String printname = "printName";
		public static final String stage = "stage";
		public static final String _stage_key = "_Stage_key";
		public static final String dbname = "dbName";
		
		}
	public class gxd_probeprep_view
	{
		public static final String _name = "GXD_ProbePrep_View";
		public static final String _assay_key = "_Assay_key";
		public static final String _probeprep_key = "_ProbePrep_key";
		public static final String _probe_key = "_Probe_key";
		public static final String _sense_key = "_Sense_key";
		public static final String _label_key = "_Label_key";
		public static final String _coverage_key = "_Coverage_key";
		public static final String _visualization_key = "_Visualization_key";
		public static final String type = "type";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String sense = "sense";
		public static final String label = "label";
		public static final String coverage = "coverage";
		public static final String visualization = "visualization";
		public static final String probename = "probeName";
		public static final String accid = "accID";
		
		}
	public class gxd_specimen_view
	{
		public static final String _name = "GXD_Specimen_View";
		public static final String _specimen_key = "_Specimen_key";
		public static final String _assay_key = "_Assay_key";
		public static final String _embedding_key = "_Embedding_key";
		public static final String _fixation_key = "_Fixation_key";
		public static final String _genotype_key = "_Genotype_key";
		public static final String sequencenum = "sequenceNum";
		public static final String specimenlabel = "specimenLabel";
		public static final String sex = "sex";
		public static final String age = "age";
		public static final String agemin = "ageMin";
		public static final String agemax = "ageMax";
		public static final String agenote = "ageNote";
		public static final String hybridization = "hybridization";
		public static final String specimennote = "specimenNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String embeddingmethod = "embeddingMethod";
		public static final String fixation = "fixation";
		public static final String strain = "strain";
		public static final String mgiid = "mgiID";
		
		}
	public class hmd_homology_assay_view
	{
		public static final String _name = "HMD_Homology_Assay_View";
		public static final String _homology_key = "_Homology_key";
		public static final String _assay_key = "_Assay_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String assay = "assay";
		public static final String abbrev = "abbrev";
		
		}
	public class hmd_homology_pairs_view
	{
		public static final String _name = "HMD_Homology_Pairs_View";
		public static final String _class_key = "_Class_key";
		public static final String _homology_key = "_Homology_key";
		public static final String markerkey1 = "markerkey1";
		public static final String organismkey1 = "organismkey1";
		public static final String marker1 = "marker1";
		public static final String marker2 = "marker2";
		public static final String organismkey2 = "organismkey2";
		public static final String markerkey2 = "markerkey2";
		
		}
	public class hmd_homology_view
	{
		public static final String _name = "HMD_Homology_View";
		public static final String _class_key = "_Class_key";
		public static final String _refs_key = "_Refs_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String classref = "classRef";
		public static final String _homology_key = "_Homology_key";
		public static final String _marker_key = "_Marker_key";
		public static final String symbol = "symbol";
		public static final String name = "name";
		public static final String chromosome = "chromosome";
		public static final String cytogeneticoffset = "cytogeneticOffset";
		public static final String _organism_key = "_Organism_key";
		public static final String organism = "organism";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		
		}
	public class hmd_summary_view
	{
		public static final String _name = "HMD_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class img_image_acc_view
	{
		public static final String _name = "IMG_Image_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class img_image_summary_view
	{
		public static final String _name = "IMG_Image_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class img_image_view
	{
		public static final String _name = "IMG_Image_View";
		public static final String _image_key = "_Image_key";
		public static final String _refs_key = "_Refs_key";
		public static final String xdim = "xDim";
		public static final String ydim = "yDim";
		public static final String figurelabel = "figureLabel";
		public static final String copyrightnote = "copyrightNote";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		
		}
	public class img_imagepane_view
	{
		public static final String _name = "IMG_ImagePane_View";
		public static final String _imagepane_key = "_ImagePane_key";
		public static final String _image_key = "_Image_key";
		public static final String _fieldtype_key = "_FieldType_key";
		public static final String panelabel = "paneLabel";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String fieldtype = "fieldType";
		
		}
	public class img_imagepaneref_view
	{
		public static final String _name = "IMG_ImagePaneRef_View";
		public static final String _image_key = "_Image_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _imagepane_key = "_ImagePane_key";
		public static final String panelabel = "paneLabel";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		
		}
	public class mgi_attrhistory_source_view
	{
		public static final String _name = "MGI_AttrHistory_Source_View";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String columnname = "columnName";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class mgi_attributehistory_view
	{
		public static final String _name = "MGI_AttributeHistory_View";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String columnname = "columnName";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class mgi_note_mrkgo_view
	{
		public static final String _name = "MGI_Note_MRKGO_View";
		public static final String _note_key = "_Note_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _notetype_key = "_NoteType_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String notetype = "noteType";
		public static final String note = "note";
		public static final String sequencenum = "sequenceNum";
		
		}
	public class mgi_note_nomen_view
	{
		public static final String _name = "MGI_Note_Nomen_View";
		public static final String _note_key = "_Note_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _notetype_key = "_NoteType_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String notetype = "noteType";
		public static final String note = "note";
		public static final String sequencenum = "sequenceNum";
		
		}
	public class mgi_note_sequence_view
	{
		public static final String _name = "MGI_Note_Sequence_View";
		public static final String _note_key = "_Note_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _notetype_key = "_NoteType_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String notetype = "noteType";
		public static final String note = "note";
		public static final String sequencenum = "sequenceNum";
		
		}
	public class mgi_note_source_view
	{
		public static final String _name = "MGI_Note_Source_View";
		public static final String _note_key = "_Note_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _notetype_key = "_NoteType_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String notetype = "noteType";
		public static final String note = "note";
		public static final String sequencenum = "sequenceNum";
		
		}
	public class mgi_note_vocevidence_view
	{
		public static final String _name = "MGI_Note_VocEvidence_View";
		public static final String _note_key = "_Note_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _notetype_key = "_NoteType_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String notetype = "noteType";
		public static final String note = "note";
		public static final String sequencenum = "sequenceNum";
		
		}
	public class mgi_notetype_mrkgo_view
	{
		public static final String _name = "MGI_NoteType_MRKGO_View";
		public static final String _notetype_key = "_NoteType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String notetype = "noteType";
		public static final String private = "private";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_notetype_nomen_view
	{
		public static final String _name = "MGI_NoteType_Nomen_View";
		public static final String _notetype_key = "_NoteType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String notetype = "noteType";
		public static final String private = "private";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_notetype_sequence_view
	{
		public static final String _name = "MGI_NoteType_Sequence_View";
		public static final String _notetype_key = "_NoteType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String notetype = "noteType";
		public static final String private = "private";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_notetype_source_view
	{
		public static final String _name = "MGI_NoteType_Source_View";
		public static final String _notetype_key = "_NoteType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String notetype = "noteType";
		public static final String private = "private";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_organism_acc_view
	{
		public static final String _name = "MGI_Organism_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class mgi_organism_antibody_view
	{
		public static final String _name = "MGI_Organism_Antibody_View";
		public static final String _organism_key = "_Organism_key";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_organism_antigen_view
	{
		public static final String _name = "MGI_Organism_Antigen_View";
		public static final String _organism_key = "_Organism_key";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_organism_homology_view
	{
		public static final String _name = "MGI_Organism_Homology_View";
		public static final String _organism_key = "_Organism_key";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String organism = "organism";
		
		}
	public class mgi_organism_marker_view
	{
		public static final String _name = "MGI_Organism_Marker_View";
		public static final String _organism_key = "_Organism_key";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String organism = "organism";
		
		}
	public class mgi_organism_mgitype_view
	{
		public static final String _name = "MGI_Organism_MGIType_View";
		public static final String _organism_key = "_Organism_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String typename = "typeName";
		
		}
	public class mgi_organism_probe_view
	{
		public static final String _name = "MGI_Organism_Probe_View";
		public static final String _organism_key = "_Organism_key";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_organism_sequence_view
	{
		public static final String _name = "MGI_Organism_Sequence_View";
		public static final String _organism_key = "_Organism_key";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_organism_summary_view
	{
		public static final String _name = "MGI_Organism_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class mgi_organism_view
	{
		public static final String _name = "MGI_Organism_View";
		public static final String _organism_key = "_Organism_key";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class mgi_reference_nomen_view
	{
		public static final String _name = "MGI_Reference_Nomen_View";
		public static final String _assoc_key = "_Assoc_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _refassoctype_key = "_RefAssocType_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String assoctype = "assocType";
		public static final String allowonlyone = "allowOnlyOne";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String firstauthor = "firstAuthor";
		public static final String isreviewarticle = "isReviewArticle";
		public static final String isreviewarticlestring = "isReviewArticleString";
		
		}
	public class mgi_reference_sequence_view
	{
		public static final String _name = "MGI_Reference_Sequence_View";
		public static final String _assoc_key = "_Assoc_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _refassoctype_key = "_RefAssocType_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String assoctype = "assocType";
		public static final String allowonlyone = "allowOnlyOne";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String firstauthor = "firstAuthor";
		public static final String isreviewarticle = "isReviewArticle";
		public static final String isreviewarticlestring = "isReviewArticleString";
		
		}
	public class mgi_reftype_nomen_view
	{
		public static final String _name = "MGI_RefType_Nomen_View";
		public static final String _refassoctype_key = "_RefAssocType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String assoctype = "assocType";
		public static final String allowonlyone = "allowOnlyOne";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_reftype_sequence_view
	{
		public static final String _name = "MGI_RefType_Sequence_View";
		public static final String _refassoctype_key = "_RefAssocType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String assoctype = "assocType";
		public static final String allowonlyone = "allowOnlyOne";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_set_clonelibrary_view
	{
		public static final String _name = "MGI_Set_CloneLibrary_View";
		public static final String _set_key = "_Set_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String name = "name";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_set_cloneset_view
	{
		public static final String _name = "MGI_Set_CloneSet_View";
		public static final String _setmember_key = "_SetMember_key";
		public static final String _set_key = "_Set_key";
		public static final String _object_key = "_Object_key";
		public static final String sequencenum = "sequenceNum";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String name = "name";
		
		}
	public class mgi_table_column_view
	{
		public static final String _name = "MGI_Table_Column_View";
		public static final String table_name = "table_name";
		public static final String table_description = "table_description";
		public static final String column_name = "column_name";
		public static final String column_description = "column_description";
		public static final String example = "example";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class mgi_translation_view
	{
		public static final String _name = "MGI_Translation_View";
		public static final String _translation_key = "_Translation_key";
		public static final String _translationtype_key = "_TranslationType_key";
		public static final String _object_key = "_Object_key";
		public static final String badname = "badName";
		public static final String sequencenum = "sequenceNum";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class mgi_translationstrain_view
	{
		public static final String _name = "MGI_TranslationStrain_View";
		public static final String _translation_key = "_Translation_key";
		public static final String _translationtype_key = "_TranslationType_key";
		public static final String _object_key = "_Object_key";
		public static final String badname = "badName";
		public static final String sequencenum = "sequenceNum";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String translationtype = "translationType";
		public static final String compressionchars = "compressionChars";
		public static final String regularexpression = "regularExpression";
		public static final String goodname = "goodName";
		
		}
	public class mgi_translationtype_view
	{
		public static final String _name = "MGI_TranslationType_View";
		public static final String _translationtype_key = "_TranslationType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String translationtype = "translationType";
		public static final String compressionchars = "compressionChars";
		public static final String regularexpression = "regularExpression";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String name = "name";
		public static final String tablename = "tableName";
		public static final String primarykeyname = "primaryKeyName";
		public static final String identitycolumnname = "identityColumnName";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class mgi_user_active_view
	{
		public static final String _name = "MGI_User_Active_View";
		public static final String _user_key = "_User_key";
		public static final String _usertype_key = "_UserType_key";
		public static final String _userstatus_key = "_UserStatus_key";
		public static final String login = "login";
		public static final String name = "name";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String usertype = "userType";
		
		}
	public class mlc_marker_edit_view
	{
		public static final String _name = "MLC_Marker_edit_View";
		public static final String _marker_key = "_Marker_key";
		public static final String tag = "tag";
		public static final String _marker_key_2 = "_Marker_key_2";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol = "symbol";
		public static final String tagsymbol = "tagSymbol";
		
		}
	public class mlc_marker_view
	{
		public static final String _name = "MLC_Marker_View";
		public static final String _marker_key = "_Marker_key";
		public static final String tag = "tag";
		public static final String _marker_key_2 = "_Marker_key_2";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol = "symbol";
		public static final String tagsymbol = "tagSymbol";
		
		}
	public class mld_acc_view
	{
		public static final String _name = "MLD_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class mld_concordance_view
	{
		public static final String _name = "MLD_Concordance_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String _expt_key = "_Expt_key";
		public static final String sequencenum = "sequenceNum";
		public static final String _marker_key = "_Marker_key";
		public static final String chromosome = "chromosome";
		public static final String cpp = "cpp";
		public static final String cpn = "cpn";
		public static final String cnp = "cnp";
		public static final String cnn = "cnn";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol = "symbol";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		
		}
	public class mld_distance_view
	{
		public static final String _name = "MLD_Distance_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String _expt_key = "_Expt_key";
		public static final String _marker_key_1 = "_Marker_key_1";
		public static final String _marker_key_2 = "_Marker_key_2";
		public static final String sequencenum = "sequenceNum";
		public static final String estdistance = "estDistance";
		public static final String endonuclease = "endonuclease";
		public static final String minfrag = "minFrag";
		public static final String notes = "notes";
		public static final String relativearrangecharstr = "relativeArrangeCharStr";
		public static final String units = "units";
		public static final String realisticdist = "realisticDist";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol1 = "symbol1";
		public static final String symbol2 = "symbol2";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		
		}
	public class mld_expt_marker_view
	{
		public static final String _name = "MLD_Expt_Marker_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String symbol = "symbol";
		public static final String expttype = "exptType";
		public static final String tag = "tag";
		public static final String _expt_key = "_Expt_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _allele_key = "_Allele_key";
		public static final String _assay_type_key = "_Assay_Type_key";
		public static final String sequencenum = "sequenceNum";
		public static final String gene = "gene";
		public static final String description = "description";
		public static final String matrixdata = "matrixData";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String allele = "allele";
		public static final String assay = "assay";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		
		}
	public class mld_expt_view
	{
		public static final String _name = "MLD_Expt_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String _expt_key = "_Expt_key";
		public static final String _refs_key = "_Refs_key";
		public static final String expttype = "exptType";
		public static final String tag = "tag";
		public static final String chromosome = "chromosome";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		public static final String mgiid = "mgiID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String exptlabel = "exptLabel";
		
		}
	public class mld_fish_view
	{
		public static final String _name = "MLD_FISH_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String _expt_key = "_Expt_key";
		public static final String band = "band";
		public static final String _strain_key = "_Strain_key";
		public static final String cellorigin = "cellOrigin";
		public static final String karyotype = "karyotype";
		public static final String robertsonians = "robertsonians";
		public static final String label = "label";
		public static final String nummetaphase = "numMetaphase";
		public static final String totalsingle = "totalSingle";
		public static final String totaldouble = "totalDouble";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String strain = "strain";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		
		}
	public class mld_hit_view
	{
		public static final String _name = "MLD_Hit_View";
		public static final String _expt_key = "_Expt_key";
		public static final String _refs_key = "_Refs_key";
		public static final String expttype = "exptType";
		public static final String tag = "tag";
		public static final String chromosome = "chromosome";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String probekey = "probeKey";
		public static final String probe = "probe";
		public static final String targetkey = "targetKey";
		public static final String target = "target";
		
		}
	public class mld_hybrid_view
	{
		public static final String _name = "MLD_Hybrid_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String _expt_key = "_Expt_key";
		public static final String chrsorgenes = "chrsOrGenes";
		public static final String band = "band";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		
		}
	public class mld_insitu_view
	{
		public static final String _name = "MLD_InSitu_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String _expt_key = "_Expt_key";
		public static final String band = "band";
		public static final String _strain_key = "_Strain_key";
		public static final String cellorigin = "cellOrigin";
		public static final String karyotype = "karyotype";
		public static final String robertsonians = "robertsonians";
		public static final String nummetaphase = "numMetaphase";
		public static final String totalgrains = "totalGrains";
		public static final String grainsonchrom = "grainsOnChrom";
		public static final String grainsotherchrom = "grainsOtherChrom";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String strain = "strain";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		
		}
	public class mld_marker_view
	{
		public static final String _name = "MLD_Marker_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String _refs_key = "_Refs_key";
		public static final String _marker_key = "_Marker_key";
		public static final String sequencenum = "sequenceNum";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol = "symbol";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		
		}
	public class mld_matrix_view
	{
		public static final String _name = "MLD_Matrix_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String tag = "tag";
		public static final String _expt_key = "_Expt_key";
		public static final String female = "female";
		public static final String female2 = "female2";
		public static final String male = "male";
		public static final String male2 = "male2";
		public static final String _cross_key = "_Cross_key";
		public static final String type = "type";
		public static final String _femalestrain_key = "_femaleStrain_key";
		public static final String femaleallele1 = "femaleAllele1";
		public static final String femaleallele2 = "femaleAllele2";
		public static final String _malestrain_key = "_maleStrain_key";
		public static final String maleallele1 = "maleAllele1";
		public static final String maleallele2 = "maleAllele2";
		public static final String abbrevho = "abbrevHO";
		public static final String _strainho_key = "_StrainHO_key";
		public static final String abbrevht = "abbrevHT";
		public static final String _strainht_key = "_StrainHT_key";
		public static final String whosecross = "whoseCross";
		public static final String allelefromsegparent = "alleleFromSegParent";
		public static final String f1directionknown = "F1DirectionKnown";
		public static final String nprogeny = "nProgeny";
		public static final String displayed = "displayed";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String femalestrain = "femaleStrain";
		public static final String malestrain = "maleStrain";
		public static final String strainho = "strainHO";
		public static final String strainht = "strainHT";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		
		}
	public class mld_mc2point_view
	{
		public static final String _name = "MLD_MC2point_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String _expt_key = "_Expt_key";
		public static final String _marker_key_1 = "_Marker_key_1";
		public static final String _marker_key_2 = "_Marker_key_2";
		public static final String sequencenum = "sequenceNum";
		public static final String numrecombinants = "numRecombinants";
		public static final String numparentals = "numParentals";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol1 = "symbol1";
		public static final String symbol2 = "symbol2";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		
		}
	public class mld_ri_view
	{
		public static final String _name = "MLD_RI_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		public static final String tag = "tag";
		public static final String _expt_key = "_Expt_key";
		public static final String _riset_key = "_RISet_key";
		public static final String ri_idlist = "RI_IdList";
		public static final String _strain_key_1 = "_Strain_key_1";
		public static final String _strain_key_2 = "_Strain_key_2";
		public static final String designation = "designation";
		public static final String abbrev1 = "abbrev1";
		public static final String abbrev2 = "abbrev2";
		public static final String origin = "origin";
		
		}
	public class mld_ri2point_view
	{
		public static final String _name = "MLD_RI2Point_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String _expt_key = "_Expt_key";
		public static final String _marker_key_1 = "_Marker_key_1";
		public static final String _marker_key_2 = "_Marker_key_2";
		public static final String sequencenum = "sequenceNum";
		public static final String numrecombinants = "numRecombinants";
		public static final String numtotal = "numTotal";
		public static final String ri_lines = "RI_Lines";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol1 = "symbol1";
		public static final String symbol2 = "symbol2";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		
		}
	public class mld_ridata_view
	{
		public static final String _name = "MLD_RIData_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String _expt_key = "_Expt_key";
		public static final String _marker_key = "_Marker_key";
		public static final String sequencenum = "sequenceNum";
		public static final String alleleline = "alleleLine";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol = "symbol";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		
		}
	public class mld_statistics_view
	{
		public static final String _name = "MLD_Statistics_View";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String _expt_key = "_Expt_key";
		public static final String sequencenum = "sequenceNum";
		public static final String _marker_key_1 = "_Marker_key_1";
		public static final String _marker_key_2 = "_Marker_key_2";
		public static final String recomb = "recomb";
		public static final String total = "total";
		public static final String pcntrecomb = "pcntrecomb";
		public static final String stderr = "stderr";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol1 = "symbol1";
		public static final String symbol2 = "symbol2";
		public static final String expttype = "exptType";
		public static final String tag = "tag";
		public static final String _primary = "_primary";
		public static final String authors = "authors";
		
		}
	public class mld_summary_view
	{
		public static final String _name = "MLD_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class mlp_strain_view
	{
		public static final String _name = "MLP_Strain_View";
		public static final String _strain_key = "_Strain_key";
		public static final String _species_key = "_Species_key";
		public static final String userdefined1 = "userDefined1";
		public static final String userdefined2 = "userDefined2";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String species = "species";
		public static final String strain = "strain";
		public static final String standard = "standard";
		public static final String needsreview = "needsReview";
		public static final String private = "private";
		
		}
	public class mlp_straintypes_view
	{
		public static final String _name = "MLP_StrainTypes_View";
		public static final String _strain_key = "_Strain_key";
		public static final String _straintype_key = "_StrainType_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String straintype = "strainType";
		
		}
	public class mrk_acc_view
	{
		public static final String _name = "MRK_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		public static final String _organism_key = "_Organism_key";
		
		}
	public class mrk_accnoref_view
	{
		public static final String _name = "MRK_AccNoRef_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		public static final String mgitype = "MGIType";
		public static final String subtype = "subType";
		public static final String description = "description";
		
		}
	public class mrk_accref_view
	{
		public static final String _name = "MRK_AccRef_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		public static final String mgitype = "MGIType";
		public static final String _refs_key = "_Refs_key";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String jnumid = "jnumID";
		
		}
	public class mrk_alias_view
	{
		public static final String _name = "MRK_Alias_View";
		public static final String _alias_key = "_Alias_key";
		public static final String _marker_key = "_Marker_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String alias = "alias";
		public static final String symbol = "symbol";
		
		}
	public class mrk_anchors_view
	{
		public static final String _name = "MRK_Anchors_View";
		public static final String chromosome = "chromosome";
		public static final String _marker_key = "_Marker_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol = "symbol";
		
		}
	public class mrk_classes_view
	{
		public static final String _name = "MRK_Classes_View";
		public static final String _marker_key = "_Marker_key";
		public static final String _class_key = "_Class_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol = "symbol";
		public static final String name = "name";
		
		}
	public class mrk_current_view
	{
		public static final String _name = "MRK_Current_View";
		public static final String _current_key = "_Current_key";
		public static final String _marker_key = "_Marker_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String current_symbol = "current_symbol";
		public static final String symbol = "symbol";
		public static final String chromosome = "chromosome";
		public static final String _marker_type_key = "_Marker_Type_key";
		
		}
	public class mrk_history_ref_view
	{
		public static final String _name = "MRK_History_Ref_View";
		public static final String _marker_key = "_Marker_key";
		public static final String _marker_event_key = "_Marker_Event_key";
		public static final String _marker_eventreason_key = "_Marker_EventReason_key";
		public static final String _history_key = "_History_key";
		public static final String _refs_key = "_Refs_key";
		public static final String sequencenum = "sequenceNum";
		public static final String name = "name";
		public static final String event_date = "event_date";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String event_display = "event_display";
		public static final String event = "event";
		public static final String eventreason = "eventReason";
		public static final String history = "history";
		public static final String historyname = "historyName";
		public static final String symbol = "symbol";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		
		}
	public class mrk_history_view
	{
		public static final String _name = "MRK_History_View";
		public static final String _marker_key = "_Marker_key";
		public static final String _marker_event_key = "_Marker_Event_key";
		public static final String _marker_eventreason_key = "_Marker_EventReason_key";
		public static final String _history_key = "_History_key";
		public static final String _refs_key = "_Refs_key";
		public static final String sequencenum = "sequenceNum";
		public static final String name = "name";
		public static final String event_date = "event_date";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String event_display = "event_display";
		public static final String event = "event";
		public static final String eventreason = "eventReason";
		public static final String history = "history";
		public static final String historyname = "historyName";
		public static final String symbol = "symbol";
		public static final String markername = "markerName";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class mrk_marker_view
	{
		public static final String _name = "MRK_Marker_View";
		public static final String _marker_key = "_Marker_key";
		public static final String _organism_key = "_Organism_key";
		public static final String _marker_status_key = "_Marker_Status_key";
		public static final String _marker_type_key = "_Marker_Type_key";
		public static final String _curationstate_key = "_CurationState_key";
		public static final String symbol = "symbol";
		public static final String name = "name";
		public static final String chromosome = "chromosome";
		public static final String cytogeneticoffset = "cytogeneticOffset";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String organism = "organism";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String status = "status";
		public static final String markertype = "markerType";
		public static final String curationstate = "curationState";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class mrk_mouse_view
	{
		public static final String _name = "MRK_Mouse_View";
		public static final String _marker_key = "_Marker_key";
		public static final String _organism_key = "_Organism_key";
		public static final String _marker_status_key = "_Marker_Status_key";
		public static final String _marker_type_key = "_Marker_Type_key";
		public static final String _curationstate_key = "_CurationState_key";
		public static final String symbol = "symbol";
		public static final String name = "name";
		public static final String chromosome = "chromosome";
		public static final String cytogeneticoffset = "cytogeneticOffset";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String offset = "offset";
		public static final String offset_str = "offset_str";
		public static final String organism = "organism";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String status = "status";
		public static final String mgiid = "mgiID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _accession_key = "_Accession_key";
		public static final String markertype = "markerType";
		public static final String curationstate = "curationState";
		
		}
	public class mrk_nonmouse_view
	{
		public static final String _name = "MRK_NonMouse_View";
		public static final String _marker_key = "_Marker_key";
		public static final String _organism_key = "_Organism_key";
		public static final String _marker_status_key = "_Marker_Status_key";
		public static final String _marker_type_key = "_Marker_Type_key";
		public static final String _curationstate_key = "_CurationState_key";
		public static final String symbol = "symbol";
		public static final String name = "name";
		public static final String chromosome = "chromosome";
		public static final String cytogeneticoffset = "cytogeneticOffset";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String organism = "organism";
		public static final String commonname = "commonName";
		public static final String latinname = "latinName";
		public static final String curationstate = "curationState";
		public static final String accid = "accID";
		public static final String _accession_key = "_Accession_key";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class mrk_other_view
	{
		public static final String _name = "MRK_Other_View";
		public static final String _other_key = "_Other_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _refs_key = "_Refs_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String isreviewarticle = "isReviewArticle";
		
		}
	public class mrk_reference_view
	{
		public static final String _name = "MRK_Reference_View";
		public static final String _marker_key = "_Marker_key";
		public static final String _refs_key = "_Refs_key";
		public static final String auto = "auto";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol = "symbol";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String isreviewarticle = "isReviewArticle";
		
		}
	public class mrk_summary_view
	{
		public static final String _name = "MRK_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class nom_acc_view
	{
		public static final String _name = "NOM_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class nom_accnoref_view
	{
		public static final String _name = "NOM_AccNoRef_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		public static final String mgitype = "MGIType";
		public static final String subtype = "subType";
		public static final String description = "description";
		
		}
	public class nom_accref_view
	{
		public static final String _name = "NOM_AccRef_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		public static final String mgitype = "MGIType";
		public static final String _refs_key = "_Refs_key";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		
		}
	public class nom_genefamily_view
	{
		public static final String _name = "NOM_GeneFamily_View";
		public static final String _nomen_key = "_Nomen_key";
		public static final String _genefamily_key = "_GeneFamily_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String term = "term";
		
		}
	public class nom_marker_valid_view
	{
		public static final String _name = "NOM_Marker_Valid_View";
		public static final String _nomen_key = "_Nomen_key";
		public static final String _marker_type_key = "_Marker_Type_key";
		public static final String _nomenstatus_key = "_NomenStatus_key";
		public static final String _marker_event_key = "_Marker_Event_key";
		public static final String _marker_eventreason_key = "_Marker_EventReason_key";
		public static final String _curationstate_key = "_CurationState_key";
		public static final String symbol = "symbol";
		public static final String name = "name";
		public static final String chromosome = "chromosome";
		public static final String humansymbol = "humanSymbol";
		public static final String statusnote = "statusNote";
		public static final String broadcast_date = "broadcast_date";
		public static final String _broadcastby_key = "_BroadcastBy_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String status = "status";
		public static final String event = "event";
		public static final String eventreason = "eventReason";
		public static final String markertype = "markerType";
		public static final String curationstate = "curationState";
		
		}
	public class nom_marker_view
	{
		public static final String _name = "NOM_Marker_View";
		public static final String _nomen_key = "_Nomen_key";
		public static final String _marker_type_key = "_Marker_Type_key";
		public static final String _nomenstatus_key = "_NomenStatus_key";
		public static final String _marker_event_key = "_Marker_Event_key";
		public static final String _marker_eventreason_key = "_Marker_EventReason_key";
		public static final String _curationstate_key = "_CurationState_key";
		public static final String symbol = "symbol";
		public static final String name = "name";
		public static final String chromosome = "chromosome";
		public static final String humansymbol = "humanSymbol";
		public static final String statusnote = "statusNote";
		public static final String broadcast_date = "broadcast_date";
		public static final String _broadcastby_key = "_BroadcastBy_key";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String status = "status";
		public static final String event = "event";
		public static final String eventreason = "eventReason";
		public static final String markertype = "markerType";
		public static final String curationstate = "curationState";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		public static final String broadcastby = "broadcastBy";
		
		}
	public class nom_synonym_view
	{
		public static final String _name = "NOM_Synonym_View";
		public static final String _synonym_key = "_Synonym_key";
		public static final String _nomen_key = "_Nomen_key";
		public static final String _refs_key = "_Refs_key";
		public static final String name = "name";
		public static final String isauthor = "isAuthor";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String firstauthor = "firstAuthor";
		
		}
	public class prb_acc_view
	{
		public static final String _name = "PRB_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class prb_accnoref_view
	{
		public static final String _name = "PRB_AccNoRef_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		public static final String mgitype = "MGIType";
		public static final String description = "description";
		
		}
	public class prb_accref_view
	{
		public static final String _name = "PRB_AccRef_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		public static final String mgitype = "MGIType";
		public static final String _reference_key = "_Reference_key";
		
		}
	public class prb_accrefnoseq_view
	{
		public static final String _name = "PRB_AccRefNoSeq_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		public static final String mgitype = "MGIType";
		public static final String _reference_key = "_Reference_key";
		
		}
	public class prb_marker_view
	{
		public static final String _name = "PRB_Marker_View";
		public static final String _probe_key = "_Probe_key";
		public static final String name = "name";
		public static final String _marker_key = "_Marker_key";
		public static final String symbol = "symbol";
		public static final String chromosome = "chromosome";
		public static final String relationship = "relationship";
		public static final String _refs_key = "_Refs_key";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		
		}
	public class prb_parent_view
	{
		public static final String _name = "PRB_Parent_View";
		public static final String _probe_key = "_Probe_key";
		public static final String name = "name";
		public static final String accid = "accID";
		public static final String accprefix = "accPrefix";
		public static final String accnumeric = "accNumeric";
		public static final String parentkey = "parentKey";
		public static final String parentclone = "parentClone";
		public static final String parentid = "parentID";
		public static final String parentprefix = "parentPrefix";
		public static final String parentnumeric = "parentNumeric";
		
		}
	public class prb_primer_view
	{
		public static final String _name = "PRB_Primer_View";
		public static final String _probe_key = "_Probe_key";
		public static final String name = "name";
		public static final String regioncovered = "regionCovered";
		public static final String regioncovered2 = "regionCovered2";
		public static final String _segmenttype_key = "_SegmentType_key";
		public static final String segmenttype = "segmentType";
		public static final String primer1sequence = "primer1sequence";
		public static final String primer2sequence = "primer2sequence";
		public static final String repeatunit = "repeatUnit";
		public static final String productsize = "productSize";
		public static final String moreproduct = "moreProduct";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String createdby = "createdBy";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String modifiedby = "modifiedBy";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		
		}
	public class prb_probe_view
	{
		public static final String _name = "PRB_Probe_View";
		public static final String _probe_key = "_Probe_key";
		public static final String name = "name";
		public static final String derivedfrom = "derivedFrom";
		public static final String _source_key = "_Source_key";
		public static final String _vector_key = "_Vector_key";
		public static final String _segmenttype_key = "_SegmentType_key";
		public static final String primer1sequence = "primer1sequence";
		public static final String primer2sequence = "primer2sequence";
		public static final String regioncovered = "regionCovered";
		public static final String regioncovered2 = "regionCovered2";
		public static final String insertsite = "insertSite";
		public static final String insertsize = "insertSize";
		public static final String repeatunit = "repeatUnit";
		public static final String productsize = "productSize";
		public static final String moreproduct = "moreProduct";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String segmenttype = "segmentType";
		public static final String vectortype = "vectorType";
		public static final String parentclone = "parentClone";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		public static final String mgiid = "mgiID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		
		}
	public class prb_reference_view
	{
		public static final String _name = "PRB_Reference_View";
		public static final String jnum = "jnum";
		public static final String jnumid = "jnumID";
		public static final String short_citation = "short_citation";
		public static final String _reference_key = "_Reference_key";
		public static final String _probe_key = "_Probe_key";
		public static final String _refs_key = "_Refs_key";
		public static final String holder = "holder";
		public static final String hasrmap = "hasRmap";
		public static final String hassequence = "hasSequence";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String authors = "authors";
		public static final String authors2 = "authors2";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class prb_rflv_view
	{
		public static final String _name = "PRB_RFLV_View";
		public static final String _rflv_key = "_RFLV_key";
		public static final String _reference_key = "_Reference_key";
		public static final String _marker_key = "_Marker_key";
		public static final String endonuclease = "endonuclease";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String _allele_key = "_Allele_key";
		public static final String allele = "allele";
		public static final String fragments = "fragments";
		public static final String strain = "strain";
		public static final String symbol = "symbol";
		public static final String _strain_key = "_Strain_key";
		public static final String chromosome = "chromosome";
		public static final String _organism_key = "_Organism_key";
		
		}
	public class prb_source_acc_view
	{
		public static final String _name = "PRB_Source_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class prb_source_summary_view
	{
		public static final String _name = "PRB_Source_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class prb_source_view
	{
		public static final String _name = "PRB_Source_View";
		public static final String _source_key = "_Source_key";
		public static final String _segmenttype_key = "_SegmentType_key";
		public static final String _vector_key = "_Vector_key";
		public static final String _organism_key = "_Organism_key";
		public static final String _strain_key = "_Strain_key";
		public static final String _tissue_key = "_Tissue_key";
		public static final String _gender_key = "_Gender_key";
		public static final String _cellline_key = "_CellLine_key";
		public static final String _refs_key = "_Refs_key";
		public static final String name = "name";
		public static final String description = "description";
		public static final String age = "age";
		public static final String agemin = "ageMin";
		public static final String agemax = "ageMax";
		public static final String iscuratoredited = "isCuratorEdited";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String organism = "organism";
		public static final String strain = "strain";
		public static final String sstandard = "sStandard";
		public static final String tissue = "tissue";
		public static final String tstandard = "tStandard";
		public static final String gender = "gender";
		public static final String cellline = "cellLine";
		public static final String segmenttype = "segmentType";
		public static final String vectortype = "vectorType";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class prb_sourceref_view
	{
		public static final String _name = "PRB_SourceRef_View";
		public static final String _source_key = "_Source_key";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		
		}
	public class prb_strain_acc_view
	{
		public static final String _name = "PRB_Strain_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class prb_strain_marker_view
	{
		public static final String _name = "PRB_Strain_Marker_View";
		public static final String _strainmarker_key = "_StrainMarker_key";
		public static final String _strain_key = "_Strain_key";
		public static final String _marker_key = "_Marker_key";
		public static final String _allele_key = "_Allele_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String symbol = "symbol";
		public static final String chromosome = "chromosome";
		public static final String sequencenum = "sequenceNum";
		public static final String allelesymbol = "alleleSymbol";
		
		}
	public class prb_strain_summary_view
	{
		public static final String _name = "PRB_Strain_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class prb_summary_view
	{
		public static final String _name = "PRB_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class prb_tissue_summary_view
	{
		public static final String _name = "PRB_Tissue_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class prb_view
	{
		public static final String _name = "PRB_View";
		public static final String _probe_key = "_Probe_key";
		public static final String name = "name";
		public static final String derivedfrom = "derivedFrom";
		public static final String _source_key = "_Source_key";
		public static final String _vector_key = "_Vector_key";
		public static final String _segmenttype_key = "_SegmentType_key";
		public static final String primer1sequence = "primer1sequence";
		public static final String primer2sequence = "primer2sequence";
		public static final String regioncovered = "regionCovered";
		public static final String regioncovered2 = "regionCovered2";
		public static final String insertsite = "insertSite";
		public static final String insertsize = "insertSize";
		public static final String repeatunit = "repeatUnit";
		public static final String productsize = "productSize";
		public static final String moreproduct = "moreProduct";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String segmenttype = "segmentType";
		public static final String vectortype = "vectorType";
		public static final String mgiid = "mgiID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String organism = "organism";
		public static final String age = "age";
		public static final String gender = "gender";
		public static final String cellline = "cellLine";
		public static final String library = "library";
		public static final String description = "description";
		public static final String _refs_key = "_Refs_key";
		public static final String _strain_key = "_Strain_key";
		public static final String strain = "strain";
		public static final String _tissue_key = "_Tissue_key";
		public static final String tissue = "tissue";
		
		}
	public class ri_riset_view
	{
		public static final String _name = "RI_RISet_View";
		public static final String _riset_key = "_RISet_key";
		public static final String _strain_key_1 = "_Strain_key_1";
		public static final String _strain_key_2 = "_Strain_key_2";
		public static final String designation = "designation";
		public static final String abbrev1 = "abbrev1";
		public static final String abbrev2 = "abbrev2";
		public static final String ri_idlist = "RI_IdList";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String strain1 = "strain1";
		public static final String strain2 = "strain2";
		public static final String origin = "origin";
		
		}
	public class seq_marker_view
	{
		public static final String _name = "SEQ_Marker_View";
		public static final String sequencekey = "sequenceKey";
		public static final String markerkey = "markerKey";
		public static final String sequenceid = "sequenceID";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String markerid = "markerID";
		public static final String symbol = "symbol";
		
		}
	public class seq_molecularsegment_view
	{
		public static final String _name = "SEQ_MolecularSegment_View";
		public static final String sequencekey = "sequenceKey";
		public static final String probekey = "probeKey";
		public static final String sequenceid = "sequenceID";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String probeid = "probeID";
		public static final String name = "name";
		
		}
	public class seq_sequence_acc_view
	{
		public static final String _name = "SEQ_Sequence_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class seq_sequence_accref_view
	{
		public static final String _name = "SEQ_Sequence_AccRef_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		public static final String mgitype = "MGIType";
		public static final String _refs_key = "_Refs_key";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String jnumid = "jnumID";
		
		}
	public class seq_sequence_view
	{
		public static final String _name = "SEQ_Sequence_View";
		public static final String _sequence_key = "_Sequence_key";
		public static final String _sequencetype_key = "_SequenceType_key";
		public static final String _sequencequality_key = "_SequenceQuality_key";
		public static final String _sequencestatus_key = "_SequenceStatus_key";
		public static final String _sequenceprovider_key = "_SequenceProvider_key";
		public static final String length = "length";
		public static final String description = "description";
		public static final String version = "version";
		public static final String division = "division";
		public static final String virtual = "virtual";
		public static final String rawtype = "rawType";
		public static final String rawlibrary = "rawLibrary";
		public static final String raworganism = "rawOrganism";
		public static final String rawstrain = "rawStrain";
		public static final String rawtissue = "rawTissue";
		public static final String rawage = "rawAge";
		public static final String rawsex = "rawSex";
		public static final String rawcellline = "rawCellLine";
		public static final String numberoforganisms = "numberOfOrganisms";
		public static final String seqrecord_date = "seqrecord_date";
		public static final String sequence_date = "sequence_date";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String sequencetype = "sequenceType";
		public static final String sequencequality = "sequenceQuality";
		public static final String sequencestatus = "sequenceStatus";
		public static final String sequenceprovider = "sequenceProvider";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class voc_annot_view
	{
		public static final String _name = "VOC_Annot_View";
		public static final String _annot_key = "_Annot_key";
		public static final String _annottype_key = "_AnnotType_key";
		public static final String _object_key = "_Object_key";
		public static final String _term_key = "_Term_key";
		public static final String isnot = "isNot";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String isnotcode = "isNotCode";
		public static final String term = "term";
		public static final String sequencenum = "sequenceNum";
		public static final String accid = "accID";
		public static final String _vocab_key = "_Vocab_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _evidencevocab_key = "_EvidenceVocab_key";
		public static final String annottype = "annotType";
		
		}
	public class voc_annottype_view
	{
		public static final String _name = "VOC_AnnotType_View";
		public static final String _annottype_key = "_AnnotType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String _evidencevocab_key = "_EvidenceVocab_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgitype = "mgiType";
		public static final String vocab = "vocab";
		public static final String evidencevocab = "evidenceVocab";
		
		}
	public class voc_evidence_view
	{
		public static final String _name = "VOC_Evidence_View";
		public static final String _annotevidence_key = "_AnnotEvidence_key";
		public static final String _annot_key = "_Annot_key";
		public static final String _evidenceterm_key = "_EvidenceTerm_key";
		public static final String _refs_key = "_Refs_key";
		public static final String inferredfrom = "inferredFrom";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String evidencecode = "evidenceCode";
		public static final String evidenceseqnum = "evidenceSeqNum";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String createdby = "createdBy";
		public static final String modifiedby = "modifiedBy";
		
		}
	public class voc_gomarker_annottype_view
	{
		public static final String _name = "VOC_GOMarker_AnnotType_View";
		public static final String _annottype_key = "_AnnotType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String _evidencevocab_key = "_EvidenceVocab_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgitype = "mgiType";
		public static final String vocab = "vocab";
		public static final String evidencevocab = "evidenceVocab";
		
		}
	public class voc_interpro_summary_view
	{
		public static final String _name = "VOC_InterPro_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class voc_psgenotype_annottype_view
	{
		public static final String _name = "VOC_PSGenotype_AnnotType_View";
		public static final String _annottype_key = "_AnnotType_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String _evidencevocab_key = "_EvidenceVocab_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgitype = "mgiType";
		public static final String vocab = "vocab";
		public static final String evidencevocab = "evidenceVocab";
		
		}
	public class voc_term_acc_view
	{
		public static final String _name = "VOC_Term_Acc_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String logicaldb = "LogicalDB";
		
		}
	public class voc_term_cellline_view
	{
		public static final String _name = "VOC_Term_CellLine_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_curationstate_view
	{
		public static final String _name = "VOC_Term_CurationState_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_gender_view
	{
		public static final String _name = "VOC_Term_Gender_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_genefamily_view
	{
		public static final String _name = "VOC_Term_GeneFamily_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_gxdindexassay_view
	{
		public static final String _name = "VOC_Term_GXDIndexAssay_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_gxdindexpriority_view
	{
		public static final String _name = "VOC_Term_GXDIndexPriority_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_gxdindexstage_view
	{
		public static final String _name = "VOC_Term_GXDIndexStage_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_gxdreportergene_view
	{
		public static final String _name = "VOC_Term_GXDReporterGene_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_nomenstatus_view
	{
		public static final String _name = "VOC_Term_NomenStatus_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_segmenttype_view
	{
		public static final String _name = "VOC_Term_SegmentType_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_segvectortype_view
	{
		public static final String _name = "VOC_Term_SegVectorType_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_sequenceprovider_view
	{
		public static final String _name = "VOC_Term_SequenceProvider_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_sequencequality_view
	{
		public static final String _name = "VOC_Term_SequenceQuality_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_sequencestatus_view
	{
		public static final String _name = "VOC_Term_SequenceStatus_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_sequencetype_view
	{
		public static final String _name = "VOC_Term_SequenceType_View";
		public static final String name = "name";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		
		}
	public class voc_term_summary_view
	{
		public static final String _name = "VOC_Term_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class voc_term_view
	{
		public static final String _name = "VOC_Term_View";
		public static final String _term_key = "_Term_key";
		public static final String _vocab_key = "_Vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequenceNum";
		public static final String isobsolete = "isObsolete";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String vocabname = "vocabName";
		public static final String accid = "accID";
		public static final String obsoletestate = "obsoleteState";
		
		}
	public class voc_text_view
	{
		public static final String _name = "VOC_Text_View";
		public static final String _term_key = "_Term_key";
		public static final String sequencenum = "sequenceNum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String _vocab_key = "_Vocab_key";
		public static final String termsequencenum = "termsequenceNum";
		
		}
	public class voc_vocab_dag_summary_view
	{
		public static final String _name = "VOC_Vocab_DAG_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class voc_vocab_summary_view
	{
		public static final String _name = "VOC_Vocab_Summary_View";
		public static final String _accession_key = "_Accession_key";
		public static final String accid = "accID";
		public static final String prefixpart = "prefixPart";
		public static final String numericpart = "numericPart";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String _object_key = "_Object_key";
		public static final String _mgitype_key = "_MGIType_key";
		public static final String private = "private";
		public static final String preferred = "preferred";
		public static final String _createdby_key = "_CreatedBy_key";
		public static final String _modifiedby_key = "_ModifiedBy_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String mgiid = "mgiID";
		public static final String subtype = "subType";
		public static final String description = "description";
		public static final String short_description = "short_description";
		
		}
	public class voc_vocab_view
	{
		public static final String _name = "VOC_Vocab_View";
		public static final String _vocab_key = "_Vocab_key";
		public static final String _refs_key = "_Refs_key";
		public static final String _logicaldb_key = "_LogicalDB_key";
		public static final String issimple = "isSimple";
		public static final String isprivate = "isPrivate";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String jnumid = "jnumID";
		public static final String jnum = "jnum";
		public static final String short_citation = "short_citation";
		public static final String logicaldb = "logicalDB";
		
		}
	public class voc_vocabdag_view
	{
		public static final String _name = "VOC_VocabDAG_View";
		public static final String _vocab_key = "_Vocab_key";
		public static final String _dag_key = "_DAG_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String vocab = "vocab";
		public static final String dag = "dag";
		public static final String dagabbrev = "dagAbbrev";
		
		}

	public class translationTypes
	{
				public static final int Gender = 1000
				public static final int Organisms = 1001
				public static final int Sequence Types = 1002
				public static final int Strains = 1003
		}

}

