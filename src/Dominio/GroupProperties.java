/*
 * Created on 6/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;
import Util.EnrolmentGroupPolicyType;

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
	private EnrolmentGroupPolicyType enrolmentPolicy;
	private Integer groupMaximumNumber;
	private String name;
	private Integer keyExecutionCourse;
	private IDisciplinaExecucao executionCourse;
	
	/** 
	 * Construtor
	 */
	public GroupProperties() {}
	
	/** 
 	* Construtor
 	*/
	public GroupProperties(Integer idInternal){
			setIdInternal(idInternal);
		}
	
	/** 
	 * Construtor
	 */
	public GroupProperties(IDisciplinaExecucao executionCourse,String name) {
			this.executionCourse=executionCourse;
			this.name = name;
	}
	
	/** 
	 * Construtor
	 */
	public GroupProperties(Integer maximumCapacity,Integer minimumCapacity,
							Integer idealCapacity,EnrolmentGroupPolicyType enrolmentPolicy,
	 						Integer groupMaximumNumber,String name,IDisciplinaExecucao executionCourse) {
		this.maximumCapacity=maximumCapacity;
		this.minimumCapacity=minimumCapacity;
		this.idealCapacity=idealCapacity;
		this.enrolmentPolicy= enrolmentPolicy;
		this.groupMaximumNumber=groupMaximumNumber;
		this.name = name;
		this.executionCourse=executionCourse;
		}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof IGroupProperties) {
			result =(getExecutionCourse().equals(((IGroupProperties) arg0).getExecutionCourse()))&&
					(getName().equals(((IGroupProperties) arg0).getName()));
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
		result += ", name=" + getName();
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
	public EnrolmentGroupPolicyType getEnrolmentPolicy() {
		return enrolmentPolicy;
	}
			
	/**
	 * @return Integer
	 */
	public Integer getGroupMaximumNumber() {
		return groupMaximumNumber;
	}

	/**
	* @return String
	*/
	public String getName() {
		return name;
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
	public void setEnrolmentPolicy(EnrolmentGroupPolicyType enrolmentPolicy) {
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
	* Sets the projectName.
	* @param projectName The projectName to set
	*/
		public void setName(String name) {
			this.name=name;
		}
	/**
	* Sets the executionCourse.
	* @param executionCourse The executionCourse to set
	*/
	public void setExecutionCourse(IDisciplinaExecucao executionCourse) {
		this.executionCourse=executionCourse;
	}	

	}
