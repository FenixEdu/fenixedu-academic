package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherSupportLessonsDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class DepartmentAdmOfficeManageTeacherSupportLessonsDispatchAction extends
        ManageTeacherSupportLessonsDispatchAction {

    public ActionForward showSupportLessons(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        Integer professorshipID = (Integer) supportLessonForm.get("professorshipID");
        Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);

        if (professorship == null
                || getTeacherOfManageableDepartments(professorship.getTeacher().getTeacherNumber(),
                        professorship.getExecutionCourse().getExecutionPeriod(), request) == null) {
            return mapping.findForward("teacher-not-found");
        }

        getSupportLessons(request, professorship);
        return mapping.findForward("list-support-lessons");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException,
            FenixServiceException {

        DynaActionForm supportLessonForm = (DynaActionForm) form;
        Integer supportLesssonID = (Integer) supportLessonForm.get("supportLessonID");
        Integer professorshipID = (Integer) supportLessonForm.get("professorshipID");

        Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);

        if (professorship == null
                || getTeacherOfManageableDepartments(professorship.getTeacher().getTeacherNumber(),
                        professorship.getExecutionCourse().getExecutionPeriod(), request) == null) {
            
            return mapping.findForward("teacher-not-found");
        }

        SupportLesson supportLesson = null;
        if (supportLesssonID != null && supportLesssonID != 0) {
            supportLesson = rootDomainObject.readSupportLessonByOID(supportLesssonID);
            if (!professorship.getSupportLessons().contains(supportLesson)) {
                return mapping.findForward("teacher-not-found");
            }
        }

        prepareToEdit(supportLesson, professorship, supportLessonForm, request);
        return mapping.findForward("edit-support-lesson");
    }

    private Teacher getTeacherOfManageableDepartments(Integer teacherNumber,
            ExecutionSemester executionSemester, HttpServletRequest request) {

        IUserView userView = UserView.getUser();
        List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
        Teacher teacher = null;
        for (Department department : manageableDepartments) {
            teacher = department.getTeacherByPeriod(teacherNumber, executionSemester.getBeginDateYearMonthDay(),
                    executionSemester.getEndDateYearMonthDay());
            if (teacher != null) {
                break;
            }
        }
        return teacher;
    }
    
    public ActionForward editSupportLesson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException, InvalidPeriodException {
        
        editSupportLesson(form, request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        return mapping.findForward("successfull-edit");
    }   
    
    public ActionForward deleteSupportLesson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,            
            FenixFilterException, FenixServiceException {
        
        deleteSupportLesson(request, form, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        return mapping.findForward("successfull-delete");
    }
}
