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
package net.sourceforge.fenixedu.presentationTier.Action.commons.delegates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.commons.delegates.DelegateSearchBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.CollectionPager;

public class FindDelegatesDispatchAction extends FenixDispatchAction {

    @Override
    protected Object getFromRequest(HttpServletRequest request, String id) {
        if (RenderUtils.getViewState(id) != null) {
            return RenderUtils.getViewState(id).getMetaObject().getObject();
        } else if (request.getParameter(id) != null) {
            return request.getParameter(id);
        } else {
            return request.getAttribute(id);
        }
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        DelegateSearchBean bean = new DelegateSearchBean();

        if (request.getParameter("searchByName") != null) {
            request.setAttribute("searchByNameBean", bean);
        } else if (request.getParameter("searchByNumber") != null) {
            request.setAttribute("searchByNumberBean", bean);
        } else if (request.getParameter("searchByDelegateType") != null) {
            request.setAttribute("searchByDelegateTypeBean", bean);
        } else if (request.getParameter("searchByDegree") != null) {
            bean.setDegreeType(DegreeType.BOLONHA_DEGREE);
            bean.setDegree(getDefaultDegreeGivenDegreeType(DegreeType.BOLONHA_DEGREE));
            bean.setExecutionYear(currentExecutionYear);
            request.setAttribute("searchByDegreeBean", bean);
        } else {
            request.setAttribute("searchByNameBean", bean);
        }

        request.setAttribute("currentExecutionYear", currentExecutionYear);
        return mapping.findForward("searchDelegates");
    }

    public ActionForward prepareSearchByDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        DelegateSearchBean bean = (DelegateSearchBean) getFromRequest(request, "searchByDegreeBean");
        RenderUtils.invalidateViewState("searchByDegreeBean");

        bean.setDegree(getDefaultDegreeGivenDegreeType(bean.getDegreeType()));
        bean.setExecutionYear(currentExecutionYear);

        request.setAttribute("searchMethod", "searchByDegree");
        request.setAttribute("searchByDegreeBean", bean);
        request.setAttribute("currentExecutionYear", currentExecutionYear);
        return mapping.findForward("searchDelegates");
    }

    public ActionForward searchByDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        DelegateSearchBean bean = (DelegateSearchBean) getFromRequest(request, "searchByDegreeBean");

        request.setAttribute("yearDelegates", getYearDelegateBeans(bean));
        request.setAttribute("degreeDelegate", getDelegateSearchBean(bean, FunctionType.DELEGATE_OF_DEGREE));
        request.setAttribute("masterDegreeDelegate", getDelegateSearchBean(bean, FunctionType.DELEGATE_OF_MASTER_DEGREE));
        request.setAttribute("integratedMasterDegreeDelegate",
                getDelegateSearchBean(bean, FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE));

        if (request.getParameter("showBackLink") != null) {
            request.setAttribute("searchMethod", "searchByDegree");
        }
        request.setAttribute("searchByDegreeBean", bean);
        request.setAttribute("currentExecutionYear", currentExecutionYear);
        return mapping.findForward("showDelegates");
    }

    public ActionForward searchByName(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        DelegateSearchBean bean = (DelegateSearchBean) getFromRequest(request, "searchByNameBean");
        RenderUtils.invalidateViewState("searchByNameBean");

        SearchParameters searchParameters =
                new SearchPerson.SearchParameters(bean.getDelegateName(), null, null, null, null, RoleType.DELEGATE.getName(),
                        null, null, null, Boolean.TRUE, null, Boolean.FALSE, (String) null);

        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

        Object[] args = { searchParameters, predicate };

        CollectionPager<Person> result = SearchPerson.runSearchPerson(searchParameters, predicate);

        List<DelegateSearchBean> delegatesFound = new ArrayList<DelegateSearchBean>();
        if (result == null) {
            addActionMessage(request, "error.delegates.searchDelegates.delegateNotFound");
        } else {
            for (Person person : result.getCollection()) {
                List<PersonFunction> delegateFunctions = null;
                if (bean.getOnlyActiveDelegates()) {
                    delegateFunctions = getActivePersonFunctionsFor(person);
                } else {
                    delegateFunctions = getAllPersonFunctionsFor(person);
                }

                for (PersonFunction function : delegateFunctions) {
                    DelegateSearchBean delegateBean = new DelegateSearchBean(person, function);
                    delegatesFound.add(delegateBean);
                }
            }
        }
        request.setAttribute("searchMethod", "searchByName");
        request.setAttribute("searchByNameBean", bean);
        request.setAttribute("currentExecutionYear", currentExecutionYear);
        request.setAttribute("delegatesFound", delegatesFound);
        return mapping.findForward("showDelegates");
    }

    public ActionForward searchByNumber(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        DelegateSearchBean bean = (DelegateSearchBean) getFromRequest(request, "searchByNumberBean");
        RenderUtils.invalidateViewState("searchByNumberBean");

        Student student = Student.readStudentByNumber(bean.getStudentNumber());
        bean.setDelegate(student.getPerson());

        List<DelegateSearchBean> delegatesFound = new ArrayList<DelegateSearchBean>();

        List<PersonFunction> delegateFunctions = null;
        if (bean.getOnlyActiveDelegates()) {
            delegateFunctions = student.getAllActiveDelegateFunctions();
        } else {
            delegateFunctions = student.getAllDelegateFunctions();
        }
        for (PersonFunction function : delegateFunctions) {
            DelegateSearchBean delegateBean = new DelegateSearchBean(student.getPerson(), function);
            delegatesFound.add(delegateBean);
        }

        request.setAttribute("searchMethod", "searchByNumber");
        request.setAttribute("searchByNumberBean", bean);
        request.setAttribute("currentExecutionYear", currentExecutionYear);
        request.setAttribute("delegatesFound", delegatesFound);
        return mapping.findForward("showDelegates");
    }

    public ActionForward searchByDelegateType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        DelegateSearchBean bean = (DelegateSearchBean) getFromRequest(request, "searchByDelegateTypeBean");
        RenderUtils.invalidateViewState("searchByDelegateTypeBean");

        if (bean.getDelegateType() == FunctionType.DELEGATE_OF_YEAR && bean.getCurricularYear() == null) {
            request.setAttribute("searchByDelegateTypeBean", bean);
            request.setAttribute("currentExecutionYear", currentExecutionYear);
            return mapping.findForward("searchDelegates");
        }

        if (bean.getDelegateType() == FunctionType.DELEGATE_OF_YEAR) {
            List<DelegateSearchBean> delegatesFound = getYearDelegateBeansByCurricularYear(bean);
            Collections.sort(delegatesFound, DelegateSearchBean.YEAR_DELEGATE_COMPARATOR_BY_EXECUTION_YEAR_AND_CURRICULAR_YEAR);
            request.setAttribute("yearDelegatesFound", delegatesFound);
        } else {
            List<DelegateSearchBean> delegatesFound = getDelegateBeansByFunctionType(bean);
            Collections.sort(delegatesFound, DelegateSearchBean.DELEGATE_COMPARATOR_BY_EXECUTION_YEAR);
            request.setAttribute("delegates", delegatesFound);
        }
        request.setAttribute("searchByDelegateTypeBean", bean);
        request.setAttribute("searchMethod", "searchByDelegateType");
        request.setAttribute("currentExecutionYear", currentExecutionYear);
        return mapping.findForward("showDelegates");
    }

    /*
     * AUXILIARY METHODS
     */

    private List<PersonFunction> getActivePersonFunctionsFor(Person person) {
        if (person.hasStudent()) {
            return person.getStudent().getAllActiveDelegateFunctions();
        } else {
            List<PersonFunction> result = new ArrayList<PersonFunction>();
            result.add(person.getActiveGGAEDelegatePersonFunction());
            return result;
        }
    }

    private List<PersonFunction> getAllPersonFunctionsFor(Person person) {
        if (person.hasStudent()) {
            return person.getStudent().getAllDelegateFunctions();
        } else {
            return person.getAllGGAEDelegatePersonFunctions();
        }
    }

    /* Year delegates from all degrees */
    private List<DelegateSearchBean> getYearDelegateBeansByCurricularYear(DelegateSearchBean searchBean) {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        List<DelegateSearchBean> result = new ArrayList<DelegateSearchBean>();
        List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
        if (searchBean.getOnlyActiveDelegates()) {
            Set<Function> functions = Function.readAllActiveFunctionsByType(FunctionType.DELEGATE_OF_YEAR);
            for (Function function : functions) {
                personFunctions.addAll(function.getActivePersonFunctionsStartingIn(currentExecutionYear));
            }
        } else {
            Set<Function> functions = Function.readAllFunctionsByType(FunctionType.DELEGATE_OF_YEAR);
            for (Function function : functions) {
                personFunctions.addAll(function.getPersonFunctions());
            }
        }
        for (PersonFunction personFunction : personFunctions) {
            if (personFunction.getCurricularYear().equals(searchBean.getCurricularYear())) {
                Student student = personFunction.getPerson().getStudent();
                DelegateSearchBean bean = new DelegateSearchBean(student.getPerson(), personFunction);
                result.add(bean);
            }
        }
        return result;
    }

    /* Delegates from all degrees */
    private List<DelegateSearchBean> getDelegateBeansByFunctionType(DelegateSearchBean searchBean) {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        List<DelegateSearchBean> result = new ArrayList<DelegateSearchBean>();
        List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
        if (searchBean.getOnlyActiveDelegates()) {
            Set<Function> functions = Function.readAllActiveFunctionsByType(searchBean.getDelegateType());
            for (Function function : functions) {
                personFunctions.addAll(function.getActivePersonFunctionsStartingIn(currentExecutionYear));
            }
        } else {
            Set<Function> functions = Function.readAllFunctionsByType(searchBean.getDelegateType());
            for (Function function : functions) {
                personFunctions.addAll(function.getPersonFunctions());
            }
        }
        for (PersonFunction personFunction : personFunctions) {
            DelegateSearchBean bean = new DelegateSearchBean(personFunction.getPerson(), personFunction);
            result.add(bean);
        }
        return result;
    }

    private Degree getDefaultDegreeGivenDegreeType(DegreeType degreeType) {
        List<Degree> degrees = Degree.readAllByDegreeType(degreeType);
        return degrees.iterator().next();
    }

    /* Delegates from given degree (not year delegates) */
    private DelegateSearchBean getDelegateSearchBean(DelegateSearchBean bean, FunctionType functionType) {
        List<Student> delegates = new ArrayList<Student>();
        if (bean.getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear())) {
            delegates.addAll(bean.getDegree().getAllActiveDelegatesByFunctionType(functionType, null));
        } else {
            delegates.addAll(bean.getDegree()
                    .getAllDelegatesByExecutionYearAndFunctionType(bean.getExecutionYear(), functionType));
        }
        return (delegates.isEmpty() ? null : new DelegateSearchBean(delegates.iterator().next().getPerson(), functionType,
                bean.getExecutionYear()));
    }

    /* Year delegates from given degree */
    private List<DelegateSearchBean> getYearDelegateBeans(DelegateSearchBean bean) {
        List<DelegateSearchBean> yearDelegates = new ArrayList<DelegateSearchBean>();
        for (int i = 1; i <= bean.getDegreeType().getYears(); i++) {
            final CurricularYear curricularYear = CurricularYear.readByYear(i);
            Student student = null;
            if (bean.getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear())) {
                student = bean.getDegree().getActiveYearDelegateByCurricularYear(curricularYear);
            } else {
                student =
                        bean.getDegree().getYearDelegateByExecutionYearAndCurricularYear(bean.getExecutionYear(), curricularYear);
            }
            if (student != null) {
                DelegateSearchBean delegateBean =
                        new DelegateSearchBean(student.getPerson(), FunctionType.DELEGATE_OF_YEAR, curricularYear,
                                bean.getExecutionYear());
                yearDelegates.add(delegateBean);
            }
        }
        return yearDelegates;
    }
}
