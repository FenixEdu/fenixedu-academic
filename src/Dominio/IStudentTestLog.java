/*
 * Created on 10/Set/2003
 *
 */
package Dominio;

import java.util.Date;

/**
 * @author Susana Fernandes
 */
public interface IStudentTestLog extends IDomainObject {
    public abstract Date getDate();

    public abstract IDistributedTest getDistributedTest();

    public abstract String getEvent();

    public abstract Integer getKeyDistributedTest();

    public abstract Integer getKeyStudent();

    public abstract IStudent getStudent();

    public abstract void setDate(Date date);

    public abstract void setDistributedTest(IDistributedTest test);

    public abstract void setEvent(String string);

    public abstract void setKeyDistributedTest(Integer integer);

    public abstract void setKeyStudent(Integer integer);

    public abstract void setStudent(IStudent student);
}