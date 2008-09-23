package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation;

public class DeletePersonSpaceOccupation extends FenixService {

    public boolean run(PersonSpaceOccupation personSpaceOccupation) {
	if (personSpaceOccupation != null) {
	    personSpaceOccupation.delete();
	}
	return true;
    }
}
