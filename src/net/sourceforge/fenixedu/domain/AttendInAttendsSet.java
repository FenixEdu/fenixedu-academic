/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.domain;


/**
 * @author joaosa & rmalo
 */
 
public class AttendInAttendsSet extends AttendInAttendsSet_Base {

	
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
			super.setAttend(attend);
			super.setAttendsSet(attendsSet);
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
		
}
