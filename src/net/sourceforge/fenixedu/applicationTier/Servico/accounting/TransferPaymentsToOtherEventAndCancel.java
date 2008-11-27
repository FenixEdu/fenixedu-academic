package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class TransferPaymentsToOtherEventAndCancel extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(final Employee responsibleEmployee, final Event sourceEvent, final Event targetEvent,
	    final String justification) {
	sourceEvent.transferPaymentsAndCancel(responsibleEmployee, targetEvent, justification);
    }

}