package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class RestrictionDoneDegreeModuleExecutor extends RuleExecutor {

    @Override
    protected RuleResult executeWithRules(CurricularRule curricularRule,
	    EnrolmentContext enrolmentContext) {

	final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
	final DegreeModuleToEnrol moduleToEnrol = getDegreeModuleToEnrolFor(enrolmentContext, rule
		.getDegreeModuleToApplyRule());
	if (!rule.appliesToContext(moduleToEnrol.getContext())) {
	    return RuleResult.createNA();
	}

	if (!enrolmentContext.getStudentCurricularPlan().isApproved(
		(CurricularCourse) rule.getPrecedenceDegreeModule())) {
	    return RuleResult
		    .createFalse(
			    "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule",
			    rule.getDegreeModuleToApplyRule().getName(), rule
				    .getPrecedenceDegreeModule().getName());
	}

	return RuleResult.createTrue();
    }

    @Override
    protected RuleResult executeNoRules(EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }

    @Override
    protected RuleResult executeWithRulesAndTemporaryEnrolment(CurricularRule curricularRule,
	    EnrolmentContext enrolmentContext) {
	final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
	final DegreeModuleToEnrol moduleToEnrol = getDegreeModuleToEnrolFor(enrolmentContext, rule
		.getDegreeModuleToApplyRule());
	if (!rule.appliesToContext(moduleToEnrol.getContext())) {
	    return RuleResult.createNA();
	}

	final CurricularCourse curricularCourse = (CurricularCourse) rule.getPrecedenceDegreeModule();
	if (!enrolmentContext.getStudentCurricularPlan().isApproved(curricularCourse)) {

	    if (enrolmentContext.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse,
		    enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod())) {
		return RuleResult.createTrue(EnrolmentResultType.TEMPORARY);
	    }

	    return RuleResult
		    .createFalse(
			    "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule",
			    rule.getDegreeModuleToApplyRule().getName(), rule
				    .getPrecedenceDegreeModule().getName());
	}

	return RuleResult.createTrue();
    }

}
