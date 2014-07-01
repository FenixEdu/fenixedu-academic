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
package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.EquivalencePlanRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class EquivalencePlanRequestPR extends EquivalencePlanRequestPR_Base {

    private EquivalencePlanRequestPR() {
        super();
    }

    public EquivalencePlanRequestPR(final DateTime startDate, final DateTime endDate,
            final ServiceAgreementTemplate serviceAgreementTemplate, final Money amountPerUnit, final Money maximumAmount) {
        this();
        super.init(EntryType.EQUIVALENCE_PLAN_REQUEST_FEE, EventType.EQUIVALENCE_PLAN_REQUEST, startDate, endDate,
                serviceAgreementTemplate);

        checkParameters(amountPerUnit, maximumAmount);

        setAmountPerUnit(amountPerUnit);
        super.setMaximumAmount(maximumAmount);
    }

    private void checkParameters(Money amountPerUnit, Money maximumAmount) {
        if (amountPerUnit == null) {
            throw new DomainException("error.accounting.postingRules.EquivalencePlanRequestPR.amountPerUnit.cannot.be.null");
        }

        if (maximumAmount == null) {
            throw new DomainException("error.accounting.postingRules.EquivalencePlanRequestPR.maximumAmount.cannot.be.null");
        }
    }

    @Override
    public void setMaximumAmount(final Money maximumAmount) {
        throw new DomainException("error.accounting.postingRules.EquivalencePlanRequestPR.maximumAmount.cannot.be.modified");
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
        final Money calculateAmountToPay = event.calculateAmountToPay(when);
        return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), event
                .getPayedAmount(), calculateAmountToPay, event.getDescriptionForEntryType(getEntryType()), calculateAmountToPay));
    }

    @Override
    protected Money doCalculationForAmountToPay(final Event event, final DateTime when, boolean applyDiscount) {
        final EquivalencePlanRequestEvent planRequest = (EquivalencePlanRequestEvent) event;

        Money amountToPay = getAmountPerUnit();

        if (planRequest.getNumberOfEquivalences() != null && planRequest.getNumberOfEquivalences().intValue() != 0) {
            amountToPay = amountToPay.multiply(planRequest.getNumberOfEquivalences().intValue());
        }

        if (getMaximumAmount().greaterThan(Money.ZERO)) {
            if (amountToPay.greaterThan(getMaximumAmount())) {
                amountToPay = getMaximumAmount();
            }
        }

        return amountToPay;
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        final EquivalencePlanRequestEvent planRequest = (EquivalencePlanRequestEvent) event;

        if (planRequest.hasAcademicEventExemption()) {
            amountToPay = amountToPay.subtract(planRequest.getAcademicEventExemption().getValue());
        }

        return amountToPay.isPositive() ? amountToPay : Money.ZERO;
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

        if (entryDTOs.size() != 1) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.EquivalencePlanRequestPR.invalid.number.of.entryDTOs");
        }

        checkIfCanAddAmount(entryDTOs.iterator().next().getAmountToPay(), event, transactionDetail.getWhenRegistered());

        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, getEntryType(), entryDTOs
                .iterator().next().getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(final Money amountToPay, final Event event, final DateTime whenRegistered) {
        final Money totalFinalAmount = event.getPayedAmount().add(amountToPay);

        if (totalFinalAmount.lessThan(calculateTotalAmountToPay(event, whenRegistered))) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.postingRules.gratuity.EquivalencePlanRequestPR.amount.being.payed.must.be.equal.to.amount.in.debt",
                    event.getDescriptionForEntryType(getEntryType()));
        }
    }

    public EquivalencePlanRequestPR edit(final Money amountPerUnit, final Money maximumAmount) {
        deactivate();
        return new EquivalencePlanRequestPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), amountPerUnit,
                maximumAmount);
    }

    public String getMaximumAmountDescription() {
        if (Money.ZERO.equals(this.getMaximumAmount())) {
            return BundleUtil.getString(Bundle.APPLICATION, "label.base.amount.plus.units.with.no.maximum.value");
        }

        return this.getMaximumAmount().getAmountAsString();
    }

    @Deprecated
    public boolean hasMaximumAmount() {
        return getMaximumAmount() != null;
    }

    @Deprecated
    public boolean hasAmountPerUnit() {
        return getAmountPerUnit() != null;
    }

}
