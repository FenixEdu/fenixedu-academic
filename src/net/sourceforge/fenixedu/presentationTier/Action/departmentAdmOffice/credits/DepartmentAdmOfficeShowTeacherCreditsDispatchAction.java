package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ShowTeacherCreditsDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class DepartmentAdmOfficeShowTeacherCreditsDispatchAction extends ShowTeacherCreditsDispatchAction {    
    
    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException, ParseException {

        DynaActionForm teacherCreditsForm = (DynaActionForm) form;
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID((Integer) teacherCreditsForm.get("executionPeriodId"));
        Teacher teacher = rootDomainObject.readTeacherByOID((Integer) teacherCreditsForm.get("teacherId"));

        if (teacher == null || getTeacherOfManageableDepartments(teacher.getTeacherNumber(), executionPeriod, request) == null) {            
            request.setAttribute("teacherNotFound", "teacherNotFound");            
            return mapping.findForward("teacher-not-found");
        }

        showLinks(request, executionPeriod, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        getAllTeacherCredits(request, executionPeriod, teacher);
        return mapping.findForward("show-teacher-credits");
    }
    
    private Teacher getTeacherOfManageableDepartments(Integer teacherNumber,
            ExecutionPeriod executionPeriod, HttpServletRequest request) {

        IUserView userView = SessionUtils.getUserView(request);
        List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
        Teacher teacher = null;
        for (Department department : manageableDepartments) {
            teacher = department.getTeacherByPeriod(teacherNumber, executionPeriod.getBeginDateYearMonthDay(), executionPeriod.getEndDateYearMonthDay());
            if (teacher != null) {
                break;
            }
        }
        return teacher;
    }
}
