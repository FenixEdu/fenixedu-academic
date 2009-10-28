/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.commons.DegreeByExecutionYearBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestSearchBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestYear;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/requestListByDegree", module = RequestListByDegreeDA.MODULE)
@Forwards( { @Forward(name = "searchRequests", path = "/academicAdminOffice/lists/searchRequestsByDegree.jsp") })
public class RequestListByDegreeDA extends FenixDispatchAction {

    protected static final String MODULE = "academicAdminOffice";

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("degreeByExecutionYearBean", new DegreeByExecutionYearBean());
	request.setAttribute("documentRequestSearchBean", new DocumentRequestSearchBean());

	return mapping.findForward("searchRequests");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Object degreeSearchBean = getRenderedObject("degreeByExecutionYearBean");
	final Object requestSearchBean = getRenderedObject("documentRequestSearchBean");
	RenderUtils.invalidateViewState();
	request.setAttribute("degreeByExecutionYearBean", degreeSearchBean);
	request.setAttribute("documentRequestSearchBean", requestSearchBean);

	return mapping.findForward("searchRequests");
    }

    public ActionForward runSearchAndShowResults(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final DegreeByExecutionYearBean degreeSearchBean = (DegreeByExecutionYearBean) getRenderedObject("degreeByExecutionYearBean");
	final DocumentRequestSearchBean requestSearchBean = (DocumentRequestSearchBean) getRenderedObject("documentRequestSearchBean");

	final List<RegistrationAcademicServiceRequest> requestList = search(degreeSearchBean, requestSearchBean);

	request.setAttribute("degreeByExecutionYearBean", degreeSearchBean);
	request.setAttribute("documentRequestSearchBean", requestSearchBean);
	request.setAttribute("registrationAcademicServiceRequestList", requestList);

	return mapping.findForward("searchRequests");
    }

    public List<RegistrationAcademicServiceRequest> search(DegreeByExecutionYearBean degreeSearchBean,
	    DocumentRequestSearchBean requestSearchBean) {
	List<RegistrationAcademicServiceRequest> resultList = new ArrayList<RegistrationAcademicServiceRequest>();

	final Degree chosenDegree = degreeSearchBean.getDegree();
	final DegreeType chosenDegreeType = degreeSearchBean.getDegreeType();
	final ExecutionYear chosenExecutionYear = degreeSearchBean.getExecutionYear();

	final AcademicServiceRequestType chosenServiceRequestType = requestSearchBean.getAcademicServiceRequestType();
	final DocumentRequestType chosenDocumentRequestType = requestSearchBean.getChosenDocumentRequestType();
	final AcademicServiceRequestSituationType chosenRequestSituation = requestSearchBean
		.getAcademicServiceRequestSituationType();

	ArrayList<AcademicServiceRequest> requestList = getRequestsByYear(chosenExecutionYear.getBeginCivilYear());
	requestList.addAll(getRequestsByYear(chosenExecutionYear.getEndCivilYear()));
	for (final AcademicServiceRequest academicServiceRequest : requestList) {
	    if (!(academicServiceRequest instanceof RegistrationAcademicServiceRequest)) {
		continue;
	    }
	    RegistrationAcademicServiceRequest request = (RegistrationAcademicServiceRequest) academicServiceRequest;

	    DegreeCurricularPlan degreeCurricularPlan = getMostRecentDegreeCurricularPlanForYear(request, chosenExecutionYear);
	    if ((chosenDegreeType != null)
		    && (degreeCurricularPlan == null || chosenDegreeType != degreeCurricularPlan.getDegreeType())) {
		continue;
	    }
	    if (chosenDegree != null && chosenDegree != request.getRegistration().getDegree()) {
		continue;
	    }
	    if (chosenServiceRequestType != null && chosenServiceRequestType != request.getAcademicServiceRequestType()) {
		continue;
	    }
	    if (request.getAcademicServiceRequestType() == AcademicServiceRequestType.DOCUMENT
		    && chosenDocumentRequestType != null
		    && chosenDocumentRequestType != (((DocumentRequest) request).getDocumentRequestType())) {
		continue;
	    }
	    if (chosenRequestSituation != null
		    && chosenRequestSituation != request.getActiveSituation().getAcademicServiceRequestSituationType()) {
		continue;
	    }
	    if (requestSearchBean.isUrgentRequest() && !request.isUrgentRequest()) {
		continue;
	    }
	    resultList.add(request);
	}

	return resultList;
    }

    private ArrayList<AcademicServiceRequest> getRequestsByYear(int year) {
	AcademicServiceRequestYear academicServiceRequestYear = AcademicServiceRequestYear.readByYear(year);
	if (academicServiceRequestYear == null) {
	    return new ArrayList<AcademicServiceRequest>();
	} else {
	    return new ArrayList<AcademicServiceRequest>(academicServiceRequestYear.getAcademicServiceRequests());
	}
    }

    private DegreeCurricularPlan getMostRecentDegreeCurricularPlanForYear(RegistrationAcademicServiceRequest request,
	    final ExecutionYear executionYear) {
	DegreeCurricularPlan degreeCurricularPlan = null;
	for (DegreeCurricularPlan plan : request.getRegistration().getDegree().getDegreeCurricularPlansForYear(executionYear)) {
	    if ((degreeCurricularPlan == null)
		    || (degreeCurricularPlan.getMostRecentExecutionDegree().isBefore(plan.getMostRecentExecutionDegree()))) {
		degreeCurricularPlan = plan;
	    }
	}
	if (degreeCurricularPlan == null) {
	    degreeCurricularPlan = request.getRegistration().getDegree().getMostRecentDegreeCurricularPlan();
	}
	return degreeCurricularPlan;
    }

    public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	final DegreeByExecutionYearBean degreeSearchBean = (DegreeByExecutionYearBean) getRenderedObject("degreeByExecutionYearBean");
	final DocumentRequestSearchBean requestSearchBean = (DocumentRequestSearchBean) getRenderedObject("documentRequestSearchBean");
	if ((degreeSearchBean == null) || (requestSearchBean == null)) {
	    return null;
	}
	final List<RegistrationAcademicServiceRequest> requestList = search(degreeSearchBean, requestSearchBean);

	try {
	    String filename;

	    ExecutionYear executionYear = degreeSearchBean.getExecutionYear();
	    if (degreeSearchBean.getDegree() == null) {
		filename = executionYear.getYear();
	    } else {
		filename = degreeSearchBean.getDegree().getNameFor(executionYear).getContent(Language.getLanguage()).replace(' ',
			'_')
			+ "_" + executionYear.getYear();
	    }

	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
	    ServletOutputStream writer = response.getOutputStream();

	    exportToXls(requestList, writer, degreeSearchBean, requestSearchBean);
	    writer.flush();
	    response.flushBuffer();
	    return null;

	} catch (IOException e) {
	    throw new FenixServiceException();
	}
    }

    private void exportToXls(List<RegistrationAcademicServiceRequest> requestList, OutputStream outputStream,
	    DegreeByExecutionYearBean degreeSearchBean, DocumentRequestSearchBean requestSearchBean) throws IOException {

	final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(
		getResourceMessage("label.requestByDegree.unspaced"));

	Degree degree = degreeSearchBean.getDegree();
	ExecutionYear executionYear = degreeSearchBean.getExecutionYear();
	spreadsheet.newHeaderRow();
	if (degree == null) {
	    spreadsheet.addHeader(executionYear.getYear());
	} else {
	    spreadsheet.addHeader(degree.getNameFor(executionYear) + " - " + executionYear.getYear());
	}

	AcademicServiceRequestType requestType = requestSearchBean.getAcademicServiceRequestType();
	AcademicServiceRequestSituationType situationType = requestSearchBean.getAcademicServiceRequestSituationType();
	spreadsheet.newRow();
	if (requestType != null) {
	    spreadsheet.addHeader(getResourceMessage("label.type")
		    + ": "
		    + getResourceMessageFromModule("Enumeration", requestType.getClass().getSimpleName() + "."
			    + requestType.getName()));
	}
	spreadsheet.newRow();
	if (situationType != null) {
	    spreadsheet.addHeader(getResourceMessage("label.state")
		    + ": "
		    + getResourceMessageFromModule("Enumeration", situationType.getClass().getSimpleName() + "."
			    + situationType.getName()));
	}
	spreadsheet.newRow();
	if (requestSearchBean.isUrgentRequest()) {
	    spreadsheet.addHeader(getResourceMessage("label.urgent.plural"));
	}

	spreadsheet.newRow();
	spreadsheet.newRow();
	spreadsheet.addCell(requestList.size() + " " + getResourceMessage("label.requests"));
	fillSpreadSheet(requestList, spreadsheet, executionYear);
	spreadsheet.getWorkbook().write(outputStream);
    }

    private void fillSpreadSheet(List<RegistrationAcademicServiceRequest> requestList, final StyledExcelSpreadsheet spreadsheet,
	    ExecutionYear executionYear) {
	setHeaders(spreadsheet);
	for (RegistrationAcademicServiceRequest request : requestList) {
	    spreadsheet.newRow();
	    spreadsheet.addCell(request.getCampus().getName());
	    spreadsheet.addCell(request.getServiceRequestNumber());
	    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
	    spreadsheet.addCell(request.getRequestDate().toString(formatter));
	    spreadsheet.addCell(request.getDescription());
	    spreadsheet.addCell(request.getStudent().getNumber());
	    spreadsheet.addCell(request.getStudent().getName());
	}
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(getResourceMessage("campus"));
	spreadsheet.addHeader(getResourceMessage("label.serviceRequestNumber"));
	spreadsheet.addHeader(getResourceMessage("label.requestDate"));
	spreadsheet
		.addHeader(getResourceMessage("label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.description"));
	spreadsheet.addHeader(getResourceMessage("label.studentNumber"));
	spreadsheet.addHeader(getResourceMessage("label.student.name"));
    }

    private String getResourceMessage(String key) {
	return getResourceMessageFromModule(MODULE, key);
    }
}