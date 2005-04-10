/*
* PhenotypicComparator.java
*
* Stolen from MJV on April 7, 2005
*/

package org.jax.mgi.shr.datafactory;


import java.util.*;


/**
*
* @author  mjv
*/
public class PhenotypicComparator implements Comparator {
    public static final int ALPHA = 1;
    public static final int CUSTOM = 2;

   /**
    * Holds the field on which the comparison is performed.
    */
   private int iType;
      /**
    * Value that will contain the information about the order of the sort: normal or reversal.
    */
   private boolean bReverse;

   /**
    * Constructor class for PhenotypicComparator.
    * <br>
    * Example:
    * <br>
    *  do not need one.
    * @param iType the field from which you want to sort
    * <br>
    */
   public PhenotypicComparator(int iType) {
       this(iType, false);
   }

   /**
    * Constructor class for PhenotypicComparator.
    * <br>
    * Example:
    * <br>
    *
    * @param iType the field from which you want to sort.
    * <br>
    * Possible values are:
    *
    * @param bReverse set this value to true, if you want to reverse the sorting results
    */
   public PhenotypicComparator(int iType, boolean bReverse) {
       this.iType = iType;
       this.bReverse = bReverse;
   }

   /**
    * Implementation of the compare method.
    */
   public int compare(Object pObj1, Object pObj2) {
       Phenotype b1 = (Phenotype)pObj1;
       Phenotype b2 = (Phenotype)pObj2;
       int iReturn = 0;
       switch(iType) {
           case ALPHA:
               List list1 = b1.getParsedCompound();
               List list2 = b2.getParsedCompound();
               if ((list1 == null || list1.size() ==0) && 
                   (list2 != null && list2.size() > 0))
                   iReturn = -1;
               else if ((list1 == null || list1.size() == 0) && 
                        (list2 == null || list2.size() == 0))
                   iReturn = 0;
               else if ((list2 == null || list2.size() ==0) && 
                   (list1 != null && list1.size() > 0))
                   iReturn = 1;
               else {         
                   HashMap ap = (HashMap)list1.get(0);
                   HashMap a = (HashMap)ap.get("allele1");
                   
                   String val1 = (String)a.get("symbol");
                       //    (String)((HashMap)((HashMap)list1.get(0)).
                       //     get("allele1")).get("symbol");
                   String val2 = 
                       (String)((HashMap)((HashMap)list2.get(0)).
                                get("allele1")).get("symbol");
                   iReturn = val1.toLowerCase().compareTo(val2.toLowerCase());
               }

               break;
           case CUSTOM:
               iReturn = b1.compareTo(b2);
               break;
           default:
               throw new IllegalArgumentException("Type passed for the field is not supported");
       }

       return bReverse ? (-1 * iReturn) : iReturn;
   }
}

