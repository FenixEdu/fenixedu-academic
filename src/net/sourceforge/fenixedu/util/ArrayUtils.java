/*
 * Created on 15/Nov/2004
 *
 */
package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.iterators.EnumerationIterator;

/**
 * @author Fenix
 * 
 */

public class ArrayUtils {

    public static void swap(Object[] array, int index1, int index2) {

	Object temp = array[index1];
	array[index1] = array[index2];
	array[index2] = temp;
    }

    public static Object[] toArray(Enumeration enumeration) {
	return IteratorUtils.toArray(new EnumerationIterator(enumeration));
    }

    public static String[] toStringArray(Enumeration enumeration) {
	List<String> result = new ArrayList<String>();
	while (enumeration.hasMoreElements()) {
	    result.add((String) enumeration.nextElement());
	}
	return result.toArray(new String[] {});
    }

    public static String[] toStringArray(Enumeration enumeration, String... elementsToIgnore) {
	List<String> result = new ArrayList<String>();
	while (enumeration.hasMoreElements()) {
	    final String nextElement = (String) enumeration.nextElement();
	    if (!org.apache.commons.lang.ArrayUtils.contains(elementsToIgnore, nextElement)) {
		result.add(nextElement);
	    }
	}
	return result.toArray(new String[] {});
    }

    public static boolean haveArraysSameElements(String[] array1, String[] array2) {

	if (!org.apache.commons.lang.ArrayUtils.isSameLength(array1, array2)) {
	    return false;
	}

	for (String parameter : array1) {
	    if (!org.apache.commons.lang.ArrayUtils.contains(array2, parameter)) {
		return false;
	    }
	}

	for (String parameter : array2) {
	    if (!org.apache.commons.lang.ArrayUtils.contains(array1, parameter)) {
		return false;
	    }
	}

	return true;
    }

}
