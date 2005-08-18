package org.jax.mgi.shr.datafactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Disease {
    
    private Integer key;
    private String term;
    private String id;
    private HashMap phenotypes;

    public Disease (Integer key, String term, String id, HashMap phenotypes) {
        this.key = key;
        this.term = term;
        this.id = id;
        this.phenotypes = phenotypes;
    }

    public Disease (Integer key, String term, String id) {
        this.key = key;
        this.term = term;
        this.id = id;
        this.phenotypes = null;
    }

    public Integer getKey () {
        return this.key;
    }

    public String getTerm () {
        return this.term;
    }

    public String getId () {
        return this.id;
    }

    /**
     *  returns an alpha sorted list of phenotypes for this disease term
     */
    public ArrayList getPhenotypes () {
        ArrayList values = new ArrayList(phenotypes.values());
        Collections.sort(values, new PhenotypicComparator(PhenotypicComparator.ALPHA));
        return values;
    }

}// Disease
