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

    public static InfoShift copyFromDomain(ITurno shift) {
        InfoShift infoShift = InfoShiftWithInfoExecutionCourse
                .copyFromDomain(shift);
        if (infoShift != null) {
            infoShift.setInfoLessons(copyILessons2InfoLessons(shift
                    .getAssociatedLessons()));
            infoShift.setInfoClasses(copyIClasses2InfoClasses(shift
                    .getAssociatedClasses()));
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
                                    .copyFromDomain((IAula) arg0);
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
                                    .copyFromDomain((ITurma) arg0);
                        }

                    });
        }
        return infoClasses;
    }

}