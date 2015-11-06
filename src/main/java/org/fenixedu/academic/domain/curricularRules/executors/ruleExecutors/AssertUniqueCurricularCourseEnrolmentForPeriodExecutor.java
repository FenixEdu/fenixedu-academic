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
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;

public class AssertUniqueCurricularCourseEnrolmentForPeriodExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule rule, IDegreeModuleToEvaluate toEvaluate,
            EnrolmentContext enrolmentContext) {

        if (!rule.appliesToContext(toEvaluate.getContext())) {
            return RuleResult.createNA(toEvaluate.getDegreeModule());
        }

        return checkDuplicateEnrolmentFor(toEvaluate, (CurricularCourse) rule.getDegreeModuleToApplyRule(), enrolmentContext);
    }

    private RuleResult checkDuplicateEnrolmentFor(IDegreeModuleToEvaluate toEvaluate, CurricularCourse curricularCourse,
            EnrolmentContext enrolmentContext) {

        IDegreeModuleToEvaluate found = null;

        for (final IDegreeModuleToEvaluate each : enrolmentContext.getDegreeModulesToEvaluate()) {

            if (!each.isFor(curricularCourse)) {
                continue;
            }

            //We need to check both sides to ensure OptionalCurricularCourses (to enrol and enroled) are always covered.
            //OptionalDegreeModuleToEnrol.getDegreeModule returns optional curricular course context not the selected curricular course
            if (found != null && (found.isFor(each.getDegreeModule()) || each.isFor(found.getDegreeModule()))) {
                if (each.isEnroled()) {
                    return RuleResult
                            .createImpossible(
                                    curricularCourse,
                                    "curricularRules.ruleExecutors.AssertUniqueCurricularCourseEnrolmentForPeriodExecutor.to.enrol.again.previous.enrolments.must.be.flunked",
                                    toEvaluate.getName());
                } else {
                    return RuleResult
                            .createFalse(
                                    curricularCourse,
                                    "curricularRules.ruleExecutors.AssertUniqueCurricularCourseEnrolmentForPeriodExecutor.to.enrol.again.previous.enrolments.must.be.flunked",
                                    toEvaluate.getName());
                }
            } else {
                found = each;
            }
        }
        return RuleResult.createTrue(curricularCourse);
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

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

}
