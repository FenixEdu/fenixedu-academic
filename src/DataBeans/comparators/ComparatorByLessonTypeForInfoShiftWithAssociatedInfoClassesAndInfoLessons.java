/*
 * Created on Mar 14, 2003
 *
 */
package DataBeans.comparators;

import java.util.Comparator;

import DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons;

/**
 * @author Luis Cruz e Sara Ribeiro
 *  
 */
public class ComparatorByLessonTypeForInfoShiftWithAssociatedInfoClassesAndInfoLessons implements
        Comparator {

    public int compare(Object obj1, Object obj2) {
        InfoShiftWithAssociatedInfoClassesAndInfoLessons infoShift1 = (InfoShiftWithAssociatedInfoClassesAndInfoLessons) obj1;
        InfoShiftWithAssociatedInfoClassesAndInfoLessons infoShift2 = (InfoShiftWithAssociatedInfoClassesAndInfoLessons) obj2;

        return (infoShift1.getInfoShift().getTipo().getTipo().intValue() - infoShift2.getInfoShift()
                .getTipo().getTipo().intValue());
    }

}