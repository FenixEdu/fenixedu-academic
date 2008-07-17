package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.contacts.EmailAddressBean;
import net.sourceforge.fenixedu.dataTransferObject.contacts.MobilePhoneBean;
import net.sourceforge.fenixedu.dataTransferObject.contacts.PartyContactBean;
import net.sourceforge.fenixedu.dataTransferObject.contacts.PhoneBean;
import net.sourceforge.fenixedu.dataTransferObject.contacts.WebAddressBean;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class PartyContactsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward postbackSetPublic(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	PartyContactBean contact = (PartyContactBean) getRenderedObject("edit-contact");
	RenderUtils.invalidateViewState();
	if (contact.getVisibleToPublic().booleanValue()) {
	    contact.setVisibleToStudents(Boolean.TRUE);
	    contact.setVisibleToTeachers(Boolean.TRUE);
	    contact.setVisibleToEmployees(Boolean.TRUE);
	}
	contact.setVisibleToManagement(Boolean.TRUE);
	request.setAttribute("partyContact", contact);
	request.setAttribute("partyContactClass", contact.getContactName());
	return backToEditOrCreate(mapping, actionForm, request, response);
    }

    public ActionForward postbackSetElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	PartyContactBean contact = (PartyContactBean) getRenderedObject("edit-contact");
	RenderUtils.invalidateViewState();
	if (contact.getVisibleToPublic().booleanValue())
	    contact.setVisibleToPublic(new Boolean(contact.getVisibleToStudents().booleanValue()
		    && contact.getVisibleToTeachers().booleanValue() && contact.getVisibleToEmployees().booleanValue()));
	contact.setVisibleToManagement(Boolean.TRUE);
	request.setAttribute("partyContact", contact);
	request.setAttribute("partyContactClass", contact.getContactName());
	return backToEditOrCreate(mapping, actionForm, request, response);
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Object rendered = getRenderedObject("edit-contact");
	if (rendered instanceof PartyContactBean) {
	    PartyContactBean contact = (PartyContactBean) rendered;
	    contact.setVisibleToManagement(Boolean.TRUE);
	    request.setAttribute("partyContact", contact);
	    request.setAttribute("partyContactClass", contact.getContactName());
	} else if (rendered instanceof PhysicalAddress) {
	    PhysicalAddress contact = (PhysicalAddress) rendered;
	    request.setAttribute("partyContact", contact);
	    request.setAttribute("partyContactClass", contact.getClass().getSimpleName());
	}
	return backToEditOrCreate(mapping, actionForm, request, response);
    }

    private ActionForward backToEditOrCreate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	if (request.getParameter("form").equals("create"))
	    return mapping.findForward("createPartyContact");
	else if (request.getParameter("form").equals("edit"))
	    return mapping.findForward("editPartyContact");
	else
	    return null;
    }

    public ActionForward prepareCreatePhysicalAddress(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareCreatePartyContact(mapping, actionForm, request, response, PhysicalAddress.class.getSimpleName());
    }

    public ActionForward prepareCreatePhone(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	PhoneBean bean = new PhoneBean(getParty(request));
	request.setAttribute("partyContact", bean);
	return prepareCreatePartyContact(mapping, actionForm, request, response, bean.getContactName());
    }

    public ActionForward prepareCreateMobilePhone(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	MobilePhoneBean bean = new MobilePhoneBean(getParty(request));
	request.setAttribute("partyContact", bean);
	return prepareCreatePartyContact(mapping, actionForm, request, response, bean.getContactName());
    }

    public ActionForward prepareCreateEmailAddress(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	EmailAddressBean bean = new EmailAddressBean(getParty(request));
	request.setAttribute("partyContact", bean);
	return prepareCreatePartyContact(mapping, actionForm, request, response, bean.getContactName());
    }

    public ActionForward prepareCreateWebAddress(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	WebAddressBean bean = new WebAddressBean(getParty(request));
	request.setAttribute("partyContact", bean);
	return prepareCreatePartyContact(mapping, actionForm, request, response, bean.getContactName());
    }

    private ActionForward prepareCreatePartyContact(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, String className) {
	request.setAttribute("person", getParty(request));
	request.setAttribute("partyContactClass", className);
	return mapping.findForward("createPartyContact");
    }

    public ActionForward createPartyContact(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	if (getRenderedObject() instanceof PartyContactBean) {
	    PartyContactBean contact = (PartyContactBean) getRenderedObject("edit-contact");
	    try {
		executeService("CreatePartyContact", contact);
	    } catch (DomainException e) {
		addActionMessage("contacts", request, e.getMessage(), e.getArgs());
	    }
	}
	return backToShowInformation(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditPartyContact(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	PartyContact contact = getPartyContact(getParty(request), request);
	if (contact instanceof PhysicalAddress) {
	    request.setAttribute("partyContact", contact);
	    request.setAttribute("partyContactClass", contact.getClass().getSimpleName());
	} else {
	    PartyContactBean contactBean = PartyContactBean.createFromDomain(contact);
	    request.setAttribute("partyContact", contactBean);
	    request.setAttribute("partyContactClass", contactBean.getContactName());
	}
	return mapping.findForward("editPartyContact");
    }

    private PartyContact getPartyContact(final Party party, final HttpServletRequest request) {
	final Integer contactId = getIntegerFromRequest(request, "contactId");
	for (final PartyContact contact : party.getPartyContacts()) {
	    if (contact.getIdInternal().equals(contactId)) {
		return contact;
	    }
	}
	return null;
    }

    public ActionForward editPartyContact(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	if (getRenderedObject() instanceof PartyContactBean) {
	    PartyContactBean contact = (PartyContactBean) getRenderedObject("edit-contact");
	    try {
		executeService("EditPartyContact", contact);
	    } catch (DomainException e) {
		addActionMessage("contacts", request, e.getMessage(), e.getArgs());
	    }
	}
	return backToShowInformation(mapping, actionForm, request, response);
    }

    public ActionForward deletePartyContact(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeService("DeletePartyContact", new Object[] { getPartyContact(getParty(request), request) });
	} catch (DomainException e) {
	    addActionMessage("contacts", request, e.getMessage(), e.getArgs());
	}
	return backToShowInformation(mapping, actionForm, request, response);
    }

    protected Party getParty(final HttpServletRequest request) {
	return AccessControl.getPerson();
    }

    public ActionForward backToShowInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("visualizePersonalInformation");
    }
}
