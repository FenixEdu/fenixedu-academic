/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class OldInquiriesTeachersResOJB extends PersistentObjectOJB implements IPersistentOldInquiriesTeachersRes {

    public List readByTeacherNumber(Integer teacherNumber) throws ExcepcaoPersistencia {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("teacherNumber", teacherNumber);
    	
    	return queryList(OldInquiriesTeachersRes.class, criteria);
    }

    public List readByTeacherNumberAndExecutionPeriod(Integer teacherNumber, Integer executionPeriodId) throws ExcepcaoPersistencia {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("teacherNumber", teacherNumber);
    	criteria.addEqualTo("keyExecutionPeriod", executionPeriodId);
    	
    	return queryList(OldInquiriesTeachersRes.class, criteria);
    }

    public IOldInquiriesTeachersRes readByInternalId(Integer oldInquiryId) throws ExcepcaoPersistencia {
        IOldInquiriesTeachersRes oitr = (IOldInquiriesTeachersRes)readByOID(OldInquiriesTeachersRes.class, oldInquiryId);
        return oitr;
        
    }

    public List readByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber(
            Integer executionPeriodId, Integer degreeId, Integer curricularYear, String courseCode, Integer teacherNumber)
    		throws ExcepcaoPersistencia {
        
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("keyExecutionPeriod", executionPeriodId);
    	criteria.addEqualTo("keyDegree", degreeId);
    	criteria.addEqualTo("curricularYear", curricularYear);
    	criteria.addEqualTo("courseCode", courseCode);
    	criteria.addEqualTo("teacherNumber", teacherNumber);

    	return queryList(OldInquiriesTeachersRes.class, criteria);
    }
    
    public List readByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCode(
            Integer executionPeriodId, Integer degreeId, Integer curricularYear, String courseCode)
    		throws ExcepcaoPersistencia {
        
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("keyExecutionPeriod", executionPeriodId);
    	criteria.addEqualTo("keyDegree", degreeId);
    	criteria.addEqualTo("curricularYear", curricularYear);
    	criteria.addEqualTo("courseCode", courseCode);

    	return queryList(OldInquiriesTeachersRes.class, criteria);
    }
    
    public List readByDegreeId(Integer degreeId) throws ExcepcaoPersistencia {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("keyDegree", degreeId);
    	
    	return queryList(OldInquiriesTeachersRes.class, criteria);
    }

}
