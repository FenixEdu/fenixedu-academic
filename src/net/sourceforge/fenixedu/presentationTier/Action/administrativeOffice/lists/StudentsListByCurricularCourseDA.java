/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchStudentsByCurricularCourseParametersBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - ï¿½ngela Almeida (argelina@ist.utl.pt)
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentsListByCurricularCourseDA extends FenixDispatchAction {

    public ActionForward prepareByCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	request.setAttribute("searchBean", new SearchStudentsByCurricularCourseParametersBean());
	return mapping.findForward("chooseCurricularCourse");
    }

    public ActionForward chooseExecutionYearPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	Object searchBean = getRenderedObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("searchBean", searchBean);

	return mapping.findForward("chooseCurricularCourse");
    }

    public ActionForward showActiveCurricularCourseScope(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

	final SearchStudentsByCurricularCourseParametersBean searchBean = (SearchStudentsByCurricularCourseParametersBean) getRenderedObject();

	final SortedSet<DegreeModuleScope> degreeModuleScopes = (SortedSet<DegreeModuleScope>) executeService(
		"ReadCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear", searchBean.getDegreeCurricularPlan()
			.getIdInternal(), searchBean.getExecutionYear().getIdInternal());

	if (degreeModuleScopes.isEmpty()) {
	    addActionMessage("message", request, "error.nonExisting.AssociatedCurricularCourses");
	} else {
	    request.setAttribute("degreeModuleScopes", degreeModuleScopes);
	}

	request.setAttribute("searchBean", searchBean);
	return mapping.findForward("chooseCurricularCourse");
    }

    public ActionForward searchByCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject
		.readDegreeModuleByOID(getIntegerFromRequest(request, "curricularCourseCode"));
	final Integer semester = getIntegerFromRequest(request, "semester");
	final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(getIntegerFromRequest(request,
		"executionYearID"));

	final List<Enrolment> enrolments = (List<Enrolment>) executeService("SearchStudentByCriteria", executionYear,
		curricularCourse, semester);

	request.setAttribute("semester", semester);
	request.setAttribute("year", getIntegerFromRequest(request, "year"));
	request.setAttribute("curricularYear", executionYear);
	request.setAttribute("enrolmentList", enrolments);
	request.setAttribute("searchBean", new SearchStudentsByCurricularCourseParametersBean(executionYear, curricularCourse));

	return mapping.findForward("studentByCurricularCourse");
    }

    public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject
		.readDegreeModuleByOID(getIntegerFromRequest(request, "curricularCourseCode"));
	final Integer semester = getIntegerFromRequest(request, "semester");
	final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName((String) getFromRequest(request,
		"curricularYear"));
	final String year = (String) getFromRequest(request, "year");

	final List<Enrolment> enrolments = (List<Enrolment>) executeService("SearchStudentByCriteria", executionYear,
		curricularCourse, semester);

	try {
	    String filename;

	    filename = curricularCourse.getDegreeCurricularPlan().getDegree().getName() + "_" + executionYear.getYear();

	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
	    ServletOutputStream writer = response.getOutputStream();

	    exportToXls(enrolments, writer, executionYear, curricularCourse.getDegreeCurricularPlan().getDegree(), year, semester
		    .toString());
	    writer.flush();
	    response.flushBuffer();

	} catch (IOException e) {
	    throw new FenixServiceException();
	}

	return null;

    }

    private void exportToXls(List<Enrolment> registrationWithStateForExecutionYearBean, OutputStream outputStream,
	    ExecutionYear executionYear, Degree degree, String year, String semester) throws IOException {

	final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("AlunosPorCurso");
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(degree.getName() + " - " + executionYear.getNextYearsYearString() + " - " + year + " Ano "
		+ semester + " Semestre");
	spreadsheet.newRow();
	spreadsheet.newRow();
	spreadsheet.addCell(registrationWithStateForExecutionYearBean.size() + " Alunos");
	fillSpreadSheet(registrationWithStateForExecutionYearBean, spreadsheet, executionYear);
	spreadsheet.getWorkbook().write(outputStream);
    }

    private void fillSpreadSheet(List<Enrolment> registrations, final StyledExcelSpreadsheet spreadsheet,
	    ExecutionYear executionYear) {
	setHeaders(spreadsheet);
	for (Enrolment registrationWithStateForExecutionYearBean : registrations) {
	    Registration registration = (Registration) registrationWithStateForExecutionYearBean.getRegistration();
	    spreadsheet.newRow();
	    spreadsheet.addCell(registration.getNumber().toString());
	    spreadsheet.addCell(registration.getPerson().getName());
	    spreadsheet.addCell(registration.getRegistrationAgreement().getName());
	    spreadsheet.addCell(registration.getDegree().getName());
	    spreadsheet.addCell(registrationWithStateForExecutionYearBean.getEnrollmentState().getDescription());
	    spreadsheet.addCell(registrationWithStateForExecutionYearBean.getEnrolmentEvaluationType().getDescription());
	}
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader("Numero");
	spreadsheet.addHeader("Nome");
	spreadsheet.addHeader("Acordo");
	spreadsheet.addHeader("Curso");
	spreadsheet.addHeader("Estado");
	spreadsheet.addHeader("Epoca");

    }

    public ActionForward downloadStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ExecutionYear executionYear = getExecutionYearParameter(request);
	final Set<Degree> degreesToInclude = getDegreesToInclude();

	final String filename = "statistics_" + executionYear.getName().replace('/', '-');
	final Spreadsheet spreadsheet = new Spreadsheet(filename);
	addStatisticsHeaders(spreadsheet);
	addStatisticsInformation(spreadsheet, executionYear, degreesToInclude);

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
	ServletOutputStream writer = response.getOutputStream();

	spreadsheet.exportToXLSSheet(writer);
	writer.flush();
	response.flushBuffer();

	return null;
    }


    private void addStatisticsHeaders(final Spreadsheet spreadsheet) {
	spreadsheet.setHeader("Sigla do Curso");
	spreadsheet.setHeader("Nome do Curso");
	spreadsheet.setHeader("Nome da Disciplina");
	spreadsheet.setHeader("Número de Inscritos");
    }

    private void addStatisticsInformation(final Spreadsheet spreadsheet, final ExecutionYear executionYear, final Set<Degree> degreesToInclude) {
	for (final DegreeCurricularPlan degreeCurricularPlan : executionYear.getDegreeCurricularPlans()) {
	    final Degree degree = degreeCurricularPlan.getDegree();
	    if (degreesToInclude == null || degreesToInclude.contains(degree)) {
		for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
		    if (curricularCourse.isActive(executionYear)) {
			final int enrolmentCount = countEnrolments(curricularCourse, executionYear);
			final Row row = spreadsheet.addRow();
			row.setCell(degree.getSigla());
			row.setCell(degree.getPresentationName());
			row.setCell(curricularCourse.getName());
			row.setCell(Integer.toString(enrolmentCount));
		    }
		}
	    }
	}
    }

    private int countEnrolments(final CurricularCourse curricularCourse, final ExecutionYear executionYear) {
	int c = 0;
	for (final CurriculumModule curriculumModule : curricularCourse.getCurriculumModulesSet()) {
	    if (curriculumModule.isEnrolment()) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
		if (executionYear == executionSemester.getExecutionYear()) {
		    c++;
		}
	    }
	}
	return c;
    }

    private Set<Degree> getDegreesToInclude() {
	final Person person = AccessControl.getPerson();
	return person == null || !person.isAdministrativeOfficeEmployee() ? null :
	    	person.getEmployee().getAdministrativeOffice().getAdministratedDegrees();
    }

    private ExecutionYear getExecutionYearParameter(final HttpServletRequest request) {
	final String executionYearIdString = request.getParameter("executionYearId");
	final Integer executionYearId = executionYearIdString != null && executionYearIdString.length() > 0 ?
		Integer.valueOf(executionYearIdString) :  null;
	return executionYearId == null ? null : rootDomainObject.readExecutionYearByOID(executionYearId);
    }

}
