/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Shift;

/**
 * @author João Mota
 *  
 */
public class InfoShiftWithInfoExecutionCourse extends InfoShift {

    public void copyFromDomain(Shift shift) {
        super.copyFromDomain(shift);
        if (shift != null) {
            setInfoDisciplinaExecucao(InfoExecutionCourseWithExecutionPeriod.newInfoFromDomain(shift
                    .getDisciplinaExecucao()));
        }
    }

    public static InfoShift newInfoFromDomain(Shift shift) {
        InfoShiftWithInfoExecutionCourse infoShift = null;
        if (shift != null) {
            infoShift = new InfoShiftWithInfoExecutionCourse();
            infoShift.copyFromDomain(shift);
        }
        return infoShift;
    }

}