package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import pt.ist.fenixframework.Atomic;

public class InsertTest {

    protected String run(ExecutionCourse executionCourse, String title, String information) {
        TestScope testScope = executionCourse.getTestScope();
        if (testScope == null) {
            testScope = new TestScope(executionCourse);
        }
        Test test = new Test(title, information, testScope);
        return test.getExternalId();
    }

    // Service Invokers migrated from Berserk

    private static final InsertTest serviceInstance = new InsertTest();

    @Atomic
    public static String runInsertTest(ExecutionCourse executionCourse, String title, String information)
            throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourse);
        return serviceInstance.run(executionCourse, title, information);
    }

}