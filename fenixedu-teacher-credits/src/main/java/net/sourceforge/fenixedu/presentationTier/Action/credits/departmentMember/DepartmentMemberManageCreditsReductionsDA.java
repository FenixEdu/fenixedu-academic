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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.accessControl.DepartmentPresidentStrategy;
import org.fenixedu.academic.domain.credits.util.ReductionServiceBean;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.teacher.ReductionService;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.domain.time.calendarStructure.TeacherCreditsFillingCE;
import org.fenixedu.academic.ui.struts.action.credits.ManageCreditsReductionsDispatchAction;
import org.fenixedu.academic.ui.struts.action.departmentMember.DepartmentMemberPresidentApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = DepartmentMemberPresidentApp.class, path = "credits-reductions",
        titleKey = "label.credits.creditsReduction", bundle = "TeacherCreditsSheetResources")
@Mapping(module = "departmentMember", path = "/creditsReductions")
@Forwards(value = { @Forward(name = "editReductionService", path = "/credits/degreeTeachingService/editCreditsReduction.jsp"),
        @Forward(name = "viewAnnualTeachingCredits", path = "/departmentMember/credits.do?method=viewAnnualTeachingCredits"),
        @Forward(name = "showReductionServices", path = "/credits/reductionService/showReductionServices.jsp"),
        @Forward(name = "showReductionService", path = "/credits/reductionService/showReductionService.jsp") })
public class DepartmentMemberManageCreditsReductionsDA extends ManageCreditsReductionsDispatchAction {

    @EntryPoint
    public ActionForward showReductionServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        User userView = Authenticate.getUser();
        Department department = userView.getPerson().getTeacher().getDepartment();
        List<ReductionService> creditsReductions = new ArrayList<ReductionService>();
        if (department != null && DepartmentPresidentStrategy.isCurrentUserCurrentDepartmentPresident(department)) {
            boolean inValidTeacherCreditsPeriod =
                    TeacherCreditsFillingCE.isInValidCreditsPeriod(executionSemester, RoleType.DEPARTMENT_MEMBER);
            for (Teacher teacher : department.getAllCurrentTeachers()) {
                TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
                if (teacherService != null
                        && teacherService.getReductionService() != null
                        && ((teacherService.getReductionService().getRequestCreditsReduction() != null && teacherService
                                .getReductionService().getRequestCreditsReduction()) || teacherService.getReductionService()
                                .getCreditsReductionAttributed() != null)
                        && (teacherService.getTeacherServiceLock() != null || !inValidTeacherCreditsPeriod)) {
                    creditsReductions.add(teacherService.getReductionService());
                }
            }
        }
        Collections.sort(creditsReductions, new BeanComparator("teacherService.teacher.teacherId"));
        request.setAttribute("creditsReductions", creditsReductions);
        return mapping.findForward("showReductionServices");
    }

    public ActionForward aproveReductionService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        ReductionService reductionService =
                FenixFramework.getDomainObject((String) getFromRequest(request, "reductionServiceOID"));
        ReductionServiceBean reductionServiceBean = null;
        if (reductionService != null) {
            reductionServiceBean = new ReductionServiceBean(reductionService);
        } else {
            reductionServiceBean = getRenderedObject("reductionServiceBean");
            if (reductionServiceBean != null && reductionServiceBean.getTeacher() != null) {
                User userView = Authenticate.getUser();
                Department department = userView.getPerson().getTeacher().getDepartment();
                if (reductionServiceBean.getReductionService() == null) {
                    TeacherService teacherService =
                            TeacherService.getTeacherServiceByExecutionPeriod(reductionServiceBean.getTeacher(),
                                    ExecutionSemester.readActualExecutionSemester());
                    if (teacherService != null) {
                        reductionServiceBean.setReductionService(teacherService.getReductionService());
                    }
                }
            }
        }
        if (request.getParameter("invalidated") == null) {
            RenderUtils.invalidateViewState();
        }
        request.setAttribute("reductionServiceBean", reductionServiceBean);
        return mapping.findForward("showReductionService");
    }

    public ActionForward selectTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        request.setAttribute("reductionServiceBean", new ReductionServiceBean());
        return mapping.findForward("showReductionService");
    }
}
