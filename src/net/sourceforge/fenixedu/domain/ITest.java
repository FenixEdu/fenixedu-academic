/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

/**
 * @author Susana Fernandes
 */
public interface ITest extends IDomainObject {
    public abstract ITestScope getTestScope();

    public abstract Integer getKeyTestScope();

    public abstract Integer getNumberOfQuestions();

    public abstract String getTitle();

    public abstract void setTestScope(ITestScope TestScope);

    public abstract void setKeyTestScope(Integer integer);

    public abstract void setNumberOfQuestions(Integer integer);

    public abstract void setTitle(String string);

    public abstract Date getCreationDate();

    public abstract Date getLastModifiedDate();

    public abstract void setCreationDate(Date date);

    public abstract void setLastModifiedDate(Date date);

    public abstract String getInformation();

    public abstract void setInformation(String string);
}