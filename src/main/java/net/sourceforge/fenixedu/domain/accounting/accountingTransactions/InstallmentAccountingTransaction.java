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
package net.sourceforge.fenixedu.domain.accounting.accountingTransactions;

import net.sourceforge.fenixedu.domain.accounting.AccountingTransactionDetail;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.User;

public class InstallmentAccountingTransaction extends InstallmentAccountingTransaction_Base {

    protected InstallmentAccountingTransaction() {
        super();
    }

    public InstallmentAccountingTransaction(User responsibleUser, Event event, Entry debit, Entry credit,
            Installment installment, AccountingTransactionDetail transactionDetail) {
        this();
        init(responsibleUser, event, debit, credit, installment, transactionDetail);
    }

    private void init(User responsibleUser, Event event, Entry debit, Entry credit, Installment installment,
            AccountingTransactionDetail transactionDetail) {
        super.init(responsibleUser, event, debit, credit, transactionDetail);
        checkParameters(installment);
        super.setInstallment(installment);
    }

    private void checkParameters(Installment installment) {
        if (installment == null) {
            throw new DomainException(
                    "error.accounting.accountingTransactions.InstallmentAccountingTransaction.installment.cannot.be.null");
        }
    }

    @Override
    public void setInstallment(Installment installment) {
        throw new DomainException(
                "error.accounting.accountingTransactions.InstallmentAccountingTransaction.cannot.modify.installment");
    }

    @Override
    public boolean isInstallment() {
        return true;
    }

    @Override
    public void delete() {
        super.setInstallment(null);
        super.delete();
    }

    @Deprecated
    public boolean hasInstallment() {
        return getInstallment() != null;
    }

}
