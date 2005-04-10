/*
* AnnotationComparator.java
*
* Stolen from MJV on April 7, 2005
*/

package org.jax.mgi.shr.datafactory;


import java.util.*;


/**
*
* @author  mjv
*/
public class AnnotationComparator implements Comparator {
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
    * Constructor class for AnnotationComparator.
    * <br>
    * Example:
    * <br>
    *  do not need one.
    * @param iType the field from which you want to sort
    * <br>
    */
   public AnnotationComparator(int iType) {
       this(iType, false);
   }

   /**
    * Constructor class for AnnotationComparator.
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
   public AnnotationComparator(int iType, boolean bReverse) {
       this.iType = iType;
       this.bReverse = bReverse;
   }

   /**
    * Implementation of the compare method.
    */
   public int compare(Object pObj1, Object pObj2) {
       Annotation b1 = (Annotation)pObj1;
       Annotation b2 = (Annotation)pObj2;
       int iReturn = 0;
       switch(iType) {
           case ALPHA:
               iReturn = b1.getTerm().toLowerCase().compareTo(b2.getTerm().toLowerCase());

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

