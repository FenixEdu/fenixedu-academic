package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class DegreeModulesSelectionLimitExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final DegreeModulesSelectionLimit rule = (DegreeModulesSelectionLimit) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}

	final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, rule);

	if (degreeModuleToEvaluate.isEnroled()) {

	    final CurriculumModuleEnroledWrapper moduleEnroledWrapper = (CurriculumModuleEnroledWrapper) degreeModuleToEvaluate;
	    final CourseGroup courseGroup = rule.getDegreeModuleToApplyRule();
	    final CurriculumGroup curriculumGroup = (CurriculumGroup) moduleEnroledWrapper.getCurriculumModule();

	    int total = countTotalDegreeModules(enrolmentContext, courseGroup, curriculumGroup);
	    if (rule.numberOfDegreeModulesExceedMaximum(total)) {
		if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
		    return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE);
		} else {
		    return createFalseRuleResult(rule);
		}
	    } else {
		return RuleResult.createTrue();
	    }

	} // is enrolling now
	return RuleResult.createNA();
    }

    private int countTotalDegreeModules(final EnrolmentContext enrolmentContext,
	    final CourseGroup courseGroup, final CurriculumGroup curriculumGroup) {

	int numberOfDegreeModulesToEnrol = countNumberOfDegreeModulesToEnrol(enrolmentContext, courseGroup);
	int numberOfApprovedEnrolments = curriculumGroup.getNumberOfApprovedEnrolments();
	int numberOfEnrolments = curriculumGroup.getNumberOfEnrolments(enrolmentContext.getExecutionPeriod());
	int numberOfChildCurriculumGroups = curriculumGroup.getNumberOfChildCurriculumGroupsWithCourseGroup();

	return numberOfApprovedEnrolments + numberOfEnrolments + numberOfDegreeModulesToEnrol + numberOfChildCurriculumGroups;
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
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(
	    final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final DegreeModulesSelectionLimit rule = (DegreeModulesSelectionLimit) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}

	final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(
		enrolmentContext, rule);

	if (degreeModuleToEvaluate.isEnroled()) {

	    final CourseGroup courseGroup = rule.getDegreeModuleToApplyRule();
	    final CurriculumModuleEnroledWrapper moduleEnroledWrapper = (CurriculumModuleEnroledWrapper) degreeModuleToEvaluate;
	    final CurriculumGroup curriculumGroup = (CurriculumGroup) moduleEnroledWrapper
		    .getCurriculumModule();

	    int total = countTotalDegreeModules(enrolmentContext, courseGroup, curriculumGroup);

	    if (rule.numberOfDegreeModulesExceedMaximum(total)) {
		if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
		    return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE);
		} else {
		    return createFalseRuleResult(rule);
		}
	    }

	    final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	    total += curriculumGroup.getNumberOfEnrolments(executionPeriod.getPreviousExecutionPeriod());

	    return rule.numberOfDegreeModulesExceedMaximum(total) ? RuleResult.createTrue(EnrolmentResultType.TEMPORARY) : RuleResult.createTrue();
	}
	// is enrolling now
	return RuleResult.createNA();
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA();
    }

}
