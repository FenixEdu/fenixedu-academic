package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class MaximumNumberOfCreditsForEnrolmentPeriodExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final StudentCurricularPlan studentCurricularPlan = enrolmentContext.getStudentCurricularPlan();
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();

	final double maximum = MaximumNumberOfCreditsForEnrolmentPeriod.MAXIMUM_NUMBER_OF_CREDITS;
	final double accumulated = studentCurricularPlan.getAccumulatedEctsCredits(executionPeriod);
	double available = ((maximum - accumulated) > 0) ? maximum - accumulated : 0.0;
	        
	for (final DegreeModuleToEnrol degreeModuleToEnrol : enrolmentContext.getDegreeModuleToEnrol()) {
	    final double degreeModuleEctsCredits = degreeModuleToEnrol.getEctsCredits(executionPeriod);
	    if (degreeModuleEctsCredits > available) {
		return RuleResult.createFalse("curricularRules.ruleExecutors.MaximumNumberOfCreditsForEnrolmentPeriodExecutor", String.valueOf(maximum));
	    } else {
		available -= degreeModuleEctsCredits;
	    }
	}
	        
	return RuleResult.createTrue();
    }
    
    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return executeEnrolmentWithRules(curricularRule, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentWithNoRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }

}
