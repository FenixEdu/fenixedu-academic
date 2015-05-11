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

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;

public class RegistrationReportFile extends RegistrationReportFile_Base {

    public RegistrationReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem de matrí­culas";
    }

    @Override
    protected String getPrefix() {
        return "matriculas";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
        spreadsheet.setHeader("número aluno");
        setDegreeHeaders(spreadsheet);
        spreadsheet.setHeader("código regime de ingresso na matrícula");
        spreadsheet.setHeader("regime de ingresso na matrícula");
        spreadsheet.setHeader("ano léctivo de início da matrícula");
        spreadsheet.setHeader("código regime de ingresso na escola");
        spreadsheet.setHeader("regime de ingresso na escola");
        spreadsheet.setHeader("ano léctivo de ingresso na escola");
        spreadsheet.setHeader("tipo de aluno");
        spreadsheet.setHeader("ano curricular");

        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (checkDegreeType(getDegreeType(), degree)) {
                if (isActive(degree)) {
                    for (final Registration registration : degree.getRegistrationsSet()) {
                        if (registration.isRegistered(getExecutionYear())) {
                            final Row row = spreadsheet.addRow();
                            row.setCell(registration.getNumber());
                            setDegreeCells(row, degree);

                            reportIngression(row, registration);

                            final Registration firstRegistration = findFirstRegistration(registration.getStudent());
                            reportIngression(row, firstRegistration);

                            if (registration.getRegistrationProtocol() != null) {
                                row.setCell(registration.getRegistrationProtocol().getCode());
                            } else {
                                row.setCell("");
                            }
                            row.setCell(Integer.toString(registration.getCurricularYear(getExecutionYear())));
                        }
                    }
                }
            }
        }
    }

    private Registration findFirstRegistration(final Student student) {
        return Collections.min(student.getRegistrationsSet(), Registration.COMPARATOR_BY_START_DATE);
    }

    private void reportIngression(final Row row, final Registration registration) {
        final IngressionType ingressionType = registration.getIngressionType();
        if (ingressionType != null) {
            row.setCell(ingressionType.getCode());
            row.setCell(ingressionType.getDescription().getContent());
        } else {
            row.setCell("");
            row.setCell("");
        }
        row.setCell(registration.getStartExecutionYear().getYear());
    }

}
