package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageCreditsNotes;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class DepartmentAdmOfficeManageCreditsNotes extends ManageCreditsNotes {

    public ActionForward viewNote (ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer teacherNumber = Integer.valueOf(request.getParameter("teacherNumber"));
        String executionPeriodId = request.getParameter("executionPeriodId");
        String noteType = request.getParameter("noteType");
                
        Teacher teacher = Teacher.readByNumber(teacherNumber);
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(Integer.valueOf(executionPeriodId));
        
        if (getTeacherOfManageableDepartments(teacherNumber, executionPeriod, request) == null) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            return mapping.findForward("teacher-not-found");
        }
                     
        getNote(actionForm, teacher, executionPeriod, noteType);
        
        return mapping.findForward("show-note");        
    }
    
    public ActionForward editNote (ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        Integer teacherId = (Integer) dynaActionForm.get("teacherId");
        Integer executionPeriodId = (Integer) dynaActionForm.get("executionPeriodId");
        String noteType = dynaActionForm.getString("noteType");
                                      
        return editNote(request, dynaActionForm, teacherId, executionPeriodId, 
                RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE, mapping, noteType);        
    }
    
    private Teacher getTeacherOfManageableDepartments(Integer teacherNumber,
            ExecutionPeriod executionPeriod, HttpServletRequest request) {

        IUserView userView = SessionUtils.getUserView(request);
        List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
        Teacher teacher = null;
        for (Department department : manageableDepartments) {
            teacher = department.getTeacherByPeriod(teacherNumber, executionPeriod.getBeginDateYearMonthDay(),
                    executionPeriod.getEndDateYearMonthDay());
            if (teacher != null) {
                break;
            }
        }
        return teacher;
    }
}
