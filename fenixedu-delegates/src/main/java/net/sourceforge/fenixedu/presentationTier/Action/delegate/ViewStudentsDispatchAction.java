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
package org.fenixedu.academic.ui.struts.action.delegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.VariantBean;
import org.fenixedu.academic.dto.delegate.DelegateCurricularCourseBean;
import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.student.Delegate;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.delegate.DelegateApplication.DelegateConsultApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
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
        if (person.getStudent() != null) {
            final Student student = person.getStudent();

            PersonFunction yearDelegateFunction = null;
            List<Registration> activeRegistrations = new ArrayList<Registration>(student.getActiveRegistrations());
            Collections.sort(activeRegistrations, Registration.COMPARATOR_BY_START_DATE);
            for (Registration registration : activeRegistrations) {
                yearDelegateFunction =
                        Delegate.getActiveDelegatePersonFunctionByStudentAndFunctionType(registration.getDegree(), student,
                                executionYear, FunctionType.DELEGATE_OF_YEAR);
                if (yearDelegateFunction != null) {
                    break;
                }
            }

            if (yearDelegateFunction != null) {
                final ExecutionYear delegateExecutionYear =
                        executionYear == null ? ExecutionYear.getExecutionYearByDate(yearDelegateFunction.getBeginDate()) : executionYear;

                students.addAll(Delegate.getStudentsResponsibleForGivenFunctionType(person.getStudent(),
                        FunctionType.DELEGATE_OF_YEAR, delegateExecutionYear));

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
        if (person.getStudent() != null) {
            final Student student = person.getStudent();
            List<Registration> activeRegistrations = new ArrayList<Registration>(student.getActiveRegistrations());
            Collections.sort(activeRegistrations, Registration.COMPARATOR_BY_START_DATE);
            for (Registration registration : activeRegistrations) {
                delegateFunction =
                        Delegate.getMostSignificantDelegateFunctionForStudent(registration.getDegree(), student, executionYear);
                if (delegateFunction != null) {
                    break;
                }
            }
        } else {
            delegateFunction = PersonFunction.getActiveGGAEDelegatePersonFunction(person);
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
            if (person.getStudent() != null) {
                Student student = person.getStudent();
                Set<CurricularCourse> curricularCourses =
                        Delegate.getCurricularCoursesResponsibleForByFunctionType(student,
                                Delegate.getDelegateFunction(student, executionYear), executionYear);
                return getCurricularCoursesBeans(delegateFunction, curricularCourses);
            } else if (!person.getCoordinatorsSet().isEmpty()) {
                Set<CurricularCourse> curricularCourses =
                        getDegreesCurricularCoursesFromCoordinatorRoles(person.getCoordinatorsSet(),
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
            for (ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
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
