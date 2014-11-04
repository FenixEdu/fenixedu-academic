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

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;

public class AssertUniqueApprovalInCurricularCourseContextsExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {

        final CurricularCourse curricularCourse = (CurricularCourse) curricularRule.getDegreeModuleToApplyRule();
        final ExecutionSemester executionSemester = enrolmentContext.getExecutionPeriod();

        if (!curricularCourse.hasAnyActiveContext(enrolmentContext.getExecutionPeriod())) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        if (isApproved(enrolmentContext, curricularCourse, executionSemester.getPreviousExecutionPeriod())) {
            if (sourceDegreeModuleToEvaluate.isEnroled()) {
                return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.AssertUniqueApprovalInCurricularCourseContextsExecutor.already.approved",
                        curricularCourse.getName());
            } else {
                return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.AssertUniqueApprovalInCurricularCourseContextsExecutor.already.approved",
                        curricularCourse.getName());
            }

        } else {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        final CurricularCourse curricularCourse = (CurricularCourse) curricularRule.getDegreeModuleToApplyRule();
        final ExecutionSemester executionSemester = enrolmentContext.getExecutionPeriod();

        if (!curricularCourse.hasAnyActiveContext(enrolmentContext.getExecutionPeriod())) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        if (isApproved(enrolmentContext, curricularCourse, executionSemester.getPreviousExecutionPeriod())) {
            if (sourceDegreeModuleToEvaluate.isEnroled()) {
                return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.AssertUniqueApprovalInCurricularCourseContextsExecutor.already.approved",
                        curricularCourse.getName());
            } else {
                return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.AssertUniqueApprovalInCurricularCourseContextsExecutor.already.approved",
                        curricularCourse.getName());
            }

        } else if (hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse,
                executionSemester.getPreviousExecutionPeriod())) {
            return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule());

        } else {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }

}
