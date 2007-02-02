package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.AnyCurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class AnyCurricularCourseExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeWithRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final AnyCurricularCourse rule = (AnyCurricularCourse) curricularRule;
	final DegreeModuleToEnrol moduleToEnrol = getDegreeModuleToEnrol(enrolmentContext, rule.getDegreeModuleToApplyRule());

	if (!rule.appliesToContext(moduleToEnrol.getContext())) {
	    return RuleResult.createNA();
	}
	
	
	
	
	 return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.CreditsLimitExecutor.limits.not.fulfilled", rule.getDegreeModuleToApplyRule().getName());
    }

    @Override
    protected RuleResult executeWithRulesAndTemporaryEnrolment(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return executeWithRules(curricularRule, enrolmentContext);
    }

    @Override
    protected RuleResult executeNoRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }

}
