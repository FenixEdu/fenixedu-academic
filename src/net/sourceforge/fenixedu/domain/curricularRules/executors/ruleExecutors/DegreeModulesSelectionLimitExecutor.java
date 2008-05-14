package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class DegreeModulesSelectionLimitExecutor extends CurricularRuleExecutor {

    private int countTotalDegreeModules(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup,
	    final CurriculumGroup curriculumGroup) {

	int numberOfDegreeModulesToEnrol = countNumberOfDegreeModulesToEnrol(enrolmentContext, courseGroup);
	int numberOfApprovedEnrolments = curriculumGroup.getNumberOfApprovedCurriculumLines();
	int numberOfEnrolments = curriculumGroup.getNumberOfEnrolments(enrolmentContext.getExecutionPeriod());
	int numberOfChildCurriculumGroups = curriculumGroup.getNumberOfChildCurriculumGroupsWithCourseGroup();

	return numberOfApprovedEnrolments + numberOfEnrolments + numberOfDegreeModulesToEnrol + numberOfChildCurriculumGroups;
    }

    private RuleResult createFalseRuleResult(final DegreeModulesSelectionLimit rule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
	if (rule.getMinimumLimit().equals(rule.getMaximumLimit())) {
	    return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limit.exceded", rule
			    .getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString());
	} else {
	    return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limits.exceded", rule
			    .getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString(), rule.getMaximumLimit()
			    .toString());
	}
    }

    private RuleResult createImpossibleRuleResult(final DegreeModulesSelectionLimit rule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
	if (rule.getMinimumLimit().equals(rule.getMaximumLimit())) {
	    return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limit.exceded", rule
			    .getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString());
	} else {
	    return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limits.exceded", rule
			    .getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString(), rule.getMaximumLimit()
			    .toString());
	}
    }

    private int countNumberOfDegreeModulesToEnrol(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
	int result = 0;
	for (final Context context : courseGroup.getValidChildContexts(enrolmentContext.getExecutionPeriod())) {
	    if (isEnrolling(enrolmentContext, context.getChildDegreeModule())) {
		result++;
	    }
	}
	return result;
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final DegreeModulesSelectionLimit rule = (DegreeModulesSelectionLimit) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, rule);

	if (degreeModuleToEvaluate.isEnroled()) {

	    final CourseGroup courseGroup = rule.getDegreeModuleToApplyRule();
	    final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;
	    final CurriculumGroup curriculumGroup = (CurriculumGroup) moduleEnroledWrapper.getCurriculumModule();

	    int total = countTotalDegreeModules(enrolmentContext, courseGroup, curriculumGroup);

	    if (rule.numberOfDegreeModulesExceedMaximum(total)) {
		if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
		    return createImpossibleRuleResult(rule, sourceDegreeModuleToEvaluate);
		} else {
		    return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
		}
	    }

	    final ExecutionSemester executionSemester = enrolmentContext.getExecutionPeriod();
	    total += curriculumGroup.getNumberOfEnrolments(executionSemester.getPreviousExecutionPeriod());

	    return rule.numberOfDegreeModulesExceedMaximum(total) ? RuleResult.createTrue(EnrolmentResultType.TEMPORARY,
		    sourceDegreeModuleToEvaluate.getDegreeModule()) : RuleResult.createTrue(sourceDegreeModuleToEvaluate
		    .getDegreeModule());
	}
	// is enrolling now
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	final DegreeModulesSelectionLimit rule = (DegreeModulesSelectionLimit) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, rule);

	if (degreeModuleToEvaluate.isEnroled()) {

	    final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;
	    final CourseGroup courseGroup = rule.getDegreeModuleToApplyRule();
	    final CurriculumGroup curriculumGroup = (CurriculumGroup) moduleEnroledWrapper.getCurriculumModule();

	    int total = countTotalDegreeModules(enrolmentContext, courseGroup, curriculumGroup);
	    if (rule.numberOfDegreeModulesExceedMaximum(total)) {
		if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
		    return createImpossibleRuleResult(rule, sourceDegreeModuleToEvaluate);
		} else {
		    return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
		}
	    } else {
		return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	    }

	} // is enrolling now
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
	    EnrolmentContext enrolmentContext) {
	return true;
    }

}
