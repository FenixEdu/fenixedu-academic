/*
 * Created on Jul 31, 2004
 *
 */
package Dominio;

import java.util.List;


/**
 * @author joaosa-rmalo
 *
 */

public interface IAttendsSet extends IDomainObject {
	
	public String getName();
	public List getAttendInAttendsSet();
	public List getStudentGroups();
	public Integer getKeyGroupProperties ();
	public IGroupProperties getGroupProperties ();
	public List getAttends();
	
	
	public void setName(String name);
	public void setAttendInAttendsSet(List attendsInAttendsSet);
	public void setStudentGroups (List studentGroups);
	public void setGroupProperties (IGroupProperties groupProperties);
	public void setKeyGroupProperties (Integer keyGroupProperties);
 
	public void addAttendInAttendsSet (IAttendInAttendsSet attendInAttendsSet);
	public void removeAttendInAttendsSet (IAttendInAttendsSet attendInAttendsSet);
	public boolean existsAttendInAttendsSet (IAttendInAttendsSet attendInAttendsSet);
	
	
	public void addStudentGroup (IStudentGroup studentGroup);
	public void removeStudentGroup (IStudentGroup studentGroup);
	public boolean existsStudentGroup (IStudentGroup studentGroup);
	
	public IFrequenta getStudentAttend(IStudent student);
	public List getStudentGroupsWithoutShift();
	public List getStudentGroupsWithShift();
	
}
