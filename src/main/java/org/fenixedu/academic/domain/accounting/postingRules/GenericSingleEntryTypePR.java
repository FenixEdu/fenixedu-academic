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
package org.fenixedu.academic.domain.accounting.postingRules;

import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public abstract class GenericSingleEntryTypePR extends GenericSingleEntryTypePR_Base {

    protected GenericSingleEntryTypePR() {
        super();
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate) {
        super.init(eventType, startDate, endDate, serviceAgreementTemplate);
        checkParameters(entryType);
        super.setEntryType(entryType);
    }

    private void checkParameters(EntryType entryType) {
        if (entryType == null) {
            throw new DomainException("error.accounting.postingRules.GenericSingleEntryTypePR.entryType.cannot.be.null");
        }
    }

    @Override
    public void setEntryType(EntryType entryType) {
        throw new DomainException("error.accounting.postingRules.GenericSingleEntryTypePR.cannot.modify.entryType");
    }

    @Override
    public void internalAddOtherPartyAmount(User responsibleUser, Event event, Account fromAcount, Account toAccount,
            Money amount, AccountingTransactionDetailDTO transactionDetailDTO) {
        makeAccountingTransaction(responsibleUser, event, fromAcount, toAccount, getEntryType(), amount, transactionDetailDTO);
    }

    @Override
    public AccountingTransaction depositAmount(User responsibleUser, Event event, Account fromAcount, Account toAccount,
            Money amount, AccountingTransactionDetailDTO transactionDetailDTO) {

        checkEntryTypeForDeposit(event, getEntryType());

        return makeAccountingTransaction(responsibleUser, event, fromAcount, toAccount, getEntryType(), amount,
                transactionDetailDTO);
    }

}
