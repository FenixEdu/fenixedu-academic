package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Receipt;

public class CancelReceipt extends Service {

    public CancelReceipt() {
	super();
    }

    public void run(final Employee employee, final Receipt receipt) {
	receipt.cancel(employee);
    }

}