/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchStudentsByDegreeParametersBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationWithStateForExecutionYearBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class StudentsListByDegreeDA extends FenixDispatchAction {

	public ActionForward prepareByDegree(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("searchParametersBean",
				new SearchStudentsByDegreeParametersBean());
		return mapping.findForward("searchRegistrations");
	}

	public ActionForward postBack(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) {

		final Object renderedObject = getRenderedObject();
		RenderUtils.invalidateViewState();
		request.setAttribute("searchParametersBean", renderedObject);

		return mapping.findForward("searchRegistrations");
	}

	public ActionForward searchByDegree(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		final SearchStudentsByDegreeParametersBean searchBean = (SearchStudentsByDegreeParametersBean) getRenderedObject();

		final List<RegistrationWithStateForExecutionYearBean> registrations = (List<RegistrationWithStateForExecutionYearBean>) executeService(
				"SearchStudents", searchBean);

		request.setAttribute("searchParametersBean", searchBean);
		request.setAttribute("studentCurricularPlanList", registrations);

		return mapping.findForward("searchRegistrations");
	}

	public ActionForward exportInfoToExcel(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixServiceException,
			FenixFilterException {

		final SearchStudentsByDegreeParametersBean searchBean = (SearchStudentsByDegreeParametersBean) getRenderedObject();
		if (searchBean != null) {
			final List<RegistrationWithStateForExecutionYearBean> registrations = (List<RegistrationWithStateForExecutionYearBean>) executeService(
					"SearchStudents",
					(SearchStudentsByDegreeParametersBean) searchBean);

			try {
				String filename;

				filename = searchBean.getDegree().getName()
						+ "_"
						+ searchBean.getExecutionYear()
								.getNextYearsYearString();

				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition",
						"attachment; filename=" + filename + ".xls");
				ServletOutputStream writer = response.getOutputStream();

				exportToXls(registrations, writer, searchBean
						.getExecutionYear(), searchBean.getDegree());
				writer.flush();
				response.flushBuffer();

			} catch (IOException e) {
				throw new FenixServiceException();
			}
		}
		return null;
	}

	private void exportToXls(
			List<RegistrationWithStateForExecutionYearBean> registrationWithStateForExecutionYearBean,
			OutputStream outputStream, ExecutionYear executionYear,
			Degree degree) throws IOException {

		final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(
				"AlunosPorCurso");
		spreadsheet.newHeaderRow();
		spreadsheet.addHeader(degree.getName() + " - "
				+ executionYear.getNextYearsYearString());
		spreadsheet.newRow();
		spreadsheet.newRow();
		spreadsheet.addCell(registrationWithStateForExecutionYearBean.size()
				+ " Alunos");
		fillSpreadSheet(registrationWithStateForExecutionYearBean, spreadsheet,
				executionYear);
		spreadsheet.getWorkbook().write(outputStream);
	}

	private void fillSpreadSheet(
			List<RegistrationWithStateForExecutionYearBean> registrations,
			final StyledExcelSpreadsheet spreadsheet,
			ExecutionYear executionYear) {
		setHeaders(spreadsheet);
		for (RegistrationWithStateForExecutionYearBean registrationWithStateForExecutionYearBean : registrations) {
			Registration registration = (Registration) registrationWithStateForExecutionYearBean
					.getRegistration();
			spreadsheet.newRow();
			spreadsheet.addCell(registration.getNumber().toString());
			spreadsheet.addCell(registration.getPerson().getName());
			final RegistrationState lastRegistrationState = registration
					.getLastRegistrationState(executionYear);

			spreadsheet.addCell(lastRegistrationState.getStateType()
					.getDescription());
			spreadsheet.addCell(registration.getRegistrationAgreement()
					.getName());
		}
	}

	private void setHeaders(final StyledExcelSpreadsheet spreadsheet) {
		spreadsheet.newHeaderRow();
		spreadsheet.addHeader("Número");
		spreadsheet.addHeader("Nome");
		spreadsheet.addHeader("Estado da Matrícula");
		spreadsheet.addHeader("Acordo");

	}

}
