package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentEnrollmentManagementDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final Student student = getLoggedStudent(request);
	if (!student.hasPersonDataAuthorizationChoiseForCurrentExecutionYear()) {
	    request.setAttribute("student", student);

	    return mapping.findForward("choosePersonalDataAuthorizationChoice");
	}

	final Set<Registration> sourceRegistrationsForTransition = getTransitionSourceRegistrationsForStudent(request);
	final List<Registration> registrationsToEnrol = getRegistrationsToEnrolByStudent(request);
	if (sourceRegistrationsForTransition.isEmpty() && registrationsToEnrol.size() == 1) {
	    final Registration registration = registrationsToEnrol.iterator().next();
	    return getActionForwardForRegistration(mapping, request, registration);
	}

	request.setAttribute("sourceRegistrationsForTransition", sourceRegistrationsForTransition);
	request.setAttribute("registrationsToEnrol", registrationsToEnrol);

	return mapping.findForward("chooseRegistration");
    }

    private Set<Registration> getTransitionSourceRegistrationsForStudent(HttpServletRequest request) {
	final Set<Registration> result = new HashSet<Registration>();

	for (final Registration registration : getLoggedStudent(request).getTransitionRegistrations()) {
	    final Registration sourceRegistrationForTransition = registration.getSourceRegistrationForTransition();
	    if (sourceRegistrationForTransition != null) {
		result.add(sourceRegistrationForTransition);
	    }
	}

	return result;
    }

    public ActionForward prepareBolonhaTransitionConfirmation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Registration sourceRegistrationForTransition = getSourceTransitionRegistration(request);
	if (!sourceRegistrationForTransitionBelongsToStudent(request, sourceRegistrationForTransition)) {
	    return mapping.findForward("notAuthorized");
	}

	request.setAttribute("sourceRegistrationForTransition", sourceRegistrationForTransition);

	return mapping.findForward("confirmBolonhaTransition");
    }

    private boolean sourceRegistrationForTransitionBelongsToStudent(HttpServletRequest request,
	    final Registration sourceRegistrationForTransition) {
	return getTransitionSourceRegistrationsForStudent(request).contains(sourceRegistrationForTransition);
    }

    private Registration getSourceTransitionRegistration(final HttpServletRequest request) {
	return getRegistrationFrom(request, "sourceRegistrationForTransitionId");
    }

    public ActionForward confirmBolonhaTransition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final Registration sourceRegistrationForTransition = getSourceTransitionRegistration(request);
	if (!sourceRegistrationForTransitionBelongsToStudent(request, sourceRegistrationForTransition)) {
	    return mapping.findForward("notAuthorized");
	}

	try {
	    executeService("TransitToBolonha", getLoggedPerson(request), sourceRegistrationForTransition);
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());

	    return prepareBolonhaTransitionConfirmation(mapping, form, request, response);
	}

	return prepare(mapping, form, request, response);
    }

    private ActionForward getActionForwardForRegistration(ActionMapping mapping, HttpServletRequest request,
	    final Registration registration) {

	request.setAttribute("registration", registration);

	return mapping.findForward("proceedToEnrolment");
    }

    public ActionForward chooseRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Registration registration = getRegistration(request);
	if (!registrationBelongsToRegistrationsToEnrol(request, registration)) {
	    return mapping.findForward("notAuthorized");
	}

	return getActionForwardForRegistration(mapping, request, registration);
    }

    private boolean registrationBelongsToRegistrationsToEnrol(HttpServletRequest request, final Registration registration) {
	return getRegistrationsToEnrolByStudent(request).contains(registration);
    }

    private Registration getRegistration(final HttpServletRequest request) {
	return getRegistrationFrom(request, "registrationId");
    }

    private Registration getRegistrationFrom(final HttpServletRequest request, final String parameterName) {
	return rootDomainObject.readRegistrationByOID(getRequestParameterAsInteger(request, parameterName));
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
