package org.fenixedu.academic.domain;

import java.util.Date;

public class EnrolmentPeriodInClassesMobility extends EnrolmentPeriodInClassesMobility_Base {

    public EnrolmentPeriodInClassesMobility(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionSemester executionSemester, final Date startDate, final Date endDate) {
        super();
        init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

    @Override
    public boolean isForClasses() {
        return true;
    }

}
