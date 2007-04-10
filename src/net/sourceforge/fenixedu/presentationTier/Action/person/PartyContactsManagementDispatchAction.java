package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PartyContactsManagementDispatchAction extends FenixDispatchAction {
    
    public ActionForward prepareCreatePhysicalAddress(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreatePartyContact(mapping, actionForm, request, response, PhysicalAddress.class);
    }
    
    public ActionForward prepareCreatePhone(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreatePartyContact(mapping, actionForm, request, response, Phone.class);
    }
    
    public ActionForward prepareCreateMobilePhone(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreatePartyContact(mapping, actionForm, request, response, MobilePhone.class);
    }
    
    public ActionForward prepareCreateEmailAddress(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreatePartyContact(mapping, actionForm, request, response, EmailAddress.class);
    }
    
    public ActionForward prepareCreateWebAddress(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreatePartyContact(mapping, actionForm, request, response, WebAddress.class);
    }
    
    private ActionForward prepareCreatePartyContact(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response, Class<? extends PartyContact> clazz) {
	
	request.setAttribute("person", getPerson(request));
	request.setAttribute("partyContactName", clazz.getSimpleName());
	
	return mapping.findForward("createPartyContact");
    }
    
    public ActionForward createPartyContact(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return backToShowInformation(mapping, actionForm, request, response);
    }
    
    public ActionForward prepareEditPartyContact(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	
	request.setAttribute("person", getPerson(request));
	request.setAttribute("partyContact", getPartyContact(getPerson(request), request));
	return mapping.findForward("editPartyContact");
    }

    private PartyContact getPartyContact(final Person person, final HttpServletRequest request) {
	final Integer contactId = getIntegerFromRequest(request, "contactId");
	for (final PartyContact contact : person.getPartyContacts()) {
	    if (contact.getIdInternal().equals(contactId)) {
		return contact;
	    }
	}
	return null;
    }
    
    public ActionForward editPartyContact(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return backToShowInformation(mapping, actionForm, request, response);
    }
    
    public ActionForward deletePartyContact(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	try {
	    executeService("DeletePartyContact", new Object[] {getPartyContact(getPerson(request), request)});
	} catch (DomainException e) {
	    addActionMessage("contacts", request, e.getMessage(), e.getArgs());
	}
	return backToShowInformation(mapping, actionForm, request, response);
    }

    protected Person getPerson(final HttpServletRequest request) {
	return AccessControl.getPerson();
    }
    
    public ActionForward backToShowInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("visualizePersonalInformation");
    }
}
