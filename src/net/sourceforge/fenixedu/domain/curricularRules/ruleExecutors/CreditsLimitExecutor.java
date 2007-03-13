package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class CreditsLimitExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final CreditsLimit rule = (CreditsLimit) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}

	final DegreeModule degreeModule = rule.getDegreeModuleToApplyRule();
	if (degreeModule.isOptional()) {
	    return evaluateIfCanEnrolToOptionalDegreeModule(enrolmentContext, rule);

	} else {

	    final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, rule);
	    if (degreeModuleToEvaluate.isEnroled()) {

		final CurriculumModuleEnroledWrapper moduleEnroledWrapper = (CurriculumModuleEnroledWrapper) degreeModuleToEvaluate;
		final CurriculumModule curriculumModule = moduleEnroledWrapper.getCurriculumModule();

		final Double ectsCredits = curriculumModule.getAprovedEctsCredits()
			+ curriculumModule.getEnroledEctsCredits(enrolmentContext.getExecutionPeriod())
			+ calculateEctsCreditsFromToEnrolCurricularCourses(enrolmentContext, rule);

		if (rule.creditsExceedMaximum(ectsCredits)) {
		    if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
			return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE);
		    } else {
			return createFalseRuleResult(rule);
		    }
		} else {
		    return RuleResult.createTrue();
		}

	    } else { // is enrolling now
		return RuleResult.createNA();
	    }
	}
    }

    private RuleResult evaluateIfCanEnrolToOptionalDegreeModule(final EnrolmentContext enrolmentContext,final CreditsLimit rule) {
	final OptionalDegreeModuleToEnrol optionalToEnrol = (OptionalDegreeModuleToEnrol) searchDegreeModuleToEvaluate(enrolmentContext, rule);
	final CurricularCourse curricularCourse = optionalToEnrol.getCurricularCourse();
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	return rule.allowCredits(curricularCourse.getEctsCredits(executionPeriod)) ? RuleResult.createTrue() : createFalseRuleResult(rule);
    }

    private Double calculateEctsCreditsFromToEnrolCurricularCourses(final EnrolmentContext enrolmentContext, final CreditsLimit rule) {
	final CourseGroup courseGroup = (CourseGroup) rule.getDegreeModuleToApplyRule();
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	
	BigDecimal result = BigDecimal.ZERO;
	for (final DegreeModule degreeModule : courseGroup.collectAllChildDegreeModules(CurricularCourse.class, executionPeriod)) {
	    final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, degreeModule);
	    if (degreeModuleToEvaluate != null && !degreeModuleToEvaluate.isEnroled()) {
		result = result.add(new BigDecimal(degreeModuleToEvaluate.getEctsCredits(executionPeriod)));
	    }
	}
	return Double.valueOf(result.doubleValue());
    }

    private RuleResult createFalseRuleResult(final CreditsLimit rule) {
	if (rule.getMinimumCredits().equals(rule.getMaximumCredits())) {
	    return RuleResult
		    .createFalse(
			    "curricularRules.ruleExecutors.CreditsLimitExecutor.limit.not.fulfilled",
			    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumCredits().toString());
	} else {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.CreditsLimitExecutor.limits.not.fulfilled", rule
			    .getDegreeModuleToApplyRule().getName(), rule.getMinimumCredits().toString(), rule.getMaximumCredits().toString());
	}
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final CreditsLimit rule = (CreditsLimit) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}

	final DegreeModule degreeModule = rule.getDegreeModuleToApplyRule();
	if (degreeModule.isOptional()) {
	    return evaluateIfCanEnrolToOptionalDegreeModule(enrolmentContext, rule);

	} else {

	    final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, rule);
	    if (degreeModuleToEvaluate.isEnroled()) {

		final CurriculumModuleEnroledWrapper moduleEnroledWrapper = (CurriculumModuleEnroledWrapper) degreeModuleToEvaluate;
		final CurriculumModule curriculumModule = moduleEnroledWrapper.getCurriculumModule();
		final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();

		Double ectsCredits = curriculumModule.getAprovedEctsCredits()
			+ curriculumModule.getEnroledEctsCredits(executionPeriod)
			+ calculateEctsCreditsFromToEnrolCurricularCourses(enrolmentContext, rule);

		if (rule.creditsExceedMaximum(ectsCredits)) {
		    if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
			return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE);
		    } else {
			return createFalseRuleResult(rule);
		    }
		}

		ectsCredits = Double.valueOf(ectsCredits.doubleValue()
			+ curriculumModule.getEnroledEctsCredits(executionPeriod.getPreviousExecutionPeriod()).doubleValue());

		if (rule.creditsExceedMaximum(ectsCredits)) { 
		    return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, 
			    "curricularRules.ruleExecutors.CreditsLimitExecutor.exceeded.maximum.credits.limit",
			    ectsCredits.toString(),
			    rule.getMaximumCredits().toString(),
			    curriculumModule.getName().getContent());
		} else {
		    return RuleResult.createTrue();
		}

	    } else { // is enrolling now
		return RuleResult.createNA();
	    }
	}
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA();
    }

}
