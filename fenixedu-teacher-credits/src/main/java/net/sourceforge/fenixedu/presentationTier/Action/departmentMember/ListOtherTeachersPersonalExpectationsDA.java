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

import org.fenixedu.academic.dto.commons.ExecutionYearBean;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.TeacherPersonalExpectationPeriod;
import org.fenixedu.academic.domain.TeacherPersonalExpectationsVisualizationPeriod;
import org.fenixedu.academic.domain.teacher.TeacherPersonalExpectation;
import org.fenixedu.academic.ui.struts.action.departmentAdmOffice.ListTeachersPersonalExpectationsDA;
import org.fenixedu.academic.ui.struts.action.departmentMember.DepartmentMemberApp.DepartmentMemberAccompanimentApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = DepartmentMemberAccompanimentApp.class, path = "list-teachers-personal-expectations",
        titleKey = "label.see.teachers.personal.expectations")
@Mapping(module = "departmentMember", path = "/listTeachersPersonalExpectations", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "seeTeacherPersonalExpectationsByYear",
                path = "/departmentMember/expectationManagement/seeTeacherPersonalExpectations.jsp"),
        @Forward(name = "listTeacherPersonalExpectations",
                path = "/departmentMember/expectationManagement/listTeacherPersonalExpectations.jsp") })
public class ListOtherTeachersPersonalExpectationsDA extends ListTeachersPersonalExpectationsDA {

    @Override
    public ActionForward listTeachersPersonalExpectationsForSelectedExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("executionYear");
        ExecutionYear executionYear = (ExecutionYear) viewState.getMetaObject().getObject();
        return readAndSetList(mapping, request, executionYear);
    }

    @Override
    public ActionForward listTeachersPersonalExpectationsByExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = getExecutionYearFromParameter(request);
        return readAndSetList(mapping, request, executionYear);
    }

    @Override
    public ActionForward listTeachersPersonalExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        return readAndSetList(mapping, request, executionYear);
    }

    @Override
    public ActionForward seeTeacherPersonalExpectation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
        ExecutionYear executionYear = teacherPersonalExpectation.getExecutionYear();
        Teacher teacher = teacherPersonalExpectation.getTeacher();

        Department loggedTeacherDepartment = getDepartment(request);
        Department teacherWorkingDepartment = teacher.getLastDepartment(executionYear.getAcademicInterval());

        TeacherPersonalExpectationsVisualizationPeriod visualizationPeriod =
                TeacherPersonalExpectationPeriod.getTeacherPersonalExpectationsVisualizationPeriodByExecutionYear(
                        loggedTeacherDepartment, executionYear);

        if (visualizationPeriod != null && visualizationPeriod.isPeriodOpen() && teacherWorkingDepartment != null
                && teacherWorkingDepartment.equals(loggedTeacherDepartment)) {

            request.setAttribute("noEdit", true);
            request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        }

        return mapping.findForward("seeTeacherPersonalExpectationsByYear");
    }

    private Department getDepartment(HttpServletRequest request) {
        return getLoggedPerson(request).getTeacher().getDepartment();
    }

    @Override
    protected ActionForward readAndSetList(ActionMapping mapping, HttpServletRequest request, ExecutionYear executionYear) {
        Department department = getDepartment(request);
        TeacherPersonalExpectationsVisualizationPeriod visualizationPeriod = null;
        if (department != null) {
            visualizationPeriod =
                    TeacherPersonalExpectationPeriod.getTeacherPersonalExpectationsVisualizationPeriodByExecutionYear(department,
                            executionYear);
        }

        if (visualizationPeriod == null || !visualizationPeriod.isPeriodOpen()) {
            request.setAttribute("executionYearBean", new ExecutionYearBean(executionYear));
            return mapping.findForward("listTeacherPersonalExpectations");
        } else {
            return super.readAndSetList(mapping, request, executionYear);
        }
    }
}
