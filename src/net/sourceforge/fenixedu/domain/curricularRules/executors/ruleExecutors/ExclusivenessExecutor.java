package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

public class ExclusivenessExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final Exclusiveness rule = (Exclusiveness) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final DegreeModule degreeModule = rule.getExclusiveDegreeModule();

	if (degreeModule.isLeaf()) {
	    final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
	    final ExecutionPeriod previousExecutionPeriod = enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod();

	    if (isApproved(enrolmentContext, curricularCourse)
		    || hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, previousExecutionPeriod)) {

		if (isEnroled(enrolmentContext, (CurricularCourse) rule.getDegreeModuleToApplyRule(), enrolmentContext
			.getExecutionPeriod())) {
		    return createImpossibleRuleResult(rule, sourceDegreeModuleToEvaluate);
		}
		return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
	    }
	}

	if (isEnroled(enrolmentContext, degreeModule) || isEnrolling(enrolmentContext, degreeModule)) {
	    return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
	}

	return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    private RuleResult createFalseRuleResult(final Exclusiveness rule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
	return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		"curricularRules.ruleExecutors.ExclusivenessExecutor.exclusive.degreeModule", rule.getDegreeModuleToApplyRule()
			.getName(), rule.getExclusiveDegreeModule().getName());
    }

    private RuleResult createImpossibleRuleResult(final Exclusiveness rule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
	return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
		"curricularRules.ruleExecutors.ExclusivenessExecutor.exclusive.degreeModule", rule.getDegreeModuleToApplyRule()
			.getName(), rule.getExclusiveDegreeModule().getName());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final Exclusiveness rule = (Exclusiveness) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final DegreeModule degreeModule = rule.getExclusiveDegreeModule();
	if (degreeModule.isLeaf()) {
	    final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;

	    if (isApproved(enrolmentContext, curricularCourse)) {
		if (isEnroled(enrolmentContext, (CurricularCourse) rule.getDegreeModuleToApplyRule(), enrolmentContext
			.getExecutionPeriod())) {
		    return createImpossibleRuleResult(rule, sourceDegreeModuleToEvaluate);
		}
		return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
	    }

	    final ExecutionPeriod previousExecutionPeriod = enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod();
	    if (hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, previousExecutionPeriod)) {
		return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule());
	    }
	}

	if (isEnroled(enrolmentContext, degreeModule) || isEnrolling(enrolmentContext, degreeModule)) {
	    return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
	}

	return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

}
