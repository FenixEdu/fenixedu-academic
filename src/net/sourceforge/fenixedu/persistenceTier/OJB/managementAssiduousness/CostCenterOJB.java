/*
 * Created on 15/Dez/2004
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness;

import java.util.List;

import net.sourceforge.fenixedu.domain.CostCenter;
import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;

import org.apache.ojb.broker.query.Criteria;


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