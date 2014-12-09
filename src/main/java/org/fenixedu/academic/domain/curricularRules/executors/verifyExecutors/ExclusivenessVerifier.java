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
package org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.curricularRules.Exclusiveness;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class ExclusivenessVerifier extends VerifyRuleExecutor {

    @Override
    protected RuleResult verifyEnrolmentWithRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
            DegreeModule degreeModuleToVerify, CourseGroup rootOrCycleCourseGroup) {

        final Exclusiveness exclusiveness = (Exclusiveness) curricularRule;
        final DegreeModule exclusiveDegreeModule = exclusiveness.getExclusiveDegreeModule();
        final IDegreeModuleToEvaluate degreeModuleToEvaluate =
                getDegreeModuleToEvaluate(enrolmentContext, exclusiveDegreeModule, rootOrCycleCourseGroup);

        if (degreeModuleToEvaluate != null) {
            if (!degreeModuleToEvaluate.isLeaf()) {
                return RuleResult.createFalse(degreeModuleToVerify);
            }

            final CurricularCourse curricularCourse = (CurricularCourse) exclusiveDegreeModule;
            if (isApproved(enrolmentContext, curricularCourse, rootOrCycleCourseGroup) || degreeModuleToEvaluate.isEnroled()
                    || degreeModuleToEvaluate.isEnroling()) {
                return RuleResult.createFalse(degreeModuleToVerify);
            }
        }

        return RuleResult.createTrue(degreeModuleToVerify);
    }

    @Override
    protected RuleResult verifyEnrolmentWithTemporaryEnrolment(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
            DegreeModule degreeModuleToVerify, CourseGroup rootOrCycleCourseGroup) {

        final Exclusiveness exclusiveness = (Exclusiveness) curricularRule;
        final DegreeModule exclusiveDegreeModule = exclusiveness.getExclusiveDegreeModule();
        final IDegreeModuleToEvaluate degreeModuleToEvaluate =
                getDegreeModuleToEvaluate(enrolmentContext, exclusiveDegreeModule, rootOrCycleCourseGroup);

        if (degreeModuleToEvaluate != null) {
            if (!degreeModuleToEvaluate.isLeaf()) {
                return RuleResult.createFalse(degreeModuleToVerify);
            }

            final CurricularCourse curricularCourse = (CurricularCourse) exclusiveDegreeModule;
            if (isApproved(enrolmentContext, curricularCourse, rootOrCycleCourseGroup)
                    || hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, enrolmentContext.getExecutionPeriod()
                            .getPreviousExecutionPeriod())) {
                return RuleResult.createFalse(degreeModuleToVerify);
            }
        }

        return RuleResult.createTrue(degreeModuleToVerify);
    }

    private IDegreeModuleToEvaluate getDegreeModuleToEvaluate(final EnrolmentContext enrolmentContext,
            final DegreeModule degreeModule, final CourseGroup rootOrCycleCourseGroup) {
        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext
                .getAllChildDegreeModulesToEvaluateFor(rootOrCycleCourseGroup)) {

            if (degreeModuleToEvaluate.isFor(degreeModule)) {
                return degreeModuleToEvaluate;
            }

        }

        return null;

    }

    @Override
    protected RuleResult verifyDegreeConclusionWithRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
            DegreeModule degreeModuleToVerify, CourseGroup rootOrCycleCourseGroup) {

        final Exclusiveness exclusiveness = (Exclusiveness) curricularRule;
        final DegreeModule exclusiveDegreeModule = exclusiveness.getExclusiveDegreeModule();

        if (exclusiveDegreeModule.isCourseGroup()) {
            if (isEnrolledIn(enrolmentContext, (CourseGroup) exclusiveDegreeModule)) {
                return RuleResult.createFalse(degreeModuleToVerify);
            }
        } else if (exclusiveDegreeModule.isCurricularCourse()) {
            if (isApproved(enrolmentContext, (CurricularCourse) exclusiveDegreeModule, rootOrCycleCourseGroup)) {
                return RuleResult.createFalse(degreeModuleToVerify);
            }
        } else {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.invalid.degree.module.to.verify");
        }

        return RuleResult.createTrue(degreeModuleToVerify);
    }
}
