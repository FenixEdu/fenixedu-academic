package net.sourceforge.fenixedu.domain.reports;

import pt.ist.fenixWebFramework.services.Service;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.util.StringUtils;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

public class CourseLoadReportFile extends CourseLoadReportFile_Base {
    
    public  CourseLoadReportFile() {
        super();
    }

    public String getJobName(){
	return "Listagem de tipos de aula e carga horária";
    }
    
    public String getDescription() {
	return getJobName() + " no formato " + getType().toUpperCase();
    }
    
    protected String getPrefix() {
	return "carga_horaria";
    }
    
    
    @Service
    public static GepReportFile newInstance(String type, DegreeType degreeType, ExecutionYear executionYear) {
	CourseLoadReportFile courseLoadReportFile = new CourseLoadReportFile();
	courseLoadReportFile.setType(type);
	courseLoadReportFile.setDegreeType(degreeType);
	courseLoadReportFile.setExecutionYear(executionYear);
	return courseLoadReportFile;
    }
    
    public void renderReport(Spreadsheet spreadsheet) {

	spreadsheet.setHeader("semestre");
	spreadsheet.setHeader("id execution course");
	spreadsheet.setHeader("id turno");
	spreadsheet.setHeader("nome turno");
	spreadsheet.setHeader("tipo aula");
	spreadsheet.setHeader("horas aula");
	spreadsheet.setHeader("total turnos");

	for (ExecutionSemester executionSemester : getExecutionYear().getExecutionPeriods()) {
	    for (ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCourses()) {

		int shiftsCount = executionCourse.getAssociatedShifts().size();

		for (CourseLoad courseLoad : executionCourse.getCourseLoads()) {

		    for (Shift shift : courseLoad.getShifts()) {

			if (!shift.hasSchoolClassForDegreeType(getDegreeType())) {
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
