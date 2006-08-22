/*
 * Created on 14/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;

/**
 * @author jpvl
 */
public class InfoShiftProfessorshipWithProfessorshipAndExecutionCourseAndShiftAndLessons extends
        InfoShiftProfessorship {

    public void copyFromDomain(ShiftProfessorship shifProfessorship) {
        super.copyFromDomain(shifProfessorship);
        if (shifProfessorship != null) {
            final Professorship professorship = shifProfessorship.getProfessorship();
            setInfoProfessorship(InfoProfessorship.newInfoFromDomain(professorship));

            final Shift shift = shifProfessorship.getShift();
            setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(shift));
        }
    }

    public static InfoShiftProfessorshipWithProfessorshipAndExecutionCourseAndShiftAndLessons newInfoFromDomain(
            ShiftProfessorship shifProfessorship) {
        InfoShiftProfessorshipWithProfessorshipAndExecutionCourseAndShiftAndLessons infoShiftProfessorship = null;
        if (shifProfessorship != null) {
            infoShiftProfessorship = new InfoShiftProfessorshipWithProfessorshipAndExecutionCourseAndShiftAndLessons();
            infoShiftProfessorship.copyFromDomain(shifProfessorship);
        }
        return infoShiftProfessorship;
    }

}