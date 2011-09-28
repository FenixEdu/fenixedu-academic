/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.lists;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

/**
 * @author - �ngela Almeida (argelina@ist.utl.pt)
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */

public abstract class StudentsListByCurricularCourseDA extends FenixDispatchAction {

    protected static final String RESOURCE_MODULE = "academicAdminOffice";

    public ActionForward prepareByCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {

	request.setAttribute("searchBean", getOrCreateSearchBean());
	return mapping.findForward("chooseCurricularCourse");
    }

    public ActionForward chooseExecutionYearPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	SearchStudentsByCurricularCourseParametersBean searchBean = getOrCreateSearchBean();
	RenderUtils.invalidateViewState();
	request.setAttribute("searchBean", searchBean);

	return mapping.findForward("chooseCurricularCourse");
    }

    private SearchStudentsByCurricularCourseParametersBean getOrCreateSearchBean() {
	SearchStudentsByCurricularCourseParametersBean bean = getRenderedObject("searchBean");
	if (bean == null) {
	    bean = new SearchStudentsByCurricularCourseParametersBean();
	    bean.setAdministratedDegrees(getAdministratedDegrees());
	}
	return bean;
    }

    public ActionForward showActiveCurricularCourseScope(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

	final SearchStudentsByCurricularCourseParametersBean searchBean = getOrCreateSearchBean();

	final SortedSet<DegreeModuleScope> degreeModuleScopes = new TreeSet<DegreeModuleScope>(
		DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME_AND_BRANCH);
	degreeModuleScopes.addAll(searchBean.getDegreeCurricularPlan().getDegreeModuleScopesFor(searchBean.getExecutionYear()));

	if (degreeModuleScopes.isEmpty()) {
	    addActionMessage("message", request, "error.nonExisting.AssociatedCurricularCourses");
	} else {
	    request.setAttribute("degreeModuleScopes", degreeModuleScopes);
	}

	request.setAttribute("searchBean", searchBean);
	return mapping.findForward("chooseCurricularCourse");
    }

    public ActionForward searchByCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException {

	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject
		.readDegreeModuleByOID(getIntegerFromRequest(request, "curricularCourseCode"));
	final Integer semester = getIntegerFromRequest(request, "semester");
	final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(getIntegerFromRequest(request,
		"executionYearID"));

	request.setAttribute("semester", semester);
	request.setAttribute("year", getIntegerFromRequest(request, "year"));
	request.setAttribute("curricularYear", executionYear);
	request.setAttribute("enrolmentList", searchStudentByCriteria(executionYear, curricularCourse, semester));
	SearchStudentsByCurricularCourseParametersBean bean = getOrCreateSearchBean();
	bean.setExecutionYear(executionYear);
	bean.setCurricularCourse(curricularCourse);
	bean.setDegreeCurricularPlan(curricularCourse.getDegreeCurricularPlan());
	request.setAttribute("searchBean", bean);

	return mapping.findForward("studentByCurricularCourse");
    }

    private List<Enrolment> searchStudentByCriteria(final ExecutionYear executionYear, final CurricularCourse curricularCourse,
	    final Integer semester) {
	final List<Enrolment> result = new ArrayList<Enrolment>();

	final ExecutionSemester executionSemester = executionYear.getExecutionSemesterFor(semester);
	for (final Enrolment enrolment : curricularCourse.getEnrolmentsByExecutionPeriod(executionSemester)) {
	    result.add(enrolment);
	}
	Collections.sort(result, new BeanComparator("studentCurricularPlan.registration.number"));

	return result;
    }

    public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	return doExportInfoToExcel(mapping, actionForm, request, response, false);
    }

    public ActionForward exportDetailedInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	return doExportInfoToExcel(mapping, actionForm, request, response, true);
    }

    public ActionForward doExportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, Boolean detailed) throws FenixServiceException, FenixFilterException {

	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject
		.readDegreeModuleByOID(getIntegerFromRequest(request, "curricularCourseCode"));
	final Integer semester = getIntegerFromRequest(request, "semester");
	final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName((String) getFromRequest(request,
		"curricularYear"));
	final String year = (String) getFromRequest(request, "year");

	try {
	    String filename = getResourceMessage("label.students") + "_" + curricularCourse.getName() + "_("
		    + curricularCourse.getDegreeCurricularPlan().getName() + ")_" + executionYear.getYear();

	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-disposition", "attachment; filename=" + filename.replace(" ", "_") + ".xls");
	    ServletOutputStream writer = response.getOutputStream();

	    exportToXls(searchStudentByCriteria(executionYear, curricularCourse, semester), writer, executionYear,
		    curricularCourse, year, semester.toString(), detailed);
	    writer.flush();
	    response.flushBuffer();

	} catch (IOException e) {
	    throw new FenixServiceException();
	}

	return null;

    }

    private void exportToXls(List<Enrolment> registrations, OutputStream outputStream, ExecutionYear executionYear,
	    CurricularCourse curricularCourse, String year, String semester, Boolean detailed) throws IOException {

	final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(
		getResourceMessage("lists.studentByCourse.unspaced"));
	fillSpreadSheetFilters(executionYear, curricularCourse, year, semester, spreadsheet);
	fillSpreadSheetResults(registrations, spreadsheet, executionYear, detailed);
	spreadsheet.getWorkbook().write(outputStream);
    }

    private void fillSpreadSheetFilters(ExecutionYear executionYear, CurricularCourse curricularCourse, String year,
	    String semester, final StyledExcelSpreadsheet spreadsheet) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(curricularCourse.getDegree().getNameFor(executionYear) + " - " + curricularCourse.getName() + " - "
		+ executionYear.getYear() + " - " + year + " " + getResourceMessage("label.year") + " " + semester + " "
		+ getResourceMessage("label.semester"));
    }

    private void fillSpreadSheetResults(List<Enrolment> registrations, final StyledExcelSpreadsheet spreadsheet,
	    ExecutionYear executionYear, Boolean detailed) {
	spreadsheet.newRow();
	spreadsheet.newRow();
	spreadsheet.addCell(registrations.size() + " " + getResourceMessage("label.students"));

	setHeaders(spreadsheet, detailed);
	for (Enrolment registrationWithStateForExecutionYearBean : registrations) {
	    Registration registration = registrationWithStateForExecutionYearBean.getRegistration();
	    spreadsheet.newRow();
	    spreadsheet.addCell(registration.getNumber().toString());
	    spreadsheet.addCell(registration.getPerson().getName());
	    spreadsheet.addCell(registration.getRegistrationAgreement().getName());
	    Degree degree = registration.getDegree();
	    spreadsheet.addCell(!(StringUtils.isEmpty(degree.getSigla())) ? degree.getSigla() : degree.getNameFor(executionYear)
		    .toString());
	    spreadsheet.addCell(BundleUtil.getEnumName(registrationWithStateForExecutionYearBean
		    .getEnrollmentState()));
	    spreadsheet.addCell(registrationWithStateForExecutionYearBean.getEnrolmentEvaluationType().getDescription());
	    if (detailed) {
		spreadsheet.addCell(registration.getPerson().hasDefaultEmailAddress() ? registration.getPerson()
			.getDefaultEmailAddressValue() : "-");
		spreadsheet.addCell(registration.getPerson().hasInstitutionalEmailAddress() ? registration.getPerson()
			.getInstitutionalEmailAddressValue() : "-");

	    }

	}
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet, Boolean detailed) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(getResourceMessage("label.student.number"));
	spreadsheet.addHeader(getResourceMessage("label.name"));
	spreadsheet.addHeader(getResourceMessage("label.registrationAgreement"));
	spreadsheet.addHeader(getResourceMessage("label.degree"));
	spreadsheet.addHeader(getResourceMessage("label.state"));
	spreadsheet.addHeader(getResourceMessage("label.epoch"));
	if (detailed) {
	    spreadsheet.addHeader(getResourceMessage("label.email"));
	    spreadsheet.addHeader(getResourceMessage("label.institutional.email"));
	}
    }

    public ActionForward downloadStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ExecutionYear executionYear = getExecutionYearParameter(request);
	final Set<Degree> degreesToInclude = getDegreesToInclude();

	final String filename = getResourceMessage("label.statistics") + "_" + executionYear.getName().replace('/', '-');
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
	spreadsheet.setHeader(getResourceMessage("label.degree.acronym"));
	spreadsheet.setHeader(getResourceMessage("label.degree.name"));
	spreadsheet.setHeader(getResourceMessage("label.curricularCourse.name"));
	spreadsheet.setHeader(getResourceMessage("label.degree.numberOfEnrolments"));
    }

    private void addStatisticsInformation(final Spreadsheet spreadsheet, final ExecutionYear executionYear,
	    final Set<Degree> degreesToInclude) {
	for (final DegreeCurricularPlan degreeCurricularPlan : executionYear.getDegreeCurricularPlans()) {
	    final Degree degree = degreeCurricularPlan.getDegree();
	    if (degreesToInclude == null || degreesToInclude.contains(degree)) {
		for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
		    if (curricularCourse.isActive(executionYear)) {
			final int enrolmentCount = countEnrolments(curricularCourse, executionYear);
			final Row row = spreadsheet.addRow();
			row.setCell(degree.getSigla());
			row.setCell(degree.getPresentationName(executionYear));
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
	return person == null || !person.isAdministrativeOfficeEmployee() ? null : person.getEmployee().getAdministrativeOffice()
		.getAdministratedDegrees();
    }

    private ExecutionYear getExecutionYearParameter(final HttpServletRequest request) {
	final String executionYearIdString = request.getParameter("executionYearId");
	final Integer executionYearId = executionYearIdString != null && executionYearIdString.length() > 0 ? Integer
		.valueOf(executionYearIdString) : null;
	return executionYearId == null ? null : rootDomainObject.readExecutionYearByOID(executionYearId);
    }

    static private String getResourceMessage(String key) {
	return BundleUtil.getMessageFromModuleOrApplication(RESOURCE_MODULE, key);
    }

    protected abstract Set<Degree> getAdministratedDegrees();
}
