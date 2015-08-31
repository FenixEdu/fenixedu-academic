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
package org.fenixedu.academic.domain.accounting;

import java.math.BigDecimal;
import java.util.Comparator;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.paymentPlan.InstallmentBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class Installment extends Installment_Base {

    public static Comparator<Installment> COMPARATOR_BY_END_DATE = new Comparator<Installment>() {
        @Override
        public int compare(Installment leftInstallment, Installment rightInstallment) {
            int comparationResult = leftInstallment.getEndDate().compareTo(rightInstallment.getEndDate());
            return (comparationResult == 0) ? leftInstallment.getExternalId().compareTo(rightInstallment.getExternalId()) : comparationResult;
        }
    };

    public static Comparator<Installment> COMPARATOR_BY_ORDER = new Comparator<Installment>() {
        @Override
        public int compare(Installment leftInstallment, Installment rightInstallment) {
            int comparationResult = leftInstallment.getInstallmentOrder().compareTo(rightInstallment.getInstallmentOrder());
            return (comparationResult == 0) ? leftInstallment.getExternalId().compareTo(rightInstallment.getExternalId()) : comparationResult;
        }
    };

    protected Installment() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        super.setWhenCreated(new DateTime());
    }

    public Installment(final PaymentPlan paymentPlan, final Money amount, YearMonthDay startDate, YearMonthDay endDate) {
        this();
        init(paymentPlan, amount, startDate, endDate);
    }

    protected void init(final PaymentPlan paymentPlan, final Money amount, YearMonthDay startDate, YearMonthDay endDate) {

        checkParameters(paymentPlan, amount, startDate, endDate);

        super.setInstallmentOrder(paymentPlan.getLastInstallmentOrder() + 1);
        super.setPaymentPlan(paymentPlan);
        super.setAmount(amount);
        super.setStartDate(startDate);
        super.setEndDate(endDate);
    }

    protected void checkParameters(PaymentPlan paymentPlan, Money amount, YearMonthDay startDate, YearMonthDay endDate) {

        if (paymentPlan == null) {
            throw new DomainException("error.accounting.Installment.paymentCondition.cannot.be.null");
        }

        checkParameters(amount, startDate, endDate);
    }

    private void checkParameters(Money amount, YearMonthDay startDate, YearMonthDay endDate) {
        if (amount == null) {
            throw new DomainException("error.accounting.Installment.amount.cannot.be.null");
        }

        if (startDate == null) {
            throw new DomainException("error.accounting.enclosing_type.startDate.cannot.be.null");
        }

        if (endDate == null) {
            throw new DomainException("error.accounting.Installment.endDate.cannot.be.null");
        }
    }

    @Override
    public void setPaymentPlan(PaymentPlan paymentPlan) {
        throw new DomainException("error.accounting.Installment.cannot.modify.paymentPlan");
    }

    @Override
    public void setAmount(Money amount) {
        throw new DomainException("error.accounting.Installment.cannot.modify.amount");
    }

    @Override
    public void setStartDate(YearMonthDay startDate) {
        throw new DomainException("error.accounting.Installment.cannot.modify.startDate");
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
        throw new DomainException("error.accounting.Installment.cannot.modify.endDate");
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
        throw new DomainException("error.accounting.installments.InstallmentWithMonthlyPenalty.cannot.modify.whenCreated");
    }

    @Override
    public void setInstallmentOrder(Integer order) {
        throw new DomainException("error.accounting.Installment.cannot.modify.installmentOrder");
    }

    public Money calculateAmount(Event event, DateTime when, BigDecimal discountPercentage, boolean applyPenalty) {
        return calculateAmountWithDiscount(event, discountPercentage);
    }

    protected Money calculateAmountWithDiscount(Event event, BigDecimal discountPercentage) {
        return calculateBaseAmount(event).multiply(BigDecimal.ONE.subtract(discountPercentage));
    }

    protected Money calculateBaseAmount(Event event) {
        return getAmount();
    }

    public int getOrder() {
        return getInstallmentOrder().intValue();
    }

    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(Bundle.APPLICATION, "label.Installment.description", getInstallmentOrder().toString(),
                getStartDate().toString("dd/MM/yyyy"), getEndDate().toString("dd/MM/yyyy"));

        return labelFormatter;

    }

    public boolean isWithMonthlyPenalty() {
        return false;
    }

    public boolean isForFirstTimeStudents() {
        return false;
    }

    public void delete() {
        super.setPaymentPlan(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public LocalDate getEndDate(final Event event) {
        return super.getEndDate().toLocalDate();
    }

    public void edit(final InstallmentBean bean) {
        Money amount = bean.getAmount();
        YearMonthDay startDate = bean.getStartDate();
        YearMonthDay endDate = bean.getEndDate();

        checkParameters(amount, startDate, endDate);

        super.setAmount(amount);
        super.setStartDate(startDate);
        super.setEndDate(endDate);
    }

}
