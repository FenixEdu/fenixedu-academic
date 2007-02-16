package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.CurricularCoursesEnrollmentDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class StudentCurricularCoursesEnrollmentDispatchAction extends
	CurricularCoursesEnrollmentDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Registration registration = (Registration) request.getAttribute("registration");
	((DynaActionForm) form).set("registrationId", registration.getIdInternal());
	
	return mapping.findForward("showWarning");
    }

    @Override
    protected Registration getRegistration(IUserView userView, DynaActionForm form)
	    throws NotAuthorizedException {

	final Registration registration = (Registration) (rootDomainObject
		.readRegistrationByOID((Integer) form.get("registrationId")));
	if (!userView.getPerson().getStudent().getRegistrationsToEnrolByStudent().contains(registration)) {
	    throw new NotAuthorizedException("Registration does not belong to student");
	}

	return registration;
    }

}