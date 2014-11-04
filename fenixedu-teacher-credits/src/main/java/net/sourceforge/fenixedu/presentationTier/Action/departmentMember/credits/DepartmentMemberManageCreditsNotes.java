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

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.ui.struts.action.credits.ManageCreditsNotes;
import org.fenixedu.academic.ui.struts.action.credits.departmentMember.DepartmentMemberViewTeacherCreditsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "departmentMember", path = "/manageCreditsNotes", formBean = "creditsNotesForm",
        functionality = DepartmentMemberViewTeacherCreditsDA.class)
@Forwards({
        @Forward(name = "show-note", path = "/credits/notes/listCreditsNotes.jsp"),
        @Forward(name = "teacher-not-found",
                path = "/departmentMember/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0"),
        @Forward(name = "edit-note", path = "/departmentMember/showFullTeacherCreditsSheet.do?method=showTeacherCredits&page=0") })
public class DepartmentMemberManageCreditsNotes extends ManageCreditsNotes {

    public ActionForward viewNote(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Teacher teacher = FenixFramework.getDomainObject(request.getParameter("teacherId"));
        String executionPeriodId = request.getParameter("executionPeriodId");
        String noteType = request.getParameter("noteType");

        if (teacher == null || teacher != getLoggedTeacher(request)) {
            createNewActionMessage(request);
            return mapping.findForward("teacher-not-found");
        }

        ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodId);
        getNote(actionForm, teacher, executionSemester, noteType);

        return mapping.findForward("show-note");
    }

    public ActionForward editNote(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        Teacher teacher = FenixFramework.getDomainObject((String) dynaActionForm.get("teacherId"));
        String executionPeriodId = (String) dynaActionForm.get("executionPeriodId");
        String noteType = dynaActionForm.getString("noteType");

        return editNote(request, dynaActionForm, teacher, executionPeriodId, RoleType.DEPARTMENT_MEMBER, mapping, noteType);
    }

    private Teacher getLoggedTeacher(HttpServletRequest request) {
        User userView = Authenticate.getUser();
        return userView.getPerson().getTeacher();
    }

    private void createNewActionMessage(HttpServletRequest request) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage("message.invalid.teacher"));
        saveMessages(request, actionMessages);
    }
}
