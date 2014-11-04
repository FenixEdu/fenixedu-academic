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
package org.fenixedu.academic.service.services.manager;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Date;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurricularPlan.StudentCurricularPlanState;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateStudentCurricularPlan {

    @Atomic
    public static void run(final Integer studentNumber, final DegreeType degreeType,
            final StudentCurricularPlanState studentCurricularPlanState, final String degreeCurricularPlanId, final Date startDate)
            throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        final Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber, degreeType);
        if (registration == null) {
            throw new NonExistingServiceException("exception.student.does.not.exist");
        }

        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException("exception.degree.curricular.plan.does.not.exist");
        }

        StudentCurricularPlan
                .createWithEmptyStructure(registration, degreeCurricularPlan, YearMonthDay.fromDateFields(startDate));
    }

}