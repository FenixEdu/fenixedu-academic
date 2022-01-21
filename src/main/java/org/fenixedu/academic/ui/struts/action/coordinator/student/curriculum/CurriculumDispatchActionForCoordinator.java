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
package org.fenixedu.academic.ui.struts.action.coordinator.student.curriculum;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.CurriculumDispatchAction;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/viewStudentCurriculum", module = "coordinator", formBean = "studentCurricularPlanAndEnrollmentsSelectionForm",
        functionality = DegreeCoordinatorIndex.class)
@Forwards({
        @Forward(name = "ShowStudentCurricularPlans", path = "/coordinator/curriculum/viewCurricularPlans_bd.jsp"),
        @Forward(name = "ShowStudentCurriculum", path = "/coordinator/student/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "ShowStudentCurriculumForCoordinator",
                path = "/coordinator/student/curriculum/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "NotAuthorized", path = "/coordinator/student/notAuthorized_bd.jsp") })
public class CurriculumDispatchActionForCoordinator extends CurriculumDispatchAction {

    @Override
    protected Registration getStudentRegistration(Student student, String degreeCurricularPlanId) {
        Registration registration = null;
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
        registration = student.readRegistrationByDegreeCurricularPlan(degreeCurricularPlan);
        if (registration == null) {
            for (final Registration r : student.getRegistrationsSet()) {
                if (r.getDegree().isEmpty()) {
                    registration = r;
                    break;
                }
            }
        }
        return registration;
    }
}
