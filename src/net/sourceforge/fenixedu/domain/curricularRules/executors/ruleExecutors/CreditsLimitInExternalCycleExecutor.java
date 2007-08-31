package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimitInExternalCycle;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
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

	final Double totalCredits = calculateApprovedAndEnrollingTotalCredits(enrolmentContext, externalCurriculumGroup);
	if (creditsLimitInExternalCycle.creditsExceedMaximum(totalCredits)) {
	    if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
		return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE, sourceDegreeModuleToEvaluate.getDegreeModule());
	    }
	    return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.limit.exceeded",
		    creditsLimitInExternalCycle.getExternalCurriculumGroup().getName().getContent(), creditsLimitInExternalCycle
			    .getMaxCredits().toString());
	}

	final Double totalEctsWithEnroledEctsCreditsFromPreviousPeriod = totalCredits
		+ externalCurriculumGroup.getEnroledEctsCredits(enrolmentContext.getExecutionPeriod()
			.getPreviousExecutionPeriod());
	if (creditsLimitInExternalCycle.creditsExceedMaximum(totalEctsWithEnroledEctsCreditsFromPreviousPeriod)) {
	    return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.limit.exceeded",
		    creditsLimitInExternalCycle.getExternalCurriculumGroup().getName().getContent(), creditsLimitInExternalCycle
			    .getMaxCredits().toString());

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

	final Double totalCredits = calculateApprovedAndEnrollingTotalCredits(enrolmentContext, externalCurriculumGroup);
	if (creditsLimitInExternalCycle.creditsExceedMaximum(totalCredits)) {
	    if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
		return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE, sourceDegreeModuleToEvaluate.getDegreeModule());
	    }
	    return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.limit.exceeded",
		    creditsLimitInExternalCycle.getExternalCurriculumGroup().getName().getContent(), creditsLimitInExternalCycle
			    .getMaxCredits().toString());
	}

	return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());

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
	    final CurriculumModuleEnroledWrapper curriculumModuleEnroledWrapper = (CurriculumModuleEnroledWrapper) degreeModuleToEvaluate;
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
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
	    if (degreeModuleToEvaluate.isLeaf()
		    && externalCurriculumGroup.hasCurriculumModule(degreeModuleToEvaluate.getCurriculumGroup())) {
		result += degreeModuleToEvaluate.getEctsCredits();
	    }
	}

	result += externalCurriculumGroup.getAprovedEctsCredits();

	return result;
    }

}
