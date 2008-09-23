package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.UnitSpaceOccupation;

public class DeleteUnitSpaceOccupation extends FenixService {

    public void run(UnitSpaceOccupation unitSpaceOccupation) {
	if (unitSpaceOccupation != null) {
	    unitSpaceOccupation.delete();
	}
    }
}
