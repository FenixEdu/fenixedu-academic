/*
 * Created on Jan 21, 2004
 */
package ServidorPersistente.OJB.grant.contract;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantCostCenter;
import Dominio.grant.contract.GrantPaymentEntity;
import Dominio.grant.contract.GrantProject;
import Dominio.grant.contract.IGrantPaymentEntity;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.grant.IPersistentGrantPaymentEntity;

/**
 * @author pica
 * @author barbosa
 */
public class GrantPaymentEntityOJB extends ObjectFenixOJB implements IPersistentGrantPaymentEntity {

    public IGrantPaymentEntity readByNumberAndClass(String entityNumber, String entityClass)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", entityNumber);
        criteria.addEqualTo("ojbConcreteClass", entityClass);
        IGrantPaymentEntity paymentEntity = paymentEntity = (IGrantPaymentEntity) queryObject(
                GrantPaymentEntity.class, criteria);
        return paymentEntity;
    }

    public List readAllPaymentEntitiesByClassName(String className) throws ExcepcaoPersistencia {
        List result = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("ojbConcreteClass", className);

        if (className.equals(GrantPaymentEntity.getGrantCostCenterOjbConcreteClass()))
            result = queryList(GrantCostCenter.class, criteria);
        if (className.equals(GrantPaymentEntity.getGrantProjectOjbConcreteClass()))
            result = queryList(GrantProject.class, criteria);

        return result;
    }
}