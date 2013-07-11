package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.space.UnitSpaceOccupation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class DeleteUnitSpaceOccupation {

    @Checked("RolePredicates.SPACE_MANAGER_PREDICATE")
    @Atomic
    public static void run(UnitSpaceOccupation unitSpaceOccupation) {
        if (unitSpaceOccupation != null) {
            unitSpaceOccupation.delete();
        }
    }
}