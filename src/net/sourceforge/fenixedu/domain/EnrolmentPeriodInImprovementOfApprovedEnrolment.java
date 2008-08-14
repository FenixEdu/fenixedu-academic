package net.sourceforge.fenixedu.domain;

import java.util.Date;

public class EnrolmentPeriodInImprovementOfApprovedEnrolment extends EnrolmentPeriodInImprovementOfApprovedEnrolment_Base {

    public EnrolmentPeriodInImprovementOfApprovedEnrolment() {
	super();
    }

    public EnrolmentPeriodInImprovementOfApprovedEnrolment(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionSemester executionSemester, final Date startDate, final Date endDate) {
	super();
	init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

}
