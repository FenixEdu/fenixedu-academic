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

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

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
