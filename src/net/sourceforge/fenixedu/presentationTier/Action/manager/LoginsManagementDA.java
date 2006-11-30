package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginAlias;
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
		personBean.getUsername(), personBean.getDocumentIdNumber(), null, null, null, null);
	SearchPerson.SearchPersonPredicate predicate = new SearchPersonPredicate(parameters);
	List<Person> persons = SearchPerson.searchPersonInAllPersons(predicate);

	request.setAttribute("resultPersons", persons);
	request.setAttribute("personBean", personBean);
	return mapping.findForward("prepareSearchPerson");
    }   
    
    public ActionForward prepareManageAlias(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
        
	Person person = getPersonFromParameter(request);
	Login login = person.getLoginIdentification();	
	request.setAttribute("login", login);	
	return mapping.findForward("prepareManageLoginAlias");
    }
    
    public ActionForward deleteAlias(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
        
	LoginAlias loginAlias = getLoginAliasFromParameter(request);
	Login login = loginAlias.getLogin();
	
	try {
	    executeService("DeleteLoginAlias", new Object[] { loginAlias });	    
	} catch (DomainException e) {
	    
	}
	
	request.setAttribute("login", login);	
	return mapping.findForward("prepareManageLoginAlias");
    }
    
    public ActionForward prepareManageLoginTimeIntervals(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	Person person = getPersonFromParameter(request);
        List<LoginPeriod> loginPeriodsWithoutInvitationPeriods = person.getLoginIdentification().getLoginPeriodsWithoutInvitationPeriods();
	
        
	return mapping.findForward("prepareManageLoginTimeIntervals");
    }    

    private Person getPersonFromParameter(HttpServletRequest request) {
	String personIDString = request.getParameter("personID");
	return (Person) ((StringUtils.isEmpty(personIDString)) ? null : rootDomainObject
		.readPartyByOID(Integer.valueOf(personIDString)));
    }
    
    private LoginAlias getLoginAliasFromParameter(HttpServletRequest request) {
	String loginAliasIDString = request.getParameter("loginAliasID");
	return (LoginAlias) ((StringUtils.isEmpty(loginAliasIDString)) ? null : rootDomainObject
		.readLoginAliasByOID(Integer.valueOf(loginAliasIDString)));
    }
}
