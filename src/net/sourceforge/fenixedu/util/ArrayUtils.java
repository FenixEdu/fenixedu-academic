/*
 * Created on 15/Nov/2004
 *
 */
package net.sourceforge.fenixedu.util;

/**
 * @author Fenix
 *
 */

public class ArrayUtils {
    
    public static void swap(Object[] array, int index1, int index2){
        
        Object temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

}
