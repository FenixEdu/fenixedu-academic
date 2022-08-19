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

import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;

public class PreviousYearsEnrolmentExecutor extends CurricularRuleExecutor {

    private PreviousYearsEnrolmentBySemesterExecutor SEMESTER_EXECUTOR = new PreviousYearsEnrolmentBySemesterExecutor();

    private PreviousYearsEnrolmentByYearExecutor YEAR_EXECUTOR = new PreviousYearsEnrolmentByYearExecutor();

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        if (enrolmentContext.isToEvaluateRulesByYear()) {
            return YEAR_EXECUTOR.executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate,
                    enrolmentContext);
        }

        return SEMESTER_EXECUTOR.executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate,
                enrolmentContext);

    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }
}
