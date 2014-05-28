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
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.teacherCredits.TeacherCreditsPeriodBean;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class CreateTeacherCreditsFillingPeriod {

    @Atomic
    public static void run(TeacherCreditsPeriodBean bean) {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);
        if (bean != null) {
            if (bean.isTeacher()) {
                bean.getExecutionPeriod().editTeacherCreditsPeriod(bean.getBeginForTeacher(), bean.getEndForTeacher());
            } else {
                bean.getExecutionPeriod().editDepartmentOfficeCreditsPeriod(bean.getBeginForDepartmentAdmOffice(),
                        bean.getEndForDepartmentAdmOffice());
            }
        }
    }
}