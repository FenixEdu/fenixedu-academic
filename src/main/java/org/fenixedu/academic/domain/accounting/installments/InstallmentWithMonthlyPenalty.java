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
package org.fenixedu.academic.domain.accounting.installments;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.PaymentPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.paymentPlan.InstallmentBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

public class InstallmentWithMonthlyPenalty extends InstallmentWithMonthlyPenalty_Base {

    protected InstallmentWithMonthlyPenalty() {
        super();
    }

    public InstallmentWithMonthlyPenalty(final PaymentPlan paymentPlan, final Money amount, final YearMonthDay startDate,
            final YearMonthDay endDate, final BigDecimal penaltyPercentage, final YearMonthDay whenStartToApplyPenalty,
            final Integer maxMonthsToApplyPenalty) {
        this();
        init(paymentPlan, amount, startDate, endDate, penaltyPercentage, whenStartToApplyPenalty, maxMonthsToApplyPenalty);
    }

    protected void init(PaymentPlan paymentPlan, Money amount, YearMonthDay startDate, YearMonthDay endDate,
            BigDecimal penaltyPercentage, YearMonthDay whenStartToApplyPenalty, Integer maxMonthsToApplyPenalty) {

        super.init(paymentPlan, amount, startDate, endDate, penaltyPercentage);
        checkParameters(whenStartToApplyPenalty, maxMonthsToApplyPenalty);

        super.setWhenStartToApplyPenalty(whenStartToApplyPenalty);
        super.setMaxMonthsToApplyPenalty(maxMonthsToApplyPenalty);

    }

    private void checkParameters(YearMonthDay whenStartToApplyPenalty, Integer maxMonthsToApplyPenalty) {
        if (!(this instanceof InstallmentForFirstTimeStudents)) {
            if (whenStartToApplyPenalty == null) {
                throw new DomainException(
                        "error.accounting.installments.InstallmentWithMonthlyPenalty.whenStartToApplyPenalty.cannot.be.null");
            }
        }

        if (maxMonthsToApplyPenalty != null && maxMonthsToApplyPenalty <= 0) {
            throw new DomainException(
                    "error.accounting.installments.InstallmentWithMonthlyPenalty.maxMonthsToApplyPenalty.must.be.greater.than.zero");
        }

    }

    @Override
    protected Money calculatePenaltyAmount(Event event, DateTime when, BigDecimal discountPercentage) {
        if (when.toDateMidnight().compareTo(getWhenStartToApplyPenalty().toDateMidnight()) >= 0) {
            return new Money(calculateMonthPenalty(event, discountPercentage).multiply(
                    new BigDecimal(getNumberOfMonthsToChargePenalty(when))));
        } else {
            return Money.ZERO;
        }
    }

    protected BigDecimal calculateMonthPenalty(Event event, BigDecimal discountPercentage) {
        final BigDecimal amount = calculateAmountWithDiscount(event, discountPercentage).getAmount();
        amount.setScale(10);
        return amount.multiply(getPenaltyPercentage());
    }

    protected int getNumberOfMonthsToChargePenalty(DateTime when) {
        final Period period = new Period(getWhenStartToApplyPenalty().withDayOfMonth(1).toDateMidnight(), when.toDateMidnight());
        final int numberOfMonths = (period.getYears() * 12) + (period.getMonths() + 1);
        if (getMaxMonthsToApplyPenalty() == null) {
            return numberOfMonths;
        } else {
            return numberOfMonths < getMaxMonthsToApplyPenalty() ? numberOfMonths : getMaxMonthsToApplyPenalty();
        }
    }

    @Override
    public boolean isWithMonthlyPenalty() {
        return true;
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(Bundle.APPLICATION, "label.InstallmentWithMonthlyPenalty.description", getInstallmentOrder()
                .toString(), getStartDate().toString("dd/MM/yyyy"), getEndDate().toString("dd/MM/yyyy"), getPenaltyPercentage()
                .multiply(BigDecimal.valueOf(100)).toString(), getWhenStartToApplyPenalty().toString("dd/MM/yyyy"));

        return labelFormatter;
    }

    @Override
    public void edit(final InstallmentBean bean) {
        Integer maxMonthsToApplyPenalty = bean.getMaxMonthsToApplyPenalty();
        YearMonthDay whenStartToApplyPenalty = bean.getWhenToStartApplyPenalty();
        BigDecimal penaltyPercentage = bean.getMontlyPenaltyPercentage();

        checkParameters(whenStartToApplyPenalty, maxMonthsToApplyPenalty);

        super.setMaxMonthsToApplyPenalty(maxMonthsToApplyPenalty);

        if (!(this instanceof InstallmentForFirstTimeStudents)) {
            super.setWhenStartToApplyPenalty(whenStartToApplyPenalty);
        }
        super.setPenaltyPercentage(penaltyPercentage);

        super.edit(bean);
    }

}
