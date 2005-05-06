package org.jax.mgi.shr.datafactory;
import java.util.ArrayList;
import java.util.HashMap;
import org.jax.mgi.shr.stringutil.Sprintf;

public class SNP {

	public String rsID = null;
	public String ssID = null;
	public String pID = null;
	public String providerName = null;
	public String functionalLocation = null;
	public String polymorphismClass = null;
	public String chromosome = null;
	public int coordinate = -1;
	public String orientation = null;

	protected static HashMap classDefs = null;
	protected static HashMap typeDefs = null;

	public static int getClassDefsSize() {
		if(classDefs == null)
			getClassDefs();
		return classDefs.size();
	}

	public static int getTypeDefsSize() {
		if(typeDefs == null)
			getTypeDefs();
		return typeDefs.size();
	}

	public static HashMap getTypeDefs() {
		if(classDefs == null) {
			classDefs = new HashMap();
			classDefs.put("SNP","Single Nucleotide Polymorphism");
			classDefs.put("MNP","Multiple Nucleotide Polymorphism");
            classDefs.put("DIP","Deletion/Insertion Polymorphism");
            classDefs.put("Mixed","Two or more of other classes");
		}
		return classDefs;
	}

	public static HashMap getClassDefs() {
		if(typeDefs == null) {
			typeDefs = new HashMap();
			typeDefs.put("Cn","Coding-Nonsynonymous");
			typeDefs.put("Cs,Cx","Coding-Synonymous");
            typeDefs.put("M","Mrna-utr");
            typeDefs.put("I","In intron, except first 2 and last 2 bases");
            typeDefs.put("S","Splice-site");
            typeDefs.put("L","not assigned to a gene");
            typeDefs.put("E","located within the Ensembl translated peptide region");
            typeDefs.put("F","NOT located within the Ensembl translated peptide region");

		}
		return typeDefs;
	}


}