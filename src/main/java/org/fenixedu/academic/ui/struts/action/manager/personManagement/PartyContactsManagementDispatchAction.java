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
package org.fenixedu.academic.ui.struts.action.manager.personManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.dto.contacts.PartyContactBean;
import org.fenixedu.academic.service.services.contacts.CreatePartyContact;
import org.fenixedu.academic.service.services.contacts.EditPartyContact;

public class PartyContactsManagementDispatchAction extends
        org.fenixedu.academic.ui.struts.action.person.PartyContactsManagementDispatchAction {

    @Override
    public ActionForward backToShowInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        PartyContactBean contact = getRenderedObject("edit-contact");
        if (contact != null) {
            request.setAttribute("person", contact.getParty());
        } else {
            request.setAttribute("person", getParty(request));
        }

        return mapping.findForward("visualizePersonalInformation");
    }

    @Override
    public boolean editContact(PartyContactBean contact) {
        return EditPartyContact.run(contact, false);
    }

    @Override
    public PartyContact createContact(PartyContactBean contact) {
        return CreatePartyContact.run(contact, false);
    }

    @Override
    protected void addWarningMessage(HttpServletRequest request, PartyContact partyContact) {
    }

    @Override
    protected void addWarningMessage(HttpServletRequest request, PartyContactBean contactBean) {
    }
}
