/*
 * Created on 6/Mai/2003
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author asnr and scpo
 *
 */
public interface IStudentGroup extends IDomainObject{
	
	public Integer getGroupNumber() ;

	public IAttendsSet getAttendsSet() ;

	public IShift getShift();
	
	public void setGroupNumber(Integer groupNumber);

	public void setAttendsSet(IAttendsSet attendsSet);

	public void setShift(IShift shift);	
}
