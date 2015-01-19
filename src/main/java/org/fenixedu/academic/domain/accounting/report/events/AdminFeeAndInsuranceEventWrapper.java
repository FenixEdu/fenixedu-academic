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
package org.fenixedu.academic.domain.accounting.report.events;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;

public class AdminFeeAndInsuranceEventWrapper implements Wrapper {
    private final AdministrativeOfficeFeeAndInsuranceEvent event;

    public AdminFeeAndInsuranceEventWrapper(AdministrativeOfficeFeeAndInsuranceEvent event) {
        this.event = event;
    }

    @Override
    public String getStudentNumber() {
        return event.getPerson().getStudent().getNumber().toString();
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
        ExecutionYear executionYear = getForExecutionYear();
        return executionYear.getName();
    }

    @Override
    public ExecutionYear getForExecutionYear() {
        return event.getExecutionYear();
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
        return !getForExecutionYear().isBefore(executionYear);
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
