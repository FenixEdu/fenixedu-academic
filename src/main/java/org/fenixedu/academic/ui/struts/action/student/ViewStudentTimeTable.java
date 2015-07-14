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
package org.fenixedu.academic.ui.struts.action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.InfoShowOccupation;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.student.ReadStudentTimeTable;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentViewApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author naat
 * @author zenida
 * 
 */
@StrutsFunctionality(app = StudentViewApp.class, path = "time-table", titleKey = "link.my.timetable")
@Mapping(module = "student", path = "/studentTimeTable", input = "/studentTimeTable.do?method=prepare",
        formBean = "studentTimeTableForm")
@Forwards(value = { @Forward(name = "showTimeTable", path = "/commons/student/timeTable/classTimeTable.jsp"),
        @Forward(name = "chooseRegistration", path = "/student/timeTable/chooseRegistration.jsp") })
public class ViewStudentTimeTable extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        List<Registration> registrations = getUserView(request).getPerson().getStudent().getActiveRegistrations();
        if (registrations.size() == 1) {
            return forwardToShowTimeTable(registrations.get(0), mapping, request, ExecutionSemester.readActualExecutionSemester());
        } else {
            request.setAttribute("registrations", registrations);
            return mapping.findForward("chooseRegistration");
        }
    }

    public ActionForward showTimeTable(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterID");
        if (executionSemester == null) {
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        }
        return forwardToShowTimeTable(getRegistration(actionForm, request), mapping, request, executionSemester);
    }

    protected ActionForward forwardToShowTimeTableForSupervisor(Registration registration, ActionMapping mapping,
            HttpServletRequest request) throws FenixActionException, FenixServiceException {

        return forwardToShowTimeTable(registration, mapping, request, ExecutionSemester.readActualExecutionSemester());
    }

    public ActionForward forwardToShowTimeTable(Registration registration, ActionMapping mapping, HttpServletRequest request,
            ExecutionSemester executionSemester) throws FenixActionException, FenixServiceException {
        List<InfoShowOccupation> infoLessons = ReadStudentTimeTable.run(registration, executionSemester);

        request.setAttribute("person", registration.getPerson());
        request.setAttribute("infoLessons", infoLessons);
        request.setAttribute("registrationId", registration.getExternalId());
        request.setAttribute("executionSemesterId", executionSemester.getExternalId());

        return mapping.findForward("showTimeTable");
    }

    private Registration getRegistration(final ActionForm form, final HttpServletRequest request) {
        String registrationId = (String) ((DynaActionForm) form).get("registrationId");
        if (StringUtils.isEmpty(registrationId) && !StringUtils.isEmpty(request.getParameter("registrationId"))) {
            registrationId = request.getParameter("registrationId");
        }
        return FenixFramework.getDomainObject(registrationId);
    }
}