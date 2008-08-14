package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.resourceManager.MaterialBean;
import net.sourceforge.fenixedu.dataTransferObject.resourceManager.MaterialBean.MaterialType;
import net.sourceforge.fenixedu.domain.material.Extension;
import net.sourceforge.fenixedu.domain.material.FireExtinguisher;

public class CreateMaterial extends Service {

    public void run(MaterialBean bean) {

	if (bean != null) {

	    MaterialType materialType = bean.getMaterialType();
	    if (materialType != null) {

		switch (materialType) {

		case EXTENSION:
		    new Extension(bean.getIdentification(), bean.getBarCodeNumber(), bean.getAcquisition(), bean.getCease(), bean
			    .getOwner());
		    break;

		case FIRE_EXTINGUISHER:
		    new FireExtinguisher(bean.getIdentification(), bean.getBarCodeNumber(), bean.getAcquisition(), bean
			    .getCease(), bean.getOwner(), bean.getDelivererEnterprise(), bean.getLoadedDate(), bean
			    .getToBeInspectedDate());
		    break;

		default:
		    break;
		}
	    }
	}
    }
}
