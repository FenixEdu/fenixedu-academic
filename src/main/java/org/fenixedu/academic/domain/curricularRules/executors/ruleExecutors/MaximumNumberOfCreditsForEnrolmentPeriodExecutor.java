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
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.student.Registration;

public class MaximumNumberOfCreditsForEnrolmentPeriodExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final ExecutionInterval executionInterval = enrolmentContext.getExecutionPeriod();

        final StudentCurricularPlan studentCurricularPlan = enrolmentContext.getStudentCurricularPlan();
        final ExecutionYear executionYear = executionInterval.getExecutionYear();
        final Registration registration = studentCurricularPlan.getRegistration();

        double accumulated = 0d;
        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            accumulated += degreeModuleToEvaluate.getAccumulatedEctsCredits(executionInterval);
        }
        final boolean isPartial = registration.isPartialRegime(executionYear);
        if (isPartial) {
            for (final ExecutionInterval interval : executionYear.getChildIntervals()) {
                if (interval != executionInterval) {
                    accumulated += studentCurricularPlan.getRoot().getCurriculumLineStream().filter(cl -> cl.isEnrolment())
                            .map(cl -> (Enrolment) cl).filter(e -> e.getExecutionInterval() == interval)
                            .filter(e -> !e.isAnnulled()).mapToDouble(e -> e.getEctsCredits()).sum();
                }
            }
        }

        final double maxEcts = MaximumNumberOfCreditsForEnrolmentPeriod.MAXIMUM_NUMBER_OF_CREDITS;

        if (accumulated > maxEcts) {
            if (sourceDegreeModuleToEvaluate.isEnroled()) {
                return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.MaximumNumberOfCreditsForEnrolmentPeriodExecutor", String.valueOf(maxEcts),
                        String.valueOf(accumulated));

            } else {
                return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.MaximumNumberOfCreditsForEnrolmentPeriodExecutor", String.valueOf(maxEcts),
                        String.valueOf(accumulated));
            }
        }

        return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }

}
