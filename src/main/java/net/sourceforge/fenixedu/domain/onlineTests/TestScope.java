/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

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
        setKeyClass(object.getExternalId());
    }

    public TestScope(String className, String classId) {
        this();
        setClassName(className);
        setKeyClass(classId);
    }

    public void setDomainObject(DomainObject domainObject) {
        this.domainObject = domainObject;

    }

    public DomainObject getDomainObject() {
        if (domainObject == null && getClassName().equals(ExecutionCourse.class.getName())) {
            ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(getKeyClass());
            setDomainObject(executionCourse);
        }
        return domainObject;
    }

    public static TestScope readByDomainObject(Class clazz, String externalId) {
        for (final TestScope testScope : RootDomainObject.getInstance().getTestScopes()) {
            if (testScope.getKeyClass().equals(externalId) && testScope.getClassName().equals(clazz.getName())) {
                return testScope;
            }
        }
        return null;
    }

    public static List<DistributedTest> readDistributedTestsByTestScope(Class clazz, String externalId) {
        List<DistributedTest> result = new ArrayList<DistributedTest>();
        TestScope testScope = readByDomainObject(clazz, externalId);
        if (testScope != null) {
            result.addAll(testScope.getDistributedTests());
        }
        return result;
    }

}
