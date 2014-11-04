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
package org.fenixedu.academic.service.services.phd;

import java.util.Collection;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCourses;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CreateEnrolmentPeriods {

    @Atomic
    static public void create(final Collection<DegreeCurricularPlan> degreeCurricularPlans, final ExecutionSemester semester,
            final DateTime startDate, final DateTime endDate) {

        for (final DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            new EnrolmentPeriodInCurricularCourses(degreeCurricularPlan, semester, startDate, endDate);
        }

    }
}
