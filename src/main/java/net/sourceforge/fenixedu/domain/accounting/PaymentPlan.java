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
package net.sourceforge.fenixedu.domain.accounting;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRuleManager;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.collections.CollectionUtils;
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

    public Money calculateTotalAmount(final Event event, final DateTime when, final BigDecimal discountPercentage) {

        Money result = Money.ZERO;
        for (final Money amount : calculateInstallmentTotalAmounts(event, when, discountPercentage).values()) {
            result = result.add(amount);
        }

        return result;
    }

    private Map<Installment, Money> calculateInstallmentTotalAmounts(final Event event, final DateTime when,
            final BigDecimal discountPercentage) {

        final Map<Installment, Money> result = new HashMap<Installment, Money>();
        final CashFlowBox cashFlowBox = new CashFlowBox(event, when, discountPercentage);

        for (final Installment installment : getInstallmentsSortedByEndDate()) {
            result.put(installment, cashFlowBox.calculateTotalAmountFor(installment));

        }

        return result;
    }

    private class CashFlowBox {
        public DateTime when;
        public Money amount;
        public DateTime currentTransactionDate;
        public List<AccountingTransaction> transactions;
        public BigDecimal discountPercentage;
        public Event event;
        public Money discountValue;
        public boolean usedDiscountValue;
        private Money discountedValue;

        public CashFlowBox(final Event event, final DateTime when, final BigDecimal discountPercentage) {
            this.event = event;
            this.transactions = new ArrayList<AccountingTransaction>(event.getSortedNonAdjustingTransactions());
            this.when = when;
            this.discountPercentage = discountPercentage;
            this.discountValue = event.getTotalDiscount();
            this.discountedValue = Money.ZERO;
            this.usedDiscountValue = false;

            if (transactions.isEmpty()) {
                this.amount = Money.ZERO;
                this.currentTransactionDate = when;
            } else {
                final AccountingTransaction transaction = transactions.remove(0);
                this.amount = transaction.getAmountWithAdjustment();
                this.currentTransactionDate = transaction.getWhenRegistered();
            }

        }

        private boolean hasMoneyFor(final Money amount) {
            return this.amount.greaterOrEqualThan(amount);
        }

        private boolean hasDiscountValue() {
            return this.discountValue.isPositive();
        }

        public boolean subtractMoneyFor(final Installment installment) {

            if (hasDiscountValue() && this.discountValue.greaterOrEqualThan(installment.getAmount())) {
                usedDiscountValue = true;
                this.discountValue = this.discountValue.subtract(installment.getAmount());
                return true;
            }

            Money installmentAmount =
                    installment.calculateAmount(this.event, this.currentTransactionDate, this.discountPercentage,
                            isToApplyPenalty(this.event, installment));

            if (hasDiscountValue()) {
                installmentAmount = installmentAmount.subtract(this.discountValue);
                this.discountedValue = this.discountValue;
            }

            if (hasMoneyFor(installmentAmount)) {
                this.amount = this.amount.subtract(installmentAmount);
                this.discountValue = Money.ZERO;
                return true;
            }

            if (this.transactions.isEmpty()) {
                return false;
            }

            final AccountingTransaction transaction = this.transactions.remove(0);
            this.amount = this.amount.add(transaction.getAmountWithAdjustment());
            this.currentTransactionDate = transaction.getWhenRegistered();

            return subtractMoneyFor(installment);
        }

        public Money subtractRemainingFor(final Installment installment) {
            final Money result =
                    installment
                            .calculateAmount(this.event, this.when, this.discountPercentage,
                                    isToApplyPenalty(this.event, installment)).subtract(this.discountValue).subtract(this.amount);
            this.amount = this.discountValue = Money.ZERO;
            return result;
        }

        public Money calculateTotalAmountFor(final Installment installment) {
            final Money result;
            if (subtractMoneyFor(installment)) {
                if (usedDiscountValue) {
                    result = Money.ZERO;
                } else {
                    result =
                            installment.calculateAmount(this.event, this.currentTransactionDate, this.discountPercentage,
                                    isToApplyPenalty(this.event, installment)).subtract(this.discountedValue);
                    this.discountedValue = Money.ZERO;
                }
            } else {
                result =
                        installment.calculateAmount(this.event, this.when, this.discountPercentage,
                                isToApplyPenalty(this.event, installment)).subtract(this.discountedValue);
                this.discountedValue = Money.ZERO;
            }
            usedDiscountValue = false;
            return result;
        }
    }

    public Map<Installment, Money> calculateInstallmentRemainingAmounts(final Event event, final DateTime when,
            final BigDecimal discountPercentage) {
        final Map<Installment, Money> result = new HashMap<Installment, Money>();
        final CashFlowBox cashFlowBox = new CashFlowBox(event, when, discountPercentage);

        for (final Installment installment : getInstallmentsSortedByEndDate()) {

            if (!cashFlowBox.subtractMoneyFor(installment)) {
                result.put(installment, cashFlowBox.subtractRemainingFor(installment));
            }
        }

        return result;

    }

    public Money calculateRemainingAmountFor(final Installment installment, final Event event, final DateTime when,
            final BigDecimal discountPercentage) {

        final Map<Installment, Money> amountsByInstallment =
                calculateInstallmentRemainingAmounts(event, when, discountPercentage);
        final Money installmentAmount = amountsByInstallment.get(installment);

        return (installmentAmount != null) ? installmentAmount : Money.ZERO;
    }

    public boolean isInstallmentInDebt(final Installment installment, final Event event, final DateTime when,
            final BigDecimal discountPercentage) {

        return calculateRemainingAmountFor(installment, event, when, discountPercentage).isPositive();

    }

    public Installment getInstallmentByOrder(int order) {
        for (final Installment installment : getInstallments()) {
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
        return hasExecutionYear() && getExecutionYear().equals(executionYear);
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
        return hasExecutionYear() && getExecutionYear().equals(executionYear);
    }

    abstract public ServiceAgreementTemplate getServiceAgreementTemplate();

    public void delete() {
        check(this, RolePredicates.MANAGER_PREDICATE);
        if (!getGratuityEventsWithPaymentPlan().isEmpty()) {
            throw new DomainException("error.accounting.PaymentPlan.cannot.delete.with.already.associated.gratuity.events");
        }

        while (hasAnyInstallments()) {
            getInstallments().iterator().next().delete();
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan> getGratuityEventsWithPaymentPlan() {
        return getGratuityEventsWithPaymentPlanSet();
    }

    @Deprecated
    public boolean hasAnyGratuityEventsWithPaymentPlan() {
        return !getGratuityEventsWithPaymentPlanSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Installment> getInstallments() {
        return getInstallmentsSet();
    }

    @Deprecated
    public boolean hasAnyInstallments() {
        return !getInstallmentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasDefaultPlan() {
        return getDefaultPlan() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
