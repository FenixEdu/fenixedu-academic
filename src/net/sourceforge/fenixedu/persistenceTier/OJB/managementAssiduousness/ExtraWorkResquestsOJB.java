/*
 * Created on 29/Jan/2005
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.managementAssiduousness.ExtraWorkRequests;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkRequests;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Tânia Pousão
 *
 */
public class ExtraWorkResquestsOJB extends PersistentObjectOJB implements IPersistentExtraWorkRequests {

    public IExtraWorkRequests readExtraWorkRequestByDay(Date day, Integer employeeID) throws Exception {
        Criteria criteria = new Criteria();
        criteria.addLessOrEqualThan("beginDate", day);
        criteria.addGreaterOrEqualThan("endDate", day);
        criteria.addEqualTo("employeeKey", employeeID);        
        
        return (IExtraWorkRequests) queryObject(ExtraWorkRequests.class, criteria);
    }

    public List readExtraWorkRequestBetweenDays(Date beginDay, Date lastDay) throws Exception {
        Criteria criteria = new Criteria();
        criteria.addBetween("beginDate", beginDay, lastDay);
        criteria.addBetween("endDate", beginDay, lastDay);
        
        return (List) queryList(ExtraWorkRequests.class, criteria);
    }   
    
    public List readExtraWorkRequestBetweenDaysAndByCC(Date beginDay, Date lastDay, Integer costCenterId, Integer costCenterMoneyId) throws Exception {
        Criteria criteria = new Criteria();
        criteria.addBetween("beginDate", beginDay, lastDay);
        criteria.addBetween("endDate", beginDay, lastDay);
        criteria.addEqualTo("costCenterExtraWorkKey", costCenterId);
        criteria.addEqualTo("costCenterMoneyKey", costCenterMoneyId);
        
        
        return (List) queryList(ExtraWorkRequests.class, criteria);
    }
}
