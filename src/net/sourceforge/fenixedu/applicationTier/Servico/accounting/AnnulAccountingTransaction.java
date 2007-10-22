package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.accounting.AnnulAccountingTransactionBean;
import net.sourceforge.fenixedu.domain.Employee;

public class AnnulAccountingTransaction extends Service {

    public AnnulAccountingTransaction() {
	super();
    }

    public void run(final Employee employee, final AnnulAccountingTransactionBean annulAccountingTransactionBean) {
	annulAccountingTransactionBean.getTransaction().annul(employee.getPerson().getUser(),
		annulAccountingTransactionBean.getReason());
    }

}