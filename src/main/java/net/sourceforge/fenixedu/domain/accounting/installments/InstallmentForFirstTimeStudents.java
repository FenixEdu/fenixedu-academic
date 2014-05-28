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
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class InstallmentForFirstTimeStudents extends InstallmentForFirstTimeStudents_Base {

    protected InstallmentForFirstTimeStudents() {
        super();
    }

    public InstallmentForFirstTimeStudents(final PaymentPlan paymentPlan, final Money amount, final YearMonthDay startDate,
            final YearMonthDay endDate, final BigDecimal penaltyPercentage, final Integer maxMonthsToApplyPenalty,
            final Integer numberOfDaysToStartApplyingPenalty) {
        this();
        init(paymentPlan, amount, startDate, endDate, penaltyPercentage, maxMonthsToApplyPenalty,
                numberOfDaysToStartApplyingPenalty);
    }

    protected void init(final PaymentPlan paymentPlan, final Money amount, final YearMonthDay startDate,
            final YearMonthDay endDate, final BigDecimal penaltyPercentage, final Integer maxMonthsToApplyPenalty,
            final Integer numberOfDaysToStartApplyingPenalty) {

        super.init(paymentPlan, amount, startDate, endDate, penaltyPercentage);
        checkParameters(maxMonthsToApplyPenalty, numberOfDaysToStartApplyingPenalty);

        super.setMaxMonthsToApplyPenalty(maxMonthsToApplyPenalty);
        super.setNumberOfDaysToStartApplyingPenalty(numberOfDaysToStartApplyingPenalty);
    }

    private void checkParameters(final Integer maxMonthsToApplyPenalty, final Integer numberOfDaysToStartApplyingPenalty) {

        if (maxMonthsToApplyPenalty == null) {
            throw new DomainException(
                    "error.accounting.installments.InstallmentForFirstTimeStudents.maxMonthsToApplyPenalty.cannot.be.null");
        }

        if (maxMonthsToApplyPenalty <= 0) {
            throw new DomainException(
                    "error.accounting.installments.InstallmentForFirstTimeStudents.maxMonthsToApplyPenalty.cannot.be.null");
        }

        if (numberOfDaysToStartApplyingPenalty == null) {
            throw new DomainException(
                    "error.accounting.installments.InstallmentForFirstTimeStudents.numberOfDaysToStartApplyingPenalty.cannot.be.null");
        }

        if (numberOfDaysToStartApplyingPenalty <= 0) {
            throw new DomainException(
                    "error.accounting.installments.InstallmentForFirstTimeStudents.numberOfDaysToStartApplyingPenalty.must.be.greater.than.zero");
        }

    }

    @Override
    public YearMonthDay getWhenStartToApplyPenalty() {
        throw new DomainException("error.InstallmentForFirstTimeStudents.unsupported.operation");
    }

    @Override
    protected Money calculatePenaltyAmount(final Event event, final DateTime when, final BigDecimal discountPercentage) {
        if (when.toDateMidnight().compareTo(getWhenStartToApplyPenalty(event, when)) >= 0) {
            return new Money(calculateMonthPenalty(event, discountPercentage).multiply(
                    new BigDecimal(getNumberOfMonthsToChargePenalty(event, when))));
        } else {
            return Money.ZERO;
        }
    }

    private DateMidnight getWhenStartToApplyPenalty(final Event event, final DateTime when) {
        final GratuityEvent gratuityEvent = (GratuityEvent) event;
        final DateMidnight startDate = gratuityEvent.getRegistration().getStartDate().toDateMidnight();
        return startDate.plusDays(getNumberOfDaysToStartApplyingPenalty()).plusMonths(1).withDayOfMonth(1);
    }

    private int getNumberOfMonthsToChargePenalty(final Event event, final DateTime when) {
        final int numberOfMonths = (new Period(getWhenStartToApplyPenalty(event, when), when.toDateMidnight()).getMonths() + 1);
        return numberOfMonths < getMaxMonthsToApplyPenalty() ? numberOfMonths : getMaxMonthsToApplyPenalty();
    }

    @Override
    public LabelFormatter getDescription() {
        return new LabelFormatter().appendLabel("application", "label.InstallmentForFirstTimeStudents.description",
                getInstallmentOrder().toString(), getStartDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT), getEndDate()
                        .toString(DateFormatUtil.DEFAULT_DATE_FORMAT), getPenaltyPercentage().multiply(BigDecimal.valueOf(100))
                        .toString(), getNumberOfDaysToStartApplyingPenalty().toString());

    }

    @Override
    public boolean isForFirstTimeStudents() {
        return true;
    }

    @Override
    public LocalDate getEndDate(final Event event) {
        final GratuityEvent gratuityEvent = (GratuityEvent) event;
        final LocalDate startDate = gratuityEvent.getRegistration().getStartDate().toLocalDate();

        return startDate.plusDays(getNumberOfDaysToStartApplyingPenalty());
    }

    @Override
    public void edit(InstallmentBean bean) {
        Integer numberOfDaysToStartApplyingPenalty = bean.getNumberOfDaysToStartApplyingPenalty();
        Integer maxMonthsToApplyPenalty = bean.getMaxMonthsToApplyPenalty();

        checkParameters(maxMonthsToApplyPenalty, numberOfDaysToStartApplyingPenalty);

        super.setNumberOfDaysToStartApplyingPenalty(numberOfDaysToStartApplyingPenalty);

        super.edit(bean);
    }

    @Deprecated
    public boolean hasNumberOfDaysToStartApplyingPenalty() {
        return getNumberOfDaysToStartApplyingPenalty() != null;
    }

}
