package org.fenixedu.academic.domain;

import java.util.Comparator;
import java.util.Date;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.joda.time.DateTime;

public class EnrolmentPeriodInCurricularCoursesCandidate extends EnrolmentPeriodInCurricularCoursesCandidate_Base {

    public static final Comparator<EnrolmentPeriodInCurricularCourses> COMPARATOR_BY_START =
            new Comparator<EnrolmentPeriodInCurricularCourses>() {

                @Override
                public int compare(EnrolmentPeriodInCurricularCourses o1, EnrolmentPeriodInCurricularCourses o2) {
                    return o1.getStartDateDateTime().compareTo(o2.getStartDateDateTime());
                }

            };

    public EnrolmentPeriodInCurricularCoursesCandidate(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionSemester executionSemester, final Date startDate, final Date endDate) {
        super();
        init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

    public EnrolmentPeriodInCurricularCoursesCandidate(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionSemester executionSemester, final DateTime startDate, final DateTime endDate) {

        super();

        checkParameters(degreeCurricularPlan, executionSemester);
        init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

    private void checkParameters(DegreeCurricularPlan degreeCurricularPlan, ExecutionSemester executionSemester) {

        String[] args = {};
        if (degreeCurricularPlan == null) {
            throw new DomainException("error.EnrolmentPeriodInCurricularCourses.invalid.degreeCurricularPlan", args);
        }
        String[] args1 = {};
        if (executionSemester == null) {
            throw new DomainException("error.EnrolmentPeriodInCurricularCourses.invalid.executionSemester", args1);
        }

        if (executionSemester.getEnrolmentPeriod(getClass(), degreeCurricularPlan) != null) {
            throw new DomainException("error.EnrolmentPeriodInCurricularCourses.dcp.already.has.enrolment.period.for.semester");
        }
    }
}
