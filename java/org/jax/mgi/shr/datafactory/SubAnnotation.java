package org.jax.mgi.shr.datafactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class SubAnnotation extends Annotation {

    private ArrayList evidence;
    private boolean used = false;

    public SubAnnotation (Integer key, String id, String term, 
                          Integer orderval) {
        setKey(key);
        setId(id);
        setTerm(term);
        setOrderVal(orderval);
        this.evidence = new ArrayList();
    }

    public void addEvidence(Integer evidenceKey, Integer refKey, String jNum, 
                            String note) {
        MPEvidence e = new MPEvidence(evidenceKey, refKey, jNum, note);
        evidence.add(e);
    }

    public final boolean hasEvidence() {
        return this.evidence.size() > 0 ? true : false;
    }

    public final ArrayList getEvidence() {
        return this.evidence;
    }

    public final void use() {
        this.used = true;
    }

    public final boolean used() {
        return this.used;
    }

    public int compareTo(Object o) 
        throws ClassCastException
    {
        SubAnnotation a = (SubAnnotation)o;
        // ordered numerically on orderVal
        return this.orderVal.compareTo(a.getOrderVal());
        //  Alphabetical on Term.
        //return this.term.compareTo(a.getTerm());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<a href=\"ANNOTURL").append(getId()).append("\">");
        sb.append(getTerm()).append("</a>");
        HashSet refsUsed = new HashSet();
        StringBuffer refs = new StringBuffer();
        StringBuffer notes = new StringBuffer();
        if (hasEvidence()) {
            boolean firstRef = true;
            boolean onlyOne = false;
            if (evidence.size() == 1) {
                onlyOne = true;
            }
            for (Iterator i = evidence.iterator(); i.hasNext(); ) {
                MPEvidence ev = (MPEvidence) i.next();
                //  We don't want to display the same ref next to the
                //  annotation term more than once.
                boolean refAlreadyGotOne = false;
                if (! refsUsed.contains(ev.getRefKey())) {
                    refsUsed.add(ev.getRefKey());
                }
                else {
                    refAlreadyGotOne = true;
                }

                //  Don't need a comma on the first reference
                if (! firstRef && ! refAlreadyGotOne) {
                    refs.append(", ");
                }
                else {
                    firstRef = false;
                }
                StringBuffer curRef = new StringBuffer();
                curRef.append("<a href=\"REFURL").append(ev.getRefKey()).append("\"><i>");
                curRef.append(ev.getJNum()).append("</i></a>");
                if (! refAlreadyGotOne) {
                    refs.append(curRef);
                }

                if (ev.hasNote()) {
                    notes.append("<li>");
                    notes.append(ev.getNote());
                    if (! onlyOne) {
                        notes.append(" (").append(curRef).append(")");
                    }
                    notes.append("</li>");
                }
            }
            sb.append(" (").append(refs).append(")");
        }
        if (! notes.equals("")) {
            sb.append("<ul type=circle><font class=\"small\">").append(notes).append("</font></ul>");
        }

        if (hasDescendents()) {
            sb.append("<dl>");
            for (Iterator i = descendents.iterator(); i.hasNext();) {
                sb.append("<dt><dd>");
                sb.append((Annotation)i.next());
            }
            sb.append("</dl>");
        }

        return sb.toString();
    }



}
