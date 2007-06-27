/**
 * Nov 23, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

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
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherInstitutionWorkingTimeDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DepartmentAdmOfficeManageTeacherInstitutionWorkingTimeDispatchAction extends
        ManageTeacherInstitutionWorkingTimeDispatchAction {

    public ActionForward showTeacherWorkingTimePeriods(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm institutionWorkingTimeForm = (DynaActionForm) form;
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(Integer
                .valueOf((String) institutionWorkingTimeForm.get("executionPeriodId")));

        Integer teacherNumber = Integer.valueOf(institutionWorkingTimeForm.getString("teacherNumber"));
        Teacher teacher = Teacher.readByNumber(teacherNumber);

        if (getTeacherOfManageableDepartments(teacherNumber, executionPeriod, request) == null) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            return mapping.findForward("teacher-not-found");
        }

        getInstitutionWokTimeList(request, institutionWorkingTimeForm, executionPeriod, teacher);
        return mapping.findForward("list-teacher-institution-working-time");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException,
            FenixServiceException {

        DynaActionForm institutionWorkingTimeForm = (DynaActionForm) form;
        Integer institutionWorkingTimeID = (Integer) institutionWorkingTimeForm
                .get("institutionWorkTimeID");
        Integer teacherID = Integer.valueOf(institutionWorkingTimeForm.getString("teacherId"));
        Integer executionPeriodID = Integer.valueOf(institutionWorkingTimeForm
                .getString("executionPeriodId"));

        Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);

        if (teacher == null
                || getTeacherOfManageableDepartments(teacher.getTeacherNumber(), executionPeriod, request) == null) {
            return mapping.findForward("teacher-not-found");
        }

        InstitutionWorkTime institutionWorkTime = null;
        if (institutionWorkingTimeID != null && institutionWorkingTimeID != 0) {
            institutionWorkTime = (InstitutionWorkTime) rootDomainObject
                    .readTeacherServiceItemByOID(institutionWorkingTimeID);
            if (!teacher.getTeacherServiceByExecutionPeriod(executionPeriod).getInstitutionWorkTimes()
                    .contains(institutionWorkTime)) {
                return mapping.findForward("teacher-not-found");
            }
        }

        prepareToEdit(institutionWorkTime, teacher, executionPeriod, request, institutionWorkingTimeForm);
        return mapping.findForward("edit-institution-work-time");
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
    
    public ActionForward editInstitutionWorkingTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException, InvalidPeriodException {

        editInstitutionWorkingTime(form, request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        return mapping.findForward("successfull-edit");
       
    }
    
    public ActionForward deleteInstitutionWorkingTime(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {
        
        deleteInstitutionWorkingTime(form, request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        return mapping.findForward("successfull-delete");
    }      
}