package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.material.Material;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteMaterial extends FenixService {

    @Checked("RolePredicates.RESOURCE_MANAGER_PREDICATE")
    @Service
    public static void run(Material material) {
        if (material != null) {
            material.delete();
        }
    }
}