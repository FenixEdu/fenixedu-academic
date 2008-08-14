package net.sourceforge.fenixedu.presentationTier.Action.commons.transition;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public abstract class AbstractBolonhaTransitionManagementDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final List<Registration> registrations = getRegistrations(request);
	if (registrations.size() == 1) {
	    final Registration registration = registrations.iterator().next();
	    setParametersToShowStudentCurricularPlan(form, request, registration);

	    return mapping.findForward("showStudentCurricularPlan");
	}

	request.setAttribute("registrations", registrations);

	return mapping.findForward("chooseRegistration");

    }

    private void setParametersToShowStudentCurricularPlan(final ActionForm form, final HttpServletRequest request,
	    final Registration registration) {

	setRegistration(request, registration);

	final DynaActionForm dynaActionForm = ((DynaActionForm) form);
	dynaActionForm.set("registrationId", registration.getIdInternal());
    }

    private void setRegistration(HttpServletRequest request, final Registration registration) {
	request.setAttribute("registration", registration);
    }

    public ActionForward showStudentCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setParametersToShowStudentCurricularPlan(form, request, getRegistration(request, form));

	return mapping.findForward("showStudentCurricularPlan");
    }

    private Registration getRegistration(final HttpServletRequest request, final ActionForm form) {
	return getRegistration(request, (Integer) ((DynaActionForm) form).get("registrationId"));
    }

    private Registration getRegistration(final HttpServletRequest request, final Integer registrationId) {
	for (final Registration registration : getRegistrations(request)) {
	    if (registration.getIdInternal().equals(registrationId)) {
		return registration;
	    }
	}

	return null;
    }

    abstract protected List<Registration> getRegistrations(final HttpServletRequest request);

}
