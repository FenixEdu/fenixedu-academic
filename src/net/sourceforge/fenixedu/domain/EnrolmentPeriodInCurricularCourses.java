/*
 * Created on 2004/08/24
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Date;

/**
 * @author Luis Cruz
 */
public class EnrolmentPeriodInCurricularCourses extends EnrolmentPeriodInCurricularCourses_Base {

    public static final Comparator<EnrolmentPeriodInCurricularCourses> COMPARATOR_BY_START = new Comparator<EnrolmentPeriodInCurricularCourses>() {

	@Override
	public int compare(EnrolmentPeriodInCurricularCourses o1, EnrolmentPeriodInCurricularCourses o2) {
	    return o1.getStartDateDateTime().compareTo(o2.getStartDateDateTime());
	}
	
    };

    public EnrolmentPeriodInCurricularCourses(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionSemester executionSemester, final Date startDate, final Date endDate) {
	super();
	init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

}