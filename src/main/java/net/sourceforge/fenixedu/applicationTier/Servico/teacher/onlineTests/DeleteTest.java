package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteTest {

    protected void run(final Integer executionCourseId, final Integer testId) throws InvalidArgumentsServiceException {
        Test test = RootDomainObject.getInstance().readTestByOID(testId);
        test.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTest serviceInstance = new DeleteTest();

    @Service
    public static void runDeleteTest(Integer executionCourseId, Integer testId) throws InvalidArgumentsServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, testId);
    }

}