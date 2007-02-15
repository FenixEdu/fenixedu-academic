package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionBetweenDegreeModules;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class RestrictionBetweenDegreeModulesExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final RestrictionBetweenDegreeModules rule = (RestrictionBetweenDegreeModules) curricularRule;
	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}
	
	final CourseGroup courseGroup = rule.getPrecedenceDegreeModule();
	
	if (isEnrolling(enrolmentContext, courseGroup)) {
	    return createFalseRuleResultWithInvalidEcts(rule);
	    
	} else if (isEnroled(enrolmentContext, courseGroup)) {
	    
	    final CurriculumModuleEnroledWrapper moduleEnroledWrapper = (CurriculumModuleEnroledWrapper) searchDegreeModuleToEvaluate(enrolmentContext, rule);
	    final CurriculumModule curriculumModule = moduleEnroledWrapper.getCurriculumModule();
	    
	    if (!rule.hasMinimumCredits() || rule.allowCredits(curriculumModule.getAprovedEctsCredits())) {
		return RuleResult.createTrue();
	    } else {
		return createFalseRuleResultWithInvalidEcts(rule);
	    }
	}
	
	return RuleResult.createFalse(
		"curricularRules.ruleExecutors.RestrictionBetweenDegreeModulesExecutor.student.has.not.precedence.degreeModule", rule
			.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
    }
    
    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final RestrictionBetweenDegreeModules rule = (RestrictionBetweenDegreeModules) curricularRule;
	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}

	final CourseGroup courseGroup = rule.getPrecedenceDegreeModule();
	
	if (isEnrolling(enrolmentContext, courseGroup)) {
	    return createFalseRuleResultWithInvalidEcts(rule);
	    
	} else if (isEnroled(enrolmentContext, courseGroup)) {
	    
	    if (!rule.hasMinimumCredits()) {
		return RuleResult.createTrue();
	    }
	    
	    final CurriculumModuleEnroledWrapper moduleEnroledWrapper = (CurriculumModuleEnroledWrapper) searchDegreeModuleToEvaluate(enrolmentContext, rule);
	    final CurriculumModule curriculumModule = moduleEnroledWrapper.getCurriculumModule();
	    Double ectsCredits = curriculumModule.getAprovedEctsCredits();

	    if (rule.allowCredits(ectsCredits)) {
		return RuleResult.createTrue();
	    }
	    
	    final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	    ectsCredits = Double.valueOf(ectsCredits.doubleValue() + curriculumModule.getEnroledEctsCredits(executionPeriod.getPreviousExecutionPeriod()).doubleValue());
	    
	    return rule.allowCredits(ectsCredits) ? RuleResult.createTrue() : createFalseRuleResultWithInvalidEcts(rule);
	}
	
	return RuleResult.createFalse(
		"curricularRules.ruleExecutors.RestrictionBetweenDegreeModulesExecutor.student.has.not.precedence.degreeModule", rule
			.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
    }

    private RuleResult createFalseRuleResultWithInvalidEcts(final RestrictionBetweenDegreeModules rule) {
	return RuleResult
	.createFalse("curricularRules.ruleExecutors.RestrictionBetweenDegreeModulesExecutor.invalid.ects.credits.in.precedence.degreeModule",
		rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName(), rule.getMinimumCredits().toString());
    }

    @Override
    protected RuleResult executeEnrolmentWithNoRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }
}
