/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.IAula;
import Dominio.ITurma;
import Dominio.ITurno;

/**
 * @author João Mota
 *  
 */
public class InfoShiftWithInfoExecutionCourseAndCollections extends
        InfoShiftWithInfoExecutionCourse {

    public void copyFromDomain(ITurno shift) {
        super.copyFromDomain(shift);
        if (shift != null) {
            setInfoLessons(copyILessons2InfoLessons(shift
                    .getAssociatedLessons()));
            setInfoClasses(copyIClasses2InfoClasses(shift
                    .getAssociatedClasses()));
        }
    }

    public static InfoShift newInfoFromDomain(ITurno shift) {
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
            infoLessons = (List) CollectionUtils.collect(list,
                    new Transformer() {

                        public Object transform(Object arg0) {

                            return InfoLessonWithInfoRoomAndInfoExecutionCourse
                                    .newInfoFromDomain((IAula) arg0);
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
            infoClasses = (List) CollectionUtils.collect(list,
                    new Transformer() {

                        public Object transform(Object arg0) {

                            return InfoClassWithInfoExecutionDegree
                                    .newInfoFromDomain((ITurma) arg0);
                        }

                    });
        }
        return infoClasses;
    }

}