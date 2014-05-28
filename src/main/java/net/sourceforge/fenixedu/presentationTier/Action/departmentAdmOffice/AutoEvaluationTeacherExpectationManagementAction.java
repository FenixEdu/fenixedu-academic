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
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.TeacherAutoEvaluationDefinitionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DepartmentAdmOfficeApp.DefineExpectationPeriods;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/autoEvaluationTeacherExpectationManagementAction",
        functionality = DefineExpectationPeriods.class)
@Forwards({
        @Forward(
                name = "showDefinitionPeriod",
                path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/showAutoEvaluationPeriodDefinition.jsp"),
        @Forward(
                name = "editDefinitionPeriod",
                path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/editAutoEvaluationPeriodDefinition.jsp") })
public class AutoEvaluationTeacherExpectationManagementAction extends TeacherPersonalExpectationsDefinitionPeriodDA {

    @Override
    protected void readAndSetPeriod(HttpServletRequest request, ExecutionYear year) {
        if (year != null) {
            TeacherAutoEvaluationDefinitionPeriod teacherExpectationDefinitionPeriod =
                    getDepartment(request).getTeacherAutoEvaluationDefinitionPeriodForExecutionYear(year);
            request.setAttribute("period", teacherExpectationDefinitionPeriod);
        }
    }
}
