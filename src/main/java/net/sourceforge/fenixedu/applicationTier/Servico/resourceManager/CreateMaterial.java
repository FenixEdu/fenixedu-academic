package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.resourceManager.MaterialBean;
import net.sourceforge.fenixedu.dataTransferObject.resourceManager.MaterialBean.MaterialType;
import net.sourceforge.fenixedu.domain.material.Extension;
import net.sourceforge.fenixedu.domain.material.FireExtinguisher;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class CreateMaterial {

    @Atomic
    public static void run(MaterialBean bean) {
        check(RolePredicates.RESOURCE_MANAGER_PREDICATE);

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