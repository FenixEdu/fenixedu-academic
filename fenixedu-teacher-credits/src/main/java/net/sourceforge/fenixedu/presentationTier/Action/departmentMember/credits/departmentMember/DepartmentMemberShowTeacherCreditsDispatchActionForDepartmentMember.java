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
package org.fenixedu.academic.ui.struts.action.departmentMember.credits.departmentMember;

import org.fenixedu.academic.ui.struts.action.credits.departmentMember.DepartmentMemberViewTeacherCreditsDA;
import org.fenixedu.academic.ui.struts.action.departmentMember.credits.DepartmentMemberShowTeacherCreditsDispatchAction;

import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/showFullTeacherCreditsSheet", formBean = "teacherCreditsForm",
        functionality = DepartmentMemberViewTeacherCreditsDA.class)
@Forwards({
        @Forward(name = "show-teacher-credits", path = "/departmentMember/credits/showTeacherCredits.jsp"),
        @Forward(name = "teacher-not-found",
                path = "/departmentMember/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0") })
public class DepartmentMemberShowTeacherCreditsDispatchActionForDepartmentMember extends
        DepartmentMemberShowTeacherCreditsDispatchAction {
}