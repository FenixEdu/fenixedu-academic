package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AnnulAccountingTransactionBean;
import net.sourceforge.fenixedu.domain.Employee;
import pt.ist.fenixframework.Atomic;

public class AnnulAccountingTransaction {

    @Atomic
    public static void run(final Employee employee, final AnnulAccountingTransactionBean annulAccountingTransactionBean) {
        annulAccountingTransactionBean.getTransaction().annul(employee.getPerson().getUser(),
                annulAccountingTransactionBean.getReason());
    }

}