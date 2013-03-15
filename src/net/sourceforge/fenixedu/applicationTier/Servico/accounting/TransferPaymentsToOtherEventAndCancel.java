package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import pt.ist.fenixWebFramework.services.Service;

public class TransferPaymentsToOtherEventAndCancel extends FenixService {

    @Service
    public static void run(final Person responsible, final Event sourceEvent, final Event targetEvent, final String justification) {
        sourceEvent.transferPaymentsAndCancel(responsible, targetEvent, justification);
    }

}