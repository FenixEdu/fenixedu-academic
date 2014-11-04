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

import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.RestrictionNotEnroledDegreeModule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;

public class RestrictionNotEnroledDegreeModuleVerifier extends RestrictionEnroledDegreeModuleVerifier {

    @Override
    protected RuleResult verifyEnrolmentWithRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
            DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {

        final RestrictionNotEnroledDegreeModule restrictionNotEnroledDegreeModule =
                (RestrictionNotEnroledDegreeModule) curricularRule;

        if (isApproved(enrolmentContext, restrictionNotEnroledDegreeModule.getPrecedenceDegreeModule(), parentCourseGroup)) {
            return RuleResult.createFalse(degreeModuleToVerify);
        }

        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext
                .getAllChildDegreeModulesToEvaluateFor(parentCourseGroup)) {

            if (degreeModuleToEvaluate.isLeaf()
                    && degreeModuleToEvaluate.isFor(restrictionNotEnroledDegreeModule.getPrecedenceDegreeModule())) {
                return RuleResult.createFalse(degreeModuleToVerify);
            }
        }
        return RuleResult.createTrue(degreeModuleToVerify);
    }

    @Override
    protected RuleResult verifyEnrolmentWithTemporaryEnrolment(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
            DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
        return verifyEnrolmentWithRules(curricularRule, enrolmentContext, degreeModuleToVerify, parentCourseGroup);
    }
}
