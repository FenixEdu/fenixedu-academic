package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests.documentRequests;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestEditBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestSearchBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest.DocumentRequestCreator;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.AdministrativeOfficeDocument;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class DocumentRequestsManagementDispatchAction extends FenixDispatchAction {

    private DocumentRequest getDocumentRequest(HttpServletRequest request) {
	return (DocumentRequest) rootDomainObject
		.readAcademicServiceRequestByOID(getRequestParameterAsInteger(request,
			"documentRequestId"));
    }

    private AcademicServiceRequest getAndSetAcademicServiceRequest(final HttpServletRequest request) {
	Integer academicServiceRequestId = getRequestParameterAsInteger(request,
		"academicServiceRequestId");
	if (academicServiceRequestId == null) {
	    academicServiceRequestId = (Integer) request.getAttribute("academicServiceRequestId");
	}
	final AcademicServiceRequest academicServiceRequest = rootDomainObject
		.readAcademicServiceRequestByOID(academicServiceRequestId);
	request.setAttribute("academicServiceRequest", academicServiceRequest);
	return academicServiceRequest;
    }

    public ActionForward printDocument(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {

	final DocumentRequest documentRequest = getDocumentRequest(request);
	final AdministrativeOfficeDocument administrativeOfficeDocument = AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator
		.create(documentRequest);

	byte[] data = ReportsUtils.exportToPdf(administrativeOfficeDocument.getReportTemplateKey(),
		administrativeOfficeDocument.getParameters(), administrativeOfficeDocument
			.getResourceBundle(), administrativeOfficeDocument.getDataSource());

	response.setContentLength(data.length);
	response.setContentType("application/pdf");
	response.addHeader("Content-Disposition", "attachment; filename="
		+ administrativeOfficeDocument.getReportFileName() + ".pdf");

	final ServletOutputStream writer = response.getOutputStream();
	writer.write(data);
	writer.flush();
	writer.close();

	response.flushBuffer();

	return mapping.findForward("");
    }

    public ActionForward prepareConcludeDocumentRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	if (request.getAttribute("academicServiceRequest") == null) {
	    request.setAttribute("academicServiceRequest", getAndSetAcademicServiceRequest(request));	    
	}
	return mapping.findForward("printDocument");
    }

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("documentRequestSearchBean", new DocumentRequestSearchBean());

	List<DocumentRequest> documentRequests = getAdministrativeOffice().searchDocumentsBy(
		null,
		AcademicServiceRequestSituationType.valueOf(request
			.getParameter("academicSituationType")), null, null, getEmployee().getCurrentCampus(), null);
	request.setAttribute("documentRequestsResult", documentRequests);
	request.setAttribute("academicSituationType", AcademicServiceRequestSituationType
		.valueOf(request.getParameter("academicSituationType")));
	return mapping.findForward("prepareSearch");
    }

    public ActionForward search(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final DocumentRequestSearchBean documentRequestSearchBean = (DocumentRequestSearchBean) getRenderedObject("documentRequestSearch");

	final AcademicServiceRequestSituationType academicSituationType = documentRequestSearchBean == null ? AcademicServiceRequestSituationType
		.valueOf(request.getParameter("academicSituationType"))
		: documentRequestSearchBean.getAcademicServiceRequestSituationType();

	List<DocumentRequest> documentRequests = getAdministrativeOffice().searchDocumentsBy(null,
		academicSituationType, null, null, getEmployee().getCurrentCampus(), null);

	request.setAttribute("academicSituationType", academicSituationType);
	request.setAttribute("documentRequestsResult", documentRequests);

	return mapping.findForward("prepareSearch");
    }

    private StringBuilder buildUrl(ActionForm actionForm, HttpServletRequest request) {
	final DynaActionForm form = (DynaActionForm) actionForm;

	final StringBuilder result = new StringBuilder();

	if (!StringUtils.isEmpty(request.getParameter("back"))) {
	    result.append("method=").append(request.getParameter("back"));
	}

	if (!StringUtils.isEmpty(form.getString("documentRequestType"))) {
	    result.append("&documentRequestType=").append(form.get("documentRequestType"));
	}

	if (!StringUtils.isEmpty(form.getString("requestSituationType"))) {
	    result.append("&requestSituationType=").append(form.get("requestSituationType"));
	}

	if (!StringUtils.isEmpty(form.getString("isUrgent"))) {
	    result.append("&isUrgent=").append(form.get("isUrgent"));
	}

	if (!StringUtils.isEmpty(form.getString("studentNumber"))) {
	    result.append("&studentNumber=").append(form.get("studentNumber"));
	}

	return result;
    }

    private Employee getEmployee() {
	return AccessControl.getPerson().getEmployee();
    }   
    
    private AdministrativeOffice getAdministrativeOffice() {
	return getEmployee().getAdministrativeOffice();
    }

    public ActionForward prepareEditDocumentRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final DocumentRequest documentRequest = getDocumentRequest(request);
	final DocumentRequestEditBean documentRequestEditBean = new DocumentRequestEditBean(
		documentRequest, getUserView(request).getPerson().getEmployee());
	request.setAttribute("documentRequestEditBean", documentRequestEditBean);
	request.setAttribute("url", buildUrl(form, request).toString());

	return mapping.findForward("editDocumentRequest");
    }

    public ActionForward editDocumentRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final DocumentRequestEditBean documentRequestEditBean = (DocumentRequestEditBean) RenderUtils
		.getViewState("documentRequestEdit").getMetaObject().getObject();

	try {
	    ServiceManagerServiceFactory.executeService(getUserView(request), "EditDocumentRequest",
		    new Object[] { documentRequestEditBean });

	    return search(mapping, form, request, response);

	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
		    .getLabelFormatterArgs()));
	    request.setAttribute("documentRequestEditBean", documentRequestEditBean);
	    request.setAttribute("url", buildUrl(form, request).toString());

	    return mapping.findForward("editDocumentRequest");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("documentRequestEditBean", documentRequestEditBean);
	    request.setAttribute("url", buildUrl(form, request).toString());

	    return mapping.findForward("editDocumentRequest");
	}

    }

    private Registration getRegistration(final HttpServletRequest request) {
	String registrationID = request.getParameter("registrationId");
	if (registrationID == null) {
	    registrationID = (String) request.getAttribute("registrationId");
	}
	final Registration registration = rootDomainObject.readRegistrationByOID(Integer
		.valueOf(registrationID));
	request.setAttribute("registration", registration);
	return registration;
    }

    public ActionForward viewRegistrationDocumentRequestsHistoric(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("registration", getRegistration(request));
	return mapping.findForward("viewRegistrationDocumentRequestsHistoric");
    }

    public ActionForward viewDocumentRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("documentRequest", getDocumentRequest(request));
	request.setAttribute("url", buildUrl(form, request).toString());

	return mapping.findForward("viewDocumentRequest");
    }

    public ActionForward prepareCreateDocumentRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	DocumentRequestCreator creator = new DocumentRequestCreator(getRegistration(request));
	creator.setSchema(request.getParameter("schema"));
	request.setAttribute("documentRequestCreateBean", creator);

	return mapping.findForward("createDocumentRequests");
    }

    public ActionForward documentRequestTypeChoosedPostBack(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final DocumentRequestCreateBean requestCreateBean = (DocumentRequestCreateBean) RenderUtils
		.getViewState().getMetaObject().getObject();
	RenderUtils.invalidateViewState();

	setAdditionalInformationSchemaName(request, requestCreateBean);
	request.setAttribute("documentRequestCreateBean", requestCreateBean);
	return mapping.findForward("createDocumentRequests");
    }

    private void setAdditionalInformationSchemaName(HttpServletRequest request,
	    final DocumentRequestCreateBean requestCreateBean) {
	if (requestCreateBean.getChosenDocumentRequestType().getHasAdditionalInformation()) {
	    request
		    .setAttribute("additionalInformationSchemaName", "DocumentRequestCreateBean."
			    + requestCreateBean.getChosenDocumentRequestType().name()
			    + ".AdditionalInformation");
	}
    }

    public ActionForward viewDocumentRequestToCreate(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final DocumentRequestCreateBean requestCreateBean = (DocumentRequestCreateBean) RenderUtils
		.getViewState().getMetaObject().getObject();

	setAdditionalInformationSchemaName(request, requestCreateBean);
	request.setAttribute("documentRequestCreateBean", requestCreateBean);
	return mapping.findForward("viewDocumentRequestsToCreate");
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final DocumentRequestCreateBean documentRequestCreateBean = (DocumentRequestCreateBean) getRenderedObject();
	final Registration registration = documentRequestCreateBean.getRegistration();
	request.setAttribute("registration", registration);
	
	DocumentRequest documentRequest = null;
	try {
	    documentRequest = (DocumentRequest) executeFactoryMethod(request);
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    return mapping.findForward("viewRegistrationDetails");
	}

	if (documentRequestCreateBean.getChosenDocumentRequestType().isAllowedToQuickDeliver()) {
	    request.setAttribute("academicServiceRequestId", documentRequest.getIdInternal());
	    return processNewAcademicServiceRequest(mapping, actionForm, request, response); 
	} else {
	    addActionMessage(request, "document.request.created.with.success");
	    return buildActionForward(mapping.findForward("viewRegistrationDetails"), registration);
	    // return mapping.findForward("viewRegistrationDetails");
	}
    }

    private ActionForward buildActionForward(ActionForward forward, Registration registration) {
	ActionForward forwardBuilded = new ActionForward();
	forwardBuilded.setName(forward.getName());
	forwardBuilded.setRedirect(true);
	StringBuilder path = new StringBuilder(forward.getPath());
	// path.append("&registrationID=").append(registration.getIdInternal());
	forwardBuilded.setPath(path.toString());
	return forwardBuilded;
    }

    public ActionForward processNewAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

	try {
	    executeService("ProcessNewAcademicServiceRequests", academicServiceRequest);
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    request.setAttribute("failingCondition", ex.getKey());
	    return mapping.findForward("prepareRejectAcademicServiceRequest");
	}

	if (academicServiceRequest.isDocumentRequest() && ((DocumentRequest) academicServiceRequest).getDocumentRequestType().isAllowedToQuickDeliver()) {
	    return prepareConcludeDocumentRequest(mapping, actionForm, request, response);
	} else {
	    return search(mapping, actionForm, request, response);
	}
    }

}
