package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests.documentRequests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestEditBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.SchoolRegistrationDeclarationRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest.DocumentRequestCreator;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.RegistrationDeclaration;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.YearMonthDay;

public class DocumentRequestsManagementDispatchAction extends FenixDispatchAction {

    private DocumentRequest getDocumentRequest(HttpServletRequest request) {
	return (DocumentRequest) rootDomainObject
		.readAcademicServiceRequestByOID(getRequestParameterAsInteger(request,
			"documentRequestId"));
    }

    private AcademicServiceRequest getAndSetAcademicServiceRequest(final HttpServletRequest request) {
	final AcademicServiceRequest academicServiceRequest = rootDomainObject
		.readAcademicServiceRequestByOID(getRequestParameterAsInteger(request,
			"academicServiceRequestId"));
	request.setAttribute("academicServiceRequest", academicServiceRequest);
	return academicServiceRequest;
    }

    public ActionForward printDocument(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {

	final DocumentRequest documentRequest = getDocumentRequest(request);
	final Person person = documentRequest.getRegistration().getPerson();

	// Parameters
	final Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("imageUrl", getServlet().getServletContext().getRealPath("/").concat(
		"/images/Logo_IST_color.tiff"));
	parameters.put("documentRequest", documentRequest);
	parameters.put("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils
		.getLocale()));

	final String name = person.getName().toUpperCase();
	final String documentIdNumber = person.getDocumentIdNumber();
	final String birthLocale = person.getParishOfBirth().toUpperCase() + ", "
		+ person.getDistrictOfBirth().toUpperCase();
	final String nameOfFather = person.getNameOfFather().toUpperCase();
	final String nameOfMother = person.getNameOfMother().toUpperCase();

	parameters.put("name", applyPadding(name));
	parameters.put("documentIdNumber", applyPadding("portador do Bilhete de Identidade "
		+ documentIdNumber));
	parameters.put("birthLocale", applyPadding("natural de " + birthLocale));
	parameters.put("nameOfFather", applyPadding("filho de " + nameOfFather));
	parameters.put("nameOfMother", applyPadding("e de " + nameOfMother));

	// Bundle
	ResourceBundle resourceBundle = null;

	// Data Source
	final List dataSource = new ArrayList();

	if (documentRequest instanceof EnrolmentCertificateRequest) {
	    final EnrolmentCertificateRequest enrolmentCertificateRequest = (EnrolmentCertificateRequest) documentRequest;
	    final ExecutionYear executionYear = enrolmentCertificateRequest.getExecutionYear();

	    final Integer curricularYear = Integer.valueOf(enrolmentCertificateRequest.getRegistration()
		    .getCurricularYear(executionYear));
	    parameters.put("curricularYear", curricularYear);
	    if (enrolmentCertificateRequest.getDetailed()) {
		GenericPair<String, String> dummy = new GenericPair<String, String>("\t\t"
			+ curricularYear + ".ANO", null);
		dataSource.add(dummy);

		final List<Enrolment> enrolments = enrolmentCertificateRequest.getRegistration()
			.getStudentCurricularPlan(executionYear).getEnrolmentsByExecutionYear(
				executionYear);
		for (final Enrolment enrolment : enrolments) {
		    dummy = new GenericPair<String, String>(applyPadding(enrolment.getName()
			    .toUpperCase()), null);
		    dataSource.add(dummy);
		}
		parameters.put("numberEnrolments", Integer.valueOf(enrolments.size()));
	    } else {
		GenericPair<String, String> dummy = new GenericPair<String, String>(StringUtils.EMPTY,
			null);
		dataSource.add(dummy);
	    }
	} else if (documentRequest instanceof SchoolRegistrationDeclarationRequest) {
	    final RegistrationDeclaration registrationDeclaration = new RegistrationDeclaration(
		    documentRequest.getRegistration(), getLoggedPerson(request));
	    parameters.put("RegistrationDeclaration", registrationDeclaration);
	    resourceBundle = RegistrationDeclaration.resourceBundle;
	    dataSource.add(registrationDeclaration);
	}

	byte[] data = ReportsUtils.exportToPdf(documentRequest.getDocumentTemplateKey(), parameters,
		resourceBundle, dataSource);

	response.setContentLength(data.length);
	response.setContentType("application/pdf");
	response.addHeader("Content-Disposition", "attachment; filename="
		+ documentRequest.getDocumentFileName() + ".pdf");

	final ServletOutputStream writer = response.getOutputStream();
	writer.write(data);
	writer.flush();
	writer.close();

	response.flushBuffer();

	return mapping.findForward("");
    }

    private String applyPadding(String field) {
	final int LINE_LENGTH = 64;

	if (field.length() <= LINE_LENGTH) {
	    return StringUtils.rightPad(field + " ", LINE_LENGTH, '-');
	} else {
	    final List<String> words = Arrays.asList(field.split(" "));
	    int currentLineLength = 0;
	    String result = StringUtils.EMPTY;

	    for (final String word : words) {
		final String toAdd = word + " ";
		if (words.lastIndexOf(word) != (words.size() - 1)
			&& (currentLineLength + toAdd.length()) > LINE_LENGTH) {
		    int spacesLength = LINE_LENGTH - currentLineLength;
		    result = StringUtils.rightPad(result, result.length() + spacesLength, ' ');

		    currentLineLength = toAdd.length();
		} else {
		    currentLineLength += toAdd.length();
		}

		result += toAdd;
	    }

	    if (currentLineLength <= LINE_LENGTH) {
		return StringUtils.rightPad(result, result.length() + (LINE_LENGTH - currentLineLength),
			'-');
	    }

	    return result;
	}
    }

    public ActionForward prepareConcludeDocumentRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	if (!academicServiceRequest.getRegistration().getActiveStudentCurricularPlan().isBolonha()) {
	    addActionMessage(request, "print.preBolonha.documentRequest.in.aplica");
	}

	return mapping.findForward("printDocument");
    }

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return mapping.findForward("prepareSearch");
    }

    public ActionForward search(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	DynaActionForm form = (DynaActionForm) actionForm;

	DocumentRequestType documentRequestType = (DocumentRequestType) getEnum(form,
		"documentRequestType", DocumentRequestType.class);

	AcademicServiceRequestSituationType requestSituationType = (AcademicServiceRequestSituationType) getEnum(
		form, "requestSituationType", AcademicServiceRequestSituationType.class);

	String isUrgentString = form.getString("isUrgent");
	Boolean isUrgent = StringUtils.isEmpty(isUrgentString) ? null : Boolean.valueOf(isUrgentString);

	Registration registration = getStudent(form.getString("studentNumber"));

	request.setAttribute("documentRequestsResult", getAdministrativeOffice().searchDocumentsBy(
		documentRequestType, requestSituationType, isUrgent, registration));

	request.setAttribute("url", buildUrl(form, request).toString());

	return mapping.findForward("showDocumentRequests");
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

    private Registration getStudent(String studentNumberString) {
	try {
	    return Registration.readStudentByNumberAndDegreeType(Integer.valueOf(studentNumberString),
		    DegreeType.DEGREE);
	} catch (NumberFormatException e) {
	    return null;
	}
    }

    public AdministrativeOffice getAdministrativeOffice() {
	return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

    private Enum getEnum(DynaActionForm form, String name, Class type) {
	return StringUtils.isEmpty(form.getString(name)) ? null : Enum.valueOf(type, form
		.getString(name));
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

    public ActionForward processNewDocuments(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final List<Integer> documentIdsToProcess = Arrays
		.asList((Integer[]) ((DynaActionForm) actionForm).get("documentIdsToProcess"));
	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
		    "ProcessNewAcademicServiceRequests", new Object[] {
			    SessionUtils.getUserView(request).getPerson().getEmployee(),
			    documentIdsToProcess });

	    request.setAttribute("registration", getRegistration(request));
	    return mapping.findForward("viewRegistrationDetails");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    return mapping.findForward("viewRegistrationDetails");
	}

    }

    public ActionForward viewDocumentRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("documentRequest", getDocumentRequest(request));
	request.setAttribute("url", buildUrl(form, request).toString());

	return mapping.findForward("viewDocumentRequest");
    }

    public ActionForward prepareCreateDocumentRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("documentRequestCreateBean", new DocumentRequestCreator(
		getRegistration(request)));

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

	executeFactoryMethod(request);
	addActionMessage(request, "document.request.created.with.success");
	request.setAttribute("registration", ((DocumentRequestCreateBean) getRenderedObject())
		.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

}
