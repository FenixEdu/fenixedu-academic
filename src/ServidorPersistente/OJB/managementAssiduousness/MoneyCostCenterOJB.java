/*
 * Created on 15/Dez/2004
 */
package ServidorPersistente.OJB.managementAssiduousness;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICostCenter;
import Dominio.managementAssiduousness.IMoneyCostCenter;
import Dominio.managementAssiduousness.MoneyCostCenter;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.managementAssiduousness.IPersistentMoneyCostCenter;

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