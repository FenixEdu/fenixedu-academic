package net.sourceforge.fenixedu.domain.reports;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.EmployeeContractSituation;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import org.apache.velocity.runtime.parser.node.GetExecutor;
import org.joda.time.YearMonthDay;
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
	spreadsheet.setHeader("Nr Docente");
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

    private void timetables(Spreadsheet spreadsheet, final ExecutionYear executionYear, final DegreeType degreeType) throws IOException {
	generateNameAndHeaders(spreadsheet, executionYear, degreeType);

	for (Teacher teacher : getRootDomainObject().getTeachers()) {
	    for (final ExecutionSemester semester : executionYear.getExecutionPeriods()) {
		for(Professorship professorship : teacher.getDegreeProfessorshipsByExecutionPeriod(semester)) {
		    boolean isSameDegreeType = false;
		    for(Degree degree : professorship.getExecutionCourse().getDegreesSortedByDegreeName()) {
			if(degree.getDegreeType().compareTo(degreeType) == 0) {
			    isSameDegreeType = true;
			}
		    }
		    if(!isSameDegreeType) {
			continue;
		    }
		    for(DegreeTeachingService service : professorship.getDegreeTeachingServicesOrderedByShift()) {
			for(Lesson lesson : service.getShift().getAssociatedLessons()) {
			    for(ShiftType shiftType : service.getShift().getTypes()) {
				final Row row = spreadsheet.addRow();

			    	// Nr Docente
			    	row.setCell(teacher.getTeacherNumber());

			    	// Nome Docente
			    	row.setCell(teacher.getPerson().getName());

			    	// Ano Lectivo
			    	row.setCell(executionYear.getQualifiedName());

			    	// Semestre
			    	row.setCell(semester.getQualifiedName());

			    	// ID Execution Course
			    	row.setCell(professorship.getExecutionCourse().getIdInternal());

			    	// OID Execution Course
			    	row.setCell(String.valueOf(professorship.getExecutionCourse().getOid()));

			    	// ID Turno
			    	row.setCell(service.getShift().getIdInternal());

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
			    	if(lesson.getSala() != null) {
			    	    row.setCell(lesson.getSala().getNome());
			    	}

			    	// Percentagem Assegurada pelo Docente
			    	row.setCell(((Math.round(((Double)service.getPercentage()).doubleValue() * 100.0)) / 100.0));

			    	// Nº Alunos Inscritos
			    	row.setCell(service.getShift().getShiftEnrolmentsOrderedByDate().size());
			    }
			}
		    }
		}
	    }
	}
	spreadsheet.exportToXLSSheet(new File("Horarios " + executionYear.getQualifiedName().replace("/", "") + " " + degreeType.getName() + ".xls"));
    }
}
