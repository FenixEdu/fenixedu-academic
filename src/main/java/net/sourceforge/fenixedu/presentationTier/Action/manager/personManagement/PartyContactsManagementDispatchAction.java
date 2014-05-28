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
package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.contacts.CreatePartyContact;
import net.sourceforge.fenixedu.applicationTier.Servico.contacts.EditPartyContact;
import net.sourceforge.fenixedu.dataTransferObject.contacts.PartyContactBean;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PartyContactsManagementDispatchAction extends
        net.sourceforge.fenixedu.presentationTier.Action.person.PartyContactsManagementDispatchAction {

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
