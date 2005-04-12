package org.jax.mgi.shr.datafactory;

public class MPEvidence {

    private Integer key;
    private Integer refkey;
    private String jnum;
    private String note;

    public MPEvidence(Integer key, Integer refkey, String jnum, String note) {
        this.key = key;
        this.refkey = refkey;
        this.jnum = jnum;
        this.note = note;
    }

    public final Integer getKey() {
        return this.key;
    }

    public final Integer getRefKey() {
        return this.refkey;
    }

    public final String getJNum() {
        return this.jnum;
    }
    
    public final boolean hasNote() {
        if (this.note != null) {
            return true;
        }
        else {
            return false;
        }
    }

    public final String getNote() {
        return this.note;
    }
}
