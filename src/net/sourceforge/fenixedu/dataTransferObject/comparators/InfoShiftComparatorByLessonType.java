/*
 * Created on Mar 14, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.comparators;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;

/**
 * @author Luis Cruz e Sara Ribeiro
 *  
 */
public class InfoShiftComparatorByLessonType implements Comparator {

    public int compare(Object obj1, Object obj2) {
        InfoShift infoShift1 = (InfoShift) obj1;
        InfoShift infoShift2 = (InfoShift) obj2;
        return infoShift1.getTipo().compareTo(infoShift2.getTipo());
    }

}