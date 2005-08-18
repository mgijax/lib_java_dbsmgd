package org.jax.mgi.shr.datafactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MouseModelCategory.java
 *
 *
 * Created: Fri Aug 12 14:37:43 2005
 *
 * @author <a href="mailto:dow@olorin">David O. Walton</a>
 */

public class MouseModelCategory {

    private String header;
    private String footnote;
    private ArrayList diseases;

    public MouseModelCategory (String header, String footnote){

        this.header = header;
        this.footnote = footnote;
        this.diseases = new ArrayList();
        
    }

    public void addDisease(Integer termKey, String term, String termId, 
                           HashMap phenotypes)
    {
        Disease d = new Disease(termKey, term, termId, phenotypes);
        diseases.add(d);
    }

    public void addDisease(Disease d) {
        diseases.add(d);
    }

    public String getHeader() {
        return this.header;
    }

    public String getFootnote () {
        return this.footnote;
    }

    public ArrayList getDiseases () {
        return diseases;
    }
}// MouseModelCategory

