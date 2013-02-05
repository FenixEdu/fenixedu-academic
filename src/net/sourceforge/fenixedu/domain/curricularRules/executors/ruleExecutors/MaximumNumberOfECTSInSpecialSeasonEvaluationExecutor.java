package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfECTSInSpecialSeasonEvaluation;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.student.Registration;

public class MaximumNumberOfECTSInSpecialSeasonEvaluationExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        final Registration registration = enrolmentContext.getRegistration();

        final MaximumNumberOfECTSInSpecialSeasonEvaluation rule = (MaximumNumberOfECTSInSpecialSeasonEvaluation) curricularRule;
        final BigDecimal totalEcts = getTotalEcts(registration, enrolmentContext);

        if (!rule.allowEcts(totalEcts)) {
            // is student enroling, then return false
            if (enrolmentContext.isResponsiblePersonStudent()) {
                return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.EnrolmentInSpecialSeasonEvaluationExecutor.too.many.specialSeason.ects",
                        rule.getMaxEcts().toPlainString());

            } else {
                // otherwise add warning, but let enrolment continue
                return RuleResult.createWarning(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.EnrolmentInSpecialSeasonEvaluationExecutor.too.many.specialSeason.ects",
                        rule.getMaxEcts().toPlainString());
            }
        }

        return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    private BigDecimal getTotalEcts(Registration registration, EnrolmentContext enrolmentContext) {
        BigDecimal result = BigDecimal.ZERO;
        for (IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            final CurricularCourse curricularCourse = (CurricularCourse) degreeModuleToEvaluate.getDegreeModule();
            if (enrolmentContext.isResponsiblePersonStudent() || !curricularCourse.isDissertation()) {
                result = result.add(BigDecimal.valueOf(degreeModuleToEvaluate.getEctsCredits()));
            }
        }
        return result;
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }

}
