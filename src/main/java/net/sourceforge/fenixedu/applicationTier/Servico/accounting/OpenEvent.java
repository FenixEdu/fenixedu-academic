package net.sourceforge.fenixedu.applicationTier.Servico.accounting;


import net.sourceforge.fenixedu.domain.accounting.Event;
import pt.ist.fenixWebFramework.services.Service;

public class OpenEvent {

    @Service
    public static void run(final Event event) {
        event.open();
    }

}