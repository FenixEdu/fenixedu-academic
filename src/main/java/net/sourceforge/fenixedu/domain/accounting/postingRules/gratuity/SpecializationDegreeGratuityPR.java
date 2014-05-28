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
package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.SpecializationDegreeGratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public abstract class SpecializationDegreeGratuityPR extends SpecializationDegreeGratuityPR_Base implements IGratuityPR {

    abstract protected static class SpecializationDegreeGratuityPREditor implements FactoryExecutor, Serializable {

        static private final long serialVersionUID = -5454487291500203873L;

        private DateTime beginDate;

        private Money specializationDegreeTotalAmount;

        private BigDecimal specializationDegreePartialAcceptedPercentage;

        private SpecializationDegreeGratuityPR specializationDegreeGratuityPR;

        protected SpecializationDegreeGratuityPREditor() {
        }

        public DateTime getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(DateTime beginDate) {
            this.beginDate = beginDate;
        }

        public Money getSpecializationDegreeTotalAmount() {
            return specializationDegreeTotalAmount;
        }

        public void setSpecializationDegreeTotalAmount(Money specializationDegreeTotalAmount) {
            this.specializationDegreeTotalAmount = specializationDegreeTotalAmount;
        }

        public BigDecimal getSpecializationDegreePartialAcceptedPercentage() {
            return specializationDegreePartialAcceptedPercentage;
        }

        public void setSpecializationDegreePartialAcceptedPercentage(BigDecimal specializationDegreePartialAcceptedPercentage) {
            this.specializationDegreePartialAcceptedPercentage = specializationDegreePartialAcceptedPercentage;
        }

        public SpecializationDegreeGratuityPR getSpecializationDegreeGratuityPR() {
            return specializationDegreeGratuityPR;
        }

        public void setSpecializationDegreeGratuityPR(SpecializationDegreeGratuityPR specializationDegreeGratuityPR) {
            this.specializationDegreeGratuityPR = specializationDegreeGratuityPR;
        }
    }

    protected SpecializationDegreeGratuityPR() {
        super();
    }

    public SpecializationDegreeGratuityPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money specializationDegreeTotalAmount,
            BigDecimal specializationDegreePartialAcceptedPercentage) {
        super();
        init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate,
                specializationDegreeTotalAmount, specializationDegreePartialAcceptedPercentage);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money specializationDegreeTotalAmount,
            BigDecimal specializationDegreePartialAcceptedPercentage) {

        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);

        checkParameters(specializationDegreeTotalAmount, specializationDegreePartialAcceptedPercentage);

        super.setSpecializationDegreeTotalAmount(specializationDegreeTotalAmount);
        super.setSpecializationDegreePartialAcceptedPercentage(specializationDegreePartialAcceptedPercentage);
    }

    private void checkParameters(Money specializationDegreeTotalAmount, BigDecimal specializationDegreePartialAcceptedPercentage) {
        if (specializationDegreeTotalAmount == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.specializationDegreeTotalAmount.cannot.be.null");
        }

        if (specializationDegreePartialAcceptedPercentage == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.specializationDegreePartialAcceptedPercentage.cannot.be.null");
        }
    }

    @Override
    public void setSpecializationDegreeTotalAmount(Money specializationDegreeTotalAmount) {
        throw new DomainException(
                "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.cannot.modify.specializationDegreeTotalAmount");
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

        if (entryDTOs.size() != 1) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.invalid.number.of.entryDTOs");
        }

        checkIfCanAddAmount(entryDTOs.iterator().next().getAmountToPay(), event, transactionDetail.getWhenRegistered());

        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, getEntryType(), entryDTOs
                .iterator().next().getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(Money amountToAdd, Event event, DateTime when) {
        if (((GratuityEvent) event).isCustomEnrolmentModel()) {
            checkIfCanAddAmountForCustomEnrolmentModel(event, when, amountToAdd);
        } else {
            checkIfCanAddAmountForCompleteEnrolmentModel(amountToAdd, event, when);
        }
    }

    private void checkIfCanAddAmountForCustomEnrolmentModel(Event event, DateTime when, Money amountToAdd) {
        if (event.calculateAmountToPay(when).greaterThan(amountToAdd)) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.amount.being.payed.must.be.equal.to.amout.in.debt",
                    event.getDescriptionForEntryType(getEntryType()));
        }
    }

    private void checkIfCanAddAmountForCompleteEnrolmentModel(final Money amountToAdd, final Event event, final DateTime when) {

        if (hasAlreadyPayedAnyAmount(event, when)) {
            final Money totalFinalAmount = event.getPayedAmount().add(amountToAdd);
            if (!(totalFinalAmount.greaterOrEqualThan(calculateTotalAmountToPay(event, when)) || totalFinalAmount
                    .equals(getPartialPaymentAmount(event, when)))) {
                throw new DomainExceptionWithLabelFormatter(
                        "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.amount.being.payed.must.be.equal.to.amout.in.debt",
                        event.getDescriptionForEntryType(getEntryType()));
            }
        } else {
            if (!isPayingTotalAmount(event, when, amountToAdd) && !isPayingPartialAmount(event, when, amountToAdd)) {
                final LabelFormatter percentageLabelFormatter = new LabelFormatter();
                percentageLabelFormatter.appendLabel(getSpecializationDegreePartialAcceptedPercentage().multiply(
                        BigDecimal.valueOf(100)).toString());

                throw new DomainExceptionWithLabelFormatter(
                        "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityPR.invalid.partial.payment.value",
                        event.getDescriptionForEntryType(getEntryType()), percentageLabelFormatter);
            }
        }
    }

    private boolean isPayingTotalAmount(final Event event, final DateTime when, Money amountToAdd) {
        return amountToAdd.greaterOrEqualThan(event.calculateAmountToPay(when));
    }

    private boolean isPayingPartialAmount(final Event event, final DateTime when, final Money amountToAdd) {
        return amountToAdd.equals(getPartialPaymentAmount(event, when));
    }

    private boolean hasAlreadyPayedAnyAmount(final Event event, final DateTime when) {
        return !calculateTotalAmountToPay(event, when).equals(event.calculateAmountToPay(when));
    }

    private Money getPartialPaymentAmount(final Event event, final DateTime when) {
        return calculateTotalAmountToPay(event, when).multiply(getSpecializationDegreePartialAcceptedPercentage());
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        final Money result;
        if (((GratuityEvent) event).isCustomEnrolmentModel()) {
            result = calculateSpecializationDegreeGratuityTotalAmountToPay(event);
        } else {
            result = getSpecializationDegreeTotalAmount();
        }

        return result;
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        final BigDecimal discountPercentage = applyDiscount ? getDiscountPercentage(event, amountToPay) : BigDecimal.ZERO;

        return amountToPay.multiply(BigDecimal.ONE.subtract(discountPercentage));
    }

    abstract protected Money calculateSpecializationDegreeGratuityTotalAmountToPay(Event event);

    private BigDecimal getDiscountPercentage(final Event event, final Money amount) {
        return ((SpecializationDegreeGratuityEvent) event).calculateDiscountPercentage(amount);
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
        return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), event
                .getPayedAmount(), event.calculateAmountToPay(when), event.getDescriptionForEntryType(getEntryType()), event
                .calculateAmountToPay(when)));
    }

    @Override
    public Money getDefaultGratuityAmount(ExecutionYear executionYear) {
        return getSpecializationDegreeTotalAmount();
    }

    @Deprecated
    public boolean hasSpecializationDegreeTotalAmount() {
        return getSpecializationDegreeTotalAmount() != null;
    }

    @Deprecated
    public boolean hasSpecializationDegreePartialAcceptedPercentage() {
        return getSpecializationDegreePartialAcceptedPercentage() != null;
    }

}
