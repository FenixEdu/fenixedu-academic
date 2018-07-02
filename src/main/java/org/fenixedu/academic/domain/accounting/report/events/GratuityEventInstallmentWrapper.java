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

import org.fenixedu.academic.domain.accounting.Installment;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class GratuityEventInstallmentWrapper implements InstallmentWrapper {

    private GratuityEventWithPaymentPlan event;
    private Installment installment;

    public GratuityEventInstallmentWrapper(final GratuityEventWithPaymentPlan event, final Installment installment) {
        this.event = event;
        this.installment = installment;
    }

    @Override
    public String getExpirationDateLabel() {
        String label =
                BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.org.fenixedu.academic.domain.accounting.report.events.EventReportQueueJob.installment.expiration.date");
        return String.format(label, this.installment.getOrder());
    }

    @Override
    public String getAmountToPayLabel() {
        String label =
                BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.org.fenixedu.academic.domain.accounting.report.events.EventReportQueueJob.installment.expiration.amountToPay");
        return String.format(label, this.installment.getOrder());
    }

    @Override
    public String getRemainingAmountLabel() {
        String label =
                BundleUtil
                        .getString(Bundle.ACADEMIC,
                                "label.org.fenixedu.academic.domain.accounting.report.events.EventReportQueueJob.installment.expiration.remainingAmount");
        return String.format(label, this.installment.getOrder());
    }

    @Override
    public String getExpirationDate() {
        return installment.getEndDate().toString("dd/MM/yyyy");
    }

    @Override
    public String getAmountToPay() {
        return installment.calculateAmount(event, installment.getStartDate().toLocalDate().toDateTimeAtStartOfDay(),
                BigDecimal.ZERO, event.getGratuityPaymentPlan().isToApplyPenalty(event, this.installment)).toPlainString();
    }

    @Override
    public String getRemainingAmount(final DateTime when) {
        return event.getDueDateAmountMap(when).getOrDefault(this.installment.getEndDate(event), Money.ZERO).toPlainString();
    }
}
