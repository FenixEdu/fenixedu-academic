/*
 * Created on 6/Mai/2003
 *
 */
package Dominio;

/**
 * @author asnr and scpo
 *  
 */
public interface IStudentGroup extends IDomainObject {

    public Integer getGroupNumber();

    public IGroupProperties getGroupProperties();

    public ITurno getShift();

    public void setGroupNumber(Integer groupNumber);

    public void setGroupProperties(IGroupProperties groupProperties);

    public void setShift(ITurno shift);

}