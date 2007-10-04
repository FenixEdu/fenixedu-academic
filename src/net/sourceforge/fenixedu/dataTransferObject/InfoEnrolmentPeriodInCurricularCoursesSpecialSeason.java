package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.EnrolmentPeriod;


public class InfoEnrolmentPeriodInCurricularCoursesSpecialSeason extends InfoEnrolmentPeriod {

    public static InfoEnrolmentPeriodInCurricularCoursesSpecialSeason newInfoFromDomain(final EnrolmentPeriod enrolmentPeriod) {
        InfoEnrolmentPeriodInCurricularCoursesSpecialSeason infoExecutionPeriod = null;
        if (enrolmentPeriod != null) {
            infoExecutionPeriod = new InfoEnrolmentPeriodInCurricularCoursesSpecialSeason();
            infoExecutionPeriod.copyFromDomain(enrolmentPeriod);
        }
        return infoExecutionPeriod;
    }

}
