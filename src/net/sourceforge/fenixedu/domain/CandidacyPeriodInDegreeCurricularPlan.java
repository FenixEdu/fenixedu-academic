package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class CandidacyPeriodInDegreeCurricularPlan extends CandidacyPeriodInDegreeCurricularPlan_Base {

    public CandidacyPeriodInDegreeCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionYear executionYear, final DateTime startDate, final DateTime endDate) {
	super();

	init(degreeCurricularPlan, executionYear, startDate, endDate);
    }

    protected void init(DegreeCurricularPlan degreeCurricularPlan, ExecutionYear executionYear,
	    DateTime startDate, DateTime endDate) {
	checkRuleToCreate(degreeCurricularPlan, executionYear);
	super.init(degreeCurricularPlan, executionYear.getFirstExecutionPeriod(), startDate, endDate);

    }

    private void checkRuleToCreate(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionYear executionYear) {
	if (degreeCurricularPlan.hasCandidacyPeriodFor(executionYear)) {
	    throw new DomainException(
		    "error.CandidacyPeriodInDegreeCurricularPlan.degree.curricular.plan.already.contains.candidacy.period.for.execution.year");
	}

    }

    public ExecutionYear getExecutionYear() {
	return getExecutionPeriod().getExecutionYear();
    }

}
