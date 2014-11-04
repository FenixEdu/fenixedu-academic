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
package org.fenixedu.academic.ui.struts.action.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.department.InsertTeacherPersonalExpectation;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.dto.department.TeacherPersonalExpectationBean;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.TeacherExpectationDefinitionPeriod;
import org.fenixedu.academic.domain.TeacherPersonalExpectationPeriod;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.teacher.TeacherPersonalExpectation;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.departmentMember.DepartmentMemberApp.DepartmentMemberAccompanimentApp;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = DepartmentMemberAccompanimentApp.class, path = "personal-expectations",
        titleKey = "link.personalExpectationsManagement")
@Mapping(module = "departmentMember", path = "/personalExpectationManagement")
@Forwards(value = {
        @Forward(name = "manageResearchAndDevelopmentExpectations",
                path = "/departmentMember/expectationManagement/researchAndDevelopmentExpectationsManagement.jsp"),
        @Forward(name = "viewTeacherPersonalExpectations",
                path = "/departmentMember/expectationManagement/viewTeacherPersonalExpectation.jsp"),
        @Forward(name = "manageProfessionalActivitiesExpectations",
                path = "/departmentMember/expectationManagement/professionalActivitiesExpectationsManagement.jsp"),
        @Forward(name = "manageEducationExpectations",
                path = "/departmentMember/expectationManagement/educationExpectationsManagement.jsp"),
        @Forward(name = "manageUniversityServicesExpectations",
                path = "/departmentMember/expectationManagement/universityServicesExpectationsManagement.jsp") })
public class PersonalExpectationManagement extends FenixDispatchAction {

    @EntryPoint
    public ActionForward viewTeacherPersonalExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Teacher teacher = getLoggedTeacher(request);
        if (teacher != null) {
            ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
            readAndSetTeacherPersonalExpectationByExecutionYear(request, teacher, executionYear);
            request.setAttribute("teacherPersonalExpectationBean", new TeacherPersonalExpectationBean(executionYear, teacher));
        }
        return mapping.findForward("viewTeacherPersonalExpectations");
    }

    public ActionForward viewTeacherPersonalExpectationInSelectedExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationInSelectedExecutionYear");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();
        if (bean != null) {
            ExecutionYear executionYear = bean.getExecutionYear();
            Teacher teacher = getLoggedTeacher(request);
            readAndSetTeacherPersonalExpectationByExecutionYear(request, teacher, executionYear);
            request.setAttribute("teacherPersonalExpectationBean", bean);
        }
        return mapping.findForward("viewTeacherPersonalExpectations");
    }

    public ActionForward prepareDefineTeacherPersonalExpection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        Teacher teacher = getLoggedTeacher(request);
        if (teacher != null) {
            ExecutionYear executionYear = getExecutionYearFromParameter(request);
            if (TeacherPersonalExpectation.getTeacherPersonalExpectationByExecutionYear(teacher, executionYear) == null) {
                request.setAttribute("teacherPersonalExpectationBean", new TeacherPersonalExpectationBean(executionYear, teacher));
            }
        }
        return mapping.findForward("manageEducationExpectations");
    }

    public ActionForward prepareManageResearchAndDevelopment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationWithEducationMainFocus");
        if (viewState == null) {
            viewState = RenderUtils.getViewState();
        }
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();
        request.setAttribute("teacherPersonalExpectationBean", bean);
        return mapping.findForward("manageResearchAndDevelopmentExpectations");
    }

    public ActionForward prepareManageUniversityServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationWithOrientationMainFocus");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();
        request.setAttribute("teacherPersonalExpectationBean", bean);
        return mapping.findForward("manageUniversityServicesExpectations");
    }

    public ActionForward prepareManageProfessionalActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationWithMainFocusUniversityServices");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();
        request.setAttribute("teacherPersonalExpectationBean", bean);
        return mapping.findForward("manageProfessionalActivitiesExpectations");
    }

    public ActionForward createTeacherPersonalExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationWithMainFocusProfessionalActivities");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();
        TeacherPersonalExpectation teacherPersonalExpectation = null;

        try {
            teacherPersonalExpectation = InsertTeacherPersonalExpectation.runInsertTeacherPersonalExpectation(bean);

        } catch (DomainException exception) {
            saveMessages(request, exception);
        }

        request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        request.setAttribute("teacherPersonalExpectationBean", bean);
        return mapping.findForward("viewTeacherPersonalExpectations");
    }

    public ActionForward prepareEditEducationExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixActionException {

        TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
        checkTeacherAndPeriodToEdit(request, teacherPersonalExpectation);
        request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        return mapping.findForward("manageEducationExpectations");
    }

    public ActionForward editEducationExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        return viewTeacherPersonalExpectation(request, "teacherPersonalExpectationWithEducationMainFocus", mapping);
    }

    public ActionForward prepareEditResearchAndDevelopmentExpectations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixActionException {

        TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
        checkTeacherAndPeriodToEdit(request, teacherPersonalExpectation);
        request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        return mapping.findForward("manageResearchAndDevelopmentExpectations");
    }

    public ActionForward editResearchAndDevelopmentExpectations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        return viewTeacherPersonalExpectation(request, "teacherPersonalExpectationWithOrientationMainFocus", mapping);
    }

    public ActionForward prepareEditUniversityServicesExpectations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixActionException {

        TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
        checkTeacherAndPeriodToEdit(request, teacherPersonalExpectation);
        request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        return mapping.findForward("manageUniversityServicesExpectations");
    }

    public ActionForward editUniversityServicesExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        return viewTeacherPersonalExpectation(request, "teacherPersonalExpectationWithMainFocusUniversityServices", mapping);
    }

    public ActionForward prepareEditProfessionalActivitiesExpectations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixActionException {

        TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
        checkTeacherAndPeriodToEdit(request, teacherPersonalExpectation);
        request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        return mapping.findForward("manageProfessionalActivitiesExpectations");
    }

    public ActionForward editProfessionalActivitiesExpectations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        return viewTeacherPersonalExpectation(request, "teacherPersonalExpectationWithMainFocusProfessionalActivities", mapping);
    }

    // Private Methods

    private ActionForward viewTeacherPersonalExpectation(HttpServletRequest request, String viewStateName, ActionMapping mapping) {
        IViewState viewState = RenderUtils.getViewState(viewStateName);
        TeacherPersonalExpectation teacherPersonalExpectation =
                (TeacherPersonalExpectation) viewState.getMetaObject().getObject();
        Teacher teacher = teacherPersonalExpectation.getTeacher();
        ExecutionYear executionYear = teacherPersonalExpectation.getExecutionYear();
        request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        request.setAttribute("teacherPersonalExpectationBean", new TeacherPersonalExpectationBean(executionYear, teacher));
        return mapping.findForward("viewTeacherPersonalExpectations");
    }

    private void readAndSetTeacherPersonalExpectationByExecutionYear(HttpServletRequest request, Teacher teacher,
            ExecutionYear executionYear) {
        TeacherPersonalExpectation teacherPersonalExpectation =
                TeacherPersonalExpectation.getTeacherPersonalExpectationByExecutionYear(teacher, executionYear);
        Department department = teacher.getDepartment();
        if (department != null) {
            TeacherExpectationDefinitionPeriod period =
                    TeacherPersonalExpectationPeriod.getTeacherExpectationDefinitionPeriodForExecutionYear(department,
                            executionYear);
            request.setAttribute("periodOpen", period != null ? period.isPeriodOpen().booleanValue() : false);
            request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        }
    }

    private void checkTeacherAndPeriodToEdit(HttpServletRequest request, TeacherPersonalExpectation teacherPersonalExpectation)
            throws FenixActionException {
        ExecutionYear executionYear = teacherPersonalExpectation.getExecutionYear();
        Department department = teacherPersonalExpectation.getTeacher().getDepartment();
        if (department != null) {
            TeacherExpectationDefinitionPeriod period =
                    TeacherPersonalExpectationPeriod.getTeacherExpectationDefinitionPeriodForExecutionYear(department,
                            executionYear);
            if (period == null || !period.isPeriodOpen()
                    || !getLoggedTeacher(request).equals(teacherPersonalExpectation.getTeacher())) {
                throw new FenixActionException();
            }
        } else {
            throw new FenixActionException();
        }
    }

    private Teacher getLoggedTeacher(HttpServletRequest request) {
        Person loggedPerson = getLoggedPerson(request);
        return (loggedPerson != null) ? loggedPerson.getTeacher() : null;
    }

    private ExecutionYear getExecutionYearFromParameter(final HttpServletRequest request) {
        final String executionYearIDString = request.getParameter("executionYearID");
        return FenixFramework.getDomainObject(executionYearIDString);
    }

    private TeacherPersonalExpectation getTeacherPersonalExpectationFromParameter(final HttpServletRequest request) {
        final String teacherPersonalExpectationIDString = request.getParameter("teacherPersonalExpectationID");
        return FenixFramework.getDomainObject(teacherPersonalExpectationIDString);
    }

    private void saveMessages(HttpServletRequest request, DomainException e) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
        saveMessages(request, actionMessages);
    }
}
