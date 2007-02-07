package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class MaximumNumberOfCreditsForEnrolmentPeriodExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	final StudentCurricularPlan studentCurricularPlan = enrolmentContext.getStudentCurricularPlan();

	final double maxECTS = MaximumNumberOfCreditsForEnrolmentPeriod.MAXIMUM_NUMBER_OF_CREDITS;
	final double ects = studentCurricularPlan.getEctsCredits(enrolmentContext.getExecutionPeriod());
	double availableEctsCredits = ((maxECTS - ects) > 0) ? maxECTS - ects : 0.0;
	        
	for (final DegreeModuleToEnrol degreeModuleToEnrol : enrolmentContext.getDegreeModuleToEnrol()) {
	    final double degreeModuleEctsCredits = degreeModuleToEnrol.getContext().getChildDegreeModule().getEctsCredits();
	    if (degreeModuleEctsCredits > availableEctsCredits) {
		return RuleResult.createFalse();
	    } else {
		availableEctsCredits -= degreeModuleEctsCredits;
	    }
	}
	        
	return RuleResult.createTrue();
    }
    
    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return executeEnrolmentWithRules(curricularRule, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentWithNoRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }

}
