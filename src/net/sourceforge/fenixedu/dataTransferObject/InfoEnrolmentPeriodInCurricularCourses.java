package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IEnrolmentPeriod;


public class InfoEnrolmentPeriodInCurricularCourses extends InfoEnrolmentPeriod {

    public static InfoEnrolmentPeriodInCurricularCourses newInfoFromDomain(final IEnrolmentPeriod enrolmentPeriod) {
        InfoEnrolmentPeriodInCurricularCourses infoExecutionPeriod = null;
        if (enrolmentPeriod != null) {
            infoExecutionPeriod = new InfoEnrolmentPeriodInCurricularCourses();
            infoExecutionPeriod.copyFromDomain(enrolmentPeriod);
        }
        return infoExecutionPeriod;
    }

}
