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
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists.SearchStudents;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchStudentsByDegreeParametersBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationWithStateForExecutionYearBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class StudentsListByDegreeDA extends FenixDispatchAction {

    public ActionForward prepareByDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("searchParametersBean", new SearchStudentsByDegreeParametersBean());
	return mapping.findForward("searchRegistrations");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Object renderedObject = getRenderedObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("searchParametersBean", renderedObject);

	return mapping.findForward("searchRegistrations");
    }

    public ActionForward searchByDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final SearchStudentsByDegreeParametersBean searchBean = (SearchStudentsByDegreeParametersBean) getRenderedObject();

	final List<RegistrationWithStateForExecutionYearBean> registrations = (List<RegistrationWithStateForExecutionYearBean>) SearchStudents
		.run(searchBean);

	request.setAttribute("searchParametersBean", searchBean);
	request.setAttribute("studentCurricularPlanList", registrations);

	return mapping.findForward("searchRegistrations");
    }

    public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	final SearchStudentsByDegreeParametersBean searchBean = (SearchStudentsByDegreeParametersBean) getRenderedObject();
	if (searchBean != null) {
	    final List<RegistrationWithStateForExecutionYearBean> registrations = (List<RegistrationWithStateForExecutionYearBean>) SearchStudents
		    .run(searchBean);

	    try {
		String filename;

		ExecutionYear executionYear = searchBean.getExecutionYear();
		if (searchBean.getDegree() == null) {
		    filename = executionYear.getNextYearsYearString();
		} else {
		    filename = searchBean.getDegree().getNameFor(executionYear) + "_" + executionYear.getNextYearsYearString();
		}

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
		ServletOutputStream writer = response.getOutputStream();

		final String param = request.getParameter("extendedInfo");
		boolean extendedInfo = param != null && param.length() > 0 && Boolean.valueOf(param).booleanValue();

		exportToXls(registrations, writer, searchBean.getExecutionYear(), searchBean.getDegree(), extendedInfo);
		writer.flush();
		response.flushBuffer();

	    } catch (IOException e) {
		throw new FenixServiceException();
	    }
	}
	return null;
    }

    private void exportToXls(List<RegistrationWithStateForExecutionYearBean> registrationWithStateForExecutionYearBean,
	    OutputStream outputStream, ExecutionYear executionYear, Degree degree, boolean extendedInfo) throws IOException {

	final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("AlunosPorCurso");
	spreadsheet.newHeaderRow();
	if (degree == null) {
	    spreadsheet.addHeader(executionYear.getYear());
	} else {
	    spreadsheet.addHeader(degree.getNameFor(executionYear) + " - " + executionYear.getYear());
	}
	spreadsheet.newRow();
	spreadsheet.newRow();
	spreadsheet.addCell(registrationWithStateForExecutionYearBean.size() + " Alunos");
	fillSpreadSheet(registrationWithStateForExecutionYearBean, spreadsheet, executionYear, extendedInfo);
	spreadsheet.getWorkbook().write(outputStream);
    }

    private void fillSpreadSheet(List<RegistrationWithStateForExecutionYearBean> registrations,
	    final StyledExcelSpreadsheet spreadsheet, ExecutionYear executionYear, boolean extendedInfo) {
	setHeaders(spreadsheet, extendedInfo);
	for (RegistrationWithStateForExecutionYearBean registrationWithStateForExecutionYearBean : registrations) {
	    Registration registration = registrationWithStateForExecutionYearBean.getRegistration();
	    spreadsheet.newRow();
	    spreadsheet.addCell(registration.getDegree().getSigla());
	    spreadsheet.addCell(registration.getNumber().toString());
	    final Person person = registration.getPerson();
	    spreadsheet.addCell(person.getName());
	    final RegistrationState lastRegistrationState = registration.getLastRegistrationState(executionYear);

	    spreadsheet.addCell(lastRegistrationState.getStateType().getDescription());
	    spreadsheet.addCell(registration.getRegistrationAgreement().getName());

	    if (extendedInfo) {
		spreadsheet.addCell(person.getGender().toLocalizedString());
		spreadsheet.addCell(person.getDateOfBirthYearMonthDay().toString("yyyy-MM-dd"));
		spreadsheet.addCell(registration.getEnrolmentsExecutionYears().size());
		spreadsheet.addCell(registration.getCurricularYear(executionYear));
		spreadsheet.addCell(registration.getEnrolments(executionYear).size());
	    }
	}
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet, final boolean extendedInfo) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader("Curso");
	spreadsheet.addHeader("Número");
	spreadsheet.addHeader("Nome");
	spreadsheet.addHeader("Estado da Matrícula");
	spreadsheet.addHeader("Acordo");
	if (extendedInfo) {
	    spreadsheet.addHeader("Género");
	    spreadsheet.addHeader("Data Nascimento");
	    spreadsheet.addHeader("Nº inscrições");
	    spreadsheet.addHeader("Ano académico");
	    spreadsheet.addHeader("Nº disciplinas inscritas");
	}
    }

}