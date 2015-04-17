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
package org.fenixedu.academic.ui.struts.action.externalSupervision.consult;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.student.ViewStudentTimeTable;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/viewTimetable", module = "externalSupervision", formBean = "studentTimeTableForm",
        functionality = ExternalSupervisorViewStudentDA.class)
@Forwards({ @Forward(name = "chooseRegistration", path = "/student/timeTable/chooseRegistration.jsp"),
        @Forward(name = "showTimeTable", path = "/externalSupervision/consult/showTimetable.jsp") })
public class ShowStudentTimeTable extends ViewStudentTimeTable {

    public ActionForward prepareForSupervisor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        final String personId = request.getParameter("personId");
        Person person = FenixFramework.getDomainObject(personId);
        final Student student = person.getStudent();

        request.setAttribute("student", student);

        List<Registration> registrations = person.getStudent().getActiveRegistrations();
        if (registrations.size() == 0) {
            return forwardToShowTimeTableForSupervisor(person.getStudent().getLastRegistration(), mapping, request);
        } else if (registrations.size() == 1) {
            return forwardToShowTimeTableForSupervisor(registrations.iterator().next(), mapping, request);
        } else {
            request.setAttribute("registrations", registrations);
            return mapping.findForward("chooseRegistration");
        }
    }

}
