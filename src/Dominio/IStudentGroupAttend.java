/*
 * Created on 9/Mai/2003
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
public interface IStudentGroupAttend extends IDomainObject{
	public IStudentGroup getStudentGroup();
	public IFrequenta getAttend() ;

	public void setStudentGroup(IStudentGroup studentGroup);
	public void setAttend(IFrequenta attend);

}
