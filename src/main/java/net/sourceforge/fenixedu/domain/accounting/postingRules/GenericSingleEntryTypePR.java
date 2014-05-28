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

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

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

    @Deprecated
    public boolean hasEntryType() {
        return getEntryType() != null;
    }

}
