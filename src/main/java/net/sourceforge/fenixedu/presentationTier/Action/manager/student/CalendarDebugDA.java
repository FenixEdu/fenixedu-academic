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
package net.sourceforge.fenixedu.presentationTier.Action.manager.student;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerStudentsApp;
import net.sourceforge.fenixedu.presentationTier.Action.student.ICalStudentTimeTable;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ManagerStudentsApp.class, path = "calendar-debug", titleKey = "title.calendar.debug",
        bundle = "ManagerResources")
@Mapping(path = "/calendarDebug", module = "manager")
@Forwards(@Forward(name = "CalendarData", path = "/manager/calendarData.jsp"))
public class CalendarDebugDA extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String info = request.getParameter("user");
        if (info != null) {
            User u = User.findByUsername(info);
            HashMap<Registration, String> hm = new HashMap<>();
            if (u.getPerson().getStudent() != null) {
                for (Registration registration : u.getPerson().getStudent().getActiveRegistrations()) {
                    hm.put(registration,
                            "<b>Classes: </b>"
                                    + StringEscapeUtils.escapeHtml(ICalStudentTimeTable.getUrl("syncClasses", registration,
                                            request))
                                    + "<br/>"
                                    + "<b>Exams: </b>"
                                    + StringEscapeUtils.escapeHtml(ICalStudentTimeTable
                                            .getUrl("syncExams", registration, request)) + "<br/>");
                }
            }
            request.setAttribute("list", hm);
        } else {
            info = "";
        }

        request.setAttribute("user", info);
        return mapping.findForward("CalendarData");
    }
}
