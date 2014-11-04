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
package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.person.PersonApplication.PersonalAreaApp;
import net.sourceforge.fenixedu.presentationTier.Action.person.UpdateEmergencyContactDA.EmergencyContactBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = PersonalAreaApp.class, descriptionKey = "label.person.visualizeInformation", path = "information",
        titleKey = "label.person.visualizeInformation")
@Mapping(path = "/visualizePersonalInfo", module = "person", parameter = "/person/visualizePersonalInfo.jsp")
public class VisualizePersonalInfo extends ForwardAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
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
