/*
 * Created on Jan 21, 2004
 */
package ServidorPersistente.OJB.grant.contract;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantCostCenter;
import Dominio.grant.contract.GrantPaymentEntity;
import Dominio.grant.contract.IGrantCostCenter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.grant.IPersistentGrantCostCenter;

/**
 * @author pica
 * @author barbosa
 */
public class GrantCostCenterOJB extends ObjectFenixOJB implements IPersistentGrantCostCenter {

    public IGrantCostCenter readGrantCostCenterByNumber(String number) throws ExcepcaoPersistencia {
        IGrantCostCenter grantCostCenter = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", number);
        criteria.addEqualTo("class_name", GrantPaymentEntity.getGrantCostCenterOjbConcreteClass());
        grantCostCenter = (IGrantCostCenter) queryObject(GrantCostCenter.class, criteria);
        return grantCostCenter;
    }
}