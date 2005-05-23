package org.jax.mgi.shr.datafactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        ListHash restrictQuery,restrictCount;
        Map snpAlleleHash = new HashMap();
        Map snpHash = new HashMap();
        ResultsNavigator nav = null;
        RowReference rr = null;
        String allele;
        String alleleCommand, resultsCommand, markerCommand,lim,vals;
        String markerSymbol;
        String strainName;
        String snpKeys;
        Strains cmp = new Strains();
        WebArgs wa = new WebArgs(QueryParameters);
        int rowCount =0;


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
        restrictQuery = getMatchingSNPs(wa);
        restrictQuery.put(SELECT_CLAUSE, "cnt=count(distinct snp._SNP_key)");
        try {
            nav = this.sqlDM.executeQuery(buildQuery(restrictQuery));
            while(nav.next()) {
                rr = (RowReference) nav.getCurrent();
                rowCount = rr.getInt(1).intValue();
            }
        } catch(DBException dbe) {
            dbe.printStackTrace();
           // throw new IOException("Your query will return too many results, please add additional query parameters.");
        } finally {
            if(nav!=null)
                nav.close();
        }
        if(rowCount>5000)
            throw new IOException("Your query will return too many results, please add additional query parameters.");
        else snps.set("unlimited",new Integer(rowCount));

        restrictQuery.remove(SELECT_CLAUSE);
        restrictQuery.put(SELECT_CLAUSE,"distinct snp._SNP_key");
        restrictQuery.put(INTO_CLAUSE, TMP_TABLE);


        commands.add(buildQuery(restrictQuery));
        //create our temp tables to store our adorned results in
        commands.add(Sprintf.sprintf(CREATE_RESULTS_TABLE,RESULTS_TABLE));
        commands.add(Sprintf.sprintf(CREATE_ALLELE_RESULTS_TABLE,
                                     ALLELE_RESULTS_TABLE));
        commands.add(Sprintf.sprintf(CREATE_MARKER_RESULTS_TABLE,
                                     MARKER_RESULTS_TABLE));

        // load our temp tables with the adorned results
        commands.add("set rowcount "+lim);
        commands.add(Sprintf.sprintf("insert into %s (_SNP_key, chromosome, coordinate, seqNum)\n"+
                        "select distinct snp._SNP_key, loc.chromosome, loc.coordinate, mc.sequenceNum\n"+
                        "from %s snp, %s loc, %s keys, MRK_Chromosome mc\n"+
                        "where snp._Location_key = loc._Location_key\n"+
                        "and snp._SNP_key = keys._SNP_key\n"+
                        "and loc.chromosome = mc.chromosome\n"+
                        "and mc._Organism_key = 1",
                        RESULTS_TABLE,
                        SNP_TABLE,
                        LOCATION_TABLE,
                        TMP_TABLE));
        commands.add("set rowcount 0");
        commands.add("create index index_SNP_key on "+RESULTS_TABLE+"(_SNP_key)");
        commands.add("drop table "+TMP_TABLE);
        commands.addAll(getSNPAdornment(RESULTS_TABLE,RESULTS_TABLE));
        commands.addAll(getStrainAdornment(RESULTS_TABLE,
                                           RESULTS_TABLE,
                                           allowedStrains));
        commands.addAll(getMarkerAdornment(RESULTS_TABLE, RESULTS_TABLE));


        //Pull the results out of our temp tables
        resultsCommand = Sprintf.sprintf(RESULTS_COMMAND, RESULTS_TABLE);
        alleleCommand = Sprintf.sprintf(ALLELE_COMMAND, ALLELE_RESULTS_TABLE);
        markerCommand = Sprintf.sprintf(MARKER_COMMAND, MARKER_RESULTS_TABLE);
        this.logger.logInfo("Starting SQL");
        for(int i=0;i<commands.size();i++) {
            String curCmd = (String)commands.get(i);
            this.logger.logInfo(curCmd);
            try {
                this.sqlDM.execute(curCmd);
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }

        nav = this.sqlDM.executeQuery(resultsCommand);
        this.logger.logInfo(resultsCommand);
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
            curSnp.coordinate= rr.getInt(8);
            curSnp.orientation = rr.getString(9);
            _SNP_key = rr.getInt(10);
            snpOrder.add(_SNP_key);
            snpHash.put(_SNP_key,curSnp);

        }
        nav.close();

        nav = this.sqlDM.executeQuery(alleleCommand);
        this.logger.logInfo(alleleCommand);
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

        nav = this.sqlDM.executeQuery(markerCommand);
        this.logger.logInfo(markerCommand);
        while(nav.next()) {
            rr = (RowReference) nav.getCurrent();
            _SNP_key = rr.getInt(1);
            markerSymbol = rr.getString(2);
            snpMarker.put(_SNP_key, markerSymbol);
        }

        nav.close();
        this.logger.logInfo("SQL Complete");


        snps.set("mrks",snpMarker);
        snps.set("snps",snpHash);
        snps.set("ordering",snpOrder);
        Collections.sort(returnedStrains,cmp);

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
    private ListHash getMatchingSNPs(WebArgs wa) throws IOException,
                                                      DBException {
        ListHash sqlClauses = new ListHash();
        sqlClauses.put(FROM_CLAUSE, SNP_TABLE+" snp");

        if(wa.hasArg("accID")) {
            sqlClauses.merge(getSNPIDSQL(wa.getArg("accID")));
        } else {

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
        }
        return sqlClauses;


    }

    //************************************************
    //************SQL Generating Functions************
    //************************************************

    //-------------------------------------------------------
    private static ListHash getSNPIDSQL(ResolvedWebArg rwa) {

        ListHash results = new ListHash();
        String[] value = rwa.getValues();
        ArrayList clauses = new ArrayList();

        results.put(FROM_CLAUSE, SNP_TABLE + " snp");
        for(int i=0;i<value.length;i++) {
            clauses.add("snp.pID like '"+value[i]+"'");
            clauses.add("snp.rsID like '"+value[i]+"'");
            clauses.add("snp.ssID like '"+value[i]+"'");
        }
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

        if(values.contains("Coding-NonSynonymous"))
            clauses.add("snp.functionalLocation = 'Cn'");
        if(values.contains("Coding-Synonymous")) {
            clauses.add("snp.functionalLocation = 'Cs'");
            clauses.add("snp.functionalLocation = 'Cx'");
        }
        if(values.contains("Intron")) {
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
        int multiplier = 1;
        List mods = Arrays.asList(start.getModifiers());
        if(mods.contains("Mbp")) {
            multiplier = 1000000;
        }
        Double startCoord = new Double(Double.parseDouble(start.getValues()[0])*multiplier);
        Double stopCoord = new Double(Double.parseDouble(stop.getValues()[0])*multiplier);

        results.merge(getCoordClauses(Integer.toString(startCoord.intValue()),
                                      Integer.toString(stopCoord.intValue())));
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
        ListHash tmpCmd = new ListHash();
        String flankOp = flank.getOperator();
        Integer flankVal = new Integer( Integer.parseInt(flank.getValues()[0])*1000);

        this.sqlDM.execute("Create table #flankingSNPs(_SNP_key int not null)");
        findMarkerOrientation(marker, "#orient");

        this.sqlDM.execute(getForwardCommand("#flankingSNPs", "#orient", flankVal,flankOp));
        this.sqlDM.execute(getReverseCommand("#flankingSNPs", "#orient", flankVal,flankOp));

        results.put(FROM_CLAUSE, "#flankingSNPs flnk");
        results.put(WHERE_CLAUSE, "flnk._SNP_key = snp._SNP_key");
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

        commands.add("SET FORCEPLAN ON");
        commands.add(Sprintf.sprintf("insert into %s (strainName, allele, _SNP_key)\n"+
                            "select distinct st.name, ssa.allele, keys._SNP_key\n"+
                            "from %s st, %s keys, %s ssa (index idx_Strain_SNP_Allele)\n"+
                            "where keys._SNP_key = ssa._SNP_key\n"+
                            "and st._Strain_key = ssa._Strain_key\n"+
                            allowed,
                                     ALLELE_RESULTS_TABLE,
                                     STRAIN_TABLE,
                                     keyTable,
                                     ALLELE_TABLE
                                     ));
        commands.add("SET FORCEPLAN OFF");

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

        if(sqlClauses.containsKey(INSERT_CLAUSE))
            command.append("insert into "+StringLib.join((List)sqlClauses.get(INSERT_CLAUSE),", ")+"\n");
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
        if(sqlClauses.containsKey(ORDER_CLAUSE))
            command.append("order by "+ StringLib.join((List)sqlClauses.get(ORDER_CLAUSE),", ")+"\n");

        return command.toString();
    }

    //--------------------------------------------------------------
    protected void findMarkerOrientation(ResolvedWebArg marker, String tableName) throws DBException {
        ListHash tmpCmd = new ListHash();
        ResultsNavigator nav = null;
        RowReference rr = null;
        HashMap results = new HashMap();

        tmpCmd.put(SELECT_CLAUSE, "distinct mrkl._Marker_key, mcf.strand");
        tmpCmd.put(INTO_CLAUSE, tableName);
        tmpCmd.put(FROM_CLAUSE, "SEQ_Marker_Cache smc");
        tmpCmd.put(FROM_CLAUSE, "MAP_Coord_Feature mcf");
        tmpCmd.put(FROM_CLAUSE, "VOC_Term vt");
        tmpCmd.put(WHERE_CLAUSE, "smc._Marker_key = mrkl._Marker_key");
        tmpCmd.put(WHERE_CLAUSE, "smc._Qualifier_key = vt._Term_key");
        tmpCmd.put(WHERE_CLAUSE, "vt.term = 'genomic'");
        tmpCmd.put(WHERE_CLAUSE, "smc._Sequence_key = mcf._Object_key");
        tmpCmd.put(WHERE_CLAUSE, "mcf._MGIType_key = 19");
        tmpCmd.merge(getLabelClauses(marker));

        this.sqlDM.execute(buildQuery(tmpCmd));
    }

    //----------------------------------
    protected ListHash getBasicFlank(String resultTable,String mrkKeyTable) {

        ListHash result = new ListHash();

        result.put(INSERT_CLAUSE, resultTable+"(_SNP_key)\n");
        result.put(SELECT_CLAUSE, "snp._SNP_key");
        result.put(FROM_CLAUSE, mrkKeyTable+" mrkKeys");
        result.put(FROM_CLAUSE, "MRK_Location_Cache mlc");
        result.put(FROM_CLAUSE, LOCATION_TABLE+" loc");
        result.put(FROM_CLAUSE, SNP_TABLE+" snp");
        result.put(WHERE_CLAUSE, "mrkKeys._Marker_key = mlc._Marker_key");
        result.put(WHERE_CLAUSE, "snp._Location_key = loc._Location_key");
        result.put(WHERE_CLAUSE, "loc.chromosome = mlc.chromosome");
        return result;
    }

    //----------------------------------------------------
    protected String getForwardCommand(String mrkKeyTable,
                                       String resultTable,
                                       Integer flankVal,
                                       String operator) {
        ListHash result = new ListHash();

        result.merge(getBasicFlank(mrkKeyTable,resultTable));
        result.put(WHERE_CLAUSE, "mrkKeys.strand='+'");
        if(!operator.equals("downstream"))
            result.put(WHERE_CLAUSE, "loc.coordinate >= mlc.startCoordinate-"+flankVal);
        else result.put(WHERE_CLAUSE, "loc.coordinate >= mlc.startCoordinate");
        if(!operator.equals("upstream"))
            result.put(WHERE_CLAUSE, "loc.coordinate <= mlc.endCoordinate+"+flankVal);
        else result.put(WHERE_CLAUSE, "loc.coordinate <= mlc.endCoordinate");

        return buildQuery(result);
    }

    //----------------------------------------------------------------------
    protected String getReverseCommand(String mrkKeyTable,
                                       String resultTable,
                                       Integer flankVal,
                                       String operator) {
        ListHash result = new ListHash();

        result.merge(getBasicFlank(mrkKeyTable,resultTable));
        result.put(WHERE_CLAUSE, "mrkKeys.strand='-'");
        if(!operator.equals("downstream"))
            result.put(WHERE_CLAUSE, "loc.coordinate <= mlc.endCoordinate+"+flankVal);
        else result.put(WHERE_CLAUSE, "loc.coordinate <= mlc.endCoordinate");
        if(!operator.equals("upstream"))
            result.put(WHERE_CLAUSE, "loc.coordinate >= mlc.startCoordinate-"+flankVal);
        else result.put(WHERE_CLAUSE, "loc.coordinate >= mlc.startCoordinate");

        return buildQuery(result);
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
            results.put(WHERE_CLAUSE,"mrkl.labelType not in (mrkl.labelType not in ('OS','ON','AS','AN','MY')");
            results.put(WHERE_CLAUSE, current);

        } else if(mods.contains("current symbols")) {
            results.put(WHERE_CLAUSE,"mrkl.labelType = 'MS'");
            results.put(WHERE_CLAUSE,current);

        } else {
            results.put(WHERE_CLAUSE,"mrkl.labelType not in ('AS','AN','OS','ON')");
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
        if( (wa.argCount()<=4) && (wa.hasArg("snpType") || wa.hasArg("fxn") || wa.hasArg("cmpStrains"))) {
            throw new IOException("Your query will return too many results, please add additional query parameters.");
        }
        if((wa.argCount()>4) && (wa.hasArg("accID"))) {
            throw new IOException("You cannot query by accession ids and other fields.");
        }
    }

    private static final String INSERT_CLAUSE = "insertClause";
    private static final String SELECT_CLAUSE = "selectClause";
    private static final String INTO_CLAUSE = "intoClause";
    private static final String FROM_CLAUSE = "fromClause";
    private static final String WHERE_CLAUSE = "whereClause";
    private static final String ORDER_CLAUSE = "orderClause";


    private static final String TMP_TABLE = "#snpData";
    private static final String RESULTS_TABLE = "#snpResults";
    private static final String ALLELE_RESULTS_TABLE = "#alleles";
    private static final String MARKER_RESULTS_TABLE = "#markers";

    private static final String LOCATION_TABLE = "radar..SNP_Location";
    private static final String SNP_TABLE = "radar..SNP_SNP";
    private static final String STRAIN_TABLE = "radar..SNP_Strain";
    private static final String ALLELE_TABLE = "radar..SNP_Strain_SNP_Assoc";
    private static final String MARKER_ASSOC_TABLE = "radar..SNP_Location_Marker_Assoc";

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
        "coordinate          int         not null,\n"+
        "orientation         char(1)     NULL,\n"+
        "seqNum              int         not null)\n";

    private static final String CREATE_ALLELE_RESULTS_TABLE =
    "create table %s (\n"+
        "_SNP_key            int         not null,\n"+
        "strainName          varchar(40) not null,\n"+
        "allele              varchar(80) not null,)\n";

    private static final String CREATE_MARKER_RESULTS_TABLE =
    "create table %s (\n"+
        "_SNP_key            int         not null,\n"+
        "symbol              varchar(50) not null)\n";

    private static final String RESULTS_COMMAND =
    "select distinct rt.rsID, rt.ssID, rt.pID, rt.providerName, rt.functionalLocation,\n"+
    "rt.polymorphismClass, rt.chromosome, rt.coordinate, rt.orientation, rt._SNP_key\n"+
    "from %s rt, MRK_Chromosome mc\n"+
    "where mc.chromosome = rt.chromosome\n"+
    "order by rt.seqNum, rt.coordinate\n";

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

