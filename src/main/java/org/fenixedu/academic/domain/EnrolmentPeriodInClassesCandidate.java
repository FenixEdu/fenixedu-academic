package org.fenixedu.academic.domain;

import java.util.Date;

public class EnrolmentPeriodInClassesCandidate extends EnrolmentPeriodInClassesCandidate_Base {
    public EnrolmentPeriodInClassesCandidate(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionSemester executionSemester, final Date startDate, final Date endDate) {
        super();
        init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }
}
