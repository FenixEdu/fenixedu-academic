package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class TransferPaymentsToOtherEventAndCancel extends Service {

    public TransferPaymentsToOtherEventAndCancel() {
	super();
    }

    public void run(final Employee responsibleEmployee, final Event sourceEvent, final Event targetEvent,
	    final String justification) {
	sourceEvent.transferPaymentsAndCancel(responsibleEmployee, targetEvent, justification);
    }

}