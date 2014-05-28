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
/*
 * Created on 5/Jan/2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.GratuitySituationPaymentCode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author Tânia Pousão
 */
public class GratuitySituation extends GratuitySituation_Base {

    // TODO: this should be configurable
    private static final BigDecimal PENALTY_PERCENTAGE = new BigDecimal("0.01");

    public GratuitySituation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public GratuitySituation(GratuityValues gratuityValues, StudentCurricularPlan studentCurricularPlan) {

        this();
        setGratuityValues(gratuityValues);
        setStudentCurricularPlan(studentCurricularPlan);
        setWhenDateTime(new DateTime());

        updateValues();
    }

    public void delete() {
        setStudentCurricularPlan(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public void updateValues() {

        updateTotalValue();
        updateRemainingValue();
    }

    private void updateTotalValue() {
        super.setTotalValue(calculateTotalAmountWithPenalty());
    }

    private double calculateTotalAmountWithPenalty() {
        return calculateOriginalTotalValue() + calculatePenalty(calculateOriginalTotalValue());
    }

    private double calculateOriginalTotalValue() {
        double totalValue = 0.0;

        Specialization specialization = getStudentCurricularPlan().getSpecialization();
        if (specialization.equals(Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION)) {
            totalValue = this.getGratuityValues().calculateTotalValueForSpecialization(this.getStudentCurricularPlan());
        } else if (specialization.equals(Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE)) {
            totalValue = this.getGratuityValues().calculateTotalValueForMasterDegree();
        }

        return totalValue;
    }

    private void updateRemainingValue() {
        super.setRemainingValue(getTotalValue() - calculatePayedValue() - calculateExemptionValue());
    }

    private double calculateExemptionValue() {
        double exemptionValue = 0;
        if (this.getExemptionPercentage() != null) {
            exemptionValue = getTotalValue() * (this.getExemptionPercentage().doubleValue() / 100.0);
        }

        if (this.getExemptionValue() != null) {
            exemptionValue += this.getExemptionValue().doubleValue();
        }
        return exemptionValue;
    }

    public double calculatePayedValue() {
        return calculatePayedValue(null);
    }

    public double calculatePayedValue(final YearMonthDay date) {
        final Collection<GratuityTransaction> transactions = (date == null ? getTransactionList() : getTransactionsUntil(date));
        BigDecimal result = BigDecimal.ZERO;
        for (final GratuityTransaction transaction : transactions) {
            result = result.add(transaction.getValueWithAdjustment());
        }

        return result.doubleValue();
    }

    private List<GratuityTransaction> getTransactionsUntil(final YearMonthDay date) {
        final List<GratuityTransaction> result = new ArrayList<GratuityTransaction>();

        for (final GratuityTransaction gratuityTransaction : getTransactionList()) {
            if (gratuityTransaction.getTransactionDateDateTime().toYearMonthDay().compareTo(date) <= 0) {
                result.add(gratuityTransaction);
            }
        }

        return result;
    }

    public GratuitySituationPaymentCode calculatePaymentCode() {
        updateValues();

        if (!hasPaymentCode()) {
            createPaymentCode();
        } else {
            updatePaymentCode();
        }

        return super.getPaymentCode();
    }

    private void updatePaymentCode() {
        super.getPaymentCode().update(new YearMonthDay(), calculatePaymentCodeEndDate(), new Money(getRemainingValue()),
                new Money(getRemainingValue()));
    }

    private void createPaymentCode() {
        GratuitySituationPaymentCode
                .create(PaymentCodeType.PRE_BOLONHA_MASTER_DEGREE_TOTAL_GRATUITY, new YearMonthDay(),
                        calculatePaymentCodeEndDate(), new Money(getRemainingValue()), new Money(getRemainingValue()),
                        getStudent(), this);
    }

    private Student getStudent() {
        return getStudentCurricularPlan().getRegistration().getStudent();
    }

    private Person getPerson() {
        return getStudent().getPerson();
    }

    @Override
    public GratuitySituationPaymentCode getPaymentCode() {
        throw new DomainException("error.GratuitySituation.paymentCode.cannot.be.accessed.directly");
    }

    public boolean hasPaymentCode() {
        return (super.getPaymentCode() != null);
    }

    private double calculatePenalty(final double remainingValue) {
        if (hasPenalty() && getGratuityValues().isPenaltyApplicable()) {

            int monthsToChargePenalty = 0;
            final YearMonthDay now = new YearMonthDay();

            if (getEndPaymentDate().getMonthOfYear() == now.getMonthOfYear()) {
                monthsToChargePenalty += 1;
            } else {
                monthsToChargePenalty = calculateMonthsToChargePenalty(calculateCloseDate());
            }

            return new Money(remainingValue).multiply(PENALTY_PERCENTAGE).multiply(new BigDecimal(monthsToChargePenalty))
                    .getAmount().doubleValue();
        }

        return 0;
    }

    private int calculateMonthsToChargePenalty(final YearMonthDay when) {

        final YearMonthDay endPaymentDate = getEndPaymentDate();
        final YearMonthDay endOfMonth = endPaymentDate.plusMonths(1).withDayOfMonth(1).minusDays(1);
        final int numberOfDaysLeftInMonth = new Period(endPaymentDate, endOfMonth).getDays();
        int monthsToChargePenalty = 0;

        if (numberOfDaysLeftInMonth > 0) {
            monthsToChargePenalty += 1;
        }

        final Period period = new Period(endPaymentDate.plusMonths(1).withDayOfMonth(1), when);
        monthsToChargePenalty += period.getMonths() + 1;

        return monthsToChargePenalty;
    }

    private YearMonthDay calculateCloseDate() {
        for (final GratuityTransaction transaction : getTransactionListSet()) {
            if (canClose(transaction.getTransactionDateDateTime().toYearMonthDay())) {
                return transaction.getTransactionDateDateTime().toYearMonthDay();
            }
        }

        return new YearMonthDay();

    }

    private boolean canClose(final YearMonthDay when) {
        if (calculatePayedAmountUntil(when) > calculateOriginalValueWithExemption()) {
            return true;
        } else {
            final int monthsToChargePenalty = calculateMonthsToChargePenalty(when);
            final double penaltyAmount =
                    new Money(calculateOriginalValueWithExemption()).multiply(PENALTY_PERCENTAGE)
                            .multiply(new BigDecimal(monthsToChargePenalty)).getAmount().doubleValue();

            return calculatePayedAmountUntil(when) > (penaltyAmount + calculateOriginalValueWithExemption());
        }

    }

    private double calculatePayedAmountUntil(final YearMonthDay endDate) {
        Money result = Money.ZERO;
        final DateTime limit = endDate.toDateTimeAtMidnight().plusDays(1).minusMinutes(1);
        for (final GratuityTransaction transaction : getTransactionListSet()) {
            if (!transaction.getTransactionDateDateTime().isAfter(limit)) {
                result = result.add(transaction.getValueWithAdjustment());
            }
        }

        return result.getAmount().doubleValue();
    }

    private boolean hasPenalty() {
        if (!getHasPenaltyExemption() && new YearMonthDay().isAfter(getEndPaymentDate())) {
            final double payedValue = calculatePayedValue(getEndPaymentDate());
            return payedValue < calculateOriginalValueWithExemption();
        }

        return false;

    }

    private double calculateOriginalValueWithExemption() {
        return (calculateOriginalTotalValue() - calculateExemptionValue());
    }

    @Override
    public Boolean getHasPenaltyExemption() {
        return super.getHasPenaltyExemption() != null && super.getHasPenaltyExemption();
    }

    private YearMonthDay calculatePaymentCodeEndDate() {
        final YearMonthDay now = new YearMonthDay();
        final YearMonthDay endPaymentDate = getEndPaymentDate();
        return now.isAfter(endPaymentDate) ? calculateNextEndDate(now) : endPaymentDate;
    }

    protected YearMonthDay calculateNextEndDate(final YearMonthDay yearMonthDay) {
        final YearMonthDay nextMonth = yearMonthDay.plusMonths(1);
        return new YearMonthDay(nextMonth.getYear(), nextMonth.getMonthOfYear(), 1).minusDays(1);
    }

    private boolean isPayedUsingPhases() {
        return getGratuityValues().hasAnyPaymentPhaseList();
    }

    private YearMonthDay getEndPaymentDate() {
        if (isPayedUsingPhases()) {
            final YearMonthDay lastPaymentPhaseDate = getLastPaymentPhaseDate();
            if (getGratuityValues().getEndPaymentYearMonthDay() == null) {
                return lastPaymentPhaseDate;
            }

            return lastPaymentPhaseDate.isAfter(getGratuityValues().getEndPaymentYearMonthDay()) ? lastPaymentPhaseDate : getGratuityValues()
                    .getEndPaymentYearMonthDay();
        }

        if (getGratuityValues().getEndPaymentYearMonthDay() == null) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.GratuitySituation.paymentMode.is.not.correctly.defined");
        }

        return getGratuityValues().getEndPaymentYearMonthDay();
    }

    private YearMonthDay getLastPaymentPhaseDate() {
        final PaymentPhase paymentPhase = getGratuityValues().getLastPaymentPhase();
        return paymentPhase == null ? null : paymentPhase.getEndDateYearMonthDay();
    }

    public boolean isPayed() {
        return new Money(getRemainingValue()).lessOrEqualThan(Money.ZERO);
    }

    public GratuityTransaction processAmount(final Person responsiblePerson, final Money amount, final DateTime whenRegistered,
            final PaymentType paymentType) {

        final GratuityTransaction transaction =
                new GratuityTransaction(amount.getAmount(), whenRegistered, paymentType, TransactionType.GRATUITY_ADHOC_PAYMENT,
                        responsiblePerson, getOrCreatePersonAccount(), this);

        updateRemainingValue();

        return transaction;

    }

    private PersonAccount getOrCreatePersonAccount() {
        return getPerson().hasAssociatedPersonAccount() ? getPerson().getAssociatedPersonAccount() : new PersonAccount(
                getPerson());
    }

    @Override
    public void setTotalValue(Double value) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.GratuitySituation.cannot.modify.value");
    }

    @Override
    public void setRemainingValue(Double value) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.GratuitySituation.cannot.modify.value");
    }

    public void editPenaltyExemption(final Boolean hasPenaltyExemption, final String justification) {
        setHasPenaltyExemption(hasPenaltyExemption);
        if (hasPenaltyExemption != null && hasPenaltyExemption) {
            setPenaltyExemptionDate(new YearMonthDay());
            setPenaltyExemptionEmployee(AccessControl.getPerson().getEmployee());
            setPenaltyExemptionJustification(justification);
        } else {
            setPenaltyExemptionDate(null);
            setPenaltyExemptionEmployee(null);
            setPenaltyExemptionJustification(null);
        }
    }

    public Money getPayedAmountBetween(DateTime startDate, DateTime endDate) {
        Money result = Money.ZERO;

        for (final GratuityTransaction transaction : getTransactionList()) {
            if (!transaction.getTransactionDateDateTime().isBefore(startDate)
                    && !transaction.getTransactionDateDateTime().isAfter(endDate)) {
                result = result.add(transaction.getValueWithAdjustment());
            }
        }

        return result;
    }

    @Deprecated
    public java.util.Date getWhen() {
        org.joda.time.DateTime dt = getWhenDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setWhen(java.util.Date date) {
        if (date == null) {
            setWhenDateTime(null);
        } else {
            setWhenDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.transactions.GratuityTransaction> getTransactionList() {
        return getTransactionListSet();
    }

    @Deprecated
    public boolean hasAnyTransactionList() {
        return !getTransactionListSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasHasPenaltyExemption() {
        return getHasPenaltyExemption() != null;
    }

    @Deprecated
    public boolean hasStudentCurricularPlan() {
        return getStudentCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasEmployee() {
        return getEmployee() != null;
    }

    @Deprecated
    public boolean hasExemptionDescription() {
        return getExemptionDescription() != null;
    }

    @Deprecated
    public boolean hasExemptionType() {
        return getExemptionType() != null;
    }

    @Deprecated
    public boolean hasPenaltyExemptionDate() {
        return getPenaltyExemptionDate() != null;
    }

    @Deprecated
    public boolean hasTotalValue() {
        return getTotalValue() != null;
    }

    @Deprecated
    public boolean hasGratuityValues() {
        return getGratuityValues() != null;
    }

    @Deprecated
    public boolean hasWhenDateTime() {
        return getWhenDateTime() != null;
    }

    @Deprecated
    public boolean hasPenaltyExemptionEmployee() {
        return getPenaltyExemptionEmployee() != null;
    }

    @Deprecated
    public boolean hasRemainingValue() {
        return getRemainingValue() != null;
    }

    @Deprecated
    public boolean hasExemptionPercentage() {
        return getExemptionPercentage() != null;
    }

    @Deprecated
    public boolean hasPenaltyExemptionJustification() {
        return getPenaltyExemptionJustification() != null;
    }

    @Deprecated
    public boolean hasExemptionValue() {
        return getExemptionValue() != null;
    }

}
