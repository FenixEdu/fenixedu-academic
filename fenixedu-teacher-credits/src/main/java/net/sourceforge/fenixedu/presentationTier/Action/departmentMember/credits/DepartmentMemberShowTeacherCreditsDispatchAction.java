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

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.ui.struts.action.credits.ShowTeacherCreditsDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

public class DepartmentMemberShowTeacherCreditsDispatchAction extends ShowTeacherCreditsDispatchAction {

    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, ParseException {

        DynaActionForm teacherCreditsForm = (DynaActionForm) form;
        ExecutionSemester executionSemester =
                FenixFramework.getDomainObject((String) teacherCreditsForm.get("executionPeriodId"));

        Teacher requestedTeacher = FenixFramework.getDomainObject((String) teacherCreditsForm.get("teacherId"));

        User userView = Authenticate.getUser();
        Teacher loggedTeacher = userView.getPerson().getTeacher();

        if (requestedTeacher == null || loggedTeacher != requestedTeacher) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage("message.invalid.teacher"));
            saveMessages(request, actionMessages);
            return mapping.findForward("teacher-not-found");
        }

        showLinks(request, executionSemester, RoleType.DEPARTMENT_MEMBER);
        getAllTeacherCredits(request, executionSemester, requestedTeacher);
        return mapping.findForward("show-teacher-credits");
    }
}
