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

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PersonInformationLog;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.PartyContactValidation;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.WebAddress;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.dto.contacts.EmailAddressBean;
import org.fenixedu.academic.dto.contacts.MobilePhoneBean;
import org.fenixedu.academic.dto.contacts.PartyContactBean;
import org.fenixedu.academic.dto.contacts.PhoneBean;
import org.fenixedu.academic.dto.contacts.PhysicalAddressBean;
import org.fenixedu.academic.dto.contacts.PhysicalAddressValidationBean;
import org.fenixedu.academic.dto.contacts.WebAddressBean;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.contacts.CreatePartyContact;
import org.fenixedu.academic.service.services.contacts.DeletePartyContact;
import org.fenixedu.academic.service.services.contacts.EditPartyContact;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.person.UpdateEmergencyContactDA.EmergencyContactBean;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Mapping(module = "person", path = "/partyContacts", functionality = VisualizePersonalInfo.class)
@Forwards({ @Forward(name = "visualizePersonalInformation", path = "/person/visualizePersonalInfo.jsp"),
        @Forward(name = "editPartyContact", path = "/person/contacts/editPartyContact.jsp"),
        @Forward(name = "createPartyContact", path = "/person/contacts/createPartyContact.jsp"),
        @Forward(name = "inputValidationCode", path = "/person/contacts/inputValidationCode.jsp"),
        @Forward(name = "viewStudentLogChanges", path = "/person/contacts/viewStudentLogChanges.jsp") })
public class PartyContactsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward postbackSetPublic(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        PartyContactBean contact = getRenderedObject("edit-contact");
        RenderUtils.invalidateViewState();
        if (contact.getVisibleToPublic().booleanValue()) {
            contact.setVisibleToStudents(Boolean.TRUE);
            contact.setVisibleToStaff(Boolean.TRUE);
        }
        contact.setVisibleToManagement(Boolean.TRUE);
        request.setAttribute("partyContact", contact);
        request.setAttribute("partyContactClass", contact.getContactName());
        return backToEditOrCreate(mapping, actionForm, request, response);
    }

    public boolean editContact(PartyContactBean contact) {
        return EditPartyContact.run(contact, isToBeValidated(contact));
    }

    public PartyContact createContact(PartyContactBean contact) {
        return CreatePartyContact.run(contact, isToBeValidated(contact));
    }

    public ActionForward postbackSetElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        PartyContactBean contact = getRenderedObject("edit-contact");
        RenderUtils.invalidateViewState();
        if (contact.getVisibleToPublic().booleanValue()) {
            contact.setVisibleToPublic(new Boolean(contact.getVisibleToStudents().booleanValue()
                    && contact.getVisibleToStaff().booleanValue()));
        }
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
        if (request.getParameter("form").equals("create")) {
            return mapping.findForward("createPartyContact");
        } else if (request.getParameter("form").equals("edit")) {
            return mapping.findForward("editPartyContact");
        } else {
            return null;
        }
    }

    public ActionForward prepareCreatePhysicalAddress(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        PhysicalAddressBean bean = new PhysicalAddressBean(getParty(request));
        request.setAttribute("partyContact", bean);
        return prepareCreatePartyContact(mapping, actionForm, request, response, bean);
    }

    public ActionForward prepareCreatePhone(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        PhoneBean bean = new PhoneBean(getParty(request));
        request.setAttribute("partyContact", bean);
        return prepareCreatePartyContact(mapping, actionForm, request, response, bean);
    }

    public ActionForward prepareCreateMobilePhone(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        MobilePhoneBean bean = new MobilePhoneBean(getParty(request));
        request.setAttribute("partyContact", bean);
        return prepareCreatePartyContact(mapping, actionForm, request, response, bean);
    }

    public ActionForward prepareCreateEmailAddress(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        EmailAddressBean bean = new EmailAddressBean(getParty(request));
        request.setAttribute("partyContact", bean);
        return prepareCreatePartyContact(mapping, actionForm, request, response, bean);
    }

    public ActionForward prepareCreateWebAddress(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        WebAddressBean bean = new WebAddressBean(getParty(request));
        request.setAttribute("partyContact", bean);
        return prepareCreatePartyContact(mapping, actionForm, request, response, bean);
    }

    private ActionForward prepareCreatePartyContact(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, PartyContactBean bean) {
        request.setAttribute("person", getParty(request));
        request.setAttribute("partyContactClass", bean.getContactName());
        return mapping.findForward("createPartyContact");
    }

    public ActionForward createPartyContact(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        if (getRenderedObject("edit-contact") instanceof PartyContactBean) {
            PartyContactBean contact = getRenderedObject("edit-contact");
            PartyContact newPartyContact = null;
            try {
                newPartyContact = createContact(contact);
                if (newPartyContact == null) {
                    addActionMessage("contacts", request, "label.contact.validate.already", contact.getValue());
                    return backToShowInformation(mapping, actionForm, request, response);
                } else {
                    addWarningMessage(request, contact);
                }
            } catch (DomainException e) {
                addActionMessage("contacts", request, e.getMessage(), e.getArgs());
                return backToShowInformation(mapping, actionForm, request, response);
            }
            return forwardToInputValidationCode(mapping, actionForm, request, response, newPartyContact);
        }
        return null;
    }

    protected void addWarningMessage(HttpServletRequest request, PartyContact partyContact) {
        PartyContactBean contactBean = PartyContactBean.createFromDomain(partyContact);
        addActionMessage("contacts", request, contactBean.getValidationMessageKey(), contactBean.getValue());
    }

    protected void addWarningMessage(HttpServletRequest request, PartyContactBean contactBean) {
        addActionMessage("contacts", request, contactBean.getValidationMessageKey(), contactBean.getValue());
    }

    public ActionForward prepareEditPartyContact(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        PartyContact contact = getPartyContact(request);
        PartyContactBean contactBean = PartyContactBean.createFromDomain(contact);
        request.setAttribute("partyContact", contactBean);
        request.setAttribute("partyContactClass", contactBean.getContactName());
        return mapping.findForward("editPartyContact");
    }

    protected PartyContact getPartyContact(final HttpServletRequest request) {
        getParty(request); // this must be called because subclasses can
        // populate request with other needed objects
        final String contactId = (String) getFromRequest(request, "contactId");
        return FenixFramework.getDomainObject(contactId);
    }

    public ActionForward forwardToInputValidationCode(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, PartyContact partyContact) {
        if (partyContact == null || !isToBeValidated(partyContact)) {
            return backToShowInformation(mapping, actionForm, request, response);
        }
        final PartyContactValidation partyContactValidation = partyContact.getPartyContactValidation();
        request.setAttribute("partyContactValidation", partyContactValidation.getExternalId());
        request.setAttribute("valid", partyContactValidation.isValid());
        request.setAttribute("tries", partyContactValidation.getAvailableTries());
        if (partyContact instanceof PhysicalAddress) {
            request.setAttribute("isPhysicalAddress", true);
            request.setAttribute("physicalAddressBean", new PhysicalAddressBean((PhysicalAddress) partyContact));
        }
        request.setAttribute("partyContact", PartyContactBean.createFromDomain(partyContact));
        request.setAttribute("canValidateRequests", ((Person) partyContact.getParty()).getCanValidateContacts());
        return mapping.findForward("inputValidationCode");
    }

    public ActionForward editPartyContact(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        if (getRenderedObject("edit-contact") instanceof PartyContactBean) {
            PartyContactBean contact = getRenderedObject("edit-contact");
            Boolean wasValidated = false;
            try {
                if (contact.hasPartyContact()) {
                    addActionMessage("contacts", request, "label.contact.validate.already", contact.getValue());
                    return backToShowInformation(mapping, actionForm, request, response);
                }
                wasValidated = editContact(contact);
            } catch (DomainException e) {
                addActionMessage("contacts", request, e.getMessage(), e.getArgs());
            }
            if (wasValidated) {
                addWarningMessage(request, contact);
                return forwardToInputValidationCode(mapping, actionForm, request, response, contact.getContact());
            }
            return backToShowInformation(mapping, actionForm, request, response);
        }
        return null;
    }

    public ActionForward prepareValidate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final String partyContactExtId = request.getParameter("partyContact");
        PartyContact partyContact = FenixFramework.getDomainObject(partyContactExtId);
        partyContact.triggerValidationProcessIfNeeded();
        PartyContactBean contactBean = PartyContactBean.createFromDomain(partyContact);
        addWarningMessage(request, contactBean);
        return forwardToInputValidationCode(mapping, actionForm, request, response, partyContact);
    }

    public ActionForward validatePhysicalAddress(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PhysicalAddressBean physicalAddressBean = getRenderedObject("physicalAddressBean");
        final PhysicalAddressValidationBean validationBean = physicalAddressBean.getValidationBean();
        try {
        validationBean.getValidation().setFile(validationBean.getFileName(), validationBean.getFileName(),
                validationBean.readStream());
        } catch (DomainException e) {
            RenderUtils.invalidateViewState();
            addActionMessageLiteral(request, e.getLocalizedMessage());
            return forwardToInputValidationCode(mapping, actionForm, request, response, physicalAddressBean.getContact());
        }
        return backToShowInformation(mapping, actionForm, request, response);
    }

    public ActionForward validatePhysicalAddressInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PhysicalAddressBean physicalAddressBean = getRenderedObject("physicalAddressBean");
        if (physicalAddressBean == null) {
            return backToShowInformation(mapping, actionForm, request, response);
        }
        return forwardToInputValidationCode(mapping, actionForm, request, response, physicalAddressBean.getContact());
    }

    public ActionForward inputValidationCode(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final String code = (String) getFromRequest(request, "validationCode");
        final String extId = (String) getFromRequest(request, "partyContactValidation");

        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(extId)) {
            addActionMessage("contacts", request, "error.contacts.validation.token.empty");
            return mapping.findForward("inputValidationCode");
        }
        PartyContactValidation partyContactValidation;

        final PhysicalAddressValidationBean validationBean = getRenderedObject("physicalAddressValidationBean");
        if (validationBean != null) {
            partyContactValidation = validationBean.getValidation();
        } else {
            partyContactValidation = FenixFramework.getDomainObject(extId);
            partyContactValidation.processValidation(code);
        }
        return forwardToInputValidationCode(mapping, actionForm, request, response, partyContactValidation.getPartyContact());
    }

    public ActionForward deletePartyContact(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            final PartyContact partyContact = getPartyContact(request);
            deleteContact(partyContact);
        } catch (DomainException e) {
            addActionMessage("contacts", request, e.getMessage(), e.getArgs());
        }
        return backToShowInformation(mapping, actionForm, request, response);
    }

    public void deleteContact(PartyContact partyContact) {
        DeletePartyContact.run(partyContact);
    }

    protected Party getParty(final HttpServletRequest request) {
        return AccessControl.getPerson();
    }

    public ActionForward backToShowInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final Person person = Authenticate.getUser().getPerson();
        request.setAttribute("personBean", new PersonBean(person));
        EmergencyContactBean emergencyContactBean = new EmergencyContactBean(person);
        request.setAttribute("emergencyContactBean", emergencyContactBean);
        return mapping.findForward("visualizePersonalInformation");
    }

    public ActionForward requestValidationToken(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final String partyContactExtId = request.getParameter("partyContactValidation");
        final PartyContactValidation partyContactValidation = FenixFramework.getDomainObject(partyContactExtId);
        final PartyContact partyContact = partyContactValidation.getPartyContact();
        PartyContactBean contactBean = PartyContactBean.createFromDomain(partyContact);
        partyContact.triggerValidationProcess();
        addWarningMessage(request, contactBean);
        return forwardToInputValidationCode(mapping, actionForm, request, response, partyContact);
    }

    public ActionForward requestOptOut(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return backToShowInformation(mapping, actionForm, request, response);
    }

    public ActionForward viewStudentLog(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        Person person = AccessControl.getPerson();

        Collection<PersonInformationLog> logsList = person.getPersonInformationLogsSet();
        request.setAttribute("person", person);
        request.setAttribute("logsList", logsList);
        return mapping.findForward("viewStudentLogChanges");
    }

    public static boolean isToBeValidated(PartyContactBean contact) {
        return !(contact instanceof WebAddressBean
                || (contact instanceof PhysicalAddressBean && !FenixEduAcademicConfiguration.getConfiguration().getPhysicalAddressRequiresValidation()));
    }

    protected boolean isToBeValidated(PartyContact contact) {
        return !(contact instanceof WebAddress
                || (contact instanceof PhysicalAddress && !FenixEduAcademicConfiguration.getConfiguration().getPhysicalAddressRequiresValidation()));
    }

}
