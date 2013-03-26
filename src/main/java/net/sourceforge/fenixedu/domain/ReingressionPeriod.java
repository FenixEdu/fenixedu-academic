package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ReingressionPeriod extends ReingressionPeriod_Base {

    public ReingressionPeriod(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionSemester executionSemester,
            final Date startDate, final Date endDate) {
        super();
        checkForExistingPeriod(degreeCurricularPlan, executionSemester);
        init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

    private void checkForExistingPeriod(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionSemester executionSemester) {
        if (degreeCurricularPlan.getReingressionPeriod(executionSemester) != null) {
            throw new DomainException("error.ReingressionPeriod.period.already.exists.for.dcp.and.semester");
        }
    }

}
