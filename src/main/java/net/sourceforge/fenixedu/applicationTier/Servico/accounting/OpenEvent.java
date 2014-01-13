package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.domain.accounting.Event;
import pt.ist.fenixframework.Atomic;

public class OpenEvent {

    @Atomic
    public static void run(final Event event) {
        event.open();
    }

}