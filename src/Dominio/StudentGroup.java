/*
 * Created on 9/Mai/2003
 *
 */
package Dominio;

/**
 * @author asnr and scpo
 */
public class StudentGroup extends DomainObject implements IStudentGroup {

    private Integer groupNumber;

    private Integer keyShift;

    private Integer keyAttendsSet;

    private IAttendsSet attendsSet;

    private IShift shift;
	
    /** 
     * Construtor
     */
    public StudentGroup() {
    }

    /**
     * Construtor
     */
    public StudentGroup(Integer idInternal) {
        setIdInternal(idInternal);
    }
	
	/** 
	 * Construtor
	 */
	public StudentGroup(Integer groupNumber,IAttendsSet attendsSet) {
			
		this.groupNumber = groupNumber;
		this.attendsSet = attendsSet;
	}
	
	/** 
	 * Construtor
	 */
	public StudentGroup(Integer groupNumber,IAttendsSet attendsSet,IShift shift) {
			
		this.groupNumber = groupNumber;
		this.attendsSet = attendsSet;
		this.shift = shift;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof IStudentGroup) {
			result = (getAttendsSet().equals(((IStudentGroup) arg0).getAttendsSet()))&&
					 (getGroupNumber().equals(((IStudentGroup) arg0).getGroupNumber()));
			
		} 
		return result;		
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[STUDENT_GROUP";
		result += ", groupNumber=" + getGroupNumber();
		result += ", attendsSet=" + getAttendsSet();
		result += ", shift =" + getShift();
		result += "]";
		return result;
	}

    /**
     * @return Integer
     */
    public Integer getKeyShift() {
        return keyShift;
    }
    
	/**
	 * @return Integer
	 */
	public Integer getkeyAttendsSet() {
		return keyAttendsSet;
	}

    /**
     * @return Integer
     */
    public Integer getGroupNumber() {
        return groupNumber;
    }

	/**
	 * @return GroupProperties
	 */
	public IAttendsSet getAttendsSet() {
		return attendsSet;
	}

	/**
	 * @return Shift
	 */
	public IShift getShift() {
		return shift;
	}

	
	/**
	* Sets the keyShift.
	* @param keyShift The keyShift to set
	*/
	public void setKeyShift(Integer keyShift) {
		this.keyShift = keyShift;
	}
	
	/**
	* Sets the groupProperties.
	* @param groupProperties The groupProperties to set
	*/
	
	public void setKeyAttendsSet(Integer keyAttendsSet) {
		this.keyAttendsSet = keyAttendsSet;
	}
	
		
	/**
	 * Sets the groupNumber.
	 * @param groupNumber The groupNumber to set
	 */
	public void setGroupNumber(Integer groupNumber) {
		this.groupNumber = groupNumber;
	}

	/**
	 * Sets the groupProperties.
	 * @param groupProperties The groupProperties to set
	 */
	public void setAttendsSet(IAttendsSet attendsSet) {
		this.attendsSet = attendsSet;
	}

	/**
	* Sets the shift.
	* @param shift The shift to set
	*/
	public void setShift(IShift shift) {
		this.shift = shift;
	}
}
	