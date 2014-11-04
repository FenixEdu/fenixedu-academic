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
/*
 * Created on 2004/04/14
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;

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