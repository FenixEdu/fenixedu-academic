/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.IShift;

/**
 * @author João Mota
 *  
 */
public class InfoShiftWithInfoExecutionCourse extends InfoShift {

    public void copyFromDomain(IShift shift) {
        super.copyFromDomain(shift);
        if (shift != null) {
            setInfoDisciplinaExecucao(InfoExecutionCourseWithExecutionPeriod.newInfoFromDomain(shift
                    .getDisciplinaExecucao()));
        }
    }

    public static InfoShift newInfoFromDomain(IShift shift) {
        InfoShiftWithInfoExecutionCourse infoShift = null;
        if (shift != null) {
            infoShift = new InfoShiftWithInfoExecutionCourse();
            infoShift.copyFromDomain(shift);
        }
        return infoShift;
    }

}