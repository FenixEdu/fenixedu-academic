package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class RegistrationPeriodInDegreeCurricularPlan extends
	RegistrationPeriodInDegreeCurricularPlan_Base {

    private RegistrationPeriodInDegreeCurricularPlan() {
	super();
    }

    public RegistrationPeriodInDegreeCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan,
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
	if (degreeCurricularPlan.hasRegistrationPeriodFor(executionYear)) {
	    throw new DomainException(
		    "error.RegistrationPeriodInDegreeCurricularPlan.degree.curricular.plan.already.contains.registration.period.for.execution.year");
	}

    }
    
    public ExecutionYear getExecutionYear() {
	return getExecutionPeriod().getExecutionYear();
    }

}
