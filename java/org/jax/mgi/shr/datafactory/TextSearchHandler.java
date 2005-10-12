package org.jax.mgi.shr.datafactory;

import java.util.*;
import java.io.*;

import org.jax.mgi.shr.exception.MGIException;

/**
 * @module TextSearchHandler.java
 * @author dow
 */

/** The StreamGobbler is used by the TextSearchHandler to grab standard out
 *  and standard error when the search engine program is exec'd.
 * @is  a thread that grabs info from a stream.
 * @has knowledge of grabbing data from atream..
 * @does grabs data from an input stream and puts it in a StringBuffer.
*/
class StreamGobbler extends Thread {
    private ArrayList lines;
    private InputStream is;
    private String type;
    private OutputStream os;
    private boolean debug = false;
    static String newline = System.getProperty("line.separator");
    
    StreamGobbler(InputStream is, String type) {
        this(is, type, null);
    }

    StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
        this.lines = new ArrayList();
    }
    
    public void run() {
        try {
            PrintWriter pw = null;
            if (os != null) {
                pw = new PrintWriter(os);
            }
                
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ((line = br.readLine()) != null) {
                if (pw != null) {
                    pw.println(line);
                }
                
                lines.add(line);
                
                if (debug) {
                    System.out.println(type + ">" + line);    
                }
            }
            if (pw != null) {
                pw.flush();
            }
            isr.close();
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();  
        }
    }
    
    /** return content of a stream as a StringBuffer
     * @return StringBuffer representing content of stream
     * @assumes that the stream is a finite string of information..
     * @effects nothing
     */
    public StringBuffer getData() {
        StringBuffer sb = new StringBuffer();
        ArrayList l = getLines();
        
        for (int i = 0; i < l.size(); i++) {
            sb.append(l.get(i));
            
            if (i != (l.size() - 1)) {
                sb.append(newline);
            }
        }
        
        return sb;
    }
    
    public ArrayList getLines() {
        return this.lines;
    }
}

/** The TextSearchHandler interfaces with a specific text search engine.
 *  The text search engine that TextSearchHandler currently interfaces with, is
 *  written in Python.  It was implemented by Jon Beal for the MPR MGI 3.2 
 *  release.  This class abstracts away the source used for doing the text
 *  searching and makes it possible to change to some other mechanism at
 *  a later date.   Currently the class is constructed with the knowledge of
 *  what script should be called to do the text search.  This was included so
 *  that the location and script name could be configureable.  However,
 *  behind the scenes there are some assumptions about the method signatures
 *  that can be used.
 * @is  for providing text search capabilities to a defined set of indicies.
 * @has knowledge of a finite set of indicies that can be searched..
 * @does produces a resulting from and where clause that can be used to join
 *       via sql to the actual results of a text search..
*/
public class TextSearchHandler {
    /////////////////////
    // instance variables
    /////////////////////
 
    //  The program that will be exec'd.  If we could migrate to a mechanism
    //  which would allow us to make direct method calls, we would not need
    //  this.
    private String searchScript;

    /////////////////////
    // public constants
    /////////////////////

    //  The datasets we can search, nothing currently uses MP.
    public static final String OMIM  = "omim";
    public static final String MP    = "mp";
    public static final String PIRSF = "pirsf";


    ///////////////
    // Constructors
    ///////////////

    /** constructor; instantiates and initializes a new TextSearchHandler.
     * @param searchScript this is the actual program that will be exec'd
     *    to execute the search.
     * @assumes the program corresponds with methods and datasets that this
     *     class has knowledge of.
     * @effects sets the search script that will be used.
     * @throws nothing
     */
    public TextSearchHandler(String searchScript) {

        this.searchScript = searchScript;
    }

    //////////////////
    // public methods
    //////////////////

    /** Execute a text search against a given dataset for a given search string
     *  This is the primary method used to provide MGI text searching to Java
     *  classes.  The text searching works with a finite set of text indexes
     *  which correspond to a set of class constants in TextSearchHandler.
     *  The result of the search will be returned as a HashMap, containing two
     *  attributes a "from" clause and a "where" clause.  These clauses can be 
     *  joined with some user defined sql to aquire the results of the search
     *  which are stored in a table in the Radar database.  The from and where
     *  are both string arrays, as there may be multiple tables in the from
     *  or multiple expressions in the where.  The need to be joined together
     *  using commas (where clause) and the SQL "and" conjuction..
     * @param dataset this should correspond to one of the supported datasets 
     *   defined as a constants to this class.
     * @param searchString a string representation of the parameters to be 
     *   searched for in the dataset.
     * @assumes a valid dataset is being searched and valid text search syntax
     *   is being passed.  Also assumes that a mechanism (searchScript) exists
     *   to execute the search.
     * @effects updates the radar database with the results, assuming any are
     *   found.
     * @returns a java.util.HashMap, containing two entries: <UL>
     *   <LI> a from clause, which is a string array of table names
     *   <LI> a where clause, which is a  string array of sql expressions.
     *   </UL>If no results are found, an empty HashMap is returned
     * @throws MGIException if the dataset or search string are invalid.
     */
    public HashMap search(String dataset, String searchString) 
        throws MGIException
    {
        if (dataset.equals(TextSearchHandler.OMIM)) {
            return doSearch("omimVocabClauses","vt._Term_key", 
                            searchString);
        }
        if (dataset.equals(TextSearchHandler.MP)) {
            return doSearch("phenotypeClauses","aa1._Allele_key",
                            searchString);
        }
        if (dataset.equals(TextSearchHandler.PIRSF)) {
            return doSearch("pirsfVocabClauses","vt._Term_key",
                            searchString);
        }
        else {
            throw new MGIException("Invalid dataset to search!");
        }
    }

    /////////////////////////////
    // protected instance methods
    /////////////////////////////


    /** The method that does the actual work of interfacing with the
     *  text search engine.
     * @param methodName  The name of the method to be called on the
     *    text search engine, this will directly correspond to the data set
     *    being searched.
     * @param keyName  The SQL key that will be joined to an "_Object_key"
     *    via SQL.
     * @param terms  These are the terms or search string that is being used in
     *    the search against the given dataset.
     * @assumes the method name is valid for the text search engine being,
     *    that we have the handle to the text search engine (script) already.
     * @effects updates the Radar database with the results of the search
     * @returns  a java.util.HashMap, containing two entries: <UL>
     *   <LI> a from clause, which is a string array of table names
     *   <LI> a where clause, which is a  string array of sql expressions.
     *   </UL>If no results are found, an empty HashMap is returned
     * @throws MGIException if any of the parameters are invalid or the
     *   search fails for a reason other than no results found.
     */
    protected HashMap doSearch(String methodName,
                               String keyName, 
                               String terms) 
        throws MGIException
    {

        try {
            Runtime rt = Runtime.getRuntime();

            //  The signature of the python program is:
            //    the method to be called
            //    the key value to be included in the returned sql
            //    the search terms.
            //  The search terms need to be wrapped in single quotes in case
            //  there are parenthesis or double quotes in the string.  The 
            //  single quotes will be stripped off in python.

            String pyCmd = this.searchScript + " " +  methodName + " " + 
                keyName + " '" + terms.replaceAll("'","''") + "'";


            //  The command we will exec
            String[] cmd = {"/bin/sh", "-c", pyCmd};
        
            //  Our handle to a process where the exec is occuring.
            Process proc = rt.exec(cmd);
            
            // any error message?
            StreamGobbler errorGobbler = new 
                StreamGobbler(proc.getErrorStream(), "ERROR");            
            
            // any output?
            StreamGobbler outputGobbler = new 
                StreamGobbler(proc.getInputStream(), "OUTPUT");
                
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
                                    
            // any error???
            int exitVal = proc.waitFor();
            
            // command failed, throw an exception
            if (exitVal != 0) {
                StringBuffer error = errorGobbler.getData();
                throw new MGIException("ERROR Received from script:\n"
                                       + error.toString());
            }
            
            // get the contents of standard out...
            StringBuffer output = outputGobbler.getData();
            
            String result = output.toString();

            //  The string returned is sepearated into a from clause
            //  and a where clause by 3 pipes.
            String[] clauses = result.split("\\|\\|\\|");

            // Both the from clause and the where clause are wrapped in open
            // and close square brakets '[' ']', and if there are multiple 
            // parts to the clause, they are comma separated.
            HashMap hm = new HashMap();

            // First parse out the from clause
            String[] fromClause = clauses[0].split(",");
            for (int i = 0; i < fromClause.length; i++) {
                String clause = fromClause[i].trim();
                if (clause.charAt(0) == '[') {
                    // must remove the bracket and a quote
                    clause = clause.substring(2);
                }
                else {
                    // just remove the quote
                    clause = clause.substring(1);
                }
                if (clause.charAt(clause.length() -1) == ']'){
                    // must remove the bracket and a quote
                    clause =
                        clause.substring(0,clause.length() -2);
                }
                else {
                    // just remove the quote
                    clause = 
                        clause.substring(0,clause.length() -1);
                }
                fromClause[i] = clause;
            }
            hm.put("from", fromClause);

            //  Then parse out the where clause
            String[] whereClause = clauses[1].split(",");
            for (int i = 0; i < whereClause.length; i++) {
                String clause = whereClause[i].trim();
                if (clause.charAt(0) == '[') {
                    // must remove the bracket and a quote
                    clause = clause.substring(2);
                }
                else {
                    // just remove the quote
                    clause = clause.substring(1);
                }
                if (clause.charAt(clause.length() -1) == ']'){
                    // must remove the bracket and a quote
                    clause =
                        clause.substring(0,clause.length() -2);
                }
                else {
                    // just remove the quote
                    clause = 
                        clause.substring(0,clause.length() -1);
                }
                whereClause[i] = clause;
            }
            hm.put("where", whereClause);

            return hm;
            
        } catch (Throwable t) {
           
            t.printStackTrace();
            throw new MGIException("ERROR While Doing Search:\n" 
                                   + t.toString());
        }
    }

    // Main method for unit testing...
    public static void main(String args[]) {
        
        if (args.length < 1) {
            System.out.println("Must enter some search parameters!");
            System.exit(1);
        }
        String parms = "";
        String method = "omimVocabClauses";
        String key = "m._Term_key";
        
        for (int i = 0; i < args.length; i++) {
            String val = args[i];
            if (i == 0) {
                if (val.charAt(0) == '\'') {
                    val = val.substring(1);
                }
            }
            else if (i == (args.length - 1)) {
                if (val.charAt(val.length() -1) == '\'') {
                    val = val.substring(0,val.length() - 2);
                }
            }

            if (i > 0) {
                parms += " "; 
            }
            parms += val;
        }
        System.out.println(parms);

        TextSearchHandler tsh = 
            new TextSearchHandler("/home/dow/omim/TextSearchWrapper.py");

        try {
            HashMap results = tsh.search(TextSearchHandler.OMIM, parms);      

            String out = "Results from search\nfrom ";
        
            for (int i = 0; i < ((String[])results.get("from")).length; i++) {
                out = out + ((String[])results.get("from"))[i] + ", ";
            }

            out = out + "\nwhere ";
            for (int i = 0; i < ((String[])results.get("where")).length; i++) {
                out = out + ((String[])results.get("where"))[i] + " and ";
            }


            System.out.println(out + "\n");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
            
    }
}
