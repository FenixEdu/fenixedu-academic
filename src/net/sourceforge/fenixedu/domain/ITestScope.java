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
public interface ITestScope extends IDomainObject {
    public abstract IDomainObject getDomainObject();

    public abstract void setDomainObject(IDomainObject object);

    public abstract Integer getKeyClass();

    public abstract String getClassName();

    public abstract void setKeyClass(Integer integer);

    public abstract void setClassName(String string);
}