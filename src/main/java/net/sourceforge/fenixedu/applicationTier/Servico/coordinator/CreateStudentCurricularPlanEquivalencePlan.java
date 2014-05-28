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
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import pt.ist.fenixframework.Atomic;

public class CreateStudentCurricularPlanEquivalencePlan {

    @Atomic
    public static StudentCurricularPlanEquivalencePlan run(final Student student) {
        for (final Registration registration : student.getRegistrations()) {
            final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
            if (studentCurricularPlan != null && studentCurricularPlan.isBoxStructure()
                    && !studentCurricularPlan.isBolonhaDegree()) {
                final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan =
                        studentCurricularPlan.getEquivalencePlan();
                return studentCurricularPlanEquivalencePlan == null ? studentCurricularPlan
                        .createStudentCurricularPlanEquivalencePlan() : studentCurricularPlanEquivalencePlan;
            }
        }
        return null;
    }

}