/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.domain;


/**
 * @author joaosa & rmalo
 */
 
public class AttendInAttendsSet extends DomainObject implements IAttendInAttendsSet {
		
	
	private Integer keyAttend;
	private Integer keyAttendsSet;
	private IAttends attend;
	private IAttendsSet attendsSet; 

	
	/** 
	 * Construtor
	 */
	public AttendInAttendsSet() {}
	
	/** 
 	* Construtor
 	*/
	public AttendInAttendsSet(Integer idInternal){
			setIdInternal(idInternal);
		}
	
	/** 
	 * Construtor
	 */
	public AttendInAttendsSet(IAttends attend,IAttendsSet attendsSet) {
			this.attend = attend;
			this.attendsSet = attendsSet;
	}
	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof IAttendInAttendsSet) {
			result =(getAttend().equals(((IAttendInAttendsSet) arg0).getAttend()))&&
					(getAttendsSet().equals(((IAttendInAttendsSet) arg0).getAttendsSet()));
		} 
		return result;		
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[AttendInAttendsSet";
		result += ", attend=" + getAttend();
		result += ", attendsSet=" + getAttendsSet();
		result += "]";
		return result;
	}
		
	
	/**
	 * @return Integer
	 */
	public Integer getKeyAttend() {
		return keyAttend;
	}
	
	/**
	 * @return Attends
	 */
	public IAttends getAttend() {
		return attend;
	}
	
	/**
	 * @return Integer
	 */
	public Integer getKeyAttendsSet() {
		return keyAttendsSet;
	}
	
	/**
	 * @return AttendsSet
	 */
	public IAttendsSet getAttendsSet() {
		return attendsSet;
	}
	
	/**
	* Sets the keyAttend.
	* @param keyAttend
	*/
	public void setKeyAttend(Integer keyAttend) {
		this.keyAttend=keyAttend;
	}
		
	/**
	* Sets the attend.
	* @param attend The attend to set
	*/
	public void setAttend(IAttends attend) {
		this.attend=attend;
	}		
	
	/**
	* Sets the keyAttendsSet.
	* @param keyAttendsSet
	*/
	public void setKeyAttendsSet(Integer keyAttendsSet) {
		this.keyAttendsSet=keyAttendsSet;
	}
	
	/**
	* Sets the attendsSet.
	* @param attendsSet The attendsSet to set
	*/
	public void setAttendsSet(IAttendsSet attendsSet) {
		this.attendsSet=attendsSet;
	}		
}
