/*
 * Created on Mar 10, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.course.inquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiryCoursesResByExecutionPeriodAndDegreeIdAndCourseCode extends Service {

    public InfoOldInquiriesCoursesRes run(Integer executionPeriodId, Integer degreeId, String courseCode)
            throws FenixServiceException, ExcepcaoPersistencia, NoSuchMethodException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        InfoOldInquiriesCoursesRes oldInquiriesCoursesRes = null;
        
        Degree degree = rootDomainObject.readDegreeByOID(degreeId);
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);

        if (executionPeriod == null) {
            throw new FenixServiceException("nullExecutionPeriodId");
        }
        if (degree == null) {
            throw new FenixServiceException("nullDegreeId");
        }
        if (courseCode == null) {
            throw new FenixServiceException("nullCourseCode");
        }

        OldInquiriesCoursesRes oics = degree.getOldInquiriesCoursesResByCourseCodeAndExecutionPeriod(courseCode, executionPeriod);

        oldInquiriesCoursesRes = new InfoOldInquiriesCoursesRes();
        oldInquiriesCoursesRes.copyFromDomain(oics);

        return oldInquiriesCoursesRes;
    }
}
