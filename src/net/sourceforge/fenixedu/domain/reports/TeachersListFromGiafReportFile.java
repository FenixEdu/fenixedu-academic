package net.sourceforge.fenixedu.domain.reports;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalRegime;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalRelation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class TeachersListFromGiafReportFile extends TeachersListFromGiafReportFile_Base {

    public TeachersListFromGiafReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Informação sobre docentes do IST (Do GIAF)";
    }

    @Override
    protected String getPrefix() {
	return "Informação sobre docentes do IST (Do GIAF)";
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
	    PersonContractSituation situation = getLastSituationFromOneTeacherAndExecutionYear(teacher, executionYear);
	    if (situation != null) {
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

		// Coluna "Departamento ou Secção Autónoma" e "Área científica ou Secção"
		
		Department department = teacher.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(),
			executionYear.getEndDateYearMonthDay());
		if (department != null) {
		    row.setCell(department.getName());
		    Unit unit = department.getDepartmentUnit();
		    if (unit != null) {
			row.setCell(unit.getName());
		    } else {
			row.setCell("");
		    }
		} else {
		    row.setCell("");
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
		PersonProfessionalCategory personProfessionalCategory = getLastCategoryFromOneTeacherAndExecutionYear(teacher,
			executionYear);
		ProfessionalCategory professionalCategory = null;
		if (personProfessionalCategory != null) {
		    professionalCategory = personProfessionalCategory.getProfessionalCategory();
		    // } else {
		    // professionalCategory =
		    // situation.getProfessionalCategory();
		}
		if (professionalCategory != null) {
		    row.setCell(professionalCategory.getName().toString());
		} else {
		    row.setCell("");
		}

		// Coluna "Regime de contratação"
		PersonProfessionalRegime personProfessionalRegime = getLastRegimeFromOneTeacherAndExecutionYear(teacher,
			executionYear);
		ProfessionalRegime professionalRegime = null;
		if (personProfessionalRegime != null) {
		    professionalRegime = personProfessionalRegime.getProfessionalRegime();
		} else if (personProfessionalCategory != null) {
		    professionalRegime = personProfessionalCategory.getProfessionalRegime();
		}
		if (professionalRegime != null) {
		    row.setCell(professionalRegime.getName().toString());
		} else {
		    row.setCell("");
		}

		// Coluna "Vínculo"
		PersonProfessionalRelation personProfessionalRelation = getLastRelationFromOneTeacherAndExecutionYear(teacher,
			executionYear);
		String professionalRelation = null;
		if (personProfessionalRelation != null) {
		    professionalRelation = personProfessionalRelation.getProfessionalRelation().getName().toString();
		} else {
		    professionalRelation = situation.getContractSituation().getName().toString();
		}
		if (professionalRelation != null) {
		    row.setCell(professionalRelation);
		} else {
		    row.setCell("");
		}

		// Coluna "Data início contrato"
		if (situation.getBeginDate() != null) {
		    row.setCell(writeDate(situation.getBeginDate()));
		} else {
		    row.setCell("");
		}

		// Coluna "Data conclusão contrato"
		if (situation.getEndDate() != null) {
		    row.setCell(writeDate(situation.getEndDate()));
		} else {
		    row.setCell("");
		}

		// Coluna "Nº de anos na instituição"
		Period yearsInHouse = Period.ZERO;
		GiafProfessionalData giafProfessionalData = teacher.getPerson().getPersonProfessionalData()
			.getGiafProfessionalDataByCategoryType(CategoryType.TEACHER);
		if (giafProfessionalData != null) {
		    for (PersonContractSituation current : giafProfessionalData.getPersonContractSituations()) {
			yearsInHouse = yearsInHouse.plus(new Period(current.getBeginDate(),
				(current.getEndDate() == null ? new LocalDate() : current.getEndDate())));
		    }
		}
		row.setCell(yearsInHouse.getYears());
	    }
	}

	spreadsheet.exportToXLSSheet(new File("Docentes do IST " + executionYear.getQualifiedName().replace("/", "") + ".xls"));
    }

    private PersonProfessionalRelation getLastRelationFromOneTeacherAndExecutionYear(Teacher teacher, ExecutionYear executionYear) {
	PersonProfessionalRelation relation = null;
	GiafProfessionalData giafProfessionalData = teacher.getPerson().getPersonProfessionalData()
		.getGiafProfessionalDataByCategoryType(CategoryType.TEACHER);
	if (giafProfessionalData != null) {
	    for (PersonProfessionalRelation rel : giafProfessionalData.getPersonProfessionalRelations()) {
		if (isPeriodInExecutionYear(rel.getBeginDate(), rel.getEndDate(), executionYear)
			&& ((relation == null) || (relation.getBeginDate().isBefore(rel.getBeginDate())))) {
		    relation = rel;
		}
	    }
	}
	return relation;
    }

    private PersonProfessionalRegime getLastRegimeFromOneTeacherAndExecutionYear(Teacher teacher, ExecutionYear executionYear) {
	PersonProfessionalRegime regime = null;
	GiafProfessionalData giafProfessionalData = teacher.getPerson().getPersonProfessionalData()
		.getGiafProfessionalDataByCategoryType(CategoryType.TEACHER);
	if (giafProfessionalData != null) {
	    for (PersonProfessionalRegime reg : giafProfessionalData.getPersonProfessionalRegimes()) {
		if (isPeriodInExecutionYear(reg.getBeginDate(), reg.getEndDate(), executionYear)
			&& ((regime == null) || (regime.getBeginDate().isBefore(reg.getBeginDate())))) {
		    regime = reg;
		}
	    }
	}
	return regime;
    }

    private PersonProfessionalCategory getLastCategoryFromOneTeacherAndExecutionYear(Teacher teacher, ExecutionYear executionYear) {
	PersonProfessionalCategory category = null;
	GiafProfessionalData giafProfessionalData = teacher.getPerson().getPersonProfessionalData()
		.getGiafProfessionalDataByCategoryType(CategoryType.TEACHER);
	if (giafProfessionalData != null) {
	    for (PersonProfessionalCategory cat : giafProfessionalData.getPersonProfessionalCategories()) {
		if (isPeriodInExecutionYear(cat.getBeginDate(), cat.getEndDate(), executionYear)
			&& ((category == null) || (category.getBeginDate().isBefore(cat.getBeginDate())))) {
		    category = cat;
		}
	    }
	}
	return category;
    }

    private PersonContractSituation getLastSituationFromOneTeacherAndExecutionYear(Teacher teacher, ExecutionYear executionYear) {
	PersonContractSituation situation = null;
	final PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
	if (personProfessionalData != null) {
	    GiafProfessionalData giafProfessionalData = personProfessionalData
		    .getGiafProfessionalDataByCategoryType(CategoryType.TEACHER);
	    if (giafProfessionalData != null) {
		for (PersonContractSituation pcs : giafProfessionalData.getPersonContractSituations()) {
		    if (pcs.getContractSituation().getEndSituation() == false
			    && isPeriodInExecutionYear(pcs.getBeginDate(), pcs.getEndDate(), executionYear)
			    && ((situation == null) || (situation.getBeginDate().isBefore(pcs.getBeginDate())))) {
			situation = pcs;
		    }
		}
	    }
	}
	return situation;
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

    private boolean isPeriodInExecutionYear(LocalDate beginLocalDate, LocalDate endLocalDate, ExecutionYear executionYear) {
	if (beginLocalDate == null) {
	    return false;
	}
	if (endLocalDate == null) {
	    return !beginLocalDate.isAfter(executionYear.getEndDateYearMonthDay());
	} else {
	    return (!beginLocalDate.isBefore(executionYear.getBeginDateYearMonthDay()) && !beginLocalDate.isAfter(executionYear
		    .getEndDateYearMonthDay()))
		    || (!endLocalDate.isBefore(executionYear.getBeginDateYearMonthDay()) && !endLocalDate.isAfter(executionYear
			    .getEndDateYearMonthDay()));
	}
    }

    private String writeDate(LocalDate localDate) {
	return localDate.toString();
    }

    private String writeDate(YearMonthDay yearMonthDay) {
	return yearMonthDay.toString();
    }
}
