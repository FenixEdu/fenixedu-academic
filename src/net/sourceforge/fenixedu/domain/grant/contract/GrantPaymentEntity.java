/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public abstract class GrantPaymentEntity extends GrantPaymentEntity_Base {
    private static final String grantCostCenterOjbConcreteClass = GrantCostCenter.class.getName();
    private static final String grantProjectOjbConcreteClass = GrantProject.class.getName();

    public GrantPaymentEntity() {
        this.setOjbConcreteClass(this.getClass().getName());
    }

    public static String getGrantCostCenterOjbConcreteClass() {
        return grantCostCenterOjbConcreteClass;
    }

    public static String getGrantProjectOjbConcreteClass() {
        return grantProjectOjbConcreteClass;
    }

    public static GrantPaymentEntity findGrantPaymentEntityByNumberAndConcreteClass(String entityNumber, String entityClass) {
        for (final GrantPaymentEntity grantPaymentEntity : RootDomainObject.getInstance().getGrantPaymentEntitysSet()) {
            if (grantPaymentEntity.getNumber().equals(entityNumber) && grantPaymentEntity.getOjbConcreteClass().equals(entityClass)) {
                return grantPaymentEntity;
            }
        }
        return null;
    }

    public static Set<GrantPaymentEntity> findGrantPaymentEntityByConcreteClass(final String classname) {
        final Set<GrantPaymentEntity> grantPaymentEntities = new HashSet<GrantPaymentEntity>();
        for (final GrantPaymentEntity grantPaymentEntity : RootDomainObject.getInstance().getGrantPaymentEntitysSet()) {
            if (grantPaymentEntity.getClass().getName().equals(classname)) {
                grantPaymentEntities.add(grantPaymentEntity);
            }
        }
        return grantPaymentEntities;
    }

}