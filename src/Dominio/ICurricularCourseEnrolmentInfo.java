/*
 * Created on 15/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

/**
 * @author dcs-rjao
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface ICurricularCourseEnrolmentInfo extends IDomainObject{
	public abstract Integer getMaxIncrementNac();
	public abstract Integer getMinIncrementNac();
	public Integer getWeigth();
	public abstract void setMaxIncrementNac(Integer integer);
	public abstract void setMinIncrementNac(Integer integer);
	public void setWeigth(Integer integer);
}