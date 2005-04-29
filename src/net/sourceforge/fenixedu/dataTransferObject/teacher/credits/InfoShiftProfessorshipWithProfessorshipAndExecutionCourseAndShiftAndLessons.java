/*
 * Created on 14/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithInfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;

/**
 * @author jpvl
 */
public class InfoShiftProfessorshipWithProfessorshipAndExecutionCourseAndShiftAndLessons extends
        InfoShiftProfessorship {

    public void copyFromDomain(IShiftProfessorship shifProfessorship) {
        super.copyFromDomain(shifProfessorship);
        if (shifProfessorship != null) {
            final IProfessorship professorship = shifProfessorship.getProfessorship();
            setInfoProfessorship(InfoProfessorshipWithInfoExecutionCourse
                    .newInfoFromDomain(professorship));

            final IShift shift = shifProfessorship.getShift();
            setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(shift));
        }
    }

    public static InfoShiftProfessorshipWithProfessorshipAndExecutionCourseAndShiftAndLessons newInfoFromDomain(
            IShiftProfessorship shifProfessorship) {
        InfoShiftProfessorshipWithProfessorshipAndExecutionCourseAndShiftAndLessons infoShiftProfessorship = null;
        if (shifProfessorship != null) {
            infoShiftProfessorship = new InfoShiftProfessorshipWithProfessorshipAndExecutionCourseAndShiftAndLessons();
            infoShiftProfessorship.copyFromDomain(shifProfessorship);
        }
        return infoShiftProfessorship;
    }

}