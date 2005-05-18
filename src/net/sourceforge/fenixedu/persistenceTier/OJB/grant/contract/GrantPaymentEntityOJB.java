package net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author pica
 * @author barbosa
 */
public class GrantPaymentEntityOJB extends PersistentObjectOJB implements IPersistentGrantPaymentEntity {

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