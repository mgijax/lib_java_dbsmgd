package org.jax.mgi.shr.datafactory;

import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayList;
import org.jax.mgi.shr.stringutil.Sprintf;
import org.jax.mgi.shr.stringutil.StringLib;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dto.DTOConstants;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.ListHash;

public class SequenceFactory extends Factory {

    public SequenceFactory (DataFactoryCfg config,
                            SQLDataManager sqlDM,
                            Logger logger) {
        super(config,sqlDM,logger,"SequenceFactory");

    }

    public String getKey(Map parms) throws DBException {

        // if a 'key' is directly specified in the 'parms', then assume it is
        // a sequence key, and return it...

        String keyStr = StringLib.getFirst((String[]) parms.get ("key"));
        if (keyStr != null) {
            this.timeStamp ("Used key parameter directly for sequence key");
            return keyStr;
        }

        // otherwise, our second choice is a seqID...
        int key = -1;
        String accID = StringLib.getFirst ((String[]) parms.get ("id"));
        if (accID != null) {
            key = this.getKeyForID (accID);
            if (key != -1) {
                this.timeStamp ("Retrieved marker key from database");
                return Integer.toString(key);
            }
        }
        return null;        // no key could be found
    }

    public int getKeyForID (String accID) throws DBException {
        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        int key = -1;

        nav = this.sqlDM.executeQuery(Sprintf.sprintf(SEQ_KEY, accID));
        if(nav.next()) {
            rr = (RowReference)nav.getCurrent();
            key = rr.getInt(1).intValue();
        }

        return key;
    }
    public DTO getFullInfo (Map parms) throws DBException {

        DTO section = DTO.getDTO();
        DTO sequence = DTO.getDTO();
        // marker key as a String
        String keyStr = getKey (parms);

        // if we could not find a marker key based on 'parms', then bail out
        // before bothering with anything else
        if (keyStr == null)
        {
            this.logger.logInfo ("Could not find sequence");
            return sequence;
        }
        // sequence key as an int
        int key = Integer.parseInt (keyStr);



        section = getBasicInfo(key);
        sequence.merge (section);
        DTO.putDTO (section);

        section = getChromosome(key);
        sequence.merge (section);
        DTO.putDTO (section);

        section = getMarkerInfo(key);
        sequence.merge (section);
        DTO.putDTO (section);

        section = getReferenceInfo(key);
        sequence.merge (section);
        DTO.putDTO (section);

        section = getProbes(key);
        sequence.merge (section);
        DTO.putDTO (section);

        return sequence;
    }

    public DTO getBasicInfo (int key) throws DBException {
        DTO section = DTO.getDTO();
        DTO sequence = DTO.getDTO();

        section.set(DTOConstants.SequenceKey,new Integer(key).toString());
        sequence.merge(section);

        section = getACCIDs(key);
        sequence.merge(section);
        DTO.putDTO(section);

        section = getSequenceVersion(key);
        sequence.merge (section);
        DTO.putDTO (section);

        section = getSequenceDescription(key);
        sequence.merge (section);
        DTO.putDTO (section);

        section = getSequenceProvider(key);
        sequence.merge (section);
        DTO.putDTO (section);

        section = getSequenceStatus(key);
        sequence.merge (section);
        DTO.putDTO (section);

        section = getSequenceType(key);
        sequence.merge (section);
        DTO.putDTO (section);

        section = getSequenceLength(key);
        sequence.merge (section);
        DTO.putDTO (section);

        section = getSequenceSource(key);
        sequence.merge (section);
        DTO.putDTO (section);

        return sequence;
    }


    public DTO getACCIDs(int key) throws DBException {
        ResultsNavigator nav = null;    // set of query results
        RowReference rr = null;         // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String id;                      //the accID of the current row
        String adbURL;                  //the actualDB URL in the current row
        String adbName;                 //the actualDB name in the current row
        Integer pref = new Integer(0);  //1 if the accID of this row is preferred.

        String lastID = "";             //the accID of the last row


        ArrayList adbs = new ArrayList(); //       Key:ADB name
                                        //       Value:ADB URL
        HashMap adb = new HashMap();
        HashMap ids = new HashMap();    //Key:An accID associated with this sequence
                                        //Value: A Hashmap of all associated ActualDBs
                                        //       Key:ADB name
                                        //       Value:ADB URL

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(ACC_IDS, key));

        while(nav.next()) {
            rr = (RowReference) nav.getCurrent();
            id = rr.getString(1);
            pref = rr.getInt(2);
            adbURL = rr.getString(3);
            adbName = rr.getString(4);
            adbURL.replaceAll("@@@@","%s");
            adb = new HashMap();

            if (!id.equals(lastID)) { //this is a new ID
                if(!lastID.equals(""))
                    ids.put(lastID,adbs);
                adbs = new ArrayList();
            }
            adb.put(adbName,adbURL);
            adbs.add(adb);

            if(pref.intValue() == 1) {
                sequence.set(DTOConstants.PrimaryAccID, id);
            }
            lastID = id;
        }
        ids.put(lastID,adbs);
        sequence.set(DTOConstants.AccIDs, ids);
        nav.close();
        return sequence;
    }


    public DTO getSequenceVersion(int key) throws DBException {
        ResultsNavigator nav = null;
        RowReference rr = null;         // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String version;
        String seqrecord_date;
        String sequence_date;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_VER, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            version = rr.getString(1);
            seqrecord_date = rr.getString(2);
            sequence_date = rr.getString(3);
            sequence.set(DTOConstants.SequenceVersion, version);
            sequence.set(DTOConstants.SequenceRecordDate, seqrecord_date);
            sequence.set(DTOConstants.SequenceDate, sequence_date);

        }
        nav.close();

        return sequence;
    }


    public DTO getSequenceDescription(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String desc;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_DESCRIPTION, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            desc = rr.getString(1);
            sequence.set(DTOConstants.SequenceDescription, desc);
        }
        nav.close();

        return sequence;
    }

    public DTO getSequenceProvider(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String provider;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_PROVIDER, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            provider = rr.getString(1);
            sequence.set(DTOConstants.SequenceProvider, provider);
        }
        nav.close();

        return sequence;
    }

    public DTO getSequenceStatus(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String status;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_STATUS, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            status = rr.getString(1);
            sequence.set(DTOConstants.SequenceStatus, status);
        }
        nav.close();

        return sequence;
    }

    public DTO getSequenceType(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String type;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_TYPE, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            type = rr.getString(1);
            sequence.set(DTOConstants.SequenceType, type);
        }
        nav.close();

        return sequence;
    }

    public DTO getSequenceLength(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        Integer len;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_LENGTH, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            len= rr.getInt(1);
            sequence.set(DTOConstants.SequenceLength, len.toString());
        }
        nav.close();

        return sequence;
    }

    public DTO getSequenceSource(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String age, cellLine, gender, library, organism, strain, tissue;
        String NOT_SPECIFIED = "Not Specified";

        this.sqlDM.execute(Sprintf.sprintf(SEQ_SOURCE_TABLE, key));
        this.sqlDM.execute(RAW_LIBRARY);
        this.sqlDM.execute(RAW_ORGANISM);
        this.sqlDM.execute(RAW_STRAIN);
        this.sqlDM.execute(RAW_TISSUE);
        this.sqlDM.execute(RAW_AGE);
        this.sqlDM.execute(RAW_SEX);
        this.sqlDM.execute(RAW_CELLLINE);
        nav = this.sqlDM.executeQuery(SEQ_SOURCE);
        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            library = rr.getString(1);
            if(library.equals("*"))
                library = NOT_SPECIFIED;
            organism = rr.getString(2);
            if(organism.equals("*"))
                organism = NOT_SPECIFIED;
            strain = rr.getString(3);
            if(strain.equals("*"))
                strain = NOT_SPECIFIED;
            tissue = rr.getString(4);
            if(tissue.equals("*"))
                tissue = NOT_SPECIFIED;
            age = rr.getString(5);
            if(age.equals("*"))
                age = NOT_SPECIFIED;
            gender = rr.getString(6);
            if(gender.equals("*"))
                gender = NOT_SPECIFIED;
            cellLine = rr.getString(7);
            if(cellLine.equals("*"))
                cellLine = NOT_SPECIFIED;
            sequence.set(DTOConstants.Age, age);
            sequence.set(DTOConstants.CellLine, cellLine);
            sequence.set(DTOConstants.Gender, gender);
            sequence.set(DTOConstants.Library, library);
            sequence.set(DTOConstants.Organism, organism);
            sequence.set(DTOConstants.Strain, strain);
            sequence.set(DTOConstants.Tissue, tissue);
        }
        nav.close();

        return sequence;
    }

    public DTO getChromosome(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String chr;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(CHROMOSOME, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            chr = rr.getString(1);
            sequence.set(DTOConstants.Chromosome, chr);
        }
        nav.close();

        return sequence;
    }

    public DTO getMarkerInfo(int key) throws DBException {
        ResultsNavigator nav = null;    // set of query results
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        Vector allMarkers = new Vector();
        DTO marker = DTO.getDTO();

        this.sqlDM.execute(MARKER_TABLE);
        this.sqlDM.execute(Sprintf.sprintf(MARKER_NOMENCLATURE, key));
        this.sqlDM.execute(MARKER_GO_COUNT);
        this.sqlDM.execute(MARKER_GO_UPDATE);
        this.sqlDM.execute(MARKER_ASSAY_COUNT);
        this.sqlDM.execute(MARKER_ASSAY_UPDATE);
        this.sqlDM.execute(MARKER_ORTHOLOGY_COUNT);
        this.sqlDM.execute(MARKER_ORTHOLOGY_UPDATE);
        this.sqlDM.execute(MARKER_ALLELE_COUNT);
        this.sqlDM.execute(MARKER_ALLELE_UPDATE);
        this.sqlDM.execute(MARKER_REF);
        nav = this.sqlDM.executeQuery(MARKER_INFO);
        while (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            marker.set(DTOConstants.MarkerKey,rr.getInt(1));
            marker.set(DTOConstants.MarkerSymbol,rr.getString(2));
            marker.set(DTOConstants.MarkerName,rr.getString(3));
            marker.set(DTOConstants.MarkerType,rr.getString(4));
            marker.set(DTOConstants.PhenoCount,rr.getString(5));
            marker.set(DTOConstants.ExpressionAssayCount, rr.getString(6));
            marker.set(DTOConstants.GOAnnotationCount, rr.getString(7));
            marker.set(DTOConstants.OrthologCount, rr.getString(8));
            marker.set(DTOConstants.RefsKey, rr.getInt(9));
            marker.set(DTOConstants.RefID, rr.getString(10));
            allMarkers.add(marker);
            marker = DTO.getDTO();
        }
        sequence.set(DTOConstants.Markers,allMarkers);
        return sequence;
    }

    public DTO getReferenceInfo(int key) throws DBException {
        ResultsNavigator nav = null;
        RowReference rr = null;
        DTO sequence = DTO.getDTO();
        Vector allRefs = new Vector();
        DTO ref = DTO.getDTO();

        nav = this.sqlDM.executeQuery(Sprintf.sprintf(REFS, key));
        while (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            ref = makeReferenceDTO(rr);
            allRefs.add(ref);
        }
        sequence.set(DTOConstants.References, allRefs);
        return sequence;
    }
    /** create a DTO describing the reference identified in 'rr'.
    * @param rr a row from a database query containing at least these fields:
    *    <OL>
    *    <LI> _Refs_key : integer
    *    <LI> J number : string
    *    <LI> authors part 1 : string
    *    <LI> authors part 2 : string
    *    <LI> title part 1 : string
    *    <LI> title part 2 : string
    *    <LI> citation : string
    *    </OL>
    * @return DTO a data transfer object describing this reference.  The
    *    fields (defined in DTOConstants) included are:
    *    <OL>
    *    <LI> RefsKey : Integer
    *    <LI> Jnum : String
    *    <LI> Citation : String
    *    <LI> RefsTitle : String
    *    <LI> Authors : ArrayList of Strings, each the name of an author;
    *        null if there are no authors
    *    </OL>
    * @assumes nothing
    * @effects nothing
    * @throws DBException if there are problems retrieving the fields from
    *    'rr'
    */
    protected DTO makeReferenceDTO (RowReference rr)
            throws DBException {

        DTO ref = DTO.getDTO();
        ref.set (DTOConstants.RefsKey, rr.getInt(1));
        ref.set (DTOConstants.Jnum, rr.getString(2));
        ref.set (DTOConstants.Citation, rr.getString(7));

        // title is stored in two database fields since varchar fields are
        // limited to 256 characters each

        ref.set (DTOConstants.RefsTitle,
                combine (rr.getString(5), rr.getString(6)) );

        // authors are stored in two database fields since varchar fields are
        // limited to 256 characters each.  A semi-colon is used to delimit
        // the lists of authors.

        String authors = combine (rr.getString(3), rr.getString(4));
        if (authors != null) {
            ref.set (DTOConstants.Authors,
            (ArrayList) StringLib.split (authors, ";") );
        } else {
            ref.set (DTOConstants.Authors, null);
        }
        return ref;
    }

    public DTO getProbes(int key) throws DBException {
        ResultsNavigator nav = null;
        RowReference rr = null;
        DTO sequence = DTO.getDTO();
        Vector allProbes = new Vector();
        DTO probe = DTO.getDTO();

        this.sqlDM.execute(PROBE_TABLE);
        this.sqlDM.execute(Sprintf.sprintf(PROBES, key));
        this.sqlDM.execute(CLONE_COLLECTION);
        nav = this.sqlDM.executeQuery(PROBE_INFO);

        while (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            probe.set(DTOConstants.ProbeKey, rr.getInt(1));
            probe.set(DTOConstants.ProbeName, rr.getString(2));
            probe.set(DTOConstants.AccID, rr.getString(3));
            probe.set(DTOConstants.CloneID, rr.getString(4));
            probe.set(DTOConstants.SegmentType, rr.getString(5));
            probe.set(DTOConstants.CloneCollection, rr.getString(6));
            allProbes.add(probe);
            probe = DTO.getDTO();
        }
        sequence.set(DTOConstants.Probes, allProbes);
        return sequence;
    }



    private static final String SEQ_KEY =
            "select ss._Sequence_key\n"+
            "from SEQ_Sequence ss, ACC_Accession aa\n"+
            "where aa.accID = '%s'\n"+
            "and ss._Sequence_key = aa._Object_key\n"+
            "and aa._MGIType_key = 19";

    private static final String ACC_IDS =
            "select aa.accID, aa.preferred,adb.url, adb.name\n"+
            "from ACC_Accession aa, ACC_ActualDB adb, MGI_SetMember msm\n"+
            "where aa._Object_key = %d\n"+
            "and aa._MGIType_key = 19\n"+
            "and aa._LogicalDB_key = adb._LogicalDB_key\n"+
            "and msm._Object_key = adb._ActualDB_key\n"+
            "and msm._Set_key = 1009\n"+
            "order by aa.accID, msm.sequenceNum";

    private static final String SEQ_VER =
            "select version, seqrecord_date, sequence_date\n"+
            "from SEQ_Sequence\n"+
            "where _Sequence_key = %d";

    private static final String SEQ_DESCRIPTION =
            "select description\n" +
            "from SEQ_Sequence\n" +
            "where _Sequence_key = %d";

    private static final String SEQ_PROVIDER =
            "select vt.term\n"+
            "from VOC_Term vt, SEQ_Sequence ss\n"+
            "where ss._Sequence_key = %d\n"+
            "and vt._Term_key = ss._SequenceProvider_key";

    private static final String SEQ_STATUS =
            "select vt.term\n"+
            "from VOC_Term vt, SEQ_Sequence ss\n"+
            "where ss._Sequence_key = %d\n"+
            "and vt._Term_key = ss._SequenceStatus_key";

    private static final String SEQ_TYPE =
            "select vt.term\n"+
            "from VOC_Term vt, SEQ_Sequence ss\n"+
            "where ss._Sequence_key = %d\n"+
            "and vt._Term_key = ss._SequenceType_key";

    private static final String SEQ_LENGTH =
            "select length\n"+
            "from SEQ_Sequence\n"+
            "where _Sequence_key = %d";

    private static final String SEQ_SOURCE_TABLE =
            "select _Sequence_key,\n"+
            "    library = psv.name,\n"+
            "    organism = psv.organism,\n"+
            "    strain = psv.strain,\n"+
            "    tissue = psv.tissue,\n"+
            "    age = psv.age,\n"+
            "    sex = psv.gender,\n"+
            "    cellLine = psv.cellLine\n"+
            "into #seqSource\n"+
            "from PRB_Source_View psv, SEQ_Source_Assoc ssa\n"+
            "where ssa._Sequence_key = %d\n"+
            "and ssa._Source_key = psv._Source_key";

    private static final String RAW_LIBRARY =
            "update #seqSource\n"+
            "set library = ss.rawLibrary + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.library = 'Not Resolved' \n"+
            "or  s.library = NULL";

    private static final String RAW_ORGANISM =
            "update #seqSource\n"+
            "set organism = ss.rawOrganism + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.organism = 'Not Resolved'";

    private static final String RAW_STRAIN =
            "update #seqSource\n"+
            "set strain = ss.rawStrain + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.strain = 'Not Resolved'";

    private static final String RAW_TISSUE =
            "update #seqSource\n"+
            "set tissue = ss.rawTissue + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.tissue = 'Not Resolved'";

    private static final String RAW_AGE =
            "update #seqSource\n"+
            "set age = ss.rawAge + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.age = 'Not Resolved'";

    private static final String RAW_SEX =
            "update #seqSource\n"+
            "set sex = ss.rawSex + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.sex = 'Not Resolved'";

    private static final String RAW_CELLLINE =
            "update #seqSource\n"+
            "set cellLine = ss.rawCellLine + '*'\n"+
            "from SEQ_Sequence ss, #seqSource s\n"+
            "where ss._Sequence_key = s._Sequence_key\n"+
            "and s.cellLine = 'Not Resolved'";

    private static final String SEQ_SOURCE =
            "select library,organism,strain,tissue,age,sex,cellLine\n"+
            "from #seqSource";

    private static final String CHROMOSOME =
            "select distinct mm.chromosome\n"+
            "from MRK_Marker mm, SEQ_Marker_Cache smc\n"+
            "where smc._Sequence_key = %d\n"+
            "and mm._Marker_key = smc._Marker_key";

    private static final String MARKER_TABLE =
            "CREATE TABLE #mrk(\n"+
            "_Marker_key  int NOT NULL,\n"+
            "markerType   varchar(255)  NOT NULL,\n"+
            "assayCount   int default 0 NULL,\n"+
            "alleleCount  int default 0 NULL,\n"+
            "GOCount      int default 0 NULL,\n"+
            "orthoCount   int default 0 NULL,\n"+
            "name         varchar(255)  NOT NULL,\n"+
            "symbol       varchar(25)   NOT NULL,\n"+
            "_Refs_key    int NULL,\n"+
            "refID        varchar(255) NULL\n"+
            ")";

    private static final String MARKER_NOMENCLATURE =
            "insert #mrk(_Marker_key,name,symbol,markerType)\n"+
            "select mm._Marker_key, mm.name, mm.symbol, mt.name\n"+
            "from MRK_Marker mm, SEQ_Marker_Cache smc, MRK_Types mt\n"+
            "where smc._Sequence_key = %s\n"+
            "and mm._Marker_key = smc._Marker_key\n"+
            "and mt._Marker_Type_key = mm._Marker_Type_key";

    private static final String MARKER_GO_COUNT =
            "select GOCount = count(vt.term), m._Marker_key\n"+
            "into #goCnt\n"+
            "from VOC_Term vt, VOC_Annot va, #mrk m\n"+
            "where m._Marker_key = va._Object_key\n"+
            "and va._AnnotType_key = 1000\n"+
            "and va._Term_key = vt._Term_key\n"+
            "group by m._Marker_key";

    private static final String MARKER_GO_UPDATE =
            "update #mrk\n"+
            "set goCount = gc.goCount\n"+
            "from #mrk m, #goCnt gc\n"+
            "where m._Marker_key = gc._Marker_key\n";

    private static final String MARKER_ASSAY_COUNT =
            "select assayCount = count(ga._Assay_key), m._Marker_key\n"+
            "into #assayCnt\n"+
            "from #mrk m, GXD_Assay ga\n"+
            "where m._Marker_key = ga._Marker_key\n"+
            "group by m._Marker_key";

    private static final String MARKER_ASSAY_UPDATE =
            "update #mrk\n"+
            "set assayCount = ac.assayCount\n"+
            "from #mrk m, #assayCnt ac\n"+
            "where m._Marker_key = ac._Marker_key\n";

    private static final String MARKER_ORTHOLOGY_COUNT =
            "select orthoCount = count(hhm._Homology_key), m._Marker_key\n"+
            "into #orthoCnt\n"+
            "from HMD_Homology_Marker hhm, #mrk m\n"+
            "where m._Marker_key = hhm._Marker_key\n"+
            "group by m._Marker_key";

    private static final String MARKER_ORTHOLOGY_UPDATE =
            "update #mrk\n"+
            "set orthoCount = oc.orthoCount\n"+
            "from #mrk m, #orthoCnt oc\n"+
            "where m._Marker_key = oc._Marker_key\n";

    private static final String MARKER_ALLELE_COUNT =
            "select alleleCount = count(al._Allele_key), m._Marker_key\n"+
            "into #alleleCnt\n"+
            "from #mrk m, ALL_Allele al\n"+
            "where m._Marker_key = al._Marker_key\n"+
            "group by m._Marker_key";

    private static final String MARKER_ALLELE_UPDATE =
            "update #mrk\n"+
            "set alleleCount = ac.alleleCount\n"+
            "from #mrk m, #alleleCnt ac\n"+
            "where m._Marker_key = ac._Marker_key\n";

    private static final String MARKER_REF =
            "update #mrk\n"+
            "set _Refs_key = smc._Refs_key, refID = aa.accID\n"+
            "from SEQ_Marker_Cache smc, ACC_Accession aa, #mrk m\n"+
            "where smc._Refs_key = aa._Object_key\n"+
            "and m._Marker_key = smc._Marker_key\n"+
            "and aa._MGIType_key = 1\n"+
            "and aa.prefixPart = 'J:'\n"+
            "and aa.preferred = 1";

    private static final String MARKER_INFO =
            "select _Marker_key, symbol, name, markerType,\n"+
            "alleleCount,assayCount,GOCount,orthoCount,\n"+
            "_Refs_key, refID\n"+
            "from #mrk\n"+
            "order by symbol";

    private static final String REFS =
            "SELECT br._Refs_key, aa.accID, br.authors, br.authors2,\n"+
            "br.title, br.title2,\n"+
            "citation = br.journal + ' ' + br.date + ';' + br.vol "+
            "+ '(' + br.issue + '):' + br.pgs \n"+
            "from MGI_Reference_Assoc mra, BIB_Refs br, ACC_Accession aa\n"+
            "where mra._Object_key = %d\n"+
            "and mra._MGIType_key = 19\n"+
            "and br._Refs_key = mra._Refs_key\n"+
            "and aa._MGIType_key = 1\n"+
            "and aa.prefixPart = 'J:'\n"+
            "and aa._Object_key = br._Refs_key";

    private static final String PROBE_TABLE =
            "CREATE TABLE #prbs(\n"+
            "_Probe_key   int NOT NULL,\n"+
            "name         varchar(40)  NOT NULL,\n"+
            "mgiID        varchar(30)  NOT NULL,\n"+
            "accID        varchar(30)  NULL,\n"+
            "segmentType  varchar(255) NULL,\n"+
            "collection   varchar(80)  NULL\n"+
            ")";


    private static final String PROBES =
            "insert #prbs(name,_Probe_key,segmentType, mgiID)\n"+
            "select distinct pp.name, pp._Probe_key, vt.term, aa.accID\n"+
            "from PRB_Probe pp, SEQ_Probe_Cache sqc, VOC_Term vt, ACC_Accession aa\n"+
            "where sqc._Sequence_key = %d\n"+
            "and pp._Probe_key = sqc._Probe_key\n"+
            "and aa._Object_key = pp._Probe_key\n"+
            "and aa._MGIType_key = 3\n"+
            "and aa.preferred = 1\n"+
            "and aa.prefixPart = 'MGI:'\n"+
            "and vt._Term_key = pp._SegmentType_key";

    private static final String CLONE_COLLECTION =
            "update #prbs\n"+
            "set collection = ldb.name, accID = aa.accID\n"+
            "from ACC_LogicalDB ldb, ACC_Accession aa, #prbs p\n"+
            "where p._Probe_key = aa._Object_key\n"+
            "and aa._MGIType_key = 3\n"+
            "and ldb._LogicalDB_key = aa._LogicalDB_key\n"+
            "and aa._LogicalDB_key in (select _Object_key\n"+
            "                          from MGI_SetMember\n"+
            "                          where _Set_key = 1000)";

    private static final String PROBE_INFO =
            "select _Probe_key, name, mgiID, accID,segmentType, collection\n"+
            "from #prbs";
}
