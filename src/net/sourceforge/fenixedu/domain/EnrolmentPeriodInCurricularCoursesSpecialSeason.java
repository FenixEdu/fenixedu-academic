package net.sourceforge.fenixedu.domain;

import java.util.Date;

public class EnrolmentPeriodInCurricularCoursesSpecialSeason extends
	EnrolmentPeriodInCurricularCoursesSpecialSeason_Base {

    public EnrolmentPeriodInCurricularCoursesSpecialSeason() {
	super();
    }

    public EnrolmentPeriodInCurricularCoursesSpecialSeason(
	    final DegreeCurricularPlan degreeCurricularPlan, final ExecutionPeriod executionPeriod,
	    final Date startDate, final Date endDate) {
	super();
	init(degreeCurricularPlan, executionPeriod, startDate, endDate);
    }

}
