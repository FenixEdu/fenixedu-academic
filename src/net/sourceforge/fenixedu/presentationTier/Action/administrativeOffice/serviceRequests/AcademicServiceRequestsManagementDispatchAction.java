package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.factoryExecutors.RegistrationAcademicServiceRequestCreator;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.YearMonthDay;

public class AcademicServiceRequestsManagementDispatchAction extends FenixDispatchAction {

    private RegistrationAcademicServiceRequest getAndSetAcademicServiceRequest(final HttpServletRequest request) {
	Integer academicServiceRequestId = getRequestParameterAsInteger(request, "academicServiceRequestId");
	if (academicServiceRequestId == null) {
	    academicServiceRequestId = (Integer) request.getAttribute("academicServiceRequestId");
	}
	final AcademicServiceRequest academicServiceRequest = rootDomainObject
		.readAcademicServiceRequestByOID(academicServiceRequestId);
	request.setAttribute("academicServiceRequest", academicServiceRequest);
	return (RegistrationAcademicServiceRequest) academicServiceRequest;
    }

    private Registration getAndSetRegistration(final HttpServletRequest request) {
	final Registration registration = rootDomainObject.readRegistrationByOID(getRequestParameterAsInteger(request,
		"registrationID"));
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

    public ActionForward viewRegistrationAcademicServiceRequestsHistoric(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("registration", getAndSetRegistration(request));
	return mapping.findForward("viewRegistrationAcademicServiceRequestsHistoric");
    }

    public ActionForward viewAcademicServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	getAndSetAcademicServiceRequest(request);
	getAndSetUrl(form, request);
	return mapping.findForward("viewAcademicServiceRequest");
    }

    public ActionForward processNewAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

	try {
	    executeService("ProcessNewAcademicServiceRequests", academicServiceRequest);
	    addActionMessage(request, "academic.service.request.processed.with.success");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    request.setAttribute("failingCondition", ex.getKey());
	    return mapping.findForward("prepareRejectAcademicServiceRequest");
	}

	if (academicServiceRequest.isDocumentRequest()
		&& ((DocumentRequest) academicServiceRequest).getDocumentRequestType().isAllowedToQuickDeliver()) {
	    return prepareConcludeAcademicServiceRequest(mapping, actionForm, request, response);
	} else if (request.getParameter("academicSituationType") != null) {
	    return search(mapping, actionForm, request, response);
	} else {
	    request.setAttribute("registration", academicServiceRequest.getRegistration());
	    return mapping.findForward("viewRegistrationDetails");
	}
    }

    public ActionForward prepareSendAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	getAndSetAcademicServiceRequest(request);
	request.setAttribute("serviceRequestBean", new AcademicServiceRequestBean());
	return mapping.findForward("prepareSendAcademicServiceRequest");
    }

    public ActionForward sendAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest serviceRequest = getAndSetAcademicServiceRequest(request);
	final AcademicServiceRequestBean requestBean = (AcademicServiceRequestBean) getObjectFromViewState("serviceRequestBean");

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
		    "SendAcademicServiceRequestToExternalEntity", new Object[] { serviceRequest, requestBean.getSituationDate(),
			    requestBean.getJustification() });

	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    request.setAttribute("serviceRequestBean", requestBean);
	    return mapping.findForward("prepareSendAcademicServiceRequest");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    request.setAttribute("serviceRequestBean", requestBean);
	    return mapping.findForward("prepareSendAcademicServiceRequest");
	}

	request.setAttribute("registration", serviceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareReceiveAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	getAndSetAcademicServiceRequest(request);
	request.setAttribute("serviceRequestBean", new AcademicServiceRequestBean());
	return mapping.findForward("prepareReceiveAcademicServiceRequest");
    }

    public ActionForward receiveAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest serviceRequest = getAndSetAcademicServiceRequest(request);
	final AcademicServiceRequestBean requestBean = (AcademicServiceRequestBean) getObjectFromViewState("serviceRequestBean");

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
		    "ReceivedAcademicServiceRequestFromExternalEntity", new Object[] { serviceRequest,
			    requestBean.getSituationDate(), requestBean.getJustification() });

	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    request.setAttribute("serviceRequestBean", requestBean);
	    return mapping.findForward("prepareReceiveAcademicServiceRequest");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    request.setAttribute("serviceRequestBean", requestBean);
	    return mapping.findForward("prepareReceiveAcademicServiceRequest");
	}

	request.setAttribute("registration", serviceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareRejectAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	getAndSetAcademicServiceRequest(request);
	return mapping.findForward("prepareRejectAcademicServiceRequest");
    }

    public ActionForward rejectAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	final String justification = ((DynaActionForm) actionForm).getString("justification");

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request), "RejectAcademicServiceRequest",
		    new Object[] { academicServiceRequest, justification });
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
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	getAndSetAcademicServiceRequest(request);
	return mapping.findForward("prepareCancelAcademicServiceRequest");
    }

    public ActionForward cancelAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	final String justification = ((DynaActionForm) actionForm).getString("justification");

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request), "CancelAcademicServiceRequest",
		    new Object[] { academicServiceRequest, justification });
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
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	((DynaActionForm) actionForm).set("sendEmailToStudent", Boolean.TRUE);

	if (academicServiceRequest.isDocumentRequest()) {
	    return mapping.findForward("prepareConcludeDocumentRequest");
	} else {
	    return mapping.findForward("prepareConcludeServiceRequest");
	}
    }

    public ActionForward concludeAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	final DynaActionForm form = (DynaActionForm) actionForm;

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request), "ConcludeAcademicServiceRequest",
		    new Object[] { academicServiceRequest, getSendEmailToStudent(form) });
	    addActionMessage(request, "academic.service.request.concluded.with.success");

	    if (academicServiceRequest.isDocumentRequest()
		    && ((DocumentRequest) academicServiceRequest).getDocumentRequestType().isAllowedToQuickDeliver()) {
		return deliveredAcademicServiceRequest(mapping, actionForm, request, response);
	    }
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	}

	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    private Boolean getSendEmailToStudent(final DynaActionForm form) {
	return (Boolean) form.get("sendEmailToStudent");
    }

    public ActionForward deliveredAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request), "DeliveredAcademicServiceRequest",
		    new Object[] { academicServiceRequest });
	    addActionMessage(request, "academic.service.request.delivered.with.success");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	}

	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	AcademicServiceRequestBean bean = (AcademicServiceRequestBean) getObjectFromViewState("bean");
	if (bean == null) {
	    Integer year = getIntegerFromRequest(request, "year");
	    if (year == null) {
		year = new YearMonthDay().getYear();
	    }

	    bean = new AcademicServiceRequestBean(AcademicServiceRequestSituationType.valueOf(request
		    .getParameter("academicSituationType")), getEmployee(), year);
	}
	request.setAttribute("bean", bean);

	final Collection<AcademicServiceRequest> academicServiceRequestsNotOwnedByEmployee = bean.searchAcademicServiceRequests();
	final Collection<AcademicServiceRequest> academicServiceRequestsOwnedByEmployee = new HashSet<AcademicServiceRequest>();

	if (bean.getAcademicServiceRequestSituationType() != AcademicServiceRequestSituationType.NEW) {
	    for (Iterator<AcademicServiceRequest> iter = academicServiceRequestsNotOwnedByEmployee.iterator(); iter.hasNext();) {
		final AcademicServiceRequest academicServiceRequest = (AcademicServiceRequest) iter.next();
		if (academicServiceRequest.getActiveSituation().getEmployee() == getEmployee()) {
		    iter.remove();
		    academicServiceRequestsOwnedByEmployee.add(academicServiceRequest);
		}
	    }
	}

	request.setAttribute("academicServiceRequests", academicServiceRequestsNotOwnedByEmployee);
	request.setAttribute("employeeRequests", academicServiceRequestsOwnedByEmployee);

	return mapping.findForward("searchResults");
    }

    private Employee getEmployee() {
	return AccessControl.getPerson().getEmployee();
    }

    private AdministrativeOffice getAdministrativeOffice() {
	return getEmployee().getAdministrativeOffice();
    }

    public ActionForward chooseServiceRequestType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("academicServiceRequestCreateBean", new RegistrationAcademicServiceRequestCreator(
		getAndSetRegistration(request)));
	return mapping.findForward("prepareCreateServiceRequest");
    }

    public ActionForward chooseServiceRequestTypePostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("academicServiceRequestCreateBean", getRenderedObject("academicServiceRequestCreateBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("prepareCreateServiceRequest");
    }

    public ActionForward chooseServiceRequestTypeInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("academicServiceRequestCreateBean", getRenderedObject("academicServiceRequestCreateBean"));
	return mapping.findForward("prepareCreateServiceRequest");
    }

    public ActionForward backToViewRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	getAndSetRegistration(request);
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward confirmCreateServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("academicServiceRequestCreateBean", getRenderedObject("academicServiceRequestCreateBean"));
	return mapping.findForward("confirmCreateServiceRequest");
    }

    public ActionForward createServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeFactoryMethod(request);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    return confirmCreateServiceRequest(mapping, actionForm, request, response);
	}

	final RegistrationAcademicServiceRequestCreator bean = (RegistrationAcademicServiceRequestCreator) getRenderedObject("academicServiceRequestCreateBean");
	request.setAttribute("registration", bean.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }
}
