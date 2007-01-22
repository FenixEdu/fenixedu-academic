package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests.documentRequests;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest.DocumentRequestCreator;
import net.sourceforge.fenixedu.domain.student.Registration;
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
	    return mapping.findForward("processNewAcademicServiceRequest"); 
	} else {
	    addActionMessage(request, "document.request.created.with.success");
	    return buildActionForward(mapping.findForward("viewRegistrationDetails"), registration);
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

}
