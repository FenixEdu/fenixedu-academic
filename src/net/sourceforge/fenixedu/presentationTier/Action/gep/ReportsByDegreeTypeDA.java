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
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.student.Registration;
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

    private void outputReport(final HttpServletResponse httpServletResponse, final String reportName, final Spreadsheet spreadsheet, final String format)
    		throws IOException {
	httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + reportName.replace(' ', '_') + "." + format);
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

    private void reportEurAce(Spreadsheet spreadsheet, DegreeType degreeType, ExecutionYear executionYear) {
	spreadsheet.setHeader("nome curso");
	spreadsheet.setHeader("nome disciplina");
	spreadsheet.setHeader("número dos docentes");
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
					row.setCell(degree.getPresentationName());
					row.setCell(curricularCourse.getName());
					row.setCell(teacher.getTeacherNumber().toString());
					double credits = 0;
					for (final DegreeTeachingService degreeTeachingService : professorship.getDegreeTeachingServicesSet()) {
					    credits += degreeTeachingService.calculateCredits();
					}
					for (final TeacherMasterDegreeService teacherMasterDegreeService : professorship.getTeacherMasterDegreeServicesSet()) {
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
	spreadsheet.setHeader("nome curso");
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
				    row.setCell(degree.getPresentationName());
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
				    if (competenceCourse != null) {
					row.setCell(competenceCourse.getObjectives(executionSemester).replace('\t', ' ').replace('\n', ' ').replace('\r', ' '));
					row.setCell(competenceCourse.getProgram(executionSemester).replace('\t', ' ').replace('\n', ' ').replace('\r', ' '));
					final BibliographicReferences bibliographicReferences = competenceCourse.getBibliographicReferences(executionSemester);
					row.setCell(getBibliographicReferences(bibliographicReferences.getMainBibliographicReferences()).replace('\t', ' ').replace('\n', ' ').replace('\r', ' '));
					row.setCell(getBibliographicReferences(bibliographicReferences.getSecondaryBibliographicReferences()).replace('\t', ' ').replace('\n', ' ').replace('\r', ' '));

					row.setCell(competenceCourse.getEvaluationMethod(executionSemester).replace('\t', ' ').replace('\n', ' ').replace('\r', ' '));
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
				}
			    }
			}
		    }
		}
	    }
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

    private void reportStatusAndAproval(final Spreadsheet spreadsheet, final DegreeType degreeType, final ExecutionYear executionYear) {
	spreadsheet.setHeader("Numero aluno");
	spreadsheet.setHeader("Ano lectivo");
	spreadsheet.setHeader("Semestre");
	spreadsheet.setHeader("Tipo curso");
	spreadsheet.setHeader("Nome curso");
	spreadsheet.setHeader("Sigla curso");
	spreadsheet.setHeader("Estatuto");
	spreadsheet.setHeader("Numero inscricoes");
	spreadsheet.setHeader("Numero aprovacoes");

	final ExecutionSemester firstExecutionSemester = ReportsByDegreeTypeDA.getExecutionYearFourYearsBack(executionYear).getFirstExecutionPeriod();
	final ExecutionSemester lastExecutionSemester = executionYear.getLastExecutionPeriod();
	for (final Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degree.getDegreeType() == degreeType) {
		if (isActive(degree, executionYear)) {
		    for (final Registration registration : degree.getRegistrationsSet()) {
			if (registration.isInRegisteredState(executionYear)) {
			    final EnrolmentAndAprovalCounterMap map = new EnrolmentAndAprovalCounterMap(firstExecutionSemester, lastExecutionSemester, registration);
			    for (final Entry<ExecutionSemester, EnrolmentAndAprovalCounter> entry : map.entrySet()) {
				final ExecutionSemester executionSemester = entry.getKey();
				final EnrolmentAndAprovalCounter enrolmentAndAprovalCounter = entry.getValue();

				final Row row = spreadsheet.addRow();
				row.setCell(registration.getNumber().toString());
				row.setCell(executionSemester.getExecutionYear().getYear());
				row.setCell(executionSemester.getSemester().toString());
				row.setCell(degree.getDegreeType().getLocalizedName());
				row.setCell(degree.getPresentationName());
				row.setCell(degree.getSigla());
				final StringBuilder stringBuilder = new StringBuilder();
				for (final StudentStatuteBean studentStatuteBean : registration.getStudent().getStatutes(executionSemester)) {
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

}

