package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;


import net.sourceforge.fenixedu.domain.material.Material;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteMaterial {

    @Atomic
    public static void run(Material material) {
        check(RolePredicates.RESOURCE_MANAGER_PREDICATE);
        if (material != null) {
            material.delete();
        }
    }
}