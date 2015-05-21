/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.reports;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.SchoolLevelType;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ConclusionProcess;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.joda.time.LocalDate;

public class GraduationReportFile extends GraduationReportFile_Base {

    public GraduationReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem de diplomados";
    }

    @Override
    protected String getPrefix() {
        return "diplomados";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) {
        spreadsheet.setHeader("número aluno");
        spreadsheet.setHeader("nome");
        setDegreeHeaders(spreadsheet);
        spreadsheet.setHeader("ciclo");
        spreadsheet.setHeader("Nota Conclusão Secundário");
        spreadsheet.setHeader("Nota Seriação");
        spreadsheet.setHeader("ano de ingresso");
        spreadsheet.setHeader("ano lectivo conclusão");
        spreadsheet.setHeader("data conclusão");
        spreadsheet.setHeader("número de anos para conclusão");
        spreadsheet.setHeader("média final");
        spreadsheet.setHeader("morada");
        spreadsheet.setHeader("código postal");
        spreadsheet.setHeader("cidade");
        spreadsheet.setHeader("país");
        spreadsheet.setHeader("telefone");
        spreadsheet.setHeader("telemovel");
        spreadsheet.setHeader("email");
        spreadsheet.setHeader("sexo");
        spreadsheet.setHeader("data nascimento");

        final Set<ExecutionYear> toInspectSet =
                getExecutionYear() == null ? getRootDomainObject().getExecutionYearsSet() : Collections
                        .singleton(getExecutionYear());

        for (final ExecutionYear toInspect : toInspectSet) {
            for (final ConclusionProcess conclusionProcess : toInspect.getConclusionProcessesConcludedSet()) {
                if (checkDegreeType(getDegreeType(), conclusionProcess)) {
                    reportGraduate(spreadsheet, conclusionProcess);
                }
            }
        }
    }

    private void reportGraduate(final Spreadsheet sheet, final ConclusionProcess conclusionProcess) {
        final Row row = sheet.addRow();

        final Registration registration = conclusionProcess.getRegistration();
        final ExecutionYear ingression = conclusionProcess.getIngressionYear();
        final ExecutionYear conclusion = conclusionProcess.getConclusionYear();
        final LocalDate conclusionDate = conclusionProcess.getConclusionDate();

        row.setCell(registration.getNumber());
        row.setCell(registration.getName());
        setDegreeCells(row, registration.getDegree());
        row.setCell(conclusionProcess.getName().getContent());
        row.setCell(registration.getPrecedentDegreeConclusionGrade(SchoolLevelType.SECOND_CYCLE_BASIC_SCHOOL));
        row.setCell(registration.getEntryGrade() != null ? registration.getEntryGrade().toString() : StringUtils.EMPTY);
        row.setCell(ingression.getYear());
        row.setCell(conclusion == null ? StringUtils.EMPTY : conclusion.getYear());
        row.setCell(conclusionDate == null ? StringUtils.EMPTY : conclusionDate.toString("yyyy-MM-dd"));
        row.setCell(conclusion == null ? StringUtils.EMPTY : String.valueOf(ingression.getDistanceInCivilYears(conclusion) + 1));
        row.setCell(conclusionProcess.getFinalGrade().getValue());

        setPersonCells(registration, row);
    }

    private void setPersonCells(final Registration registration, final Row row) {
        final Person person = registration.getPerson();

        final PhysicalAddress defaultPhysicalAddress = person.getDefaultPhysicalAddress();

        if (defaultPhysicalAddress != null) {
            row.setCell(defaultPhysicalAddress.getAddress());
            row.setCell(defaultPhysicalAddress.getPostalCode());
            row.setCell(defaultPhysicalAddress.getArea());
            row.setCell(defaultPhysicalAddress.getCountryOfResidence() == null ? StringUtils.EMPTY : defaultPhysicalAddress
                    .getCountryOfResidence().getName());
        } else {
            row.setCell(StringUtils.EMPTY);
            row.setCell(StringUtils.EMPTY);
            row.setCell(StringUtils.EMPTY);
            row.setCell(StringUtils.EMPTY);
        }

        row.setCell(person.getDefaultPhoneNumber());
        row.setCell(person.getDefaultMobilePhoneNumber());
        row.setCell(person.getInstitutionalOrDefaultEmailAddressValue());
        row.setCell(person.getGender().toLocalizedString());
        row.setCell(person.getDateOfBirthYearMonthDay() != null ? person.getDateOfBirthYearMonthDay().toString("yyyy-MM-dd") : StringUtils.EMPTY);
    }
}
