package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.StudentsSearchBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.EditCandidacyInformationDA.ChooseRegistrationOrPhd;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/students", module = "academicAdminOffice")
@Forwards( { @Forward(name = "viewStudentDetails", path = "/academicAdminOffice/student/viewStudentDetails.jsp"),
	@Forward(name = "search", path = "/academicAdminOffice/searchStudents.jsp") })
public class SearchForStudentsDA extends FenixDispatchAction {

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	StudentsSearchBean studentsSearchBean = getRenderedObject();

	if (studentsSearchBean == null) { // 1st time
	    studentsSearchBean = new StudentsSearchBean();
	} else {
	    final Employee employee = AccessControl.getPerson().getEmployee();
	    final AdministrativeOffice administrativeOffice = employee.getAdministrativeOffice();

	    final Set<Student> students = studentsSearchBean.searchForOffice(administrativeOffice);

	    if (students.size() == 1) {
		Student student = students.iterator().next();
		request.setAttribute("student", student);
		request.setAttribute("choosePhdOrRegistration", new ChooseRegistrationOrPhd(student));
		return mapping.findForward("viewStudentDetails");
	    }
	    request.setAttribute("students", students);
	}

	request.setAttribute("studentsSearchBean", studentsSearchBean);
	return mapping.findForward("search");
    }

}
