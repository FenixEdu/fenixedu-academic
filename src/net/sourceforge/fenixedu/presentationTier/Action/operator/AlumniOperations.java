package net.sourceforge.fenixedu.presentationTier.Action.operator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AlumniOperations extends FenixDispatchAction {

    public ActionForward prepareIdentityRequestsList(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("identityRequestsList", AlumniIdentityCheckRequest.readPendingRequests());
	return mapping.findForward("alumni.view.identity.requests.list");
    }

    public ActionForward prepareIdentityValidation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("requestBody", RootDomainObject.getInstance().readAlumniIdentityCheckRequestByOID(getIntegerFromRequest(request, "requestId")));
	request.setAttribute("personBody", RootDomainObject.getInstance().readPartyByOID(getIntegerFromRequest(request, "personId")));
	request.setAttribute("operation", "validate");
	return mapping.findForward("alumni.validate.request");
    }

    public ActionForward showIdentityValidation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("requestBody", RootDomainObject.getInstance().readAlumniIdentityCheckRequestByOID(getIntegerFromRequest(request, "requestId")));
	request.setAttribute("personBody", RootDomainObject.getInstance().readPartyByOID(getIntegerFromRequest(request, "personId")));
	return mapping.findForward("alumni.validate.request");
    }

    public ActionForward confirmIdentity(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AlumniIdentityCheckRequest identityRequest = (AlumniIdentityCheckRequest) getObjectFromViewState("requestBody");
	executeService("ValidateAlumniIdentity", identityRequest, Boolean.TRUE, getLoggedPerson(request));
	
	request.setAttribute("identityRequestResult", "identity.validation.ok");
	return mapping.findForward("alumni.validate.request.result");
    }

    public ActionForward refuseIdentity(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AlumniIdentityCheckRequest identityRequest = (AlumniIdentityCheckRequest) getObjectFromViewState("requestBody");
	executeService("ValidateAlumniIdentity", identityRequest, Boolean.FALSE, getLoggedPerson(request));

	request.setAttribute("identityRequestResult", "identity.validation.nok");
	return mapping.findForward("alumni.validate.request.result");
    }

    public ActionForward viewClosedRequests(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("identityRequestsList", AlumniIdentityCheckRequest.readClosedRequests());
	return mapping.findForward("alumni.closed.identity.requests");
    }

}
