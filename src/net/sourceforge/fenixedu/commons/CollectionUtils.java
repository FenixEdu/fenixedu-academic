/*
 * Created on Oct 31, 2003
 *
 */
package net.sourceforge.fenixedu.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Luis Cruz
 *  
 */
public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {

    /**
     *  
     */
    public CollectionUtils() {
        super();
    }

    public static String[] toArrayOfString(Collection collection) {
        String[] strings = new String[collection.size()];

        int i = 0;
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            strings[i++] = (String) iterator.next();
        }

        return strings;
    }

    public static List toList(Object[] array) {
        List result = new ArrayList();
        for (int i = 0; i < array.length; i++) {
            result.add(array[i]);
        }
        return result;
    }
}