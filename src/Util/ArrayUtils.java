/*
 * Created on 15/Nov/2004
 *
 */
package Util;

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
