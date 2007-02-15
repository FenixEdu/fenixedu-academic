package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class RestrictionDoneDegreeModuleExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {

	final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
	
	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}
	return isApproved(enrolmentContext, rule.getPrecedenceDegreeModule()) ? RuleResult.createTrue() : createFalseRuleResult(rule);
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
	
	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}

	final CurricularCourse curricularCourse = rule.getPrecedenceDegreeModule();
	
	if (isApproved(enrolmentContext, curricularCourse)) {
	    return RuleResult.createTrue();    
	}
	
	if (isEnrolling(enrolmentContext, curricularCourse) || isEnroled(enrolmentContext, curricularCourse)) {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.cannot.enrol.simultaneously.to.degreeModule.and.precedenceDegreeModule",
		    rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());    
	}
	
	final ExecutionPeriod previousExecutionPeriod = enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod();
	if (hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, previousExecutionPeriod)) {
	    return RuleResult.createTrue(EnrolmentResultType.TEMPORARY);
	}

	return createFalseRuleResult(rule);
    }

    private RuleResult createFalseRuleResult(final RestrictionDoneDegreeModule rule) {
	return RuleResult
	    .createFalse(
		    "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule",
		    rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
    }
    
    @Override
    protected RuleResult executeEnrolmentWithNoRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }
}
