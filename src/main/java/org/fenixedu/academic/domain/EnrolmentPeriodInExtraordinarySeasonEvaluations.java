package org.fenixedu.academic.domain;

import java.util.Comparator;
import java.util.Date;

public class EnrolmentPeriodInExtraordinarySeasonEvaluations extends EnrolmentPeriodInExtraordinarySeasonEvaluations_Base {


    public static final Comparator<EnrolmentPeriodInExtraordinarySeasonEvaluations> COMPARATOR_BY_START =
            Comparator.comparing(EnrolmentPeriod_Base::getStartDateDateTime);

    public EnrolmentPeriodInExtraordinarySeasonEvaluations() {
        super();
    }

    public EnrolmentPeriodInExtraordinarySeasonEvaluations(final DegreeCurricularPlan degreeCurricularPlan,
                                                     final ExecutionSemester executionSemester, final Date startDate, final Date endDate) {
        super();
        init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

    @Override
    public boolean isForCurricularCourses() {
        return true;
    }
    
}
