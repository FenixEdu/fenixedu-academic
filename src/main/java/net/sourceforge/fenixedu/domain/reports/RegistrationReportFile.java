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

import java.util.Collections;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

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

                            if (registration.getRegistrationAgreement() != null) {
                                row.setCell(registration.getRegistrationAgreement().getName());
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
        final Ingression ingression = registration.getIngression();
        if (ingression != null) {
            row.setCell(ingression.getName());
            row.setCell(ingression.getDescription());
        } else {
            row.setCell("");
            row.setCell("");
        }
        row.setCell(registration.getStartExecutionYear().getYear());
    }

}
