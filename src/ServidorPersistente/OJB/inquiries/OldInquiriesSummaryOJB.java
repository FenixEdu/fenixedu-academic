/*
 * Created on Nov 15, 2004
 *
 */
package ServidorPersistente.OJB.inquiries;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.inquiries.OldInquiriesSummary;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.inquiries.IPersistentOldInquiriesSummary;

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
    	criteria.addEqualTo("degreeKey", degreeID);
    	criteria.addEqualTo("keyExecutionPeriod", executionPeriodID);
    	
    	return queryList(OldInquiriesSummary.class, criteria);
    }

    public List readByDegreeId(Integer degreeID) throws ExcepcaoPersistencia {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("degreeKey", degreeID);

    	return queryList(OldInquiriesSummary.class, criteria);
    }

}
