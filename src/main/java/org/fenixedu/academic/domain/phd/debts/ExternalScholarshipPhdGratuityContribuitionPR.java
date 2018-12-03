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
package org.fenixedu.academic.domain.phd.debts;

import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class ExternalScholarshipPhdGratuityContribuitionPR extends ExternalScholarshipPhdGratuityContribuitionPR_Base {

    public ExternalScholarshipPhdGratuityContribuitionPR() {
        super();

    }

    public ExternalScholarshipPhdGratuityContribuitionPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate) {
        this();
        init(EventType.EXTERNAL_SCOLARSHIP, startDate, endDate, serviceAgreementTemplate);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event) {
        return ((ExternalScholarshipPhdGratuityContribuitionEvent) event).getPhdGratuityExternalScholarshipExemption().getValue();
    }

    @Override
    protected EntryType getEntryType() {
        return EntryType.EXTERNAL_SCOLARSHIP_PAYMENT;
    }

    @Override
    public AccountingTransaction depositAmount(User responsibleUser, Event event, Account fromAccount, Account toAccount,
            Money amount, AccountingTransactionDetailDTO transactionDetailDTO) {
        return depositAmount(responsibleUser, event, fromAccount, toAccount, amount, EntryType.EXTERNAL_SCOLARSHIP_PAYMENT,
                transactionDetailDTO);
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
