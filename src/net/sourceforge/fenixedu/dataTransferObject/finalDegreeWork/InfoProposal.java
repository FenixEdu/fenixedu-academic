/*
 * Created on Mar 7, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

/**
 * @author Luis Cruz
 *  
 */
public class InfoProposal extends InfoObject {

    private DomainReference<Proposal> proposalDomainReference;
    
    public InfoProposal(final Proposal proposal) {
	proposalDomainReference = new DomainReference<Proposal>(proposal);
    }
    
    public static InfoProposal newInfoFromDomain(final Proposal proposal) {
	return proposal == null ? null : new InfoProposal(proposal);
    }

    private Proposal getProposal() {
	return proposalDomainReference == null ? null : proposalDomainReference.getObject();
    }

    @Override
    public Integer getIdInternal() {
	return getProposal().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

    public boolean equals(Object obj) {
	return obj instanceof InfoProposal
		&& getProposal() == ((InfoProposal) obj).getProposal();
    }

    public int hashCode() {
	return getProposal().hashCode();
    }

    public String toString() {
	return getProposal().toString();
    }

    /**
     * @return Returns the degreeType.
     */
    public DegreeType getDegreeType() {
        return getProposal().getDegreeType();
    }

    /**
     * @return Returns the maximumNumberOfGroupElements.
     */
    public Integer getMaximumNumberOfGroupElements() {
	return getProposal().getMaximumNumberOfGroupElements();
    }

    /**
     * @return Returns the minimumNumberOfGroupElements.
     */
    public Integer getMinimumNumberOfGroupElements() {
        return getProposal().getMinimumNumberOfGroupElements();
    }

    /**
     * @return Returns the companyName.
     */
    public String getCompanyName() {
        return getProposal().getCompanyName();
    }

    /**
     * @return Returns the coorientatorsCreditsPercentage.
     */
    public Integer getCoorientatorsCreditsPercentage() {
        return getProposal().getCoorientatorsCreditsPercentage();
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return getProposal().getDescription();
    }

    /**
     * @return Returns the objectives.
     */
    public String getObjectives() {
        return getProposal().getObjectives();
    }

    /**
     * @return Returns the observations.
     */
    public String getObservations() {
        return getProposal().getObservations();
    }

    /**
     * @return Returns the orientatorsCreditsPercentage.
     */
    public Integer getOrientatorsCreditsPercentage() {
        return getProposal().getOrientatorsCreditsPercentage();
    }

    /**
     * @return Returns the requirements.
     */
    public String getRequirements() {
        return getProposal().getRequirements();
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return getProposal().getTitle();
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return getProposal().getUrl();
    }

    /**
     * @return Returns the framing.
     */
    public String getFraming() {
        return getProposal().getFraming();
    }

    /**
     * @return Returns the deliverable.
     */
    public String getDeliverable() {
        return getProposal().getDeliverable();
    }

    /**
     * @return Returns the location.
     */
    public String getLocation() {
        return getProposal().getLocation();
    }

    /**
     * @return Returns the companionMail.
     */
    public String getCompanionMail() {
        return getProposal().getCompanionMail();
    }

    /**
     * @return Returns the companionName.
     */
    public String getCompanionName() {
        return getProposal().getCompanionName();
    }

    /**
     * @return Returns the companionPhone.
     */
    public Integer getCompanionPhone() {
        return getProposal().getCompanionPhone();
    }

    /**
     * @return Returns the companyAdress.
     */
    public String getCompanyAdress() {
        return getProposal().getCompanyAdress();
    }

    /**
     * @return Returns the proposalNumber.
     */
    public Integer getProposalNumber() {
        return getProposal().getProposalNumber();
    }

    /**
     * @return Returns the status.
     */
    public FinalDegreeWorkProposalStatus getStatus() {
        return getProposal().getStatus();
    }

    /**
     * @return Returns the groupAttributedByTeacher.
     */
    public InfoGroup getGroupAttributedByTeacher() {
        return InfoGroup.newInfoFromDomain(getProposal().getGroupAttributedByTeacher());
    }

    /**
     * @return Returns the groupAttributed.
     */
    public InfoGroup getGroupAttributed() {
        return InfoGroup.newInfoFromDomain(getProposal().getGroupAttributed());
    }

    /**
     * @return Returns the orientator.
     */
    public InfoPerson getOrientator() {
        return InfoPerson.newInfoFromDomain(getProposal().getOrientator());
    }

    /**
     * @return Returns the coorientator.
     */
    public InfoPerson getCoorientator() {
        return InfoPerson.newInfoFromDomain(getProposal().getCoorientator());
    }

    /**
     * @return Returns the executionDegree.
     */
    public InfoExecutionDegree getExecutionDegree() {
        return InfoExecutionDegree.newInfoFromDomain(getProposal().getScheduleing().getExecutionDegrees().get(0));
    }

    /**
     * @return Returns the orientatorsDepartment.
     */
    public InfoDepartment getOrientatorsDepartment() {
	return getProposal().getOrientator().getTeacher() == null ? null :
	    	InfoDepartment.newInfoFromDomain(getProposal().getOrientator().getTeacher().getCurrentWorkingDepartment());
    }

    /**
     * @return Returns the coorientatorsDepartment.
     */
    public InfoDepartment getCoorientatorsDepartment() {
	return getProposal().getCoorientator().getTeacher() == null ? null :
	    	InfoDepartment.newInfoFromDomain(getProposal().getCoorientator().getTeacher().getCurrentWorkingDepartment());
    }

    /**
     * @return Returns the branches.
     */
    public List<InfoBranch> getBranches() {
	List<InfoBranch> result = new ArrayList<InfoBranch>();
	
	for (final Branch branch : getProposal().getBranches()) {
	    result.add(InfoBranch.newInfoFromDomain(branch));
	}

	return result;
    }

}
