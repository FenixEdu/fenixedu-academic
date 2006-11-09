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
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestEditBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.SchoolRegistrationDeclarationRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
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
	
	final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("imageUrl", getServlet().getServletContext().getRealPath("/").concat("/images/Logo_IST_color.tiff"));
        parameters.put("student", documentRequest.getRegistration().getStudent());
        parameters.put("documentRequest", documentRequest);
        
        final YearMonthDay yearMonthDay = new YearMonthDay();
        parameters.put("day", yearMonthDay.toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
        
        ResourceBundle resourceBundle = null;
        
        final List dataSource = new ArrayList();
        dataSource.add(documentRequest);
        
	if (documentRequest instanceof SchoolRegistrationDeclarationRequest) {
	    final RegistrationDeclaration registrationDeclaration = new RegistrationDeclaration(documentRequest.getRegistration(), getLoggedPerson(request));
	    parameters.put("RegistrationDeclaration", registrationDeclaration);
	    resourceBundle = RegistrationDeclaration.resourceBundle;
	    dataSource.add(registrationDeclaration);
	}

        byte[] data = ReportsUtils.exportToPdf(documentRequest.getDocumentTemplateKey(), parameters, resourceBundle, dataSource);
        
        response.setContentLength(data.length);
	response.setContentType("application/pdf");
	response.addHeader("Content-Disposition", "attachment; filename="+ documentRequest.getDocumentFileName() + ".pdf");
        
        final ServletOutputStream writer = response.getOutputStream();
        writer.write(data);
        writer.flush();
        writer.close();
        
        response.flushBuffer();

        return mapping.findForward("");
    }
    
    public ActionForward prepareConcludeDocumentRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	if (!academicServiceRequest.getStudentCurricularPlan().isBolonha()) {
	    addActionMessage(request, "print.preBolonha.documentRequest.in.aplica");
	}

	return mapping.findForward("printDocument");
    }

    public ActionForward prepareCreateDocumentRequest(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
    
    final Employee employee = AccessControl.getUserView().getPerson().getEmployee();
    final AdministrativeOffice administrativeOffice = AdministrativeOffice.readByEmployee(employee);
    Registration registration = getRegistration(request);

    if (registration.getStudent().hasRegistrationForOffice(administrativeOffice)) {

        request.setAttribute("registration", registration);
        request.setAttribute("executionYears", registration.getEnrolmentsExecutionYears());
    } else {
        addActionMessage(request, "message.no.enrolments");
    }

    return mapping.findForward("createDocumentRequests");
    
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
    
    private ExecutionYear getExecutionYear(final HttpServletRequest request,final DynaActionForm dynaActionForm,String executionYearString) {   
    final Integer executionYearId = (Integer) dynaActionForm.get(executionYearString);
    final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);
    
    return executionYear;
    }

    public ActionForward viewRegistrationDocumentRequestsHistoric(ActionMapping mapping,
        ActionForm form, HttpServletRequest request, HttpServletResponse response) {

    request.setAttribute("registration", getRegistration(request));
    return mapping.findForward("viewRegistrationDocumentRequestsHistoric");
    }
   
    public ActionForward viewDocumentRequestsToCreate(ActionMapping mapping, ActionForm actionForm,
        HttpServletRequest request, HttpServletResponse response) {
   
    Registration registration = getRegistration(request);

  
    final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
    dynaActionForm.set("registrationId", registration.getIdInternal());
    
    request.setAttribute("executionYears", registration.getEnrolmentsExecutionYears());
    final String chosenDocumentRequestType = getAndSetChosenDocumentRequestTypes(request,
            dynaActionForm);
    final DocumentPurposeType chosenDocumentPurposeType = getAndSetChoseDocumentPurposeType(request,
            dynaActionForm);
    
    final String otherPurpose = getAndSetOtherPurpose(request, dynaActionForm,
            chosenDocumentPurposeType);
    if (hasActionMessage(request)) {
        return mapping.findForward("createDocumentRequests");
    }

    String isUrgentString = dynaActionForm.getString("isUrgent");
    Boolean isUrgent = StringUtils.isEmpty(isUrgentString) ? null : Boolean.valueOf(isUrgentString);
    getAndSetDocumentRequestCreateBeans(request, dynaActionForm, registration,
            chosenDocumentRequestType, chosenDocumentPurposeType, otherPurpose, dynaActionForm
                    .getString("notes"), isUrgent);

    return mapping.findForward("viewDocumentRequestsToCreate");
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm,
        HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
        FenixServiceException {
    Registration registration = getRegistration(request);
    final Employee employee = AccessControl.getUserView().getPerson().getEmployee();
    final AdministrativeOffice administrativeOffice = AdministrativeOffice.readByEmployee(employee);
    
    final Object[] args = { getConfirmedDocumentRequestCreateBeans(),administrativeOffice };
    final List<String> messages = (List<String>) executeService(request,
            "CreateDocumentRequestsByAcademicOffice", args);

    for (final String message : messages) {
        final ActionMessages actionMessages = new ActionMessages();
        actionMessages.add(message, new ActionMessage(message));
        saveMessages(request, actionMessages);
    }
    request.setAttribute("registration",registration);
    
    request.setAttribute("documentRequestCreateBeans",getConfirmedDocumentRequestCreateBeans());
    return mapping.findForward("viewDocumentRequestsCreateSucess");
    }
    
   

    private List<DocumentRequestCreateBean> getConfirmedDocumentRequestCreateBeans() {
    final List<DocumentRequestCreateBean> result = new ArrayList<DocumentRequestCreateBean>();

    for (final DocumentRequestCreateBean documentRequestCreateBean : (List<DocumentRequestCreateBean>) RenderUtils
            .getViewState().getMetaObject().getObject()) {
        if (documentRequestCreateBean.getToBeCreated()) {
            result.add(documentRequestCreateBean);
        }
    }
    return result;
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

    private void getAndSetExecutionYears(HttpServletRequest request,
        StudentCurricularPlan studentCurricularPlan, Registration registration) {
    if (studentCurricularPlan != null
            && studentCurricularPlan.getEnrolmentsExecutionYears().isEmpty()) {
        addActionMessage(request, "message.no.enrolments");
    } else if (studentCurricularPlan != null) {
        request.setAttribute("executionYears", studentCurricularPlan.getEnrolmentsExecutionYears());
    }
    }

    private String getAndSetChosenDocumentRequestTypes(HttpServletRequest request,
        final DynaActionForm dynaActionForm) {
    final String chosenDocumentRequestType = (String) dynaActionForm
            .get("chosenDocumentRequestType");
    if (chosenDocumentRequestType == null) {
        addActionMessage(request, "error.choose.one.type.of.document");
    }
    request.setAttribute("chosenDocumentRequestTypes", chosenDocumentRequestType);
    return chosenDocumentRequestType;
    }

    private DocumentPurposeType getAndSetChoseDocumentPurposeType(HttpServletRequest request,
        final DynaActionForm dynaActionForm) {
    final DocumentPurposeType chosenDocumentPurposeType = (!StringUtils.isEmpty(dynaActionForm
            .getString("chosenDocumentPurposeType"))) ? DocumentPurposeType.valueOf(dynaActionForm
            .getString("chosenDocumentPurposeType")) : null;
    request.setAttribute("chosenDocumentPurposeType", chosenDocumentPurposeType);
    return chosenDocumentPurposeType;
    }

    private String getAndSetOtherPurpose(HttpServletRequest request,
        final DynaActionForm dynaActionForm, final DocumentPurposeType chosenDocumentPurposeType) {
    final String otherPurpose = dynaActionForm.getString("otherPurpose");
    if (chosenDocumentPurposeType == DocumentPurposeType.OTHER) {
        if (otherPurpose == null) {
            addActionMessage(request, "error.fill.notes");
        }
    } else if (chosenDocumentPurposeType != null) {
        if (!StringUtils.isEmpty(otherPurpose)) {
            addActionMessage(request, "error.only.one.purpose");
        }
    }
    request.setAttribute("otherPurpose", otherPurpose);
    return otherPurpose;
    }

    private void getAndSetDocumentRequestCreateBeans(HttpServletRequest request,
    final DynaActionForm dynaActionForm, Registration registration,
    final String chosenDocumentRequestType,
   
    final DocumentPurposeType chosenDocumentPurposeType, final String otherPurpose,
    final String notes, final Boolean urgentRequest) {
    
   
    StudentCurricularPlan  studentCurricularPlan = null;
    final List<DocumentRequestCreateBean> documentRequestCreateBeans = new ArrayList<DocumentRequestCreateBean>();


    final DocumentRequestType documentRequestType = DocumentRequestType
            .valueOf(chosenDocumentRequestType);
    
    if(documentRequestType.equals(DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE)){
        studentCurricularPlan = registration.getStudentCurricularPlan(getExecutionYear(request,dynaActionForm,"schoolRegistrationExecutionYearId"));
    }else if(documentRequestType.equals(DocumentRequestType.ENROLMENT_CERTIFICATE)){
        studentCurricularPlan = registration.getStudentCurricularPlan(getExecutionYear(request,dynaActionForm,"enrolmentExecutionYearId"));
    }else{
        studentCurricularPlan = registration.getLastStudentCurricularPlan();
    }
   
    
    final DocumentRequestCreateBean documentRequestCreateBean = buildDocumentRequestCreateBean(
            dynaActionForm, studentCurricularPlan, chosenDocumentPurposeType, otherPurpose,
            notes, urgentRequest, documentRequestType,registration);

    documentRequestCreateBeans.add(documentRequestCreateBean);
    
    request.setAttribute("documentRequestCreateBeans", documentRequestCreateBeans);
    }

    private DocumentRequestCreateBean buildDocumentRequestCreateBean(
    final DynaActionForm dynaActionForm, final StudentCurricularPlan studentCurricularPlan,
    final DocumentPurposeType chosenDocumentPurposeType, final String otherPurpose,
    final String notes, final Boolean urgentRequest,
    final DocumentRequestType documentRequestType,Registration registration) {
    
    final List<String> warningsToReport = new ArrayList<String>();

    final DocumentRequestCreateBean documentRequestCreateBean = new DocumentRequestCreateBean();

    documentRequestCreateBean.setToBeCreated(Boolean.TRUE);
    documentRequestCreateBean.setStudentCurricularPlan(studentCurricularPlan);
    documentRequestCreateBean.setChosenDocumentRequestType(documentRequestType);
    documentRequestCreateBean.setChosenDocumentPurposeType(chosenDocumentPurposeType);
    documentRequestCreateBean.setOtherPurpose(otherPurpose);
    documentRequestCreateBean.setNotes(notes);
 
    documentRequestCreateBean.setUrgentRequest(urgentRequest);

    final Boolean average;
    final Boolean detailed;
    final ExecutionYear executionYear;
    if (documentRequestType == DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE) {
        average = null;
        detailed = null;

        Integer executionYearId = (Integer) dynaActionForm.get("schoolRegistrationExecutionYearId");
        executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);
       
    } else if (documentRequestType == DocumentRequestType.ENROLMENT_CERTIFICATE) {
        average = null;
        detailed = Boolean.valueOf(dynaActionForm.getString("enrolmentDetailed"));

        Integer executionYearId = (Integer) dynaActionForm.get("enrolmentExecutionYearId");
        executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);
    } else if (documentRequestType == DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE) {
        average = Boolean.valueOf(dynaActionForm.getString("degreeFinalizationAverage"));
        detailed = Boolean.valueOf(dynaActionForm.getString("degreeFinalizationDetailed"));
        executionYear = null;
    } else if(documentRequestType == DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION
            || documentRequestType == DocumentRequestType.ENROLMENT_DECLARATION){
        average = null;
        detailed = null;

        executionYear = (ExecutionYear)(registration.getLastStudentCurricularPlan().getEnrolmentsExecutionYears().iterator().next());
    }else {
    
        average = null;
        executionYear = null;
        detailed = null;
    }
    documentRequestCreateBean.setAverage(average);
    documentRequestCreateBean.setDetailed(detailed);
    documentRequestCreateBean.setExecutionYear(executionYear);

    if (documentRequestCreateBean.hasWarningsToReport()) {
        warningsToReport.addAll(documentRequestCreateBean.getWarningsToReport());
    }

    return documentRequestCreateBean;
    }

}
