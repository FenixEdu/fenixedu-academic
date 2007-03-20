package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewEnroledExecutionCoursesAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final Student student = getLoggedPerson(request).getStudent();
	final List<Registration> registrations = student.getActiveRegistrations();
	
	if (registrations.size() == 1) {
	    request.setAttribute("executionCourses", executeService("ReadEnroledExecutionCourses", new Object[] {registrations.get(0)}));
	    return mapping.findForward("showEnroledExecutionCourses");
	    
	} else {
	    request.setAttribute("registrations", registrations);
	    return mapping.findForward("showActiveRegistrations");
	}
    }
    
    public ActionForward select(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final Registration registration = getRegistrationByID(getLoggedPerson(request).getStudent(), getIntegerFromRequest(request, "registrationId"));
	request.setAttribute("executionCourses", executeService("ReadEnroledExecutionCourses", new Object[] {registration}));
	return mapping.findForward("showEnroledExecutionCourses");
    }
    
    private Registration getRegistrationByID(final Student student, final Integer registrationId) {
	for (final Registration registration : student.getActiveRegistrations()) {
	    if (registration.getIdInternal().equals(registrationId)) {
		return registration;
	    }
	}
	return null;
    }
}