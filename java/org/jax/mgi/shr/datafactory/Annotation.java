package org.jax.mgi.shr.datafactory;

import java.util.TreeSet;
import java.util.Iterator;
import java.util.ArrayList;
import org.jax.mgi.shr.cache.ExpiringObjectCache;

public abstract class Annotation implements Comparable {

    protected Integer key;
    protected String id;
    protected String term;
    protected Integer orderVal;
    protected TreeSet descendents;

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getKey() {
        return this.key;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTerm() {
        return this.term;
    }
    
    public void setOrderVal(Integer val) {
        this.orderVal = val;
    }

    public Integer getOrderVal() {
        return this.orderVal;
    }

    public void addDescendent(Annotation a) {
        if (this.descendents == null) {
            this.descendents = new TreeSet();
        }

        ExpiringObjectCache eoc = ExpiringObjectCache.getSharedCache();
        MPClosureDAG cdag = 
            (MPClosureDAG)eoc.get(PhenotypeFactory.CLOSURE_CACHE_KEY);

        boolean added = false;
        for (Iterator i = this.descendents.iterator(); i.hasNext(); ) {
            Annotation fromList = (Annotation)i.next();
            if (cdag.isDescendent(fromList.getKey(), a.getKey())) {
                fromList.addDescendent(a);
                added = true;
                break;
            }
            else if (cdag.isDescendent(a.getKey(), fromList.getKey())) {
                this.descendents.remove(fromList);
                a.addDescendent(fromList);
                this.descendents.add(a);
                added = true;
                break;
            }
        }
        if (! added ) {
            this.descendents.add(a);
        }
    }

    public TreeSet getDescendents() {
        return this.descendents;
    }

    public boolean hasDescendents() {
        if (this.descendents == null ||
            this.descendents.size() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public abstract int compareTo(Object o);  

    public abstract String toString();

}
