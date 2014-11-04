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
package org.fenixedu.academic.ui.struts.action.coordinator.transition;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.ui.struts.action.commons.transition.AbstractBolonhaTransitionManagementDA;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(module = "coordinator", path = "/bolonhaTransitionManagement", formBean = "bolonhaTransitionManagementForm",
        functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "showStudentCurricularPlan", path = "/coordinator/transition/bolonha/showStudentCurricularPlan.jsp"),
        @Forward(name = "chooseRegistration", path = "/coordinator/transition/bolonha/chooseRegistration.jsp"),
        @Forward(name = "NotAuthorized", path = "/coordinator/student/notAuthorized_bd.jsp") })
public class BolonhaTransitionManagementDA extends AbstractBolonhaTransitionManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected List<Registration> getRegistrations(final HttpServletRequest request) {
        return getStudent(request).getTransitionRegistrationsForDegreeCurricularPlansManagedByCoordinator(
                getLoggedPerson(request));

    }

    private Student getStudent(final HttpServletRequest request) {
        return getDomainObject(request, "studentId");
    }
}
