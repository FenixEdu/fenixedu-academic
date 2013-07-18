package net.sourceforge.fenixedu.applicationTier.Servico.accounting;


import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import pt.ist.fenixWebFramework.services.Service;

public class CancelEvent {

    @Service
    public static void run(final Event event, final Person responsible, final String justification) {
        event.cancel(responsible, justification);
    }
}