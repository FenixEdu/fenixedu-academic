package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.domain.curricularRules.EnrolmentInSpecialSeasonEvaluation;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.student.Registration;

public class EnrolmentInSpecialSeasonEvaluationExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA();
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA();
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	final EnrolmentInSpecialSeasonEvaluation enrolmentInSpecialSeasonEvaluation = (EnrolmentInSpecialSeasonEvaluation) curricularRule;
	final Enrolment enrolment = enrolmentInSpecialSeasonEvaluation.getEnrolment();
	final DegreeModule degreeModule = enrolment.getDegreeModule();
	
	if (enrolment.hasSpecialSeasonInExecutionYear()) {
	    return RuleResult.createFalse("curricularRules.ruleExecutors.EnrolmentInSpecialSeasonEvaluationExecutor.already.enroled.in.special.season", degreeModule.getName(), enrolment.getExecutionYear().getYear());
	}
	
	if (enrolment.isApproved()) {
	    return RuleResult.createFalse("curricularRules.ruleExecutors.EnrolmentInSpecialSeasonEvaluationExecutor.degree.module.has.been.approved", degreeModule.getName());
	}
	
	final Registration registration = enrolment.getRegistration();
	final ExecutionYear executionYear = enrolment.getExecutionPeriod().getExecutionYear();
	final SpecialSeasonCode specialSeasonCode = registration.getSpecialSeasonCodeByExecutionYear(executionYear);
	if (specialSeasonCode == null) {
	    return RuleResult.createFalse("curricularRules.ruleExecutors.EnrolmentInSpecialSeasonEvaluationExecutor.no.specialSeason.code");
	}

	final Integer maxEnrolments = specialSeasonCode.getMaxEnrolments();
	final int sum = registration.getActiveStudentCurricularPlan().getSpecialSeasonEnrolments(executionYear).size() + enrolmentContext.getDegreeModuleToEvaluate().size();
	
	if (maxEnrolments < sum) {
	    return RuleResult.createFalse("curricularRules.ruleExecutors.EnrolmentInSpecialSeasonEvaluationExecutor.too.many.specialSeason.enrolments", specialSeasonCode.getSituation(), maxEnrolments.toString());
	}
	
	return RuleResult.createTrue();
    }

}
