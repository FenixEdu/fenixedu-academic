/*
 * Created on Mar 10, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.course.inquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesCoursesRes;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiryCoursesResByExecutionPeriodAndDegreeIdAndCourseCode extends Service {

    public InfoOldInquiriesCoursesRes run(Integer executionPeriodId, Integer degreeId, String courseCode)
            throws FenixServiceException, ExcepcaoPersistencia, NoSuchMethodException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        InfoOldInquiriesCoursesRes oldInquiriesCoursesRes = null;

        if (executionPeriodId == null) {
            throw new FenixServiceException("nullExecutionPeriodId");
        }
        if (degreeId == null) {
            throw new FenixServiceException("nullDegreeId");
        }
        if (courseCode == null) {
            throw new FenixServiceException("nullCourseCode");
        }

        IPersistentOldInquiriesCoursesRes poics = persistentSupport.getIPersistentOldInquiriesCoursesRes();

        OldInquiriesCoursesRes oics = poics.readByExecutionPeriodAndDegreeIdAndCourseCode(
                executionPeriodId, degreeId, courseCode);

        oldInquiriesCoursesRes = new InfoOldInquiriesCoursesRes();
        oldInquiriesCoursesRes.copyFromDomain(oics);

        return oldInquiriesCoursesRes;
    }
}
