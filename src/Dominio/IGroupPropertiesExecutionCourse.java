/*
 * Created on 17/Ago/2004
 */
package Dominio;

import Util.ProposalState;

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
	public IPessoa getSenderPerson();
	public Integer getKeyReceiverPerson();
	public IPessoa getReceiverPerson();
	public Integer getKeySenderExecutionCourse(); 
	public IExecutionCourse getSenderExecutionCourse(); 
	
	public void setKeyGroupProperties(Integer keyGroupProperties);
	public void setGroupProperties(IGroupProperties groupProperties);
	public void setKeyExecutionCourse(Integer keyExecutionCourse);
	public void setExecutionCourse(IExecutionCourse executionCourse);
	public void setProposalState(ProposalState proposalState);
	public void setKeySenderPerson(Integer keySenderPerson);
	public void setSenderPerson(IPessoa senderPerson);
	public void setKeyReceiverPerson(Integer keyReceiverPerson);
	public void setReceiverPerson(IPessoa receiverPerson);
	public void setKeySenderExecutionCourse(Integer keySenderExecutionCourse); 
	public void setSenderExecutionCourse(IExecutionCourse senderExecutionCourse); 
}
