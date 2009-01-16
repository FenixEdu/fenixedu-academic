package net.sourceforge.fenixedu.domain.reports;

import java.io.ByteArrayOutputStream;

import javax.activation.MimetypesFileTypeMap;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherMasterDegreeService;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;
import pt.ist.fenixWebFramework.services.Service;

public class EurAceQueueJobFile extends EurAceQueueJobFile_Base {

    public EurAceQueueJobFile() {
	super();
    }

    public String getFilename() {
	return getReportName().replace(' ', '_') + "." + getType();
    }

    public String getDescription() {
	return "Listagem para EUR-ACE no formato " + getType().toUpperCase();
    }

    @Service
    public static void newInstance(String type, DegreeType degreeType, ExecutionYear executionYear, String reportName) {
	EurAceQueueJobFile eurAceQueueJobFile = new EurAceQueueJobFile();
	eurAceQueueJobFile.setType(type);
	eurAceQueueJobFile.setDegreeType(degreeType);
	eurAceQueueJobFile.setExecutionYear(executionYear);
	eurAceQueueJobFile.setReportName(reportName);
    }

    private static void setDegreeHeaders(final Spreadsheet spreadsheet) {
	spreadsheet.setHeader("tipo curso");
	spreadsheet.setHeader("nome curso");
	spreadsheet.setHeader("sigla curso");
    }

    private static void setDegreeCells(final Row row, final Degree degree) {
	row.setCell(degree.getDegreeType().getLocalizedName());
	row.setCell(degree.getNameI18N().getContent());
	row.setCell(degree.getSigla());
    }

    private static boolean checkDegreeType(final DegreeType degreeType, final Degree degree) {
	return degreeType == null || degree.getDegreeType() == degreeType;
    }

    private static boolean checkExecutionYear(final ExecutionYear executionYear, final DegreeCurricularPlan degreeCurricularPlan) {
	return executionYear == null || degreeCurricularPlan.hasExecutionDegreeFor(executionYear);
    }

    private static boolean checkExecutionYear(ExecutionYear executionYear, final CurricularCourse curricularCourse) {
	return executionYear == null || curricularCourse.isActive(executionYear);
    }

    public void execute() throws Exception {
	final Spreadsheet spreadsheet = new Spreadsheet(this.getReportName());
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("nome disciplina");
	spreadsheet.setHeader("n�mero do docente");
	spreadsheet.setHeader("cr�ditos");

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(this.getDegreeType(), degree)) {
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (checkExecutionYear(getExecutionYear(), degreeCurricularPlan)) {
			for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
			    if (checkExecutionYear(getExecutionYear(), curricularCourse)) {
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

	ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

	if ("csv".compareTo(getType()) == 0) {
	    spreadsheet.exportToCSV(byteArrayOS, "\t");
	} else {
	    spreadsheet.exportToXLSSheet(byteArrayOS);
	}
	setContent(new ByteArray(byteArrayOS.toByteArray()));
	MimetypesFileTypeMap mft = new MimetypesFileTypeMap();
	setContentType(mft.getContentType(getFilename()));

	System.out.println("Job " + getFilename() + " completed");

    }

}