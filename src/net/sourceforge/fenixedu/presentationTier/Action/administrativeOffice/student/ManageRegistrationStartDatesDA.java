package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Input;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */

@Mapping(path = "/manageRegistrationStartDates", module = "academicAdministration")
@Forwards({ @Forward(
		name = "showEditStartDates",
		path = "/academicAdminOffice/student/registration/manageRegistrationStartDates.jsp") })
public class ManageRegistrationStartDatesDA extends FenixDispatchAction {

	@Input
	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		final Integer registrationId = getIntegerFromRequest(request, "registrationId");
		request.setAttribute("registration", rootDomainObject.readRegistrationByOID(registrationId));
		return mapping.findForward("showEditStartDates");
	}

}
