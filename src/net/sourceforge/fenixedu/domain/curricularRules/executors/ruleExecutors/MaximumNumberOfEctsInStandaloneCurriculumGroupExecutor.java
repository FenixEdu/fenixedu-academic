package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfEctsInStandaloneCurriculumGroup;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.StandaloneCurriculumGroup;

public class MaximumNumberOfEctsInStandaloneCurriculumGroupExecutor extends CurricularRuleExecutor {

	@Override
	protected boolean canBeEvaluated(final ICurricularRule curricularRule,
			final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
		return true;
	}

	@Override
	protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
			final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
		return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	@Override
	protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
			final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

		final MaximumNumberOfEctsInStandaloneCurriculumGroup rule =
				(MaximumNumberOfEctsInStandaloneCurriculumGroup) curricularRule;
		final double total = calculateTotalEctsCredits(enrolmentContext) + calculateApprovedEcts(enrolmentContext);
		if (!rule.allowEcts(total)) {
			return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
					"curricularRules.ruleExecutors.MaximumNumberOfEctsInStandaloneCurriculumGroupExecutor",
					String.valueOf(rule.getMaximumEcts()), String.valueOf(total));
		}

		return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	private double calculateTotalEctsCredits(final EnrolmentContext enrolmentContext) {
		double accumulated = 0d;
		for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
			accumulated += degreeModuleToEvaluate.getAccumulatedEctsCredits(enrolmentContext.getExecutionPeriod());
		}
		return accumulated;
	}

	private double calculateApprovedEcts(final EnrolmentContext enrolmentContext) {

		final ExecutionSemester executionSemester = enrolmentContext.getExecutionPeriod();
		final StandaloneCurriculumGroup group = enrolmentContext.getStudentCurricularPlan().getStandaloneCurriculumGroup();

		double approved = 0d;

		for (final CurriculumLine line : group.getChildCurriculumLines()) {
			if (line.isApproved() && line.isValid(executionSemester)) {
				approved += line.getAccumulatedEctsCredits(executionSemester);
			}
		}

		return approved;
	}

	@Override
	protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(ICurricularRule curricularRule,
			IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
		return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
	}

}
