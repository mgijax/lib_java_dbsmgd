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

    /**
     *  This method adds the passed annotation as a descendent of this
     *  annotation or one of it's existing descendents.  Where appropriate, 
     *  this annotation has a descendent which is a descendent for the
     *  annotation passed, it will handle this appropriately as well.
     */
    public void addDescendent(Annotation a) {
        if (this.descendents == null) {
            this.descendents = new TreeSet();
        }

        //  This object is a static class attribute which is used to 
        //  determine ancestor/descendent releationships.
        ExpiringObjectCache eoc = ExpiringObjectCache.getSharedCache();
        MPClosureDAG cdag = 
            (MPClosureDAG)eoc.get(PhenotypeFactory.CLOSURE_CACHE_KEY);

        boolean added = false;
        for (Iterator i = this.descendents.iterator(); i.hasNext(); ) {
            Annotation fromList = (Annotation)i.next();
            if (cdag.isDescendent(fromList.getKey(), a.getKey())) {
                //  If the passed annotation is actually a descendent of
                //  one of this annotations direct descendents, add it under
                //  the descendent instead.
                fromList.addDescendent(a);
                added = true;
                break;
            }
            else if (cdag.isDescendent(a.getKey(), fromList.getKey())) {
                //  The passed annotation may have descendents that
                //  are currently listed as descendents of this annotation.
                //  Move them under the passed annotation.
                i.remove();
                a.addDescendent(fromList);
            }
        }

        //  If the passed annotation has not been added to any of this
        //  annotations descendents, then it must be a direct descendent of
        //  this.
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
