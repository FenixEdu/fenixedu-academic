package net.sourceforge.fenixedu.domain.reports;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class TimetablesReportFile extends TimetablesReportFile_Base {

    public TimetablesReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Horarios";
    }

    @Override
    protected String getPrefix() {
        return "Horarios";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws IOException {
        timetables(spreadsheet, getExecutionYear(), getDegreeType());
    }

    private void generateNameAndHeaders(Spreadsheet spreadsheet, ExecutionYear executionYear, DegreeType degreeType) {
        spreadsheet.setName("Horarios " + executionYear.getQualifiedName().replace("/", "") + " " + degreeType.getName());
        spreadsheet.setHeader("Id Docente");
        spreadsheet.setHeader("Nome Docente");
        spreadsheet.setHeader("Ano Lectivo");
        spreadsheet.setHeader("Semestre");
        spreadsheet.setHeader("ID Execution Course");
        spreadsheet.setHeader("OID Execution Course");
        spreadsheet.setHeader("ID Turno");
        spreadsheet.setHeader("Nome Turno");
        spreadsheet.setHeader("Tipo Aula");
        spreadsheet.setHeader("Dia Semana");
        spreadsheet.setHeader("Hora Inicio");
        spreadsheet.setHeader("Hora Fim");
        spreadsheet.setHeader("Sala");
        spreadsheet.setHeader("Percentagem Assegurada pelo Docente");
        spreadsheet.setHeader("Nr Alunos Inscritos");
    }

    private void timetables(Spreadsheet spreadsheet, final ExecutionYear executionYear, final DegreeType degreeType)
            throws IOException {
        generateNameAndHeaders(spreadsheet, executionYear, degreeType);

        Map<ExecutionCourse, Boolean> areLessonsWithoutTeacherConsidered = new HashMap<ExecutionCourse, Boolean>();
        for (ExecutionCourse executionCourse : getRootDomainObject().getExecutionCourses()) {
            areLessonsWithoutTeacherConsidered.put(executionCourse, false);
        }
        for (Teacher teacher : getRootDomainObject().getTeachers()) {
            for (final ExecutionSemester semester : executionYear.getExecutionPeriods()) {
                ExecutionCourse executionCourse = null;
                for (Professorship professorship : teacher.getDegreeProfessorshipsByExecutionPeriod(semester)) {
                    executionCourse = professorship.getExecutionCourse();
                    boolean isSameDegreeType = false;
                    for (Degree degree : professorship.getExecutionCourse().getDegreesSortedByDegreeName()) {
                        if (degree.getDegreeType().compareTo(degreeType) == 0) {
                            isSameDegreeType = true;
                        }
                    }
                    if (!isSameDegreeType) {
                        continue;
                    }
                    for (DegreeTeachingService service : professorship.getDegreeTeachingServicesOrderedByShift()) {
                        for (Lesson lesson : service.getShift().getAssociatedLessons()) {
                            for (ShiftType shiftType : service.getShift().getTypes()) {
                                final Row row = spreadsheet.addRow();

                                // Nr Docente
                                row.setCell(teacher.getPerson().getIstUsername());

                                // Nome Docente
                                row.setCell(teacher.getPerson().getName());

                                // Ano Lectivo
                                row.setCell(executionYear.getQualifiedName());

                                // Semestre
                                row.setCell(semester.getSemester());

                                // ID Execution Course
                                row.setCell(executionCourse.getExternalId());

                                // OID Execution Course
                                row.setCell(String.valueOf(executionCourse.getOid()));

                                // ID Turno
                                row.setCell(service.getShift().getExternalId());

                                // Nome Turno
                                row.setCell(service.getShift().getNome());

                                // Tipo Aula
                                row.setCell(shiftType.getFullNameTipoAula());

                                // Dia Semana
                                row.setCell(lesson.getDiaSemana().getDiaSemanaString());

                                // Hora Início
                                row.setCell(lesson.getBeginHourMinuteSecond().toString());

                                // Hora Fim
                                row.setCell(lesson.getEndHourMinuteSecond().toString());

                                // Sala
                                if (lesson.getSala() != null) {
                                    row.setCell(lesson.getSala().getNome());
                                } else {
                                    row.setCell("");
                                }

                                // Percentagem Assegurada pelo Docente
                                row.setCell(((Math.round((service.getPercentage()).doubleValue() * 100.0)) / 100.0));

                                // Nº Alunos Inscritos
                                row.setCell(service.getShift().getShiftEnrolmentsOrderedByDate().size());
                            }
                        }
                    }
                }
                if ((executionCourse != null) && (areLessonsWithoutTeacherConsidered.get(executionCourse) == false)) {
                    for (Lesson lesson : getRootDomainObject().getLessons()) {
                        if (lesson.getExecutionCourse() != executionCourse) {
                            continue;
                        }
                        if (lesson.getShift().getDegreeTeachingServices().isEmpty()) {
                            for (ShiftType shiftType : lesson.getShift().getTypes()) {
                                // Licoes sem professor
                                final Row row = spreadsheet.addRow();

                                // Nr Docente
                                row.setCell("");

                                // Nome Docente
                                row.setCell("");

                                // Ano Lectivo
                                row.setCell(executionYear.getQualifiedName());

                                // Semestre
                                row.setCell(semester.getSemester());

                                // ID Execution Course
                                row.setCell(executionCourse.getExternalId());

                                // OID Execution Course
                                row.setCell(String.valueOf(executionCourse.getOid()));

                                // ID Turno
                                row.setCell(lesson.getShift().getExternalId());

                                // Nome Turno
                                row.setCell(lesson.getShift().getNome());

                                // Tipo Aula
                                row.setCell(shiftType.getFullNameTipoAula());

                                // Dia Semana
                                row.setCell(lesson.getDiaSemana().getDiaSemanaString());

                                // Hora Início
                                row.setCell(lesson.getBeginHourMinuteSecond().toString());

                                // Hora Fim
                                row.setCell(lesson.getEndHourMinuteSecond().toString());

                                // Sala
                                if (lesson.getSala() != null) {
                                    row.setCell(lesson.getSala().getNome());
                                } else {
                                    row.setCell("");
                                }

                                // Percentagem Assegurada pelo Docente
                                row.setCell("");

                                // Nº Alunos Inscritos
                                row.setCell(lesson.getShift().getShiftEnrolmentsOrderedByDate().size());

                                areLessonsWithoutTeacherConsidered.put(executionCourse, true);
                            }
                        }
                        if (areLessonsWithoutTeacherConsidered.get(executionCourse) == true) {
                            break;
                        }
                    }
                }
            }
        }
        spreadsheet.exportToXLSSheet(new File("Horarios " + executionYear.getQualifiedName().replace("/", "") + " "
                + degreeType.getName() + ".xls"));
    }
}
