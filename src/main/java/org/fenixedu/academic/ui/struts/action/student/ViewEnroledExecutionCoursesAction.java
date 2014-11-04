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
package org.fenixedu.academic.ui.struts.action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.student.ReadEnroledExecutionCourses;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentEnrollApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = StudentEnrollApp.class, path = "groups", titleKey = "link.groupEnrolment")
@Mapping(module = "student", path = "/viewEnroledExecutionCourses")
@Forwards({ @Forward(name = "showEnroledExecutionCourses", path = "/student/viewEnroledExecutionCourses_bd.jsp"),
        @Forward(name = "showActiveRegistrations", path = "/student/viewActiveRegistrations.jsp") })
public class ViewEnroledExecutionCoursesAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final Student student = getLoggedPerson(request).getStudent();
        final List<Registration> registrations = student.getActiveRegistrations();

        if (registrations.size() == 1) {
            request.setAttribute("executionCourses", ReadEnroledExecutionCourses.run(registrations.iterator().next()));
            return mapping.findForward("showEnroledExecutionCourses");

        } else {
            request.setAttribute("registrations", registrations);
            return mapping.findForward("showActiveRegistrations");
        }
    }

    public ActionForward select(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final Registration registration =
                getRegistrationByID(getLoggedPerson(request).getStudent(), getStringFromRequest(request, "registrationId"));
        request.setAttribute("executionCourses", ReadEnroledExecutionCourses.run(registration));
        return mapping.findForward("showEnroledExecutionCourses");
    }

    private Registration getRegistrationByID(final Student student, final String registrationId) {
        for (final Registration registration : student.getActiveRegistrations()) {
            if (registration.getExternalId().equals(registrationId)) {
                return registration;
            }
        }
        return null;
    }
}