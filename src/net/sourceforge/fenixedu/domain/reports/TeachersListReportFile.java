package net.sourceforge.fenixedu.domain.reports;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.TeacherProfessionalSituation;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class TeachersListReportFile extends TeachersListReportFile_Base {

    public TeachersListReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Informação sobre docentes do IST (Do Aplica)";
    }

    @Override
    protected String getPrefix() {
	return "Informação sobre docentes do IST (Do Aplica)";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws IOException {
	listTeachers(spreadsheet, getExecutionYear());
    }

    private void generateNameAndHeaders(Spreadsheet spreadsheet, ExecutionYear executionYear) {
	spreadsheet.setName("Docentes do IST " + executionYear.getQualifiedName().replace("/", ""));
	spreadsheet.setHeader("Identificação");
	spreadsheet.setHeader("Nome");
	spreadsheet.setHeader("Data de nascimento");
	spreadsheet.setHeader("Sexo");
	spreadsheet.setHeader("Departamento ou Secção Autónoma");
	spreadsheet.setHeader("Área científica ou Secção");
	spreadsheet.setHeader("Grau académico");
	spreadsheet.setHeader("Local de obtenção do grau");
	spreadsheet.setHeader("Nome ou Área do grau");
	spreadsheet.setHeader("E-mail");
	spreadsheet.setHeader("Categoria");
	spreadsheet.setHeader("Regime de contratação");
	spreadsheet.setHeader("Vínculo");
	spreadsheet.setHeader("Data início contrato");
	spreadsheet.setHeader("Data conclusão contrato");
	spreadsheet.setHeader("Nº de anos na instituição");
    }

    private void listTeachers(Spreadsheet spreadsheet, final ExecutionYear executionYear) throws IOException {
	generateNameAndHeaders(spreadsheet, executionYear);

	for (final Teacher teacher : getRootDomainObject().getTeachers()) {
	    TeacherProfessionalSituation teacherProfessionalSituation = getLastSituationFromOneTeacherAndExecutionYear(teacher,
		    executionYear);
	    if (teacherProfessionalSituation != null
		    && ((!teacherProfessionalSituation.isEndSituation()) || (teacherProfessionalSituation.isEndSituation() && teacherProfessionalSituation
			    .getBeginDate().after(executionYear.getBeginDate())))) {
		final Row row = spreadsheet.addRow();
		Person person = teacher.getPerson();

		// Coluna "Nr mecanográfico"
		row.setCell(teacher.getPerson().getIstUsername());

		// Coluna "Nome"
		row.setCell(person.getName());

		// Coluna "Data de nascimento"
		row.setCell(writeDate(YearMonthDay.fromDateFields(person.getDateOfBirth())));

		// Coluna "Sexo"
		row.setCell(person.getGender().toLocalizedString());

		// Coluna "Departamento ou Secção Autónoma"
		Department department = teacher.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(),
			executionYear.getEndDateYearMonthDay());
		if (department != null) {
		    row.setCell(department.getName());
		} else {
		    row.setCell("");
		}

		// Coluna "Área científica ou Secção"
		Unit unit = teacher.getLastWorkingUnit(executionYear.getBeginDateYearMonthDay(),
			executionYear.getEndDateYearMonthDay());
		if (unit != null) {
		    row.setCell(unit.getName());
		} else {
		    row.setCell("");
		}

		// Coluna "Grau académico"
		// Coluna "Local de obtenção do grau"
		// Coluna "Nome ou área do grau"
		Qualification qualification = getBetterQualificationOfPersonByExecutionYear(person, executionYear);
		if (qualification != null) {
		    if (qualification.getType() != null) {
			row.setCell(qualification.getType().getLocalizedName());
		    } else {
			row.setCell("");
		    }
		    row.setCell(qualification.getSchool());
		    row.setCell(qualification.getDegree());
		} else {
		    row.setCell("");
		    row.setCell("");
		    row.setCell("");
		}

		// Coluna "E-mail"
		row.setCell(person.getEmail());

		// Coluna "Categoria"
		if (teacherProfessionalSituation.getProfessionalCategory() != null) {
		    row.setCell(teacherProfessionalSituation.getProfessionalCategory().getName().getContent());
		} else {
		    row.setCell("");
		}

		// Coluna "Regime de contratação"
		if (teacherProfessionalSituation.getRegimeType() != null) {
		    row.setCell(ResourceBundle.getBundle("resources.EnumerationResources", Language.getDefaultLocale())
			    .getString(teacherProfessionalSituation.getRegimeType().getName()));
		} else {
		    row.setCell("");
		}

		// Coluna "Vínculo"
		if (teacherProfessionalSituation.getSituationType() != null) {
		    row.setCell(ResourceBundle.getBundle("resources.EnumerationResources", Language.getDefaultLocale())
			    .getString(teacherProfessionalSituation.getSituationType().getName()));
		} else {
		    row.setCell("");
		}

		// Coluna "Data início contrato"
		if (teacherProfessionalSituation.getBeginDate() != null) {
		    row.setCell(writeDate(teacherProfessionalSituation.getBeginDateYearMonthDay()));
		} else {
		    row.setCell("");
		}

		// Coluna "Data conclusão contrato"
		if (teacherProfessionalSituation.getEndDate() != null) {
		    row.setCell(writeDate(teacherProfessionalSituation.getEndDateYearMonthDay()));
		} else {
		    row.setCell("");
		}

		// Coluna "Nº de anos na instituição"
		Period yearsInHouse = Period.ZERO;
		for (TeacherProfessionalSituation situation : teacher.getLegalRegimens()) {
		    yearsInHouse = yearsInHouse.plus(new Period(situation.getBeginDateYearMonthDay(), (situation
			    .getEndDateYearMonthDay() == null ? new YearMonthDay() : situation.getEndDateYearMonthDay())));
		}
		row.setCell(yearsInHouse.getYears());
	    }
	}

	spreadsheet.exportToXLSSheet(new File("Docentes do IST " + executionYear.getQualifiedName().replace("/", "") + ".xls"));
    }

    private TeacherProfessionalSituation getLastSituationFromOneTeacherAndExecutionYear(Teacher teacher,
	    ExecutionYear executionYear) {
	TeacherProfessionalSituation teacherProfessionalSituation = null;
	for (TeacherProfessionalSituation tps : teacher.getLegalRegimens()) {
	    if (!tps.isEndSituation()
		    && isPeriodInExecutionYear(tps.getBeginDate(), tps.getEndDate(), executionYear)
		    && ((teacherProfessionalSituation == null) || (teacherProfessionalSituation.getBeginDate().before(tps
			    .getBeginDate())))) {
		teacherProfessionalSituation = tps;
	    }
	}
	return teacherProfessionalSituation;
    }

    private Qualification getBetterQualificationOfPersonByExecutionYear(Person person, ExecutionYear executionYear) {
	Qualification qualification = null;
	for (Qualification q : person.getAssociatedQualifications()) {
	    if (q.getDate() != null && q.getDate().before(executionYear.getEndDate())
		    && ((qualification == null) || (Qualification.COMPARATOR_BY_YEAR.compare(qualification, q) < 0))) {
		qualification = q;
	    }
	}
	return qualification;
    }

    private boolean isPeriodInExecutionYear(Date beginLocalDate, Date endLocalDate, ExecutionYear executionYear) {
	if (endLocalDate == null) {
	    return isPeriodInExecutionYear(LocalDate.fromDateFields(beginLocalDate), null, executionYear);
	} else {
	    return isPeriodInExecutionYear(LocalDate.fromDateFields(beginLocalDate), LocalDate.fromDateFields(endLocalDate),
		    executionYear);
	}
    }

    private boolean isPeriodInExecutionYear(LocalDate beginLocalDate, LocalDate endLocalDate, ExecutionYear executionYear) {
	if (endLocalDate == null) {
	    return !beginLocalDate.isAfter(executionYear.getEndDateYearMonthDay());
	} else {
	    return (!beginLocalDate.isBefore(executionYear.getBeginDateYearMonthDay()) && !beginLocalDate.isAfter(executionYear
		    .getEndDateYearMonthDay()))
		    || (!endLocalDate.isBefore(executionYear.getBeginDateYearMonthDay()) && !endLocalDate.isAfter(executionYear
			    .getEndDateYearMonthDay()));
	}
    }

    private String writeDate(YearMonthDay yearMonthDay) {
	return yearMonthDay.getYear() + "-" + yearMonthDay.getMonthOfYear() + "-" + yearMonthDay.getDayOfMonth();
    }
}
