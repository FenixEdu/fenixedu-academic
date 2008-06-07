package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ShowTeacherCreditsDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class DepartmentAdmOfficeShowTeacherCreditsDispatchAction extends ShowTeacherCreditsDispatchAction {

    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException,
	    ParseException {

	DynaActionForm teacherCreditsForm = (DynaActionForm) form;
	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID((Integer) teacherCreditsForm
		.get("executionPeriodId"));
	Teacher teacher = rootDomainObject.readTeacherByOID((Integer) teacherCreditsForm.get("teacherId"));

	if (teacher == null || getTeacherOfManageableDepartments(teacher.getTeacherNumber(), executionSemester, request) == null) {
	    request.setAttribute("teacherNotFound", "teacherNotFound");
	    return mapping.findForward("teacher-not-found");
	}

	showLinks(request, executionSemester, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
	getAllTeacherCredits(request, executionSemester, teacher);
	return mapping.findForward("show-teacher-credits");
    }

    private Teacher getTeacherOfManageableDepartments(Integer teacherNumber, ExecutionSemester executionSemester,
	    HttpServletRequest request) {

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
}
