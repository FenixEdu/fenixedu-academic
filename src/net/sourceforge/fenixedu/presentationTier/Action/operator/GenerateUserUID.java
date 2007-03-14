package net.sourceforge.fenixedu.presentationTier.Action.operator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.manager.loginsManagement.LoginAliasBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginAliasType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class GenerateUserUID extends FenixDispatchAction {
    
    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("personBean", new PersonBean());
	return mapping.findForward("prepareSearchPerson");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	PersonBean personBean = (PersonBean) getRenderedObject("personBeanID");	
	readAndSetResultPersons(request, personBean);
	return mapping.findForward("prepareSearchPerson");
    }
      
    public ActionForward generateUserUID(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Person person = getPersonFromParameter(request);	
	Login login = person.getLoginIdentification();
	
	LoginAliasBean bean = null;
	if(login != null) {
	    bean = new LoginAliasBean(login, LoginAliasType.INSTITUTION_ALIAS);   		   
	    try {
                executeService("CreateNewLoginAlias", new Object[] { bean });	    
            
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());                
            }
            
            if(login.getInstitutionalLoginAlias() == null) {
    	    	addActionMessage(request, "error.no.conditions.create.institutional.alias");
            }
            
	} else {
	    addActionMessage(request, "error.person.without.login.identification");
	}
		
	PersonBean personBean = new PersonBean(person.getName(), person.getUsername(), person.getDocumentIdNumber().toString());
	readAndSetResultPersons(request, personBean);
	
	return mapping.findForward("prepareSearchPerson");
    }
    
    
    // Private Methods
    
    private void readAndSetResultPersons(HttpServletRequest request, PersonBean personBean) throws FenixFilterException, FenixServiceException {
	
	SearchPerson.SearchParameters parameters = new SearchParameters(personBean.getName(), null, 
		personBean.getUsername(), personBean.getDocumentIdNumber(), null, null, null, null, null);	
	SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);

	CollectionPager<Person> persons = (CollectionPager<Person>) executeService("SearchPerson", new Object[] {parameters, predicate});
	
	request.setAttribute("resultPersons", persons.getCollection());
	request.setAttribute("personBean", personBean);
    } 
    
    private Person getPersonFromParameter(HttpServletRequest request) {
	String personIDString = request.getParameter("personID");
	return (Person) ((StringUtils.isEmpty(personIDString)) ? null : rootDomainObject.readPartyByOID(Integer.valueOf(personIDString)));
    }      
}
