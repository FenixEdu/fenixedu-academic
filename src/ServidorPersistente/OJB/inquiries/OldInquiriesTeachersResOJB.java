/*
 * Created on Feb 4, 2005
 * 
 */
package ServidorPersistente.OJB.inquiries;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.inquiries.IOldInquiriesTeachersRes;
import Dominio.inquiries.OldInquiriesTeachersRes;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.inquiries.IPersistentOldInquiriesTeachersRes;

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
