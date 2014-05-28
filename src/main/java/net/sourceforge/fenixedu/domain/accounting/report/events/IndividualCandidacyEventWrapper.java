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
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.IndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

public class IndividualCandidacyEventWrapper implements Wrapper {
    IndividualCandidacyEvent event;

    public IndividualCandidacyEventWrapper(IndividualCandidacyEvent event) {
        this.event = event;
    }

    @Override
    public String getStudentNumber() {
        if (event.getPerson().hasStudent()) {
            return event.getPerson().getStudent().getNumber().toString();
        }

        return "--";
    }

    @Override
    public String getStudentName() {
        return event.getPerson().getName();
    }

    @Override
    public String getStudentEmail() {
        return event.getPerson().getDefaultEmailAddressValue();
    }

    @Override
    public String getRegistrationStartDate() {
        return "--";
    }

    @Override
    public String getExecutionYear() {
        return getForExecutionYear().getName();
    }

    @Override
    public String getDegreeName() {
        return "--";
    }

    @Override
    public String getDegreeType() {
        return "--";
    }

    @Override
    public String getPhdProgramName() {
        return "--";
    }

    @Override
    public String getEnrolledECTS() {
        return "--";
    }

    @Override
    public String getRegime() {
        return "--";
    }

    @Override
    public String getEnrolmentModel() {
        return "--";
    }

    @Override
    public String getResidenceYear() {
        return "--";
    }

    @Override
    public String getResidenceMonth() {
        return "--";
    }

    @Override
    public String getStudiesType() {
        return "--";
    }

    @Override
    public String getTotalDiscount() {
        return event.getTotalDiscount().toPlainString();
    }

    @Override
    public boolean isAfterOrEqualExecutionYear(ExecutionYear executionYear) {
        return !ExecutionYear.readByDateTime(event.getIndividualCandidacy().getCandidacyDate()).isBefore(executionYear);
    }

    @Override
    public ExecutionYear getForExecutionYear() {
        return ExecutionYear.readByDateTime(event.getIndividualCandidacy().getCandidacyDate());
    }

    @Override
    public AdministrativeOffice getRelatedAcademicOffice() {
        return event.getAdministrativeOffice();
    }

    @Override
    public String getRelatedEventExternalId() {
        return "--";
    }

    @Override
    public String getDebtorFiscalId() {
        return "--";
    }

    @Override
    public String getDebtorName() {
        return "--";
    }

}
