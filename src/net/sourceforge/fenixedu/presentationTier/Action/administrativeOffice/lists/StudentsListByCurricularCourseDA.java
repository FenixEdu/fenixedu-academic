/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

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
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

/**
 * @author - �ngela Almeida (argelina@ist.utl.pt)
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */

@Mapping(path = "/studentsListByCurricularCourse", module = StudentsListByCurricularCourseDA.MODULE)
@Forwards( { @Forward(name = "chooseCurricularCourse", path = "/academicAdminOffice/lists/chooseCurricularCourses.jsp"),
	@Forward(name = "studentByCurricularCourse", path = "/academicAdminOffice/lists/studentsByCurricularCourses.jsp") })
public class StudentsListByCurricularCourseDA extends FenixDispatchAction {

    protected static final String MODULE = "academicAdminOffice";

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
	SearchStudentsByCurricularCourseParametersBean bean = (SearchStudentsByCurricularCourseParametersBean) getRenderedObject("searchBean");
	return (bean != null) ? bean : new SearchStudentsByCurricularCourseParametersBean();
    }

    public ActionForward showActiveCurricularCourseScope(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

	final SearchStudentsByCurricularCourseParametersBean searchBean = (SearchStudentsByCurricularCourseParametersBean) getRenderedObject();

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
	request.setAttribute("searchBean", new SearchStudentsByCurricularCourseParametersBean(executionYear, curricularCourse));

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

	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject
		.readDegreeModuleByOID(getIntegerFromRequest(request, "curricularCourseCode"));
	final Integer semester = getIntegerFromRequest(request, "semester");
	final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName((String) getFromRequest(request,
		"curricularYear"));
	final String year = (String) getFromRequest(request, "year");

	try {
	    String filename;

	    filename = curricularCourse.getDegreeCurricularPlan().getDegree().getNameFor(executionYear).getContent().replace(' ',
		    '_')
		    + "_" + executionYear.getYear();

	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
	    ServletOutputStream writer = response.getOutputStream();

	    exportToXls(searchStudentByCriteria(executionYear, curricularCourse, semester), writer, executionYear,
		    curricularCourse.getDegreeCurricularPlan().getDegree(), year, semester.toString());
	    writer.flush();
	    response.flushBuffer();

	} catch (IOException e) {
	    throw new FenixServiceException();
	}

	return null;

    }

    private void exportToXls(List<Enrolment> registrationWithStateForExecutionYearBean, OutputStream outputStream,
	    ExecutionYear executionYear, Degree degree, String year, String semester) throws IOException {

	final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(
		getResourceMessage("lists.studentByCourse.unspaced"));
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(degree.getNameFor(executionYear) + " - " + executionYear.getYear() + " - " + year + " "
		+ getResourceMessage("label.year") + " " + semester + " " + getResourceMessage("label.semester"));
	spreadsheet.newRow();
	spreadsheet.newRow();
	spreadsheet.addCell(registrationWithStateForExecutionYearBean.size() + " " + getResourceMessage("label.students"));
	fillSpreadSheet(registrationWithStateForExecutionYearBean, spreadsheet, executionYear);
	spreadsheet.getWorkbook().write(outputStream);
    }

    private void fillSpreadSheet(List<Enrolment> registrations, final StyledExcelSpreadsheet spreadsheet,
	    ExecutionYear executionYear) {
	setHeaders(spreadsheet);
	for (Enrolment registrationWithStateForExecutionYearBean : registrations) {
	    Registration registration = registrationWithStateForExecutionYearBean.getRegistration();
	    spreadsheet.newRow();
	    spreadsheet.addCell(registration.getNumber().toString());
	    spreadsheet.addCell(registration.getPerson().getName());
	    spreadsheet.addCell(registration.getRegistrationAgreement().getName());
	    Degree degree = registration.getDegree();
	    spreadsheet.addCell(!(StringUtils.isEmpty(degree.getSigla())) ? degree.getSigla() : degree.getNameFor(executionYear)
		    .toString());
	    spreadsheet.addCell(getEnumNameFromResources(registrationWithStateForExecutionYearBean.getEnrollmentState()));
	    spreadsheet.addCell(registrationWithStateForExecutionYearBean.getEnrolmentEvaluationType().getDescription());
	}
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(getResourceMessage("label.student.number"));
	spreadsheet.addHeader(getResourceMessage("label.name"));
	spreadsheet.addHeader(getResourceMessage("label.registrationAgreement"));
	spreadsheet.addHeader(getResourceMessage("label.degree"));
	spreadsheet.addHeader(getResourceMessage("label.state"));
	spreadsheet.addHeader(getResourceMessage("label.epoch"));
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
	return getResourceMessageFromModuleOrApplication(MODULE, key);
    }

    static private String getEnumNameFromResources(Enum<?> enumeration) {
	return getResourceMessageFromModule("Enumeration", enumeration.getClass().getSimpleName() + "." + enumeration.name());
    }

}
