package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;

public class OpenPunctualRoomsOccupationRequest extends Service {

    public void run(PunctualRoomsOccupationRequest request, Person person) {
	if(request != null) {
	    request.openRequestAndAssociateOwnerOnlyForEmployess(new DateTime(), person);	    
	}
    }
}
