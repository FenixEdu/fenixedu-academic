package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class RestrictionDoneDegreeModuleExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
	    final EnrolmentContext enrolmentContext) {

	final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}

	final CurricularCourse curricularCourse = rule.getPrecedenceDegreeModule();
	if (isEnrolling(enrolmentContext, curricularCourse) || isEnroled(enrolmentContext, curricularCourse, executionPeriod)) {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.cannot.enrol.simultaneously.to.degreeModule.and.precedenceDegreeModule",
		    rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());    
	}
	
	if (isApproved(enrolmentContext, curricularCourse)) {
	    return RuleResult.createTrue();
	}

	/*
	 * CurricularCourse is not approved
	 * If DegreeModule is Enroled in current semester then Enrolment must be impossible
	 */ 
	if (isEnroled(enrolmentContext, rule.getDegreeModuleToApplyRule())) {
	    return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE);
	}

	return createFalseRuleResult(rule);
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(
	    final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {

	final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}

	final CurricularCourse curricularCourse = rule.getPrecedenceDegreeModule();

	if (isEnrolling(enrolmentContext, curricularCourse)
		|| isEnroled(enrolmentContext, curricularCourse, executionPeriod)) {
	    return RuleResult
		    .createFalse(
			    "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.cannot.enrol.simultaneously.to.degreeModule.and.precedenceDegreeModule",
			    rule.getDegreeModuleToApplyRule().getName(), rule
				    .getPrecedenceDegreeModule().getName());
	}

	if (isApproved(enrolmentContext, curricularCourse)) {
	    return RuleResult.createTrue();
	}

	if (hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, executionPeriod
		.getPreviousExecutionPeriod())) {
	    return RuleResult.createTrue(EnrolmentResultType.TEMPORARY);
	}

	/*
         * CurricularCourse is not approved and is not enroled in previous
         * semester If DegreeModule is Enroled in current semester then
         * Enrolment must be impossible
         */
	if (isEnroled(enrolmentContext, rule.getDegreeModuleToApplyRule(), executionPeriod)) {
	    return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE);
	}

	return createFalseRuleResult(rule);
    }

    private RuleResult createFalseRuleResult(final RestrictionDoneDegreeModule rule) {
	return RuleResult
		.createFalse(
			"curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule",
			rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule()
				.getName());
    }

}
