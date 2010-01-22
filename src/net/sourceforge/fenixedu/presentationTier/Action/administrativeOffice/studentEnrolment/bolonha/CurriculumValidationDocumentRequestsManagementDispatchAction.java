package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment.bolonha;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.documents.DocumentRequestGeneratedDocument;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests.documentRequests.DocumentRequestsManagementDispatchAction;
import net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.AdministrativeOfficeDocument;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/curriculumValidationDocumentRequestsManagement", module = "academicAdminOffice")
@Forwards( {
	@Forward(name = "printDocument", path = "/academicAdminOffice/student/curriculumValidation/documentRequests/printDocument.jsp"),
	@Forward(name = "viewDocuments", path = "/academicAdminOffice/student/curriculumValidation/documentRequests/viewDocuments.jsp"),
	@Forward(name = "notValidDocument", path = "/academicAdminOffice/student/curriculumValidation/documentRequests/notValidDocument.jsp") })
public class CurriculumValidationDocumentRequestsManagementDispatchAction extends DocumentRequestsManagementDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	StudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(request);
	request.setAttribute("studentCurriculumValidationAllowed", studentCurricularPlan
		.getEvaluationForCurriculumValidationAllowed());

	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward listDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	StudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(request);

	Registration registration = studentCurricularPlan.getRegistration();
	request.setAttribute("registration", registration);

	return mapping.findForward("viewDocuments");
    }

    public ActionForward preparePrintDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Integer dcocumentRequestId = getRequestParameterAsInteger(request, "documentRequestId");

	final DocumentRequest documentRequest = getDocumentRequest(request);

	if (!(DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE.equals(documentRequest.getDocumentRequestType()) || DocumentRequestType.DIPLOMA_REQUEST
		.equals(documentRequest.getDocumentRequestType()))) {
	    return mapping.findForward("notValidDocument");
	}

	String conlusionDate = "";
	if (DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE.equals(documentRequest.getDocumentRequestType())) {
	    conlusionDate = (String) ((List<AdministrativeOfficeDocument>) AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator
		    .create(documentRequest)).get(0).getParameters().get("degreeFinalizationDate");
	} else if (DocumentRequestType.DIPLOMA_REQUEST.equals(documentRequest.getDocumentRequestType())) {
	    conlusionDate = (String) ((List<AdministrativeOfficeDocument>) AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator
		    .create(documentRequest)).get(0).getParameters().get("conclusionDate");
	}

	DocumentFieldsCustomization customization = new DocumentFieldsCustomization();
	customization.setConclusionDate(conlusionDate);

	request.setAttribute("documentFieldsCustomization", customization);
	request.setAttribute("documentRequest", documentRequest);

	return mapping.findForward("printDocument");
    }

    public ActionForward printDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws JRException, IOException, FenixFilterException, FenixServiceException {
	final DocumentRequest documentRequest = getDocumentRequest(request);
	try {
	    final List<AdministrativeOfficeDocument> documents = (List<AdministrativeOfficeDocument>) AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator
		    .create(documentRequest);

	    DocumentFieldsCustomization customization = (DocumentFieldsCustomization) getRenderedObject("document.fields.customization");

	    if (DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE.equals(documentRequest.getDocumentRequestType())) {
		documents.iterator().next().getParameters().put("degreeFinalizationDate", customization.getConclusionDate());
	    } else if (DocumentRequestType.DIPLOMA_REQUEST.equals(documentRequest.getDocumentRequestType())) {
		documents.iterator().next().getParameters().put("conclusionDate", customization.getConclusionDate());
	    }

	    final AdministrativeOfficeDocument[] array = {};
	    byte[] data = ReportsUtils.exportMultipleToPdfAsByteArray(documents.toArray(array));

	    DocumentRequestGeneratedDocument.store(documentRequest, documents.iterator().next().getReportFileName() + ".pdf",
		    data);
	    response.setContentLength(data.length);
	    response.setContentType("application/pdf");
	    response.addHeader("Content-Disposition", "attachment; filename=" + documents.iterator().next().getReportFileName()
		    + ".pdf");

	    final ServletOutputStream writer = response.getOutputStream();
	    writer.write(data);
	    writer.flush();
	    writer.close();

	    response.flushBuffer();
	    return mapping.findForward("");
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey());
	    request.setAttribute("registration", documentRequest.getRegistration());
	    return mapping.findForward("viewRegistrationDetails");
	}
    }

    private StudentCurricularPlan readStudentCurricularPlan(HttpServletRequest request) {
	if (request.getAttribute("studentCurricularPlan") != null)
	    return (StudentCurricularPlan) request.getAttribute("studentCurricularPlan");

	request.setAttribute("studentCurricularPlan", getDomainObject(request, "studentCurricularPlanId"));
	return getDomainObject(request, "studentCurricularPlanId");

    }

    public static class DocumentFieldsCustomization implements java.io.Serializable {
	private String conclusionDate;

	public DocumentFieldsCustomization() {

	}

	public String getConclusionDate() {
	    return this.conclusionDate;
	}

	public void setConclusionDate(final String value) {
	    this.conclusionDate = value;
	}
    }
}
