package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionPeriod extends FenixService {

    protected InfoExecutionPeriod run(Integer executionCourseCode) throws FenixServiceException {
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
        ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
        return InfoExecutionPeriod.newInfoFromDomain(executionSemester);
    }

    // Service Invokers migrated from Berserk

    private static final ReadExecutionPeriod serviceInstance = new ReadExecutionPeriod();

    @Service
    public static InfoExecutionPeriod runReadExecutionPeriodByExecutionCourseId(Integer executionCourseCode) throws FenixServiceException  , NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode);
    }

}