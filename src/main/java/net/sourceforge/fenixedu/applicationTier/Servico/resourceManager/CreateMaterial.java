package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;


import net.sourceforge.fenixedu.dataTransferObject.resourceManager.MaterialBean;
import net.sourceforge.fenixedu.dataTransferObject.resourceManager.MaterialBean.MaterialType;
import net.sourceforge.fenixedu.domain.material.Extension;
import net.sourceforge.fenixedu.domain.material.FireExtinguisher;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class CreateMaterial {

    @Checked("RolePredicates.RESOURCE_MANAGER_PREDICATE")
    @Atomic
    public static void run(MaterialBean bean) {

        if (bean != null) {

            MaterialType materialType = bean.getMaterialType();
            if (materialType != null) {

                switch (materialType) {

                case EXTENSION:
                    new Extension(bean.getIdentification(), bean.getBarCodeNumber(), bean.getAcquisition(), bean.getCease(),
                            bean.getOwner());
                    break;

                case FIRE_EXTINGUISHER:
                    new FireExtinguisher(bean.getIdentification(), bean.getBarCodeNumber(), bean.getAcquisition(),
                            bean.getCease(), bean.getOwner(), bean.getDelivererEnterprise(), bean.getLoadedDate(),
                            bean.getToBeInspectedDate());
                    break;

                default:
                    break;
                }
            }
        }
    }
}