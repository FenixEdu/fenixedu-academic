package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.material.Material;

public class DeleteMaterial extends FenixService {

    public void run(Material material) {
	if (material != null) {
	    material.delete();
	}
    }
}
