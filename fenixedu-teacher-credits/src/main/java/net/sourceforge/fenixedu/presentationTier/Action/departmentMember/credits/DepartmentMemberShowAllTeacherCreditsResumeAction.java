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
package org.fenixedu.academic.ui.struts.action.departmentMember.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.ui.struts.action.credits.ShowAllTeacherCreditsResumeAction;
import org.fenixedu.academic.ui.struts.action.credits.departmentMember.DepartmentMemberViewTeacherCreditsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/showAllTeacherCreditsResume",
        functionality = DepartmentMemberViewTeacherCreditsDA.class)
@Forwards(
        value = { @Forward(name = "show-all-credits-resume", path = "/departmentMember/credits/listAllTeacherCreditsResume.jsp") })
public class DepartmentMemberShowAllTeacherCreditsResumeAction extends ShowAllTeacherCreditsResumeAction {

    public ActionForward showTeacherCreditsResume(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();
        Teacher teacher = userView.getPerson().getTeacher();
        readAllTeacherCredits(request, teacher);
        return mapping.findForward("show-all-credits-resume");
    }
}
