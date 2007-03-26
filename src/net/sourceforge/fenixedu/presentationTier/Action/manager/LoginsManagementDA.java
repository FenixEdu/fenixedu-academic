package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.manager.loginsManagement.LoginAliasBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.LoginAliasType;
import net.sourceforge.fenixedu.domain.LoginPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class LoginsManagementDA extends FenixDispatchAction {

    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("personBean", new PersonBean());
	return mapping.findForward("prepareSearchPerson");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final IViewState viewState = RenderUtils.getViewState("personBeanID");
	PersonBean personBean = (PersonBean) viewState.getMetaObject().getObject();

	SearchPerson.SearchParameters parameters = new SearchParameters(personBean.getName(), null,
		personBean.getUsername(), personBean.getDocumentIdNumber(), null, null, null, null,
		null, null, null);
	SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);

	CollectionPager<Person> persons = (CollectionPager<Person>) executeService("SearchPerson",
		new Object[] { parameters, predicate });

	request.setAttribute("resultPersons", persons.getCollection());
	request.setAttribute("personBean", personBean);
	return mapping.findForward("prepareSearchPerson");
    }

    public ActionForward prepareManageAlias(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Person person = getPersonFromParameter(request);
	request.setAttribute("login", person.getLoginIdentification());
	return mapping.findForward("prepareManageLoginAlias");
    }

    public ActionForward deleteAlias(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	LoginAlias loginAlias = getLoginAliasFromParameter(request);
	Login login = (loginAlias != null) ? loginAlias.getLogin() : null;

	try {
	    executeService("DeleteLoginAlias", new Object[] { loginAlias });
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}

	request.setAttribute("login", login);
	return mapping.findForward("prepareManageLoginAlias");
    }

    public ActionForward createNewLoginAlias(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	LoginAliasBean bean = null;
	IViewState viewState = RenderUtils.getViewState("newRoleTypeAliasBeanID");
	if (viewState == null) {
	    viewState = RenderUtils.getViewState("newCustomAliasBeanID");
	}

	bean = (LoginAliasBean) viewState.getMetaObject().getObject();
	Login login = (bean != null) ? bean.getLogin() : null;

	try {
	    executeService("CreateNewLoginAlias", new Object[] { bean });
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    request.setAttribute("aliasType", bean.getLoginAliasType());
	    request.setAttribute("loginAliasBean", bean);
	    return mapping.findForward("prepareCreateNewLoginAlias");
	}

	request.setAttribute("login", login);
	return mapping.findForward("prepareManageLoginAlias");
    }

    public ActionForward prepareCreateRoleTypeAlias(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return goToInsertNewAliasPage(mapping, request, LoginAliasType.ROLE_TYPE_ALIAS);
    }

    public ActionForward prepareCreateCustomAlias(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return goToInsertNewAliasPage(mapping, request, LoginAliasType.CUSTOM_ALIAS);
    }

    public ActionForward prepareEditAlias(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	LoginAlias loginAlias = getLoginAliasFromParameter(request);
	request.setAttribute("loginAlias", loginAlias);
	return mapping.findForward("prepareEditLoginAlias");
    }

    public ActionForward prepareManageLoginTimeIntervals(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Person person = getPersonFromParameter(request);
	request.setAttribute("login", person.getLoginIdentification());
	return mapping.findForward("prepareManageLoginTimeIntervals");
    }

    public ActionForward prepareEditLoginTimeInterval(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	LoginPeriod loginPeriod = getLoginPeriodFromParameter(request);
	request.setAttribute("loginPeriod", loginPeriod);
	return mapping.findForward("prepareEditLoginTimeInterval");
    }

    public ActionForward prepareCreateLoginTimeInterval(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Login login = getLoginFromParameter(request);
	request.setAttribute("login", login);
	return mapping.findForward("prepareCreateNewLoginTimeInterval");
    }

    public ActionForward deleteLoginTimeInterval(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	LoginPeriod loginPeriod = getLoginPeriodFromParameter(request);
	Login login = (loginPeriod != null) ? loginPeriod.getLogin() : null;

	try {
	    executeService("DeleteLoginPeriod", new Object[] { loginPeriod });
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}

	request.setAttribute("login", login);
	return mapping.findForward("prepareManageLoginTimeIntervals");
    }
    
     public ActionForward prepareCreateInstitutionalAlias(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	Login login = getLoginFromParameter(request);	
	LoginAliasBean bean = new LoginAliasBean(login, LoginAliasType.INSTITUTION_ALIAS);
				
	try {
	    executeService("CreateNewLoginAlias", new Object[] { bean });	    
	
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());            
	}
	
	if(login.getInstitutionalLoginAlias() == null) {
	    addActionMessage(request, "error.no.conditions.create.institutional.alias");
	}
	
	request.setAttribute("login", login);
	return mapping.findForward("prepareManageLoginAlias");
    } 
  
    // Private Methods
    

    private ActionForward goToInsertNewAliasPage(ActionMapping mapping, HttpServletRequest request,
	    LoginAliasType type) {
	Login login = getLoginFromParameter(request);
	request.setAttribute("aliasType", type);
	request.setAttribute("loginAliasBean", new LoginAliasBean(login, type));
	return mapping.findForward("prepareCreateNewLoginAlias");
    }

    private Person getPersonFromParameter(HttpServletRequest request) {
	String personIDString = request.getParameter("personID");
	return (Person) ((StringUtils.isEmpty(personIDString)) ? null : rootDomainObject
		.readPartyByOID(Integer.valueOf(personIDString)));
    }

    private Login getLoginFromParameter(HttpServletRequest request) {
	String loginIDString = request.getParameter("loginID");
	return (Login) ((StringUtils.isEmpty(loginIDString)) ? null : rootDomainObject
		.readIdentificationByOID(Integer.valueOf(loginIDString)));
    }

    private LoginAlias getLoginAliasFromParameter(HttpServletRequest request) {
	String loginAliasIDString = request.getParameter("loginAliasID");
	return (LoginAlias) ((StringUtils.isEmpty(loginAliasIDString)) ? null : rootDomainObject
		.readLoginAliasByOID(Integer.valueOf(loginAliasIDString)));
    }

    private LoginPeriod getLoginPeriodFromParameter(HttpServletRequest request) {
	String loginPeriodIDString = request.getParameter("loginPeriodID");
	return (LoginPeriod) ((StringUtils.isEmpty(loginPeriodIDString)) ? null : rootDomainObject
		.readLoginPeriodByOID(Integer.valueOf(loginPeriodIDString)));
    }
}
