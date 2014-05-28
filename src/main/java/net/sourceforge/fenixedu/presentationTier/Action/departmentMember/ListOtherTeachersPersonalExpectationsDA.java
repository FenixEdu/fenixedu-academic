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
package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.commons.ExecutionYearBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherPersonalExpectationsVisualizationPeriod;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.ListTeachersPersonalExpectationsDA;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp.DepartmentMemberAccompanimentApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
        Department teacherWorkingDepartment =
                teacher.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay());

        TeacherPersonalExpectationsVisualizationPeriod visualizationPeriod =
                loggedTeacherDepartment.getTeacherPersonalExpectationsVisualizationPeriodByExecutionYear(executionYear);

        if (visualizationPeriod != null && visualizationPeriod.isPeriodOpen() && teacherWorkingDepartment != null
                && teacherWorkingDepartment.equals(loggedTeacherDepartment)) {

            request.setAttribute("noEdit", true);
            request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        }

        return mapping.findForward("seeTeacherPersonalExpectationsByYear");
    }

    private Department getDepartment(HttpServletRequest request) {
        return getLoggedPerson(request).getTeacher().getCurrentWorkingDepartment();
    }

    @Override
    protected ActionForward readAndSetList(ActionMapping mapping, HttpServletRequest request, ExecutionYear executionYear) {
        Department department = getDepartment(request);
        TeacherPersonalExpectationsVisualizationPeriod visualizationPeriod = null;
        if (department != null) {
            visualizationPeriod = department.getTeacherPersonalExpectationsVisualizationPeriodByExecutionYear(executionYear);
        }

        if (visualizationPeriod == null || !visualizationPeriod.isPeriodOpen()) {
            request.setAttribute("executionYearBean", new ExecutionYearBean(executionYear));
            return mapping.findForward("listTeacherPersonalExpectations");
        } else {
            return super.readAndSetList(mapping, request, executionYear);
        }
    }
}
