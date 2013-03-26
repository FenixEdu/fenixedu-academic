package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Date;

public class EnrolmentPeriodInCurricularCoursesFlunkedSeason extends EnrolmentPeriodInCurricularCoursesFlunkedSeason_Base {

    public static final Comparator<EnrolmentPeriodInCurricularCoursesFlunkedSeason> COMPARATOR_BY_START =
            new Comparator<EnrolmentPeriodInCurricularCoursesFlunkedSeason>() {

                @Override
                public int compare(EnrolmentPeriodInCurricularCoursesFlunkedSeason o1,
                        EnrolmentPeriodInCurricularCoursesFlunkedSeason o2) {
                    return o1.getStartDateDateTime().compareTo(o2.getStartDateDateTime());
                }

            };

    public EnrolmentPeriodInCurricularCoursesFlunkedSeason() {
        super();
    }

    public EnrolmentPeriodInCurricularCoursesFlunkedSeason(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionSemester executionSemester, final Date startDate, final Date endDate) {
        super();
        init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

}
