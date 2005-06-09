package org.jax.mgi.shr.datafactory;

import java.util.HashMap;
import java.util.HashSet;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.ResultsNavigator;
import org.jax.mgi.shr.dbutils.RowReference;
import org.jax.mgi.shr.dbutils.DBException;

public class MPClosureDAG {

    private HashMap dag;

    public MPClosureDAG (SQLDataManager sqlDM) throws DBException
    {

        ResultsNavigator nav = null;// set of query results
        RowReference rr = null;		// one row in 'nav'

        nav = sqlDM.executeQuery (GET_CLOSURE);
        dag = new HashMap();

        while (nav.next())	{	// step through all returned rows
            rr = (RowReference) nav.getCurrent();
            Integer ancestor = rr.getInt(1);
            Integer descendent = rr.getInt(2);
            HashSet descendents = (HashSet) dag.get(ancestor);
            if (descendents == null) {
                descendents = new HashSet();
            }
            descendents.add(descendent);
            dag.put(ancestor, descendents);
        }
        nav.close();
        
    }

    public boolean isDescendent(int ancestor, int descendent) {
        HashSet descendents = (HashSet)dag.get(new Integer(ancestor));

        if (descendents == null) {
            return false;
        }
        else {
            if (descendents.contains(new Integer(descendent))) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public boolean isDescendent(Integer ancestor, Integer descendent) {
        HashSet descendents = (HashSet)dag.get(ancestor);

        if (descendents == null) {
            return false;
        }
        else {
            if (descendents.contains(descendent)) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    //////////////////
    // class variables
    //////////////////

    /* --------------------------------------------------------------------
    ** class variables -- used to hold standard SQL statements, so we don't
    ** need to re-join the Strings in each thread of the servlet.  For each
    ** one, we note the pieces that need to be filled in using Sprintf.
    ** --------------------------------------------------------------------
    */

    // find the genotypes, their allelic compositions and background strains.
    // fill in: allele key (int)
    private static final String GET_CLOSURE =
		"select dc._AncestorObject_key as ancestor, "
        + " dc._DescendentObject_key as descendent "
        + " from VOC_Term vt, DAG_Closure dc "
        + " where vt._Vocab_key = 5  "
        + " and vt._Term_key = dc._AncestorObject_key "
        + " order by dc._AncestorObject_key";

}
