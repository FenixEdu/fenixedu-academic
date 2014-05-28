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
package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.util.ReductionServiceBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.ReductionService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageCreditsReductionsDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp.DepartmentMemberPresidentApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
        Department department = userView.getPerson().getTeacher().getCurrentWorkingDepartment();
        List<ReductionService> creditsReductions = new ArrayList<ReductionService>();
        if (department != null && department.isCurrentUserCurrentDepartmentPresident()) {
            boolean inValidTeacherCreditsPeriod = executionSemester.isInValidCreditsPeriod(RoleType.DEPARTMENT_MEMBER);
            for (Teacher teacher : department.getAllCurrentTeachers()) {
                TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
                if (teacherService != null && teacherService.getReductionService() != null
                        && teacherService.getReductionService().getRequestCreditsReduction()
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
                Department department = userView.getPerson().getTeacher().getCurrentWorkingDepartment();
                if (!isTeacherFromDepartment(reductionServiceBean.getTeacher(), department)) {
                    addActionMessage("error", request, "message.teacher.not-found-or-not-belong-to-department");
                    return selectTeacher(mapping, form, request, response);
                }
                if (reductionServiceBean.getReductionService() == null) {
                    TeacherService teacherService =
                            reductionServiceBean.getTeacher().getTeacherServiceByExecutionPeriod(
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

    private boolean isTeacherFromDepartment(Teacher teacher, Department department) {
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        List<Unit> workingPlacesByPeriod =
                teacher.getWorkingPlacesByPeriod(executionSemester.getBeginDateYearMonthDay(),
                        executionSemester.getEndDateYearMonthDay());
        for (Unit unit : workingPlacesByPeriod) {
            DepartmentUnit departmentUnit = unit.getDepartmentUnit();
            Department teacherDepartment = departmentUnit != null ? departmentUnit.getDepartment() : null;
            if (teacherDepartment != null && teacherDepartment.equals(department)) {
                return true;
            }
        }
        return false;
    }

}
