/*
 * Created on 9/Mai/2003
 *
 */
package Dominio;

/**
 * @author asnr and scpo
 *  
 */
public interface IStudentGroupAttend extends IDomainObject {
    public IStudentGroup getStudentGroup();

    public IFrequenta getAttend();

    public void setStudentGroup(IStudentGroup studentGroup);

    public void setAttend(IFrequenta attend);

    public Integer getKeyStudentGroup();

}