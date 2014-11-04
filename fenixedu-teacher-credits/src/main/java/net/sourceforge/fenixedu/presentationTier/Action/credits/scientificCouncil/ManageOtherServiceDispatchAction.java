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
 *  Apr 21, 2006
 */
package org.fenixedu.academic.ui.struts.action.credits.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.teacher.OtherService;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "scientificCouncil", path = "/otherServiceManagement",
        functionality = ScientificCouncilViewTeacherCreditsDA.class)
@Forwards(value = { @Forward(name = "editOtherService", path = "/credits/otherService/editOtherService.jsp"),
        @Forward(name = "viewAnnualTeachingCredits", path = "/scientificCouncil/credits.do?method=viewAnnualTeachingCredits") })
@Exceptions(value = { @ExceptionHandling(type = org.fenixedu.academic.domain.exceptions.DomainException.class,
        handler = org.fenixedu.academic.ui.struts.config.FenixDomainExceptionHandler.class, scope = "request") })
public class ManageOtherServiceDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEditOtherService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        OtherService otherService = FenixFramework.getDomainObject((String) getFromRequest(request, "otherServiceOid"));
        if (otherService != null) {
            request.setAttribute("otherService", otherService);
        } else {
            ExecutionSemester executionSemester =
                    FenixFramework.getDomainObject((String) getFromRequest(request, "executionPeriodOid"));
            Teacher teacher = FenixFramework.getDomainObject((String) getFromRequest(request, "teacherId"));
            TeacherService teacherService = TeacherService.getTeacherService(teacher, executionSemester);
            request.setAttribute("teacherService", teacherService);
        }
        return mapping.findForward("editOtherService");
    }

    public ActionForward deleteOtherService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        OtherService otherService = FenixFramework.getDomainObject((String) getFromRequest(request, "otherServiceOid"));
        request.setAttribute("teacherOid", otherService.getTeacherService().getTeacher().getExternalId());
        request.setAttribute("executionYearOid", otherService.getTeacherService().getExecutionPeriod().getExecutionYear()
                .getExternalId());
        otherService.delete();
        return mapping.findForward("viewAnnualTeachingCredits");
    }
}