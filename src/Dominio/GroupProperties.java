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
public class GroupProperties extends DomainObject implements IGroupProperties{
	
	private Integer maximumCapacity;
	private Integer minimumCapacity;
	private Integer idealCapacity;
	private String enrolmentPolicy;
	private Integer groupMaximumNumber;
	private Integer keyExecutionCourse;
	private IDisciplinaExecucao executionCourse;
	
	/** 
	 * Construtor
	 */
	public GroupProperties() {}
	
	/** 
	 * Construtor
	 */
	public GroupProperties(IDisciplinaExecucao executionCourse) {
			this.executionCourse=executionCourse;
	}
	
	/** 
	 * Construtor
	 */
	public GroupProperties(Integer maximumCapacity,Integer minimumCapacity,
							Integer idealCapacity,String enrolmentPolicy,
	 						Integer groupMaximumNumber,IDisciplinaExecucao executionCourse) {
		this.maximumCapacity=maximumCapacity;
		this.minimumCapacity=minimumCapacity;
		this.idealCapacity=idealCapacity;
		this.enrolmentPolicy=enrolmentPolicy;
		this.groupMaximumNumber=groupMaximumNumber;
		this.executionCourse=executionCourse;
		}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof IGroupProperties) {
			result = (getExecutionCourse().equals(((IGroupProperties) arg0).getExecutionCourse()));
		} 
		return result;		
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[GROUP";
		result += ", maximumCapacity=" + getMaximumCapacity();
		result += ", minimumCapacity=" + getMinimumCapacity();
		result += ", idealCapacity=" + getIdealCapacity();
		result += ", enrolmentPolicy=" + getEnrolmentPolicy();
		result += ", groupMaximumNumber=" + getGroupMaximumNumber();
		result += ", executionCourse=" + getExecutionCourse();
		result += "]";
		return result;
	}
		
	
	/**
	 * @return Integer
	 */
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}
	
	/**
	 * @return Integer
	 */
	public Integer getMaximumCapacity() {
		return maximumCapacity;
	}

	/**
	 * @return Integer
	 */
	public Integer getMinimumCapacity() {
		return minimumCapacity;
	}
	
	/**
	 * @return Integer
	 */
	public Integer getIdealCapacity() {
		return idealCapacity;
	}
	
	/**
	* @return EnrolmentPolicy
	*/
	public String getEnrolmentPolicy() {
		return enrolmentPolicy;
	}
			
	/**
	 * @return Integer
	 */
	public Integer getGroupMaximumNumber() {
		return groupMaximumNumber;
	}

	

	/**
	 * @return Turno
	 */
	public IDisciplinaExecucao getExecutionCourse() {
		return executionCourse;
	}
	
	/**
	* Sets the keyExecutionCourse.
	* @param keyExecutionCourse The keyExecutionCourse to set
	*/
	public void setKeyExecutionCourse(Integer keyExecutionCourse) {
		this.keyExecutionCourse=keyExecutionCourse;
	}
	
	/**
	* Sets the maximumCapacity.
	* @param maximumCapacity The maximumCapacity to set
	*/
	public void setMaximumCapacity(Integer maximumCapacity) {
		this.maximumCapacity=maximumCapacity;
	}

	/**
	* Sets the minimumCapacity.
	* @param minimumCapacity The minimumCapacity to set
	*/
	public void setMinimumCapacity(Integer minimumCapacity) {
		this.minimumCapacity=minimumCapacity;
	}
	
	/**
	* Sets the idealCapacity.
	* @param idealCapacity The idealCapacity to set
	*/
	public void setIdealCapacity(Integer idealCapacity) {
		this.idealCapacity=idealCapacity;
	}
	
	/**
	* Sets the enrolmentPolicy.
	* @param enrolmentPolicy The enrolmentPolicy to set
	*/
	public void setEnrolmentPolicy(String enrolmentPolicy) {
		this.enrolmentPolicy=enrolmentPolicy;
	}		
	/**
	* Sets the groupMaximumNumber.
	* @param groupMaximumNumber The groupMaximumNumber to set
	*/
	public void setGroupMaximumNumber(Integer groupMaximumNumber) {
		this.groupMaximumNumber=groupMaximumNumber;
	}	

	/**
	* Sets the executionCourse.
	* @param executionCourse The executionCourse to set
	*/
	public void setExecutionCourse(IDisciplinaExecucao executionCourse) {
		this.executionCourse=executionCourse;
	}	

	}
