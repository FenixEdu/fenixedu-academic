package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class CreditsLimitExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final CreditsLimit rule = (CreditsLimit) curricularRule;
	
	if (ruleWasSelectedFromAnyModuleToEnrol(enrolmentContext, curricularRule)) {
	    final DegreeModule degreeModule = rule.getDegreeModuleToApplyRule();

	    if (degreeModule.isOptional()) {
		
		final OptionalDegreeModuleToEnrol optionalToEnrol = (OptionalDegreeModuleToEnrol) searchDegreeModuleToEnrol(enrolmentContext, rule);
		final CurricularCourse curricularCourse = optionalToEnrol.getCurricularCourse();
		
		if (appliesToContext(enrolmentContext, rule) && !rule.creditsExceedMaximum(curricularCourse.getEctsCredits())) {
		    return RuleResult.createTrue();
		}
		
	    } else {
		return RuleResult.createNA();
	    }
	} 
	
	final CurriculumModule curriculumModule = searchCurriculumModule(enrolmentContext, rule);
	if (!rule.creditsExceedMaximum(curriculumModule.getEctsCredits())) {
	    return RuleResult.createTrue();
	}
	
	return createFalseRuleResult(rule);
    }

    private RuleResult createFalseRuleResult(final CreditsLimit rule) {
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
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return executeEnrolmentWithRules(curricularRule, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentWithNoRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }
}
