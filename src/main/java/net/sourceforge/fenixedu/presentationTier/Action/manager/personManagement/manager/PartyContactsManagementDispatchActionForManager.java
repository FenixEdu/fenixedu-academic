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
package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactValidation;
import net.sourceforge.fenixedu.domain.contacts.PartyContactValidationState;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.FindPersonAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.PartyContactsManagementDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "manager", path = "/partyContacts", functionality = FindPersonAction.class)
@Forwards({ @Forward(name = "visualizePersonalInformation", path = "/manager/personManagement/viewPerson.jsp"),
        @Forward(name = "editPartyContact", path = "/manager/personManagement/contacts/editPartyContact.jsp"),
        @Forward(name = "createPartyContact", path = "/manager/personManagement/contacts/createPartyContact.jsp") })
public class PartyContactsManagementDispatchActionForManager extends PartyContactsManagementDispatchAction {

    @Override
    protected Party getParty(HttpServletRequest request) {
        final String personID = (String) getFromRequest(request, "personID");
        return FenixFramework.getDomainObject(personID);
    }

    public ActionForward validate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final String partyContactValidationExtId = (String) getFromRequest(request, "partyContactValidation");
        final PartyContactValidation partyContactValidation = FenixFramework.getDomainObject(partyContactValidationExtId);
        partyContactValidation.setState(PartyContactValidationState.VALID);
        request.setAttribute("personID", partyContactValidation.getPartyContact().getParty().getExternalId());
        return backToShowInformation(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward forwardToInputValidationCode(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, PartyContact partyContact) {
        request.setAttribute("personID", partyContact.getParty().getExternalId());
        return backToShowInformation(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward backToShowInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return new ActionForward("redirectToShowInformation", "/findPerson.do?method=viewPerson&personID="
                + getFromRequest(request, "personID"), false, "/manager");
    }

    public ActionForward resetValidationRequests(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final String personID = (String) getFromRequest(request, "personID");
        Person person = FenixFramework.getDomainObject(personID);
        if (person != null) {
            person.setNumberOfValidationRequests(0);
        }
        return backToShowInformation(mapping, actionForm, request, response);
    }

}