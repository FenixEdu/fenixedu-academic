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
package net.sourceforge.fenixedu.domain.accounting.installments;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.InstallmentBean;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

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

        if (maxMonthsToApplyPenalty == null) {
            throw new DomainException(
                    "error.accounting.installments.InstallmentWithMonthlyPenalty.maxMonthsToApplyPenalty.cannot.be.null");
        }

        if (maxMonthsToApplyPenalty <= 0) {
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
        final int numberOfMonths =
                (new Period(getWhenStartToApplyPenalty().withDayOfMonth(1).toDateMidnight(), when.toDateMidnight()).getMonths() + 1);
        return numberOfMonths < getMaxMonthsToApplyPenalty() ? numberOfMonths : getMaxMonthsToApplyPenalty();
    }

    @Override
    public boolean isWithMonthlyPenalty() {
        return true;
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel("application", "label.InstallmentWithMonthlyPenalty.description", getInstallmentOrder()
                .toString(), getStartDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT),
                getEndDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT),
                getPenaltyPercentage().multiply(BigDecimal.valueOf(100)).toString(),
                getWhenStartToApplyPenalty().toString(DateFormatUtil.DEFAULT_DATE_FORMAT));

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

    @Deprecated
    public boolean hasMaxMonthsToApplyPenalty() {
        return getMaxMonthsToApplyPenalty() != null;
    }

    @Deprecated
    public boolean hasWhenStartToApplyPenalty() {
        return getWhenStartToApplyPenalty() != null;
    }

}
