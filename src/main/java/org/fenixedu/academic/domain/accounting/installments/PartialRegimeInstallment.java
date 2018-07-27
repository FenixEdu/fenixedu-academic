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
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.accounting.paymentPlans.FullGratuityPaymentPlanForPartialRegime;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.paymentPlan.InstallmentBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class PartialRegimeInstallment extends PartialRegimeInstallment_Base {

    protected PartialRegimeInstallment() {
        super();
    }

    public PartialRegimeInstallment(final FullGratuityPaymentPlanForPartialRegime paymentPlan, final Money amount,
            final YearMonthDay startDate, final YearMonthDay endDate, final BigDecimal ectsForAmount,
            final List<ExecutionSemester> executionSemesters) {
        this();
        init(paymentPlan, amount, startDate, endDate, false, null, null, null, ectsForAmount, executionSemesters);
    }

    private void init(FullGratuityPaymentPlanForPartialRegime paymentPlan, Money amount, YearMonthDay startDate,
            YearMonthDay endDate, boolean penaltyAppliable, BigDecimal penaltyPercentage, YearMonthDay whenStartToApplyPenalty,
            Integer maxMonthsToApplyPenalty, BigDecimal ectsForAmount, List<ExecutionSemester> executionSemesters) {

        if (penaltyAppliable) {
            super.init(paymentPlan, amount, startDate, endDate, penaltyPercentage, whenStartToApplyPenalty,
                    maxMonthsToApplyPenalty);
        } else {
            super.init(paymentPlan, amount, startDate, endDate);
        }

        checkParameters(ectsForAmount, executionSemesters);

        super.setPenaltyAppliable(penaltyAppliable);
        super.setEctsForAmount(ectsForAmount);

        for (final ExecutionSemester executionSemester : executionSemesters) {
            super.addExecutionSemesters(executionSemester);
        }
    }

    private void checkParameters(final BigDecimal ectsForAmount, final Collection<ExecutionSemester> executionSemesters) {
        if (ectsForAmount == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.installments.PartialRegimeInstallment.ectsForAmount.cannot.be.null");
        }

        if (executionSemesters == null || executionSemesters.isEmpty()) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.installments.PartialRegimeInstallment.executionSemesters.cannot.be.null.or.empty");
        }
    }

    public PartialRegimeInstallment(FullGratuityPaymentPlanForPartialRegime paymentPlan, Money amount, YearMonthDay startDate,
            YearMonthDay endDate, BigDecimal penaltyPercentage, YearMonthDay whenStartToApplyPenalty,
            Integer maxMonthsToApplyPenalty, BigDecimal ectsForAmount, List<ExecutionSemester> executionSemesters) {
        this();
        init(paymentPlan, amount, startDate, endDate, true, penaltyPercentage, whenStartToApplyPenalty, maxMonthsToApplyPenalty,
                ectsForAmount, executionSemesters);
    }

    /**
     * Formula: 0.5 x Amount x (1 + EnroledEcts / ectsForAmount)
     */
    @Override
    public Money calculateBaseAmount(Event event) {
        final BigDecimal enroledEcts = getEnroledEcts((GratuityEvent) event);
        if (enroledEcts.compareTo(BigDecimal.ZERO) == 0) {
            return Money.ZERO;
        }

        final BigDecimal proporcionToPay = enroledEcts.divide(getEctsForAmount());
        // TODO: Fix Money limitation of scale = 2 to prevent this type of
        // coding
        final BigDecimal amount = getAmount().getAmount().setScale(10);

        return new Money(amount.multiply(new BigDecimal("0.5")).multiply(BigDecimal.ONE.add(proporcionToPay)));

    }

    @Override
    protected Money calculatePenaltyAmount(Event event, DateTime when, BigDecimal discountPercentage) {
        if (isPenaltyAppliable()) {
            return super.calculatePenaltyAmount(event, when, discountPercentage);
        }

        return Money.ZERO;
    }

    private BigDecimal getEnroledEcts(GratuityEvent gratuityEvent) {
    	return getExecutionSemestersSet().stream()
        		.flatMap(es -> gratuityEvent.getStudentCurricularPlan().getCycleCurriculumGroups().stream()
        						.flatMap(ccg -> ccg.getEnrolmentsBy(es).stream()))
        		.map(e -> e.getEctsCreditsForCurriculum())
        		.reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public FullGratuityPaymentPlanForPartialRegime getPaymentPlan() {
        return (FullGratuityPaymentPlanForPartialRegime) super.getPaymentPlan();
    }

    public boolean isPenaltyAppliable() {
        return getPenaltyAppliable().booleanValue();
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();

        if (isPenaltyAppliable()) {
            labelFormatter.appendLabel(Bundle.APPLICATION, "label.PartialRegimeInstallment.description.with.penalty",
                    getInstallmentOrder().toString(), getStartDate().toString("dd/MM/yyyy"), getEndDate().toString("dd/MM/yyyy"),
                    buildExecutionSemesterDescription(), getPenaltyPercentage().multiply(BigDecimal.valueOf(100)).toString(),
                    getWhenStartToApplyPenalty().toString("dd/MM/yyyy"));
        } else {
            labelFormatter.appendLabel(Bundle.APPLICATION, "label.PartialRegimeInstallment.description.without.penalty",
                    getInstallmentOrder().toString(), getStartDate().toString("dd/MM/yyyy"), getEndDate().toString("dd/MM/yyyy"),
                    buildExecutionSemesterDescription());
        }

        return labelFormatter;
    }

    private String buildExecutionSemesterDescription() {
        final StringBuilder result = new StringBuilder();
        for (final ExecutionSemester executionSemester : getExecutionSemestersSet()) {
            result.append(executionSemester.getName()).append(", ");
        }

        if (result.length() > 0 && result.toString().endsWith(", ")) {
            result.delete(result.length() - 2, result.length());
        }

        return result.toString();
    }

    @Override
    public void delete() {
        getExecutionSemestersSet().clear();
        super.delete();
    }

    @Override
    public void edit(final InstallmentBean bean) {
    	final Collection<ExecutionSemester> executionSemesters = bean.getExecutionSemesters();
        final BigDecimal ectsForAmount = bean.getEctsForAmount();

        checkParameters(ectsForAmount, executionSemesters);

        getExecutionSemestersSet().addAll(executionSemesters);
        getExecutionSemestersSet().retainAll(executionSemesters);

        super.setEctsForAmount(ectsForAmount);
        super.edit(bean);
    }

}
