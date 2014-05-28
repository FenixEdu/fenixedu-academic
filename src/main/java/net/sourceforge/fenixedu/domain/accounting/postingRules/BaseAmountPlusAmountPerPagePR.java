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
package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.math.BigDecimal;
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
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.AcademicEventExemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

abstract public class BaseAmountPlusAmountPerPagePR extends BaseAmountPlusAmountPerPagePR_Base {

    protected BaseAmountPlusAmountPerPagePR() {
        super();
    }

    protected void init(final EntryType entryType, final EventType eventType, final DateTime startDate, final DateTime endDate,
            final ServiceAgreementTemplate serviceAgreementTemplate, final Money baseAmount, final Money amountPerPage) {
        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);
        checkParameters(baseAmount, amountPerPage);
        super.setBaseAmount(baseAmount);
        super.setAmountPerPage(amountPerPage);
    }

    private void checkParameters(final Money baseAmount, final Money amountPerPage) {
        if (baseAmount == null) {
            throw new DomainException(
                    "error.accounting.postingRules.BaseAmountPlusAmountPerUnitGreaterThanOnePR.baseAmount.cannot.be.null");
        }
        if (amountPerPage == null) {
            throw new DomainException(
                    "error.accounting.postingRules.BaseAmountPlusAmountPerUnitGreaterThanOnePR.amountPerPage.cannot.be.null");
        }
    }

    @Override
    public void setBaseAmount(Money baseAmount) {
        throw new DomainException(
                "error.accounting.postingRules.BaseAmountPlusAmountPerUnitGreaterThanOnePR.cannot.modify.baseAmount");
    }

    @Override
    public void setAmountPerPage(Money amountPerPage) {
        throw new DomainException(
                "error.accounting.postingRules.BaseAmountPlusAmountPerUnitGreaterThanOnePR.cannot.modify.amountPerUnit");
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
        return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), event
                .getPayedAmount(), event.calculateAmountToPay(when), event.getDescriptionForEntryType(getEntryType()), event
                .calculateAmountToPay(when)));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {
        if (entryDTOs.size() != 1) {
            throw new DomainException(
                    "error.accounting.postingRules.BaseAmountPlusAmountPerUnitGreaterThanOnePR.invalid.number.of.entryDTOs");
        }

        final EntryDTO entryDTO = entryDTOs.iterator().next();
        checkIfCanAddAmount(entryDTO.getAmountToPay(), event, transactionDetail.getWhenRegistered());

        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, entryDTO.getEntryType(),
                entryDTO.getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(Money amountToPay, Event event, DateTime when) {
        if (amountToPay.compareTo(calculateTotalAmountToPay(event, when)) != 0) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.postingRules.BaseAmountPlusAmountPerUnitGreaterThanOnePR.amount.being.payed.must.match.amount.to.pay",
                    event.getDescriptionForEntryType(getEntryType()));
        }
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        final Money result = isUrgent(event) ? getBaseAmount().multiply(BigDecimal.valueOf(2d)) : getBaseAmount();
        return result.add(getAmountForPages(event));
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        if (event.hasAnyExemptions()) {
            Collection<Exemption> exemptions = event.getExemptionsSet();

            for (Exemption exemption : exemptions) {
                AcademicEventExemption academicEventExemption = (AcademicEventExemption) exemption;

                amountToPay = amountToPay.subtract(academicEventExemption.getValue());
            }
        }

        if (amountToPay.isNegative()) {
            return Money.ZERO;
        }

        return amountToPay;
    }

    abstract protected boolean isUrgent(final Event event);

    abstract protected Money getAmountForPages(final Event event);

    @Deprecated
    public boolean hasBaseAmount() {
        return getBaseAmount() != null;
    }

    @Deprecated
    public boolean hasAmountPerPage() {
        return getAmountPerPage() != null;
    }

}
