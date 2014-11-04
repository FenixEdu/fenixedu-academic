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
package org.fenixedu.academic.ui.struts.action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.domain.teacher.TeacherServiceComment;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.FenixFramework;

public class ManageTeacherServiceCommentsDispatchAction extends FenixDispatchAction {

    public ActionForward editTeacherServiceComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        TeacherServiceComment teacherServiceComment =
                FenixFramework.getDomainObject((String) getFromRequest(request, "teacherServiceCommentOid"));
        if (teacherServiceComment != null) {
            request.setAttribute("teacherServiceComment", teacherServiceComment);
        } else {
            Teacher teacher = FenixFramework.getDomainObject((String) getFromRequest(request, "teacherOid"));
            ExecutionYear executionYear = FenixFramework.getDomainObject((String) getFromRequest(request, "executionYearOid"));

            ExecutionSemester firstExecutionPeriod = executionYear.getFirstExecutionPeriod();
            TeacherService teacherService = TeacherService.getTeacherService(teacher, firstExecutionPeriod);

            request.setAttribute("teacherService", teacherService);
        }
        return mapping.findForward("editTeacherServiceComment");
    }

    public ActionForward deleteTeacherServiceComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        TeacherServiceComment teacherServiceComment =
                FenixFramework.getDomainObject((String) getFromRequest(request, "teacherServiceCommentOid"));
        request.setAttribute("teacherOid", teacherServiceComment.getTeacherService().getTeacher().getExternalId());
        request.setAttribute("executionYearOid", teacherServiceComment.getTeacherService().getExecutionPeriod()
                .getExecutionYear().getExternalId());
        if (teacherServiceComment != null) {
            teacherServiceComment.delete();
        }
        return mapping.findForward("viewAnnualTeachingCredits");
    }
}
