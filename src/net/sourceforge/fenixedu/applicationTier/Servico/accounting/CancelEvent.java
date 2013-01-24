package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CancelEvent extends FenixService {

    @Checked("AcademicPredicates.MANAGE_PAYMENTS")
    @Service
    public static void run(final Event event, final Person responsible, final String justification) {
	event.cancel(responsible, justification);
    }
}