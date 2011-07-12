package net.sourceforge.fenixedu.domain.reports;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRelation;
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
	spreadsheet.setHeader("OID");
	spreadsheet.setHeader("Nome");
	spreadsheet.setHeader("Data de nascimento");
	spreadsheet.setHeader("Sexo");
	spreadsheet.setHeader("Nacionalidade");
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
	    PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
	    if (personProfessionalData != null) {
		GiafProfessionalData giafProfessionalData = personProfessionalData
			.getGiafProfessionalDataByCategoryType(CategoryType.TEACHER);

		if (personProfessionalData != null && giafProfessionalData != null) {
		    PersonContractSituation personContractSituation = personProfessionalData
			    .getCurrentOrLastPersonContractSituationByCategoryType(CategoryType.TEACHER, executionYear
				    .getBeginDateYearMonthDay().toLocalDate(), executionYear.getEndDateYearMonthDay()
				    .toLocalDate());

		    if (personContractSituation != null) {
			final Row row = spreadsheet.addRow();
			Person person = teacher.getPerson();

			// Coluna "Nr mecanográfico"
			row.setCell(teacher.getPerson().getIstUsername());

			// Coluna "OID"
			row.setCell(teacher.getPerson().getExternalId());

			// Coluna "Nome"
			row.setCell(person.getName());

			// Coluna "Data de nascimento"
			row.setCell(writeDate(YearMonthDay.fromDateFields(person.getDateOfBirth())));

			// Coluna "Sexo"
			row.setCell(person.getGender().toLocalizedString());

			// Coluna "Nacionalidade"
			row.setCell(person.getCountry() != null ? person.getCountry().getCountryNationality().getContent() : "");

			// Coluna "Departamento ou Secção Autónoma" e
			// "Área científica ou Secção"

			Department department = teacher.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(),
				executionYear.getEndDateYearMonthDay());
			if (department != null) {
			    row.setCell(department.getName());
			    row.setCell(department.getDepartmentUnit() != null ? department.getDepartmentUnit().getName() : "");
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
			row.setCell(person.getEmailForSendingEmails());

			// Coluna "Categoria"
			ProfessionalCategory professionalCategory = personProfessionalData.getLastProfessionalCategory(
				giafProfessionalData, executionYear.getBeginDateYearMonthDay().toLocalDate(), executionYear
					.getEndDateYearMonthDay().toLocalDate());
			row.setCell(professionalCategory != null ? professionalCategory.getName().getContent() : "");

			// Coluna "Regime de contratação"
			ProfessionalRegime professionalRegime = personProfessionalData.getLastProfessionalRegime(
				giafProfessionalData, executionYear.getBeginDateYearMonthDay().toLocalDate(), executionYear
					.getEndDateYearMonthDay().toLocalDate());
			row.setCell(professionalRegime != null ? professionalRegime.getName().toString() : "");

			// Coluna "Vínculo"
			ProfessionalRelation professionalRelation = personProfessionalData.getLastProfessionalRelation(
				giafProfessionalData, executionYear.getBeginDateYearMonthDay().toLocalDate(), executionYear
					.getEndDateYearMonthDay().toLocalDate());

			row.setCell(professionalRelation != null ? professionalRelation.getName().toString() : "");

			// Coluna "Data início contrato"
			row.setCell(writeDate(personContractSituation.getBeginDate()));

			// Coluna "Data conclusão contrato"
			row.setCell(writeDate(personContractSituation.getEndDate()));

			// Coluna "Nº de anos na instituição"
			Period yearsInHouse = Period.ZERO;
			for (PersonContractSituation current : personProfessionalData
				.getPersonContractSituationsByCategoryType(CategoryType.TEACHER)) {
			    yearsInHouse = yearsInHouse.plus(new Period(current.getBeginDate(),
				    (current.getEndDate() == null ? new LocalDate() : current.getEndDate())));
			}
			row.setCell(yearsInHouse.getYears());
		    }
		}
	    }
	}

	spreadsheet.exportToXLSSheet(new File("Docentes do IST " + executionYear.getQualifiedName().replace("/", "") + ".xls"));
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

    private String writeDate(LocalDate localDate) {
	return localDate == null ? "" : localDate.toString();
    }

    private String writeDate(YearMonthDay yearMonthDay) {
	return yearMonthDay.toString();
    }
}
