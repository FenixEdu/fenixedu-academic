package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteDistributedTest {

    protected void run(Integer executionCourseId, final Integer distributedTestId) {
        final DistributedTest distributedTest = RootDomainObject.getInstance().readDistributedTestByOID(distributedTestId);

        for (Metadata metadata : RootDomainObject.getInstance().getMetadatasSet()) {
            if (metadata.getVisibility() != null && !metadata.getVisibility().booleanValue()
                    && metadata.getQuestionsSet().size() == 0) {
                metadata.delete();
            }
        }

        distributedTest.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteDistributedTest serviceInstance = new DeleteDistributedTest();

    @Service
    public static void runDeleteDistributedTest(Integer executionCourseId, Integer distributedTestId)
            throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, distributedTestId);
    }

}