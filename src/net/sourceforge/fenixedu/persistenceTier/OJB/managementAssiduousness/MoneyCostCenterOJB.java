package net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness;

import java.util.List;

import net.sourceforge.fenixedu.domain.CostCenter;
import net.sourceforge.fenixedu.domain.managementAssiduousness.MoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentMoneyCostCenter;

import org.apache.ojb.broker.query.Criteria;

public class MoneyCostCenterOJB extends PersistentObjectOJB implements IPersistentMoneyCostCenter {

    public List readAllByYear(Integer year) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("year", year);

        return queryList(MoneyCostCenter.class, criteria);
    }

    public MoneyCostCenter readByCostCenterAndYear(CostCenter costCenter, Integer year)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("costCenterKey", costCenter.getIdInternal());
        criteria.addEqualTo("year", year);

        return (MoneyCostCenter) queryObject(MoneyCostCenter.class, criteria);
    }

}
