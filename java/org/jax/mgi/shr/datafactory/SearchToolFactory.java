package org.jax.mgi.shr.datafactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.Arrays;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dto.DTOConstants;
import org.jax.mgi.shr.cache.ExpiringObjectCache;
import org.jax.mgi.shr.timing.TimeStamper;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.DBException;

import searchtool.SearchToolHandler;

/**
* A SearchTool is a servlet that implements the search tool for the WI.
* A SearchTool has no instance variables.
* A SearchTool processes requests from the web and gets the appropriate
* search results.
*/
public class SearchToolFactory extends Factory {

    //The args that MUST be passed to the servlet.  There can be
    //more than one selectedQuery, but only the first query will be
    //processed.
    public static final String QUERYOBJ_KEY = "selectedQuery";
    public static final String QUERYSTR_KEY = "query";
    public static final String MAX_COUNT_EXCEEDED = "The maximum number of users has been reached.  Please try your search again later.";
    protected static volatile int iActiveCount = 0;


    public SearchToolFactory (DataFactoryCfg config,
                              SQLDataManager sqlDM,
                              Logger logger) {
        super(config,sqlDM,logger,"SearchToolFactory");

    }

    public String getKey(Map parms) throws DBException {
        return "-1";
    }
    public int getKeyForID (String accID) throws DBException {
        return -1;
    }
    public DTO getBasicInfo (int key) throws DBException {
        return DTO.getDTO();
    }
    /**
    * Extracts the query string from req and generates the main search
    * results section.
    * @param req A HttpServletRequest object containing the query string
    *            from the client
    * @returns A String containing the HTMLized search results.
    */
    public DTO getFullInfo(Map parms) {

        String strPythonWIInstallation;
        String strQuery, strResults;
        ArrayList alSelectedQueries = new ArrayList();
        SearchToolHandler results;
        DTO dtoResults = DTO.getDTO();
        String[] tmp;
        int iMaxCount = Integer.parseInt(config.get("MAX_CONNECTIONS"));
        int newCount;

        strPythonWIInstallation = config.get("WI_URL");

        //determine the selected searches and the query string.

        tmp = (String[])parms.get(QUERYSTR_KEY);
        strQuery = tmp[0];
        tmp = (String[])parms.get(QUERYOBJ_KEY);
        alSelectedQueries.addAll(Arrays.asList(tmp));
        if(strQuery.length()==0) {
            dtoResults.set(DTOConstants.SearchToolResults,
                  "<H3><B>There was a problem with your query.</H3><BR>"+
                  "Entries must contain some text.<BR>"+
                  "Please resubmit your query.</B>");
           return dtoResults;
        }
        if(alSelectedQueries.indexOf(SearchToolHandler.QUERYOPT_ALL) != -1)
           newCount = 10;
        else
            newCount = alSelectedQueries.size();
        iActiveCount += newCount;
        if(iActiveCount<iMaxCount)
            results = new SearchToolHandler(strQuery,
                                            strPythonWIInstallation,
                                            alSelectedQueries,
                                            config);
        else {
            iActiveCount -= newCount;
            dtoResults.set(DTOConstants.SearchToolResults,MAX_COUNT_EXCEEDED);
            return dtoResults;
        }
        try {
            strResults = results.get();
        } catch(Exception e) {
            dtoResults.set(DTOConstants.SearchToolResults,
                           "There was a system error, please contact User Support");
            return dtoResults;
        }
        iActiveCount -= newCount;
        results = null;
        System.gc();
        dtoResults.set(DTOConstants.SearchToolResults,strResults);
        return dtoResults;
    }
/*
    protected boolean isValid(String strQuery) {
        if(strQuery.indexOf("%26")!=-1)
            return false;
        return true;
    }

    protected String getPageTitle() {
    return "Search Results";
    }

    /**
    * Generates the page header and title.
    * @param req A HttpServletRequest object from the client
    * @returns A String containing a help link and the words "Search Tool"
    *          in a blue box.
    */
/*    protected String header (HttpServletRequest req) {

        String strHead;
    strHead =  "<TABLE WIDTH=100% border=0 cellpadding=2 cellspacing=1>"+
           " <TR>"+
           "  <TD WIDTH=100% BGCOLOR=\"#D0E0F0\">";
        strHead += "<TABLE WIDTH=100% cellpadding=0 cellspacing=0>"+
                    "\r\n"+
                   " <TR>\r\n"+
                   "  <TD WIDTH=20% VALIGN=center ALIGN=left>\r\n"+
           "   <FONT COLOR=\"000000\" face=\"Arial,Helvetica\" size=-1>"+
                   "   <A HREF=\""+config.get("WI_URL")+
                       "userdocs/searchtool_help.shtml#results\""+
                   "   border=0><IMG SRC=\""+config.get("WI_URL")+
                       "images/shared/help_large.jpg\""+
                   "   border=0 HEIGHT=30 WIDTH=32></A>\r\n"+
           "   </FONT>"+
                   "  </TD>\r\n"+
                   "  <TD VALIGN=center ALIGN=center WIDTH=\"60%\">\r\n"+
                   "   <FONT COLOR=\"#000000\" SIZE=5 face=\"Arial,Helvetica"+
                       "\">\r\n"+
                   "    <B>"+getPageTitle()+"</B>\r\n"+
                   "   </FONT>\r\n"+
                   "  </TD>\r\n"+
           "  <TD VALIGN=center ALIGN=right WIDTH=\"20%\">"+
           "  </TD>\r\n"+
                   " </TR>\r\n"+
                   "</TABLE>\r\n";
    strHead += "  </TD>"+
           " </TR>"+
           "</TABLE>";
        return strHead;
    }*/
}

