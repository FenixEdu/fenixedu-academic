/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.ProposalState;

/**
 * @author joaosa & rmalo
 */

public interface IGroupPropertiesExecutionCourse extends IDomainObject{

	public Integer getKeyGroupProperties();
	public IGroupProperties getGroupProperties();
	public Integer getKeyExecutionCourse();
	public IExecutionCourse getExecutionCourse();
	public ProposalState getProposalState();
	public Integer getKeySenderPerson();
	public IPerson getSenderPerson();
	public Integer getKeyReceiverPerson();
	public IPerson getReceiverPerson();
	public Integer getKeySenderExecutionCourse(); 
	public IExecutionCourse getSenderExecutionCourse(); 
	
	public void setKeyGroupProperties(Integer keyGroupProperties);
	public void setGroupProperties(IGroupProperties groupProperties);
	public void setKeyExecutionCourse(Integer keyExecutionCourse);
	public void setExecutionCourse(IExecutionCourse executionCourse);
	public void setProposalState(ProposalState proposalState);
	public void setKeySenderPerson(Integer keySenderPerson);
	public void setSenderPerson(IPerson senderPerson);
	public void setKeyReceiverPerson(Integer keyReceiverPerson);
	public void setReceiverPerson(IPerson receiverPerson);
	public void setKeySenderExecutionCourse(Integer keySenderExecutionCourse); 
	public void setSenderExecutionCourse(IExecutionCourse senderExecutionCourse); 
}
