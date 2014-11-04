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

import org.fenixedu.academic.domain.curricularRules.EvenOddRule;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;

public class EvenOddRuleVerifier extends VerifyRuleExecutor {

    @Override
    protected RuleResult verifyEnrolmentWithRules(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
            DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
        final EvenOddRule evenOddRule = (EvenOddRule) curricularRule;

        if (evenOddRule.getCurricularPeriodOrder().equals(enrolmentContext.getExecutionPeriod().getSemester())) {
            if (evenOddRule.getEven() && ((enrolmentContext.getRegistration().getStudent().getNumber().intValue() & 1) == 0)
                    || !evenOddRule.getEven()
                    && ((enrolmentContext.getRegistration().getStudent().getNumber().intValue() & 1) != 0)) {
                return RuleResult.createTrue(degreeModuleToVerify);
            }

            return RuleResult.createFalse(degreeModuleToVerify);
        } else {
            return RuleResult.createNA(degreeModuleToVerify);
        }
    }

    @Override
    protected RuleResult verifyEnrolmentWithTemporaryEnrolment(ICurricularRule curricularRule, EnrolmentContext enrolmentContext,
            DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
        return verifyEnrolmentWithRules(curricularRule, enrolmentContext, degreeModuleToVerify, parentCourseGroup);
    }

}
