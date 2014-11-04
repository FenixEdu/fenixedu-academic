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
package org.fenixedu.academic.ui.struts.action.credits.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.SupportLesson;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.ui.struts.action.credits.ManageTeacherSupportLessonsDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "departmentAdmOffice", path = "/supportLessonsManagement",
        input = "/supportLessonsManagement.do?method=prepareEdit&page=0", formBean = "supportLessonForm",
        functionality = DepartmentAdmOfficeViewTeacherCreditsDA.class)
@Forwards(value = {
        @Forward(name = "successfull-delete",
                path = "/departmentAdmOffice/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
        @Forward(name = "successfull-edit",
                path = "/departmentAdmOffice/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
        @Forward(name = "edit-support-lesson", path = "/credits/supportLessons/editSupportLesson.jsp"),
        @Forward(name = "list-support-lessons",
                path = "/departmentAdmOffice/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
        @Forward(name = "teacher-not-found", path = "/departmentAdmOffice/credits.do?method=viewAnnualTeachingCredits") })
@Exceptions(
        value = {
                @ExceptionHandling(
                        type = org.fenixedu.academic.ui.struts.action.credits.ManageTeacherSupportLessonsDispatchAction.InvalidPeriodException.class,
                        key = "message.invalidPeriod", handler = org.apache.struts.action.ExceptionHandler.class,
                        path = "/supportLessonsManagement.do?method=prepareEdit&page=0", scope = "request"),
                @ExceptionHandling(type = org.fenixedu.academic.domain.exceptions.DomainException.class,
                        handler = org.fenixedu.academic.ui.struts.config.FenixDomainExceptionHandler.class,
                        scope = "request") })
public class DepartmentAdmOfficeManageTeacherSupportLessonsDispatchAction extends ManageTeacherSupportLessonsDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        String supportLesssonID = (String) supportLessonForm.get("supportLessonID");

        Professorship professorship = getDomainObject(supportLessonForm, "professorshipID");

        if (professorship == null
                || professorship.getTeacher() == null
                || !isTeacherOfManageableDepartments(professorship.getTeacher(), professorship.getExecutionCourse()
                        .getExecutionPeriod(), request)) {

            return mapping.findForward("teacher-not-found");
        }

        SupportLesson supportLesson = null;
        if (!StringUtils.isEmpty(supportLesssonID)) {
            supportLesson = FenixFramework.getDomainObject(supportLesssonID);
            if (!professorship.getSupportLessonsSet().contains(supportLesson)) {
                return mapping.findForward("teacher-not-found");
            }
        }

        prepareToEdit(supportLesson, professorship, supportLessonForm, request);
        return mapping.findForward("edit-support-lesson");
    }

    private boolean isTeacherOfManageableDepartments(Teacher teacher, ExecutionSemester executionSemester,
            HttpServletRequest request) {
        return Authenticate.getUser().getPerson().getManageableDepartmentCreditsSet()
                .contains(teacher.getDepartment(executionSemester.getAcademicInterval()));
    }

    public ActionForward editSupportLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, InvalidPeriodException {

        editSupportLesson(form, request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        return mapping.findForward("successfull-edit");
    }

    public ActionForward deleteSupportLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        deleteSupportLesson(request, form, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        return mapping.findForward("successfull-delete");
    }
}
