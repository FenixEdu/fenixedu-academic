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
package net.sourceforge.fenixedu.applicationTier.Servico.student.reports;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentDataShareAuthorization;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class GenerateStudentReport implements Serializable {

    public static class StudentReportPredicate implements Serializable {

        private ExecutionYear executionYear;

        private DegreeType degreeType;

        private boolean concluded = false;

        private boolean active = false;

        {
            setExecutionYear(ExecutionYear.readCurrentExecutionYear());
        }

        public ExecutionYear getExecutionYear() {
            return executionYear;
        }

        public void setExecutionYear(final ExecutionYear executionYear) {
            this.executionYear = executionYear;
        }

        public DegreeType getDegreeType() {
            return degreeType;
        }

        public void setDegreeType(DegreeType degreeType) {
            this.degreeType = degreeType;
        }

        public boolean getConcluded() {
            return concluded;
        }

        public void setConcluded(boolean concluded) {
            this.concluded = concluded;
        }

        public boolean getActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean getAreRequiredFieldsFilledOut() {
            return (getActive() || getConcluded()) && getExecutionYear() != null;
        }

        public boolean applyFor(final DegreeType forDegreeType) {
            final DegreeType degreeType = getDegreeType();
            return degreeType == null || degreeType == forDegreeType;
        }

        public boolean applyFor(final Registration registration) {
            final ExecutionYear executionYear = getExecutionYear();
            final RegistrationState registrationState = registration.getLastRegistrationState(executionYear);
            if (registrationState == null) {
                return false;
            }
            final RegistrationStateType registrationStateType = registrationState.getStateType();
            return (getActive() && registrationStateType.isActive())
                    || (getConcluded() && registrationStateType == RegistrationStateType.CONCLUDED && executionYear
                            .containsDate(registrationState.getStateDate()));
        }

    }

    public static Spreadsheet generateReport(final StudentReportPredicate studentReportPredicate) {
        final Spreadsheet spreadsheet = new Spreadsheet("StudentDataAuthorizations");
        addHeaders(spreadsheet);
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            final DegreeType degreeType = degree.getDegreeType();
            if (studentReportPredicate.applyFor(degreeType)) {
                processDegree(spreadsheet, studentReportPredicate, degree, executionYear);
            }
        }
        return spreadsheet;
    }

    private static void addHeaders(final Spreadsheet spreadsheet) {
        spreadsheet.setHeader("Número");
        spreadsheet.setHeader("Nome");
        spreadsheet.setHeader("Curso");
        spreadsheet.setHeader("Ramo");
        spreadsheet.setHeader("Ano Curricular");
        spreadsheet.setHeader("Média");
        spreadsheet.setHeader("Ano Léctivo de Conclusão");
        spreadsheet.setHeader("Morada");
        spreadsheet.setHeader("Localidade");
        spreadsheet.setHeader("Código Postal");
        spreadsheet.setHeader("Localidade do Código Postal");
        spreadsheet.setHeader("Telefone");
        spreadsheet.setHeader("Telemovel");
        spreadsheet.setHeader("E-mail");
        spreadsheet.setHeader("Autorização");
    }

    private static void processDegree(final Spreadsheet spreadsheet, final StudentReportPredicate studentReportPredicate,
            final Degree degree, final ExecutionYear executionYear) {
        for (final Registration registration : degree.getRegistrationsSet()) {
            if (studentReportPredicate.applyFor(registration)) {
                final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
                if (studentCurricularPlan != null) {
                    processStudentCurricularPlan(spreadsheet, studentCurricularPlan, executionYear);
                }
            }
        }
    }

    private static void processStudentCurricularPlan(final Spreadsheet spreadsheet,
            final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
        final Registration registration = studentCurricularPlan.getRegistration();
        final Student student = registration.getStudent();
        final Person person = student.getPerson();
        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        final Degree degree = degreeCurricularPlan.getDegree();
        final Branch branch = studentCurricularPlan.getBranch();
        StudentDataShareAuthorization dataAccess =
                student.getPersonalDataAuthorizationAt(executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight());
        final Row row = spreadsheet.addRow();
        row.setCell(student.getNumber().toString());
        row.setCell(person.getName());
        row.setCell(degree.getDegreeType().getLocalizedName() + " "
                + degree.getNameFor(registration.getStartExecutionYear()).getContent());
        row.setCell(branch == null ? "" : branch.getName());
        row.setCell("" + registration.getCurricularYear(executionYear));
        row.setCell("" + registration.getAverage(executionYear));
        if (registration.isConcluded()) {
            row.setCell(executionYear.getYear());
        } else {
            row.setCell("");
        }
        row.setCell(person.getAddress());
        row.setCell(person.getArea());
        row.setCell(person.getAreaCode());
        row.setCell(person.getAreaOfAreaCode());
        row.setCell(person.getDefaultPhoneNumber());
        row.setCell(person.getDefaultMobilePhoneNumber());
        row.setCell(person.getEmail());
        row.setCell(dataAccess != null ? dataAccess.getAuthorizationChoice().getDescription() : StudentPersonalDataAuthorizationChoice.NO_END
                .getDescription());
    }

}
