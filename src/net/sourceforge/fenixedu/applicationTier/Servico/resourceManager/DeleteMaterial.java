package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.material.Material;

public class DeleteMaterial extends FenixService {

    @Checked("RolePredicates.RESOURCE_MANAGER_PREDICATE")
    @Service
    public static void run(Material material) {
	if (material != null) {
	    material.delete();
	}
    }
}