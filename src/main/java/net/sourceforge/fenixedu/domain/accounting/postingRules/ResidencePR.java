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
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class ResidencePR extends ResidencePR_Base {

    public ResidencePR(final DateTime startDate, final DateTime endDate, final ServiceAgreementTemplate serviceAgreementTemplate,
            Money penaltyPerDay) {
        super.init(EntryType.RESIDENCE_FEE, EventType.RESIDENCE_PAYMENT, startDate, endDate, serviceAgreementTemplate);
        setPenaltyPerDay(penaltyPerDay);
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
        Money amounToPay = event.calculateAmountToPay(when);
        return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), amounToPay,
                amounToPay, event.getDescriptionForEntryType(getEntryType()), amounToPay));
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        ResidenceEvent residenceEvent = (ResidenceEvent) event;
        Money baseValue = residenceEvent.getRoomValue();
        if (residenceEvent.getPaymentLimitDate().isAfter(when)) {
            return baseValue;
        }
        return baseValue.add(getPenaltyPerDay().multiply(
                BigDecimal.valueOf(Days.daysBetween(residenceEvent.getPaymentLimitDate(), when).getDays())));
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        return amountToPay;
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

        if (entryDTOs.size() != 1) {
            throw new DomainException("error.accounting.postingRules.residencePR.invalid.number.of.entryDTOs");
        }

        final EntryDTO entryDTO = entryDTOs.iterator().next();

        checkIfCanAddAmount(entryDTO.getAmountToPay(), event, transactionDetail.getWhenRegistered());

        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, entryDTO.getEntryType(),
                entryDTO.getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(Money amountToPay, Event event, DateTime whenRegistered) {
        if (!event.calculateAmountToPay(whenRegistered).equals(amountToPay)) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.postingRules.amount.being.payed.must.be.equal.to.amout.in.debt",
                    event.getDescriptionForEntryType(getEntryType()));
        }
    }

    @Deprecated
    public boolean hasPenaltyPerDay() {
        return getPenaltyPerDay() != null;
    }

}
