package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteBlueprintVersion {

    @Atomic
    public static void run(Blueprint blueprint) {
        check(RolePredicates.SPACE_MANAGER_PREDICATE);
        if (blueprint == null) {
            throw new DomainException("error.delete.blueprint.no.blueprint");
        }
        blueprint.delete();
    }
}