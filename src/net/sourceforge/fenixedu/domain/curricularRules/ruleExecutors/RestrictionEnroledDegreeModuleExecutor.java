package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionEnroledDegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class RestrictionEnroledDegreeModuleExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {

	final RestrictionEnroledDegreeModule rule = (RestrictionEnroledDegreeModule) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}
	
	final CurricularCourse curricularCourseToBeEnroled = rule.getPrecedenceDegreeModule();
	if (isEnroled(enrolmentContext, curricularCourseToBeEnroled) || isEnrolling(enrolmentContext, curricularCourseToBeEnroled)) {
	    return RuleResult.createTrue();
	}
	
	return RuleResult
	    .createFalse(
		    "curricularRules.ruleExecutors.RestrictionEnroledDegreeModuleExecutor.student.is.not.enroled.to.precendenceDegreeModule",
		    rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
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
