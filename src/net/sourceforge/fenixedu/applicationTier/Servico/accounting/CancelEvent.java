package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class CancelEvent extends Service {

    public CancelEvent() {
	super();
    }

    public void run(final Event event, final Employee responsibleEmployee, final String justification) {
	event.cancel(responsibleEmployee, justification);
    }

}