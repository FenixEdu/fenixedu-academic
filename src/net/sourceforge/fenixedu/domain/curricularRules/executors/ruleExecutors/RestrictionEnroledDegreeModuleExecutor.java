package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionEnroledDegreeModule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

public class RestrictionEnroledDegreeModuleExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final RestrictionEnroledDegreeModule rule = (RestrictionEnroledDegreeModule) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final CurricularCourse curricularCourseToBeEnroled = rule.getPrecedenceDegreeModule();
	if (isApproved(enrolmentContext, curricularCourseToBeEnroled) || isEnroled(enrolmentContext, curricularCourseToBeEnroled)
		|| isEnrolling(enrolmentContext, curricularCourseToBeEnroled)) {
	    return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	return RuleResult
		.createFalse(
			sourceDegreeModuleToEvaluate.getDegreeModule(),
			"curricularRules.ruleExecutors.RestrictionEnroledDegreeModuleExecutor.student.is.not.enroled.to.precendenceDegreeModule",
			rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
	    EnrolmentContext enrolmentContext) {

	RestrictionEnroledDegreeModule restrictionEnroledDegreeModule = (RestrictionEnroledDegreeModule) curricularRule;

	Collection<CycleCourseGroup> cycleCourseGroups = restrictionEnroledDegreeModule.getPrecedenceDegreeModule()
		.getParentCycleCourseGroups();
	for (CycleCourseGroup cycleCourseGroup : cycleCourseGroups) {
	    CycleCurriculumGroup cycleCurriculumGroup = (CycleCurriculumGroup) enrolmentContext.getStudentCurricularPlan()
		    .findCurriculumGroupFor(cycleCourseGroup);
	    if (cycleCurriculumGroup != null) {
		return true;
	    }
	}

	return false;
    }

}
