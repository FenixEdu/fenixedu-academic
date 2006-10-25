package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestEditBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentsSearchBean;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

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

    private Registration getRegistration(final HttpServletRequest request) {
	final String registrationID = request.getParameter("registrationID");
	final Registration registration = rootDomainObject.readRegistrationByOID(Integer
		.valueOf(registrationID));
	request.setAttribute("registration", registration);
	return registration;
    }

    public ActionForward viewRegistrationDocumentRequestsHistoric(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("academicServiceRequestList", getRegistration(request).getNewDocumentRequests());
	return mapping.findForward("viewNewDocumentRequests");
    }

    public ActionForward printDocument(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final DocumentRequest documentRequest = getDocumentRequest(request);
	
	try {
	    documentRequest.checkConditions();    
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey());
	}
	
	request.setAttribute("documentRequest", documentRequest);
	return mapping.findForward("printDocument");
    }
    
    public ActionForward concludeDocumentRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final DocumentRequest documentRequest = getDocumentRequest(request);
	documentRequest.conclude(null);
	
	request.setAttribute("registration", documentRequest.getStudentCurricularPlan().getRegistration());
	return mapping.findForward("student.viewRegistrationDetails");
    }

    public ActionForward viewNewDocumentRequests(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("academicServiceRequestList", getAdministrativeOffice()
		.getNewAcademicServiceRequests());
	return mapping.findForward("viewNewDocumentRequests");
    }

    public ActionForward prepareCreateDocumentRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	StudentsSearchBean studentsSearchBean = (StudentsSearchBean) getRenderedObject();

	if (studentsSearchBean == null) { //1st time
	    studentsSearchBean = new StudentsSearchBean();
	} else {
	    final Employee employee = AccessControl.getUserView().getPerson().getEmployee();
	    final AdministrativeOffice administrativeOffice = AdministrativeOffice
		    .readByEmployee(employee);

	    final Set<Student> students = studentsSearchBean.searchForOffice(administrativeOffice);

	    if (students.size() == 1) {
		request.setAttribute("student", students.iterator().next());
		return mapping.findForward("createDocumentRequests");
	    }
	    request.setAttribute("students", students);
	}

	request.setAttribute("studentsSearchBean", studentsSearchBean);
	return mapping.findForward("chooseStudentToCreateDocumentRequest");
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
	    return mapping.findForward("showOperations");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    return mapping.findForward("showOperations");
	}

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

	request.setAttribute("url", buildUrlFrom(form));

	return mapping.findForward("showDocumentRequests");
    }

    public ActionForward viewDocumentRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("documentRequest", getDocumentRequest(request));
	request.setAttribute("url", buildUrlFrom((DynaActionForm) form));

	return mapping.findForward("viewDocumentRequest");
    }

    private String buildUrlFrom(DynaActionForm form) {

	final StringBuilder result = new StringBuilder();

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

	return result.toString();
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
	request.setAttribute("url", buildUrlFrom((DynaActionForm) form));

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
	    request.setAttribute("url", buildUrlFrom((DynaActionForm) form));

	    return mapping.findForward("editDocumentRequest");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("documentRequestEditBean", documentRequestEditBean);
	    request.setAttribute("url", buildUrlFrom((DynaActionForm) form));

	    return mapping.findForward("editDocumentRequest");
	}

    }

}
