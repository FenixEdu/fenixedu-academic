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
	
	if (!appliesToContext(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}

	if (!isApproved(enrolmentContext, rule.getPrecedenceDegreeModule())) {
	    return RuleResult
		    .createFalse(
			    "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule",
			    rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
	}
	
	return RuleResult.createTrue();
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
	
	if (!appliesToContext(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}

	final CurricularCourse curricularCourse = rule.getPrecedenceDegreeModule();
	if (!isApproved(enrolmentContext, curricularCourse)) {
	    
	    final ExecutionPeriod previous = enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod();
	    if (isEnroled(enrolmentContext, curricularCourse, previous)) {
		return RuleResult.createTrue(EnrolmentResultType.TEMPORARY);
	    }
	    return RuleResult
		    .createFalse(
			    "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule",
			    rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
	}

	return RuleResult.createTrue();
    }
    
    @Override
    protected RuleResult executeEnrolmentWithNoRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }
}
