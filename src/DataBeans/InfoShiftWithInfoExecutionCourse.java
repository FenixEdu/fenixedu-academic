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

    public static InfoShift copyFromDomain(ITurno shift) {
        InfoShift infoShift = InfoShift.copyFromDomain(shift);
        if (infoShift!=null) {
        infoShift
                .setInfoDisciplinaExecucao(InfoExecutionCourseWithExecutionPeriod
                        .copyFromDomain(shift.getDisciplinaExecucao()));
        }
       return infoShift;
    }

}