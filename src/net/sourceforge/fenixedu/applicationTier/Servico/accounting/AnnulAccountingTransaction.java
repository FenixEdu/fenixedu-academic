package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.accounting.AnnulAccountingTransactionBean;
import net.sourceforge.fenixedu.domain.Employee;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AnnulAccountingTransaction extends FenixService {

    @Checked("AcademicPredicates.MANAGE_PAYMENTS")
    @Service
    public static void run(final Employee employee, final AnnulAccountingTransactionBean annulAccountingTransactionBean) {
	annulAccountingTransactionBean.getTransaction().annul(employee.getPerson().getUser(),
		annulAccountingTransactionBean.getReason());
    }

}