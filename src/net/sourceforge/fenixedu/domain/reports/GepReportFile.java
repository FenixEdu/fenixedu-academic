package net.sourceforge.fenixedu.domain.reports;

import java.io.ByteArrayOutputStream;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcess;
import net.sourceforge.fenixedu.util.HtmlToTextConverterUtil;
import net.sourceforge.fenixedu.util.StringUtils;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

public abstract class GepReportFile extends GepReportFile_Base {

    public GepReportFile() {
	super();
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

    public String getDescription() {
	return " no formato " + getType().toUpperCase();
    }

    public abstract String getJobName();

    protected abstract String getPrefix();

    public String getFilename() {
	return getReportName().replace(' ', '_') + "." + getType();
    }

    private String getReportName() {

	final StringBuilder result = new StringBuilder();
	result.append(getRequestDate().toString("yyyy_MM_dd_HH_mm")).append("_");
	result.append(getPrefix()).append("_");
	result.append(getDegreeType() == null ? "Todos_Cursos" : getDegreeType().name()).append("_");
	result.append(getExecutionYear() == null ? "Todos_Anos" : getExecutionYear().getName().replace('/', '_'));

	return result.toString();
    }

    protected void setDegreeHeaders(final Spreadsheet spreadsheet) {
	spreadsheet.setHeader("tipo curso");
	spreadsheet.setHeader("nome curso");
	spreadsheet.setHeader("sigla curso");
    }

    protected void setDegreeHeaders(final Spreadsheet spreadsheet, final String suffix) {
	spreadsheet.setHeader("tipo curso " + suffix);
	spreadsheet.setHeader("nome curso " + suffix);
	spreadsheet.setHeader("sigla curso " + suffix);
    }

    protected void setDegreeCells(final Row row, final Degree degree) {
	row.setCell(degree.getDegreeType().getLocalizedName());
	row.setCell(degree.getNameI18N().getContent());
	row.setCell(degree.getSigla());
    }

    protected boolean checkDegreeType(final DegreeType degreeType, final ConclusionProcess conclusionProcess) {
	return degreeType == null || conclusionProcess.getDegree().getDegreeType() == degreeType;
    }

    protected static boolean checkDegreeType(final DegreeType degreeType, final Degree degree) {
	return degreeType == null || degree.getDegreeType() == degreeType;
    }

    protected static boolean checkExecutionYear(final ExecutionYear executionYear, final DegreeCurricularPlan degreeCurricularPlan) {
	return executionYear == null || degreeCurricularPlan.hasExecutionDegreeFor(executionYear);
    }

    protected static boolean checkExecutionYear(ExecutionYear executionYear, final CurricularCourse curricularCourse) {
	return executionYear == null || curricularCourse.isActive(executionYear);
    }

    protected boolean isActive(final Degree degree) {
	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
	    if (checkExecutionYear(getExecutionYear(), degreeCurricularPlan)) {
		return true;
	    }
	}
	return false;
    }

    protected String normalize(final String text) {
	if (!StringUtils.isEmpty(text)) {
	    String result = "";
	    try {
		result = HtmlToTextConverterUtil.convertToText(text);
	    } catch (Exception ex) {
		result = HtmlToTextConverterUtil.convertToTextWithRegEx(text);
	    }
	    return result.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
	}
	return "";
    }

    public abstract void renderReport(Spreadsheet spreadsheet) throws Exception;

    public QueueJobResult execute() throws Exception {
	final Spreadsheet spreadsheet = new Spreadsheet(getReportName());

	this.renderReport(spreadsheet);

	ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

	if ("csv".compareTo(getType()) == 0) {
	    spreadsheet.exportToCSV(byteArrayOS, "\t");
	} else {
	    spreadsheet.exportToXLSSheet(byteArrayOS);
	}

	final QueueJobResult queueJobResult = new QueueJobResult();
	queueJobResult.setContentType("application/txt");
	queueJobResult.setContent(byteArrayOS.toByteArray());

	System.out.println("Job " + getFilename() + " completed");

	return queueJobResult;
    }

    public String getUpperCaseType() {
	return this.getType().toUpperCase();
    }
}
