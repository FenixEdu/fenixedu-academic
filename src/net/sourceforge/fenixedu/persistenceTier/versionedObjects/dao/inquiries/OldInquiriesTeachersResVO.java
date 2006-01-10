/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
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
		
		List<OldInquiriesTeachersRes> inquiriesTeachersRes = (List<OldInquiriesTeachersRes>) readAll(OldInquiriesTeachersRes.class);

		List<OldInquiriesTeachersRes> res = new ArrayList<OldInquiriesTeachersRes>();
		
		for(OldInquiriesTeachersRes ioitr : inquiriesTeachersRes) {
			if(ioitr.getTeacherNumber().equals(teacherNumber))
				res.add(ioitr);
		}
		
		return res;
    }

    public List readByTeacherNumberAndExecutionPeriod(Integer teacherNumber, Integer executionPeriodId) throws ExcepcaoPersistencia {

		ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class, executionPeriodId);
		
		List<OldInquiriesTeachersRes> inquiriesTeachersRes = executionPeriod.getAssociatedOldInquiriesTeachersRes();

		List<OldInquiriesTeachersRes> res = new ArrayList<OldInquiriesTeachersRes>();
		
		for(OldInquiriesTeachersRes ioitr : inquiriesTeachersRes) {
			if(ioitr.getTeacherNumber().equals(teacherNumber))
				res.add(ioitr);
		}
		
		return res;

	}

    public OldInquiriesTeachersRes readByInternalId(Integer oldInquiryId) throws ExcepcaoPersistencia {
        OldInquiriesTeachersRes oitr = (OldInquiriesTeachersRes)readByOID(OldInquiriesTeachersRes.class, oldInquiryId);
        return oitr;
        
    }

    public List readByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber(
            Integer executionPeriodId, Integer degreeId, Integer curricularYear, String courseCode, Integer teacherNumber)
    		throws ExcepcaoPersistencia {
        
		Degree degree = (Degree) readByOID(Degree.class, degreeId);
		
		List<OldInquiriesTeachersRes> inquiriesTeachersRes = degree.getAssociatedOldInquiriesTeachersRes();

		List<OldInquiriesTeachersRes> res = new ArrayList<OldInquiriesTeachersRes>();
		
		for(OldInquiriesTeachersRes ioitr : inquiriesTeachersRes) {
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
        
		Degree degree = (Degree) readByOID(Degree.class, degreeId);
		
		List<OldInquiriesTeachersRes> inquiriesTeachersRes = degree.getAssociatedOldInquiriesTeachersRes();

		List<OldInquiriesTeachersRes> res = new ArrayList<OldInquiriesTeachersRes>();
		
		for(OldInquiriesTeachersRes ioitr : inquiriesTeachersRes) {
			if(ioitr.getExecutionPeriod().getIdInternal().equals(executionPeriodId) &&
					ioitr.getCurricularYear().equals(curricularYear) &&
					ioitr.getCourseCode().equalsIgnoreCase(courseCode))
				res.add(ioitr);
		}
		
		return res;
    }
    
    public List readByDegreeId(Integer degreeId) throws ExcepcaoPersistencia {
		Degree degree = (Degree) readByOID(Degree.class, degreeId);
		
		List<OldInquiriesTeachersRes> inquiriesTeachersRes = degree.getAssociatedOldInquiriesTeachersRes();
		return inquiriesTeachersRes;
    }

}
