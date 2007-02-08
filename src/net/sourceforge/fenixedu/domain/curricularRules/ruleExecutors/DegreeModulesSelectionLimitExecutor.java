package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class DegreeModulesSelectionLimitExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final CurricularRule curricularRule,
	    final EnrolmentContext enrolmentContext) {

	final DegreeModulesSelectionLimit rule = (DegreeModulesSelectionLimit) curricularRule;

	if (ruleWasSelectedFromAnyModuleToEnrol(enrolmentContext, curricularRule)) {
	    return RuleResult.createNA();
	}

	final CourseGroup courseGroup = rule.getDegreeModuleToApplyRule();
	final CurriculumGroup curriculumGroup = (CurriculumGroup) searchCurriculumModule(enrolmentContext, rule);
	final CourseGroup parentCourseGroup = curriculumGroup.getCurriculumGroup().getDegreeModule();
	
	if (rule.appliesToCourseGroup(parentCourseGroup)) {
	    int total = countTotalDegreeModules(enrolmentContext, courseGroup, curriculumGroup);
	    return rule.allowNumberOfDegreeModules(total) ? RuleResult.createTrue() : createFalseRuleResult(rule);
	}

	return RuleResult.createNA();

    }
    
    private int countTotalDegreeModules(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup, final CurriculumGroup curriculumGroup) {
	
	int numberOfDegreeModulesToEnrol = countNumberOfDegreeModulesToEnrol(enrolmentContext, courseGroup);
	int numberOfApprovedEnrolments = curriculumGroup.getNumberOfApprovedEnrolments();
	int numberOfEnrolments = curriculumGroup.getNumberOfEnrolments(enrolmentContext.getExecutionPeriod());
	
	return numberOfApprovedEnrolments + numberOfEnrolments + numberOfDegreeModulesToEnrol;
    }

    private RuleResult createFalseRuleResult(final DegreeModulesSelectionLimit rule) {
	if (rule.getMinimumLimit().equals(rule.getMaximumLimit())) {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limit.exceded", 
		    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString());
	} else {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limits.exceded", 
		    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString(), rule.getMaximumLimit().toString());
	}
    }

    private int countNumberOfDegreeModulesToEnrol(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
	int result = 0;
	for (final Context context : courseGroup.getChildContexts(enrolmentContext.getExecutionPeriod())) {
	    if (isEnrolling(enrolmentContext, context.getChildDegreeModule())) {
		result++;
	    }
	}
	return result;
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final DegreeModulesSelectionLimit rule = (DegreeModulesSelectionLimit) curricularRule;
	
	if (ruleWasSelectedFromAnyModuleToEnrol(enrolmentContext, curricularRule)) {
	    return RuleResult.createNA();
	}
	
	final CourseGroup courseGroup = rule.getDegreeModuleToApplyRule();
	final CurriculumGroup curriculumGroup = (CurriculumGroup) searchCurriculumModule(enrolmentContext, rule);
	final CourseGroup parentCourseGroup = curriculumGroup.getCurriculumGroup().getDegreeModule();
	
	if (rule.appliesToCourseGroup(parentCourseGroup)) {

	    int total = countTotalDegreeModules(enrolmentContext, courseGroup, curriculumGroup);

	    if (!rule.allowNumberOfDegreeModules(total)) {
		return createFalseRuleResult(rule);
	    }
	    
	    final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	    total += curriculumGroup.getNumberOfEnrolments(executionPeriod.getPreviousExecutionPeriod());
	    return rule.allowNumberOfDegreeModules(total) ? RuleResult.createTrue() : RuleResult.createTrue(EnrolmentResultType.TEMPORARY);
	    
	}
	
	return RuleResult.createNA();
    }

    @Override
    protected RuleResult executeEnrolmentWithNoRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }
}
