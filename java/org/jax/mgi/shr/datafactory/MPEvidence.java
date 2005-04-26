package org.jax.mgi.shr.datafactory;

public class MPEvidence {

    private Integer key;
    private Integer refkey;
    private String jnum;
    private String note;
    private String noteType;

    public MPEvidence(Integer key, Integer refkey, String jnum, String note,
                      String noteType) {
        this.key = key;
        this.refkey = refkey;
        this.jnum = jnum;
        this.note = note;
        this.noteType = noteType;
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

    public final String getNoteType() {
        return this.noteType;
    }
}
