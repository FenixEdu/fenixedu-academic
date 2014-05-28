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
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentViewApp;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.fenixedu.bennu.portal.servlet.PortalLayoutInjector;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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

        return forwardToShowTimeTable(getRegistration(actionForm, request), mapping, request,
                ExecutionSemester.readActualExecutionSemester());
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

        skipLayoutInjection(request);
        return mapping.findForward("showTimeTable");
    }

    protected void skipLayoutInjection(HttpServletRequest request) {
        PortalLayoutInjector.skipLayoutOn(request);
    }

    private Registration getRegistration(final ActionForm form, final HttpServletRequest request) {
        String registrationId = (String) ((DynaActionForm) form).get("registrationId");
        if (StringUtils.isEmpty(registrationId) && !StringUtils.isEmpty(request.getParameter("registrationId"))) {
            registrationId = request.getParameter("registrationId");
        }
        return FenixFramework.getDomainObject(registrationId);
    }
}