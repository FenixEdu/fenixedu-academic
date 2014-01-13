/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class TestScope extends TestScope_Base {

    public TestScope() {
        setRootDomainObject(Bennu.getInstance());
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.Test> getTests() {
        return getTestsSet();
    }

    @Deprecated
    public boolean hasAnyTests() {
        return !getTestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.DistributedTest> getDistributedTests() {
        return getDistributedTestsSet();
    }

    @Deprecated
    public boolean hasAnyDistributedTests() {
        return !getDistributedTestsSet().isEmpty();
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
