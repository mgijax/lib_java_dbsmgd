package org.jax.mgi.dbs.mgd;

public class MGD
{
	public class acc_accession
	{
		public static final String _name = "acc_accession";
		public static final String _accession_key = "_accession_key";
		public static final String accid = "accid";
		public static final String prefixpart = "prefixpart";
		public static final String numericpart = "numericpart";
		public static final String _logicaldb_key = "_logicaldb_key";
		public static final String _object_key = "_object_key";
		public static final String _mgitype_key = "_mgitype_key";
		public static final String privateJ = "private";
		public static final String preferred = "preferred";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String release_date = "release_date";
	}
	public class acc_accessionmax
	{
		public static final String _name = "acc_accessionmax";
		public static final String prefixpart = "prefixpart";
		public static final String maxnumericpart = "maxnumericpart";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String release_date = "release_date";
	}
	public class acc_accessionreference
	{
		public static final String _name = "acc_accessionreference";
		public static final String _accession_key = "_accession_key";
		public static final String _refs_key = "_refs_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String release_date = "release_date";
	}
	public class acc_actualdb
	{
		public static final String _name = "acc_actualdb";
		public static final String _actualdb_key = "_actualdb_key";
		public static final String _logicaldb_key = "_logicaldb_key";
		public static final String name = "name";
		public static final String active = "active";
		public static final String url = "url";
		public static final String allowsmultiple = "allowsmultiple";
		public static final String delimiter = "delimiter";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String release_date = "release_date";
	}
	public class acc_logicaldb
	{
		public static final String _name = "acc_logicaldb";
		public static final String _logicaldb_key = "_logicaldb_key";
		public static final String name = "name";
		public static final String description = "description";
		public static final String _species_key = "_species_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
		public static final String release_date = "release_date";
	}
	public class acc_mgitype
	{
		public static final String _name = "acc_mgitype";
		public static final String _mgitype_key = "_mgitype_key";
		public static final String name = "name";
		public static final String tablename = "tablename";
		public static final String primarykeyname = "primarykeyname";
		public static final String identitycolumnname = "identitycolumnname";
		public static final String dbview = "dbview";
		public static final String _createdby_key = "_createdby_key";
		public static final String _modifiedby_key = "_modifiedby_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class all_allele
	{
		public static final String _name = "all_allele";
		public static final String _allele_key = "_allele_key";
		public static final String _marker_key = "_marker_key";
		public static final String _strain_key = "_strain_key";
		public static final String _mode_key = "_mode_key";
		public static final String _allele_type_key = "_allele_type_key";
		public static final String _cellline_key = "_cellline_key";
		public static final String _allele_status_key = "_allele_status_key";
		public static final String symbol = "symbol";
		public static final String name = "name";
		public static final String nomensymbol = "nomensymbol";
		public static final String createdby = "createdby";
		public static final String modifiedby = "modifiedby";
		public static final String approvedby = "approvedby";
		public static final String approval_date = "approval_date";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class all_cellline
	{
		public static final String _name = "all_cellline";
		public static final String _cellline_key = "_cellline_key";
		public static final String cellline = "cellline";
		public static final String _strain_key = "_strain_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class bib_books
	{
		public static final String _name = "bib_books";
		public static final String _refs_key = "_refs_key";
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
		public static final String _name = "bib_notes";
		public static final String _refs_key = "_refs_key";
		public static final String sequencenum = "sequencenum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class bib_refs
	{
		public static final String _name = "bib_refs";
		public static final String _refs_key = "_refs_key";
		public static final String _reviewstatus_key = "_reviewstatus_key";
		public static final String reftype = "reftype";
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
		public static final String nlmstatus = "nlmstatus";
		public static final String abstractJ = "abstract";
		public static final String isreviewarticle = "isreviewarticle";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class bib_reviewstatus
	{
		public static final String _name = "bib_reviewstatus";
		public static final String _reviewstatus_key = "_reviewstatus_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mgi_columns
	{
		public static final String _name = "mgi_columns";
		public static final String table_name = "table_name";
		public static final String column_name = "column_name";
		public static final String description = "description";
		public static final String example = "example";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mgi_dbinfo
	{
		public static final String _name = "mgi_dbinfo";
		public static final String public_version = "public_version";
		public static final String product_name = "product_name";
		public static final String schema_version = "schema_version";
		public static final String lastdump_date = "lastdump_date";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mgi_fantom2
	{
		public static final String _name = "mgi_fantom2";
		public static final String _fantom2_key = "_fantom2_key";
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
		public static final String riken_locusstatus = "riken_locusstatus";
		public static final String mgi_statuscode = "mgi_statuscode";
		public static final String mgi_numbercode = "mgi_numbercode";
		public static final String riken_numbercode = "riken_numbercode";
		public static final String cds_category = "cds_category";
		public static final String cluster_analysis = "cluster_analysis";
		public static final String gene_name_curation = "gene_name_curation";
		public static final String cds_go_curation = "cds_go_curation";
		public static final String blast_groupid = "blast_groupid";
		public static final String blast_mgiids = "blast_mgiids";
		public static final String final_mgiid = "final_mgiid";
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
		public static final String createdby = "createdby";
		public static final String modifiedby = "modifiedby";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mgi_fantom2cache
	{
		public static final String _name = "mgi_fantom2cache";
		public static final String _fantom2_key = "_fantom2_key";
		public static final String gba_mgiid = "gba_mgiid";
		public static final String gba_symbol = "gba_symbol";
		public static final String gba_name = "gba_name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mgi_fantom2notes
	{
		public static final String _name = "mgi_fantom2notes";
		public static final String _fantom2_key = "_fantom2_key";
		public static final String notetype = "notetype";
		public static final String sequencenum = "sequencenum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mgi_tables
	{
		public static final String _name = "mgi_tables";
		public static final String table_name = "table_name";
		public static final String description = "description";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mgi_translation
	{
		public static final String _name = "mgi_translation";
		public static final String _translation_key = "_translation_key";
		public static final String _translationtype_key = "_translationtype_key";
		public static final String _object_key = "_object_key";
		public static final String badname = "badname";
		public static final String sequencenum = "sequencenum";
		public static final String _createdby_key = "_createdby_key";
		public static final String _modifiedby_key = "_modifiedby_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mgi_translationtype
	{
		public static final String _name = "mgi_translationtype";
		public static final String _translationtype_key = "_translationtype_key";
		public static final String _mgitype_key = "_mgitype_key";
		public static final String translationtype = "translationtype";
		public static final String compressionchars = "compressionchars";
		public static final String regularexpression = "regularexpression";
		public static final String _createdby_key = "_createdby_key";
		public static final String _modifiedby_key = "_modifiedby_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_alias
	{
		public static final String _name = "mrk_alias";
		public static final String _alias_key = "_alias_key";
		public static final String _marker_key = "_marker_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_anchors
	{
		public static final String _name = "mrk_anchors";
		public static final String chromosome = "chromosome";
		public static final String _marker_key = "_marker_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_chromosome
	{
		public static final String _name = "mrk_chromosome";
		public static final String _species_key = "_species_key";
		public static final String chromosome = "chromosome";
		public static final String sequencenum = "sequencenum";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_class
	{
		public static final String _name = "mrk_class";
		public static final String _class_key = "_class_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_classes
	{
		public static final String _name = "mrk_classes";
		public static final String _marker_key = "_marker_key";
		public static final String _class_key = "_class_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_current
	{
		public static final String _name = "mrk_current";
		public static final String _current_key = "_current_key";
		public static final String _marker_key = "_marker_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_event
	{
		public static final String _name = "mrk_event";
		public static final String _marker_event_key = "_marker_event_key";
		public static final String event = "event";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_eventreason
	{
		public static final String _name = "mrk_eventreason";
		public static final String _marker_eventreason_key = "_marker_eventreason_key";
		public static final String eventreason = "eventreason";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_history
	{
		public static final String _name = "mrk_history";
		public static final String _marker_key = "_marker_key";
		public static final String _marker_event_key = "_marker_event_key";
		public static final String _marker_eventreason_key = "_marker_eventreason_key";
		public static final String _history_key = "_history_key";
		public static final String _refs_key = "_refs_key";
		public static final String sequencenum = "sequencenum";
		public static final String name = "name";
		public static final String event_date = "event_date";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_label
	{
		public static final String _name = "mrk_label";
		public static final String _marker_key = "_marker_key";
		public static final String _marker_status_key = "_marker_status_key";
		public static final String _species_key = "_species_key";
		public static final String label = "label";
		public static final String labeltype = "labeltype";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_marker
	{
		public static final String _name = "mrk_marker";
		public static final String _marker_key = "_marker_key";
		public static final String _species_key = "_species_key";
		public static final String _marker_status_key = "_marker_status_key";
		public static final String _marker_type_key = "_marker_type_key";
		public static final String symbol = "symbol";
		public static final String name = "name";
		public static final String chromosome = "chromosome";
		public static final String cytogeneticoffset = "cytogeneticoffset";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_notes
	{
		public static final String _name = "mrk_notes";
		public static final String _marker_key = "_marker_key";
		public static final String sequencenum = "sequencenum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_offset
	{
		public static final String _name = "mrk_offset";
		public static final String _marker_key = "_marker_key";
		public static final String source = "source";
		public static final String offset = "offset";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_other
	{
		public static final String _name = "mrk_other";
		public static final String _other_key = "_other_key";
		public static final String _marker_key = "_marker_key";
		public static final String _refs_key = "_refs_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_reference
	{
		public static final String _name = "mrk_reference";
		public static final String _marker_key = "_marker_key";
		public static final String _refs_key = "_refs_key";
		public static final String auto = "auto";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_species
	{
		public static final String _name = "mrk_species";
		public static final String _species_key = "_species_key";
		public static final String name = "name";
		public static final String species = "species";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_status
	{
		public static final String _name = "mrk_status";
		public static final String _marker_status_key = "_marker_status_key";
		public static final String status = "status";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class mrk_types
	{
		public static final String _name = "mrk_types";
		public static final String _marker_type_key = "_marker_type_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class prb_alias
	{
		public static final String _name = "prb_alias";
		public static final String _alias_key = "_alias_key";
		public static final String _reference_key = "_reference_key";
		public static final String alias = "alias";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class prb_allele
	{
		public static final String _name = "prb_allele";
		public static final String _allele_key = "_allele_key";
		public static final String _rflv_key = "_rflv_key";
		public static final String allele = "allele";
		public static final String fragments = "fragments";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class prb_marker
	{
		public static final String _name = "prb_marker";
		public static final String _probe_key = "_probe_key";
		public static final String _marker_key = "_marker_key";
		public static final String relationship = "relationship";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class prb_notes
	{
		public static final String _name = "prb_notes";
		public static final String _probe_key = "_probe_key";
		public static final String sequencenum = "sequencenum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class prb_probe
	{
		public static final String _name = "prb_probe";
		public static final String _probe_key = "_probe_key";
		public static final String name = "name";
		public static final String derivedfrom = "derivedfrom";
		public static final String _source_key = "_source_key";
		public static final String _vector_key = "_vector_key";
		public static final String primer1sequence = "primer1sequence";
		public static final String primer2sequence = "primer2sequence";
		public static final String regioncovered = "regioncovered";
		public static final String regioncovered2 = "regioncovered2";
		public static final String insertsite = "insertsite";
		public static final String insertsize = "insertsize";
		public static final String dnatype = "dnatype";
		public static final String repeatunit = "repeatunit";
		public static final String productsize = "productsize";
		public static final String moreproduct = "moreproduct";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class prb_reference
	{
		public static final String _name = "prb_reference";
		public static final String _reference_key = "_reference_key";
		public static final String _probe_key = "_probe_key";
		public static final String _refs_key = "_refs_key";
		public static final String holder = "holder";
		public static final String hasrmap = "hasrmap";
		public static final String hassequence = "hassequence";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class prb_rflv
	{
		public static final String _name = "prb_rflv";
		public static final String _rflv_key = "_rflv_key";
		public static final String _reference_key = "_reference_key";
		public static final String _marker_key = "_marker_key";
		public static final String endonuclease = "endonuclease";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class prb_source
	{
		public static final String _name = "prb_source";
		public static final String _source_key = "_source_key";
		public static final String name = "name";
		public static final String description = "description";
		public static final String _refs_key = "_refs_key";
		public static final String _probespecies_key = "_probespecies_key";
		public static final String _strain_key = "_strain_key";
		public static final String _tissue_key = "_tissue_key";
		public static final String age = "age";
		public static final String agemin = "agemin";
		public static final String agemax = "agemax";
		public static final String sex = "sex";
		public static final String cellline = "cellline";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class prb_species
	{
		public static final String _name = "prb_species";
		public static final String _probespecies_key = "_probespecies_key";
		public static final String species = "species";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class prb_strain
	{
		public static final String _name = "prb_strain";
		public static final String _strain_key = "_strain_key";
		public static final String strain = "strain";
		public static final String standard = "standard";
		public static final String needsreview = "needsreview";
		public static final String privateJ = "private";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class prb_tissue
	{
		public static final String _name = "prb_tissue";
		public static final String _tissue_key = "_tissue_key";
		public static final String tissue = "tissue";
		public static final String standard = "standard";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class seq_sequence
	{
		public static final String _name = "seq_sequence";
		public static final String _sequence_key = "_sequence_key";
		public static final String _sequencetype_key = "_sequencetype_key";
		public static final String _sequencequality_key = "_sequencequality_key";
		public static final String _sequencestatus_key = "_sequencestatus_key";
		public static final String _sequenceprovider_key = "_sequenceprovider_key";
		public static final String length = "length";
		public static final String description = "description";
		public static final String version = "version";
		public static final String division = "division";
		public static final String virtual = "virtual";
		public static final String rawtype = "rawtype";
		public static final String rawlibrary = "rawlibrary";
		public static final String raworganism = "raworganism";
		public static final String rawstrain = "rawstrain";
		public static final String rawtissue = "rawtissue";
		public static final String rawage = "rawage";
		public static final String rawsex = "rawsex";
		public static final String rawcellline = "rawcellline";
		public static final String numberoforganisms = "numberoforganisms";
		public static final String seqrecord_date = "seqrecord_date";
		public static final String sequence_date = "sequence_date";
		public static final String _createdby_key = "_createdby_key";
		public static final String _modifiedby_key = "_modifiedby_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class test_dbkeyedint
	{
		public static final String _name = "test_dbkeyedint";
		public static final String columna = "columna";
		public static final String columnb = "columnb";
	}
	public class voc_annot
	{
		public static final String _name = "voc_annot";
		public static final String _annot_key = "_annot_key";
		public static final String _annottype_key = "_annottype_key";
		public static final String _object_key = "_object_key";
		public static final String _term_key = "_term_key";
		public static final String isnot = "isnot";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class voc_annottype
	{
		public static final String _name = "voc_annottype";
		public static final String _annottype_key = "_annottype_key";
		public static final String _mgitype_key = "_mgitype_key";
		public static final String _vocab_key = "_vocab_key";
		public static final String _evidencevocab_key = "_evidencevocab_key";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class voc_evidence
	{
		public static final String _name = "voc_evidence";
		public static final String _annot_key = "_annot_key";
		public static final String _evidenceterm_key = "_evidenceterm_key";
		public static final String _refs_key = "_refs_key";
		public static final String inferredfrom = "inferredfrom";
		public static final String createdby = "createdby";
		public static final String modifiedby = "modifiedby";
		public static final String notes = "notes";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class voc_synonym
	{
		public static final String _name = "voc_synonym";
		public static final String _synonym_key = "_synonym_key";
		public static final String _term_key = "_term_key";
		public static final String synonym = "synonym";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class voc_term
	{
		public static final String _name = "voc_term";
		public static final String _term_key = "_term_key";
		public static final String _vocab_key = "_vocab_key";
		public static final String term = "term";
		public static final String abbreviation = "abbreviation";
		public static final String sequencenum = "sequencenum";
		public static final String isobsolete = "isobsolete";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class voc_text
	{
		public static final String _name = "voc_text";
		public static final String _term_key = "_term_key";
		public static final String sequencenum = "sequencenum";
		public static final String note = "note";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class voc_vocab
	{
		public static final String _name = "voc_vocab";
		public static final String _vocab_key = "_vocab_key";
		public static final String _refs_key = "_refs_key";
		public static final String _logicaldb_key = "_logicaldb_key";
		public static final String issimple = "issimple";
		public static final String isprivate = "isprivate";
		public static final String name = "name";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
	public class voc_vocabdag
	{
		public static final String _name = "voc_vocabdag";
		public static final String _vocab_key = "_vocab_key";
		public static final String _dag_key = "_dag_key";
		public static final String creation_date = "creation_date";
		public static final String modification_date = "modification_date";
	}
}
