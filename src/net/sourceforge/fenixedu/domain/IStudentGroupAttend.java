/*
 * Created on 9/Mai/2003
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author asnr and scpo
 *  
 */
public interface IStudentGroupAttend extends IDomainObject {
    public IStudentGroup getStudentGroup();

    public IAttends getAttend();

    public void setStudentGroup(IStudentGroup studentGroup);

    public void setAttend(IAttends attend);

    public Integer getKeyStudentGroup();

}