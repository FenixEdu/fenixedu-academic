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
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.ui.struts.action.credits.ManageDegreeTeachingServicesDispatchAction;

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

@Mapping(module = "departmentAdmOffice", path = "/degreeTeachingServiceManagement",
        input = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails",
        formBean = "teacherExecutionCourseShiftProfessorshipForm", functionality = DepartmentAdmOfficeViewTeacherCreditsDA.class)
@Forwards(value = {
        @Forward(name = "teacher-not-found", path = "/departmentAdmOffice/credits.do?method=viewAnnualTeachingCredits"),
        @Forward(name = "sucessfull-edit", path = "/departmentAdmOffice/credits.do?method=viewAnnualTeachingCredits"),
        @Forward(name = "show-teaching-service-percentages",
                path = "/credits/degreeTeachingService/showTeachingServicePercentages.jsp") })
@Exceptions(value = { @ExceptionHandling(type = java.lang.NumberFormatException.class,
        key = "message.invalid.professorship.percentage", handler = org.apache.struts.action.ExceptionHandler.class,
        path = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails&page=0", scope = "request") })
public class DepartmentAdmOfficeManageDegreeTeachingServicesDispatchAction extends ManageDegreeTeachingServicesDispatchAction {

    public ActionForward showTeachingServiceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        Professorship professorship = getDomainObject(dynaForm, "professorshipID");

        if (professorship == null
                || professorship.getTeacher() == null
                || !isTeacherOfManageableDepartments(professorship.getTeacher(), professorship.getExecutionCourse()
                        .getExecutionPeriod(), request)) {
            return mapping.findForward("teacher-not-found");
        }

        teachingServiceDetailsProcess(professorship, request, dynaForm);
        return mapping.findForward("show-teaching-service-percentages");
    }

    private boolean isTeacherOfManageableDepartments(Teacher teacher, ExecutionSemester executionSemester,
            HttpServletRequest request) {
        return Authenticate.getUser().getPerson().getManageableDepartmentCreditsSet()
                .contains(teacher.getDepartment(executionSemester.getAcademicInterval()));
    }

    public ActionForward updateTeachingServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        return updateTeachingServices(mapping, form, request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
    }
}
