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
package net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.EvenOddRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

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
