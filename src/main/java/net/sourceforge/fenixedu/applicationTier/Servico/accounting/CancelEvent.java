package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import pt.ist.fenixframework.Atomic;

public class CancelEvent {

    @Atomic
    public static void run(final Event event, final Person responsible, final String justification) {
        event.cancel(responsible, justification);
    }
}