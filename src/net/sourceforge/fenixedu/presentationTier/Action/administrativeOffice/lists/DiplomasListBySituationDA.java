/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists.SearchDiplomas;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchDiplomasBySituationParametersBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class DiplomasListBySituationDA extends FenixDispatchAction {

    @SuppressWarnings("unused")
    public ActionForward prepareBySituation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("searchParametersBean", new SearchDiplomasBySituationParametersBean(AccessControl.getPerson()
		.getEmployee()));
	return mapping.findForward("searchDiplomas");
    }

    @SuppressWarnings("unused")
    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Object renderedObject = getRenderedObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("searchParametersBean", renderedObject);

	return mapping.findForward("searchDiplomas");
    }

    @SuppressWarnings("unused")
    public ActionForward searchBySituation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final SearchDiplomasBySituationParametersBean bean = (SearchDiplomasBySituationParametersBean) getRenderedObject();
	request.setAttribute("searchParametersBean", bean);
	request.setAttribute("diplomasList", SearchDiplomas.run(bean));

	return mapping.findForward("searchDiplomas");
    }

    @SuppressWarnings("unused")
    public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	final SearchDiplomasBySituationParametersBean bean = (SearchDiplomasBySituationParametersBean) getRenderedObject();
	if (bean != null) {
	    final Collection<DiplomaRequest> requests = SearchDiplomas.run(bean);

	    try {
		String filename = "Diplomas_" + bean.getAcademicServiceRequestSituationType().getLocalizedName() + "_"
			+ bean.getSearchBegin().toString("yyyy-MM-dd") + "_" + bean.getSearchEnd().toString("yyyy-MM-dd");

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

	final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("Diplomas");
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
		// TODO
	    }
	}
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet, final boolean extendedInfo) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader("Nr Aluno");
	spreadsheet.addHeader("Nome");
	spreadsheet.addHeader("Curso");
	spreadsheet.addHeader("Pedido");
	spreadsheet.addHeader("Estado");
	spreadsheet.addHeader("Id Pedido");

	if (extendedInfo) {
	    // TODO
	}
    }

}
