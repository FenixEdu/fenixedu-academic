package net.sourceforge.fenixedu.applicationTier.Servico.space;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteSpaceResponsibility {

    @Atomic
    public static Boolean run(SpaceResponsibility spaceResponsibility) {
        check(RolePredicates.SPACE_MANAGER_PREDICATE);
        if (spaceResponsibility != null) {
            spaceResponsibility.delete();
        }
        return true;
    }
}