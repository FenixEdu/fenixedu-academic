package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.EnrolmentPeriod;

public class InfoEnrolmentPeriodInSchoolClasses extends InfoEnrolmentPeriod {

    public static InfoEnrolmentPeriodInSchoolClasses newInfoFromDomain(final EnrolmentPeriod enrolmentPeriod) {
	InfoEnrolmentPeriodInSchoolClasses infoExecutionPeriod = null;
	if (enrolmentPeriod != null) {
	    infoExecutionPeriod = new InfoEnrolmentPeriodInSchoolClasses();
	    infoExecutionPeriod.copyFromDomain(enrolmentPeriod);
	}
	return infoExecutionPeriod;
    }

}
