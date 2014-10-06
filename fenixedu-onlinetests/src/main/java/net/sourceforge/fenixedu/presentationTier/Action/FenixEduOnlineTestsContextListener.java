package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.MergeExecutionCourses;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import pt.ist.fenixframework.FenixFramework;

@WebListener
public class FenixEduOnlineTestsContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MergeExecutionCourses.registerMergeHandler(FenixEduOnlineTestsContextListener::copyDistributedTestStuff);
        FenixFramework.getDomainModel().registerDeletionListener(ExecutionCourse.class, (executionCourse) -> {
            for (; !executionCourse.getMetadatasSet().isEmpty(); executionCourse.getMetadatasSet().iterator().next().delete()) {
                ;
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    private static void copyDistributedTestStuff(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {
        for (final Metadata metadata : executionCourseFrom.getMetadatasSet()) {
            metadata.setExecutionCourse(executionCourseTo);
        }
        List<DistributedTest> distributedTests = TestScope.readDistributedTestsByTestScope(executionCourseFrom);
        for (final DistributedTest distributedTest : distributedTests) {
            final TestScope testScope = distributedTest.getTestScope();
            testScope.setExecutionCourse(executionCourseTo);
        }
    }
}
