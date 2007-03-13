package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.curricularRules.EnrolmentInSpecialSeasonEvaluation;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

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
	
	return RuleResult.createTrue();
    }

}
