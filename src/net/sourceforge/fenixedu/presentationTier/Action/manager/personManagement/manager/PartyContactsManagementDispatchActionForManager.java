package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactValidation;
import net.sourceforge.fenixedu.domain.contacts.PartyContactValidationState;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/partyContacts", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "visualizePersonalInformation", path = "/manager/personManagement/viewPerson.jsp"),
	@Forward(name = "editPartyContact", path = "/manager/personManagement/contacts/editPartyContact.jsp"),
	@Forward(name = "createPartyContact", path = "/manager/personManagement/contacts/createPartyContact.jsp") })
public class PartyContactsManagementDispatchActionForManager extends
	net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.PartyContactsManagementDispatchAction {

    @Override
    protected Party getParty(HttpServletRequest request) {
	final String personID = (String) getFromRequest(request, "personID");
	return Party.fromExternalId(personID);
    }

    public ActionForward validate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final String partyContactValidationExtId = (String) getFromRequest(request, "partyContactValidation");
	final PartyContactValidation partyContactValidation = PartyContactValidation.fromExternalId(partyContactValidationExtId);
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
	Person person = Person.fromExternalId(personID);
	if (person != null) {
	    person.setNumberOfValidationRequests(0);
	}
	return backToShowInformation(mapping, actionForm, request, response);
    }

}