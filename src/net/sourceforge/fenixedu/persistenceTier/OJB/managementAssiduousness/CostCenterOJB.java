package net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness;

import net.sourceforge.fenixedu.domain.CostCenter;
import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;

import org.apache.ojb.broker.query.Criteria;

public class CostCenterOJB extends PersistentObjectOJB implements IPersistentCostCenter {

    public Integer countAllCostCenter() {
        Criteria criteria = new Criteria();
        return new Integer(count(CostCenter.class, criteria));
    }

    public ICostCenter readCostCenterByCode(String code) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("code", code);
        return (ICostCenter) queryObject(CostCenter.class, criteria);
    }

}
