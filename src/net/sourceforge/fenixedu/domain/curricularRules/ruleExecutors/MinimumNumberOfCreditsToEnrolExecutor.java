package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.MinimumNumberOfCreditsToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class MinimumNumberOfCreditsToEnrolExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final MinimumNumberOfCreditsToEnrol rule = (MinimumNumberOfCreditsToEnrol) curricularRule;
	if (!canApplyRule(enrolmentContext, curricularRule)) {
	    return RuleResult.createNA();
	}

	final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().getRoot();
	final Double totalEctsCredits = curriculumGroup.getAprovedEctsCredits();

	if (rule.allowCredits(totalEctsCredits)) {
	    return RuleResult.createTrue();
	}

	if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
	    return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE);
	} else {
	    return createFalseRuleResult(rule, totalEctsCredits);
	}
    }

    private RuleResult createFalseRuleResult(final MinimumNumberOfCreditsToEnrol rule,
	    final Double ectsCredits) {
	return RuleResult
		.createFalse(
			"curricularRules.ruleExecutors.MinimumNumberOfCreditsToEnrolExecutor.student.has.not.minimum.number.of.credits",
			ectsCredits.toString(), rule.getMinimumCredits().toString(), rule
				.getDegreeModuleToApplyRule().getName());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(
	    final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final MinimumNumberOfCreditsToEnrol rule = (MinimumNumberOfCreditsToEnrol) curricularRule;
	if (!canApplyRule(enrolmentContext, curricularRule)) {
	    return RuleResult.createNA();
	}

	final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().getRoot();
	Double totalEctsCredits = curriculumGroup.getAprovedEctsCredits();

	if (rule.allowCredits(totalEctsCredits)) {
	    return RuleResult.createTrue();
	}

	final ExecutionPeriod previousExecutionPeriod = enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod();
	totalEctsCredits = Double.valueOf(totalEctsCredits.doubleValue() + curriculumGroup.getEnroledEctsCredits(previousExecutionPeriod).doubleValue());

	if (rule.allowCredits(totalEctsCredits)) {
	    return RuleResult.createTrue(EnrolmentResultType.TEMPORARY);
	}
	
	if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
	    return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE);
	} else {
	    return createFalseRuleResult(rule, totalEctsCredits);
	}
    }

}
