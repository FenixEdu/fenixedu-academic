/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Susana Fernandes
 *  
 */
public class TestScope extends DomainObject implements ITestScope {
    private IDomainObject domainObject;

    private String className;

    private Integer keyClass;

    public TestScope() {
    }

    public TestScope(IDomainObject object) {
        super();
        setDomainObject(object);
        setClassName(object.getClass().getName());
        setKeyClass(object.getIdInternal());
    }

    public TestScope(Integer idInternal) {
        super(idInternal);
    }

    public IDomainObject getDomainObject() {
        return domainObject;
    }

    public void setDomainObject(IDomainObject object) {
        domainObject = object;
    }

    public Integer getKeyClass() {
        return keyClass;
    }

    public String getClassName() {
        return className;
    }

    public void setKeyClass(Integer integer) {
        keyClass = integer;
    }

    public void setClassName(String string) {
        className = string;
    }

}