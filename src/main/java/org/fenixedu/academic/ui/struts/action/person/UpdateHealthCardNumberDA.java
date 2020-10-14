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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.person.UpdateEmergencyContactDA.EmergencyContactBean;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(module = "person", path = "/updateHealthCardNumber", functionality = VisualizePersonalInfo.class)
@Forwards({ @Forward(name = "visualizePersonalInformation", path = "/person/visualizePersonalInfo.jsp") })
public class UpdateHealthCardNumberDA extends FenixDispatchAction {

    public ActionForward updateHealthCardNumber(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        PersonBean personBean = getRenderedObject("healthCardNumber");
        Person person = getLoggedPerson(request);
        EmergencyContactBean emergencyContactBean = new EmergencyContactBean(person);

        try {
            atomic(() -> {
                person.setHealthCardNumber(personBean.getHealthCardNumber());
            });
        } catch (DomainException e) {
            addActionMessage(request, e.getKey());
            request.setAttribute("personBean", new PersonBean(person));
            request.setAttribute("emergencyContactBean", new EmergencyContactBean(person));
        } catch (BennuCoreDomainException be) {
            addActionMessage(request, be.getLocalizedMessage(), false);
            request.setAttribute("personBean", new PersonBean(person));
            request.setAttribute("emergencyContactBean", new EmergencyContactBean(person));

        }

        request.setAttribute("personBean", personBean);
        request.setAttribute("emergencyContactBean", emergencyContactBean);
        return mapping.findForward("visualizePersonalInformation");
    }
}
