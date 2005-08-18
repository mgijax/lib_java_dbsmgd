package org.jax.mgi.shr.datafactory;

public class DiseasePhenotype extends Phenotype {
    
    private String footnote = null;

    public DiseasePhenotype (Integer genotype,
                             Integer orderVal,
                             String compound,
                             String background) {
        super(genotype, orderVal, compound, background);
        this.footnote = null;
    }

    public DiseasePhenotype (Integer genotype,
                             Integer orderVal,
                             String compound,
                             String background,
                             String footnote) {
        this(genotype, orderVal, compound, background);
        this.footnote = footnote;
    }

    public String getFootnote () {
        return this.footnote;
    }
}// DiseasePhenotype
        
