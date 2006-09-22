/*
 * Created on 2004/04/15
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;

/**
 * @author Luis Cruz
 *  
 */
public class InfoGroupProposal extends InfoObject {

    private DomainReference<GroupProposal> groupProposalDomainReference;
    
    public InfoGroupProposal(final GroupProposal groupProposal) {
	groupProposalDomainReference = new DomainReference<GroupProposal>(groupProposal);
    }
    
    public static InfoGroupProposal newInfoFromDomain(final GroupProposal groupProposal) {
	return groupProposal == null ? null : new InfoGroupProposal(groupProposal);
    }

    private GroupProposal getGroupProposal() {
	return groupProposalDomainReference == null ? null : groupProposalDomainReference.getObject();
    }

    @Override
    public Integer getIdInternal() {
	return getGroupProposal().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

    public boolean equals(Object obj) {
	return obj instanceof InfoGroupProposal
		&& getGroupProposal() == ((InfoGroupProposal) obj).getGroupProposal();
    }

    public int hashCode() {
	return getGroupProposal().hashCode();
    }

    public String toString() {
	return getGroupProposal().toString();
    }
    
    /**
     * @return Returns the finalDegreeWorkProposal.
     */
    public InfoProposal getFinalDegreeWorkProposal() {
	return InfoProposal.newInfoFromDomain(getGroupProposal().getFinalDegreeWorkProposal());
    }

    /**
     * @return Returns the orderOfPreference.
     */
    public Integer getOrderOfPreference() {
	return getGroupProposal().getOrderOfPreference();
    }

    /**
     * @return Returns the infoGroup.
     */
    public InfoGroup getInfoGroup() {
	return InfoGroup.newInfoFromDomain(getGroupProposal().getFinalDegreeDegreeWorkGroup());
    }

}
