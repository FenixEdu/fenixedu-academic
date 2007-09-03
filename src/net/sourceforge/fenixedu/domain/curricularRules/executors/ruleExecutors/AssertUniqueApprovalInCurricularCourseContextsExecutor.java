package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

public class AssertUniqueApprovalInCurricularCourseContextsExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	final CurricularCourse curricularCourse = (CurricularCourse) curricularRule.getDegreeModuleToApplyRule();
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();

	if (!curricularCourse.hasAnyActiveContext(enrolmentContext.getExecutionPeriod())) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	if (isApproved(enrolmentContext, curricularCourse, executionPeriod.getPreviousExecutionPeriod())) {
	    if (sourceDegreeModuleToEvaluate.isEnroled()) {
		return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
			"curricularRules.ruleExecutors.AssertUniqueApprovalInCurricularCourseContextsExecutor.already.approved",
			curricularCourse.getName());
	    } else {
		return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
			"curricularRules.ruleExecutors.AssertUniqueApprovalInCurricularCourseContextsExecutor.already.approved",
			curricularCourse.getName());
	    }

	} else {
	    return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	}
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	final CurricularCourse curricularCourse = (CurricularCourse) curricularRule.getDegreeModuleToApplyRule();
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();

	if (!curricularCourse.hasAnyActiveContext(enrolmentContext.getExecutionPeriod())) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	if (isApproved(enrolmentContext, curricularCourse, executionPeriod.getPreviousExecutionPeriod())) {
	    if (sourceDegreeModuleToEvaluate.isEnroled()) {
		return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
			"curricularRules.ruleExecutors.AssertUniqueApprovalInCurricularCourseContextsExecutor.already.approved",
			curricularCourse.getName());
	    } else {
		return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
			"curricularRules.ruleExecutors.AssertUniqueApprovalInCurricularCourseContextsExecutor.already.approved",
			curricularCourse.getName());
	    }

	} else if (hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, executionPeriod.getPreviousExecutionPeriod())) {
	    return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule());

	} else {
	    return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	}
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

}
