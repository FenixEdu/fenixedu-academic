package net.sourceforge.fenixedu.applicationTier.Servico.space;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.space.UnitSpaceOccupation;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteUnitSpaceOccupation {

    @Atomic
    public static void run(UnitSpaceOccupation unitSpaceOccupation) {
        check(RolePredicates.SPACE_MANAGER_PREDICATE);
        if (unitSpaceOccupation != null) {
            unitSpaceOccupation.delete();
        }
    }
}