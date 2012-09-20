/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchDiplomasBySituationParametersBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */

@Mapping(path = "/diplomasListBySituation", module = DiplomasListBySituationDA.MODULE)
@Forwards( { @Forward(name = "searchDiplomas", path = "/academicAdminOffice/lists/searchDiplomasBySituation.jsp") })
public class DiplomasListBySituationDA extends FenixDispatchAction {

    private static final String LOCALDATE_FORMAT = "yyyy-MM-dd";
    protected static final String MODULE = "academicAdminOffice";

    public ActionForward prepareBySituation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("searchParametersBean", getOrCreateSearchParametersBean());
	return mapping.findForward("searchDiplomas");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final SearchDiplomasBySituationParametersBean searchParametersBean = getOrCreateSearchParametersBean();
	RenderUtils.invalidateViewState();
	request.setAttribute("searchParametersBean", searchParametersBean);

	return mapping.findForward("searchDiplomas");
    }

    private SearchDiplomasBySituationParametersBean getOrCreateSearchParametersBean() {
	SearchDiplomasBySituationParametersBean bean = getRenderedObject("searchParametersBean");
	return (bean != null) ? bean : new SearchDiplomasBySituationParametersBean(AccessControl.getPerson().getEmployee());
    }

    public ActionForward searchBySituation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final SearchDiplomasBySituationParametersBean bean = getOrCreateSearchParametersBean();
	request.setAttribute("searchParametersBean", bean);
	try {
	    request.setAttribute("diplomasList", search(bean));
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	}
	return mapping.findForward("searchDiplomas");
    }

    private static Collection<DiplomaRequest> search(final SearchDiplomasBySituationParametersBean bean) {
	final Collection<DiplomaRequest> result = new HashSet<DiplomaRequest>();

	for (final AcademicServiceRequest request : bean.searchAcademicServiceRequests()) {
	    if (!request.isRequestForRegistration()) {
		continue;
	    }
	    if (!request.isDocumentRequest() || !((DocumentRequest) request).isDiploma()) {
		continue;
	    }
	    DiplomaRequest diplomaRequest = (DiplomaRequest) request;

	    DegreeType diplomaDegreeType = diplomaRequest.getRegistration().getDegree().getDegreeType();
	    if ((diplomaDegreeType != DegreeType.EMPTY) && (!getAdministratedDegreeTypes().contains(diplomaDegreeType))) {
		continue;
	    }

	    result.add(diplomaRequest);
	}

	return result;
    }

    private static Collection<DegreeType> getAdministratedDegreeTypes() {
	return AccessControl.getPerson().getEmployee().getAdministrativeOffice().getAdministratedDegreeTypes();
    }

    public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	final SearchDiplomasBySituationParametersBean bean = getOrCreateSearchParametersBean();
	if (bean != null) {
	    final Collection<DiplomaRequest> requests = search(bean);

	    try {
		String filename = getResourceMessage("label.diplomas") + "_"
			+ bean.getAcademicServiceRequestSituationType().getLocalizedName() + "_"
			+ bean.getSearchBegin().toString(LOCALDATE_FORMAT) + "_-_"
			+ bean.getSearchEnd().toString(LOCALDATE_FORMAT);

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
		ServletOutputStream writer = response.getOutputStream();

		final String param = request.getParameter("extendedInfo");
		boolean extendedInfo = param != null && param.length() > 0 && Boolean.valueOf(param).booleanValue();

		exportToXls(requests, writer, extendedInfo);
		writer.flush();
		response.flushBuffer();

	    } catch (IOException e) {
		throw new FenixServiceException();
	    }
	}
	return null;
    }

    private void exportToXls(final Collection<DiplomaRequest> requests, final OutputStream os, final boolean extendedInfo)
	    throws IOException {

	final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(getResourceMessage("label.diplomas"));
	fillSpreadSheet(requests, spreadsheet, extendedInfo);
	spreadsheet.getWorkbook().write(os);
    }

    private void fillSpreadSheet(final Collection<DiplomaRequest> requests, final StyledExcelSpreadsheet sheet,
	    final boolean extendedInfo) {

	setHeaders(sheet, extendedInfo);

	for (DiplomaRequest request : requests) {
	    final Registration registration = request.getRegistration();
	    final Person person = registration.getPerson();

	    sheet.newRow();
	    sheet.addCell(registration.getNumber().toString());
	    sheet.addCell(person.getName());
	    sheet.addCell(registration.getDegreeNameWithDegreeCurricularPlanName());
	    sheet.addCell(request.getDescription());
	    sheet.addCell(request.getAcademicServiceRequestSituationType().getLocalizedName());
	    sheet.addCell(request.getServiceRequestNumberYear());

	    if (extendedInfo) {
		String studentEmail = request.getStudent().getPerson().getEmailForSendingEmails();
		sheet.addCell(studentEmail != null ? studentEmail : "N/A");
		sheet.addCell(request.getCreationDate());

	    }
	}
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet, final boolean extendedInfo) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(getResourceMessage("label.studentNumber"));
	spreadsheet.addHeader(getResourceMessage("label.name"));
	spreadsheet.addHeader(getResourceMessage("label.degree"));
	spreadsheet.addHeader(getResourceMessage("label.request"));
	spreadsheet.addHeader(getResourceMessage("label.state"));
	spreadsheet.addHeader(getResourceMessage("label.serviceRequestNumber"));

	if (extendedInfo) {
	    spreadsheet.addHeader(getResourceMessage("label.email"));
	    spreadsheet
		    .addHeader(getResourceMessage("label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.creationDate"));
	}
    }

    static private String getResourceMessage(String key) {
	return BundleUtil.getMessageFromModuleOrApplication(MODULE, key);
    }
}
