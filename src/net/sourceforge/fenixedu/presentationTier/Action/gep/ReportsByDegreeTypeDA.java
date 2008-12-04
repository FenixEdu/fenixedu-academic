package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.StudentStatuteBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
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
import net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcess;
import net.sourceforge.fenixedu.domain.student.curriculum.CycleConclusionProcess;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherMasterDegreeService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.HtmlToTextConverterUtil;
import net.sourceforge.fenixedu.util.StringUtils;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;

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

	public Integer getExecutionYearOID() {
	    return getExecutionYear() == null ? null : getExecutionYear().getIdInternal();
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

    @SuppressWarnings("unused")
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
	return StringUtils.isEmpty(degreeTypeString) ? null : DegreeType.valueOf(degreeTypeString);
    }

    private ExecutionYear getExecutionYear(final HttpServletRequest httpServletRequest) {
	final String OIDString = httpServletRequest.getParameter("executionYearID");
	return StringUtils.isEmpty(OIDString) ? null : rootDomainObject.readExecutionYearByOID(Integer.valueOf(OIDString));
    }

    private String getFormat(final HttpServletRequest httpServletRequest) {
	return httpServletRequest.getParameter("format");
    }

    private String getReportName(final String prefix, final DegreeType degreeType, final ExecutionYear executionYear) {

	final StringBuilder result = new StringBuilder();
	result.append(new LocalDateTime().toString("yyyy_MM_dd_HH_mm")).append("_");
	result.append(prefix).append("_");
	result.append(degreeType == null ? "Todos_Cursos" : degreeType.getLocalizedName()).append("_");
	result.append(executionYear == null ? "Todos_Anos" : executionYear.getName().replace('/', '_'));

	return result.toString();
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

    @SuppressWarnings("unused")
    public ActionForward downloadEurAce(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("eurAce", degreeType, executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportEurAce(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    @SuppressWarnings("unused")
    public ActionForward downloadEctsLabelForCurricularCourses(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {

	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("ectsLabel_Disciplinas", degreeType, executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportEctsLabelForCurricularCourses(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    @SuppressWarnings("unused")
    public ActionForward downloadEctsLabelForDegrees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("ectsLabel_Cursos", degreeType, executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportEctsLabelForDegrees(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    @SuppressWarnings("unused")
    public ActionForward downloadStatusAndAproval(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("statusAndAproval", degreeType, executionYear);

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

    private void setDegreeCells(final Row row, final Degree degree) {
	row.setCell(degree.getDegreeType().getLocalizedName());
	row.setCell(degree.getNameI18N().getContent());
	row.setCell(degree.getSigla());
    }

    @SuppressWarnings("unused")
    public ActionForward downloadEti(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("etiGrades", degreeType, executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportEti(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    @SuppressWarnings("unused")
    public ActionForward downloadRegistrations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("matriculas", degreeType, executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportRegistrations(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    @SuppressWarnings("unused")
    public ActionForward downloadFlunked(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("prescricoes", degreeType, executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportFlunked(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    @SuppressWarnings("unused")
    public ActionForward downloadGraduations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("diplomados", degreeType, executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportGraduations(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    @SuppressWarnings("unused")
    public ActionForward downloadTeachersByShift(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("teachersByShift", degreeType, executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportTeachersByShift(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    @SuppressWarnings("unused")
    public ActionForward downloadCourseLoads(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	final DegreeType degreeType = getDegreeType(request);
	final ExecutionYear executionYear = getExecutionYear(request);
	final String format = getFormat(request);
	final String reportName = getReportName("teachersByShift", degreeType, executionYear);

	final Spreadsheet spreadsheet = new Spreadsheet(reportName);
	reportCourseLoads(spreadsheet, degreeType, executionYear);

	outputReport(response, reportName, spreadsheet, format);
	return null;
    }

    private boolean checkDegreeType(final DegreeType degreeType, final Degree degree) {
	return degreeType == null || degree.getDegreeType() == degreeType;
    }

    private boolean checkExecutionYear(final ExecutionYear executionYear, final DegreeCurricularPlan degreeCurricularPlan) {
	return executionYear == null || degreeCurricularPlan.hasExecutionDegreeFor(executionYear);
    }

    private boolean checkExecutionYear(ExecutionYear executionYear, final CurricularCourse curricularCourse) {
	return executionYear == null || curricularCourse.isActive(executionYear);
    }

    private void reportEurAce(Spreadsheet spreadsheet, DegreeType degreeType, ExecutionYear executionYear) {
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("nome disciplina");
	spreadsheet.setHeader("número do docente");
	spreadsheet.setHeader("créditos");

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(degreeType, degree)) {
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (checkExecutionYear(executionYear, degreeCurricularPlan)) {
			for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
			    if (checkExecutionYear(executionYear, curricularCourse)) {
				for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
				    for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
					final Teacher teacher = professorship.getTeacher();
					final Row row = spreadsheet.addRow();
					setDegreeCells(row, degree);
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

    private void reportEctsLabelForDegrees(final Spreadsheet spreadsheet, final DegreeType degreeType,
	    final ExecutionYear executionYear) {

	createEctsLabelDegreesHeader(spreadsheet);

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(degreeType, degree)) {
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (checkExecutionYear(executionYear, degreeCurricularPlan)) {
			addEctsLabelDegreeRow(spreadsheet, degreeCurricularPlan, executionYear);
		    }
		}
	    }
	}
    }

    private void createEctsLabelDegreesHeader(final Spreadsheet spreadsheet) {
	spreadsheet.setHeaders(new String[] {

	"Nome",

	"Nome Inglês",

	"Tipo Curso",

	"Duração em anos",

	"Duração em Semanas de Estudo",

	"Créditos ECTS",

	"Requisitos de Ingresso",

	"Requisitos de Ingresso (inglês)",

	"Objectivos Educacionais",

	"Objectivos Educacionais (inglês)",

	"Acesso a um nível superior de estudos",

	"Acesso a um nível superior de estudos (inglês)",

	"Normas e Regulamentos",

	"Normas e Regulamentos (inglês)",

	"Coordenador",

	"Contactos",

	"Contactos (inglês)"

	});
    }

    private void addEctsLabelDegreeRow(final Spreadsheet spreadsheet, final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionYear executionYear) {

	final Row row = spreadsheet.addRow();
	final Degree degree = degreeCurricularPlan.getDegree();

	row.setCell(normalize(degree.getNameFor(executionYear).getContent(Language.pt)));
	row.setCell(normalize(degree.getNameFor(executionYear).getContent(Language.en)));

	row.setCell(degree.getDegreeType().getLocalizedName());
	row.setCell(degree.getDegreeType().getYears());
	row.setCell(degree.getDegreeType().getYears() * 40);
	row.setCell(degree.getEctsCredits());

	final DegreeInfo degreeInfo = degree.getMostRecentDegreeInfo(executionYear);
	if (degreeInfo != null) {
	    row.setCell(normalize(degreeInfo.getDesignedFor(Language.pt)));
	    row.setCell(normalize(degreeInfo.getDesignedFor(Language.en)));
	    row.setCell(normalize(degreeInfo.getObjectives(Language.pt)));
	    row.setCell(normalize(degreeInfo.getObjectives(Language.en)));
	    row.setCell(normalize(degreeInfo.getProfessionalExits(Language.pt)));
	    row.setCell(normalize(degreeInfo.getProfessionalExits(Language.en)));
	    row.setCell(normalize(degreeInfo.getOperationalRegime(Language.pt)));
	    row.setCell(normalize(degreeInfo.getOperationalRegime(Language.en)));
	    row.setCell(normalize(getResponsibleCoordinatorNames(degreeCurricularPlan, executionYear)));
	    row.setCell(normalize(degreeInfo.getAdditionalInfo(Language.pt)));
	    row.setCell(normalize(degreeInfo.getAdditionalInfo(Language.en)));
	}
    }

    private String getResponsibleCoordinatorNames(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionYear executionYear) {
	final StringBuilder builder = new StringBuilder();
	final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
	final Iterator<Coordinator> coordinators = executionDegree.getResponsibleCoordinators().iterator();
	while (coordinators.hasNext()) {
	    builder.append(coordinators.next().getPerson().getName()).append(coordinators.hasNext() ? ", " : "");
	}
	return builder.toString();
    }

    private void reportEctsLabelForCurricularCourses(final Spreadsheet spreadsheet, final DegreeType degreeType,
	    final ExecutionYear executionYear) {

	createEctsLabelCurricularCoursesHeader(spreadsheet);

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(degreeType, degree)) {
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (checkExecutionYear(executionYear, degreeCurricularPlan)) {
			for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
			    if (checkExecutionYear(executionYear, curricularCourse)
				    && !curricularCourse.isOptionalCurricularCourse()) {
				for (final Context context : curricularCourse.getParentContextsByExecutionYear(executionYear)) {
				    addEctsLabelContextRow(spreadsheet, context, executionYear);
				}
			    }
			}
		    }
		}
	    }
	}
    }

    private void createEctsLabelCurricularCoursesHeader(final Spreadsheet spreadsheet) {
	spreadsheet.setHeader("Tipo Curso");
	spreadsheet.setHeader("Nome Curso");
	spreadsheet.setHeader("Sigla Curso");
	spreadsheet.setHeader("Nome Disciplina");
	spreadsheet.setHeader("Nome Disciplina (inglês)");
	spreadsheet.setHeader("Código Disciplina");
	spreadsheet.setHeader("Ano curricular");
	spreadsheet.setHeader("Semestre");
	spreadsheet.setHeader("Duração");
	spreadsheet.setHeader("Tipo");
	spreadsheet.setHeader("Créditos ECTS");
	spreadsheet.setHeader("Idioma");
	spreadsheet.setHeader("Docentes");
	spreadsheet.setHeader("Horas de contacto");
	spreadsheet.setHeader("Objectivos");
	spreadsheet.setHeader("Objectivos (inglês)");
	spreadsheet.setHeader("Programa");
	spreadsheet.setHeader("Programa (inglês)");
	spreadsheet.setHeader("Bibliografia Principal");
	spreadsheet.setHeader("Bibliografia Secundária (inglês)");
	spreadsheet.setHeader("Avaliação");
	spreadsheet.setHeader("Avaliação (inglês)");
	spreadsheet.setHeader("Estimativa total de trabalho");
    }

    private void addEctsLabelContextRow(final Spreadsheet spreadsheet, final Context context, final ExecutionYear executionYear) {

	final Row row = spreadsheet.addRow();
	final ExecutionSemester executionSemester = getExecutionSemester(context, executionYear);
	final CurricularCourse curricular = (CurricularCourse) context.getChildDegreeModule();

	row.setCell(curricular.getDegree().getDegreeType().getLocalizedName());
	row.setCell(curricular.getDegree().getNameFor(executionSemester).getContent());
	row.setCell(curricular.getDegree().getSigla());
	row.setCell(curricular.getName(executionSemester));
	row.setCell(curricular.getNameEn(executionSemester));
	final CompetenceCourse competenceCourse = curricular.getCompetenceCourse();
	if (competenceCourse != null) {
	    row.setCell(competenceCourse.getAcronym(executionSemester));
	} else {
	    row.setCell("");
	}

	row.setCell(context.getCurricularYear());
	setSemesterAndDuration(row, context);
	row.setCell(curricular.hasCompetenceCourseLevel() ? curricular.getCompetenceCourseLevel().getLocalizedName() : "");
	row.setCell(curricular.getEctsCredits(executionSemester));

	row.setCell(getLanguage(curricular));
	row.setCell(getTeachers(curricular, executionSemester));
	row.setCell(curricular.getContactLoad(context.getCurricularPeriod(), executionSemester));

	row.setCell(normalize(curricular.getObjectives(executionSemester)));
	row.setCell(normalize(curricular.getObjectivesEn(executionSemester)));
	row.setCell(normalize(curricular.getProgram(executionSemester)));
	row.setCell(normalize(curricular.getProgramEn(executionSemester)));

	final BibliographicReferences references = getBibliographicReferences(curricular, executionSemester);
	if (references == null) {
	    row.setCell(" ");
	    row.setCell(" ");
	} else {
	    row.setCell(normalize(getBibliographicReferences(references.getMainBibliographicReferences())));
	    row.setCell(normalize(getBibliographicReferences(references.getSecondaryBibliographicReferences())));
	}

	row.setCell(normalize(curricular.getEvaluationMethod(executionSemester)));
	row.setCell(normalize(curricular.getEvaluationMethodEn(executionSemester)));

	row.setCell(curricular.getTotalLoad(context.getCurricularPeriod(), executionSemester));
    }

    private String getLanguage(final CurricularCourse curricularCourse) {
	final DegreeType degreeType = curricularCourse.getDegreeType();
	if (degreeType.hasExactlyOneCycleType() && degreeType.getCycleType() == CycleType.FIRST_CYCLE) {
	    return "Português";
	} else {
	    return "Português/Inglês";
	}
    }

    private String getTeachers(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	final StringBuilder builder = new StringBuilder();
	for (final ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester)) {
	    for (final Professorship professorship : executionCourse.getProfessorshipsSortedAlphabetically()) {
		builder.append(professorship.getTeacher().getPerson().getName()).append("; ");
	    }
	}
	return builder.toString();
    }

    private BibliographicReferences getBibliographicReferences(final CurricularCourse curricularCourse,
	    final ExecutionSemester executionSemester) {
	final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
	return competenceCourse == null ? null : competenceCourse.getBibliographicReferences(executionSemester);
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

    private ExecutionSemester getExecutionSemester(final Context context, final ExecutionYear executionYear) {
	final CurricularPeriod curricularPeriod = context.getCurricularPeriod();
	if (curricularPeriod.getAcademicPeriod().getName().equals("SEMESTER")) {
	    return (curricularPeriod.getChildOrder().intValue() == 1) ? executionYear.getFirstExecutionPeriod() : executionYear
		    .getLastExecutionPeriod();
	} else {
	    return executionYear.getFirstExecutionPeriod();
	}
    }

    private void setSemesterAndDuration(final Row row, final Context context) {
	final CurricularPeriod curricularPeriod = context.getCurricularPeriod();
	if (curricularPeriod.getAcademicPeriod().getName().equals("SEMESTER")) {
	    row.setCell(curricularPeriod.getChildOrder());
	    row.setCell("Semestral");
	} else {
	    row.setCell(" ");
	    row.setCell("Anual");
	}
    }

    private String normalize(final String text) {
	if (text == null || text.isEmpty()) {
	    return "";
	}
	return HtmlToTextConverterUtil.convertToText(text).replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
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
	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(degreeType, degree)) {
		if (isActive(degree, executionYear)) {
		    for (final Registration registration : degree.getRegistrationsSet()) {
			if (registration.isRegistered(executionYear)) {
			    final EnrolmentAndAprovalCounterMap map = new EnrolmentAndAprovalCounterMap(firstExecutionSemester,
				    lastExecutionSemester, registration);
			    for (final Entry<ExecutionSemester, EnrolmentAndAprovalCounter> entry : map.entrySet()) {
				final ExecutionSemester executionSemester = entry.getKey();
				final EnrolmentAndAprovalCounter enrolmentAndAprovalCounter = entry.getValue();

				final Row row = spreadsheet.addRow();
				row.setCell(registration.getNumber().toString());
				row.setCell(executionSemester.getExecutionYear().getYear());
				row.setCell(executionSemester.getSemester().toString());
				setDegreeCells(row, degree);
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
	    if (checkExecutionYear(executionYear, degreeCurricularPlan)) {
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
	spreadsheet.setHeader("creditos");
	spreadsheet.setHeader("estado");
	spreadsheet.setHeader("época");
	spreadsheet.setHeader("nota");
	spreadsheet.setHeader("época normal");
	spreadsheet.setHeader("época especial");
	spreadsheet.setHeader("melhoria");
	spreadsheet.setHeader("tipo Aluno");
	spreadsheet.setHeader("número inscricoes anteriores");
	spreadsheet.setHeader("executionCourseId");
	spreadsheet.setHeader("disponível para inquérito");

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(degreeType, degree)) {
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (checkExecutionYear(executionYear, degreeCurricularPlan)) {
			for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
			    if (checkExecutionYear(executionYear, curricularCourse)) {

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
	setDegreeCells(row, registration.getDegree());
	row.setCell(executionSemester.getSemester().toString());
	row.setCell(executionSemester.getExecutionYear().getYear());
	row.setCell(curricularCourse.getName());
	setDegreeCells(row, degree);
	row.setCell(enrolment.getEctsCredits().toString().replace('.', ','));
	row.setCell(enrolment.getEnrollmentState().getDescription());
	row.setCell(enrolment.getEnrolmentEvaluationType().getDescription());
	row.setCell(enrolment.getGradeValue());

	final EnrolmentEvaluation normal = enrolment.getLatestNormalEnrolmentEvaluation();
	row.setCell(normal == null ? "" : normal.getGradeValue());
	final EnrolmentEvaluation special = enrolment.getLatestSpecialSeasonEnrolmentEvaluation();
	row.setCell(special == null ? "" : special.getGradeValue());
	final EnrolmentEvaluation improvement = enrolment.getLatestImprovementEnrolmentEvaluation();
	row.setCell(improvement == null ? "" : improvement.getGradeValue());

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

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(degreeType, degree)) {
		if (isActive(degree, executionYear)) {
		    for (final Registration registration : degree.getRegistrationsSet()) {
			if (registration.isRegistered(executionYear)) {
			    final Row row = spreadsheet.addRow();
			    row.setCell(registration.getNumber());
			    setDegreeCells(row, degree);

			    reportIngression(row, registration);

			    final Registration firstRegistration = findFirstRegistration(registration.getStudent());
			    reportIngression(row, firstRegistration);

			    if (registration.getRegistrationAgreement() != null) {
				row.setCell(registration.getRegistrationAgreement().getName());
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

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(degreeType, degree)) {
		for (final Registration registration : degree.getRegistrationsSet()) {
		    for (final RegistrationState registrationState : registration.getRegistrationStates()) {
			final RegistrationStateType registrationStateType = registrationState.getStateType();
			if (registrationStateType == RegistrationStateType.FLUNKED
				&& registrationState.getExecutionYear() == executionYear) {
			    final Row row = spreadsheet.addRow();
			    row.setCell(registration.getNumber());
			    setDegreeCells(row, degree);
			}
		    }
		}
	    }
	}
    }

    private void reportGraduations(final Spreadsheet spreadsheet, final DegreeType degreeType, final ExecutionYear executionYear) {
	spreadsheet.setHeader("número aluno");
	spreadsheet.setHeader("nome");
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("ciclo");
	spreadsheet.setHeader("ano de ingresso");
	spreadsheet.setHeader("ano lectivo conclusão");
	spreadsheet.setHeader("data conclusão");
	spreadsheet.setHeader("número de anos para conclusão");
	spreadsheet.setHeader("média final");
	spreadsheet.setHeader("morada");
	spreadsheet.setHeader("código postal");
	spreadsheet.setHeader("telefone");
	spreadsheet.setHeader("telemovel");
	spreadsheet.setHeader("email");
	spreadsheet.setHeader("sexo");

	final Set<ExecutionYear> toInspectSet = executionYear == null ? rootDomainObject.getExecutionYearsSet() : Collections
		.singleton(executionYear);

	for (final ExecutionYear toInspect : toInspectSet) {
	    for (final ConclusionProcess conclusionProcess : toInspect.getConclusionProcessesConcludedSet()) {
		if (checkDegreeType(degreeType, conclusionProcess.getDegree())) {
		    reportGraduate(spreadsheet, conclusionProcess);
		}
	    }
	}
    }

    private void reportGraduate(final Spreadsheet sheet, final ConclusionProcess conclusionProcess) {
	final Row row = sheet.addRow();

	final Registration registration = conclusionProcess.getRegistration();
	final ExecutionYear ingression = conclusionProcess.getIngressionYear();
	final ExecutionYear conclusion = conclusionProcess.getConclusionYear();
	final LocalDate conclusionDate = conclusionProcess.getConclusionDate();

	row.setCell(registration.getNumber());
	row.setCell(registration.getName());
	setDegreeCells(row, registration.getDegree());
	if (conclusionProcess.isCycleConclusionProcess()) {
	    row.setCell(((CycleConclusionProcess) conclusionProcess).getCycleType().getDescription());
	} else {
	    row.setCell(StringUtils.EMPTY);
	}
	row.setCell(ingression.getYear());
	row.setCell(conclusion == null ? StringUtils.EMPTY : conclusion.getYear());
	row.setCell(conclusionDate == null ? StringUtils.EMPTY : conclusionDate.toString("yyyy-MM-dd"));
	row.setCell(conclusion == null ? StringUtils.EMPTY : String.valueOf(ingression.getDistanceInCivilYears(conclusion) + 1));
	row.setCell(conclusionProcess.getFinalAverage());

	setPersonCells(registration, row);
    }

    private void setPersonCells(final Registration registration, final Row row) {
	final Person person = registration.getPerson();
	row.setCell(person.getAddress());
	row.setCell(person.getPostalCode());
	row.setCell(person.getPhone());
	row.setCell(person.getMobile());
	row.setCell(person.getInstitutionalOrDefaultEmailAddressValue());
	row.setCell(person.getGender().toLocalizedString());
    }

    private void reportTeachersByShift(Spreadsheet spreadsheet, DegreeType degreeType, ExecutionYear executionYear) {

	spreadsheet.setHeader("semestre");
	spreadsheet.setHeader("nº docente");
	spreadsheet.setHeader("id turno");
	spreadsheet.setHeader("nome turno");
	spreadsheet.setHeader("id execution course");
	spreadsheet.setHeader("% assegurada pelo docente");

	for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    for (TeacherService teacherService : executionSemester.getTeacherServices()) {
		for (DegreeTeachingService degreeTeachingService : teacherService.getDegreeTeachingServices()) {

		    final Shift shift = degreeTeachingService.getShift();

		    if (!shift.hasSchoolClassForDegreeType(degreeType)) {
			continue;
		    }

		    Row row = spreadsheet.addRow();
		    row.setCell(executionSemester.getSemester());
		    row.setCell(teacherService.getTeacher().getTeacherNumber());
		    row.setCell(shift.getIdInternal());
		    row.setCell(shift.getNome());
		    row.setCell(shift.getExecutionCourse().getIdInternal());
		    row.setCell(degreeTeachingService.getPercentage() != null ? degreeTeachingService.getPercentage().toString()
			    .replace('.', ',') : StringUtils.EMPTY);

		}
	    }
	}
    }

    private void reportCourseLoads(Spreadsheet spreadsheet, DegreeType degreeType, ExecutionYear executionYear) {

	spreadsheet.setHeader("semestre");
	spreadsheet.setHeader("id execution course");
	spreadsheet.setHeader("id turno");
	spreadsheet.setHeader("nome turno");
	spreadsheet.setHeader("tipo aula");
	spreadsheet.setHeader("horas aula");
	spreadsheet.setHeader("total turnos");

	for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    for (ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCourses()) {

		int shiftsCount = executionCourse.getAssociatedShifts().size();

		for (CourseLoad courseLoad : executionCourse.getCourseLoads()) {

		    for (Shift shift : courseLoad.getShifts()) {

			if (!shift.hasSchoolClassForDegreeType(degreeType)) {
			    continue;
			}

			Row row = spreadsheet.addRow();
			row.setCell(executionSemester.getSemester());
			row.setCell(executionCourse.getIdInternal());
			row.setCell(shift.getIdInternal());
			row.setCell(shift.getNome());
			row.setCell(courseLoad.getType().name());
			row.setCell(courseLoad.getTotalQuantity() != null ? courseLoad.getTotalQuantity().toPlainString()
				.replace('.', ',') : StringUtils.EMPTY);
			row.setCell(shiftsCount);

		    }
		}
	    }
	}

    }

}
