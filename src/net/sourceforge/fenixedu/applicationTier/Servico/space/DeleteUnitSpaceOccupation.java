package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.UnitSpaceOccupation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteUnitSpaceOccupation extends FenixService {

    @Checked("RolePredicates.SPACE_MANAGER_PREDICATE")
    @Service
    public static void run(UnitSpaceOccupation unitSpaceOccupation) {
        if (unitSpaceOccupation != null) {
            unitSpaceOccupation.delete();
        }
    }
}