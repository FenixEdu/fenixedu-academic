package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class AcademicServiceRequestsManagementDispatchAction extends FenixDispatchAction {

    private AcademicServiceRequest getAndSetAcademicServiceRequest(final HttpServletRequest request) {
	final AcademicServiceRequest academicServiceRequest = rootDomainObject
		.readAcademicServiceRequestByOID(getRequestParameterAsInteger(request,
			"academicServiceRequestId"));
	request.setAttribute("academicServiceRequest", academicServiceRequest);
	return academicServiceRequest;
    }

    private Registration getAndSetRegistration(final HttpServletRequest request) {
	final Registration registration = rootDomainObject
		.readRegistrationByOID(getRequestParameterAsInteger(request, "registrationID"));
	request.setAttribute("registration", registration);
	return registration;
    }

    private String getAndSetUrl(ActionForm actionForm, HttpServletRequest request) {
	final StringBuilder result = new StringBuilder();

	if (!StringUtils.isEmpty(request.getParameter("backAction"))) {
	    result.append("/").append(request.getParameter("backAction")).append(".do?");
	    
	    if (!StringUtils.isEmpty(request.getParameter("backMethod"))) {
		result.append("method=").append(request.getParameter("backMethod"));
	    }
	}

	request.setAttribute("url", result.toString());
	return result.toString();
    }

    public ActionForward viewRegistrationAcademicServiceRequestsHistoric(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("registration", getAndSetRegistration(request));
	return mapping.findForward("viewRegistrationAcademicServiceRequestsHistoric");
    }

    public ActionForward viewAcademicServiceRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	getAndSetAcademicServiceRequest(request);
	getAndSetUrl(form, request);
	return mapping.findForward("viewAcademicServiceRequest");
    }

    public ActionForward processNewAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

	final List<Integer> documentIdsToProcess = new ArrayList<Integer>();
	documentIdsToProcess.add(academicServiceRequest.getIdInternal());

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
		    "ProcessNewAcademicServiceRequests", new Object[] {
			    SessionUtils.getUserView(request).getPerson().getEmployee(),
			    documentIdsToProcess });
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    request.setAttribute("failingCondition", ex.getKey());
	    return mapping.findForward("prepareRejectAcademicServiceRequest");
	}

	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareRejectAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	getAndSetAcademicServiceRequest(request);
	return mapping.findForward("prepareRejectAcademicServiceRequest");
    }

    public ActionForward rejectAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	final String justification = ((DynaActionForm) actionForm).getString("justification");

	try {
		ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
			"RejectAcademicServiceRequest", new Object[] { academicServiceRequest, justification });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    return mapping.findForward("prepareRejectAcademicServiceRequest");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    return mapping.findForward("prepareRejectAcademicServiceRequest");
	}
	
	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareCancelAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	getAndSetAcademicServiceRequest(request);
	return mapping.findForward("prepareCancelAcademicServiceRequest");
    }

    public ActionForward cancelAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	final String justification = ((DynaActionForm) actionForm).getString("justification");

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
		    "CancelAcademicServiceRequest", new Object[] { academicServiceRequest, justification });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    return mapping.findForward("prepareCancelAcademicServiceRequest");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    return mapping.findForward("prepareCancelAcademicServiceRequest");
	} 
	
	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareConcludeAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

	if (academicServiceRequest.isDocumentRequest()) {
	    return mapping.findForward("prepareConcludeDocumentRequest");
	} else {
	    request.setAttribute("registration", academicServiceRequest.getRegistration());
	    return mapping.findForward("viewRegistrationDetails");    
	}
    }

    public ActionForward concludeAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
		    "ConcludeAcademicServiceRequest", new Object[] { academicServiceRequest });
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	}
	
	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward deliveredAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
		    "DeliveredAcademicServiceRequest", new Object[] { academicServiceRequest });
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	}
	
	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

}
