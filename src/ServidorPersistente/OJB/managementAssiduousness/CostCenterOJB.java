/*
 * Created on 15/Dez/2004
 */
package ServidorPersistente.OJB.managementAssiduousness;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CostCenter;
import Dominio.ICostCenter;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.managementAssiduousness.IPersistentCostCenter;


/**
 * @author Tânia Pousão
 *
 */
public class CostCenterOJB extends PersistentObjectOJB implements IPersistentCostCenter{

    public CostCenterOJB() {
        super();
    }

    public Integer countAllCostCenter() throws Exception {
        Criteria criteria = new Criteria(); 
        return new Integer(count(CostCenter.class, criteria));
    }

    public List readAll() throws Exception {
        Criteria criteria = new Criteria();
        
        return queryList(CostCenter.class, criteria, "code", true);
    }

    public ICostCenter readCostCenterByCode(String code) throws Exception {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("code", code);
        return (ICostCenter) queryObject(CostCenter.class, criteria);
    }
}