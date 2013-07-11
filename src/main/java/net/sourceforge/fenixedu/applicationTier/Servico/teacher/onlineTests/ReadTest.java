package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class ReadTest {

    protected Test run(String executionCourseId, String testId) throws FenixServiceException {
        final Test test = FenixFramework.getDomainObject(testId);
        if (test == null) {
            throw new FenixServiceException();
        }
        return test;
    }

    // Service Invokers migrated from Berserk

    private static final ReadTest serviceInstance = new ReadTest();

    @Service
    public static Test runReadTest(String executionCourseId, String testId) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, testId);
    }

}