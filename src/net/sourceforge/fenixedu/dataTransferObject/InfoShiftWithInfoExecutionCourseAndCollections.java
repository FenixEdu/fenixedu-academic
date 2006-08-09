/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author João Mota
 *  
 */
public class InfoShiftWithInfoExecutionCourseAndCollections extends InfoShiftWithInfoExecutionCourse {

    public void copyFromDomain(Shift shift) {
        super.copyFromDomain(shift);
        if (shift != null) {
            setInfoLessons(copyILessons2InfoLessons(shift.getAssociatedLessons()));
            setInfoClasses(copyIClasses2InfoClasses(shift.getAssociatedClasses()));
        }
    }

    public static InfoShift newInfoFromDomain(Shift shift) {
        InfoShiftWithInfoExecutionCourseAndCollections infoShift = null;
        if (shift != null) {
            infoShift = new InfoShiftWithInfoExecutionCourseAndCollections();
            infoShift.copyFromDomain(shift);
        }
        return infoShift;
    }

    /**
     * @param list
     * @return
     */
    private static List copyILessons2InfoLessons(List list) {
        List infoLessons = null;
        if (list != null) {
            infoLessons = (List) CollectionUtils.collect(list, new Transformer() {

                public Object transform(Object arg0) {

                    return InfoLesson.newInfoFromDomain((Lesson) arg0);
                }

            });
        }
        return infoLessons;
    }

    /**
     * @param list
     * @return
     */
    private static List copyIClasses2InfoClasses(List list) {
        List infoClasses = null;
        if (list != null) {
            infoClasses = (List) CollectionUtils.collect(list, new Transformer() {

                public Object transform(Object arg0) {

                    return InfoClassWithInfoExecutionDegree.newInfoFromDomain((SchoolClass) arg0);
                }

            });
        }
        return infoClasses;
    }

}