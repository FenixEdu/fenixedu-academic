package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.DepositAmountBean;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;

public class DepositAmountOnEvent extends Service {

    public AccountingTransaction run(final DepositAmountBean depositAmountBean) {
	return depositAmountBean.getEvent().depositAmount(
		null,
		depositAmountBean.getAmount(),
		depositAmountBean.getEntryType(),
		new AccountingTransactionDetailDTO(depositAmountBean.getWhenRegistered(), PaymentMode.CASH, depositAmountBean
			.getReason()));

    }

}
