/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class TestScope extends TestScope_Base {

    private DomainObject domainObject;

    public TestScope() {
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public TestScope(DomainObject object) {
        this();
        setDomainObject(object);
        setClassName(object.getClass().getName());
        setKeyClass(object.getIdInternal());
    }

    public TestScope(String className, Integer classId) {
        this();
        setClassName(className);
        setKeyClass(classId);
    }

    public void setDomainObject(DomainObject domainObject) {
        this.domainObject = domainObject;

    }

    public DomainObject getDomainObject() {
        if (domainObject == null && getClassName().equals(ExecutionCourse.class.getName())) {
            ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(
                    getKeyClass());
            setDomainObject(executionCourse);
        }
        return domainObject;
    }

    public static TestScope readByDomainObject(Class clazz, Integer idInternal) {
        for (final TestScope testScope : RootDomainObject.getInstance().getTestScopes()) {
            if (testScope.getClassName().equals(clazz.getName())
                    && testScope.getKeyClass().equals(idInternal)) {
                return testScope;
            }
        }
        return null;
    }

    public static List<DistributedTest> readDistributedTestsByTestScope(Class clazz, Integer idInternal) {
        List<DistributedTest> result = new ArrayList<DistributedTest>();
        TestScope testScope = readByDomainObject(clazz, idInternal);
        if (testScope != null) {
            result.addAll(testScope.getDistributedTests());
        }
        return result;
    }

}
