package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class GrantPaymentEntityVO extends VersionedObjectsBase implements IPersistentGrantPaymentEntity {

    public IGrantPaymentEntity readByNumberAndClass(String entityNumber, String entityClass)
            throws ExcepcaoPersistencia {
        List<IGrantPaymentEntity> grantPaymentEntities = (List<IGrantPaymentEntity>) readAll(GrantPaymentEntity.class);

        for (IGrantPaymentEntity entity : grantPaymentEntities) {
            if (entity.getNumber().equals(entityNumber)
                    && entity.getOjbConcreteClass().equals(entityClass)) {
                return entity;
            }
        }

        return null;
    }

    public List readAllPaymentEntitiesByClassName(String className) throws ExcepcaoPersistencia {
        List<IGrantPaymentEntity> grantPaymentEntities = (List<IGrantPaymentEntity>) readAll(GrantPaymentEntity.class);

        List<IGrantPaymentEntity> result = null;
        for (IGrantPaymentEntity grantPaymentEntity : grantPaymentEntities) {
            if (grantPaymentEntity.getOjbConcreteClass().equals(className)) {
                result.add(grantPaymentEntity);
            }
        }

        return result;
    }

}