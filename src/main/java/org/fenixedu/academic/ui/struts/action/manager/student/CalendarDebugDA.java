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
package org.fenixedu.academic.ui.struts.action.manager.student;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerStudentsApp;
import org.fenixedu.academic.ui.struts.action.student.ICalStudentTimeTable;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

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
                u.getPerson().getStudent().getActiveRegistrationStream().forEach(r -> hm.put(r, content(request, hm, r)));
            }
            request.setAttribute("list", hm);
        } else {
            info = "";
        }

        request.setAttribute("user", info);
        return mapping.findForward("CalendarData");
    }

    private String content(HttpServletRequest request, HashMap<Registration, String> hm, Registration registration) {
        try {
            return "<b>Classes: </b>"
                    + StringEscapeUtils.escapeHtml(ICalStudentTimeTable.getUrl("syncClasses", registration, request)) + "<br/>";
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
