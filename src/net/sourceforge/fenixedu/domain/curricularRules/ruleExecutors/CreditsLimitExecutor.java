package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class CreditsLimitExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeWithRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {

	final CreditsLimit rule = (CreditsLimit) curricularRule;

	if (ruleWasSelectedFromAnyModuleToEnrol(enrolmentContext, curricularRule)
		&& !rule.appliesToContext(searchDegreeModuleToEnrol(enrolmentContext, rule.getDegreeModuleToApplyRule()).getContext())) {
	    return RuleResult.createNA();
	}
	
	final CurriculumModule curriculumModule = searchCurriculumModule(enrolmentContext, rule.getDegreeModuleToApplyRule());
	if (rule.allowCredits(curriculumModule.getEctsCredits())) {
	    return RuleResult.createTrue();
	}
	
	if (rule.getMinimumCredits().equals(rule.getMaximumCredits())) {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.CreditsLimitExecutor.limit.not.fulfilled", 
		    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumCredits().toString());
	} else {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.CreditsLimitExecutor.limits.not.fulfilled", 
		    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumCredits().toString(), rule.getMaximumCredits().toString());
	}
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
