/*
 * Created on 27/Jun/2005 - 12:04:15
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class OldInquiriesCoursesResVO extends VersionedObjectsBase implements IPersistentOldInquiriesCoursesRes {

    public OldInquiriesCoursesRes readByExecutionPeriodAndDegreeIdAndCourseCode(
            Integer executionPeriodId, Integer degreeId, String courseCode)
    		throws ExcepcaoPersistencia {
        
		
		Degree degree = (Degree) readByOID(Degree.class, degreeId);
		List<OldInquiriesCoursesRes> inquiriesCoursesRes = degree.getAssociatedOldInquiriesCoursesRes();
		
		for(OldInquiriesCoursesRes ioicr : inquiriesCoursesRes) {
			if(ioicr.getExecutionPeriod().getIdInternal().equals(executionPeriodId) &&
					ioicr.getCourseCode().equalsIgnoreCase(courseCode)) {
				return ioicr;
			}
		}
		
		return null;
    }
    
}
