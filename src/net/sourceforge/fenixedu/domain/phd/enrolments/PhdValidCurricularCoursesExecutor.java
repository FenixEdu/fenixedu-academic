package net.sourceforge.fenixedu.domain.phd.enrolments;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleExecutor;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;

public class PhdValidCurricularCoursesExecutor extends CurricularRuleExecutor {

	@Override
	protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
			EnrolmentContext enrolmentContext) {
		return true;
	}

	@Override
	protected RuleResult executeEnrolmentInEnrolmentEvaluation(ICurricularRule curricularRule,
			IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
		return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	@Override
	protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
			IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {

		final CurricularCourse curricularCourse = getCurricularCourse(sourceDegreeModuleToEvaluate, curricularRule);

		if (!curricularCourse.hasAnyActiveContext(enrolmentContext.getExecutionPeriod())) {
			return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
		}

		final Collection<CompetenceCourse> collection = getCompetenceCoursesAvailableToEnrol(enrolmentContext);

		if (!collection.contains(curricularCourse.getCompetenceCourse())) {

			if (isEnrolling(enrolmentContext, curricularCourse)) {

				return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
						"curricularRules.ruleExecutors.PhdValidCurricularCoursesExecutor.invalid.curricularCourse",
						curricularCourse.getName());

			} else if (isApproved(enrolmentContext, curricularCourse) || isEnroled(enrolmentContext, curricularCourse)) {

				return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
						"curricularRules.ruleExecutors.PhdValidCurricularCoursesExecutor.invalid.curricularCourse",
						curricularCourse.getName());
			}
		}

		return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	private CurricularCourse getCurricularCourse(IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
			ICurricularRule curricularRule) {

		if (sourceDegreeModuleToEvaluate.isOptional()) {
			return ((OptionalDegreeModuleToEnrol) sourceDegreeModuleToEvaluate).getCurricularCourse();
		} else {
			return (CurricularCourse) curricularRule.getDegreeModuleToApplyRule();
		}
	}

	private Collection<CompetenceCourse> getCompetenceCoursesAvailableToEnrol(final EnrolmentContext context) {
		return context.getRegistration().getPhdIndividualProgramProcess().getCompetenceCoursesAvailableToEnrol();
	}

	@Override
	protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(ICurricularRule curricularRule,
			IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
		return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
	}

}
