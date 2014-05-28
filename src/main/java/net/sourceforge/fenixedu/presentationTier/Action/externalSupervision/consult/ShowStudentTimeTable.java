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
package net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.student.ViewStudentTimeTable;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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

    @Override
    protected void skipLayoutInjection(HttpServletRequest request) {
        // Don't skip layout injection
    }

}
