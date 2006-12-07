package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.UnitSpaceOccupation;

public class DeleteUnitSpaceOccupation extends Service {

    public void run(UnitSpaceOccupation unitSpaceOccupation) {
	if(unitSpaceOccupation != null) {
	    unitSpaceOccupation.delete();
	}
    }
}
