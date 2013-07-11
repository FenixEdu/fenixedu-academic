package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class DeleteSpaceResponsibility {

    @Checked("RolePredicates.SPACE_MANAGER_PREDICATE")
    @Atomic
    public static Boolean run(SpaceResponsibility spaceResponsibility) {
        if (spaceResponsibility != null) {
            spaceResponsibility.delete();
        }
        return true;
    }
}