/*
 * Created on 15/Dez/2004
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IMoneyCostCenter;
import net.sourceforge.fenixedu.domain.managementAssiduousness.MoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentMoneyCostCenter;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Tânia Pousão
 *
 */
public class MoneyCostCenterOJB extends PersistentObjectOJB implements IPersistentMoneyCostCenter{

    public MoneyCostCenterOJB() {
        super();
    }

    public List readAllByYear(Integer year) throws Exception {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("year", year);
        
        return queryList(MoneyCostCenter.class, criteria);
    }

    public IMoneyCostCenter readByCostCenterAndYear(ICostCenter costCenter, Integer year) throws Exception {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("costCenterKey", costCenter.getIdInternal());
        criteria.addEqualTo("year", year);
        
        return (IMoneyCostCenter) queryObject(MoneyCostCenter.class, criteria);
    }   
}