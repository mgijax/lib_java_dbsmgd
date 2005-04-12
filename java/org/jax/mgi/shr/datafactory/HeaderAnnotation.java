package org.jax.mgi.shr.datafactory;


import java.util.Iterator;

public class HeaderAnnotation extends Annotation {

    public HeaderAnnotation (Integer key, String id, String term,
                             Integer orderval) {
        setKey(key);
        setId(id);
        setTerm(term);
        setOrderVal(orderval);
    }

    public int compareTo(Object o) 
        throws ClassCastException
    {
        HeaderAnnotation a = (HeaderAnnotation)o;
        // ordered numerically on orderVal
        return this.orderVal.compareTo(a.getOrderVal());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<dl>");
        sb.append("<font class=\"label\">");
        sb.append(getTerm());
        sb.append("</font>");
        if (hasDescendents()) {
            for (Iterator i = descendents.iterator(); i.hasNext();) {
                sb.append("<dt><dd>");
                sb.append((Annotation)i.next());
            }
        }
        sb.append("</dl>");
        return sb.toString();
    }


}
