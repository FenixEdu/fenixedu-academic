package net.sourceforge.fenixedu.domain.reports;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class SummaryOccupancyReportFile extends SummaryOccupancyReportFile_Base {

    public SummaryOccupancyReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Listagem de Presencas em Aulas com base em Sumarios";
    }

    @Override
    protected String getPrefix() {
	return "summaryOccupancy";
    }

    @Override
    public void renderReport(final Spreadsheet spreadsheet) throws Exception {
	spreadsheet.setHeader("Ano Lectivo");
	spreadsheet.setHeader("Semestre");
	spreadsheet.setHeader("OID da Execução da Disciplina");
	spreadsheet.setHeader("Nome da Disciplina");
	spreadsheet.setHeader("Tipo do Turno");
	spreadsheet.setHeader("Turno");
	spreadsheet.setHeader("ID Docente");
	spreadsheet.setHeader("Nome do Docente");
	spreadsheet.setHeader("Horario");
	spreadsheet.setHeader("Sala");
	spreadsheet.setHeader("Numero Presencas");
	for (final ExecutionSemester executionSemester : getExecutionYear().getExecutionPeriodsSet()) {
	    for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
		for (final Summary summary : executionCourse.getAssociatedSummariesSet()) {
		    final Row row = spreadsheet.addRow();
		    row.setCell(getExecutionYear().getYear());
		    row.setCell(executionSemester.getSemester());
		    row.setCell(executionCourse.getExternalId());
		    row.setCell(executionCourse.getName());
		    final LessonInstance lessonInstance = summary.getLessonInstance();
		    final CourseLoad courseLoad = lessonInstance == null ? null : lessonInstance.getCourseLoad();
		    final ShiftType shiftType = courseLoad == null ? null : courseLoad.getType();
		    row.setCell(shiftType == null ? "" : shiftType.getName());
		    final Shift shift = summary.getShift();
		    row.setCell(shift == null ? "" : shift.getNome());
		    row.setCell(getTeacherId(summary));
		    row.setCell(getTeacherName(summary));
		    row.setCell(getSchedule(summary));
		    final AllocatableSpace room = summary.getRoom();
		    row.setCell(room == null ? "" : room.getIdentification());
		    row.setCell(summary.getStudentsNumber());
		}
	    }
	}
    }

    private String getSchedule(final Summary summary) {
	final LessonInstance lessonInstance = summary.getLessonInstance();
	if (lessonInstance != null) {
	    final DateTime begin = lessonInstance.getBeginDateTime();
	    final DateTime end = lessonInstance.getEndDateTime();
	    return begin.toString("yyyy-MM-dd HH:mm - ") + end.toString("yyyy-MM-dd HH:mm");
	}
	final DateTime summaryDateTime = summary.getSummaryDateTime();
	return summaryDateTime.toString("yyyy-MM-dd HH:mm");
    }

    private String getTeacherName(final Summary summary) {
	final Professorship professorship = summary.getProfessorship();
	if (professorship != null) {
	    return professorship.getPerson().getName();
	}
	final Teacher teacher = summary.getTeacher();
	if (teacher != null) {
	    return teacher.getPerson().getName();
	}
	return summary.getTeacherName();
    }

    private String getTeacherId(Summary summary) {
	final Professorship professorship = summary.getProfessorship();
	if (professorship != null) {
	    return professorship.getPerson().getUsername();
	}
	final Teacher teacher = summary.getTeacher();
	if (teacher != null) {
	    return teacher.getPerson().getUsername();
	}
	return null;
    }

}
