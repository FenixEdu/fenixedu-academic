package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;

public interface IPastRequestEvent {

    public void setPastAmount(Money pastAmount);

    public AccountingTransaction depositAmount(final User responsibleUser, final Money amount,
            final AccountingTransactionDetailDTO transactionDetailDTO);

}
