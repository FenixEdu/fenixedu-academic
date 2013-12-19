/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateNewInvitedPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateNewPersonInvitation;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteInvitation;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.EditInvitationHostUnit;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.EditInvitationResponsible;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.person.InvitedPersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person.AnyPersonSearchBean;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.CollectionPager;

/**
 * @author T�nia Pous�o
 * 
 */
@Mapping(module = "personnelSection", path = "/findPerson", input = "findPerson", attribute = "findPersonForm",
        formBean = "findPersonForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "viewPerson", path = "/personnelSection/people/viewPerson.jsp"),
        @Forward(name = "displayPerson", path = "/manager/personManagement/displayPerson.jsp"),
        @Forward(name = "findPerson", path = "/manager/personManagement/findPerson.jsp", tileProperties = @Tile(
                title = "private.staffarea.interfacegiaf.interfacegiaf.searchpeople")) })
public class PersonManagementAction extends FenixDispatchAction {

    public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("firstPage");
    }

    public ActionForward viewPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        String externalId = request.getParameter("personID");
        Person person = (Person) FenixFramework.getDomainObject(externalId);
        request.setAttribute("person", person);
        return mapping.findForward("viewPerson");
    }

    private String getModulePrefix(final ActionMapping mapping, final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final int contextPathLength = request.getContextPath().length() + 1;
        final String mappingPath = mapping.getPath();
        final int i = uri.indexOf(mappingPath);
        return uri.substring(contextPathLength, i);
    }

    public ActionForward prepareFindPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String modulePrefix = getModulePrefix(mapping, request);
        request.setAttribute("modulePrefix", modulePrefix);
        return mapping.findForward("findPerson");
    }

    public ActionForward findPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        User userView = Authenticate.getUser();

        DynaActionForm findPersonForm = (DynaActionForm) actionForm;
        String name = null;
        if (findPersonForm.get("name") != null) {
            name = (String) findPersonForm.get("name");
            request.setAttribute("name", name);
        }

        String email = null;
        if (findPersonForm.get("email") != null) {
            email = (String) findPersonForm.get("email");
            request.setAttribute("email", email);
        }

        String username = null;
        if (findPersonForm.get("username") != null) {
            username = (String) findPersonForm.get("username");
            request.setAttribute("username", username);
        }

        String documentIdNumber = null;
        if (findPersonForm.get("documentIdNumber") != null) {
            documentIdNumber = (String) findPersonForm.get("documentIdNumber");
            request.setAttribute("documentIdNumber", documentIdNumber);
        }

        String mechanoGraphicalNumber = null;
        if (findPersonForm.get("mechanoGraphicalNumber") != null) {
            mechanoGraphicalNumber = (String) findPersonForm.get("mechanoGraphicalNumber");
            request.setAttribute("mechanoGraphicalNumber", mechanoGraphicalNumber);
        }

        SearchParameters searchParameters =
                new SearchPerson.SearchParameters(name, email, username, documentIdNumber, null, null, null, null, null, null,
                        null, null, (String) null);

        if ((mechanoGraphicalNumber != null) && (mechanoGraphicalNumber.length() > 0)) {
            searchParameters.setMechanoGraphicalNumber(Integer.parseInt(mechanoGraphicalNumber));
        }

        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

        CollectionPager<Person> result = SearchPerson.runSearchPerson(searchParameters, predicate);

        if (result == null) {
            errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        final String pageNumberString = request.getParameter("pageNumber");
        final Integer pageNumber =
                !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);
        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("numberOfPages", Integer.valueOf(result.getNumberOfPages()));
        request.setAttribute("personListFinded", result.getPage(pageNumber.intValue()));
        request.setAttribute("totalFindedPersons", result.getCollection().size());

        final String modulePrefix = getModulePrefix(mapping, request);
        request.setAttribute("modulePrefix", modulePrefix);

        return mapping.findForward("displayPerson");
    }

    public ActionForward findEmployee(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("displayEmployee");
    }

    public ActionForward prepareSearchExistentPersonBeforeCreateNewInvitedPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        AnyPersonSearchBean anyPersonSearchBean = new AnyPersonSearchBean();
        request.setAttribute("anyPersonSearchBean", anyPersonSearchBean);
        return mapping.findForward("showExistentPersonsBeforeCreateInvitedPerson");
    }

    public ActionForward showExistentPersonsWithSameMandatoryDetails(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final IViewState viewState = RenderUtils.getViewState("anyPersonSearchBeanId");
        AnyPersonSearchBean bean = (AnyPersonSearchBean) viewState.getMetaObject().getObject();

        SearchParameters searchParameters =
                new SearchPerson.SearchParameters(bean.getName(), null, null, bean.getDocumentIdNumber(),
                        bean.getIdDocumentType() != null ? bean.getIdDocumentType().getName() : null, null, null, null, null,
                        null, null, null, (String) null);

        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

        User userView = Authenticate.getUser();
        CollectionPager<Person> result = SearchPerson.runSearchPerson(searchParameters, predicate);

        request.setAttribute("resultPersons", result.getCollection());
        request.setAttribute("anyPersonSearchBean", bean);
        return mapping.findForward("showExistentPersonsBeforeCreateInvitedPerson");
    }

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
        return prepareSearchExistentPersonBeforeCreateNewInvitedPerson(mapping, actionForm, request, response);
    }

    public ActionForward prepareSearchPersonToEdit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        PersonBean personBean = new PersonBean();
        request.setAttribute("personBean", personBean);
        return mapping.findForward("searchPersonToEdit");
    }

    public ActionForward searchPersonToEdit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        readAndSetValidPersons(request);
        return mapping.findForward("searchPersonToEdit");
    }

    public ActionForward prepareEditPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = getPersonFromParameter(request);
        request.setAttribute("person", person);
        return mapping.findForward("prepareEditPerson");
    }

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
                        null, null, null, null, null, null, null, null, (String) null);
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