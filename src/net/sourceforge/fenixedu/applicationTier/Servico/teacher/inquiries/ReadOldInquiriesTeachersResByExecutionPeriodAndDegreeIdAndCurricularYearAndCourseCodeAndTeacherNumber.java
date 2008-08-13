/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber extends
	Service {

    public List run(Integer executionPeriodId, Integer degreeId, Integer curricularYear, String courseCode, Integer teacherNumber)
	    throws FenixServiceException{

	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);

	Degree degree = rootDomainObject.readDegreeByOID(degreeId);

	Teacher teacher = Teacher.readByNumber(teacherNumber);

	if (executionSemester == null) {
	    throw new FenixServiceException("nullExecutionPeriodId");
	}
	if (degree == null) {
	    throw new FenixServiceException("nullDegreeId");
	}
	if (curricularYear == null) {
	    throw new FenixServiceException("nullCurricularYear");
	}
	if (courseCode == null) {
	    throw new FenixServiceException("nullCourseCode");
	}
	if (teacher == null) {
	    throw new FenixServiceException("nullTeacherNumber");
	}

	List<OldInquiriesTeachersRes> oldInquiriesTeachersResList = degree
		.getOldInquiriesTeachersResByExecutionPeriodAndCurricularYearAndCourseCodeAndTeacher(executionSemester,
			curricularYear, courseCode, teacher);

	CollectionUtils.transform(oldInquiriesTeachersResList, new Transformer() {

	    public Object transform(Object oldInquiriesTeachersRes) {
		InfoOldInquiriesTeachersRes ioits = new InfoOldInquiriesTeachersRes();
		try {
		    ioits.copyFromDomain((OldInquiriesTeachersRes) oldInquiriesTeachersRes);

		} catch (Exception ex) {
		}

		return ioits;
	    }
	});

	return oldInquiriesTeachersResList;
    }

}
