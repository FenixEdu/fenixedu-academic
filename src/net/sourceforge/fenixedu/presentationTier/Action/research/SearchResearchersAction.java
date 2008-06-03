package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.Researcher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class SearchResearchersAction extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	VariantBean nameBean = getBean("nameSearch");
	VariantBean keywordBean = getBean("keywordSearch");
	request.setAttribute("nameBean", nameBean);
	request.setAttribute("keywordBean", keywordBean);
	
	return mapping.findForward("showResearchersResults");
    
    }
    
    public ActionForward searchByKeyword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	VariantBean bean = getBean("keywordSearch");

	String keywords = bean.getString();
	if (keywords != null) {
	    List<Researcher> results = new ArrayList<Researcher>();
	    for (Researcher researcher : rootDomainObject.getResearchers()) {
		if (researcher.getAllowsToBeSearched() && researcher.hasAtLeastOneKeyword(keywords.split(" "))) {
		    results.add(researcher);
		}
	    }
	    Collections.sort(results, Researcher.PUBLICATION_VOLUME_COMPARATOR);
	    request.setAttribute("researchers", results);

	}
	return search(mapping, form, request, response);
    }

    public ActionForward searchByName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	VariantBean bean = getBean("nameSearch");

	if (bean.getString() != null) {
	    SearchParameters parameters = new SearchPerson.SearchParameters(bean.getString(), null, null, null, null,
		    RoleType.RESEARCHER.toString(), null, null, null, Boolean.TRUE, null, Boolean.FALSE, Boolean.TRUE);

	    final SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);

	    CollectionPager<Person> result = null;
	    try {
		result = (CollectionPager<Person>) executeService("SearchPerson", parameters, predicate);
	    } catch (FenixFilterException e) {
		e.printStackTrace();
	    } catch (FenixServiceException e) {
		e.printStackTrace();
	    }

	    List<Researcher> results = new ArrayList<Researcher>();
	    for (Person person : result.getCollection()) {
		results.add(person.getResearcher());
	    }
	    Collections.sort(results, Researcher.PUBLICATION_VOLUME_COMPARATOR);
	    request.setAttribute("researchers", results);
	}
	return search(mapping, form, request, response);
    }

    public ActionForward browseByDepartment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String departmentId = request.getParameter("departmentId");
	if (departmentId != null) {
	    Department department = rootDomainObject.readDepartmentByOID(Integer.valueOf(departmentId));
	    List<Researcher> results = new ArrayList<Researcher>();
	    for (Employee employee : department.getDepartmentUnit().getAllCurrentActiveWorkingEmployees()) {
		Person person = employee.getPerson();
		if (person.hasResearcher() && person.getResearcher().getAllowsToBeSearched()) {
		    results.add(person.getResearcher());
		}
	    }
	    Collections.sort(results, Researcher.PUBLICATION_VOLUME_COMPARATOR);
	    request.setAttribute("researchers", results);
	    request.setAttribute("department", department);
	}
	request.setAttribute("departments", rootDomainObject.getDepartments());

	return mapping.findForward("browseResearchers");
    }

    private VariantBean getBean(String viewStateName) {
	VariantBean bean = new VariantBean();
	IViewState viewState = RenderUtils.getViewState(viewStateName);
	if (viewState != null) {
	    bean.setString((String) viewState.getMetaObject().getObject());
	}
	return bean;
    }
}
