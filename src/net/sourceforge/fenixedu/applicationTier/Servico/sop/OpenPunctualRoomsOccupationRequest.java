package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;

public class OpenPunctualRoomsOccupationRequest extends Service {

    public void run(PunctualRoomsOccupationRequest request, Person person, boolean reOpen) {
	if (request != null && !reOpen) {
	    request.openRequest(person);
	} else if (request != null && reOpen) {
	    request.reOpenRequest(person);
	}
    }
}
