/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.MaximumNumberOfECTSInSpecialSeasonEvaluation;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.student.Registration;

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
/*
        final Registration registration = enrolmentContext.getRegistration();

        final MaximumNumberOfECTSInSpecialSeasonEvaluation rule = (MaximumNumberOfECTSInSpecialSeasonEvaluation) curricularRule;
        final BigDecimal totalEcts = getTotalEcts(registration, enrolmentContext);

        if (!rule.allowEcts(totalEcts)) {
            // a person can not enroll it self if ects are exceeded (usually the students case)
            if (enrolmentContext.isRegistrationFromResponsiblePerson()) {
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
*/
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
