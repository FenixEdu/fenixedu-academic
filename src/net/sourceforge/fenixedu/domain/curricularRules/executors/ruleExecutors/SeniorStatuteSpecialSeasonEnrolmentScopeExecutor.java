package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.SeniorStatuteSpecialSeasonEnrolmentScope;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.student.Registration;

public class SeniorStatuteSpecialSeasonEnrolmentScopeExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        final SeniorStatuteSpecialSeasonEnrolmentScope seniorStatuteSpecialSeasonEnrolmentScope =
                (SeniorStatuteSpecialSeasonEnrolmentScope) curricularRule;
        final Enrolment enrolment = seniorStatuteSpecialSeasonEnrolmentScope.getEnrolment();
        final Registration registration = seniorStatuteSpecialSeasonEnrolmentScope.getRegistration();
        final DegreeModule degreeModule = enrolment.getDegreeModule();

        if (enrolment.getDegreeCurricularPlanOfDegreeModule() != enrolment.getDegreeCurricularPlanOfStudent()
                && enrolment.getRegistration() == registration) {
            if (enrolmentContext.isResponsiblePersonStudent()) {
                return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.SeniorStatuteSpecialSeasonEnrolmentScope.enrolment.out.of.senior.scope",
                        degreeModule.getName());
            } else {
                return RuleResult.createWarning(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.SeniorStatuteSpecialSeasonEnrolmentScope.enrolment.out.of.senior.scope",
                        degreeModule.getName());
            }

        }

        return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }

}
