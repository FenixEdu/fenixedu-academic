/*
 * Created on 27/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

/**
 * @author Alexandra Alves
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface ICredits extends IDomainObject {
	public abstract Double getCredits();
	public abstract IExecutionPeriod getExecutionPeriod();
	public abstract ITeacher getTeacher();
	public abstract Integer getTfcStudentsNumber();
	String getAdditionalCreditsJustification();
	void setAdditionalCreditsJustification(String additionalCreditsJustification);

	Double getAdditionalCredits();
	void setAdditionalCredits(Double additionalCredits);
	
	public abstract void setCredits(Double double1);
	public abstract void setExecutionPeriod(IExecutionPeriod period);
	public abstract void setTeacher(ITeacher teacher);
	public abstract void setTfcStudentsNumber(Integer integer);
}