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
/*
 * Created on 2004/04/14
 *
 */
package org.fenixedu.academic.service.services.student;

import static org.fenixedu.academic.predicate.AccessControl.check;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.InfoStudentCurricularPlan;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 * 
 */
public class ReadActiveStudentCurricularPlanByDegreeType {

    @Atomic
    public static InfoStudentCurricularPlan run(User userView, DegreeType degreeType) {
        check(RolePredicates.STUDENT_PREDICATE);

        final Person person = userView.getPerson();
        final Registration registration = person.getStudentByType(degreeType);

        if (registration != null) {
            final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
            if (studentCurricularPlan != null) {
                final InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan(studentCurricularPlan);
                return infoStudentCurricularPlan;
            }
        }

        return null;
    }

}