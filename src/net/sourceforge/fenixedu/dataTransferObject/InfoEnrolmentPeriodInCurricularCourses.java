package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.EnrolmentPeriod;

public class InfoEnrolmentPeriodInCurricularCourses extends InfoEnrolmentPeriod {

    public static InfoEnrolmentPeriodInCurricularCourses newInfoFromDomain(final EnrolmentPeriod enrolmentPeriod) {
	InfoEnrolmentPeriodInCurricularCourses infoExecutionPeriod = null;
	if (enrolmentPeriod != null) {
	    infoExecutionPeriod = new InfoEnrolmentPeriodInCurricularCourses();
	    infoExecutionPeriod.copyFromDomain(enrolmentPeriod);
	}
	return infoExecutionPeriod;
    }

}
