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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.domain.accounting.paymentPlanRules.PaymentPlanRule;
import org.fenixedu.academic.domain.accounting.paymentPlanRules.PaymentPlanRuleManager;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

abstract public class PaymentPlan extends PaymentPlan_Base {

    protected PaymentPlan() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        super.setWhenCreated(new DateTime());
    }

    protected void init(final ExecutionYear executionYear, final Boolean defaultPlan) {

        checkParameters(executionYear, defaultPlan);

        super.setDefaultPlan(defaultPlan);
        super.setExecutionYear(executionYear);
    }

    private void checkParameters(final ExecutionYear executionYear, final Boolean defaultPlan) {
        if (executionYear == null) {
            throw new DomainException("error.accounting.PaymentPlan.executionYear.cannot.be.null");
        }

        if (defaultPlan == null) {
            throw new DomainException("error.accounting.PaymentPlan.defaultPlan.cannot.be.null");
        }
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
        throw new DomainException("error.accounting.PaymentCondition.cannot.modify.executionYear");
    }

    @Override
    public void setDefaultPlan(Boolean defaultPlan) {
        throw new DomainException("error.domain.accounting.PaymentPlan.cannot.modify.defaultPlan");
    }

    public List<Installment> getInstallmentsSortedByEndDate() {
        final List<Installment> result = new ArrayList<Installment>(getInstallmentsSet());
        Collections.sort(result, Installment.COMPARATOR_BY_END_DATE);

        return result;
    }

    public Installment getLastInstallment() {
        return (getInstallmentsSet().size() == 0) ? null : Collections.max(getInstallmentsSet(), Installment.COMPARATOR_BY_ORDER);
    }

    public Installment getFirstInstallment() {
        return (getInstallmentsSet().size() == 0) ? null : Collections.min(getInstallmentsSet(), Installment.COMPARATOR_BY_ORDER);
    }

    public int getLastInstallmentOrder() {
        final Installment installment = getLastInstallment();
        return installment == null ? 0 : installment.getOrder();
    }

    @Override
    public void addInstallments(Installment installment) {
        throw new DomainException("error.accounting.PaymentPlan.cannot.add.installment");
    }

    @Override
    public Set<Installment> getInstallmentsSet() {
        return Collections.unmodifiableSet(super.getInstallmentsSet());
    }

    @Override
    public void removeInstallments(Installment installment) {
        throw new DomainException("error.accounting.PaymentPlan.cannot.remove.installment");
    }

    public boolean isDefault() {
        return getDefaultPlan().booleanValue();
    }

    public Money calculateOriginalTotalAmount() {
        Money result = Money.ZERO;
        for (final Installment installment : getInstallmentsSet()) {
            result = result.add(installment.getAmount());
        }

        return result;
    }

    public Money calculateBaseAmount(final Event event) {
        Money result = Money.ZERO;
        for (final Installment installment : getInstallmentsSet()) {
            result = result.add(installment.calculateBaseAmount(event));
        }

        return result;
    }

    public Money calculateRemainingAmountFor(final Installment installment, final Event event, final DateTime when,
            final BigDecimal discountPercentage) {

        DebtInterestCalculator debtInterestCalculator = event.getDebtInterestCalculator(when);

        BigDecimal installmentOpenAmount = debtInterestCalculator.getDebtsOrderedByDueDate().stream()
                .filter(d -> d.getDueDate().equals(installment.getEndDate(event))).findAny().map(d -> d.getOpenAmount())
                .orElse(BigDecimal.ZERO);

        return new Money(installmentOpenAmount);
    }

    public boolean isInstallmentInDebt(final Installment installment, final Event event, final DateTime when,
            final BigDecimal discountPercentage) {
        return calculateRemainingAmountFor(installment, event, when, discountPercentage).isPositive();
    }

    public Installment getInstallmentByOrder(int order) {
        for (final Installment installment : getInstallmentsSet()) {
            if (installment.getInstallmentOrder() == order) {
                return installment;
            }
        }

        return null;
    }

    public boolean isToApplyPenalty(final Event event, final Installment installment) {
        return true;
    }

    protected void removeParameters() {
        super.setExecutionYear(null);
    }

    public boolean isGratuityPaymentPlan() {
        return false;
    }

    public boolean isCustomGratuityPaymentPlan() {
        return false;
    }

    private boolean hasExecutionYear(final ExecutionYear executionYear) {
        return getExecutionYear() != null && getExecutionYear().equals(executionYear);
    }

    final public boolean isAppliableFor(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {

        if (!hasExecutionYear(executionYear)) {
            return false;
        }

        final Collection<PaymentPlanRule> specificRules = getSpecificPaymentPlanRules();

        if (specificRules.isEmpty()) {
            return false;
        }

        for (final PaymentPlanRule rule : specificRules) {
            if (!rule.isAppliableFor(studentCurricularPlan, executionYear)) {
                return false;
            }
        }

        for (final PaymentPlanRule rule : getNotSpecificPaymentRules()) {
            if (rule.isEvaluatedInNotSpecificPaymentRules() && rule.isAppliableFor(studentCurricularPlan, executionYear)) {
                return false;
            }
        }

        return true;
    }

    protected Collection<PaymentPlanRule> getNotSpecificPaymentRules() {
        /*
         * All payment rules could be connected do
         * DegreeCurricularPlanServiceAgreementTemplate, but for now are just
         * value types
         */
        return CollectionUtils.subtract(PaymentPlanRuleManager.getAllPaymentPlanRules(), getSpecificPaymentPlanRules());
    }

    abstract protected Collection<PaymentPlanRule> getSpecificPaymentPlanRules();

    public String getDescription() {
        return BundleUtil.getString(Bundle.APPLICATION, this.getClass().getSimpleName() + ".description");
    }

    public boolean isFor(final ExecutionYear executionYear) {
        return getExecutionYear() != null && getExecutionYear().equals(executionYear);
    }

    abstract public ServiceAgreementTemplate getServiceAgreementTemplate();

    public void delete() {
        if (!getGratuityEventsWithPaymentPlanSet().isEmpty()) {
            throw new DomainException("error.accounting.PaymentPlan.cannot.delete.with.already.associated.gratuity.events");
        }

        while (!getInstallmentsSet().isEmpty()) {
            getInstallmentsSet().iterator().next().delete();
        }

        removeParameters();
        setRootDomainObject(null);
        super.deleteDomainObject();

    }

    public boolean isForPartialRegime() {
        return false;
    }

    public boolean isForFirstTimeInstitutionStudents() {
        return false;
    }

    public boolean hasSingleInstallment() {
        return getInstallmentsSet().size() == 1;
    }

}
