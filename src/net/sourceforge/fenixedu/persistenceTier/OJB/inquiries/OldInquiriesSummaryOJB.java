/*
 * Created on Nov 15, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesSummary;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author João Fialho & Rita Ferreira
 *
 */

public class OldInquiriesSummaryOJB extends PersistentObjectOJB implements
		IPersistentOldInquiriesSummary {
	
    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(OldInquiriesSummary.class, criteria);
    }
    
    public List readByDegreeIdAndExecutionPeriod(Integer degreeID, Integer executionPeriodID) throws ExcepcaoPersistencia {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("keyDegree", degreeID);
    	criteria.addEqualTo("keyExecutionPeriod", executionPeriodID);
    	
    	return queryList(OldInquiriesSummary.class, criteria);
    }

    public List readByDegreeId(Integer degreeID) throws ExcepcaoPersistencia {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("keyDegree", degreeID);

    	return queryList(OldInquiriesSummary.class, criteria);
    }

}
