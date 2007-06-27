package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.material.Material;

public class DeleteMaterial extends Service {

    public void run(Material material) {
	if(material != null) {
	    material.delete();
	}
    }    
}
