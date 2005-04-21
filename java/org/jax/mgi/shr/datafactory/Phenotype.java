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

public class Phenotype implements Comparable {

    private Integer genotypeKey;
    private Integer orderVal;
    private String allelicComposition;
    private String strainBackground;
    private HashMap headers;
    private HashMap annots;
    private String htmlCompound = null;

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

    public Integer getGenotypeKey() {
        return genotypeKey;
    }

    public String getCompound() {
        return allelicComposition;
    }

    public String getHTMLCompound(String javaWI) {
        if (this.htmlCompound == null && this.allelicComposition != null) {
            StringBuffer sb = new StringBuffer();
            //topTable.append("WIFetch?page=alleleDetail&id=");
            boolean first = true;
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
                Map allele1 = (Map)allelePair.get("allele1");
                Map allele2 = (Map)allelePair.get("allele2");
                String a1MGIID = (String)allele1.get("id");
                String a1Symbol = (String)allele1.get("symbol");
                String a2MGIID = (String)allele2.get("id");
                String a2Symbol = (String)allele2.get("symbol");

                //  If there is no mgi id, don't put in a link.
                if (a1MGIID == null) {
                    sb.append(superscript(a1Symbol));
                    sb.append("/");
                }
                else {
                    sb.append("<a href=\"");
                    sb.append(javaWI);
                    sb.append(a1MGIID);
                    sb.append("\">");
                    sb.append(superscript(a1Symbol));
                    sb.append("</a>/");
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
            this.htmlCompound = sb.toString();
        }
        else if (this.allelicComposition == null) {
            //  Formatting was thrown off when this was null
            this.htmlCompound = "&nbsp;";
        }
        return this.htmlCompound;
    }

    public String getBackground() {
        return strainBackground;
    }

    public Integer getOrderVal() {
        return this.orderVal;
    }

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

    public void addHeaderTerm(Integer termKey, String id, String headerTerm, 
                              Integer orderVal) {
        HeaderAnnotation header = new HeaderAnnotation(termKey, id, headerTerm,
                                                       orderVal);
        this.headers.put(termKey, header);
    }

    public void addAnnotTerm(Integer termKey, String id, String annotTerm,
                             Integer orderVal) {
        SubAnnotation annot = new SubAnnotation(termKey, id, annotTerm, 
                                                orderVal);
        this.annots.put(termKey, annot);
    }

    public void addAnnotEvidence(Integer termKey, Integer evidenceKey,
                                 Integer refKey, String jNum, String note) {
        
        SubAnnotation a = (SubAnnotation)this.annots.get(termKey);
        a.addEvidence(evidenceKey, refKey, jNum, note);
   }

    public boolean hasHeaders() {
        if (this.headers.size() > 0) {
            return true;
        }
        else {
            return false;
        }

    }

    public boolean hasAnnotations() {
        if (this.annots.size() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public void organizeAnnotations() {

        //  Get the ClosureDag from memory.  We use this to determine
        //  descendancy.
        ExpiringObjectCache eoc = ExpiringObjectCache.getSharedCache();
        MPClosureDAG cdag = 
            (MPClosureDAG)eoc.get(PhenotypeFactory.CLOSURE_CACHE_KEY);

        //  If there are not headers and no annotations, there is no point!
        if (hasHeaders() && hasAnnotations()) {
            //  For each header term, check to see if any of the annotations
            //  are descendents.  If they are, add them to the header.
            for (Iterator i = headers.values().iterator(); i.hasNext(); ) {
                HeaderAnnotation ha = (HeaderAnnotation)i.next();
                for (Iterator j = annots.values().iterator(); j.hasNext(); ) {
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

            //  Create an Other Annotations header in case there are
            //  some annotations with no headers.
            HeaderAnnotation other = new HeaderAnnotation(new Integer(-1), 
                                                          new String(""), 
                                                          "Other Annotations",
                                                          new Integer(999));
            boolean someOthers = false;
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

    public List getHeaders() {
        Collection headColl = headers.values();
        HeaderAnnotation[] headarray = 
            (HeaderAnnotation[])headColl.toArray(new HeaderAnnotation[0]);
        Arrays.sort(headarray);
        return Arrays.asList(headarray);
    }

    public List getAnnotations() {
        Collection annotColl = annots.values();
        SubAnnotation[] annotarray = 
            (SubAnnotation[])annotColl.toArray(new SubAnnotation[0]);
        Arrays.sort(annotarray);
        return Arrays.asList(annotarray);
    }

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

    ///////////////////////////
    // private instance methods
    ///////////////////////////

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

    private Map parseAllelePair(String ap) {
        HashMap hm = new HashMap();
        //  An allele pair is seperated by a forward slash character.
        String[] pair = ap.split("\\)/");
        hm.put("allele1", parseAllele(pair[0]) );
        if (pair.length > 1) {
            hm.put("allele2", parseAllele(pair[1]) );
        }
        else {
            hm.put("allele2", parseAllele(""));
        }
        return hm;
    }

    private Map parseAllele(String allele) {
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
        return hm;
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
