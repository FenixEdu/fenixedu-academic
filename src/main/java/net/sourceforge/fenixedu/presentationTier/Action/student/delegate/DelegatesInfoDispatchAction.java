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
package net.sourceforge.fenixedu.presentationTier.Action.student.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.commons.delegates.DelegateSearchBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentViewApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
            delegates.addAll(bean.getDegree().getAllActiveDelegatesByFunctionType(functionType, bean.getExecutionYear()));
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
        final Degree degree = bean.getDegree();
        final ExecutionYear executionYear = bean.getExecutionYear();

        for (int i = 1; i <= bean.getDegreeType().getYears(); i++) {
            final CurricularYear curricularYear = CurricularYear.readByYear(i);
            Student student = null;

            student = degree.getYearDelegateByExecutionYearAndCurricularYear(executionYear, curricularYear);

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
            for (PersonFunction personFunction : function.getActivePersonFunctionsStartingIn(currentExecutionYear)) {
                DelegateSearchBean bean = new DelegateSearchBean(personFunction.getPerson(), personFunction);
                result.add(bean);
            }
        }

        return result;
    }
}
