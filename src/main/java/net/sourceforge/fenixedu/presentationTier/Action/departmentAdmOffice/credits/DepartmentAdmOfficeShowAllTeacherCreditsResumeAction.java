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
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ShowAllTeacherCreditsResumeAction;
import net.sourceforge.fenixedu.presentationTier.Action.credits.departmentAdmOffice.DepartmentAdmOfficeViewTeacherCreditsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/showAllTeacherCreditsResume", formBean = "teacherSearchForm",
        functionality = DepartmentAdmOfficeViewTeacherCreditsDA.class)
@Forwards(value = { @Forward(name = "search-teacher-form", path = "search-for-teacher-credits"),
        @Forward(name = "teacher-not-found", path = "search-for-teacher-credits"),
        @Forward(name = "show-all-credits-resume", path = "/credits/commons/listAllTeacherCreditsResume.jsp") })
@Exceptions(value = { @ExceptionHandling(type = java.lang.NumberFormatException.class, key = "errors.invalid.teacher-number",
        handler = org.apache.struts.action.ExceptionHandler.class,
        path = "/showAllTeacherCreditsResume.do?method=prepareTeacherSearch&page=0", scope = "request") })
public class DepartmentAdmOfficeShowAllTeacherCreditsResumeAction extends ShowAllTeacherCreditsResumeAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("method", "showTeacherCreditsResume");
        return mapping.findForward("search-teacher-form");
    }

    public ActionForward showTeacherCreditsResume(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        Teacher teacher = Teacher.readByIstId(dynaActionForm.getString("teacherId").trim());

        if (teacher == null || !isTeacherOfManageableDepartments(teacher, executionSemester, request)) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            dynaActionForm.set("method", "showTeacherCreditsResume");
            return mapping.findForward("teacher-not-found");
        }

        readAllTeacherCredits(request, teacher);
        return mapping.findForward("show-all-credits-resume");
    }

    private boolean isTeacherOfManageableDepartments(Teacher teacher, ExecutionSemester executionSemester,
            HttpServletRequest request) {
        User userView = Authenticate.getUser();
        Collection<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
        List<Unit> workingPlacesByPeriod =
                teacher.getWorkingPlacesByPeriod(executionSemester.getBeginDateYearMonthDay(),
                        executionSemester.getEndDateYearMonthDay());
        for (Unit unit : workingPlacesByPeriod) {
            DepartmentUnit departmentUnit = unit.getDepartmentUnit();
            Department teacherDepartment = departmentUnit != null ? departmentUnit.getDepartment() : null;
            if (teacherDepartment != null && manageableDepartments.contains(teacherDepartment)) {
                return true;
            }
        }
        return false;
    }
}
