package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class OpenEvent extends Service {

    public OpenEvent() {
	super();
    }

    public void run(final Event event) {
	event.open();
    }

}