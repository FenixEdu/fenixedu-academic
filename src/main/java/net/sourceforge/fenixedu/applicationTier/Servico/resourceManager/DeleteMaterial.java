package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;


import net.sourceforge.fenixedu.domain.material.Material;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class DeleteMaterial {

    @Checked("RolePredicates.RESOURCE_MANAGER_PREDICATE")
    @Atomic
    public static void run(Material material) {
        if (material != null) {
            material.delete();
        }
    }
}