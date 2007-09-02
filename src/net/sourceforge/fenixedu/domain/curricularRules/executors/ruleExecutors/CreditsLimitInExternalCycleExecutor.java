package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimitInExternalCycle;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalCurriculumGroup;

public class CreditsLimitInExternalCycleExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {

	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	final CreditsLimitInExternalCycle creditsLimitInExternalCycle = (CreditsLimitInExternalCycle) curricularRule;
	final ExternalCurriculumGroup externalCurriculumGroup = creditsLimitInExternalCycle.getExternalCurriculumGroup();

	if (!isToApply(sourceDegreeModuleToEvaluate, enrolmentContext, externalCurriculumGroup)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final CycleCurriculumGroup previousCycleCurriclumGroup = creditsLimitInExternalCycle.getPreviousCycleCurriculumGroup();
	final Double totalCreditsInPreviousCycle = previousCycleCurriclumGroup.getAprovedEctsCredits();
	if (!creditsLimitInExternalCycle.creditsInPreviousCycleSufficient(totalCreditsInPreviousCycle)) {
	    return createRuleResultForNotSatisfiedCreditsForPreviousCycle(sourceDegreeModuleToEvaluate,
		    creditsLimitInExternalCycle, previousCycleCurriclumGroup);

	}

	final Double totalCredits = calculateApprovedAndEnrollingTotalCredits(enrolmentContext, externalCurriculumGroup);
	if (creditsLimitInExternalCycle.creditsExceedMaximumInExternalCycle(totalCredits)) {
	    if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
		return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE, sourceDegreeModuleToEvaluate.getDegreeModule());
	    }
	    return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.limit.exceeded",
		    creditsLimitInExternalCycle.getExternalCurriculumGroup().getName().getContent(), creditsLimitInExternalCycle
			    .getMaxCreditsInExternalCycle().toString());
	}

	final Double totalEctsWithEnroledEctsCreditsFromPreviousPeriod = totalCredits
		+ externalCurriculumGroup.getEnroledEctsCredits(enrolmentContext.getExecutionPeriod()
			.getPreviousExecutionPeriod());
	if (creditsLimitInExternalCycle.creditsExceedMaximumInExternalCycle(totalEctsWithEnroledEctsCreditsFromPreviousPeriod)) {
	    return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.limit.exceeded",
		    creditsLimitInExternalCycle.getExternalCurriculumGroup().getName().getContent(), creditsLimitInExternalCycle
			    .getMaxCreditsInExternalCycle().toString());

	}

	return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentWithRules(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {

	final CreditsLimitInExternalCycle creditsLimitInExternalCycle = (CreditsLimitInExternalCycle) curricularRule;
	final ExternalCurriculumGroup externalCurriculumGroup = creditsLimitInExternalCycle.getExternalCurriculumGroup();

	if (!isToApply(sourceDegreeModuleToEvaluate, enrolmentContext, externalCurriculumGroup)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final CycleCurriculumGroup previousCycleCurriclumGroup = creditsLimitInExternalCycle.getPreviousCycleCurriculumGroup();
	final Double totalCreditsInPreviousCycle = previousCycleCurriclumGroup.getAprovedEctsCredits();
	if (!creditsLimitInExternalCycle.creditsInPreviousCycleSufficient(totalCreditsInPreviousCycle)) {
	    return createRuleResultForNotSatisfiedCreditsForPreviousCycle(sourceDegreeModuleToEvaluate,
		    creditsLimitInExternalCycle, previousCycleCurriclumGroup);

	}

	final Double totalCreditsInExternalCycle = calculateApprovedAndEnrollingTotalCredits(enrolmentContext,
		externalCurriculumGroup);
	if (creditsLimitInExternalCycle.creditsExceedMaximumInExternalCycle(totalCreditsInExternalCycle)) {
	    if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
		return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE, sourceDegreeModuleToEvaluate.getDegreeModule());
	    }
	    return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.external.cycle.limit.exceeded",
		    creditsLimitInExternalCycle.getExternalCurriculumGroup().getName().getContent(), creditsLimitInExternalCycle
			    .getMaxCreditsInExternalCycle().toString());
	}

	return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());

    }

    private RuleResult createRuleResultForNotSatisfiedCreditsForPreviousCycle(
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final CreditsLimitInExternalCycle creditsLimitInExternalCycle,
	    final CycleCurriculumGroup previousCycleCurriclumGroup) {
	if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
	    return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE, sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		"curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.previous.cycle.minimum.credits.not.fulfilled",
		creditsLimitInExternalCycle.getExternalCurriculumGroup().getName().getContent(), creditsLimitInExternalCycle
			.getMinCreditsInPreviousCycle().toString(), previousCycleCurriclumGroup.getName().getContent());
    }

    private boolean isToApply(IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext,
	    final ExternalCurriculumGroup externalCurriculumGroup) {
	if (!sourceDegreeModuleToEvaluate.isLeaf()) {
	    return false;
	}

	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
	    if (externalCurriculumGroup.hasCurriculumModule(degreeModuleToEvaluate.getCurriculumGroup())
		    && (degreeModuleToEvaluate.isEnroling() || isEnroledIn(degreeModuleToEvaluate, enrolmentContext
			    .getExecutionPeriod()))) {
		return true;
	    }
	}

	return false;

    }

    private boolean isEnroledIn(IDegreeModuleToEvaluate degreeModuleToEvaluate, ExecutionPeriod executionPeriod) {
	if (degreeModuleToEvaluate.isLeaf()) {
	    final EnroledCurriculumModuleWrapper curriculumModuleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;
	    final CurriculumLine curriculumLine = (CurriculumLine) (curriculumModuleEnroledWrapper).getCurriculumModule();

	    if (curriculumLine.isEnrolment()) {
		return curriculumLine.getExecutionPeriod() == executionPeriod;
	    }

	    return false;
	}

	return false;
    }

    private Double calculateApprovedAndEnrollingTotalCredits(final EnrolmentContext enrolmentContext,
	    final ExternalCurriculumGroup externalCurriculumGroup) {
	double result = 0;
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
	    if (degreeModuleToEvaluate.isLeaf()
		    && externalCurriculumGroup.hasCurriculumModule(degreeModuleToEvaluate.getCurriculumGroup())) {
		result += degreeModuleToEvaluate.getEctsCredits(executionPeriod);
	    }
	}

	result += externalCurriculumGroup.getAprovedEctsCredits();

	return result;
    }

}
