/*
 * Created on 2004/03/09
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

/**
 * @author Luis Cruz
 *  
 */
public class FinalDegreeWorkProposalHeader extends InfoObject {

    private DomainReference<Proposal> proposalDomainReference;

    private DomainReference<ExecutionDegree> executionDegreeDomainReference;

    public FinalDegreeWorkProposalHeader(final Proposal proposal) {
	proposalDomainReference = new DomainReference<Proposal>(proposal);
	executionDegreeDomainReference = new DomainReference<ExecutionDegree>(proposal.getScheduleing()
		.getExecutionDegrees().get(0));
    }

    public FinalDegreeWorkProposalHeader(final Proposal proposal, final ExecutionDegree executionDegree) {
	proposalDomainReference = new DomainReference<Proposal>(proposal);
	executionDegreeDomainReference = new DomainReference<ExecutionDegree>(executionDegree);
    }

    public static FinalDegreeWorkProposalHeader newInfoFromDomain(final Proposal proposal) {
	return proposal == null ? null : new FinalDegreeWorkProposalHeader(proposal);
    }

    public static FinalDegreeWorkProposalHeader newInfoFromDomain(final Proposal proposal,
	    final ExecutionDegree executionDegree) {
	return proposal == null ? null : new FinalDegreeWorkProposalHeader(proposal, executionDegree);
    }

    private Proposal getProposal() {
	return proposalDomainReference == null ? null : proposalDomainReference.getObject();
    }

    private ExecutionDegree getExecutionDegree() {
	return executionDegreeDomainReference == null ? null : executionDegreeDomainReference
		.getObject();
    }

    public boolean equals(Object obj) {
	return obj instanceof FinalDegreeWorkProposalHeader
		&& getProposal() == ((FinalDegreeWorkProposalHeader) obj).getProposal()
		&& getExecutionDegree() == ((FinalDegreeWorkProposalHeader) obj).getExecutionDegree();
    }

    public int hashCode() {
	return getProposal().hashCode();
    }

    @Override
    public Integer getIdInternal() {
	return getProposal().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

    /**
     * @return Returns the status.
     */
    public FinalDegreeWorkProposalStatus getStatus() {
	return getProposal().getStatus();
    }

    /**
     * @return Returns the companyLink.
     */
    public String getCompanyLink() {
	return getProposal().getCompanionName();
    }

    /**
     * @return Returns the coorientatorName.
     */
    public String getCoorientatorName() {
	return getProposal().hasCoorientator() ? getProposal().getCoorientator().getPerson().getNome() : StringUtils.EMPTY;
    }

    /**
     * @return Returns the coorientatorOID.
     */
    public Integer getCoorientatorOID() {
	return getProposal().getCoorientator().getIdInternal();
    }

    /**
     * @return Returns the orientatorName.
     */
    public String getOrientatorName() {
	return getProposal().getOrientator().getPerson().getNome();
    }

    /**
     * @return Returns the orientatorOID.
     */
    public Integer getOrientatorOID() {
	return getProposal().getOrientator().getIdInternal();
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return getProposal().getTitle();
    }

    /**
     * @return Returns the proposalNumber.
     */
    public Integer getProposalNumber() {
	return getProposal().getProposalNumber();
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
     * @return Returns the branches.
     */
    public List<InfoBranch> getBranches() {
	List<InfoBranch> result = new ArrayList<InfoBranch>();

	for (final Branch branch : getProposal().getBranches()) {
	    result.add(InfoBranch.newInfoFromDomain(branch));
	}

	return result;
    }

    /**
     * @return Returns the groupProposals.
     */
    public List<InfoGroupProposal> getGroupProposals() {
	List<InfoGroupProposal> result = new ArrayList<InfoGroupProposal>();

	for (final GroupProposal groupProposal : getProposal().getGroupProposals()) {
	    result.add(InfoGroupProposal.newInfoFromDomain(groupProposal));
	}

	return result;
    }
    public Integer getExecutionDegreeOID() {
	return getExecutionDegree().getIdInternal();
    }

    public String getExecutionYear() {
	return getExecutionDegree().getExecutionYear().getYear();
    }

    public String getDegreeCode() {
	return getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla();
    }

    public Boolean getEditable() {
	final Scheduleing scheduleing = getProposal().getScheduleing();

	return scheduleing != null && scheduleing.getStartOfProposalPeriod() != null
		&& scheduleing.getEndOfProposalPeriod() != null
		&& scheduleing.getStartOfProposalPeriod().before(Calendar.getInstance().getTime())
		&& scheduleing.getEndOfProposalPeriod().after(Calendar.getInstance().getTime());
    }

}
