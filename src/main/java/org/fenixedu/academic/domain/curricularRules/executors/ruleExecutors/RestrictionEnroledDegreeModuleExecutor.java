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

import java.util.Collection;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.RestrictionEnroledDegreeModule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;

public class RestrictionEnroledDegreeModuleExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final RestrictionEnroledDegreeModule rule = (RestrictionEnroledDegreeModule) curricularRule;

        if (!canApplyRule(enrolmentContext, rule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final CurricularCourse curricularCourseToBeEnroled = rule.getPrecedenceDegreeModule();
        if (isApproved(enrolmentContext, curricularCourseToBeEnroled) || isEnroled(enrolmentContext, curricularCourseToBeEnroled)
                || isEnrolling(enrolmentContext, curricularCourseToBeEnroled)) {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        if (isEnroled(enrolmentContext, rule.getDegreeModuleToApplyRule())) {
            return RuleResult
                    .createImpossible(
                            sourceDegreeModuleToEvaluate.getDegreeModule(),
                            "curricularRules.ruleExecutors.RestrictionEnroledDegreeModuleExecutor.student.is.not.enroled.to.precendenceDegreeModule",
                            rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
        }

        return RuleResult
                .createFalse(
                        sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.RestrictionEnroledDegreeModuleExecutor.student.is.not.enroled.to.precendenceDegreeModule",
                        rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {

        RestrictionEnroledDegreeModule restrictionEnroledDegreeModule = (RestrictionEnroledDegreeModule) curricularRule;

        Collection<CycleCourseGroup> cycleCourseGroups =
                restrictionEnroledDegreeModule.getPrecedenceDegreeModule().getParentCycleCourseGroups();
        for (CycleCourseGroup cycleCourseGroup : cycleCourseGroups) {
            CycleCurriculumGroup cycleCurriculumGroup =
                    (CycleCurriculumGroup) enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(cycleCourseGroup);
            if (cycleCurriculumGroup != null) {
                return true;
            }
        }

        return false;
    }

}
