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
package org.fenixedu.academic.ui.struts.action.manager.personManagement.personnelSection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.ui.struts.action.manager.personManagement.PartyContactsManagementDispatchAction;
import org.fenixedu.academic.ui.struts.action.personnelSection.PersonManagementAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "personnelSection", path = "/partyContacts", functionality = PersonManagementAction.class)
@Forwards({ @Forward(name = "visualizePersonalInformation", path = "/personnelSection/people/viewPerson.jsp"),
        @Forward(name = "editPartyContact", path = "/manager/personManagement/contacts/editPartyContact.jsp"),
        @Forward(name = "createPartyContact", path = "/manager/personManagement/contacts/createPartyContact.jsp") })
public class PartyContactsManagementDispatchActionForPersonnelSection extends PartyContactsManagementDispatchAction {

    @Override
    public ActionForward forwardToInputValidationCode(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, PartyContact partyContact) {
        return backToShowInformation(mapping, actionForm, request, response);
    }

    @Override
    protected Party getParty(HttpServletRequest request) {
        final String personID = (String) getFromRequest(request, "personID");
        return FenixFramework.getDomainObject(personID);
    }

}