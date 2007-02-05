package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.GenericEvent;

public class DeleteGenericEvent extends Service {

    public void run(GenericEvent genericEvent) {
	if(genericEvent != null) {
	    genericEvent.delete();
	}
    }
}
