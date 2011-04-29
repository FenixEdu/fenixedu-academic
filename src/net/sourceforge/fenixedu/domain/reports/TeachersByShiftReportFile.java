package net.sourceforge.fenixedu.domain.reports;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class TeachersByShiftReportFile extends TeachersByShiftReportFile_Base {

    public TeachersByShiftReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Listagem de docentes associados a turnos";
    }

    @Override
    protected String getPrefix() {
	return "teachersByShift";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) {

	spreadsheet.setHeader("semestre");
	spreadsheet.setHeader("id docente");
	spreadsheet.setHeader("id turno");
	spreadsheet.setHeader("nome turno");
	spreadsheet.setHeader("id execution course");
	spreadsheet.setHeader("% assegurada pelo docente");
	spreadsheet.setHeader("OID execucao disciplina");
	spreadsheet.setHeader("OID professorship");

	for (ExecutionSemester executionSemester : getExecutionYear().getExecutionPeriods()) {
	    for (TeacherService teacherService : executionSemester.getTeacherServices()) {
		for (DegreeTeachingService degreeTeachingService : teacherService.getDegreeTeachingServices()) {

		    final Shift shift = degreeTeachingService.getShift();

		    if (!shift.hasSchoolClassForDegreeType(getDegreeType())) {
			continue;
		    }

		    Row row = spreadsheet.addRow();
		    row.setCell(executionSemester.getSemester());
		    row.setCell(teacherService.getTeacher().getPerson().getIstUsername());
		    row.setCell(shift.getIdInternal());
		    row.setCell(shift.getNome());
		    row.setCell(shift.getExecutionCourse().getIdInternal());
		    row.setCell(degreeTeachingService.getPercentage() != null ? degreeTeachingService.getPercentage().toString()
			    .replace('.', ',') : StringUtils.EMPTY);
		    row.setCell(String.valueOf(shift.getExecutionCourse().getOid()));
		    row.setCell(String.valueOf(degreeTeachingService.getProfessorship().getOid()));
		}
	    }
	}
    }
}
