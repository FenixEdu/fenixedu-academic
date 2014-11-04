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
package org.fenixedu.academic.ui.struts.action.credits.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.teacher.InstitutionWorkTime;
import org.fenixedu.academic.ui.struts.action.credits.ManageTeacherInstitutionWorkingTimeDispatchAction;
import org.fenixedu.academic.ui.struts.config.FenixDomainExceptionHandler;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "departmentMember", path = "/institutionWorkingTimeManagement",
        input = "/institutionWorkingTimeManagement.do?method=prepareEdit&page=0", formBean = "teacherInstitutionWorkingTimeForm",
        functionality = DepartmentMemberViewTeacherCreditsDA.class)
@Exceptions({ @ExceptionHandling(type = DomainException.class, handler = FenixDomainExceptionHandler.class, scope = "request") })
public class DepartmentMemberManageTeacherInstitutionWorkingTimeDispatchAction extends
        ManageTeacherInstitutionWorkingTimeDispatchAction {

    @Override
    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        InstitutionWorkTime institutionWorkTime =
                FenixFramework.getDomainObject((String) getFromRequest(request, "institutionWorkTimeOid"));
        Teacher teacher = institutionWorkTime.getTeacherService().getTeacher();
        if (teacher == null || teacher != getLoggedTeacher(request)) {
            createNewActionMessage(request);
            return mapping.findForward("viewAnnualTeachingCredits");
        }
        request.setAttribute("institutionWorkTime", institutionWorkTime);
        return mapping.findForward("edit-institution-work-time");
    }

    private void createNewActionMessage(HttpServletRequest request) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage("message.invalid.teacher"));
        saveMessages(request, actionMessages);
    }

    private Teacher getLoggedTeacher(HttpServletRequest request) {
        User userView = Authenticate.getUser();
        return userView.getPerson().getTeacher();
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws NumberFormatException, FenixServiceException {
        return delete(mapping, form, request, response, RoleType.DEPARTMENT_MEMBER);
    }

}
