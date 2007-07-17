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

    public GrantPaymentEntity() {
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public static GrantPaymentEntity findGrantPaymentEntityByNumberAndConcreteClass(String entityNumber, String entityClass) {
        for (final GrantPaymentEntity grantPaymentEntity : RootDomainObject.getInstance().getGrantPaymentEntitysSet()) {
            if (grantPaymentEntity.getNumber().equals(entityNumber) && grantPaymentEntity.getClass().getName().equals(entityClass)) {
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