/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.ITurno;

/**
 * @author João Mota
 *  
 */
public class InfoShiftWithInfoExecutionCourse extends InfoShift {

    public void copyFromDomain(ITurno shift) {
        super.copyFromDomain(shift);
        if (shift != null) {
            setInfoDisciplinaExecucao(InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(shift.getDisciplinaExecucao()));
        }
    }

    public static InfoShift newInfoFromDomain(ITurno shift) {
        InfoShiftWithInfoExecutionCourse infoShift = null;
        if (shift != null) {
            infoShift = new InfoShiftWithInfoExecutionCourse();
            infoShift.copyFromDomain(shift);
        }
        return infoShift;
    }

}