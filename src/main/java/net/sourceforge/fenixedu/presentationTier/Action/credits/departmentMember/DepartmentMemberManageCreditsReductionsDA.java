package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.util.ReductionServiceBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.ReductionService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageCreditsReductionsDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "departmentMember", path = "/creditsReductions", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "editReductionService", path = "/credits/degreeTeachingService/editCreditsReduction.jsp"),
        @Forward(name = "viewAnnualTeachingCredits", path = "/credits.do?method=viewAnnualTeachingCredits"),
        @Forward(name = "showReductionServices", path = "/credits/reductionService/showReductionServices.jsp"),
        @Forward(name = "showReductionService", path = "/credits/reductionService/showReductionService.jsp") })
public class DepartmentMemberManageCreditsReductionsDA extends ManageCreditsReductionsDispatchAction {

    public ActionForward showReductionServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        IUserView userView = UserView.getUser();
        Department department = userView.getPerson().getTeacher().getCurrentWorkingDepartment();
        List<ReductionService> creditsReductions = new ArrayList<ReductionService>();
        if (department != null && department.isCurrentUserCurrentDepartmentPresident()) {
            for (Teacher teacher : department.getAllCurrentTeachers()) {
                TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
                if (teacherService != null && teacherService.getReductionService() != null
                        && teacherService.getReductionService().getRequestCreditsReduction()) {
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
                IUserView userView = UserView.getUser();
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
