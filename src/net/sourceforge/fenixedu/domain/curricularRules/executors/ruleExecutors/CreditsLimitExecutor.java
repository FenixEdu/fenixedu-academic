package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class CreditsLimitExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	final CreditsLimit rule = (CreditsLimit) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final DegreeModule degreeModule = rule.getDegreeModuleToApplyRule();
	if (degreeModule.isOptional()) {
	    return evaluateIfCanEnrolToOptionalDegreeModule(enrolmentContext, rule, sourceDegreeModuleToEvaluate);

	} else {

	    final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, rule);
	    if (degreeModuleToEvaluate.isEnroled()) {

		final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;
		final CurriculumModule curriculumModule = moduleEnroledWrapper.getCurriculumModule();

		final Double ectsCredits = curriculumModule.getAprovedEctsCredits()
			+ curriculumModule.getEnroledEctsCredits(enrolmentContext.getExecutionPeriod())
			+ calculateEctsCreditsFromToEnrolCurricularCourses(enrolmentContext, curriculumModule);

		if (rule.creditsExceedMaximum(ectsCredits)) {
		    if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
			return createImpossibleResult(rule, sourceDegreeModuleToEvaluate);
		    } else {
			return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate, ectsCredits);
		    }
		} else {
		    return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
		}

	    } else { // is enrolling now
		return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	    }
	}

    }

    private RuleResult evaluateIfCanEnrolToOptionalDegreeModule(final EnrolmentContext enrolmentContext, final CreditsLimit rule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
	final CurricularCourse curricularCourse;
	if (sourceDegreeModuleToEvaluate.isEnroled()) {
	    curricularCourse = (CurricularCourse) sourceDegreeModuleToEvaluate.getDegreeModule();
	} else {
	    curricularCourse = ((OptionalDegreeModuleToEnrol) sourceDegreeModuleToEvaluate).getCurricularCourse();
	}

	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	Double ectsCredits = curricularCourse.getEctsCredits(executionPeriod);
	return rule.allowCredits(ectsCredits) ? RuleResult
		.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule()) : createFalseRuleResult(rule,
		sourceDegreeModuleToEvaluate, ectsCredits);
    }

    private Double calculateEctsCreditsFromToEnrolCurricularCourses(final EnrolmentContext enrolmentContext,
	    final CurriculumModule parentCurriculumModule) {
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();

	BigDecimal result = BigDecimal.ZERO;
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
	    if (degreeModuleToEvaluate.isEnroling()
		    && parentCurriculumModule.hasCurriculumModule(degreeModuleToEvaluate.getCurriculumGroup())) {
		result = result.add(BigDecimal.valueOf(degreeModuleToEvaluate.getEctsCredits(executionPeriod)));
	    }
	}

	return Double.valueOf(result.doubleValue());
    }

    private RuleResult createFalseRuleResult(final CreditsLimit rule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final Double ectsCredits) {
	if (rule.getMinimumCredits().equals(rule.getMaximumCredits())) {
	    return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.CreditsLimitExecutor.limit.not.fulfilled", rule.getDegreeModuleToApplyRule()
			    .getName(), rule.getMinimumCredits().toString(), ectsCredits.toString());
	} else {
	    return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.CreditsLimitExecutor.limits.not.fulfilled", rule.getDegreeModuleToApplyRule()
			    .getName(), rule.getMinimumCredits().toString(), rule.getMaximumCredits().toString(), ectsCredits.toString());
	}
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final CreditsLimit rule = (CreditsLimit) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final DegreeModule degreeModule = rule.getDegreeModuleToApplyRule();
	if (degreeModule.isOptional()) {
	    return evaluateIfCanEnrolToOptionalDegreeModule(enrolmentContext, rule, sourceDegreeModuleToEvaluate);

	} else {

	    final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, rule);
	    if (degreeModuleToEvaluate.isEnroled()) {
		final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;
		final CurriculumModule curriculumModule = moduleEnroledWrapper.getCurriculumModule();
		final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();

		Double ectsCredits = curriculumModule.getAprovedEctsCredits()
			+ curriculumModule.getEnroledEctsCredits(executionPeriod)
			+ calculateEctsCreditsFromToEnrolCurricularCourses(enrolmentContext, curriculumModule);

		if (rule.creditsExceedMaximum(ectsCredits)) {
		    if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
			return createImpossibleResult(rule, sourceDegreeModuleToEvaluate);
		    } else {
			return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate, ectsCredits);
		    }
		}

		ectsCredits = Double.valueOf(ectsCredits.doubleValue()
			+ curriculumModule.getEnroledEctsCredits(executionPeriod.getPreviousExecutionPeriod()).doubleValue());

		//TODO: remove duplicated ects from anual CurricularCourses
		
		if (rule.creditsExceedMaximum(ectsCredits)) {
		    return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule(),
			    "curricularRules.ruleExecutors.CreditsLimitExecutor.exceeded.maximum.credits.limit", ectsCredits
				    .toString(), rule.getMaximumCredits().toString(), curriculumModule.getName().getContent());
		} else {
		    return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
		}

	    } else { // is enrolling now
		return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	    }
	}
    }

    private RuleResult createImpossibleResult(final CreditsLimit rule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
	if (rule.getMinimumCredits().equals(rule.getMaximumCredits())) {
	    return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.CreditsLimitExecutor.limit.not.fulfilled", rule.getDegreeModuleToApplyRule()
			    .getName(), rule.getMinimumCredits().toString());
	} else {
	    return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.CreditsLimitExecutor.limits.not.fulfilled", rule.getDegreeModuleToApplyRule()
			    .getName(), rule.getMinimumCredits().toString(), rule.getMaximumCredits().toString());
	}
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
	    EnrolmentContext enrolmentContext) {
	return true;
    }

}
