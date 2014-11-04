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
package org.fenixedu.academic.ui.struts.action.student.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.commons.delegates.DelegateSearchBean;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.student.Delegate;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.YearDelegate;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentViewApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = StudentViewApp.class, path = "delegates-info", titleKey = "link.student.delegatesInfo")
@Mapping(module = "student", path = "/delegatesInfo")
@Forwards(@Forward(name = "showDegreeDelegates", path = "/student/delegates/showDelegates.jsp"))
public class DelegatesInfoDispatchAction extends FenixDispatchAction {
    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Person person = getLoggedPerson(request);
        final Student student = person.getStudent();
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        if (student.getLastActiveRegistration() == null) {
            return mapping.findForward("showDegreeDelegates");
        }
        final Degree degree = student.getLastActiveRegistration().getDegree();

        DelegateSearchBean bean = new DelegateSearchBean(currentExecutionYear, degree, degree.getDegreeType());

        return showAll(mapping, request, bean);
    }

    public ActionForward showAll(ActionMapping mapping, HttpServletRequest request, DelegateSearchBean bean) {
        /* DEGREE DELEGATES */
        if (bean.getExecutionYear() != null && bean.getDegree() != null && bean.getDegreeType() != null) {
            request.setAttribute("yearDelegates", getYearDelegateBeans(bean));
            request.setAttribute("degreeDelegate", getDelegateSearchBean(bean, FunctionType.DELEGATE_OF_DEGREE));
            request.setAttribute("masterDegreeDelegate", getDelegateSearchBean(bean, FunctionType.DELEGATE_OF_MASTER_DEGREE));
            request.setAttribute("integratedMasterDegreeDelegate",
                    getDelegateSearchBean(bean, FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE));
        }
        /* GGAE DELEGATES */
        List<DelegateSearchBean> delegatesFound = getGGAEDelegateBeans();
        Collections.sort(delegatesFound, DelegateSearchBean.DELEGATE_COMPARATOR_BY_EXECUTION_YEAR);

        request.setAttribute("ggaeDelegates", delegatesFound);
        request.setAttribute("searchBean", bean);
        request.setAttribute("searchByDegreeBean", bean);
        return mapping.findForward("showDegreeDelegates");
    }

    private void updateBeanDegree(DelegateSearchBean bean) {
        boolean updated = false;
        Degree first = null;

        for (ExecutionDegree executionDegree : bean.getExecutionYear().getExecutionDegreesSet()) {
            if (executionDegree.getDegreeType().equals(bean.getDegreeType())) {
                if (!updated) {
                    first = executionDegree.getDegree();
                    updated = true;
                }
                if (bean.getDegree() != null && executionDegree.getDegree().getName().equals(bean.getDegree().getName())) {
                    bean.setDegree(executionDegree.getDegree());
                    return;
                }
            }
        }

        bean.setDegree(first);
    }

    private void updateBeanDegreeType(DelegateSearchBean bean) {
        boolean updated = false;
        DegreeType first = bean.getExecutionYear().getExecutionDegreesSet().iterator().next().getDegreeType();

        for (ExecutionDegree executionDegree : bean.getExecutionYear().getExecutionDegreesSet()) {
            if (executionDegree.getDegreeType().getGraduateTitle().split(" ")[0].equals(bean.getDegreeType().getGraduateTitle()
                    .split(" ")[0])) {
                bean.setDegreeType(executionDegree.getDegreeType());
                updated = true;
                break;
            }
        }

        if (!updated) {
            bean.setDegreeType(first);
        }

        updateBeanDegree(bean);
    }

    public ActionForward updateDelegates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DelegateSearchBean bean = (DelegateSearchBean) getRenderedObject("searchByDegreeBean");
        return showAll(mapping, request, bean);
    }

    public ActionForward updateExecutionYear(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DelegateSearchBean bean = (DelegateSearchBean) getRenderedObject("searchByDegreeBean");
        RenderUtils.invalidateViewState();
        updateBeanDegreeType(bean);
        return showAll(mapping, request, bean);
    }

    public ActionForward updateDegreeType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DelegateSearchBean bean = (DelegateSearchBean) getRenderedObject("searchByDegreeBean");
        RenderUtils.invalidateViewState();
        updateBeanDegree(bean);
        return showAll(mapping, request, bean);
    }

    /*
     * AUXILIARY METHODS
     */

    /* Delegates from given degree (not year delegates) */
    private DelegateSearchBean getDelegateSearchBean(DelegateSearchBean bean, FunctionType functionType) {
        List<Student> delegates = new ArrayList<Student>();
        if (bean.getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear())) {
            delegates
                    .addAll(Delegate.getAllActiveDelegatesByFunctionType(bean.getDegree(), functionType, bean.getExecutionYear()));
        } else {
            delegates.addAll(Delegate.getAllDelegatesByExecutionYearAndFunctionType(bean.getDegree(), bean.getExecutionYear(),
                    functionType));
        }
        return (delegates.isEmpty() ? null : new DelegateSearchBean(delegates.iterator().next().getPerson(), functionType,
                bean.getExecutionYear()));
    }

    /* Year delegates from given degree */
    private List<DelegateSearchBean> getYearDelegateBeans(DelegateSearchBean bean) {
        List<DelegateSearchBean> yearDelegates = new ArrayList<DelegateSearchBean>();
        final Degree degree = bean.getDegree();
        final ExecutionYear executionYear = bean.getExecutionYear();

        for (int i = 1; i <= bean.getDegreeType().getYears(); i++) {
            final CurricularYear curricularYear = CurricularYear.readByYear(i);
            Student student = null;

            student = YearDelegate.getYearDelegateByExecutionYearAndCurricularYear(degree, executionYear, curricularYear);

            if (student != null) {
                DelegateSearchBean delegateBean =
                        new DelegateSearchBean(student.getPerson(), FunctionType.DELEGATE_OF_YEAR, curricularYear, executionYear);
                yearDelegates.add(delegateBean);
            }
        }
        return yearDelegates;
    }

    /* Delegates from all degrees */
    private List<DelegateSearchBean> getGGAEDelegateBeans() {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        List<DelegateSearchBean> result = new ArrayList<DelegateSearchBean>();

        Set<Function> functions = Function.readAllActiveFunctionsByType(FunctionType.DELEGATE_OF_GGAE);
        for (Function function : functions) {
            for (PersonFunction personFunction : PersonFunction.getActivePersonFunctionsStartingIn(function, currentExecutionYear)) {
                DelegateSearchBean bean = new DelegateSearchBean(personFunction.getPerson(), personFunction);
                result.add(bean);
            }
        }

        return result;
    }
}
