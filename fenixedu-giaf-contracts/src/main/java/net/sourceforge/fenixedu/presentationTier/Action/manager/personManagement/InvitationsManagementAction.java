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
package org.fenixedu.academic.ui.struts.action.manager.personManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.CreateNewInvitedPerson;
import org.fenixedu.academic.service.services.manager.CreateNewPersonInvitation;
import org.fenixedu.academic.service.services.manager.DeleteInvitation;
import org.fenixedu.academic.service.services.manager.EditInvitationHostUnit;
import org.fenixedu.academic.service.services.manager.EditInvitationResponsible;
import org.fenixedu.academic.service.services.person.SearchPerson;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchParameters;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchPersonPredicate;
import org.fenixedu.academic.dto.person.InvitedPersonBean;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Invitation;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerPersonManagementApp;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@StrutsFunctionality(app = ManagerPersonManagementApp.class, path = "invitations-management",
        titleKey = "title.manage.external.persons")
@Mapping(path = "/invitationsManagement", module = "manager")
@Forwards({
        @Forward(name = "searhPersonBeforeInvitationsManagement",
                path = "/manager/personManagement/choosePersonForManageInvitations.jsp"),
        @Forward(name = "managePersonInvitations", path = "/manager/personManagement/managePersonInvitations.jsp"),
        @Forward(name = "prepareEditInvitation", path = "/manager/personManagement/editInvitation.jsp"),
        @Forward(name = "prepareEditInvitationDetails", path = "/manager/personManagement/changeInvitationDetails.jsp"),
        @Forward(name = "prepareCreateNewPersonInvitation", path = "/manager/personManagement/createNewPersonInvitation.jsp"),
        @Forward(name = "prepareCreateInvitedPerson", path = "/manager/personManagement/createInvitedPerson.jsp") })
public class InvitationsManagementAction extends FenixDispatchAction {

    public ActionForward prepareCreateInvitedPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        setRequestParametersToCreateInvitedPerson(request, new InvitedPersonBean());
        request.setAttribute("initialUnit", UnitUtils.readInstitutionUnit());
        return mapping.findForward("prepareCreateInvitedPerson");
    }

    public ActionForward associateResponsibilityParty(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IViewState viewState = RenderUtils.getViewState("invitedPersonBeanWithResponsibilityParty");
        InvitedPersonBean invitedPersonBean = (InvitedPersonBean) viewState.getMetaObject().getObject();
        request.setAttribute("invitedPersonBean", invitedPersonBean);
        return mapping.findForward("prepareCreateInvitedPerson");
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IViewState viewState = RenderUtils.getViewState("invitedPersonBeanWithLoginInfo");
        InvitedPersonBean invitedPersonBean = (InvitedPersonBean) viewState.getMetaObject().getObject();
        request.setAttribute("invitedPersonBean", invitedPersonBean);
        return mapping.findForward("prepareCreateInvitedPerson");
    }

    public ActionForward createNewInvitedPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IViewState viewState = RenderUtils.getViewState("invitedPersonBeanWithLoginInfo");
        InvitedPersonBean invitedPersonBean = (InvitedPersonBean) viewState.getMetaObject().getObject();

        Invitation invitation = null;
        try {
            invitation = CreateNewInvitedPerson.run(invitedPersonBean);

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("invitedPersonBean", invitedPersonBean);
            return mapping.findForward("prepareCreateInvitedPerson");
        }

        request.setAttribute("createdPerson", invitation != null ? invitation.getInvitedPerson() : null);
        return prepareSearchPersonForManageInvitations(mapping, actionForm, request, response);
    }

    @EntryPoint
    public ActionForward prepareSearchPersonForManageInvitations(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        PersonBean personBean = new PersonBean();
        request.setAttribute("personBean", personBean);
        return mapping.findForward("searhPersonBeforeInvitationsManagement");
    }

    public ActionForward searchPersonForManageInvitations(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        readAndSetValidPersons(request);
        return mapping.findForward("searhPersonBeforeInvitationsManagement");
    }

    public ActionForward managePersonInvitations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = getPersonFromParameter(request);
        request.setAttribute("person", person);
        return mapping.findForward("managePersonInvitations");
    }

    public ActionForward prepareEditPersonInvitation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Invitation invitation = getInvitationFromParameter(request);
        request.setAttribute("invitation", invitation);
        return mapping.findForward("prepareEditInvitation");
    }

    public ActionForward prepareEditPersonInvitationHostUnit(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return goToChangeInvitationDetailsPage("hostUnit", mapping, request);
    }

    public ActionForward prepareEditPersonInvitationResponsible(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return goToChangeInvitationDetailsPage("responsibleParty", mapping, request);
    }

    public ActionForward prepareEditPersonInvitationTimeInterval(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return goToChangeInvitationDetailsPage("timeInterval", mapping, request);
    }

    public ActionForward editPersonInvitationHostUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Invitation invitation = getInvitationFromParameter(request);
        Unit hostUnit = getHostUnitFromParameter(request);
        try {
            EditInvitationHostUnit.run(invitation, hostUnit);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }
        request.setAttribute("invitation", invitation);
        return mapping.findForward("prepareEditInvitation");
    }

    public ActionForward prepareCreateNewPersonInvitation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        InvitedPersonBean bean = null;
        final IViewState viewState = RenderUtils.getViewState("invitedPersonBeanWithResponsibilityParty");
        if (viewState != null) {
            bean = (InvitedPersonBean) viewState.getMetaObject().getObject();
        } else {
            bean = new InvitedPersonBean();
            bean.setUnit(getHostUnitFromParameter(request));
            bean.setInvitedPerson(getPersonFromParameter(request));
            bean.setResponsible(getResponsibleUnitFromParameter(request));
        }
        return goToPrepareCreateNewPersonInvitationPage(mapping, request, bean);
    }

    public ActionForward createNewPersonInvitation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IViewState viewState = RenderUtils.getViewState("invitedPersonBeanWithTimeInterval");
        InvitedPersonBean bean = (InvitedPersonBean) viewState.getMetaObject().getObject();

        try {
            CreateNewPersonInvitation.run(bean);

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return goToPrepareCreateNewPersonInvitationPage(mapping, request, bean);
        }

        request.setAttribute("person", bean.getInvitedPerson());
        return mapping.findForward("managePersonInvitations");
    }

    public ActionForward editPersonInvitationResponsible(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Invitation invitation = getInvitationFromParameter(request);
        Unit hostUnit = getHostUnitFromParameter(request);
        try {
            EditInvitationResponsible.run(invitation, hostUnit);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }
        request.setAttribute("invitation", invitation);
        return mapping.findForward("prepareEditInvitation");
    }

    public ActionForward deletePersonInvitation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Invitation invitation = getInvitationFromParameter(request);

        try {
            DeleteInvitation.run(invitation);

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }
        return managePersonInvitations(mapping, actionForm, request, response);
    }

    public ActionForward editPartyAddress(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("partyContact", getPartyContact(request));
        return mapping.findForward("editPartyAddress");
    }

    protected PartyContact getPartyContact(final HttpServletRequest request) {
        return getDomainObject(request, "addressID");
    }

    private ActionForward goToPrepareCreateNewPersonInvitationPage(ActionMapping mapping, HttpServletRequest request,
            InvitedPersonBean bean) {
        request.setAttribute("initialUnit", UnitUtils.readInstitutionUnit());
        request.setAttribute("invitedPersonBean", bean);
        return mapping.findForward("prepareCreateNewPersonInvitation");
    }

    private ActionForward goToChangeInvitationDetailsPage(String infoToEdit, ActionMapping mapping, HttpServletRequest request) {
        Invitation invitation = getInvitationFromParameter(request);
        request.setAttribute("initialUnit", UnitUtils.readInstitutionUnit());
        request.setAttribute("invitation", invitation);
        request.setAttribute("infoToEdit", infoToEdit);
        return mapping.findForward("prepareEditInvitationDetails");
    }

    private void setRequestParametersToCreateInvitedPerson(final HttpServletRequest request,
            final InvitedPersonBean invitedPersonBean) {

        final String name = request.getParameter("name");
        if (isSpecified(name)) {
            invitedPersonBean.setName(name);
        }
        final String idDocumentType = request.getParameter("idDocumentType");
        if (isSpecified(idDocumentType)) {
            invitedPersonBean.setIdDocumentType(IDDocumentType.valueOf(idDocumentType));
        }
        final String documentIdNumber = request.getParameter("documentIdNumber");
        if (isSpecified(documentIdNumber)) {
            invitedPersonBean.setDocumentIdNumber(documentIdNumber);
        }
        invitedPersonBean.setUnit(getHostUnitFromParameter(request));
        invitedPersonBean.setResponsible(getResponsibleUnitFromParameter(request));
        request.setAttribute("invitedPersonBean", invitedPersonBean);
    }

    private void readAndSetValidPersons(HttpServletRequest request) throws FenixServiceException {
        final IViewState viewState = RenderUtils.getViewState("personBeanID");
        PersonBean personBean = (PersonBean) viewState.getMetaObject().getObject();

        SearchPerson.SearchParameters parameters =
                new SearchParameters(personBean.getName(), null, personBean.getUsername(), personBean.getDocumentIdNumber(),
                        null, null, null, null, null, null, null, (String) null);
        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);

        CollectionPager<Person> persons = SearchPerson.runSearchPerson(parameters, predicate);
        request.setAttribute("resultPersons", persons.getCollection());
        request.setAttribute("personBean", personBean);
    }

    private Unit getHostUnitFromParameter(HttpServletRequest request) {
        return getDomainObject(request, "unitID");
    }

    private Person getPersonFromParameter(HttpServletRequest request) {
        return getDomainObject(request, "personID");
    }

    private Invitation getInvitationFromParameter(HttpServletRequest request) {
        return getDomainObject(request, "invitationID");
    }

    private Unit getResponsibleUnitFromParameter(HttpServletRequest request) {
        return getDomainObject(request, "responsibilityUnitID");
    }

    private boolean isSpecified(final String string) {
        return !StringUtils.isEmpty(string);
    }

}
