package org.jax.mgi.shr.datafactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.jax.mgi.shr.cache.ExpiringObjectCache;
import org.jax.mgi.shr.dto.DTO;
import org.jax.mgi.shr.dto.DTOConstants;
import org.jax.mgi.shr.dbutils.DBException;

/**
 * @module Phenotype.java
 * @author dow
 */

/** The Phenotype is specifically for defining a phenotype/genotype concept
 *  used in the WI.  It represetns an allelic composition, it's associated 
 *  background strain, and the annotations associated with it..
 * @is a class for representing phenotypes/genotypes..
 * @has all data necessary for displaying a given genotype/phenotype and it's
 *      associated MP annotations.
 * @does very little on it's own.  Is used for holding and displaying phenotype
 *       data specifically associated with the MP.
*/
public class Phenotype implements Comparable {

    /////////////////////
    // instance variables
    /////////////////////
    private Integer genotypeKey;
    private Integer orderVal;
    private String allelicComposition;
    private String strainBackground;
    private HashMap headers;
    private HashMap annots;
    private String htmlCompound = null;
    private DTO primaryImage = null;

    ///////////////
    // Constructors
    ///////////////

    /** constructor; instantiates and initializes a new Phenotype.
     * @param genotype this is the database key for this genotype
     * @param orderVal used for sorting genotypes
     * @param compound the display value for the genotype/allelic composition
     * @param background the background strain associated with this genotype
     * @assumes nothing
     * @effects nothing
     * @throws nothing
     */
    public Phenotype (Integer genotype, 
                      Integer orderVal,
                      String compound, 
                      String background) {
        this.genotypeKey = genotype;
        this.orderVal = orderVal;
        this.allelicComposition = compound;
        this.strainBackground = background;

        this.headers = new HashMap();
        this.annots  = new HashMap();
    }

    //////////////////
    // public methods
    //////////////////

    /** returns the database key for this genotype.
     * @assumes nothing
     * @effects nothing
     * @returns Integer representation of database key
     * @throws nothing
     */
    public Integer getGenotypeKey() {
        return genotypeKey;
    }

    /** returns the allelic compositions exactly as it was stored in the
     *  database.
     * @assumes nothing
     * @effects nothing
     * @returns String  value of the allelic composition
     * @throws nothing
     */
    public String getCompound() {
        return allelicComposition;
    }

    public void loadPrimaryImage(ImageFactory imgFac) 
        throws DBException
    {
        
        this.primaryImage = 
            imgFac.getPrimaryThumbnailForGenotype (getGenotypeKey().intValue());
    
    }

    public DTO getPrimaryImage() {
        return this.primaryImage;
    }

    /** returns the allelic compositions in an HTML based format, intended for
     *  the allele detail page in the WI..
     * @assumes nothing
     * @effects nothing
     * @returns String  value of the allelic composition
     * @throws nothing
     */
    public String getHTMLCompound(String javaWI) {
        // Build it only once!
        if (this.htmlCompound == null && this.allelicComposition != null) {
            StringBuffer sb = new StringBuffer();
            boolean first = true;

            //  Pull apart the compound into multiple rows for each allele pair
            for (Iterator i = getParsedCompound().iterator(); i.hasNext(); ) {
                //  If there are multiple allele pairs they are seperated by
                //  a line break.
                if (first) {
                    first = false;
                }
                else {
                    sb.append("<br>"); 
                }
                
                Map allelePair = (Map)i.next();

                //  There might actually be multiple alleles within
                //  each allele part
                List allele1 = (List)allelePair.get("allele1");
                List allele2 = (List)allelePair.get("allele2");

                boolean firstAllele = true;
                for (int j = 0; j < allele1.size(); j++) {
                    String a1MGIID = 
                        (String)((HashMap)allele1.get(j)).get("id");
                    String a1Symbol = 
                        (String)((HashMap)allele1.get(j)).get("symbol");

                    //  if there are multiple alleles, put a space between
                    //  them.
                    if (! firstAllele) {
                        sb.append(" ");
                    }
                    else {
                        firstAllele = false;
                    }
                    //  If there is no mgi id, don't put in a link.
                    if (a1MGIID == null) {
                        sb.append(superscript(a1Symbol));
                    }
                    else {
                        sb.append("<a href=\"");
                        sb.append(javaWI);
                        sb.append(a1MGIID);
                        sb.append("\">");
                        sb.append(superscript(a1Symbol));
                        sb.append("</a>");
                    }
                }
                sb.append("/");


                firstAllele = true;
                for (int j = 0; j < allele2.size(); j++) {
                    String a2MGIID =  
                        (String)((HashMap)allele2.get(j)).get("id");
                    String a2Symbol =  
                        (String)((HashMap)allele2.get(j)).get("symbol");

                    //  if there are multiple alleles, put a space between
                    //  them.
                    if (! firstAllele) {
                        sb.append(" ");
                    }
                    else {
                        firstAllele = false;
                    }
                    //  If there is no mgi id, don't put in a link.
                    if (a2MGIID == null) {
                        sb.append(superscript(a2Symbol));
                    }
                    else {
                        sb.append("<a href=\"");
                        sb.append(javaWI);
                        sb.append(a2MGIID);
                        sb.append("\">");
                        sb.append(superscript(a2Symbol));
                        sb.append("</a>");
                    }
                }
            }
            this.htmlCompound = sb.toString();
        }
        else if (this.allelicComposition == null) {
            //  Formatting was thrown off when this was null
            this.htmlCompound = "&nbsp;";
        }
        return this.htmlCompound;
    }

    /** return the strain background.
     * @assumes nothing
     * @effects nothing
     * @returns String  value of the strain background
     * @throws nothing
     */
    public String getBackground() {
        return strainBackground;
    }

    /** return the value used for sorting genotypes associated with a given
     *  allele.
     * @assumes nothing
     * @effects nothing
     * @returns Integer value for sorting.
     * @throws nothing
     */
    public Integer getOrderVal() {
        return this.orderVal;
    }

    /** return a list of allele pairs in the order they appear in the 
     *  allelic composition.
     * @assumes nothing
     * @effects nothing
     * @returns List of the allele pairs.
     * @throws nothing
     */
    public List getParsedCompound() {
        ArrayList al = new ArrayList();
        //  When there are multiple allele pairs in the composition,
        //  the are seperated by the newline character.
        StringTokenizer st = new StringTokenizer(allelicComposition, "\n");
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if (token != null && ! token.equals("")) {
                al.add(  parseAllelePair(token) );
            }
        }
        return al;
    }

    /** add a header term value to this phenotype.
     * @param termKey is the database key for this header term.
     * @param id is the MGI Accession ID for this header term.
     * @param headerTerm the actual term value
     * @param orderVal Used for sorting header terms.
     * @assumes nothing
     * @effects nothing
     * @returns nothing.
     * @throws nothing
     */
    public void addHeaderTerm(Integer termKey, String id, String headerTerm, 
                              Integer orderVal) {
        HeaderAnnotation header = new HeaderAnnotation(termKey, id, headerTerm,
                                                       orderVal);
        this.headers.put(termKey, header);
    }

    /** add a MP annotation term value to this phenotype.
     * @param termKey is the database key for this mp term.
     * @param id is the MGI Accession ID for this mp term.
     * @param headerTerm the actual term value
     * @param orderVal Used for sorting mp annotation terms.
     * @assumes nothing
     * @effects nothing
     * @returns nothing.
     * @throws nothing
     */
    public void addAnnotTerm(Integer termKey, String id, String annotTerm,
                             Integer orderVal) {
        SubAnnotation annot = new SubAnnotation(termKey, id, annotTerm, 
                                                orderVal);
        this.annots.put(termKey, annot);
    }

    /** add a MP annotation evidence associated with a term to this phenotype.
     * @param termKey is the database key for the mp term associated with
     *        this evidence.
     * @param evidenceKey the database key for the evidence entry.
     * @param refKey the database key of the reference for this evidence
     * @param jNum the J number for the reference
     * @param note if there is written evidence, it will be in this note..
     * @assumes nothing
     * @effects nothing
     * @returns nothing.
     * @throws nothing
     */
    public void addAnnotEvidence(Integer termKey, Integer evidenceKey,
                                 Integer refKey, String jNum, String note,
                                 String noteType) {
        
        SubAnnotation a = (SubAnnotation)this.annots.get(termKey);
        a.addEvidence(evidenceKey, refKey, jNum, note, noteType);
   }

    /** if true, then there are headers associated with this phenotype.
     * @assumes nothing
     * @effects nothing
     * @returns true has headers, false does not.
     * @throws nothing
     */
    public boolean hasHeaders() {
        if (this.headers.size() > 0) {
            return true;
        }
        else {
            return false;
        }

    }

    /** if true, then there are annotationss associated with this phenotype.
     * @assumes nothing
     * @effects nothing
     * @returns true has annotations, false does not.
     * @throws nothing
     */
    public boolean hasAnnotations() {
        if (this.annots.size() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /** This method will associate all of the headers that have been added
     *  to the phenotype with all of the annotations that have been added to
     *  the phenotype.
     *  <P>
     *  The proper organization of headers and annotations associated with a
     *  phenotype can be found in the 3.2 requirements document under the
     *  section for the allele detail page.  I will address the organization
     *  briefly.
     *  <P>
     *  First we find which annotations are descendents of which headers.  An
     *  annotation can be added under multiple headers.  Then under each 
     *  header, the annotations are organized so that if a term is a 
     *  descendent of another term it will be added under that, instead of
     *  directly under the header term.  
     *  <P>
     *  This is primarily for the purposes of display in the WI Allele Detail
     *  page.
     * @assumes It assumes that you've already added all the headers and
     *    annotations you are going to.  If you don't have some of both,
     *    the method won't continue.  If you add more after running this
     *    method, they will not be organized with the rest.
     * @effects alters the structure of the header and annotations internal
     *    structures for this object.
     * @returns nothing.
     * @throws nothing
     */
    public void organizeAnnotations() {

        //  Get the ClosureDag from memory.  We use this to determine
        //  descendancy.
        ExpiringObjectCache eoc = ExpiringObjectCache.getSharedCache();
        MPClosureDAG cdag = 
            (MPClosureDAG)eoc.get(PhenotypeFactory.CLOSURE_CACHE_KEY);


        if (hasHeaders() || hasAnnotations()) {

            //  If there are not headers and no annotations, there is no point!
            if (hasHeaders() && hasAnnotations()) {
                //  For each header term, check to see if any of the annots
                //  are descendents.  If they are, add them to the header.
                for (Iterator i = headers.values().iterator(); i.hasNext(); ) {
                    HeaderAnnotation ha = (HeaderAnnotation)i.next();
                    for (Iterator j = annots.values().iterator(); 
                         j.hasNext(); ) {
                        SubAnnotation sa = (SubAnnotation)j.next();
                        // If the sub annotation is a descendent of the header
                        // or they are the same annotation, add as descendent.
                        if ((ha.getKey().equals(sa.getKey())) ||
                            (cdag.isDescendent(ha.getKey(), sa.getKey()))) {
                            ha.addDescendent(sa);
                            //  Mark annotation as used.  Any left over will
                            //  be added to a special header later.
                            sa.use();
                        }
                    }             
                }
            }

            //  Create an Other Annotations header in case there are
            //  some annotations with no headers.
            HeaderAnnotation other = new HeaderAnnotation(new Integer(-1), 
                                                          new String(""), 
                                                          "Other Annotations",
                                                          new Integer(999));
            boolean someOthers = false;
            //  Organize the case where there are annots with no headers.
            for (Iterator i = annots.values().iterator(); i.hasNext(); ) {
                SubAnnotation sa = (SubAnnotation)i.next();
                if (! sa.used()) {
                    someOthers = true;
                    other.addDescendent(sa);
                    sa.use();
                }
            }

            if (someOthers) {
                headers.put(other.getKey(), other);
            }
        }
    }

    /** Returns the list of all headers for this phenotype.
     * @assumes nothing
     * @effects nothing
     * @returns List of header terms as HeaderAnnotation objects.
     * @throws nothing
     */
    public List getHeaders() {
        Collection headColl = headers.values();
        HeaderAnnotation[] headarray = 
            (HeaderAnnotation[])headColl.toArray(new HeaderAnnotation[0]);
        Arrays.sort(headarray);
        return Arrays.asList(headarray);
    }

    /** Returns the list of all annotations for this phenotype.
     * @assumes nothing
     * @effects nothing
     * @returns List of Mp Annotation terms as SubAnnotation objects.
     * @throws nothing
     */
    public List getAnnotations() {
        Collection annotColl = annots.values();
        SubAnnotation[] annotarray = 
            (SubAnnotation[])annotColl.toArray(new SubAnnotation[0]);
        Arrays.sort(annotarray);
        return Arrays.asList(annotarray);
    }

    /** Over-rides the compareTo method necessary for the Comparable interface.
     *  This method orders by the "order value" passed into the constructor.
     * @assumes nothing
     * @effects nothing
     * @returns List of Mp Annotation terms as SubAnnotation objects.
     * @throws nothing
     */
    public int compareTo(Object o) {
        Phenotype p = (Phenotype)o;
        int ret = this.orderVal.compareTo(p.getOrderVal());
        return ret;
    }

    /*  This is the old version of compareTo.  In it I did some
        additional sorting.  In theory this should now be unnecessary
        as all of this should now be done in the database.

        I wanted to wait to totally throw the method until after testing
        in 3.2 is completed.
    public int compareTo(Object o) {
        Phenotype p = (Phenotype)o;
        int ret = this.orderVal.compareTo(p.getOrderVal());
        if (ret == 0) {
            int compareValue = 1;
            int thisValue = 1;
            String compBackground = p.getBackground();
            if (isInvolves(compBackground)) {
                compareValue = 2;
            }
            else if (isEither(compBackground)) {
                compareValue = 3;
            }
            else if (isUnspecified(compBackground)) {
                compareValue = 4;
            }

            
            if (isInvolves(strainBackground)) {
                thisValue = 2;
            }
            else if (isEither(strainBackground)) {
                thisValue = 3;
            }
            else if (isUnspecified(strainBackground)) {
                thisValue = 4;
            }

            if (thisValue < compareValue) {
                ret = -1;
            }
            else if (thisValue > compareValue) {
                ret = 1;
            }
            else {
                ret = strainBackground.compareTo(compBackground);
            }
            
        }
        return ret;
    }
    */

    ///////////////////////////
    // private instance methods
    ///////////////////////////

    /*  These three methods wer used in conjunction with the old
        compareTo method.  When it is gone, they should go as well.
    private boolean isInvolves(String s) {
        if (s.startsWith("involves:")) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isEither(String s) {
        if (s.startsWith("either:")) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isUnspecified(String s) {
        if (s.equals("Not Specified")) {
            return true;
        }
        else {
            return false;
        }
    }
    */

    //  This method is used to parse an allele pair that was part of
    //  an allelic composition returned from the database.
    private Map parseAllelePair(String ap) {
        HashMap hm = new HashMap();
        //  An allele pair is seperated by a forward slash character.
        String[] pair = ap.split("\\/");
        hm.put("allele1", parseAllele(pair[0]) );
        if (pair.length > 1) {
            hm.put("allele2", parseAllele(pair[1]) );
        }
        else {
            hm.put("allele2", parseAllele(""));
        }
        return hm;
    }

    //  This method is used to parse an allele into two parts. The
    //  allele is likely the result of a call to parseAllelePair.
    private List parseAllele(String allele) {
        ArrayList ret = new ArrayList();

        String[] vals = allele.split(" ");
        if (vals.length > 1) {
            for (int i = 0; i < vals.length; i++) {
                ret.addAll((List)parseAllele(vals[i]));
            }
        }
        else {
        
            HashMap hm = new HashMap();
            if (! allele.startsWith("\\Allele") ) {
                hm.put("id",null);
                hm.put("symbol", allele);
            }
            else {
                String value = allele.substring(8, allele.length()-1);
                String[] pair = value.split("\\|");
                hm.put("id",pair[0]);
                hm.put("symbol",pair[1]);
            }
            ret.add(hm);
        }
        return ret;
    }

    /** convert all "<" and ">" pairs in 's' to be HTML superscript tags.
     * @param s the source String
     * @return String as 's', but with the noted replacement made.  returns
     *    null if 's' is null.
     * @assumes nothing
     * @effects nothing
     * @throws nothing
     */
    private  String superscript (String s)
    {
        return superscript (s, "<", ">");
    }

    /* -------------------------------------------------------------------- */

    /** convert all 'start' and 'stop' pair in 's' to be HTML
     *    superscript tags.
     * @param s the source String
     * @param start the String which indicates the position for the HTML
     *    superscript start tag "<SUP>"
     * @param stop the String which indicates the position for the HTML
     *    superscript stop tag "</SUP>"
     * @return String as 's', but with the noted replacement made.  returns
     *    null if 's' is null.  returns 's' if either 'start' or 'stop' is
     *    null.
     * @assumes nothing
     * @effects nothing
     * @throws nothing
     */
    private String superscript (String s, String start, String stop)
    {
        if (s == null)
            {
                return null;			// no source string
            }

        if ((start == null) || (stop == null))
            {
                return s;				// no start/stop string
            }

        // Otherwise, find the first instance of 'start' and 'stop' in 's'.
        // If either does not appear, then short-circuit and just return 's'
        // as-is.

        int startPos = s.indexOf(start);
        if (startPos == -1)
            {
                return s;
            }

        int stopPos = s.indexOf(stop);
        if (stopPos == -1)
            {
                return s;
            }

        int startLen = start.length();	// how many chars to cut out for start
        int stopLen = stop.length();	// how many chars to cut out for stop
        int sectionStart = 0;		// position of char starting section

        StringBuffer sb = new StringBuffer();

        while ((startPos != -1) && (stopPos != -1))
            {
                sb.append (s.substring(sectionStart, startPos));
                sb.append ("<SUP>");
                sb.append (s.substring(startPos + startLen, stopPos));
                sb.append ("</SUP>");

                sectionStart = stopPos + stopLen;
                startPos = s.indexOf(start, sectionStart);
                stopPos = s.indexOf(stop, sectionStart);
            }
        sb.append (s.substring(sectionStart));

        return sb.toString();
    }
}
