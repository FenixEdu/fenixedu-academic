/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.domain;


/**
 * @author joaosa & rmalo
 */
 
public interface IAttendInAttendsSet extends IDomainObject {
		
	public Integer getKeyAttend();
	public IAttends getAttend();
	public Integer getKeyAttendsSet();
	public IAttendsSet getAttendsSet();
	public void setKeyAttend(Integer keyAttend);
	public void setAttend(IAttends attend);
	public void setKeyAttendsSet(Integer keyAttendsSet);
	public void setAttendsSet(IAttendsSet attendsSet);
	
}
