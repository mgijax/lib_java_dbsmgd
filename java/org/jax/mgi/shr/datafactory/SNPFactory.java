package org.jax.mgi.shr.datafactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jax.mgi.shr.ListHash;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.stringutil.Sprintf;
import org.jax.mgi.shr.stringutil.StringLib;
import org.jax.mgi.shr.timing.TimeStamper;
import org.jax.mgi.shr.webapp.ResolvedWebArg;
import org.jax.mgi.shr.webapp.WebArgs;

public class SNPFactory extends Factory implements SummaryReportFactory{

    //----------------------------------------
    public SNPFactory (DataFactoryCfg config,
                       SQLDataManager sqlDM,
                       Logger logger) {

        super(config,sqlDM,logger,"snpFactory");
    }


    //--------------------------------------------------------------------------
    public DTO getResults(Map QueryParameters) throws DBException, IOException {

        ArrayList allowedStrains = new ArrayList();
        ArrayList commands = new ArrayList();
        ArrayList returnedStrains = new ArrayList();
        ArrayList snpOrder = new ArrayList();
        DTO snps = DTO.getDTO();
        Integer _SNP_key;
        Integer lastKey = null;
        ListHash snpMarker = new ListHash();
        Map snpAlleleHash = new HashMap();
        Map snpHash = new HashMap();
        ResultsNavigator nav = null;
        RowReference rr = null;
        String allele;
        String alleleCommand, resultsCommand, markerCommand,lim,vals;
        String markerSymbol;
        String strainName;
        WebArgs wa = new WebArgs(QueryParameters);


        sanityCheck(wa);

        if(wa.hasArg("limit")) {
            lim = wa.getArg("limit").getValues()[0];
        } else lim = "5000";
        if((Integer.parseInt(lim)>5000) || (lim == null)) {
            lim = "5000";
        }

        if(wa.hasArg("restrictStrains"))
            vals = wa.getArg(("restrictStrains")).getValues()[0];
        else vals = "no";

        if(wa.hasArg("cmpStrains") && vals.equals("yes")) {
            allowedStrains.addAll(Arrays.asList(wa.getArg("cmpStrains").getValues()));
            if(wa.hasArg("refStrain")) {
              allowedStrains.addAll(Arrays.asList(wa.getArg("refStrain").getValues()));
            }
        }

        //Get the sql that will find all the SNPs requested by the user
        commands.add(getMatchingSNPs(wa));
        //create our temp tables to store our adorned results in
        commands.add(Sprintf.sprintf(CREATE_RESULTS_TABLE,RESULTS_TABLE));
        commands.add(Sprintf.sprintf(CREATE_ALLELE_RESULTS_TABLE,
                                     ALLELE_RESULTS_TABLE));
        commands.add(Sprintf.sprintf(CREATE_MARKER_RESULTS_TABLE,
                                     MARKER_RESULTS_TABLE));

        // load our temp tables with the adorned results
        commands.add("set rowcount "+lim);
        commands.addAll(getSNPAdornment(TMP_TABLE,RESULTS_TABLE));
        commands.add("set rowcount 0");
        commands.addAll(getStrainAdornment(TMP_TABLE,
                                           RESULTS_TABLE,
                                           allowedStrains));
        commands.addAll(getMarkerAdornment(TMP_TABLE, RESULTS_TABLE));


        //Pull the results out of our temp tables
        resultsCommand = Sprintf.sprintf(RESULTS_COMMAND, RESULTS_TABLE);
        alleleCommand = Sprintf.sprintf(ALLELE_COMMAND, ALLELE_RESULTS_TABLE);
        markerCommand = Sprintf.sprintf(MARKER_COMMAND, MARKER_RESULTS_TABLE);

        for(int i=0;i<commands.size();i++) {
            String curCmd = (String)commands.get(i);
//System.out.println(curCmd);
            try {
                this.sqlDM.execute(curCmd);
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }

//System.out.println(resultsCommand);
        nav = this.sqlDM.executeQuery(resultsCommand);
        while(nav.next()) {
            SNP curSnp = new SNP();
            rr = (RowReference) nav.getCurrent();
            curSnp.rsID = rr.getString(1);
            curSnp.ssID = rr.getString(2);
            curSnp.pID = rr.getString(3);
            curSnp.providerName = rr.getString(4);
            curSnp.functionalLocation = rr.getString(5);
            curSnp.polymorphismClass = rr.getString(6);
            curSnp.chromosome = rr.getString(7);
            curSnp.coordinate = rr.getDouble(8).intValue();
            curSnp.orientation = rr.getString(9);
            _SNP_key = rr.getInt(10);
            snpOrder.add(_SNP_key);
            snpHash.put(_SNP_key,curSnp);
        }
        nav.close();

//System.out.println(alleleCommand);
        nav = this.sqlDM.executeQuery(alleleCommand);
        while(nav.next()) {
            Map strainAlleleMap;
            rr = (RowReference) nav.getCurrent();
            strainName = rr.getString(1);
            allele = rr.getString(2);
            _SNP_key = rr.getInt(3);
            if(_SNP_key.equals(lastKey))
                strainAlleleMap = (Map)snpAlleleHash.get(lastKey);
            else {
                strainAlleleMap = new HashMap();
                lastKey = _SNP_key;
            }
            if(!returnedStrains.contains(strainName))
                returnedStrains.add(strainName);
            strainAlleleMap.put(strainName,allele);
            snpAlleleHash.put(lastKey,strainAlleleMap);
        }
        nav.close();

//System.out.println(markerCommand);
        nav = this.sqlDM.executeQuery(markerCommand);
        while(nav.next()) {
            rr = (RowReference) nav.getCurrent();
            _SNP_key = rr.getInt(1);
            markerSymbol = rr.getString(2);
            snpMarker.put(_SNP_key, markerSymbol);
        }

        nav.close();
        nav = this.sqlDM.executeQuery("select cnt=count(*) from "+TMP_TABLE);
        while(nav.next()) {
            rr = (RowReference) nav.getCurrent();
            snps.set("unlimited",rr.getInt(1));

        }


        snps.set("mrks",snpMarker);
        snps.set("snps",snpHash);
        snps.set("ordering",snpOrder);
        if(wa.hasArg("refStrain") && returnedStrains.contains(wa.getArg("refStrain").getValues()[0])) {
            returnedStrains.remove(wa.getArg("refStrain").getValues()[0]);
            returnedStrains.add(0,wa.getArg("refStrain").getValues()[0]);
        }
        snps.set("strainList",returnedStrains);
        snps.set("strainAllele",snpAlleleHash);
        snps.set("webArgs",wa);
        return snps;
    }

    //-------------------------------------------------------------
    private String getMatchingSNPs(WebArgs wa) throws IOException,
                                                      DBException {
        ListHash sqlClauses = new ListHash();
        sqlClauses.put(SELECT_CLAUSE, "distinct snp._SNP_key");
        sqlClauses.put(INTO_CLAUSE, TMP_TABLE);
        sqlClauses.put(FROM_CLAUSE, SNP_TABLE+" snp");

        if(wa.hasArg("accID")) {
            sqlClauses.merge(getSNPIDSQL(wa.getArg("accID")));
        }

        if(wa.hasArg("snpType")) {
            sqlClauses.merge(getPolymorphismClassSQL(wa.getArg("snpType")));
        }

        if(wa.hasArg("cmpStrains")){
            sqlClauses.merge(getStrainSQL(wa.getArg("cmpStrains")));
        }

        if(wa.hasArg("refStrain")){
            sqlClauses.merge(getReferenceStrainSQL(wa.getArg("refStrain"),
                                                   wa.getArg("cmpStrains")));
        }

        if(wa.hasArg("chromosome")) {
            sqlClauses.merge(getChromosomeSQL(wa.getArg("chromosome")));
        }

        if( (wa.hasArg("coordStart")) && (wa.hasArg("coordEnd"))) {
            sqlClauses.merge(getGenomeCoordinateSQL(wa.getArg("coordStart"),
                                                    wa.getArg("coordEnd")));
        }

        if(wa.hasArg("symname")) {
            if(wa.hasArg("asmbl")) {
                sqlClauses.merge(getMarkerNomenWithFlankSQL(wa.getArg("symname"),
                                                            wa.getArg("asmbl")));
            } else {
                sqlClauses.merge(getMarkerNomenclatureSQL(wa.getArg("symname")));
            }
        }

        if((wa.hasArg("startMarker")) && (wa.hasArg("endMarker"))) {
            sqlClauses.merge(getBoundryMarkersSQL(wa.getArg("startMarker"),
                                                  wa.getArg("endMarker")));
        }

        if(wa.hasArg("fxn")) {
            sqlClauses.merge(getFunctionalLocationSQL(wa.getArg("fxn")));
        }

        return buildQuery(sqlClauses);


    }

    //************************************************
    //************SQL Generating Functions************
    //************************************************

    //-------------------------------------------------------
    private static ListHash getSNPIDSQL(ResolvedWebArg rwa) {

        ListHash results = new ListHash();
        String value = rwa.getValues()[0];
        ArrayList clauses = new ArrayList();

        results.put(FROM_CLAUSE, SNP_TABLE + " snp");
        clauses.add("snp.pID like '"+value+"'");
        clauses.add("snp.rsID like '"+value+"'");
        clauses.add("snp.ssID like '"+value+"'");
        results.put(WHERE_CLAUSE, "("+StringLib.join(clauses, "\nor ")+")");
        return results;
    }

    //-------------------------------------------------------------------
    private static ListHash getPolymorphismClassSQL(ResolvedWebArg rwa) {

        ListHash results = new ListHash();
        List values = Arrays.asList(rwa.getValues());
        if(values.contains("DIP")) {
            values.set(values.indexOf("DIP"), "in-del");
        }
        results.put(FROM_CLAUSE, SNP_TABLE + " snp");
        results.put(WHERE_CLAUSE,
                    Sprintf.sprintf("snp.polymorphismClass in ('%s')",
                                    StringLib.join(values,"', '")));
        return results;
    }

    //--------------------------------------------------------------------
    private static ListHash getFunctionalLocationSQL(ResolvedWebArg rwa) {
        ListHash results = new ListHash();
        List values = Arrays.asList(rwa.getValues());
        ArrayList clauses = new ArrayList();

        if(values.contains("coding-NonSynonymous"))
            clauses.add("snp.functionalLocation = 'Cn'");
        if(values.contains("coding-Synonymous")) {
            clauses.add("snp.functionalLocation = 'Cs'");
            clauses.add("snp.functionalLocation = 'Cx'");
        }
        if(values.contains("intron")) {
            clauses.add("snp.functionalLocation = 'I'");
            clauses.add("snp.functionalLocation = 'S'");
        }
        if(values.contains("UTR"))
             clauses.add("snp.functionalLocation = 'M'");

        if(values.contains("Locus"))
            clauses.add("snp.functionalLocation = 'L'");

        results.put(FROM_CLAUSE, SNP_TABLE + " snp");
        results.put(WHERE_CLAUSE,"("+StringLib.join(clauses, "\nor ")+")");

        return results;
    }

    //--------------------------------------------------------
    private static ListHash getStrainSQL(ResolvedWebArg rwa) {
        ListHash results = new ListHash();
        String[] values = rwa.getValues();
        ArrayList clauses = new ArrayList();

        results.put(FROM_CLAUSE, SNP_TABLE + " snp");
        results.put(FROM_CLAUSE, STRAIN_TABLE + " strain");
        results.put(FROM_CLAUSE, ALLELE_TABLE + " allele");
        results.put(WHERE_CLAUSE, "allele._SNP_key = snp._SNP_key");
        results.put(WHERE_CLAUSE, "allele._Strain_key = strain._Strain_key");


        for(int i=0;i<values.length;i++) {
            clauses.add("strain.name = '"+values[i]+"'");
        }
        results.put(WHERE_CLAUSE, "("+StringLib.join(clauses, "\nor ")+")");
        return results;
    }

    //---------------------------------------------------------------------
    private static ListHash getReferenceStrainSQL(ResolvedWebArg refStrain,
                                                  ResolvedWebArg strains) {
        ListHash results = new ListHash();
        String[] strainValues = strains.getValues();
        String refVal = refStrain.getValues()[0];

        ArrayList clauses = new ArrayList();

        results.put(FROM_CLAUSE, SNP_TABLE + " snp");
        results.put(FROM_CLAUSE, STRAIN_TABLE + " strain");
        results.put(FROM_CLAUSE, ALLELE_TABLE + " allele");
        results.put(FROM_CLAUSE, STRAIN_TABLE + " refStrain");
        results.put(FROM_CLAUSE, ALLELE_TABLE + " refAllele");
        results.put(WHERE_CLAUSE, "allele._SNP_key = snp._SNP_key");
        results.put(WHERE_CLAUSE, "allele._Strain_key = strain._Strain_key");
        results.put(WHERE_CLAUSE, "refAllele._SNP_key = snp._SNP_key");
        results.put(WHERE_CLAUSE, "refAllele._Strain_key = refStrain._Strain_key");
        results.put(WHERE_CLAUSE, "refAllele.allele != allele.allele");

        for(int i=0;i<strainValues.length;i++) {
            clauses.add("strain.name = '"+strainValues[i]+"'");
        }
        results.put(WHERE_CLAUSE, "("+StringLib.join(clauses, "\nor ")+")");
        results.put(WHERE_CLAUSE, "refStrain.name = '"+refVal+"'");
        return results;

    }

    //------------------------------------------------------------
    private static ListHash getChromosomeSQL(ResolvedWebArg rwa) {

        ListHash results = new ListHash();
        List values = Arrays.asList(rwa.getValues());

        results.put(FROM_CLAUSE, SNP_TABLE + " snp");
        results.put(FROM_CLAUSE, LOCATION_TABLE + " loc");

        results.put(WHERE_CLAUSE, "loc._Location_key = snp._Location_key");
        results.put(WHERE_CLAUSE,
                    Sprintf.sprintf("loc.chromosome in ('%s')",
                                    StringLib.join(values,"', '")));
        return results;

    }

    //-------------------------------------------------------------------
    private static ListHash getGenomeCoordinateSQL(ResolvedWebArg start,
                                                   ResolvedWebArg stop) {

        ListHash results = new ListHash();
        String startCoord = start.getValues()[0];
        String stopCoord = stop.getValues()[0];

        results.merge(getCoordClauses(startCoord,stopCoord));
        return results;
    }

    //--------------------------------------------------------------------
    private static ListHash getMarkerNomenclatureSQL(ResolvedWebArg rwa) {

        ListHash results = new ListHash();

        results.merge(getLabelClauses(rwa));

        results.put(FROM_CLAUSE, MARKER_ASSOC_TABLE+" mat");
        results.put(FROM_CLAUSE, SNP_TABLE + " snp");

        //Joins:
        results.put(WHERE_CLAUSE, "mrkl._Marker_key = mat._Marker_key");
        results.put(WHERE_CLAUSE, "mat._Location_key = snp._Location_key");
        results.put(WHERE_CLAUSE, "mrkl._Marker_key = mat._Marker_key");

        return results;
    }

    //------------------------------------------------------------------------------------
    private ListHash getMarkerNomenWithFlankSQL(ResolvedWebArg marker,
                                                ResolvedWebArg flank) throws DBException {

        ListHash results = new ListHash();
        ArrayList curWhere;
        ArrayList whereClauses = new ArrayList();
        String flankOp = flank.getOperator();
        Integer flankVal = new Integer( Integer.parseInt(flank.getValues()[0])*1000);
        String val;
        HashMap orient;
        Object[] keys;

        orient = findMarkerOrientation(marker);
        keys = orient.keySet().toArray();

        results.put(FROM_CLAUSE, LOCATION_TABLE + " loc");
        results.put(FROM_CLAUSE, "VOC_Term vt");

        results.put(WHERE_CLAUSE, "vt.term = 'genomic'");
        results.put(WHERE_CLAUSE, "loc._Location_key = snp._Location_key");

        for(int i=0;i<keys.length;i++) {
            curWhere = new ArrayList();
            results.put(FROM_CLAUSE, "SEQ_Marker_Cache smc"+i);
            results.put(FROM_CLAUSE, "MAP_Coord_Feature mcf"+i);
            results.put(FROM_CLAUSE, "MRK_Chromosome chromo"+i);
            results.put(FROM_CLAUSE, "MAP_Coordinate mcoord"+i);

            results.put(WHERE_CLAUSE,"smc"+i+"._Qualifier_key = vt._Term_key");
            results.put(WHERE_CLAUSE,"smc"+i+"._Marker_key = "+keys[i]);
            results.put(WHERE_CLAUSE,"mcf"+i+"._Object_key = smc"+i+"._Sequence_key");
            results.put(WHERE_CLAUSE,"mcf"+i+"._MGIType_key = 19");
            results.put(WHERE_CLAUSE,"loc.chromosome = chromo"+i+".chromosome");
            results.put(WHERE_CLAUSE,"mcoord"+i+"._Object_key = chromo"+i+"._Chromosome_key");
            results.put(WHERE_CLAUSE,"mcoord"+i+"._MGIType_key = 27");
            results.put(WHERE_CLAUSE,"mcoord"+i+"._Map_key = mcf"+i+"._Map_key");
            if(!flankOp.equals("upstream")) {
                if(((String)orient.get(keys[i])).equals("+"))
                    curWhere.add("loc.coordinate>=mcf"+i+".startCoordinate-"+flankVal);
                else curWhere.add("loc.coordinate<=mcf"+i+".endCoordinate+"+flankVal);
            } else {
                if(((String)orient.get(keys[i])).equals("+"))
                    curWhere.add("loc.coordinate>=mcf"+i+".startCoordinate");
                else curWhere.add("loc.coordinate<=mcf"+i+".endCoordinate");
            }

            if(!flankOp.equals("downstream")) {
                if(((String)orient.get(keys[i])).equals("+"))
                   curWhere.add("loc.coordinate<=mcf"+i+".endCoordinate+"+flankVal);
                else curWhere.add("loc.coordinate>=mcf"+i+".startCoordinate-"+flankVal);
            } else {
                 if(((String)orient.get(keys[i])).equals("+"))
                   curWhere.add("loc.coordinate<=mcf"+i+".endCoordinate");
                else curWhere.add("loc.coordinate>=mcf"+i+".startCoordinate");
            }
            whereClauses.add(StringLib.join(curWhere, "\nand "));
        }
        results.put(WHERE_CLAUSE, "("+StringLib.join(whereClauses, "\nor ")+")");
        return results;
    }

    //-----------------------------------------------------------------------
    private static ListHash getBoundryMarkersSQL(ResolvedWebArg startMarker,
                                                 ResolvedWebArg stopMarker) {

        ListHash results = new ListHash();

        results.put(FROM_CLAUSE, "SEQ_Marker_Cache smc1");
        results.put(FROM_CLAUSE, "SEQ_Marker_Cache smc2");
        results.put(FROM_CLAUSE, "MAP_Coord_Feature mcf1");
        results.put(FROM_CLAUSE, "MAP_Coord_Feature mcf2");
        results.put(FROM_CLAUSE, SNP_TABLE + " snp");
        results.put(FROM_CLAUSE, LOCATION_TABLE + " loc");
        results.put(FROM_CLAUSE, "MRK_Marker mm1");
        results.put(FROM_CLAUSE, "MRK_Marker mm2");
        results.put(FROM_CLAUSE, "VOC_Term vt");


        results.put(WHERE_CLAUSE, "mm1._Marker_key = smc1._Marker_key");
        results.put(WHERE_CLAUSE, "mm1.symbol = '"+startMarker.getValues()[0]+"'");
        results.put(WHERE_CLAUSE, "mcf1._Object_key = smc1._Sequence_key");
        results.put(WHERE_CLAUSE, "mcf1._MGIType_key = 19");
        results.put(WHERE_CLAUSE, "loc.coordinate >= mcf1.endCoordinate");
        results.put(WHERE_CLAUSE, "smc1._Qualifier_key = vt._Term_key");
        results.put(WHERE_CLAUSE, "vt.term = 'genomic'");

        results.put(WHERE_CLAUSE, "mm2._Marker_key = smc2._Marker_key");
        results.put(WHERE_CLAUSE, "mm2.symbol = '"+stopMarker.getValues()[0]+"'");
        results.put(WHERE_CLAUSE, "mcf2._Object_key = smc2._Sequence_key");
        results.put(WHERE_CLAUSE, "mcf2._MGIType_key = 19");
        results.put(WHERE_CLAUSE, "loc.coordinate <= mcf2.startCoordinate");
        results.put(WHERE_CLAUSE, "smc2._Qualifier_key = vt._Term_key");
        results.put(WHERE_CLAUSE, "vt.term = 'genomic'");

        results.put(WHERE_CLAUSE, "loc.chromosome = mm1.chromosome");
        results.put(WHERE_CLAUSE, "loc.chromosome = mm2.chromosome");
        results.put(WHERE_CLAUSE, "loc._Location_key = snp._Location_key");

        return results;
    }

    //************************************************
    //************Adornment SQL Functions*************
    //************************************************


    //-----------------------------------------------------------------------
    private static ArrayList getSNPAdornment(String keyTable, String resultsTable) {
        ArrayList commands = new ArrayList();

        commands.add(Sprintf.sprintf("insert into %s (_SNP_key, chromosome, coordinate)\n"+
                        "select snp._SNP_key, loc.chromosome, loc.coordinate\n"+
                        "from %s snp, %s loc, %s keys\n"+
                        "where snp._Location_key = loc._Location_key\n"+
                        "and snp._SNP_key = keys._SNP_key\n",
                        RESULTS_TABLE,
                        SNP_TABLE,
                        LOCATION_TABLE,
                        keyTable));
        for(int i=0;i<NULL_COLS.length;i++)
            commands.add(Sprintf.sprintf("update %s\n"+
                            "set %s = snp.%s\n"+
                            "from %s snp, %s res\n"+
                            "where snp._SNP_key = res._SNP_key\n",
                            RESULTS_TABLE,
                            NULL_COLS[i],
                            NULL_COLS[i],
                            SNP_TABLE,
                            RESULTS_TABLE));
        commands.add(Sprintf.sprintf("update %s\n"+
                        "set orientation = loc.orientation\n"+
                        "from %s snp, %s res, %s loc\n"+
                        "where snp._SNP_key = res._SNP_key\n"+
                        "and snp._Location_key = loc._Location_key\n",
                        RESULTS_TABLE,
                        SNP_TABLE,
                        RESULTS_TABLE,
                        LOCATION_TABLE));
        return commands;

    }

    //--------------------------------------------------------------
    private static ArrayList getStrainAdornment(String keyTable,
                                         String resultsTable,
                                         ArrayList allowedStrains) {
        ArrayList commands = new ArrayList();
        String allowed = "";

        if(allowedStrains.size()>0) {
            allowed = Sprintf.sprintf("and st.name in ('%s')",StringLib.join(allowedStrains,"','"));
        }

        commands.add(Sprintf.sprintf("insert into %s (strainName, allele, _SNP_key)\n"+
                        "select distinct st.name, ssa.allele, keys._SNP_key\n"+
                        "from %s st, %s ssa, %s keys\n"+
                        "where keys._SNP_key = ssa._SNP_key\n"+
                        "and st._Strain_key = ssa._Strain_key\n"+allowed,
                        ALLELE_RESULTS_TABLE,
                        STRAIN_TABLE,
                        ALLELE_TABLE,
                        keyTable));

        return commands;
    }

    //--------------------------------------------------------------------------
    private static ArrayList getMarkerAdornment(String keyTable, String resultsTable) {
        ArrayList commands = new ArrayList();
        commands.add(Sprintf.sprintf("insert into %s (symbol, _SNP_key)\n"+
                        "select mm.symbol, keys._SNP_key\n"+
                        "from MRK_Marker mm, %s keys, %s mrkLoc, %s snp\n"+
                        "where keys._SNP_key = snp._SNP_key\n"+
                        "and snp._Location_key = mrkLoc._Location_key\n"+
                        "and mrkLoc._Marker_key = mm._Marker_key\n",
                        MARKER_RESULTS_TABLE,
                        keyTable,
                        MARKER_ASSOC_TABLE,
                        SNP_TABLE
                        ));

        return commands;
    }


    //************************************************
    //**************SQL Helper functions**************
    //************************************************

    //-------------------------------------------------------
    protected static String buildQuery(ListHash sqlClauses) {
        StringBuffer command = new StringBuffer();
        sqlClauses.removeDuplicates(SELECT_CLAUSE);
        sqlClauses.removeDuplicates(FROM_CLAUSE);
        sqlClauses.removeDuplicates(WHERE_CLAUSE);

        if(sqlClauses.containsKey(SELECT_CLAUSE))
            command.append("select "+StringLib.join((List)sqlClauses.get(SELECT_CLAUSE),", ")+"\n");
        else return null;
        if(sqlClauses.containsKey(INTO_CLAUSE))
            command.append("into "+StringLib.join((List)sqlClauses.get(INTO_CLAUSE),"")+"\n");
        if(sqlClauses.containsKey(FROM_CLAUSE))
            command.append("from "+StringLib.join((List)sqlClauses.get(FROM_CLAUSE),", ")+"\n");
        else return null;
        if(sqlClauses.containsKey(WHERE_CLAUSE))
            command.append("where "+StringLib.join((List)sqlClauses.get(WHERE_CLAUSE),"\nand ")+"\n");


        return command.toString();
    }

    //--------------------------------------------------------------
    protected HashMap findMarkerOrientation(ResolvedWebArg marker) throws DBException {
        ListHash tmpCmd = new ListHash();
        ResultsNavigator nav = null;
        RowReference rr = null;
        HashMap results = new HashMap();
        String query;

        tmpCmd.put(SELECT_CLAUSE, "distinct mrkl._Marker_key, mcf.strand");
        tmpCmd.put(FROM_CLAUSE, "SEQ_Marker_Cache smc");
        tmpCmd.put(FROM_CLAUSE, "MAP_Coord_Feature mcf");
        tmpCmd.put(FROM_CLAUSE, "VOC_Term vt");
        tmpCmd.put(WHERE_CLAUSE, "smc._Marker_key = mrkl._Marker_key");
        tmpCmd.put(WHERE_CLAUSE, "smc._Qualifier_key = vt._Term_key");
        tmpCmd.put(WHERE_CLAUSE, "vt.term = 'genomic'");
        tmpCmd.put(WHERE_CLAUSE, "smc._Sequence_key = mcf._Object_key");
        tmpCmd.put(WHERE_CLAUSE, "mcf._MGIType_key = 19");
        tmpCmd.merge(getLabelClauses(marker));

        query = buildQuery(tmpCmd);

        nav = this.sqlDM.executeQuery(query);
        while(nav.next()) {
            rr = (RowReference) nav.getCurrent();
            results.put(rr.getInt(1),rr.getString(2));
        }
        return results;
    }

    //--------------------------------------------------------------
    private static ListHash getLabelClauses(ResolvedWebArg marker) {
        List mods = Arrays.asList(marker.getModifiers());
        String op;
        String[] labels = marker.getValues();
        String current = "mrkl._Label_Status_key = 1";
        ArrayList clauses = new ArrayList();
        ListHash results = new ListHash();

        results.put(FROM_CLAUSE, "MRK_Label mrkl");


        if (mods.contains("current symbols/names")) {
            results.put(WHERE_CLAUSE,"mrkl.labelType not in ('AS','AN','MY')");
            results.put(WHERE_CLAUSE, current);

        } else if(mods.contains("current symbols")) {
            results.put(WHERE_CLAUSE,"mrkl.labelType in ('MS','OS')");
            results.put(WHERE_CLAUSE,current);

        } else {
            results.put(WHERE_CLAUSE,"mrkl.labelType not in ('AS','AN')");
            results.put(WHERE_CLAUSE,current);
        }

        for(int i=0;i<labels.length;i++) {
            String curLabel = labels[i];
            op = marker.getOperator();
            if(op.equals("contains")) {
                curLabel = "%"+curLabel+"%";
            } else if(op.equals("begins")) {
                curLabel = curLabel+"%";
            } else if(op.equals("ends")) {
                curLabel = "%"+curLabel;
            }
            if(!op.equals("=")) {
                op = "like";
            }
            clauses.add("mrkl.label "+op+ " '"+curLabel+"'");
        }
        results.put(WHERE_CLAUSE,"("+StringLib.join(clauses, "\nor ")+")");
        return results;
    }

    //----------------------------------------------------------------------------
    private static ListHash getCoordClauses(String startCoord, String stopCoord) {

        ListHash results = new ListHash();
        results.put(FROM_CLAUSE, LOCATION_TABLE + " loc");
        results.put(FROM_CLAUSE, SNP_TABLE + " snp");

        results.put(WHERE_CLAUSE, "loc.coordinate >= "+ startCoord);
        results.put(WHERE_CLAUSE, "loc.coordinate <= "+ stopCoord);
        results.put(WHERE_CLAUSE, "loc._Location_key = snp._Location_key");
        return results;


    }

    //************************************************
    //****************Input Validation****************
    //************************************************

    //-------------------------------------------------------
    private void sanityCheck(WebArgs wa) throws IOException {
        if((wa.hasArg("startMarker") && !(wa.hasArg("endMarker"))) ||
           (!(wa.hasArg("startMarker")) && wa.hasArg("endMarker")))
            throw new IOException("You must specify both a starting marker and an ending marker.");
        if((wa.hasArg("coordStart") && !(wa.hasArg("coordEnd"))) ||
           (!(wa.hasArg("coordStart")) && wa.hasArg("coordEnd")) ||
           (wa.hasArg("coordStart") && wa.hasArg("coordEnd") && !(wa.hasArg("chromosome"))))
            throw new IOException("You must specify both a starting and ending coordinate and a chromosome.");
        if(wa.hasArg("refStrain") && !(wa.hasArg("cmpStrains"))) {
            throw new IOException("You must select at least one strain to compare with your reference strain.");
        }
        if(wa.hasArg("asmbl") && !(wa.hasArg("symname")))
            throw new IOException("You must enter a marker symbol if you enter a value for flanking distance.");

    }

    private static final String SELECT_CLAUSE = "selectClause";
    private static final String INTO_CLAUSE = "intoClause";
    private static final String FROM_CLAUSE = "fromClause";
    private static final String WHERE_CLAUSE = "whereClause";
    private static final String ORDER_CLAUSE = "orderClause";


    private static final String TMP_TABLE = "#snpData";
    private static final String RESULTS_TABLE = "#snpResults";
    private static final String ALLELE_RESULTS_TABLE = "#alleles";
    private static final String MARKER_RESULTS_TABLE = "#markers";

    private static final String LOCATION_TABLE = "radar_dbm..SNP_Location";
    private static final String SNP_TABLE = "radar_dbm..SNP_SNP";
    private static final String STRAIN_TABLE = "radar_dbm..SNP_Strain";
    private static final String ALLELE_TABLE = "radar_dbm..SNP_Strain_SNP_Assoc";
    private static final String MARKER_ASSOC_TABLE = "radar_dbm..SNP_Location_Marker_Assoc";

    private static final String CREATE_RESULTS_TABLE =
    "create table %s (\n"+
        "_SNP_key            int         not null,\n"+
        "polymorphismClass   varchar(6)  NULL,\n"+
        "functionalLocation  varchar(6)  NULL,\n"+
        "providerName        varchar(20) NULL,\n"+
        "ssID                varchar(12) NULL,\n"+
        "rsID                varchar(12) NULL,\n"+
        "pID                 varchar(30) NULL,\n"+
        "chromosome          varchar(2)  not null,\n"+
        "coordinate          float(8)    not null,\n"+
        "orientation         char(1)     NULL)\n";

    private static final String CREATE_ALLELE_RESULTS_TABLE =
    "create table %s (\n"+
        "_SNP_key            int         not null,\n"+
        "strainName          varchar(40) not null,\n"+
        "allele              varchar(80) not null)\n";

    private static final String CREATE_MARKER_RESULTS_TABLE =
    "create table %s (\n"+
        "_SNP_key            int         not null,\n"+
        "symbol              varchar(50) not null)\n";

    private static final String RESULTS_COMMAND =
    "select distinct rt.rsID, rt.ssID, rt.pID, rt.providerName, rt.functionalLocation,\n"+
    "rt.polymorphismClass, rt.chromosome, rt.coordinate, rt.orientation, rt._SNP_key\n"+
    "from %s rt\n";

    private static final String ALLELE_COMMAND =
    "select distinct al.strainName, al.allele, al._SNP_key\n"+
    "from %s al\n";

    private static final String MARKER_COMMAND =
    "select distinct _SNP_key, symbol\n"+
    "from %s \n";


    private static final String[] NULL_COLS = {"polymorphismClass",
                                               "functionalLocation",
                                               "providerName",
                                               "ssID",
                                               "rsID",
                                               "pID"};

}

