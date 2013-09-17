package net.sourceforge.fenixedu.applicationTier.Servico.accounting;


import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import pt.ist.fenixframework.Atomic;

public class TransferPaymentsToOtherEventAndCancel {

    @Atomic
    public static void run(final Person responsible, final Event sourceEvent, final Event targetEvent, final String justification) {
        sourceEvent.transferPaymentsAndCancel(responsible, targetEvent, justification);
    }

}