/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.person.InvitedPersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person.AnyPersonSearchBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.utl.ist.fenix.tools.util.CollectionPager;

/**
 * @author Tânia Pousão
 * 
 */
public class PersonManagementAction extends FenixDispatchAction {
    public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return mapping.findForward("firstPage");
    }

    public ActionForward prepareFindPerson(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return mapping.findForward("findPerson");
    }

    public ActionForward findPerson(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	ActionErrors errors = new ActionErrors();

	IUserView userView = SessionUtils.getUserView(request);

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

	SearchParameters searchParameters = new SearchPerson.SearchParameters(name, email, username,
		documentIdNumber, null, null, null, null, null);

	SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

	Object[] args = { searchParameters, predicate };

	CollectionPager result = null;
	try {
	    result = (CollectionPager) ServiceManagerServiceFactory.executeService(userView,
		    "SearchPerson", args);

	} catch (FenixServiceException e) {
	    e.printStackTrace();
	    errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
	}
	if (result == null) {
	    errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
	}
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    return mapping.getInputForward();
	}

	final String pageNumberString = request.getParameter("pageNumber");
	final Integer pageNumber = !StringUtils.isEmpty(pageNumberString) ? Integer
		.valueOf(pageNumberString) : Integer.valueOf(1);
	request.setAttribute("pageNumber", pageNumber);
	request.setAttribute("numberOfPages", Integer.valueOf(result.getNumberOfPages()));
	request.setAttribute("personListFinded", result.getPage(pageNumber.intValue()));
	request.setAttribute("totalFindedPersons", result.getCollection().size());

	return mapping.findForward("displayPerson");
    }

    public ActionForward findEmployee(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return mapping.findForward("displayEmployee");
    }

    public ActionForward prepareSearchExistentPersonBeforeCreateNewInvitedPerson(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	AnyPersonSearchBean anyPersonSearchBean = new AnyPersonSearchBean();
	request.setAttribute("anyPersonSearchBean", anyPersonSearchBean);
	return mapping.findForward("showExistentPersonsBeforeCreateInvitedPerson");
    }

    public ActionForward showExistentPersonsWithSameMandatoryDetails(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	final IViewState viewState = RenderUtils.getViewState("anyPersonSearchBeanId");
	AnyPersonSearchBean anyPersonSearchBean = (AnyPersonSearchBean) viewState.getMetaObject().getObject();
	request.setAttribute("resultPersons", anyPersonSearchBean.search());
	request.setAttribute("anyPersonSearchBean", anyPersonSearchBean);
	return mapping.findForward("showExistentPersonsBeforeCreateInvitedPerson");
    }

    public ActionForward prepareCreateInvitedPerson(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	setRequestParametersToCreateInvitedPerson(request, new InvitedPersonBean());
	request.setAttribute("initialUnit", UnitUtils.readInstitutionUnit());
	return mapping.findForward("prepareCreateInvitedPerson");
    }

    public ActionForward associateResponsibilityParty(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final IViewState viewState = RenderUtils.getViewState("invitedPersonBeanWithResponsibilityParty");
	InvitedPersonBean invitedPersonBean = (InvitedPersonBean) viewState.getMetaObject().getObject();
	request.setAttribute("invitedPersonBean", invitedPersonBean);
	return mapping.findForward("prepareCreateInvitedPerson");
    }

    public ActionForward createNewInvitedPerson(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final IViewState viewState = RenderUtils.getViewState("invitedPersonBeanWithLoginInfo");
	InvitedPersonBean invitedPersonBean = (InvitedPersonBean) viewState.getMetaObject().getObject();

	Object[] args = { invitedPersonBean };
	Invitation invitation = null;
	try {
	    invitation = (Invitation) executeService("CreateNewInvitedPerson", args);

	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    request.setAttribute("invitedPersonBean", invitedPersonBean);
	    return mapping.findForward("prepareCreateInvitedPerson");
	}

	request.setAttribute("createdPerson", invitation != null ? invitation.getInvitedPerson() : null);
	return prepareSearchExistentPersonBeforeCreateNewInvitedPerson(mapping, actionForm, request,
		response);
    }

    public ActionForward prepareSearchPersonToEdit(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	PersonBean personBean = new PersonBean();
	request.setAttribute("personBean", personBean);
	return mapping.findForward("searchPersonToEdit");
    }

    public ActionForward searchPersonToEdit(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	readAndSetValidPersons(request);
	return mapping.findForward("searchPersonToEdit");
    }

    public ActionForward prepareEditPerson(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Person person = getPersonFromParameter(request);
	request.setAttribute("person", person);
	return mapping.findForward("prepareEditPerson");
    }

    public ActionForward prepareSearchPersonForManageInvitations(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	PersonBean personBean = new PersonBean();
	request.setAttribute("personBean", personBean);
	return mapping.findForward("searhPersonBeforeInvitationsManagement");
    }

    public ActionForward searchPersonForManageInvitations(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	readAndSetValidPersons(request);
	return mapping.findForward("searhPersonBeforeInvitationsManagement");
    }

    public ActionForward managePersonInvitations(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Person person = getPersonFromParameter(request);
	request.setAttribute("person", person);
	return mapping.findForward("managePersonInvitations");
    }

    public ActionForward prepareEditPersonInvitation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Invitation invitation = getInvitationFromParameter(request);
	request.setAttribute("invitation", invitation);
	return mapping.findForward("prepareEditInvitation");
    }

    public ActionForward prepareEditPersonInvitationHostUnit(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	return goToChangeInvitationDetailsPage("hostUnit", mapping, request);
    }

    public ActionForward prepareEditPersonInvitationResponsible(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	return goToChangeInvitationDetailsPage("responsibleParty", mapping, request);
    }

    public ActionForward prepareEditPersonInvitationTimeInterval(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	return goToChangeInvitationDetailsPage("timeInterval", mapping, request);
    }

    public ActionForward editPersonInvitationHostUnit(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return processInvitationEditService("EditInvitationHostUnit", mapping, request);
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

    public ActionForward createNewPersonInvitation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final IViewState viewState = RenderUtils.getViewState("invitedPersonBeanWithTimeInterval");
	InvitedPersonBean bean = (InvitedPersonBean) viewState.getMetaObject().getObject();

	Object[] args = { bean };
	try {
	    executeService("CreateNewPersonInvitation", args);

	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    return goToPrepareCreateNewPersonInvitationPage(mapping, request, bean);
	}

	request.setAttribute("person", bean.getInvitedPerson());
	return mapping.findForward("managePersonInvitations");
    }    

    public ActionForward editPersonInvitationResponsible(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return processInvitationEditService("EditInvitationResponsible", mapping, request);
    }
    
    public ActionForward deletePersonInvitation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	Invitation invitation = getInvitationFromParameter(request);
	Object[] args = { invitation };
	try {
	    executeService("DeleteInvitation", args);

	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}
	return managePersonInvitations(mapping, actionForm, request, response);	
    }

    private ActionForward goToPrepareCreateNewPersonInvitationPage(ActionMapping mapping, HttpServletRequest request, InvitedPersonBean bean) {
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

    private ActionForward processInvitationEditService(String serviceName, ActionMapping mapping,
	    HttpServletRequest request) throws FenixFilterException, FenixServiceException {
	
	Invitation invitation = getInvitationFromParameter(request);
	Unit hostUnit = getHostUnitFromParameter(request);
	Object[] args = { invitation, hostUnit };
	try {
	    executeService(serviceName, args);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}
	request.setAttribute("invitation", invitation);
	return mapping.findForward("prepareEditInvitation");
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

    private void readAndSetValidPersons(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
	final IViewState viewState = RenderUtils.getViewState("personBeanID");
	PersonBean personBean = (PersonBean) viewState.getMetaObject().getObject();

	SearchPerson.SearchParameters parameters = new SearchParameters(personBean.getName(), null,
		personBean.getUsername(), personBean.getDocumentIdNumber(), null, null, null, null, null);
			
	List<Person> persons = (List<Person>) executeService("SearchPerson", new Object[] {parameters});
	
	request.setAttribute("resultPersons", persons);
	request.setAttribute("personBean", personBean);
    }

    private Unit getHostUnitFromParameter(HttpServletRequest request) {
	String unitIDString = request.getParameter("unitID");
	return (Unit) ((StringUtils.isEmpty(unitIDString)) ? null : rootDomainObject
		.readPartyByOID(Integer.valueOf(unitIDString)));
    }

    private Person getPersonFromParameter(HttpServletRequest request) {
	String personIDString = request.getParameter("personID");
	return (Person) ((StringUtils.isEmpty(personIDString)) ? null : rootDomainObject
		.readPartyByOID(Integer.valueOf(personIDString)));
    }

    private Invitation getInvitationFromParameter(HttpServletRequest request) {
	String invitationIDString = request.getParameter("invitationID");
	return (Invitation) ((StringUtils.isEmpty(invitationIDString)) ? null : rootDomainObject
		.readAccountabilityByOID(Integer.valueOf(invitationIDString)));
    }

    private Unit getResponsibleUnitFromParameter(HttpServletRequest request) {
	String unitIDString = request.getParameter("responsibilityUnitID");
	return (Unit) ((StringUtils.isEmpty(unitIDString)) ? null : rootDomainObject
		.readPartyByOID(Integer.valueOf(unitIDString)));
    }

    private boolean isSpecified(final String string) {
	return !StringUtils.isEmpty(string);
    }
}