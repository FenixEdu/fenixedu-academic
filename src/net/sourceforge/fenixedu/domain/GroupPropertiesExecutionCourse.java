/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.ProposalState;

/**
 * @author joaosa & rmalo
 */
 
public class GroupPropertiesExecutionCourse extends GroupPropertiesExecutionCourse_Base {
		
	
	private ProposalState proposalState;

	
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
			super.setGroupProperties(groupProperties);
			super.setExecutionCourse(executionCourse);
	}
	
	/** 
	 * Construtor
	 */
	public GroupPropertiesExecutionCourse(IGroupProperties groupProperties,IExecutionCourse executionCourse,ProposalState proposalState) {
	    	super.setGroupProperties(groupProperties);
	    	super.setExecutionCourse(executionCourse);
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
	* @return ProposalState
	**/
	public ProposalState getProposalState() {
		return proposalState;
	}

	/**
	* Sets the proposalState.
	* @param proposalState The proposalState to set
	*/
	public void setProposalState(ProposalState proposalState) {
		this.proposalState=proposalState;
	}

}
