package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.MinimumNumberOfCreditsToEnrol;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.RootCurriculumGroup;

public class MinimumNumberOfCreditsToEnrolExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final MinimumNumberOfCreditsToEnrol rule = (MinimumNumberOfCreditsToEnrol) curricularRule;
	if (!canApplyRule(enrolmentContext, curricularRule)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final Double totalEctsCredits = getTotalEctsCredits(enrolmentContext.getStudentCurricularPlan().getRoot(),
		enrolmentContext.getExecutionPeriod().getExecutionYear());

	if (rule.allowCredits(totalEctsCredits)) {
	    return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
	    return createImpossibleRuleResult(rule, totalEctsCredits, sourceDegreeModuleToEvaluate);
	} else {
	    return createFalseRuleResult(rule, totalEctsCredits, sourceDegreeModuleToEvaluate);
	}
    }

    private Double getTotalEctsCredits(final RootCurriculumGroup rootCurriculumGroup, ExecutionYear executionYear) {
	Double res = 0d;
	for (CycleType cycleType : rootCurriculumGroup.getDegreeType().getOrderedCycleTypes()) {
	    CycleCurriculumGroup cycleCurriculumGroup = rootCurriculumGroup.getCycleCurriculumGroup(cycleType);
	    if (cycleCurriculumGroup == null) {
		res += cycleType.getDefaultEcts();
	    } else {
		res += rootCurriculumGroup.getCreditsConcluded(executionYear);
		break;
	    }
	}
	return res;
    }

    private RuleResult createFalseRuleResult(final MinimumNumberOfCreditsToEnrol rule, final Double ectsCredits,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
	return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		"curricularRules.ruleExecutors.MinimumNumberOfCreditsToEnrolExecutor.student.has.not.minimum.number.of.credits",
		ectsCredits.toString(), rule.getMinimumCredits().toString(), rule.getDegreeModuleToApplyRule().getName());
    }

    private RuleResult createImpossibleRuleResult(final MinimumNumberOfCreditsToEnrol rule, final Double ectsCredits,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
	return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
		"curricularRules.ruleExecutors.MinimumNumberOfCreditsToEnrolExecutor.student.has.not.minimum.number.of.credits",
		ectsCredits.toString(), rule.getMinimumCredits().toString(), rule.getDegreeModuleToApplyRule().getName());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final MinimumNumberOfCreditsToEnrol rule = (MinimumNumberOfCreditsToEnrol) curricularRule;
	if (!canApplyRule(enrolmentContext, curricularRule)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().getRoot();
	Double totalEctsCredits = curriculumGroup.getAprovedEctsCredits();

	if (rule.allowCredits(totalEctsCredits)) {
	    return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final ExecutionSemester previousExecutionPeriod = enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod();
	totalEctsCredits = Double.valueOf(totalEctsCredits.doubleValue()
		+ curriculumGroup.getEnroledEctsCredits(previousExecutionPeriod).doubleValue());

	if (rule.allowCredits(totalEctsCredits)) {
	    return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
	    return createImpossibleRuleResult(rule, totalEctsCredits, sourceDegreeModuleToEvaluate);
	} else {
	    return createFalseRuleResult(rule, totalEctsCredits, sourceDegreeModuleToEvaluate);
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
