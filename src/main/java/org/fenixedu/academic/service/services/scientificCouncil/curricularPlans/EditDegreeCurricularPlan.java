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
package org.fenixedu.academic.service.services.scientificCouncil.curricularPlans;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.GradeScaleEnum;
import org.fenixedu.academic.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;

import pt.ist.fenixframework.Atomic;

public class EditDegreeCurricularPlan {

    @Atomic
    public static void run(final DegreeCurricularPlan degreeCurricularPlan, final String name, final CurricularStage stage,
            final DegreeCurricularPlanState state, final ExecutionYear executionInterval,
            final AcademicPeriod duration, final Boolean applyPreviousYearsEnrolmentRule) {


        if (degreeCurricularPlan == null) {
            throw new IllegalArgumentException("error.degreeCurricularPlan.no.existing.degreeCurricularPlan");
        }

        degreeCurricularPlan.edit(name, stage, state, executionInterval);
        degreeCurricularPlan.editDuration(duration);
        degreeCurricularPlan.editApplyPreviousYearsEnrolment(applyPreviousYearsEnrolmentRule);
    }

}
