/*
 * Created on 17/Ago/2004
 */
package Dominio;

import Util.ProposalState;

/**
 * @author joaosa & rmalo
 */
 
public class GroupPropertiesExecutionCourse extends DomainObject implements IGroupPropertiesExecutionCourse {
		
	
	private Integer keyGroupProperties;
	private Integer keyExecutionCourse;
	private IGroupProperties groupProperties;
	private IExecutionCourse executionCourse; 
	private ProposalState proposalState;
	private Integer keySenderPerson;
	private Integer keyReceiverPerson;
	private IPessoa senderPerson;
	private IPessoa receiverPerson; 
	private Integer keySenderExecutionCourse;
	private IExecutionCourse senderExecutionCourse;

	
	/** 
	 * Construtor
	 */
	public GroupPropertiesExecutionCourse() {}
	
	/** 
 	* Construtor
 	*/
	public GroupPropertiesExecutionCourse(Integer idInternal){
			setIdInternal(idInternal);
		}
	
	/** 
	 * Construtor
	 */
	public GroupPropertiesExecutionCourse(IGroupProperties groupProperties,IExecutionCourse executionCourse) {
			this.groupProperties = groupProperties;
			this.executionCourse = executionCourse;
	}
	
	/** 
	 * Construtor
	 */
	public GroupPropertiesExecutionCourse(IGroupProperties groupProperties,IExecutionCourse executionCourse,ProposalState proposalState) {
			this.groupProperties = groupProperties;
			this.executionCourse = executionCourse;
			this.proposalState = proposalState;
	}
	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof IGroupPropertiesExecutionCourse) {
			result =(getGroupProperties().equals(((IGroupPropertiesExecutionCourse) arg0).getGroupProperties()))&&
					(getExecutionCourse().equals(((IGroupPropertiesExecutionCourse) arg0).getExecutionCourse()));
		} 
		return result;		
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[GROUPPROPERTIESEXECUTIONCOURSE";
		result += ", groupProperties=" + getGroupProperties();
		result += ", executionCourse=" + getExecutionCourse();
		result += ", proposalState=" + getProposalState();
		result += "]";
		return result;
	}
		
	
	/**
	 * @return Integer
	 */
	public Integer getKeyGroupProperties() {
		return keyGroupProperties;
	}
	
	/**
	 * @return GroupProperties
	 */
	public IGroupProperties getGroupProperties() {
		return groupProperties;
	}
	
	/**
	 * @return Integer
	 */
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}
	
	/**
	 * @return ExecutionCourse
	 */
	public IExecutionCourse getExecutionCourse() {
		return executionCourse;
	}
	
	
	
	/**
	* @return ProposalState
	**/
	public ProposalState getProposalState() {
		return proposalState;
	}


	
	/**
	 * @return Integer
	 */
	public Integer getKeySenderPerson() {
		return keySenderPerson;
	}
	
	/**
	 * @return IPessoa
	 */
	public IPessoa getSenderPerson() {
		return senderPerson;
	}
	
	/**
	 * @return Integer
	 */
	public Integer getKeySenderExecutionCourse() {
		return keySenderExecutionCourse;
	}
	
	/**
	 * @return IExecutionCourse
	 */
	public IExecutionCourse getSenderExecutionCourse() {
		return senderExecutionCourse;
	}
	
	/**
	 * @return Integer
	 */
	public Integer getKeyReceiverPerson() {
		return keyReceiverPerson;
	}
	
	/**
	 * @return IPessoa
	 */
	public IPessoa getReceiverPerson() {
		return receiverPerson;
	}
	

	
	/**
	* Sets the keyGroupProperties.
	* @param keyGroupProperties
	*/
	public void setKeyGroupProperties(Integer keyGroupProperties) {
		this.keyGroupProperties=keyGroupProperties;
	}
		
	/**
	* Sets the groupProperties.
	* @param groupProperties The groupProperties to set
	*/
	public void setGroupProperties(IGroupProperties groupProperties) {
		this.groupProperties=groupProperties;
	}		
	
	/**
	* Sets the keyExecutionCourse.
	* @param keyExecutionCourse
	*/
	public void setKeyExecutionCourse(Integer keyExecutionCourse) {
		this.keyExecutionCourse=keyExecutionCourse;
	}
	
	/**
	* Sets the executionCourse.
	* @param executionCourse The executionCourse to set
	*/
	public void setExecutionCourse(IExecutionCourse executionCourse) {
		this.executionCourse=executionCourse;
	}	

	/**
	* Sets the proposalState.
	* @param proposalState The proposalState to set
	*/
	public void setProposalState(ProposalState proposalState) {
		this.proposalState=proposalState;
	}


	/**
	* Sets the keySenderPerson.
	* @param keySenderPerson
	*/
	public void setKeySenderPerson(Integer keySenderPerson) {
		this.keySenderPerson=keySenderPerson;
	}
		
	/**
	* Sets the senderPerson.
	* @param senderPerson The senderPerson to set
	*/
	public void setSenderPerson(IPessoa senderPerson) {
		this.senderPerson=senderPerson;
	}		
	
	
	/**
	* Sets the keyReceiverPerson.
	* @param keyReceiverPerson
	*/
	public void setKeyReceiverPerson(Integer keyReceiverPerson) {
		this.keyReceiverPerson=keyReceiverPerson;
	}
	
	/**
	* Sets the receiverPerson.
	* @param receiverPerson The receiverPerson to set
	*/
	public void setReceiverPerson(IPessoa receiverPerson) {
		this.receiverPerson=receiverPerson;
	}	

	/**
	* Sets the keySenderExecutionCourse.
	* @param keySenderExecutionCourse
	*/
	public void setKeySenderExecutionCourse(Integer keySenderExecutionCourse) {
		this.keySenderExecutionCourse=keySenderExecutionCourse;
	}
	
	/**
	* Sets the senderExecutionCourse.
	* @param senderExecutionCourse The senderExecutionCourse to set
	*/
	public void setSenderExecutionCourse(IExecutionCourse senderExecutionCourse) {
		this.senderExecutionCourse=senderExecutionCourse;
	}	
	
}
