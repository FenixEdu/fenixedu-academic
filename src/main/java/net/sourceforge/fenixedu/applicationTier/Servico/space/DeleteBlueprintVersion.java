package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class DeleteBlueprintVersion {

    @Checked("RolePredicates.SPACE_MANAGER_PREDICATE")
    @Atomic
    public static void run(Blueprint blueprint) {
        if (blueprint == null) {
            throw new DomainException("error.delete.blueprint.no.blueprint");
        }
        blueprint.delete();
    }
}