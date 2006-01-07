/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class TestScope extends TestScope_Base {

    private DomainObject domainObject;

    public TestScope() {
    }

    public TestScope(DomainObject object) {
        super();
        setDomainObject(object);
        setClassName(object.getClass().getName());
        setKeyClass(object.getIdInternal());
    }

    public TestScope(String className, Integer classId) {
        super();
        setClassName(className);
        setKeyClass(classId);
    }

    public void setDomainObject(DomainObject domainObject) {
        this.domainObject = domainObject;

    }

    public DomainObject getDomainObject() {
        return domainObject;
    }
}