/*
 * Created on Feb 4, 2005
 * 
 */
package ServidorPersistente.OJB.inquiries;

import org.apache.ojb.broker.query.Criteria;

import Dominio.inquiries.IOldInquiriesCoursesRes;
import Dominio.inquiries.OldInquiriesCoursesRes;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.inquiries.IPersistentOldInquiriesCoursesRes;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class OldInquiriesCoursesResOJB extends PersistentObjectOJB implements IPersistentOldInquiriesCoursesRes {

    public IOldInquiriesCoursesRes readByExecutionPeriodAndDegreeIdAndCourseCode(
            Integer executionPeriodId, Integer degreeId, String courseCode)
    		throws ExcepcaoPersistencia {
        
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("keyExecutionPeriod", executionPeriodId);
    	criteria.addEqualTo("keyDegree", degreeId);
    	criteria.addEqualTo("courseCode", courseCode);

    	return (IOldInquiriesCoursesRes) queryObject(OldInquiriesCoursesRes.class, criteria);
    }
    
}
