package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Date;

public class EnrolmentPeriodInCurricularCoursesSpecialSeason extends EnrolmentPeriodInCurricularCoursesSpecialSeason_Base {

    public static final Comparator<EnrolmentPeriodInCurricularCoursesSpecialSeason> COMPARATOR_BY_START = new Comparator<EnrolmentPeriodInCurricularCoursesSpecialSeason>() {

	@Override
	public int compare(EnrolmentPeriodInCurricularCoursesSpecialSeason o1, EnrolmentPeriodInCurricularCoursesSpecialSeason o2) {
	    return o1.getStartDateDateTime().compareTo(o2.getStartDateDateTime());
	}
	
    };

    public EnrolmentPeriodInCurricularCoursesSpecialSeason() {
	super();
    }

    public EnrolmentPeriodInCurricularCoursesSpecialSeason(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionSemester executionSemester, final Date startDate, final Date endDate) {
	super();
	init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

}
