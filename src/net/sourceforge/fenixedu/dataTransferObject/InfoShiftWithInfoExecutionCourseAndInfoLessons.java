/*
 * Created on 18/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Tânia Pousão 18/Jun/2004
 */
public class InfoShiftWithInfoExecutionCourseAndInfoLessons extends InfoShiftWithInfoExecutionCourse {
    public void copyFromDomain(Shift shift) {
        super.copyFromDomain(shift);
        if (shift != null) {
            setInfoLessons(copyILessons2InfoLessons(shift.getAssociatedLessons()));
        }
    }

    public static InfoShift newInfoFromDomain(Shift shift) {
        InfoShiftWithInfoExecutionCourseAndInfoLessons infoShift = null;
        if (shift != null) {
            infoShift = new InfoShiftWithInfoExecutionCourseAndInfoLessons();
            infoShift.copyFromDomain(shift);
        }
        return infoShift;
    }

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
}