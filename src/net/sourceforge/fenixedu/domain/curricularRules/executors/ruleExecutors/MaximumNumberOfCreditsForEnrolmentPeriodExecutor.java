package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

public class MaximumNumberOfCreditsForEnrolmentPeriodExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final ExecutionSemester executionSemester = enrolmentContext.getExecutionPeriod();

	double accumulated = 0d;
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
	    accumulated += degreeModuleToEvaluate.getAccumulatedEctsCredits(executionSemester);
	}

	final double maxEcts = MaximumNumberOfCreditsForEnrolmentPeriod.getMaximumNumberOfCredits(enrolmentContext
		.getStudentCurricularPlan(), executionSemester.getExecutionYear());

	if (accumulated > maxEcts) {
	    if (sourceDegreeModuleToEvaluate.isEnroled()) {
		return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
			"curricularRules.ruleExecutors.MaximumNumberOfCreditsForEnrolmentPeriodExecutor",
			String.valueOf(maxEcts), String.valueOf(accumulated));

	    } else {
		return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
			"curricularRules.ruleExecutors.MaximumNumberOfCreditsForEnrolmentPeriodExecutor",
			String.valueOf(maxEcts), String.valueOf(accumulated));
	    }
	}

	return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
	    EnrolmentContext enrolmentContext) {
	return true;
    }

}
