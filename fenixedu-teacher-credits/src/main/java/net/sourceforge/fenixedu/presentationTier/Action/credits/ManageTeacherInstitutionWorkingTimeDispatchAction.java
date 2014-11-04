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
/**
 * Nov 23, 2005
 */
package org.fenixedu.academic.ui.struts.action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.teacher.InstitutionWorkTime;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Ricardo Rodrigues
 * 
 */

@Forwards({
        @Forward(name = "viewAnnualTeachingCredits", path = "/credits.do?method=viewAnnualTeachingCredits",
                contextRelative = false),
        @Forward(name = "edit-institution-work-time", path = "/credits/workingTime/editTeacherInstitutionWorkTime.jsp") })
public class ManageTeacherInstitutionWorkingTimeDispatchAction extends FenixDispatchAction {

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws NumberFormatException, FenixServiceException {
        Teacher teacher = FenixFramework.getDomainObject((String) getFromRequest(request, "teacherId"));
        ExecutionSemester executionPeriod = FenixFramework.getDomainObject((String) getFromRequest(request, "executionPeriodId"));
        TeacherService teacherService = TeacherService.getTeacherService(teacher, executionPeriod);
        request.setAttribute("teacherService", teacherService);
        return mapping.findForward("edit-institution-work-time");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        InstitutionWorkTime institutionWorkTime =
                FenixFramework.getDomainObject((String) getFromRequest(request, "institutionWorkTimeOid"));
        request.setAttribute("institutionWorkTime", institutionWorkTime);
        return mapping.findForward("edit-institution-work-time");
    }

    protected ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, RoleType roleType) throws NumberFormatException, FenixServiceException {
        InstitutionWorkTime institutionWorkTime =
                FenixFramework.getDomainObject((String) getFromRequest(request, "institutionWorkTimeOid"));
        request.setAttribute("teacherOid", institutionWorkTime.getTeacherService().getTeacher().getExternalId());
        request.setAttribute("executionYearOid", institutionWorkTime.getTeacherService().getExecutionPeriod().getExecutionYear()
                .getExternalId());
        institutionWorkTime.delete(roleType);
        return mapping.findForward("viewAnnualTeachingCredits");
    }

}
