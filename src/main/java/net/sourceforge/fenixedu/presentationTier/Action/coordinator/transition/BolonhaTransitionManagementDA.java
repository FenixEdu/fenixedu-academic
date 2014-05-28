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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.transition;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.transition.AbstractBolonhaTransitionManagementDA;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
