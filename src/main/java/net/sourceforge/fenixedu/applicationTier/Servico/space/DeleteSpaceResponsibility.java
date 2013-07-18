package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteSpaceResponsibility {

    @Checked("RolePredicates.SPACE_MANAGER_PREDICATE")
    @Service
    public static Boolean run(SpaceResponsibility spaceResponsibility) {
        if (spaceResponsibility != null) {
            spaceResponsibility.delete();
        }
        return true;
    }
}