/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.inquiries;

import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesCoursesRes;

import org.apache.ojb.broker.query.Criteria;

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
