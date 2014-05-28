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

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class TutorshipProgramReportFile extends TutorshipProgramReportFile_Base {

    public TutorshipProgramReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem para programa tutorado";
    }

    @Override
    protected String getPrefix() {
        return "tutorshipProgram";
    }

    @Override
    public void renderReport(final Spreadsheet spreadsheet) throws Exception {
        spreadsheet.setHeader("Número");
        spreadsheet.setHeader("Sexo");
        spreadsheet.setHeader("Média");
        spreadsheet.setHeader("Média Anual");
        spreadsheet.setHeader("Número Inscrições");
        spreadsheet.setHeader("Número Aprovações");
        spreadsheet.setHeader("Nota de Seriação");
        spreadsheet.setHeader("Local de Origem");

        final ExecutionYear executionYear = getExecutionYear();
        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (checkDegreeType(getDegreeType(), degree)) {
                if (isActive(degree)) {
                    for (final Registration registration : degree.getRegistrationsSet()) {
                        if (registration.isRegistered(getExecutionYear())) {

                            int enrolmentCounter = 0;
                            int aprovalCounter = 0;
                            BigDecimal bigDecimal = null;
                            double totalCredits = 0;

                            for (final Registration otherRegistration : registration.getStudent().getRegistrationsSet()) {
                                if (otherRegistration.getDegree() == registration.getDegree()) {
                                    for (final StudentCurricularPlan studentCurricularPlan : otherRegistration
                                            .getStudentCurricularPlansSet()) {
                                        for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                                            final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
                                            if (executionSemester.getExecutionYear() == executionYear) {
                                                enrolmentCounter++;
                                                if (enrolment.isApproved()) {
                                                    aprovalCounter++;
                                                    final Grade grade = enrolment.getGrade();
                                                    if (grade.isNumeric()) {
                                                        final double credits =
                                                                enrolment.getEctsCreditsForCurriculum().doubleValue();
                                                        totalCredits += credits;
                                                        bigDecimal =
                                                                bigDecimal == null ? grade.getNumericValue().multiply(
                                                                        new BigDecimal(credits)) : bigDecimal.add(grade
                                                                        .getNumericValue().multiply(new BigDecimal(credits)));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            final Row row = spreadsheet.addRow();
                            row.setCell(registration.getNumber().toString());
                            row.setCell(registration.getPerson().getGender().toLocalizedString());
                            row.setCell(registration.getAverage(executionYear));
                            if (bigDecimal == null) {
                                row.setCell("");
                            } else {
                                row.setCell(bigDecimal.divide(new BigDecimal(totalCredits), 5, RoundingMode.HALF_UP));
                            }
                            row.setCell(Integer.toString(enrolmentCounter));
                            row.setCell(Integer.toString(aprovalCounter));
                            row.setCell(registration.getEntryGrade() != null ? registration.getEntryGrade().toString() : StringUtils.EMPTY);
                            Boolean dislocated = null;
                            if (registration.hasStudentCandidacy()) {
                                dislocated = registration.getStudentCandidacy().getDislocatedFromPermanentResidence();
                            }

                            final String dislocatedString =
                                    dislocated == null ? "" : (dislocated.booleanValue() ? "Deslocado" : "Não Deslocado");
                            row.setCell(dislocatedString);
                        }
                    }
                }
            }
        }
    }

}
