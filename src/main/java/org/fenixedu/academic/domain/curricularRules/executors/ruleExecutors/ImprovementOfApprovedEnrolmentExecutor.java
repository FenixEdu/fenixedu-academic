/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ImprovementOfApprovedEnrolment;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

public class ImprovementOfApprovedEnrolmentExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        final ImprovementOfApprovedEnrolment improvementOfApprovedEnrolment = (ImprovementOfApprovedEnrolment) curricularRule;
        final Enrolment enrolment = improvementOfApprovedEnrolment.getEnrolment();
        final DegreeModule degreeModule = enrolment.getDegreeModule();

        if (enrolment.hasImprovement()) {
            return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.already.enroled.in.improvement",
                    degreeModule.getName());
        }

        if (!enrolment.isApproved()) {
            return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.degree.module.hasnt.been.approved",
                    degreeModule.getName());
        }

        if (!enrolment.isBolonhaDegree()) {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final ExecutionSemester executionSemester = enrolmentContext.getExecutionPeriod();

        if (!executionSemester.isOneYearAfter(enrolment.getExecutionPeriod())) {
            if (!degreeModule.hasAnyParentContexts(executionSemester)) {
                return RuleResult
                        .createFalse(
                                sourceDegreeModuleToEvaluate.getDegreeModule(),
                                "curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.degree.module.has.no.context.in.present.execution.period",
                                degreeModule.getName(), executionSemester.getQualifiedName());
            }

            if (!enrolment.isImprovingInExecutionPeriodFollowingApproval(executionSemester)) {
                return RuleResult
                        .createFalse(
                                sourceDegreeModuleToEvaluate.getDegreeModule(),
                                "curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.is.not.improving.in.execution.period.following.approval",
                                degreeModule.getName());
            }
        }

        return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }

}
