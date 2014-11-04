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
package org.fenixedu.academic.service.services.scientificCouncil.credits;

import static org.fenixedu.academic.predicate.AccessControl.check;
import org.fenixedu.academic.dto.teacherCredits.TeacherCreditsPeriodBean;
import org.fenixedu.academic.domain.time.calendarStructure.TeacherCreditsFillingCE;
import org.fenixedu.academic.predicate.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class CreateTeacherCreditsFillingPeriod {

    @Atomic
    public static void run(TeacherCreditsPeriodBean bean) {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);
        if (bean != null) {
            if (bean.isTeacher()) {
                TeacherCreditsFillingCE.editTeacherCreditsPeriod(bean.getExecutionPeriod(), bean.getBeginForTeacher(),
                        bean.getEndForTeacher());
            } else {
                TeacherCreditsFillingCE.editDepartmentOfficeCreditsPeriod(bean.getExecutionPeriod(),
                        bean.getBeginForDepartmentAdmOffice(), bean.getEndForDepartmentAdmOffice());
            }
        }
    }
}