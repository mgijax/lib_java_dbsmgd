package org.jax.mgi.shr.datafactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Vector;
import org.jax.mgi.shr.stringutil.Sprintf;
import org.jax.mgi.shr.stringutil.StringLib;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dto.DTOConstants;
import org.jax.mgi.shr.cache.ExpiringObjectCache;
import org.jax.mgi.shr.timing.TimeStamper;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.DBException;

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
            rr = (RowReference)nav.current();
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
            this.logInfo ("Could not find sequence");
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

        section = getCloneCollection(key);
        sequence.merge (section);
        DTO.putDTO (section);

        section = getCloneID(key);
        sequence.merge (section);
        DTO.putDTO (section);

        section = getSegmentType(key);
        sequence.merge (section);
        DTO.putDTO (section);


        return DTO.getDTO();
    }

    public DTO getBasicInfo (int key) throws DBException {
        DTO section = DTO.getDTO();
        DTO sequence = DTO.getDTO();

        section.set(DTOConstants.SequenceKey,key);

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


        HashMap adbs = new HashMap();   //       Key:ADB name
                                        //       Value:ADB URL

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

            adbURL.replace("@@@@","%s");

            if (id != lastID) { //this is a new ID
                ids.put(lastID,adbs);
                adbs = new HashMap();
            }
            adbs.put(adbName,adbURL);
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
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String version;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_VER, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            version = rr.getString(1);
            sequence.set(DTOConstants.SequenceVersion, version);
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
                        Sprintf.sprintf(SEQ_DESCRIPTION, key));

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
                        Sprintf.sprintf(SEQ_DESCRIPTION, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            len= rr.getInt(1);
            sequence.set(DTOConstants.SequenceLength, len);
        }
        nav.close();

        return sequence;
    }

    public DTO getSequenceSource(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String age, cellLine, gender, library, organism, strain, tissue;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_DESCRIPTION, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            age = rr.getString(1);
            cellLine = rr.getString(2);
            gender = rr.getString(3);
            //library = rr.getString(1);
            organism = rr.getString(4);
            strain = rr.getString(5);
            tissue = rr.getString(6);
            sequence.set(DTOConstants.Age, age);
            sequence.set(DTOConstants.CellLine, cellLine);
            sequence.set(DTOConstants.Gender, gender);
            //sequence.set(DTOConstants.Library, library);
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
        Integer chr;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEQ_DESCRIPTION, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            chr = rr.getInt(1);
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

        this.sqlDM.executeQuery(MARKER_TABLE);
        this.sqlDM.executeQuery(Sprintf.sprintf(MARKER_NOMENCLATURE, key));
        this.sqlDM.executeQuery(MARKER_GO_COUNT);
        this.sqlDM.executeQuery(MARKER_ASSAY_COUNT);
        this.sqlDM.executeQuery(MARKER_ORTHOLOGY_COUNT);
        this.sqlDM.executeQuery(MARKER_ALLELE_COUNT);
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
        Vector allRefs = DTO.getDTO();
        DTO ref = DTO.getDTO();

        nav = this.sqlDM.executeQuery(Sprintf.sprintf(REFS, key));
        while (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            ref.set(DTOConstants.RefsKey, rr.getInt(1));
            ref.set(DTOConstants.AccID, rr.getString(2));
            allRefs.add(ref);
            ref = DTO.getDTO();
        }
        sequence.set(DTOConstants.References, allRefs);
        return sequence;
    }

    public DTO getProbes(int key) throws DBException {
        ResultsNavigator nav = null;
        RowReference rr = null;
        DTO sequence = DTO.getDTO();
        Vector allProbes = DTO.getDTO();
        DTO probe = DTO.getDTO();

        nav = this.sqlDM.executeQuery(Sprintf.sprintf(PROBES, key));
        while (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            probe.set(DTOConstants.ProbeName, rr.getString(1));
            probe.set(DTOConstants.ProbeKey, rr.getInt(2));
            allProbes.add(probe);
            probe = DTO.getDTO();
        }
        sequence.set(DTOConstants.Probes, allProbes);
        return sequence;
    }

    public DTO getCloneCollection(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String collection;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(CLONE_COLLECTION, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            collection = rr.getString(1);
            sequence.set(DTOConstants.CloneCollection, collection);
        }
        nav.close();

        return sequence;
    }


    public DTO getCloneID(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String id;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(CLONE_ID, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            id = rr.getString(1);
            sequence.set(DTOConstants.cloneID, id);
        }
        nav.close();

        return sequence;
    }

    public DTO getSegmentType(int key) throws DBException {

        ResultsNavigator nav = null;
        RowReference rr = null;     // one row in 'nav'
        DTO sequence = DTO.getDTO();    // start with a new DTO
        String type;

        nav = this.sqlDM.executeQuery(
                        Sprintf.sprintf(SEGMENT_TYPE, key));

        if (nav.next()) {
            rr = (RowReference) nav.getCurrent();
            type = rr.getString(1);
            sequence.set(DTOConstants.segmentType, type);
        }
        nav.close();

        return sequence;
    }

    private static final String SEQ_KEY =
            "select ss._Sequence_key\n"+
            "from SEQ_Sequence ss, ACC_Accession aa\n"+
            "where aa.accID = %s\n"+
            "and ss._Sequence_key = aa._Object_key\n"+
            "and aa._MGIType_key = 19";

    private static final String ACC_IDS =
            "select aa.accID, aa.preferred,adb.url, adb.name\n"+
            "from ACC_Accession aa, ACC_ActualDB adb\n"+
            "where aa._Object_key = %d\n"+
            "and aa._MGIType_key = 19\n"+
            "and aa._LogicalDB_key = adb._LogicalDB_key\n"+
            "order by accID";

    private static final String SEQ_VER =
            "select version\n"+
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
            "and vt._Object_key = ss._SequenceProvider_key";

    private static final String SEQ_STATUS =
            "select vt.term\n"+
            "from VOC_Term vt, SEQ_Sequence ss\n"+
            "where ss._Sequence_key = %d\n"+
            "and vt._Object_key = ss._SequenceStatus_key";

    private static final String SEQ_TYPE =
            "select vt.term\n"+
            "from VOC_Term vt, SEQ_Sequence ss\n"+
            "where ss._Sequence_key = %d\n"+
            "and vt._Object_key = ss._SequenceType_key";

    private static final String SEQ_LENGTH =
            "select length\n"+
            "from SEQ_Sequence\n"+
            "where _Sequence_key = %d";

    private static final String SEQ_SOURCE =
            "select select psv.age, psv.cellLine, psv.gender,\n"+
            "       psv.organism, psv.strain, psv.tissue\n"+  // include library
            "from PRB_Source_View psv, SEQ_Source_Assoc ssa\n"+
            "where ssa._Sequence_key = %d\n"+
            "and psv._Source_key = ssa._Source_key";

    private static final String CHROMOSOME =
            "select distinct mm.chromosome\n"+
            "from MRK_Marker mm, SEQ_Marker_Cache smc\n"+
            "where smc._Sequence_key = %d\n"+
            "and mm._Marker_key = smc._Marker_key";

    private static final String MARKER_TABLE =
            "CREATE TABLE #mrk(\n"+
            "_Marker_key  int NOT NULL,\n"+
            "markerType   varchar(255)  NOT NULL,\n"+
            "assayCount   int NULL,\n"+
            "alleleCount  int NULL,\n"+
            "GOCount      int NULL,\n"+
            "orthoCount   int NULL,\n"+
            "name         varchar(255)  NOT NULL,\n"+
            "symbol       varchar(25)   NOT NULL\n"+
            ")";

    private static final String MARKER_NOMENCLATURE =
            "insert #mrk(_Marker_key,name,symbol,markerType)\n"+
            "select mm._Marker_key, mm.name, mm.symbol, vt.term\n"+
            "from MRK_Marker mm, SEQ_Marker_Cache smc, VOC_Term vt\n"+
            "where smc._Sequence_key = 14104\n"+
            "and mm._Marker_key = smc._Marker_key\n"+
            "and vt._Term_key = mm._Marker_Type_key";

    private static final String MARKER_GO_COUNT =
            "update #mrk\n"+
            "set GOCount = count(vt.term)\n"+
            "from VOC_Term vt, VOC_Annot va, #mrk m\n"+
            "where m._Marker_key = va._Object_key\n"+
            "and va._AnnotType_key = 1000\n"+
            "and va._Term_key = vt._Term_key";

    private static final String MARKER_ASSAY_COUNT =
            "update #mrk\n"+
            "set assayCount = count(ga._Assay_key)\n"+
            "from #mrk m, GXD_Assay ga\n"+
            "where m._Marker_key = ga._Marker_key";

    private static final String MARKER_ORTHOLOGY_COUNT =
            "update #mrk\n"+
            "set orthoCount = count(hhm._Homology_key)\n"+
            "from HMD_Homology_Marker hhm, #mrk m\n"+
            "where m._Marker_key = hhm._Marker_key";

    private static final String MARKER_ALLELE_COUNT =
            "update #mrk\n"+
            "set alleleCount = count(al._Allele_key)\n"+
            "from #mrk m, ALL_Allele al\n"+
            "where m._Marker_key = al._Marker_key";

    private static final String MARKER_INFO =
            "select _Marker_key, symbol, name, markerType,\n"+
            "alleleCount,assayCount,GOCount,orthoCount\n"+
            "from #mrk";

    private static final String REFS =
            "select smc._Refs_key, aa.accID\n"+
            "from ACC_Accession aa, SEQ_Marker_Cache smc\n"+
            "where smc._Sequence_key = %d\n"+
            "and smc._Refs_key = aa._Object_key\n"+
            "and aa._MGIType_key = 1\n"+
            "and aa.preferred = 1";

    private static final String PROBES =
            "select pp.name, pp._Probe_key\n"+
            "from PRB_Probe pp, SEQ_Probe_Cache sqc\n"+
            "where sqc._Sequence_key = %d\n"+
            "and pp._Probe_key = sqc._Probe_key";

    private static final String CLONE_COLLECTION =
            "select collection = ldb.name\n"+
            "from ACC_LogicalDB ldb, ACC_Accession aa, SEQ_Probe_Cache sqc\n"+
            "where sqc._Sequence_key = %d\n"+
            "and aa._Object_key = spc._Probe_key\n"+
            "and aa._MGIType_key = 3\n"+
            "and aa._LogicalDB_key in (select _Object_key\n"+
            "                          from MGI_SetMember\n"+
            "                          where _Set_key = 1000)";

    private static final String CLONE_ID =
            "select aa.accID\n"+
            "from ACC_Accession aa, SEQ_Probe_Cache sqc\n"+
            "sqc._Sequence_key = %d\n"+
            "and aa._Object_key = spc._Probe_key\n"+
            "and aa._MGIType_key = 3\n"+
            "and aa.preferred = 1";

    private static final String SEGMENT_TYPE =
            "select type = vt.term\n"+
            "from VOC_Term vt, SEQ_Probe_Cache sqc, PRB_Probe pp\n"+
            "where sqc._Sequence_key = %d\n"+
            "and pp._Probe_key = spc._Probe_key\n"+
            "and pp._SegmentType_key = vt._Term_key";




}
