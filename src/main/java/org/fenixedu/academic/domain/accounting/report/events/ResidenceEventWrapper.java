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
import org.fenixedu.academic.domain.accounting.ResidenceEvent;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;

public class ResidenceEventWrapper implements Wrapper {
    private final ResidenceEvent event;

    public ResidenceEventWrapper(ResidenceEvent event) {
        this.event = event;
    }

    @Override
    public String getStudentNumber() {
        if (event.getPerson().getStudent() != null) {
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
        return "--";
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
        return event.getResidenceMonth().getYear().getYear().toString();
    }

    @Override
    public String getResidenceMonth() {
        return event.getResidenceMonth().getMonth().getName();
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ExecutionYear getForExecutionYear() {
        return null;
    }

    @Override
    public AdministrativeOffice getRelatedAcademicOffice() {
        return null;
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
