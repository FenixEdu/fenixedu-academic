/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatute.CreateStudentStatuteFactory;
import net.sourceforge.fenixedu.domain.student.StudentStatute.DeleteStudentStatuteFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentStatutesDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final Student student = rootDomainObject.readStudentByOID(getIntegerFromRequest(request,
		"studentId"));
	request.setAttribute("student", student);
	request.setAttribute("manageStatuteBean", new CreateStudentStatuteFactory(student));

	return mapping.findForward("manageStatutes");
    }

    public ActionForward addNewStatute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	// add new statute
	executeFactoryMethod();

	final Student student = ((CreateStudentStatuteFactory) getRenderedObject()).getStudent();
	request.setAttribute("student", student);
	request.setAttribute("manageStatuteBean", new CreateStudentStatuteFactory(student));

	return mapping.findForward("manageStatutes");

    }

    public ActionForward deleteStatute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final StudentStatute studentStatute = rootDomainObject
		.readStudentStatuteByOID(getIntegerFromRequest(request, "statuteId"));
	final Student student = studentStatute.getStudent();

	// delete statute
	executeFactoryMethod(new DeleteStudentStatuteFactory(studentStatute));

	request.setAttribute("student", student);
	request.setAttribute("manageStatuteBean", new CreateStudentStatuteFactory(student));

	return mapping.findForward("manageStatutes");

    }

}
