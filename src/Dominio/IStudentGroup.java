/*
 * Created on 6/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IStudentGroup extends IDomainObject{
	
	public Integer getGroupNumber() ;
	public IGroupProperties getGroupProperties() ;
	public ITurno getShift();
	
	public void setGroupNumber(Integer groupNumber); 
	public void setGroupProperties(IGroupProperties groupProperties);
	public void setShift(ITurno shift);
	
}
