package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentsSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SearchForStudents extends FenixDispatchAction {

	public ActionForward prepareSearch(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		 StudentsSearchBean studentsSearchBean = (StudentsSearchBean) getRenderedObject();
	        
	        if (studentsSearchBean == null) { //1st time
	            studentsSearchBean = new StudentsSearchBean();
	        } else {
	          
	            final AdministrativeOffice administrativeOffice = AdministrativeOffice
				.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
	            
	            final Set<Student> students = studentsSearchBean.searchForOffice(administrativeOffice);
	        
	            if(students.size() == 1) {
	            
	                request.setAttribute("student", students.iterator().next());
	                return mapping.findForward("viewStudentDetails");
	            }
	            request.setAttribute("students", students);
	        }

	        request.setAttribute("studentsSearchBean", studentsSearchBean);
	        return mapping.findForward("search");
	    
	}

	public ActionForward visualizeStudent(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		getStudent(request);
		return mapping.findForward("viewStudentDetails");
	}

	

	private Student getStudent(final HttpServletRequest request) {
		final String studentID = request.getParameter("studentID");
		final Student student = rootDomainObject.readStudentByOID(Integer
			.valueOf(studentID));

		request.setAttribute("student", student);
		return student;
	}


}
