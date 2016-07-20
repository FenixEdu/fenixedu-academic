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
package org.fenixedu.academic.domain.accounting.events.gratuity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class ExternalScholarshipGratuityContributionPR extends ExternalScholarshipGratuityContributionPR_Base {

    public ExternalScholarshipGratuityContributionPR() {
        super();

    }

    public ExternalScholarshipGratuityContributionPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate) {
        this();
        init(EventType.EXTERNAL_CONTRIBUTION, startDate, endDate, serviceAgreementTemplate);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        return ((ExternalScholarshipGratuityContributionEvent) event).getTotalValue();
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        return amountToPay;
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
        return Collections.singletonList(new EntryDTO(EntryType.EXTERNAL_CONTRIBUTION_PAYMENT, event, calculateTotalAmountToPay(
                event, when), event.getPayedAmount(), event.calculateAmountToPay(when), event
                .getDescriptionForEntryType(EntryType.EXTERNAL_CONTRIBUTION_PAYMENT), event.calculateAmountToPay(when)));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

        if (entryDTOs.size() != 1) {
            throw new DomainException("error.accounting.postingRules.gratuity.GratuityContributionPR.invalid.number.of.entryDTOs");
        }

        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount,
                EntryType.EXTERNAL_CONTRIBUTION_PAYMENT, entryDTOs.iterator().next().getAmountToPay(), transactionDetail));
    }

    @Override
    public AccountingTransaction depositAmount(User responsibleUser, Event event, Account fromAcount, Account toAccount,
            Money amount, AccountingTransactionDetailDTO transactionDetailDTO) {
        return depositAmount(responsibleUser, event, fromAcount, toAccount, amount, EntryType.EXTERNAL_CONTRIBUTION_PAYMENT,
                transactionDetailDTO);
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
