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
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestSearchBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
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

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */

@Mapping(path = "/requestListByDegree", module = "academicAdminOffice")
@Forwards( { @Forward(name = "searchRequests", path = "/academicAdminOffice/lists/searchRequestsByDegree.jsp") })
public class RequestListByDegreeDA extends FenixDispatchAction {

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("degreeByExecutionYearBean", new DegreeByExecutionYearBean());
	request.setAttribute("academicServiceRequestSearchBean", new AcademicServiceRequestSearchBean());

	return mapping.findForward("searchRequests");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Object degreeSearchBean = getRenderedObject("degreeByExecutionYearBean");
	final Object requestSearchBean = getRenderedObject("academicServiceRequestSearchBean");
	RenderUtils.invalidateViewState();
	request.setAttribute("degreeByExecutionYearBean", degreeSearchBean);
	request.setAttribute("academicServiceRequestSearchBean", requestSearchBean);

	return mapping.findForward("searchRequests");
    }

    public ActionForward runSearchAndShowResults(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final DegreeByExecutionYearBean degreeSearchBean = (DegreeByExecutionYearBean) getRenderedObject("degreeByExecutionYearBean");
	final AcademicServiceRequestSearchBean requestSearchBean = (AcademicServiceRequestSearchBean) getRenderedObject("academicServiceRequestSearchBean");

	final List<RegistrationAcademicServiceRequest> requestList = search(degreeSearchBean, requestSearchBean);

	request.setAttribute("degreeByExecutionYearBean", degreeSearchBean);
	request.setAttribute("academicServiceRequestSearchBean", requestSearchBean);
	request.setAttribute("registrationAcademicServiceRequestList", requestList);

	return mapping.findForward("searchRequests");
    }

    public List<RegistrationAcademicServiceRequest> search(DegreeByExecutionYearBean degreeSearchBean,
	    AcademicServiceRequestSearchBean requestSearchBean) {
	List<RegistrationAcademicServiceRequest> requestList = new ArrayList<RegistrationAcademicServiceRequest>();

	final Degree degree = degreeSearchBean.getDegree();
	final DegreeType degreetype = degreeSearchBean.getDegreeType();
	final ExecutionYear executionYear = degreeSearchBean.getExecutionYear();
	final AcademicServiceRequestType requestType = requestSearchBean.getAcademicServiceRequestType();
	final AcademicServiceRequestSituationType requestSituation = requestSearchBean.getAcademicServiceRequestSituationType();
	for (final AcademicServiceRequest academicServiceRequest : executionYear.getAcademicServiceRequests()) {
	    if (!(academicServiceRequest instanceof RegistrationAcademicServiceRequest)) {
		continue;
	    }
	    RegistrationAcademicServiceRequest request = (RegistrationAcademicServiceRequest) academicServiceRequest;

	    DegreeCurricularPlan degreeCurricularPlan = null;
	    for (DegreeCurricularPlan plan : request.getDegree().getDegreeCurricularPlansForYear(executionYear)) {
		if ((degreeCurricularPlan == null)
			|| (degreeCurricularPlan.getMostRecentExecutionDegree().isBefore(plan.getMostRecentExecutionDegree()))) {
		    degreeCurricularPlan = plan;
		}
	    }
	    if (degreeCurricularPlan == null) {
		degreeCurricularPlan = request.getDegree().getMostRecentDegreeCurricularPlan();
	    }

	    if ((degreeCurricularPlan == null) || (degreetype != null && degreetype != degreeCurricularPlan.getDegreeType())) {
		continue;
	    }
	    if (degree != null && degree != request.getDegree()) {
		continue;
	    }
	    if (requestType != null && requestType != request.getAcademicServiceRequestType()) {
		continue;
	    }
	    if (requestSituation != null
		    && requestSituation != request.getActiveSituation().getAcademicServiceRequestSituationType()) {
		continue;
	    }
	    if (requestSearchBean.isUrgentRequest() && !request.isUrgentRequest()) {
		continue;
	    }
	    requestList.add(request);
	}

	return requestList;
    }

    public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	final DegreeByExecutionYearBean degreeSearchBean = (DegreeByExecutionYearBean) getRenderedObject("degreeByExecutionYearBean");
	final AcademicServiceRequestSearchBean requestSearchBean = (AcademicServiceRequestSearchBean) getRenderedObject("academicServiceRequestSearchBean");
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
	    DegreeByExecutionYearBean degreeSearchBean, AcademicServiceRequestSearchBean requestSearchBean) throws IOException {

	final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("PedidosPorCurso");

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
	    spreadsheet.addHeader("Pedidos do tipo: " + requestType.getName());
	}
	spreadsheet.newRow();
	if (situationType != null) {
	    spreadsheet.addHeader("Estado: " + situationType.getName());
	}
	spreadsheet.newRow();
	if (requestSearchBean.isUrgentRequest()) {
	    spreadsheet.addHeader("Urgentes");
	}

	spreadsheet.newRow();
	spreadsheet.newRow();
	spreadsheet.addCell(requestList.size() + " Pedidos");
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
	spreadsheet.addHeader("Campus");
	spreadsheet.addHeader("Número de Pedido");
	spreadsheet.addHeader("Data de Entrada");
	spreadsheet.addHeader("Descrição");
	spreadsheet.addHeader("Número de Aluno");
	spreadsheet.addHeader("Nome de Aluno");
    }

}