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
package net.sourceforge.fenixedu.domain.accounting.report.events;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.phd.debts.ExternalScholarshipPhdGratuityContribuitionEvent;
import net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityEvent;
import net.sourceforge.fenixedu.domain.student.Student;

public class ExternalScholarshipPhdGratuityContribuitionEventWrapper implements Wrapper {
    private final ExternalScholarshipPhdGratuityContribuitionEvent event;

    public ExternalScholarshipPhdGratuityContribuitionEventWrapper(Event event) {
        this.event = (ExternalScholarshipPhdGratuityContribuitionEvent) event;
    }

    private PhdGratuityEvent getPhdGratuityEvent() {
        PhdGratuityEvent phdGratuityEvent = (PhdGratuityEvent) this.event.getPhdGratuityExternalScholarshipExemption().getEvent();

        return phdGratuityEvent;
    }

    private Student getStudent() {
        return getPhdGratuityEvent().getPerson().getStudent();
    }

    @Override
    public String getStudentNumber() {
        Student student = getStudent();

        return student.getNumber().toString();
    }

    @Override
    public String getStudentName() {
        return getStudent().getPerson().getName();
    }

    @Override
    public String getStudentEmail() {
        return getStudent().getPerson().getDefaultEmailAddressValue();
    }

    @Override
    public String getRegistrationStartDate() {
        return "";
    }

    @Override
    public String getExecutionYear() {
        return getForExecutionYear().getName();
    }

    @Override
    public String getDegreeName() {
        return "";
    }

    @Override
    public String getDegreeType() {
        return "";
    }

    @Override
    public String getPhdProgramName() {
        PhdGratuityEvent phdGratuityEvent = getPhdGratuityEvent();

        return phdGratuityEvent.getPhdIndividualProgramProcess().getPhdProgram().getName().getContent();
    }

    @Override
    public String getEnrolledECTS() {
        return "";
    }

    @Override
    public String getRegime() {
        return "";
    }

    @Override
    public String getEnrolmentModel() {
        return "";
    }

    @Override
    public String getResidenceYear() {
        return "";
    }

    @Override
    public String getResidenceMonth() {
        return "";
    }

    @Override
    public String getStudiesType() {
        return Wrapper.PHD_PROGRAM_STUDIES;
    }

    @Override
    public String getTotalDiscount() {
        return "";
    }

    @Override
    public boolean isAfterOrEqualExecutionYear(ExecutionYear executionYear) {
        return getForExecutionYear().isAfterOrEquals(executionYear);
    }

    @Override
    public ExecutionYear getForExecutionYear() {
        return ExecutionYear.readByDateTime(event.getEventStateDate());
    }

    @Override
    public AdministrativeOffice getRelatedAcademicOffice() {
        return getPhdGratuityEvent().getPhdIndividualProgramProcess().getAdministrativeOffice();
    }

    @Override
    public String getRelatedEventExternalId() {
        return getPhdGratuityEvent().getExternalId();
    }

    @Override
    public String getDebtorFiscalId() {
        return event.getParty().getSocialSecurityNumber();
    }

    @Override
    public String getDebtorName() {
        return event.getParty().getName();
    }

}
