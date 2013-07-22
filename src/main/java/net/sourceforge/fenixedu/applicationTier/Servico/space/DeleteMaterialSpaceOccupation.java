package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.domain.space.MaterialSpaceOccupation;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteMaterialSpaceOccupation {

    @Atomic
    public static void run(MaterialSpaceOccupation materialSpaceOccupation) {
        check(RolePredicates.SPACE_MANAGER_PREDICATE);
        if (materialSpaceOccupation != null) {
            materialSpaceOccupation.delete();
        }
    }
}