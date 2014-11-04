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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.commons.ExecutionYearBean;
import org.fenixedu.academic.dto.delegate.DelegateCurricularCourseBean;
import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.DelegateStudentsGroup;
import org.fenixedu.academic.domain.accessControl.StudentGroup;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.student.Delegate;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.delegate.DelegateApplication.DelegateMessagingApp;
import org.fenixedu.academic.ui.struts.action.messaging.EmailsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = DelegateMessagingApp.class, path = "send-email-to-students", titleKey = "link.sendEmailToStudents")
@Mapping(path = "/sendEmailToDelegateStudents", module = "delegate")
@Forwards({ @Forward(name = "choose-receivers", path = "/delegate/chooseReceivers.jsp"),
        @Forward(name = "choose-student-receivers", path = "/delegate/chooseStudentReceivers.jsp") })
public class SendEmailToDelegateStudents extends FenixDispatchAction {

    @SuppressWarnings("unchecked")
    protected List<Group> getPossibleReceivers(HttpServletRequest request, ExecutionYear executionYear) {
        List<Group> groups = new ArrayList<Group>();
        final Person person = getLoggedPerson(request);
        PersonFunction delegateFunction = getDelegateFunction(executionYear, person);
        if (delegateFunction != null) {
            if (request.getAttribute("curricularCoursesList") != null) {
                executionYear =
                        executionYear == null ? ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate()) : executionYear;

                List<CurricularCourse> curricularCourses = (List<CurricularCourse>) request.getAttribute("curricularCoursesList");
                for (CurricularCourse curricularCourse : curricularCourses) {
                    List<ExecutionCourse> executions = curricularCourse.getExecutionCoursesByExecutionYear(executionYear);
                    for (ExecutionCourse executionCourse : executions) {
                        groups.add(StudentGroup.get(executionCourse));
                    }
                }
            } else {
                if (delegateFunction != null && delegateFunction.getFunction().isOfFunctionType(FunctionType.DELEGATE_OF_GGAE)) {
                    groups.add(DelegateStudentsGroup.get(delegateFunction, delegateFunction.getFunction().getFunctionType()));
                } else if (delegateFunction != null
                        && delegateFunction.getFunction().isOfFunctionType(FunctionType.DELEGATE_OF_YEAR)) {
                    groups.add(DelegateStudentsGroup.get(delegateFunction, delegateFunction.getFunction().getFunctionType()));
                } else {
                    groups.add(DelegateStudentsGroup.get(delegateFunction, FunctionType.DELEGATE_OF_YEAR));
                    groups.add(DelegateStudentsGroup.get(delegateFunction, delegateFunction.getFunction().getFunctionType()));
                }
            }
        }

        return groups;
    }

    public ActionForward prepareSendEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        GroupsBean recipientsBean = getRenderedObject("recipientsBean");
        String year = request.getParameter("year");
        ExecutionYear executionYear =
                year == null ? ExecutionYear.readCurrentExecutionYear() : (ExecutionYear) FenixFramework.getDomainObject(year);
        List<Recipient> recipients;
        List<Group> recipientsGroups;
        if (request.getAttribute("curricularCoursesList") != null) {
            recipientsGroups = getPossibleReceivers(request, executionYear);
        } else {
            recipientsGroups = recipientsBean.getSelected();
        }
        recipients = Recipient.newInstance(recipientsGroups);
        return EmailsDA.sendEmail(request, null, recipients.toArray(new Recipient[] {}));
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        RenderUtils.invalidateViewState();
        String year = request.getParameter("year");
        ExecutionYear executionYear =
                year == null ? ExecutionYear.readCurrentExecutionYear() : (ExecutionYear) FenixFramework.getDomainObject(year);
        return prepare(mapping, actionForm, request, response, executionYear);
    }

    private ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, ExecutionYear executionYear) throws Exception {
        GroupsBean recipients = new GroupsBean(getPossibleReceivers(request, executionYear));
        request.setAttribute("recipients", recipients);
        ExecutionYearBean executionYearBean = new ExecutionYearBean(executionYear);
        request.setAttribute("currentExecutionYear", executionYearBean);
        return mapping.findForward("choose-student-receivers");
    }

    private ActionForward prepareSendToStudentsFromSelectedCurricularCourses(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response, ExecutionYear executionYear) throws Exception {
        final Person person = getLoggedPerson(request);
        PersonFunction delegateFunction = getDelegateFunction(executionYear, person);
        if (delegateFunction != null) {
            request.setAttribute("curricularCoursesList", getCurricularCourses(delegateFunction, executionYear));
        } else {
            addActionMessage(request, "error.delegates.sendMail.notExistentDelegateFunction");
        }

        ExecutionYearBean executionYearBean = new ExecutionYearBean(executionYear);
        request.setAttribute("currentExecutionYear", executionYearBean);
        return mapping.findForward("choose-receivers");
    }

    private PersonFunction getDelegateFunction(ExecutionYear executionYear, final Person person) {
        PersonFunction delegateFunction = null;
        if (person.getStudent() != null) {
            final Student student = person.getStudent();
            List<Registration> activeRegistrations = new ArrayList<Registration>(student.getActiveRegistrations());
            Collections.sort(activeRegistrations, Registration.COMPARATOR_BY_START_DATE);
            for (Registration registration : activeRegistrations) {
                delegateFunction =
                        Delegate.getMostSignificantActiveDelegateFunctionForStudent(registration.getDegree(), student,
                                executionYear);
                if (delegateFunction != null && delegateFunction.isActive()) {
                    return delegateFunction;
                }
            }
        } else {
            return PersonFunction.getActiveGGAEDelegatePersonFunction(person);
        }
        return null; // no active function was found
    }

    public ActionForward prepareSendToStudentsFromSelectedCurricularCourses(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String year = request.getParameter("year");
        ExecutionYear executionYear =
                (ExecutionYear) (year != null ? FenixFramework.getDomainObject(year) : ExecutionYear.readCurrentExecutionYear());
        return prepareSendToStudentsFromSelectedCurricularCourses(mapping, actionForm, request, response, executionYear);
    }

    public ActionForward sendToStudentsFromSelectedCurricularCourses(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final String[] parameterValues = request.getParameterValues("selectedCurricularCourses");
        final List<String> selectedCurricularCourses =
                parameterValues == null || parameterValues.length == 0 ? Collections.EMPTY_LIST : Arrays.asList(parameterValues);
        List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
        for (String curricularCourseId : selectedCurricularCourses) {
            CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseId);
            curricularCourses.add(curricularCourse);
        }

        if (!curricularCourses.isEmpty()) {
            request.setAttribute("curricularCoursesList", curricularCourses);
            return prepareSendEmail(mapping, actionForm, request, response);

        } else {
            addActionMessage(request, "error.delegates.sendMail.curricularCoursesNotSelected");
            RenderUtils.invalidateViewState("selectedCurricularCourses");
            return prepareSendToStudentsFromSelectedCurricularCourses(mapping, actionForm, request, response);
        }
    }

    /*
     * AUXILIARY METHODS
     */

    private Set<CurricularCourse> getDegreesCurricularCoursesFromCoordinatorRoles(Collection<Coordinator> coordinators,
            ExecutionYear executionYear) {
        Set<CurricularCourse> curricularCourses = new HashSet<CurricularCourse>();
        for (Coordinator coordinator : coordinators) {
            final Degree degree = coordinator.getExecutionDegree().getDegree();
            curricularCourses.addAll(degree.getAllCurricularCourses(executionYear));
        }
        return curricularCourses;
    }

    private List<DelegateCurricularCourseBean> getCurricularCourses(final PersonFunction delegateFunction,
            ExecutionYear executionYear) {
        List<DelegateCurricularCourseBean> result = new ArrayList<DelegateCurricularCourseBean>();
        if (delegateFunction != null) {
            if (delegateFunction.getPerson().getStudent() != null) {
                Set<CurricularCourse> curricularCourses =
                        Delegate.getCurricularCoursesResponsibleForByFunctionType(delegateFunction.getPerson().getStudent(),
                                delegateFunction, executionYear);
                return getCurricularCoursesBeans(delegateFunction, curricularCourses, executionYear);
            } else if (!delegateFunction.getPerson().getCoordinatorsSet().isEmpty()) {
                Set<CurricularCourse> curricularCourses =
                        getDegreesCurricularCoursesFromCoordinatorRoles(delegateFunction.getPerson().getCoordinatorsSet(),
                                executionYear);
                return getCurricularCoursesBeans(delegateFunction, curricularCourses, executionYear);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<DelegateCurricularCourseBean> getCurricularCoursesBeans(PersonFunction delegateFunction,
            Set<CurricularCourse> curricularCourses, ExecutionYear executionYear) {
        final FunctionType delegateFunctionType = delegateFunction.getFunction().getFunctionType();

        List<DelegateCurricularCourseBean> result = new ArrayList<DelegateCurricularCourseBean>();

        for (CurricularCourse curricularCourse : curricularCourses) {
            for (ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
                if (curricularCourse.hasAnyExecutionCourseIn(executionSemester)) {
                    for (DegreeModuleScope scope : curricularCourse.getDegreeModuleScopes()) {
                        if (scope.isActiveForAcademicInterval(executionSemester.getAcademicInterval())) {
                            if (!scope.getCurricularSemester().equals(executionSemester.getSemester())) {
                                continue;
                            }

                            if (delegateFunctionType.equals(FunctionType.DELEGATE_OF_YEAR)
                                    && !scopeBelongsToDelegateCurricularYear(scope, delegateFunction.getCurricularYear()
                                            .getYear())) {
                                continue;
                            }
                            DelegateCurricularCourseBean bean =
                                    new DelegateCurricularCourseBean(curricularCourse, executionYear, scope.getCurricularYear(),
                                            executionSemester);
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

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ExecutionYearBean executionYearBean = getRenderedObject("chooseExecutionYear");
        RenderUtils.invalidateViewState();
        return prepare(mapping, actionForm, request, response, executionYearBean.getExecutionYear());
    }

    public ActionForward chooseExecutionYearCurricularCourseList(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExecutionYearBean executionYearBean = getRenderedObject("chooseExecutionYear");
        RenderUtils.invalidateViewState();
        return prepareSendToStudentsFromSelectedCurricularCourses(mapping, actionForm, request, response,
                executionYearBean.getExecutionYear());
    }

}
