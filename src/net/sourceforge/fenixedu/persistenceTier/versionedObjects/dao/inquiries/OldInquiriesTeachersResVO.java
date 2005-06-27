/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class OldInquiriesTeachersResVO extends VersionedObjectsBase implements IPersistentOldInquiriesTeachersRes {

    public List readByTeacherNumber(Integer teacherNumber) throws ExcepcaoPersistencia {
		
		List<IOldInquiriesTeachersRes> inquiriesTeachersRes = (List<IOldInquiriesTeachersRes>) readAll(OldInquiriesTeachersRes.class);

		List<IOldInquiriesTeachersRes> res = new ArrayList<IOldInquiriesTeachersRes>();
		
		for(IOldInquiriesTeachersRes ioitr : inquiriesTeachersRes) {
			if(ioitr.getTeacherNumber().equals(teacherNumber))
				res.add(ioitr);
		}
		
		return res;
    }

    public List readByTeacherNumberAndExecutionPeriod(Integer teacherNumber, Integer executionPeriodId) throws ExcepcaoPersistencia {

		IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class, executionPeriodId);
		
		List<IOldInquiriesTeachersRes> inquiriesTeachersRes = (List<IOldInquiriesTeachersRes>) executionPeriod.getOldInquiriesTeachersRes();

		List<IOldInquiriesTeachersRes> res = new ArrayList<IOldInquiriesTeachersRes>();
		
		for(IOldInquiriesTeachersRes ioitr : inquiriesTeachersRes) {
			if(ioitr.getTeacherNumber().equals(teacherNumber))
				res.add(ioitr);
		}
		
		return res;

	}

    public IOldInquiriesTeachersRes readByInternalId(Integer oldInquiryId) throws ExcepcaoPersistencia {
        IOldInquiriesTeachersRes oitr = (IOldInquiriesTeachersRes)readByOID(OldInquiriesTeachersRes.class, oldInquiryId);
        return oitr;
        
    }

    public List readByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber(
            Integer executionPeriodId, Integer degreeId, Integer curricularYear, String courseCode, Integer teacherNumber)
    		throws ExcepcaoPersistencia {
        
		IDegree degree = (IDegree) readByOID(Degree.class, degreeId);
		
		List<IOldInquiriesTeachersRes> inquiriesTeachersRes = (List<IOldInquiriesTeachersRes>) degree.getOldInquiriesTeachersRes();

		List<IOldInquiriesTeachersRes> res = new ArrayList<IOldInquiriesTeachersRes>();
		
		for(IOldInquiriesTeachersRes ioitr : inquiriesTeachersRes) {
			if(ioitr.getTeacherNumber().equals(teacherNumber) &&
					ioitr.getExecutionPeriod().getIdInternal().equals(executionPeriodId) &&
					ioitr.getCurricularYear().equals(curricularYear) &&
					ioitr.getCourseCode().equalsIgnoreCase(courseCode))
				res.add(ioitr);
		}
		
		return res;
    }
    
    public List readByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCode(
            Integer executionPeriodId, Integer degreeId, Integer curricularYear, String courseCode)
    		throws ExcepcaoPersistencia {
        
		IDegree degree = (IDegree) readByOID(Degree.class, degreeId);
		
		List<IOldInquiriesTeachersRes> inquiriesTeachersRes = (List<IOldInquiriesTeachersRes>) degree.getOldInquiriesTeachersRes();

		List<IOldInquiriesTeachersRes> res = new ArrayList<IOldInquiriesTeachersRes>();
		
		for(IOldInquiriesTeachersRes ioitr : inquiriesTeachersRes) {
			if(ioitr.getExecutionPeriod().getIdInternal().equals(executionPeriodId) &&
					ioitr.getCurricularYear().equals(curricularYear) &&
					ioitr.getCourseCode().equalsIgnoreCase(courseCode))
				res.add(ioitr);
		}
		
		return res;
    }
    
    public List readByDegreeId(Integer degreeId) throws ExcepcaoPersistencia {
		IDegree degree = (IDegree) readByOID(Degree.class, degreeId);
		
		List<IOldInquiriesTeachersRes> inquiriesTeachersRes = (List<IOldInquiriesTeachersRes>) degree.getOldInquiriesTeachersRes();
		return inquiriesTeachersRes;
    }

}
