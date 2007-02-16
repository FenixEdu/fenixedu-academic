package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentEnrollmentManagementDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Student student = getLoggedStudent(request);
	if (!student.hasPersonDataAuthorizationChoiseForCurrentExecutionYear()) {
	    request.setAttribute("student", student);

	    return mapping.findForward("choosePersonalDataAuthorizationChoice");
	}

	final List<Registration> registrations = getRegistrationsToEnrolByStudent(request);
	if (registrations.size() == 1) {
	    final Registration registration = registrations.iterator().next();
	    return getActionForwardForRegistration(mapping, request, registration);

	}

	return prepareChooseRegistration(mapping, form, request, response);

    }

    private ActionForward getActionForwardForRegistration(ActionMapping mapping,
	    HttpServletRequest request, final Registration registration) {

	request.setAttribute("registration", registration);

	if (registration.getLastStudentCurricularPlan().isBolonha()) {
	    return mapping.findForward("proceedToBolonhaEnrollment");
	}

	return mapping.findForward("proceedToPreBolonhaEnrollment");
    }

    public ActionForward prepareChooseRegistration(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("registrationsToEnrol", getRegistrationsToEnrolByStudent(request));

	return mapping.findForward("chooseRegistration");
    }

    public ActionForward chooseRegistration(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final Registration registration = getRegistration(request);
	if (!getLoggedStudent(request).getRegistrationsToEnrolByStudent().contains(registration)) {
	    return mapping.findForward("notAuthorized");
	}

	return getActionForwardForRegistration(mapping, request, registration);

    }

    private Registration getRegistration(final HttpServletRequest request) {
	return rootDomainObject.readRegistrationByOID(getRequestParameterAsInteger(request,
		"registrationId"));
    }

    public ActionForward choosePersonalDataAuthorizationChoice(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return prepare(mapping, form, request, response);

    }

    private List<Registration> getRegistrationsToEnrolByStudent(final HttpServletRequest request) {
	return getLoggedStudent(request).getRegistrationsToEnrolByStudent();
    }

    private Student getLoggedStudent(final HttpServletRequest request) {
	return getLoggedPerson(request).getStudent();
    }

}
