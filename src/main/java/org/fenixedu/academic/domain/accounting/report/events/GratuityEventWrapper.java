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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.Installment;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.student.EnrolmentModel;
import org.fenixedu.academic.util.MultiLanguageString;

public class GratuityEventWrapper implements Wrapper {

    private final GratuityEvent event;

    public GratuityEventWrapper(final GratuityEvent event) {
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
        return event.getRegistration().getStartDate().toString("dd/MM/yyyy");
    }

    @Override
    public String getExecutionYear() {
        return getForExecutionYear().getName();
    }

    @Override
    public String getDegreeName() {
        return event.getRegistration().getDegree().getNameI18N().getContent(MultiLanguageString.pt);
    }

    @Override
    public String getDegreeType() {
        return event.getRegistration().getDegreeType().getName().getContent();
    }

    @Override
    public String getPhdProgramName() {
        return "--";
    }

    @Override
    public String getEnrolledECTS() {
        return new BigDecimal(event.getRegistration().getLastStudentCurricularPlan()
                .getEnrolmentsEctsCredits(event.getExecutionYear())).toString();
    }

    @Override
    public String getRegime() {
        return event.getRegistration().getRegimeType(event.getExecutionYear()).getLocalizedName();
    }

    @Override
    public String getEnrolmentModel() {
        if (event.isDfaGratuityEvent()) {
            EnrolmentModel enrolmentModelForExecutionYear =
                    event.getRegistration().getEnrolmentModelForExecutionYear(event.getExecutionYear());
            if (enrolmentModelForExecutionYear != null) {
                return enrolmentModelForExecutionYear.getLocalizedName();
            }
        }

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
        return REGISTRATION_STUDIES;
    }

    @Override
    public String getTotalDiscount() {
        return event.getTotalDiscount().toPlainString();
    }

    @Override
    public boolean isAfterOrEqualExecutionYear(ExecutionYear executionYear) {
        return !event.getExecutionYear().isBefore(executionYear);
    }

    @Override
    public ExecutionYear getForExecutionYear() {
        return event.getExecutionYear();
    }

    @Override
    public AdministrativeOffice getRelatedAcademicOffice() {
        return event.getAdministrativeOffice();
    }

    public List<InstallmentWrapper> getInstallments() {
        List<InstallmentWrapper> wrappers = new ArrayList<InstallmentWrapper>();

        if (this.event.isGratuityEventWithPaymentPlan()) {
            GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan = (GratuityEventWithPaymentPlan) this.event;
            List<Installment> installments = gratuityEventWithPaymentPlan.getInstallments();

            for (Installment installment : installments) {
                wrappers.add(new GratuityEventInstallmentWrapper(gratuityEventWithPaymentPlan, installment));
            }
        }

        return wrappers;
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
