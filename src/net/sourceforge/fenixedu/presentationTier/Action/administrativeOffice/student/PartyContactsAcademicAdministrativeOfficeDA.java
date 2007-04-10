package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.person.PartyContactsManagementDispatchAction;

public class PartyContactsAcademicAdministrativeOfficeDA extends PartyContactsManagementDispatchAction {
    
    private Student getStudent(final HttpServletRequest request) {
	final Student student = rootDomainObject.readStudentByOID(Integer.valueOf(request.getParameter("studentID")));
	request.setAttribute("student", student);
	return student;
    }

    @Override
    protected Person getPerson(final HttpServletRequest request) {
        return getStudent(request).getPerson();
    }
    
    @Override
    public ActionForward backToShowInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("editPersonalData");
    }

}
