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

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.SeniorStatuteSpecialSeasonEnrolmentScope;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.student.Registration;

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
