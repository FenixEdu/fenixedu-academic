/*
 * Created on 18/Jun/2004
 *
 */
package DataBeans;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.ILesson;
import Dominio.IShift;

/**
 * @author Tânia Pousão 18/Jun/2004
 */
public class InfoShiftWithInfoExecutionCourseAndInfoLessons extends InfoShiftWithInfoExecutionCourse {
    public void copyFromDomain(IShift shift) {
        super.copyFromDomain(shift);
        if (shift != null) {
            setInfoLessons(copyILessons2InfoLessons(shift.getAssociatedLessons()));
        }
    }

    public static InfoShift newInfoFromDomain(IShift shift) {
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

                    return InfoLessonWithInfoRoom.newInfoFromDomain((ILesson) arg0);
                }

            });
        }
        return infoLessons;
    }
}