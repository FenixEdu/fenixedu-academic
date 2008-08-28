package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.StudentStatuteBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherMasterDegreeService;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicSemesters;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ReportsByDegreeTypeDA extends FenixDispatchAction {

    public static class ReportBean implements Serializable {
	private DegreeType degreeType;
	private DomainReference<ExecutionYear> executionYearReference;

	public DegreeType getDegreeType() {
	    return degreeType;
	}

	public void setDegreeType(DegreeType degreeType) {
	    this.degreeType = degreeType;
	}

	public ExecutionYear getExecutionYear() {
	    return executionYearReference == null ? null : executionYearReference.getObject();
	}

	public void setExecutionYear(final ExecutionYear executionYear) {
	    executionYearReference = executionYear == null ? null : new DomainReference<ExecutionYear>(executionYear);
	}

	public ExecutionYear getExecutionYearFourYearsBack() {
	    final ExecutionYear executionYear = getExecutionYear();
	    return executionYear == null ? null : ReportsByDegreeTypeDA.getExecutionYearFourYearsBack(executionYear);
	}
    }

    public static ExecutionYear getExecutionYearFourYearsBack(final ExecutionYear executionYear) {
	ExecutionYear executionYearFourYearsBack = executionYear;
	if (executionYear != null) {
	    for (int i = 5; i > 1; i--) {
		final ExecutionYear previousExecutionYear = executionYearFourYearsBack.getPreviousExecutionYear();
		if (previousExecutionYear != null) {
		    executionYearFourYearsBack = previousExecutionYear;
		}
	    }
	}
	return executionYearFourYearsBack;
    }

    public ActionForward selectDegreeType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	ReportBean reportBean = (ReportBean) getRenderedObject();
	if (reportBean == null) {
	    reportBean = new ReportBean();
	}
	RenderUtils.invalidateViewState();
	request.setAttribute("reportBean", reportBean);
	return mapping.findForward("selectDegreeType");
    }

    private DegreeType getDegreeType(final HttpServletRequest httpServletRequest) {
	final String degreeTypeString = httpServletRequest.getParameter("degreeType");
	return DegreeType.valueOf(degreeTypeString);
    }

    private ExecutionYear getExecutionYear(final HttpServletRequest httpServletRequest) {
	final String executionYearIdString = httpServletRequest.getParameter("executionYearID");
	return rootDomainObject.readExecutionYearByOID(Integer.valueOf(executionYearIdString));
    }

    private String getFormat(final HttpServletRequest httpServletRequest) {
	return httpServletRequest.getParameter("format");
    }

    private String getReportName(final String prefix, final String degreeTypeName, final ExecutionYear executionYear) {
	return prefix + degreeTypeName + executionYear.getYear().replace('/', '_');
    }

    private void outputReport(final HttpServletResponse httpServletResponse, final String reportName,
	    final Spreadsheet spreadsheet, final String format) throws IOException {
	httpServletResponse
		.setHeader("Content-disposition", "attachment;filename=" + reportName.replace(' ', '_') + "." + format);
	httpServletResponse.setContentType("application/txt");
	final OutputStream outputStream = httpServletResponse.getOutputStream();
	try {
	    if (format.equalsIgnoreCase("xls")) {
		spreadsheet.exportToXLSSheet(outputStream);
	    } else {
		spreadsheet.exportToCSV(outputStream, "\t");
	    }
	} finally {
	    outputStream.close();
	}
    }

    public ActionForward downloadEurAce(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("eurAce", degreeType.getLocalizedName(), executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportEurAce(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    public ActionForward downloadEctsLabel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("ectsLabel", degreeType.getLocalizedName(), executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportEctsLabel(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    public ActionForward downloadStatusAndAproval(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("statusAndAproval", degreeType.getLocalizedName(), executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportStatusAndAproval(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    private void setDegreeHeaders(final Spreadsheet spreadsheet) {
	spreadsheet.setHeader("tipo curso");
	spreadsheet.setHeader("nome curso");
	spreadsheet.setHeader("sigla curso");
    }

    private void setDegreeHeaders(final Spreadsheet spreadsheet, final String suffix) {
	spreadsheet.setHeader("tipo curso " + suffix);
	spreadsheet.setHeader("nome curso " + suffix);
	spreadsheet.setHeader("sigla curso " + suffix);
    }

    private void setDegreeColumns(final Row row, final Degree degree) {
	row.setCell(degree.getDegreeType().getLocalizedName());
	row.setCell(degree.getName());
	row.setCell(degree.getSigla());
    }

    public ActionForward downloadEti(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("etiGrades", degreeType.getLocalizedName(), executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportEti(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    public ActionForward downloadRegistrations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("matriculas", degreeType.getLocalizedName(), executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportRegistrations(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    public ActionForward downloadFlunked(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("prescricoes", degreeType.getLocalizedName(), executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportFlunked(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    public ActionForward downloadGraduations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("diplomados", degreeType.getLocalizedName(), executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportGraduations(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    private void reportEurAce(Spreadsheet spreadsheet, DegreeType degreeType, ExecutionYear executionYear) {
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("nome disciplina");
	spreadsheet.setHeader("número do docente");
	spreadsheet.setHeader("créditos");

	for (final Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degree.getDegreeType() == degreeType) {
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (degreeCurricularPlan.hasExecutionDegreeFor(executionYear)) {
			for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
			    if (curricularCourse.isActive(executionYear)) {
				for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
				    for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
					final Teacher teacher = professorship.getTeacher();
					final Row row = spreadsheet.addRow();
					setDegreeColumns(row, degree);
					row.setCell(curricularCourse.getName());
					row.setCell(teacher.getTeacherNumber().toString());
					double credits = 0;
					for (final DegreeTeachingService degreeTeachingService : professorship
						.getDegreeTeachingServicesSet()) {
					    credits += degreeTeachingService.calculateCredits();
					}
					for (final TeacherMasterDegreeService teacherMasterDegreeService : professorship
						.getTeacherMasterDegreeServicesSet()) {
					    final Double d = teacherMasterDegreeService.getCredits();
					    if (d != null) {
						credits += d.doubleValue();
					    }
					}
					row.setCell(Double.toString(Math.round((credits * 100.0)) / 100.0));
				    }
				}
			    }
			}
		    }
		}
	    }
	}
    }

    private String getBibliographicReferences(final List<BibliographicReference> references) {
	Collections.sort(references);
	final StringBuilder stringBuilder = new StringBuilder();
	for (final BibliographicReference bibliographicReference : references) {
	    if (stringBuilder.length() > 0) {
		stringBuilder.append("; ");
	    }
	    stringBuilder.append(bibliographicReference.getTitle());
	    stringBuilder.append(", ");
	    stringBuilder.append(bibliographicReference.getAuthors());
	    stringBuilder.append(", ");
	    stringBuilder.append(bibliographicReference.getYear());
	    stringBuilder.append(", ");
	    stringBuilder.append(bibliographicReference.getReference());
	}
	return stringBuilder.toString();
    }

    private void reportEctsLabel(final Spreadsheet spreadsheet, final DegreeType degreeType, final ExecutionYear executionYear) {
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("nome disciplina");
	spreadsheet.setHeader("ano curricular");
	spreadsheet.setHeader("semestre");
	spreadsheet.setHeader("objectivos");
	spreadsheet.setHeader("programa");
	spreadsheet.setHeader("bibliografia principal");
	spreadsheet.setHeader("bibliografia secundária");
	spreadsheet.setHeader("método de avaliação");
	spreadsheet.setHeader("créditos ECTS");
	spreadsheet.setHeader("duração");
	spreadsheet.setHeader("carga teórica");
	spreadsheet.setHeader("carga problemas");
	spreadsheet.setHeader("carga laboratorial");
	spreadsheet.setHeader("carga serminários");
	spreadsheet.setHeader("carga trabalho de campo");
	spreadsheet.setHeader("carga estágio");
	spreadsheet.setHeader("carga orientação tutorial");
	spreadsheet.setHeader("carga trabalho autónomo");
	spreadsheet.setHeader("código interno da disciplina execução");
	spreadsheet.setHeader("carga teórica disciplina execução");
	spreadsheet.setHeader("carga problemas disciplina execução");
	spreadsheet.setHeader("carga laboratorial disciplina execução");
	spreadsheet.setHeader("carga serminários disciplina execução");
	spreadsheet.setHeader("carga trabalho de campo disciplina execução");
	spreadsheet.setHeader("carga estágio disciplina execução");
	spreadsheet.setHeader("carga orientação tutorial disciplina execução");

	for (final Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degree.getDegreeType() == degreeType) {
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (degreeCurricularPlan.hasExecutionDegreeFor(executionYear)) {
			for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
			    if (curricularCourse.isActive(executionYear)) {
				for (final Context context : curricularCourse.getParentContextsByExecutionYear(executionYear)) {
				    final CurricularPeriod curricularPeriod = context.getCurricularPeriod();
				    final AcademicPeriod academicPeriod = curricularPeriod.getAcademicPeriod();
				    final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();

				    final Row row = spreadsheet.addRow();
				    setDegreeColumns(row, degree);
				    row.setCell(curricularCourse.getName());
				    row.setCell(context.getCurricularYear());
				    final ExecutionSemester executionSemester;
				    final Integer order;
				    final String duration;
				    if (academicPeriod.getName().equals("SEMESTER")) {
					final AcademicSemesters academicSemesters = (AcademicSemesters) academicPeriod;
					order = curricularPeriod.getChildOrder();
					row.setCell(order.toString());
					if (curricularPeriod.getChildOrder().intValue() == 1) {
					    executionSemester = executionYear.getFirstExecutionPeriod();
					} else {
					    executionSemester = executionYear.getLastExecutionPeriod();
					}
					duration = "Semestral";
				    } else {
					order = null;
					row.setCell(" ");
					executionSemester = executionYear.getFirstExecutionPeriod();
					duration = "Anual";
				    }

				    final List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);

				    if (competenceCourse != null) {
					row.setCell(competenceCourse.getObjectives(executionSemester).replace('\t', ' ').replace(
						'\n', ' ').replace('\r', ' '));
					row.setCell(competenceCourse.getProgram(executionSemester).replace('\t', ' ').replace(
						'\n', ' ').replace('\r', ' '));
					final BibliographicReferences bibliographicReferences = competenceCourse
						.getBibliographicReferences(executionSemester);
					row.setCell(getBibliographicReferences(
						bibliographicReferences.getMainBibliographicReferences()).replace('\t', ' ')
						.replace('\n', ' ').replace('\r', ' '));
					row.setCell(getBibliographicReferences(
						bibliographicReferences.getSecondaryBibliographicReferences()).replace('\t', ' ')
						.replace('\n', ' ').replace('\r', ' '));

					row.setCell(competenceCourse.getEvaluationMethod(executionSemester).replace('\t', ' ')
						.replace('\n', ' ').replace('\r', ' '));
					row.setCell(competenceCourse.getEctsCredits(order, executionYear));
					row.setCell(duration);

					row.setCell(competenceCourse.getTheoreticalHours(order, executionSemester));
					row.setCell(competenceCourse.getProblemsHours(order, executionSemester));
					row.setCell(competenceCourse.getLaboratorialHours(order, executionSemester));
					row.setCell(competenceCourse.getSeminaryHours(order, executionSemester));
					row.setCell(competenceCourse.getFieldWorkHours(order, executionSemester));
					row.setCell(competenceCourse.getTrainingPeriodHours(order, executionSemester));
					row.setCell(competenceCourse.getTutorialOrientationHours(order, executionSemester));
					row.setCell(competenceCourse.getAutonomousWorkHours(order, executionSemester));
				    } else {
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
				    }

				    if (executionCourses.isEmpty()) {
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
					row.setCell(" ");
				    } else {
					final ExecutionCourse executionCourse = executionCourses.iterator().next();
					row.setCell(executionCourse.getIdInternal());
					setCourseLoad(row, executionCourse.getCourseLoadByShiftType(ShiftType.TEORICA));
					setCourseLoad(row, executionCourse.getCourseLoadByShiftType(ShiftType.PROBLEMS));
					setCourseLoad(row, executionCourse.getCourseLoadByShiftType(ShiftType.LABORATORIAL));
					setCourseLoad(row, executionCourse.getCourseLoadByShiftType(ShiftType.SEMINARY));
					setCourseLoad(row, executionCourse.getCourseLoadByShiftType(ShiftType.FIELD_WORK));
					setCourseLoad(row, executionCourse.getCourseLoadByShiftType(ShiftType.TRAINING_PERIOD));
					setCourseLoad(row, executionCourse.getCourseLoadByShiftType(ShiftType.TUTORIAL_ORIENTATION));
				    }
				}
			    }
			}
		    }
		}
	    }
	}
    }

    private void setCourseLoad(final Row row, final CourseLoad courseLoad) {
	if (courseLoad == null) {
	    row.setCell("0.0");
	} else {
	    row.setCell(courseLoad.getWeeklyHours().toString());
	}
    }

    public static class EnrolmentAndAprovalCounter {
	private int enrolments = 0;
	private int aprovals = 0;

	public void count(final Enrolment enrolment) {
	    enrolments++;
	    if (enrolment.isApproved()) {
		aprovals++;
	    }
	}

	public int getEnrolments() {
	    return enrolments;
	}

	public int getAprovals() {
	    return aprovals;
	}
    }

    public static class EnrolmentAndAprovalCounterMap extends HashMap<ExecutionSemester, EnrolmentAndAprovalCounter> {

	private final ExecutionSemester firstExecutionSemester;
	private final ExecutionSemester lastExecutionSemester;

	public EnrolmentAndAprovalCounterMap(final ExecutionSemester firstExecutionSemester,
		final ExecutionSemester lastExecutionSemester) {
	    this.firstExecutionSemester = firstExecutionSemester;
	    this.lastExecutionSemester = lastExecutionSemester;
	}

	public EnrolmentAndAprovalCounterMap(final ExecutionSemester firstExecutionSemester,
		final ExecutionSemester lastExecutionSemester, final Registration registration) {
	    this(firstExecutionSemester, lastExecutionSemester);
	    for (final Registration otherRegistration : registration.getStudent().getRegistrationsSet()) {
		if (otherRegistration.getDegree() == registration.getDegree()) {
		    for (final StudentCurricularPlan studentCurricularPlan : otherRegistration.getStudentCurricularPlansSet()) {
			for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
			    count(enrolment);
			}
		    }
		}
	    }
	}

	public void count(final Enrolment enrolment) {
	    final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
	    if (firstExecutionSemester.isBeforeOrEquals(executionSemester)
		    && executionSemester.isBeforeOrEquals(lastExecutionSemester)) {
		final EnrolmentAndAprovalCounter enrolmentAndAprovalCounter = get(executionSemester);
		enrolmentAndAprovalCounter.count(enrolment);
	    }
	}

	@Override
	public EnrolmentAndAprovalCounter get(final Object key) {
	    EnrolmentAndAprovalCounter enrolmentAndAprovalCounter = super.get(key);
	    if (enrolmentAndAprovalCounter == null) {
		enrolmentAndAprovalCounter = new EnrolmentAndAprovalCounter();
		put((ExecutionSemester) key, enrolmentAndAprovalCounter);
	    }
	    return enrolmentAndAprovalCounter;
	}

    }

    private void reportStatusAndAproval(final Spreadsheet spreadsheet, final DegreeType degreeType,
	    final ExecutionYear executionYear) {
	spreadsheet.setHeader("número aluno");
	spreadsheet.setHeader("ano lectivo");
	spreadsheet.setHeader("semestre");
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("estatuto");
	spreadsheet.setHeader("número inscricoes");
	spreadsheet.setHeader("núumero aprovacoes");

	final ExecutionSemester firstExecutionSemester = ReportsByDegreeTypeDA.getExecutionYearFourYearsBack(executionYear)
		.getFirstExecutionPeriod();
	final ExecutionSemester lastExecutionSemester = executionYear.getLastExecutionPeriod();
	for (final Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degree.getDegreeType() == degreeType) {
		if (isActive(degree, executionYear)) {
		    for (final Registration registration : degree.getRegistrationsSet()) {
			if (registration.isInRegisteredState(executionYear)) {
			    final EnrolmentAndAprovalCounterMap map = new EnrolmentAndAprovalCounterMap(firstExecutionSemester,
				    lastExecutionSemester, registration);
			    for (final Entry<ExecutionSemester, EnrolmentAndAprovalCounter> entry : map.entrySet()) {
				final ExecutionSemester executionSemester = entry.getKey();
				final EnrolmentAndAprovalCounter enrolmentAndAprovalCounter = entry.getValue();

				final Row row = spreadsheet.addRow();
				row.setCell(registration.getNumber().toString());
				row.setCell(executionSemester.getExecutionYear().getYear());
				row.setCell(executionSemester.getSemester().toString());
				setDegreeColumns(row, degree);
				final StringBuilder stringBuilder = new StringBuilder();
				for (final StudentStatuteBean studentStatuteBean : registration.getStudent().getStatutes(
					executionSemester)) {
				    if (stringBuilder.length() > 0) {
					stringBuilder.append(", ");
				    }
				    stringBuilder.append(studentStatuteBean.getStudentStatute().getStatuteType());
				}
				row.setCell(stringBuilder.toString());
				row.setCell(Integer.toString(enrolmentAndAprovalCounter.getEnrolments()));
				row.setCell(Integer.toString(enrolmentAndAprovalCounter.getAprovals()));
			    }
			}
		    }
		}
	    }
	}
    }

    private boolean isActive(final Degree degree, final ExecutionYear executionYear) {
	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
	    if (degreeCurricularPlan.hasExecutionDegreeFor(executionYear)) {
		return true;
	    }
	}
	return false;
    }

    private void reportEti(final Spreadsheet spreadsheet, final DegreeType degreeType, final ExecutionYear executionYear) {
	spreadsheet.setHeader("número Aluno");
	setDegreeHeaders(spreadsheet, "aluno");
	spreadsheet.setHeader("semestre");
	spreadsheet.setHeader("ano lectivo");
	spreadsheet.setHeader("nome Disciplina");
	setDegreeHeaders(spreadsheet, "disciplina");
	spreadsheet.setHeader("nota");
	spreadsheet.setHeader("creditos");
	spreadsheet.setHeader("estado");
	spreadsheet.setHeader("época");
	spreadsheet.setHeader("tipo Aluno");
	spreadsheet.setHeader("número inscricoes anteriores");
	spreadsheet.setHeader("executionCourseId");
	spreadsheet.setHeader("disponível para inquérito");

	for (final Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degree.getDegreeType() == degreeType) {
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (degreeCurricularPlan.hasExecutionDegreeFor(executionYear)) {
			for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
			    if (curricularCourse.isActive(executionYear)) {

				for (final CurriculumModule curriculumModule : curricularCourse.getCurriculumModulesSet()) {
				    if (curriculumModule.isEnrolment()) {
					final Enrolment enrolment = (Enrolment) curriculumModule;
					if (enrolment.getExecutionYear() == executionYear) {
					    final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
					    if (curricularCourse.isAnual()) {
						addEtiRow(spreadsheet, degree, curricularCourse, enrolment, executionSemester,
							executionSemester);
						if (executionSemester.getSemester().intValue() == 1) {
						    final ExecutionSemester nextSemester = executionSemester
							    .getNextExecutionPeriod();
						    addEtiRow(spreadsheet, degree, curricularCourse, enrolment, nextSemester,
							    executionSemester);
						}
					    } else {
						addEtiRow(spreadsheet, degree, curricularCourse, enrolment, executionSemester,
							executionSemester);
					    }

					}
				    }
				}
			    }
			}
		    }
		}
	    }
	}
    }

    private void addEtiRow(final Spreadsheet spreadsheet, final Degree degree, final CurricularCourse curricularCourse,
	    final Enrolment enrolment, final ExecutionSemester executionSemester,
	    final ExecutionSemester executionSemesterForPreviousEnrolmentCount) {
	final StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();
	final Registration registration = studentCurricularPlan.getRegistration();
	final Student student = registration.getStudent();

	final Row row = spreadsheet.addRow();
	row.setCell(student.getNumber().toString());
	setDegreeColumns(row, registration.getDegree());
	row.setCell(executionSemester.getSemester().toString());
	row.setCell(executionSemester.getExecutionYear().getYear());
	row.setCell(curricularCourse.getName());
	setDegreeColumns(row, degree);
	row.setCell(enrolment.getGradeValue());
	row.setCell(enrolment.getEctsCredits().toString().replace('.', ','));
	row.setCell(enrolment.getEnrollmentState().getDescription());
	row.setCell(enrolment.getEnrolmentEvaluationType().getDescription());
	row.setCell(registration.getRegistrationAgreement().getName());
	row.setCell(countPreviousEnrolments(curricularCourse, executionSemesterForPreviousEnrolmentCount, student));
	final Attends attends = enrolment.getAttendsFor(executionSemester);
	if (attends == null) {
	    row.setCell("");
	} else {
	    final ExecutionCourse executionCourse = attends.getExecutionCourse();
	    row.setCell(executionCourse.getIdInternal());
	    if (executionCourse.getAvailableForInquiries() != null && executionCourse.getAvailableForInquiries().booleanValue()) {
		row.setCell("sim");
	    } else {
		row.setCell("não");
	    }
	}
    }

    private String countPreviousEnrolments(final CurricularCourse curricularCourse, final ExecutionSemester executionPeriod,
	    final Student student) {
	int count = 0;
	for (final CurriculumModule curriculumModule : curricularCourse.getCurriculumModulesSet()) {
	    if (curriculumModule.isEnrolment()) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		if (executionPeriod.compareTo(enrolment.getExecutionPeriod()) > 0) {
		    if (enrolment.getStudentCurricularPlan().getRegistration().getStudent() == student) {
			count++;
		    }
		}
	    }
	}
	return Integer.toString(count);
    }

    private void reportRegistrations(Spreadsheet spreadsheet, DegreeType degreeType, ExecutionYear executionYear) {
	spreadsheet.setHeader("número aluno");
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("código regime de ingresso na matrícula");
	spreadsheet.setHeader("regime de ingresso na matrícula");
	spreadsheet.setHeader("ano léctivo de início da matrícula");
	spreadsheet.setHeader("código regime de ingresso na escola");
	spreadsheet.setHeader("regime de ingresso na escola");
	spreadsheet.setHeader("ano léctivo de ingresso na escola");
	spreadsheet.setHeader("tipo de aluno");
	spreadsheet.setHeader("ano curricular");

	for (final Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degree.getDegreeType() == degreeType) {
		if (isActive(degree, executionYear)) {
		    for (final Registration registration : degree.getRegistrationsSet()) {
			if (registration.isInRegisteredState(executionYear)) {
			    final Row row = spreadsheet.addRow();
			    row.setCell(registration.getNumber());
			    setDegreeColumns(row, degree);

			    reportIngression(row, registration);

			    final Registration firstRegistration = findFirstRegistration(registration.getStudent());
			    reportIngression(row, firstRegistration);

			    if (registration.getRegistrationAgreement() != null) {
				row.setCell(registration.getRegistrationAgreement().getName()); // TODO
												// :
												// "tipo de aluno"
												// :
												// check
												// this
			    } else {
				row.setCell("");
			    }
			    row.setCell(Integer.toString(registration.getCurricularYear(executionYear)));
			}
		    }
		}
	    }
	}
    }

    private Registration findFirstRegistration(final Student student) {
	return Collections.min(student.getRegistrationsSet(), Registration.COMPARATOR_BY_START_DATE);
    }

    private void reportIngression(final Row row, final Registration registration) {
	final Ingression ingression = registration.getIngression();
	if (ingression != null) {
	    row.setCell(ingression.getName());
	    row.setCell(ingression.getDescription());
	} else {
	    row.setCell("");
	    row.setCell("");
	}
	row.setCell(registration.getStartExecutionYear().getYear());
    }

    private void reportFlunked(Spreadsheet spreadsheet, DegreeType degreeType, ExecutionYear executionYear) {
	spreadsheet.setHeader("número aluno");
	setDegreeHeaders(spreadsheet);

	for (final Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degree.getDegreeType() == degreeType) {
		for (final Registration registration : degree.getRegistrationsSet()) {
		    for (final RegistrationState registrationState : registration.getRegistrationStates()) {
			final RegistrationStateType registrationStateType = registrationState.getStateType();
			if (registrationStateType == RegistrationStateType.FLUNKED
				&& registrationState.getExecutionYear() == executionYear) {
			    final Row row = spreadsheet.addRow();
			    row.setCell(registration.getNumber());
			    setDegreeColumns(row, degree);
			}
		    }
		}
	    }
	}
    }

    private void reportGraduations(Spreadsheet spreadsheet, DegreeType degreeType, ExecutionYear executionYear) {
	spreadsheet.setHeader("número aluno");
	spreadsheet.setHeader("nome");
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("ano de ingresso");
	spreadsheet.setHeader("ano léctivo conclusão");
	spreadsheet.setHeader("data conclusão");
	spreadsheet.setHeader("número de anos para conclusão");
	spreadsheet.setHeader("média final");
	spreadsheet.setHeader("morada");
	spreadsheet.setHeader("código postal");
	spreadsheet.setHeader("telefone");
	spreadsheet.setHeader("telemovel");
	spreadsheet.setHeader("email");
	spreadsheet.setHeader("sexo");

	final CycleType cycleType = degreeType.getLastCycleType();

	for (final Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degree.getDegreeType() == degreeType) {
		if (isActive(degree, executionYear)) {
		    for (final Registration registration : degree.getRegistrationsSet()) {
			final RegistrationState registrationState = registration.getLastRegistrationState(executionYear);
			if (registrationState != null && registrationState.getStateType() == RegistrationStateType.CONCLUDED
				&& registration.isRegistrationConclusionProcessed()
				&& !registration.getStudentCurricularPlansSet().isEmpty()
				&& (!degreeType.isBolonhaType() || registration.hasConcludedCycle(cycleType))) {
			    final ExecutionYear conclusionExecutionYear;
			    final YearMonthDay conclusionDate;
			    if (degreeType.isBolonhaType()) {
				conclusionExecutionYear = registration.getConclusionYear();
				conclusionDate = registration.getConclusionDate(cycleType);
			    } else {
				conclusionDate = registration.getConclusionDate();
				conclusionExecutionYear = conclusionDate == null ? null : ExecutionYear
					.getExecutionYearByDate(conclusionDate);
			    }
			    if (conclusionExecutionYear == null || conclusionExecutionYear == executionYear) {
				final Person person = registration.getPerson();

				final Row row = spreadsheet.addRow();
				row.setCell(registration.getNumber());
				row.setCell(person.getName());
				setDegreeColumns(row, degree);
				row.setCell(registration.getStartExecutionYear().getYear());
				if (conclusionExecutionYear != null) {
				    row.setCell(conclusionExecutionYear.getYear());
				} else {
				    row.setCell("");
				}
				if (conclusionDate != null) {
				    row.setCell(conclusionDate.toString("yyyy-MM-dd"));
				} else {
				    row.setCell("");
				}
				row.setCell(Integer.valueOf(registration.getSortedEnrolmentsExecutionYears().size()));
				row.setCell(registration.getFinalAverage());
				row.setCell(person.getAddress());
				row.setCell(person.getPostalCode());
				row.setCell(person.getPhone());
				row.setCell(person.getMobile());
				row.setCell(person.getEmail());
				row.setCell(person.getGender().toLocalizedString());
			    }
			}
		    }
		}
	    }
	}
    }

}
