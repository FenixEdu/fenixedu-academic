package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class DeleteTest {

    protected void run(final String executionCourseId, final String testId) throws InvalidArgumentsServiceException {
        Test test = FenixFramework.getDomainObject(testId);
        test.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTest serviceInstance = new DeleteTest();

    @Service
    public static void runDeleteTest(String executionCourseId, String testId) throws InvalidArgumentsServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, testId);
    }

}