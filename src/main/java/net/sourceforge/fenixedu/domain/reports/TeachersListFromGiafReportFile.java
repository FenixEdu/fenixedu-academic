/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.reports;

import java.io.IOException;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
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
        return "Informação sobre docentes do " + Unit.getInstitutionAcronym();
    }

    @Override
    protected String getPrefix() {
        return "Informação sobre docentes do " + Unit.getInstitutionAcronym();
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws IOException {
        listTeachers(spreadsheet, getExecutionYear());
    }

    private void generateNameAndHeaders(Spreadsheet spreadsheet, ExecutionYear executionYear) {
        spreadsheet.setName("Docentes do " + Unit.getInstitutionAcronym() + " "
                + executionYear.getQualifiedName().replace("/", ""));
        spreadsheet.setHeader("Identificação");
        spreadsheet.setHeader("OID_PERSON");
        spreadsheet.setHeader("Tipo Docente");
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
        spreadsheet.setHeader("Data início contrato/Autorização");
        spreadsheet.setHeader("Data conclusão contrato/Autorização");
        spreadsheet.setHeader("Nº de Horas lectivas");
        spreadsheet.setHeader("Nº de anos na instituição");
    }

    private void listTeachers(Spreadsheet spreadsheet, final ExecutionYear executionYear) throws IOException {
        generateNameAndHeaders(spreadsheet, executionYear);
        for (final Teacher teacher : getRootDomainObject().getTeachersSet()) {
            PersonProfessionalData personProfessionalData = teacher.getPerson().getPersonProfessionalData();
            if (personProfessionalData != null) {
                GiafProfessionalData giafProfessionalData = personProfessionalData.getGiafProfessionalData();
                if (personProfessionalData != null && giafProfessionalData != null) {
                    PersonContractSituation personContractSituation =
                            personProfessionalData.getCurrentOrLastPersonContractSituationByCategoryType(CategoryType.TEACHER,
                                    executionYear.getBeginDateYearMonthDay().toLocalDate(), executionYear
                                            .getEndDateYearMonthDay().toLocalDate());
                    if (personContractSituation != null) {
                        Unit unit =
                                teacher.getLastWorkingUnit(executionYear.getBeginDateYearMonthDay(),
                                        executionYear.getEndDateYearMonthDay());
                        ProfessionalCategory professionalCategory =
                                personProfessionalData.getLastProfessionalCategoryByCategoryType(CategoryType.TEACHER,
                                        executionYear.getBeginDateYearMonthDay().toLocalDate(), executionYear
                                                .getEndDateYearMonthDay().toLocalDate());

                        ProfessionalRegime professionalRegime =
                                personProfessionalData.getLastProfessionalRegime(giafProfessionalData, executionYear
                                        .getBeginDateYearMonthDay().toLocalDate(), executionYear.getEndDateYearMonthDay()
                                        .toLocalDate());

                        ProfessionalRelation professionalRelation =
                                personProfessionalData.getLastProfessionalRelation(giafProfessionalData, executionYear
                                        .getBeginDateYearMonthDay().toLocalDate(), executionYear.getEndDateYearMonthDay()
                                        .toLocalDate());

                        Double mandatoryLessonHours =
                                teacher.getMandatoryLessonHours(getLastSemester(personContractSituation, executionYear));

                        Period yearsInHouse =
                                new Period(giafProfessionalData.getInstitutionEntryDate(),
                                        (personContractSituation.getEndDate() == null ? new LocalDate() : personContractSituation
                                                .getEndDate()));

                        writePersonInformationRow(spreadsheet, executionYear, teacher, "CONTRATADO", unit, professionalCategory,
                                professionalRegime, professionalRelation, personContractSituation.getBeginDate(),
                                personContractSituation.getEndDate(), mandatoryLessonHours, yearsInHouse.getYears());

                    }
                }
            }
        }

        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            for (ExternalTeacherAuthorization externalTeacherAuthorization : ExternalTeacherAuthorization
                    .getExternalTeacherAuthorizationSet(executionSemester)) {
                writePersonInformationRow(spreadsheet, executionYear, externalTeacherAuthorization.getTeacher(), "AUTORIZADO",
                        externalTeacherAuthorization.getDepartment().getDepartmentUnit(),
                        externalTeacherAuthorization.getProfessionalCategory(), null, null, externalTeacherAuthorization
                                .getExecutionSemester().getBeginDateYearMonthDay().toLocalDate(), externalTeacherAuthorization
                                .getExecutionSemester().getEndDateYearMonthDay().toLocalDate(),
                        externalTeacherAuthorization.getLessonHours(), null);

            }
        }
    }

    private ExecutionSemester getLastSemester(PersonContractSituation personContractSituation, ExecutionYear executionYear) {
        ExecutionSemester lastExecutionSemester = null;
        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            if (lastExecutionSemester == null
                    || personContractSituation.overlaps(executionSemester.getLessonsPeriod().getIntervalWithNextPeriods())) {
                lastExecutionSemester = executionSemester;
            }
        }
        return lastExecutionSemester;
    }

    private void writePersonInformationRow(Spreadsheet spreadsheet, ExecutionYear executionYear, Teacher teacher,
            String teacherType, Unit unit, ProfessionalCategory professionalCategory, ProfessionalRegime professionalRegime,
            ProfessionalRelation professionalRelation, LocalDate beginDate, LocalDate endDate, Double hours,
            Integer yearsInInstitution) {
        final Row row = spreadsheet.addRow();
        // Coluna "Nr mecanográfico"
        row.setCell(teacher.getPerson().getIstUsername());
        // Coluna "OID"
        row.setCell(teacher.getPerson().getExternalId());
        // Coluna "Tipo"
        row.setCell(teacherType);
        // Coluna "Nome"
        row.setCell(teacher.getPerson().getName());
        // Coluna "Data de nascimento"
        row.setCell(teacher.getPerson().getDateOfBirth() != null ? writeDate(YearMonthDay.fromDateFields(teacher.getPerson()
                .getDateOfBirth())) : null);
        // Coluna "Sexo"
        row.setCell(teacher.getPerson().getGender().toLocalizedString());
        // Coluna "Nacionalidade"
        row.setCell(teacher.getPerson().getCountry() != null ? teacher.getPerson().getCountry().getCountryNationality()
                .getContent() : null);

        // Coluna "Departamento ou Secção Autónoma" e
        // "Área científica ou Secção"
        DepartmentUnit departmentUnit = unit != null ? unit.getDepartmentUnit() : null;
        row.setCell(departmentUnit != null ? departmentUnit.getName() : null);
        row.setCell(unit != null && (departmentUnit == null || departmentUnit != unit) ? unit.getName() : null);

        // Coluna "Grau académico"
        // Coluna "Local de obtenção do grau"
        // Coluna "Nome ou área do grau"
        Qualification qualification = getBetterQualificationOfPersonByExecutionYear(teacher.getPerson(), executionYear);

        row.setCell(qualification != null && qualification.getType() != null ? qualification.getType().getLocalizedName() : null);
        row.setCell(qualification != null ? qualification.getSchool() : null);
        row.setCell(qualification != null ? qualification.getDegree() : null);

        // Coluna "E-mail"
        row.setCell(teacher.getPerson().getEmailForSendingEmails());

        // Coluna "Categoria"
        row.setCell(professionalCategory != null ? professionalCategory.getName().getContent() : null);

        // Coluna "Regime de contratação"
        row.setCell(professionalRegime != null ? professionalRegime.getName().toString() : null);

        // Coluna "Vínculo"
        row.setCell(professionalRelation != null ? professionalRelation.getName().toString() : null);

        // Coluna "Data início contrato/Autorização"
        row.setCell(writeDate(beginDate));

        // Coluna "Data conclusão contrato/Autorização"
        row.setCell(writeDate(endDate));

        // Nº de Horas lectivas
        row.setCell(hours);

        // Coluna "Nº de anos na instituição"
        row.setCell(yearsInInstitution);

        return;
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
        return localDate == null ? null : localDate.toString();
    }

    private String writeDate(YearMonthDay yearMonthDay) {
        return yearMonthDay.toString();
    }
}
