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
package net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.installments.InstallmentForFirstTimeStudents;
import net.sourceforge.fenixedu.domain.accounting.installments.InstallmentWithMonthlyPenalty;
import net.sourceforge.fenixedu.domain.accounting.installments.PartialRegimeInstallment;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

public class InstallmentBean implements Serializable {

    static private final long serialVersionUID = -103744109361724129L;

    private boolean selected;

    private Money amount;

    private BigDecimal ectsForAmount;

    private List<ExecutionSemester> executionSemesters;

    private YearMonthDay startDate;

    private YearMonthDay endDate;

    private boolean penaltyAppliable;

    private BigDecimal montlyPenaltyPercentage;

    private YearMonthDay whenToStartApplyPenalty;

    private Integer maxMonthsToApplyPenalty;

    private PaymentPlanBean paymentPlanBean;

    private Integer numberOfDaysToStartApplyingPenalty = null;

    public InstallmentBean(PaymentPlanBean paymentPlanBean) {
        setExecutionSemesters(new ArrayList<ExecutionSemester>());
        setPaymentPlanBean(paymentPlanBean);
    }

    public InstallmentBean(final Installment installment) {
        setPaymentPlanBean(new PaymentPlanBean(installment.getPaymentPlan().getExecutionYear()));
        setExecutionSemesters(new ArrayList<ExecutionSemester>());
        setAmount(installment.getAmount());
        setStartDate(installment.getStartDate());
        setEndDate(installment.getEndDate());

        if (installment instanceof InstallmentWithMonthlyPenalty) {
            InstallmentWithMonthlyPenalty installmentWithPenalty = (InstallmentWithMonthlyPenalty) installment;
            setMaxMonthsToApplyPenalty(installmentWithPenalty.getMaxMonthsToApplyPenalty());

            if (!(installment instanceof InstallmentForFirstTimeStudents)) {
                setWhenToStartApplyPenalty(installmentWithPenalty.getWhenStartToApplyPenalty());
            }

            setMontlyPenaltyPercentage(installmentWithPenalty.getPenaltyPercentage());

        }

        if (installment instanceof PartialRegimeInstallment) {
            PartialRegimeInstallment partialInstallment = (PartialRegimeInstallment) installment;
            setExecutionSemesters(partialInstallment.getExecutionSemesters());
            setEctsForAmount(partialInstallment.getEctsForAmount());
        }

        if (installment instanceof InstallmentForFirstTimeStudents) {
            InstallmentForFirstTimeStudents installmentForFirstTimeStudents = (InstallmentForFirstTimeStudents) installment;
            setNumberOfDaysToStartApplyingPenalty(installmentForFirstTimeStudents.getNumberOfDaysToStartApplyingPenalty());
        }
    }

    public PaymentPlanBean getPaymentPlanBean() {
        return paymentPlanBean;
    }

    public void setPaymentPlanBean(PaymentPlanBean paymentPlanBean) {
        this.paymentPlanBean = paymentPlanBean;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public YearMonthDay getStartDate() {
        return startDate;
    }

    public void setStartDate(YearMonthDay startDate) {
        this.startDate = startDate;
    }

    public YearMonthDay getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getMontlyPenaltyPercentage() {
        return montlyPenaltyPercentage;
    }

    public void setMontlyPenaltyPercentage(BigDecimal montlyPenaltyPercentage) {
        this.montlyPenaltyPercentage = montlyPenaltyPercentage;
    }

    public YearMonthDay getWhenToStartApplyPenalty() {
        return whenToStartApplyPenalty;
    }

    public void setWhenToStartApplyPenalty(YearMonthDay whenToStartApplyPenalty) {
        this.whenToStartApplyPenalty = whenToStartApplyPenalty;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getMaxMonthsToApplyPenalty() {
        return maxMonthsToApplyPenalty;
    }

    public void setMaxMonthsToApplyPenalty(Integer maxMonthsToApplyPenalty) {
        this.maxMonthsToApplyPenalty = maxMonthsToApplyPenalty;
    }

    public boolean hasRequiredInformation() {
        boolean result = this.amount != null && this.startDate != null && this.endDate != null;

        if (!isPenaltyAppliable()) {
            return result;
        }

        return result && this.montlyPenaltyPercentage != null && this.whenToStartApplyPenalty != null
                && this.maxMonthsToApplyPenalty != null;

    }

    public boolean isPenaltyAppliable() {
        return penaltyAppliable;
    }

    public void setPenaltyAppliable(boolean penaltyAppliable) {
        this.penaltyAppliable = penaltyAppliable;
    }

    public BigDecimal getEctsForAmount() {
        return ectsForAmount;
    }

    public void setEctsForAmount(BigDecimal ectsForAmount) {
        this.ectsForAmount = ectsForAmount;
    }

    public Collection<ExecutionSemester> getExecutionSemesters() {
        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
        for (final ExecutionSemester each : this.executionSemesters) {
            result.add(each);
        }

        return result;
    }

    public void setExecutionSemesters(Collection<ExecutionSemester> executionSemesters) {
        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();
        for (final ExecutionSemester each : executionSemesters) {
            result.add(each);
        }

        this.executionSemesters = result;
    }

    public Integer getNumberOfDaysToStartApplyingPenalty() {
        return numberOfDaysToStartApplyingPenalty;
    }

    public void setNumberOfDaysToStartApplyingPenalty(Integer numberOfDaysToStartApplyingPenalty) {
        this.numberOfDaysToStartApplyingPenalty = numberOfDaysToStartApplyingPenalty;
    }

    public boolean isForFirstTimeInstitutionStudents() {
        return getNumberOfDaysToStartApplyingPenalty() != null;
    }
}
