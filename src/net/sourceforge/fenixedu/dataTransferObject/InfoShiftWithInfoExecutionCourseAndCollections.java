/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author João Mota
 *  
 */
public class InfoShiftWithInfoExecutionCourseAndCollections extends InfoShiftWithInfoExecutionCourse {

    public void copyFromDomain(IShift shift) {
        super.copyFromDomain(shift);
        if (shift != null) {
            setInfoLessons(copyILessons2InfoLessons(shift.getAssociatedLessons()));
            setInfoClasses(copyIClasses2InfoClasses(shift.getAssociatedClasses()));
        }
    }

    public static InfoShift newInfoFromDomain(IShift shift) {
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

                    return InfoLessonWithInfoRoomAndInfoExecutionCourse.newInfoFromDomain((ILesson) arg0);
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

                    return InfoClassWithInfoExecutionDegree.newInfoFromDomain((ISchoolClass) arg0);
                }

            });
        }
        return infoClasses;
    }

}