package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.EnrolmentPeriod;


public class InfoEnrolmentPeriodInImprovementOfApprovedEnrolment extends InfoEnrolmentPeriod {

    public static InfoEnrolmentPeriodInImprovementOfApprovedEnrolment newInfoFromDomain(final EnrolmentPeriod enrolmentPeriod) {
        InfoEnrolmentPeriodInImprovementOfApprovedEnrolment infoExecutionPeriod = null;
        if (enrolmentPeriod != null) {
            infoExecutionPeriod = new InfoEnrolmentPeriodInImprovementOfApprovedEnrolment();
            infoExecutionPeriod.copyFromDomain(enrolmentPeriod);
        }
        return infoExecutionPeriod;
    }

}
