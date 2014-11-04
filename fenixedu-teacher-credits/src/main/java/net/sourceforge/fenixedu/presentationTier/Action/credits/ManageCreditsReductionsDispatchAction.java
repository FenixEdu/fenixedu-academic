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

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.teacher.ReductionService;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.credits.departmentMember.DepartmentMemberViewTeacherCreditsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "departmentMember", path = "/membersCreditsReductions",
        functionality = DepartmentMemberViewTeacherCreditsDA.class)
@Forward(name = "editReductionService", path = "/credits/degreeTeachingService/editCreditsReduction.jsp")
public class ManageCreditsReductionsDispatchAction extends FenixDispatchAction {

    public ActionForward editCreditsReduction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionSemester executionSemester =
                FenixFramework.getDomainObject((String) getFromRequest(request, "executionPeriodOID"));
        Teacher teacher = FenixFramework.getDomainObject((String) getFromRequest(request, "teacherOID"));
        TeacherService teacherService = TeacherService.getTeacherService(teacher, executionSemester);
        ReductionService reductionService = teacherService.getReductionService();
        if (reductionService != null) {
            request.setAttribute("reductionService", reductionService);
        } else {
            request.setAttribute("teacherService", teacherService);
        }
        return mapping.findForward("editReductionService");
    }
}
