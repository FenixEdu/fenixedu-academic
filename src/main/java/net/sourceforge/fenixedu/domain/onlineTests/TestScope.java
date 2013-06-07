/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class TestScope extends TestScope_Base {

    public TestScope() {
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public TestScope(ExecutionCourse executionCourse) {
        this();
        setExecutionCourse(executionCourse);
    }

    public static List<DistributedTest> readDistributedTestsByTestScope(ExecutionCourse executionCourse) {
        List<DistributedTest> result = new ArrayList<DistributedTest>();
        TestScope testScope = executionCourse.getTestScope();
        if (testScope != null) {
            result.addAll(testScope.getDistributedTests());
        }
        return result;
    }

}
