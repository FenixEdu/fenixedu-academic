/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract;

import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author pica
 * @author barbosa
 */
public class GrantCostCenterOJB extends PersistentObjectOJB implements IPersistentGrantCostCenter {

    public IGrantCostCenter readGrantCostCenterByNumber(String number) throws ExcepcaoPersistencia {
        IGrantCostCenter grantCostCenter = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", number);
        criteria.addEqualTo("class_name", GrantPaymentEntity.getGrantCostCenterOjbConcreteClass());
        grantCostCenter = (IGrantCostCenter) queryObject(GrantCostCenter.class, criteria);
        return grantCostCenter;
    }
}