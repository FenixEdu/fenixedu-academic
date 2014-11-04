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

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Photograph;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.person.UpdateEmergencyContactDA.EmergencyContactBean;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

/**
 * 
 * @author Gil Lacerda (gil.lacerda@ist.utl.pt)
 * 
 */

@Mapping(path = "/photoHistory", module = "person", functionality = VisualizePersonalInfo.class)
@Forwards({ @Forward(name = "userHistory", path = "/person/visualizePhotoHistory.jsp"),
        @Forward(name = "visualizePersonalInformation", path = "/person/visualizePersonalInfo.jsp") })
public class PhotoHistoryDA extends FenixDispatchAction {

    public ActionForward userHistory(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getUserView(request).getPerson();
        List<Photograph> photoHistory = person.getPhotographHistory();
        // most recent photos first
        Collections.reverse(photoHistory);
        request.setAttribute("history", photoHistory);
        return mapping.findForward("userHistory");
    }

    public ActionForward backToShowInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final Person person = Authenticate.getUser().getPerson();
        request.setAttribute("personBean", new PersonBean(person));
        EmergencyContactBean emergencyContactBean = new EmergencyContactBean(person);
        request.setAttribute("emergencyContactBean", emergencyContactBean);
        return mapping.findForward("visualizePersonalInformation");
    }

}
