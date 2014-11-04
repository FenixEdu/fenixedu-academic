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
package org.fenixedu.academic.domain.accounting.postingRules.serviceRequests;

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
import org.fenixedu.academic.domain.accounting.events.serviceRequests.PastFreeSolicitationAcademicRequestEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class PastFreeSolicitationAcademicRequestPR extends PastFreeSolicitationAcademicRequestPR_Base {

    protected PastFreeSolicitationAcademicRequestPR() {
        super();
    }

    public PastFreeSolicitationAcademicRequestPR(final DateTime startDate, final DateTime endDate,
            final ServiceAgreementTemplate serviceAgreementTemplate) {
        super.init(EntryType.FREE_SOLICITATION_ACADEMIC_REQUEST_FEE, EventType.PAST_FREE_SOLICITATION_ACADEMIC_REQUEST,
                startDate, endDate, serviceAgreementTemplate);
    }

    @Override
    protected Money doCalculationForAmountToPay(final Event event, final DateTime when, final boolean applyDiscount) {
        return ((PastFreeSolicitationAcademicRequestEvent) event).getPastAmount();
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        return amountToPay;
    }

    @Override
    public List<EntryDTO> calculateEntries(final Event event, final DateTime when) {
        return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), event
                .getPayedAmount(), event.calculateAmountToPay(when), event.getDescriptionForEntryType(getEntryType()), event
                .calculateAmountToPay(when)));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(final User user, final Collection<EntryDTO> entryDTOs,
            final Event event, final Account fromAccount, final Account toAccount,
            final AccountingTransactionDetailDTO transactionDetail) {

        if (entryDTOs.size() != 1) {
            throw new DomainException("error.accounting.postingRules.invalid.number.of.entryDTOs");
        }

        checkIfCanAddAmount(entryDTOs.iterator().next().getAmountToPay(), event, transactionDetail.getWhenRegistered());

        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, getEntryType(), entryDTOs
                .iterator().next().getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(final Money amountToPay, final Event event, final DateTime when) {
        if (event.calculateAmountToPay(when).greaterThan(amountToPay)) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.postingRules.amount.being.payed.must.be.equal.to.amout.in.debt",
                    event.getDescriptionForEntryType(getEntryType()));
        }
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
