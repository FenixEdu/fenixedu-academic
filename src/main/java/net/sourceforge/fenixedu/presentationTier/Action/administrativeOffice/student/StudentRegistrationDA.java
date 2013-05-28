package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Forwards({ @Forward(name = "viewRegistrationDetails",
        path = "/academicAdminOffice/student/registration/viewRegistrationDetails.jsp") })
public abstract class StudentRegistrationDA extends FenixDispatchAction {

    protected Registration getAndSetRegistration(final HttpServletRequest request) {
        final Integer registrationID =
                getIntegerFromRequest(request, "registrationID") != null ? getIntegerFromRequest(request, "registrationID") : getIntegerFromRequest(
                        request, "registrationId");
        final Registration registration = AbstractDomainObject.fromExternalId(Integer.valueOf(registrationID));
        request.setAttribute("registration", registration);
        return registration;
    }

    public ActionForward visualizeRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        getAndSetRegistration(request);
        return mapping.findForward("viewRegistrationDetails");
    }
}
