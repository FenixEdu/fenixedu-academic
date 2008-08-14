package net.sourceforge.fenixedu.domain;

import java.util.Date;

public class EnrolmentPeriodInCurricularCoursesSpecialSeason extends EnrolmentPeriodInCurricularCoursesSpecialSeason_Base {

    public EnrolmentPeriodInCurricularCoursesSpecialSeason() {
	super();
    }

    public EnrolmentPeriodInCurricularCoursesSpecialSeason(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionSemester executionSemester, final Date startDate, final Date endDate) {
	super();
	init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

}
