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
package org.fenixedu.academic.ui.struts.action.person;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.ui.struts.action.person.PersonApplication.PersonalAreaApp;
import org.fenixedu.academic.ui.struts.action.person.UpdateEmergencyContactDA.EmergencyContactBean;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.NotificationPlug;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@StrutsFunctionality(app = PersonalAreaApp.class, descriptionKey = "label.person.visualizeInformation", path = "information",
        titleKey = "label.person.visualizeInformation")
@Mapping(path = "/visualizePersonalInfo", module = "person", parameter = "/person/visualizePersonalInfo.jsp")
public class VisualizePersonalInfo extends ForwardAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final HttpSession httpSession = request.getSession(false);
        final NotificationPlug notificationPlug = NotificationPlug.PLUGS.stream()
                .filter(plug -> plug.showNotification(Authenticate.getUser(), httpSession))
                .findAny().orElse(null);
        if (notificationPlug != null) {
            return new ActionForward(notificationPlug.redirectUrl(httpSession), true);
        }

        PersonBean person = new PersonBean(getLoggedUser());
        EmergencyContactBean emergencyContactBean = new EmergencyContactBean(getLoggedUser());
        request.setAttribute("personBean", person);
        request.setAttribute("emergencyContactBean", emergencyContactBean);
        return super.execute(mapping, form, request, response);
    }

    private Person getLoggedUser() {
        User user = Authenticate.getUser();
        return (user == null) ? null : user.getPerson();
    }
}
