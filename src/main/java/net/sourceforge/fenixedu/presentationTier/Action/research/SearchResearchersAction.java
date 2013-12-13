package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(module = "messaging", path = "/searchResearchers", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showResearchersResults", path = "/researcher/expertDetails/searchResearchers.jsp"),
        @Forward(name = "browseResearchers", path = "/researcher/expertDetails/browserResearchers.jsp") })
public class SearchResearchersAction extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

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
            String[] keywordsArray = filterKeywords(keywords.split(" "));

            List<Researcher> results = new ArrayList<Researcher>();
            for (Researcher researcher : rootDomainObject.getResearchersSet()) {
                if (researcher.getAllowsToBeSearched() && researcher.hasAtLeastOneKeyword(keywordsArray)) {
                    results.add(researcher);
                }
            }
            Collections.sort(results, Researcher.PUBLICATION_VOLUME_COMPARATOR);
            request.setAttribute("researchers", results);

        }
        return search(mapping, form, request, response);
    }

    private static final int MIN_KEYWORD_LENGTH = 1;

    private String[] filterKeywords(String[] keywords) {
        Collection<String> keywordsList = Arrays.asList(keywords);
        CollectionUtils.filter(keywordsList, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((String) arg0).length() > MIN_KEYWORD_LENGTH;
            }
        });

        return keywordsList.toArray(new String[0]);
    }

    public ActionForward searchByName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        VariantBean bean = getBean("nameSearch");

        if (bean.getString() != null) {
            SearchParameters parameters =
                    new SearchPerson.SearchParameters(bean.getString(), null, null, null, null, RoleType.RESEARCHER.toString(),
                            null, null, null, Boolean.TRUE, null, Boolean.FALSE, Boolean.TRUE);

            final SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);

            CollectionPager<Person> result = SearchPerson.runSearchPerson(parameters, predicate);

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
            Department department = FenixFramework.getDomainObject(departmentId);
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
        request.setAttribute("departments", rootDomainObject.getDepartmentsSet());

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
