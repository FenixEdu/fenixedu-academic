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
package net.sourceforge.fenixedu.presentationTier.Action.delegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.delegate.DelegateCurricularCourseBean;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.delegate.DelegateApplication.DelegateConsultApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = DelegateConsultApp.class, path = "students", titleKey = "link.students")
@Mapping(module = "delegate", path = "/viewStudents")
@Forwards(@Forward(name = "showStudents", path = "/delegate/showStudents.jsp"))
public class ViewStudentsDispatchAction extends FenixDispatchAction {

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

    public ActionForward showStudents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person person = getLoggedPerson(request);

        String year = request.getParameter("year");
        ExecutionYear executionYear = year == null ? null : ExecutionYear.readExecutionYearByName(year);

        List<Student> students = new ArrayList<Student>();
        if (person.hasStudent()) {
            final Student student = person.getStudent();

            PersonFunction yearDelegateFunction = null;
            List<Registration> activeRegistrations = new ArrayList<Registration>(student.getActiveRegistrations());
            Collections.sort(activeRegistrations, Registration.COMPARATOR_BY_START_DATE);
            for (Registration registration : activeRegistrations) {
                yearDelegateFunction =
                        registration.getDegree().getActiveDelegatePersonFunctionByStudentAndFunctionType(student, executionYear,
                                FunctionType.DELEGATE_OF_YEAR);
                if (yearDelegateFunction != null) {
                    break;
                }
            }

            if (yearDelegateFunction != null) {
                final ExecutionYear delegateExecutionYear =
                        executionYear == null ? ExecutionYear.getExecutionYearByDate(yearDelegateFunction.getBeginDate()) : executionYear;

                students.addAll(person.getStudent().getStudentsResponsibleForGivenFunctionType(FunctionType.DELEGATE_OF_YEAR,
                        delegateExecutionYear));

                Collections.sort(students, Student.NUMBER_COMPARATOR);
            }
        }

        VariantBean variantBean = new VariantBean();
        variantBean.setDomainObject(executionYear);

        request.setAttribute("studentsList", students);
        request.setAttribute("currentExecutionYear", variantBean);
        return mapping.findForward("showStudents");
    }

    @EntryPoint
    public ActionForward prepareShowStudentsByCurricularCourse(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String year = request.getParameter("year");
        ExecutionYear executionYear =
                year == null ? ExecutionYear.readCurrentExecutionYear() : ExecutionYear.readExecutionYearByName(year);
        return prepareShowStudentsByCurricularCourse(mapping, actionForm, request, response, executionYear);
    }

    private ActionForward prepareShowStudentsByCurricularCourse(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response, ExecutionYear executionYear) throws Exception {
        final Person person = getLoggedPerson(request);

        VariantBean variantBean = new VariantBean();
        variantBean.setDomainObject(executionYear);
        request.setAttribute("curricularCoursesList", getCurricularCourses(person, executionYear));
        request.setAttribute("currentExecutionYear", variantBean);
        return mapping.findForward("showStudents");
    }

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        VariantBean variantBean = getRenderedObject("chooseExecutionYear");
        RenderUtils.invalidateViewState();
        return prepareShowStudentsByCurricularCourse(mapping, actionForm, request, response,
                (ExecutionYear) variantBean.getDomainObject());
    }

    public ActionForward showStudentsByCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String year = request.getParameter("year");
        ExecutionYear executionYear =
                year == null ? ExecutionYear.readCurrentExecutionYear() : ExecutionYear.readExecutionYearByName(year);

        final Person person = getLoggedPerson(request);
        final PersonFunction delegateFunction = getPersonFunction(person, executionYear);

        if (delegateFunction != null) {
            final CurricularCourse curricularCourse =
                    (CurricularCourse) FenixFramework.getDomainObject(request.getParameter("curricularCourseID"));
            final Integer curricularYear = Integer.parseInt(request.getParameter("curricularYear"));
            final ExecutionSemester executionSemester =
                    FenixFramework.getDomainObject(request.getParameter("executionPeriodOID"));

            DelegateCurricularCourseBean bean =
                    new DelegateCurricularCourseBean(curricularCourse, executionYear, curricularYear, executionSemester);
            bean.calculateEnrolledStudents();
            request.setAttribute("selectedCurricularCourseBean", bean);
        }

        VariantBean variantBean = new VariantBean();
        variantBean.setDomainObject(executionYear);

        request.setAttribute("currentExecutionYear", variantBean);
        return mapping.findForward("showStudents");
    }

    /* AUXILIARY METHODS */

    private PersonFunction getPersonFunction(final Person person, final ExecutionYear executionYear) {
        PersonFunction delegateFunction = null;
        if (person.hasStudent()) {
            final Student student = person.getStudent();
            List<Registration> activeRegistrations = new ArrayList<Registration>(student.getActiveRegistrations());
            Collections.sort(activeRegistrations, Registration.COMPARATOR_BY_START_DATE);
            for (Registration registration : activeRegistrations) {
                delegateFunction = registration.getDegree().getMostSignificantDelegateFunctionForStudent(student, executionYear);
                if (delegateFunction != null) {
                    break;
                }
            }
        } else {
            delegateFunction = person.getActiveGGAEDelegatePersonFunction();
        }
        return delegateFunction;
    }

    private Set<CurricularCourse> getDegreesCurricularCoursesFromCoordinatorRoles(Collection<Coordinator> coordinators,
            ExecutionYear executionYear) {
        Set<CurricularCourse> curricularCourses = new HashSet<CurricularCourse>();
        for (Coordinator coordinator : coordinators) {
            final Degree degree = coordinator.getExecutionDegree().getDegree();
            for (CurricularCourse curricularCourse : degree.getAllCurricularCourses(executionYear)) {
                if (!curricularCourses.contains(curricularCourse)) {
                    curricularCourses.add(curricularCourse);
                }
            }

        }
        return curricularCourses;
    }

    private List<DelegateCurricularCourseBean> getCurricularCourses(final Person person, ExecutionYear executionYear) {
        List<DelegateCurricularCourseBean> result = new ArrayList<DelegateCurricularCourseBean>();

        final PersonFunction delegateFunction = getPersonFunction(person, executionYear);
        if (delegateFunction != null) {
            if (person.hasStudent()) {
                Set<CurricularCourse> curricularCourses =
                        person.getStudent().getCurricularCoursesResponsibleForByFunctionType(
                                delegateFunction.getFunction().getFunctionType(), executionYear);
                return getCurricularCoursesBeans(delegateFunction, curricularCourses);
            } else if (person.hasAnyCoordinators()) {
                Set<CurricularCourse> curricularCourses =
                        getDegreesCurricularCoursesFromCoordinatorRoles(person.getCoordinators(),
                                ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate()));
                return getCurricularCoursesBeans(delegateFunction, curricularCourses);
            }
        }
        return result;
    }

    private List<DelegateCurricularCourseBean> getCurricularCoursesBeans(PersonFunction delegateFunction,
            Set<CurricularCourse> curricularCourses) {
        final FunctionType delegateFunctionType = delegateFunction.getFunction().getFunctionType();
        final ExecutionYear executionYear = ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate());

        List<DelegateCurricularCourseBean> result = new ArrayList<DelegateCurricularCourseBean>();

        for (CurricularCourse curricularCourse : curricularCourses) {
            for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
                if (curricularCourse.hasAnyExecutionCourseIn(executionSemester)) {
                    for (DegreeModuleScope scope : curricularCourse.getDegreeModuleScopes()) {
                        if (!scope.isActiveForExecutionPeriod(executionSemester)) {
                            continue;
                        }

                        if (delegateFunctionType.equals(FunctionType.DELEGATE_OF_YEAR)
                                && !scopeBelongsToDelegateCurricularYear(scope, delegateFunction.getCurricularYear().getYear())) {
                            continue;
                        }

                        DelegateCurricularCourseBean bean =
                                new DelegateCurricularCourseBean(curricularCourse, executionYear, scope.getCurricularYear(),
                                        executionSemester);
                        if (!result.contains(bean)) {
                            bean.calculateEnrolledStudents();
                            result.add(bean);
                        }
                    }
                }
            }
        }
        Collections.sort(result,
                DelegateCurricularCourseBean.CURRICULAR_COURSE_COMPARATOR_BY_CURRICULAR_YEAR_AND_CURRICULAR_SEMESTER);

        return result;
    }

    private boolean scopeBelongsToDelegateCurricularYear(DegreeModuleScope scope, Integer curricularYear) {
        if (scope.getCurricularYear().equals(curricularYear)) {
            return true;
        }
        return false;
    }
}
