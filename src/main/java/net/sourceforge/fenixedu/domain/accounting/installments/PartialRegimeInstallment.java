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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.InstallmentBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.FullGratuityPaymentPlanForPartialRegime;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

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

    private void checkParameters(final BigDecimal ectsForAmount, final List<ExecutionSemester> executionSemesters) {
        if (ectsForAmount == null) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.installments.PartialRegimeInstallment.ectsForAmount.cannot.be.null");
        }

        if (executionSemesters == null || executionSemesters.isEmpty()) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.installments.PartialRegimeInstallment.executionSemesters.cannot.be.null.or.empty");
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
    protected Money calculateBaseAmount(Event event) {
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

        BigDecimal total = BigDecimal.ZERO;

        for (final Enrolment enrolment : collectEnrolments(gratuityEvent)) {
            total = total.add(enrolment.getEctsCreditsForCurriculum());
        }

        return total;

    }

    private Set<Enrolment> collectEnrolments(GratuityEvent gratuityEvent) {
        final Set<Enrolment> result = new HashSet<Enrolment>();

        for (final ExecutionSemester executionSemester : getExecutionSemesters()) {
            for (final CycleCurriculumGroup cycleCurriculumGroup : gratuityEvent.getStudentCurricularPlan()
                    .getCycleCurriculumGroups()) {
                for (final Enrolment enrolment : cycleCurriculumGroup.getEnrolmentsBy(executionSemester)) {
                    result.add(enrolment);
                }
            }
        }

        return result;

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
            labelFormatter.appendLabel("application", "label.PartialRegimeInstallment.description.with.penalty",
                    getInstallmentOrder().toString(), getStartDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT), getEndDate()
                            .toString(DateFormatUtil.DEFAULT_DATE_FORMAT), buildExecutionSemesterDescription(),
                    getPenaltyPercentage().multiply(BigDecimal.valueOf(100)).toString(),
                    getWhenStartToApplyPenalty().toString(DateFormatUtil.DEFAULT_DATE_FORMAT));
        } else {
            labelFormatter.appendLabel("application", "label.PartialRegimeInstallment.description.without.penalty",
                    getInstallmentOrder().toString(), getStartDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT), getEndDate()
                            .toString(DateFormatUtil.DEFAULT_DATE_FORMAT), buildExecutionSemesterDescription());
        }

        return labelFormatter;
    }

    private String buildExecutionSemesterDescription() {
        final StringBuilder result = new StringBuilder();
        for (final ExecutionSemester executionSemester : getExecutionSemesters()) {
            result.append(executionSemester.getName()).append(", ");
        }

        if (result.length() > 0 && result.toString().endsWith(", ")) {
            result.delete(result.length() - 2, result.length());
        }

        return result.toString();
    }

    @Override
    public void delete() {
        getExecutionSemesters().clear();
        super.delete();
    }

    @Override
    public void edit(InstallmentBean bean) {
        List<ExecutionSemester> executionSemesters = new ArrayList<>(bean.getExecutionSemesters());
        BigDecimal ectsForAmount = bean.getEctsForAmount();

        checkParameters(ectsForAmount, executionSemesters);

        for (ExecutionSemester executionSemester : executionSemesters) {
            super.addExecutionSemesters(executionSemester);
        }

        super.setEctsForAmount(ectsForAmount);
        super.edit(bean);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionSemester> getExecutionSemesters() {
        return getExecutionSemestersSet();
    }

    @Deprecated
    public boolean hasAnyExecutionSemesters() {
        return !getExecutionSemestersSet().isEmpty();
    }

    @Deprecated
    public boolean hasEctsForAmount() {
        return getEctsForAmount() != null;
    }

    @Deprecated
    public boolean hasPenaltyAppliable() {
        return getPenaltyAppliable() != null;
    }

}
