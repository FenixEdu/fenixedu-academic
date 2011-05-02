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
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDegreeTeachingServicesDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class DepartmentAdmOfficeManageDegreeTeachingServicesDispatchAction extends ManageDegreeTeachingServicesDispatchAction {

    public ActionForward showTeachingServiceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm dynaForm = (DynaActionForm) form;
	Integer professorshipID = (Integer) dynaForm.get("professorshipID");
	Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);

	if (professorship == null
		|| professorship.getTeacher() == null
		|| !isTeacherOfManageableDepartments(professorship.getTeacher(), professorship.getExecutionCourse()
			.getExecutionPeriod(), request)) {
	    return mapping.findForward("teacher-not-found");
	}

	teachingServiceDetailsProcess(professorship, request, dynaForm);
	return mapping.findForward("show-teaching-service-percentages");
    }

    private boolean isTeacherOfManageableDepartments(Teacher teacher, ExecutionSemester executionSemester,
	    HttpServletRequest request) {

	IUserView userView = UserView.getUser();
	List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();

	List<Unit> workingPlacesByPeriod = teacher.getWorkingPlacesByPeriod(executionSemester.getBeginDateYearMonthDay(),
		executionSemester.getEndDateYearMonthDay());
	for (Unit unit : workingPlacesByPeriod) {
	    Department teacherDepartment = unit.isDepartmentUnit() ? unit.getDepartment() : unit.getDepartmentUnit()
		    .getDepartment();
	    if (manageableDepartments.contains(teacherDepartment)) {
		return true;
	    }
	}
	return false;
    }

    public ActionForward updateTeachingServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
	return updateTeachingServices(mapping, form, request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
    }
}
