/*
 * Created on Mar 10, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.course.inquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InfoOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesCoursesRes;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiryCoursesResByExecutionPeriodAndDegreeIdAndCourseCode extends FenixService {

    @Checked("RolePredicates.TEACHER_PREDICATE")
    @Service
    public static InfoOldInquiriesCoursesRes run(Integer executionPeriodId, Integer degreeId, String courseCode)
            throws FenixServiceException, NoSuchMethodException, InvocationTargetException, NoSuchMethodException,
            IllegalAccessException {
        InfoOldInquiriesCoursesRes oldInquiriesCoursesRes = null;

        Degree degree = rootDomainObject.readDegreeByOID(degreeId);
        ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);

        if (executionSemester == null) {
            throw new FenixServiceException("nullExecutionPeriodId");
        }
        if (degree == null) {
            throw new FenixServiceException("nullDegreeId");
        }
        if (courseCode == null) {
            throw new FenixServiceException("nullCourseCode");
        }

        OldInquiriesCoursesRes oics =
                degree.getOldInquiriesCoursesResByCourseCodeAndExecutionPeriod(courseCode, executionSemester);

        oldInquiriesCoursesRes = new InfoOldInquiriesCoursesRes();
        oldInquiriesCoursesRes.copyFromDomain(oics);

        return oldInquiriesCoursesRes;
    }
}