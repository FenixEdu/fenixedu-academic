package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;

import org.joda.time.DateTime;

public class ClosePunctualRoomsOccupationRequest extends FenixService {

    public void run(PunctualRoomsOccupationRequest request, Person person) {
	if (request != null) {
	    request.closeRequestAndAssociateOwnerOnlyForEmployees(new DateTime(), person);
	}
    }
}
