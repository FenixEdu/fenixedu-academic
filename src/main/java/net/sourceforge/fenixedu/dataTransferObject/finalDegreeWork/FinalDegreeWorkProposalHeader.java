/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 2004/03/09
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

import org.apache.commons.lang.StringUtils;

/**
 * @author Luis Cruz
 * 
 */
@SuppressWarnings("serial")
public class FinalDegreeWorkProposalHeader extends InfoObject {

    private final Proposal proposalDomainReference;

    private final ExecutionDegree executionDegreeDomainReference;

    public FinalDegreeWorkProposalHeader(final Proposal proposal) {
        proposalDomainReference = proposal;
        executionDegreeDomainReference = proposal.getScheduleing().getExecutionDegrees().iterator().next();
    }

    public FinalDegreeWorkProposalHeader(final Proposal proposal, final ExecutionDegree executionDegree) {
        proposalDomainReference = proposal;
        executionDegreeDomainReference = executionDegree;
    }

    public static FinalDegreeWorkProposalHeader newInfoFromDomain(final Proposal proposal) {
        return proposal == null ? null : new FinalDegreeWorkProposalHeader(proposal);
    }

    public static FinalDegreeWorkProposalHeader newInfoFromDomain(final Proposal proposal, final ExecutionDegree executionDegree) {
        return proposal == null ? null : new FinalDegreeWorkProposalHeader(proposal, executionDegree);
    }

    private Proposal getProposal() {
        return proposalDomainReference;
    }

    private ExecutionDegree getExecutionDegree() {
        return executionDegreeDomainReference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FinalDegreeWorkProposalHeader
                && getProposal() == ((FinalDegreeWorkProposalHeader) obj).getProposal()
                && getExecutionDegree() == ((FinalDegreeWorkProposalHeader) obj).getExecutionDegree();
    }

    @Override
    public int hashCode() {
        return getProposal().hashCode();
    }

    @Override
    public String getExternalId() {
        return getProposal().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    public String getProposalOID() {
        return getProposal().getExternalId();
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
        return getProposal().hasCoorientator() ? getProposal().getCoorientator().getName() : StringUtils.EMPTY;
    }

    /**
     * @return Returns the coorientatorOID.
     */
    public String getCoorientatorOID() {
        return getProposal().getCoorientator().getExternalId();
    }

    /**
     * @return Returns the orientatorName.
     */
    public String getOrientatorName() {
        return getProposal().getOrientator().getName();
    }

    /**
     * @return Returns the orientatorOID.
     */
    public String getOrientatorOID() {
        return getProposal().getOrientator().getExternalId();
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

    public String getExecutionDegreeOID() {
        return getExecutionDegree().getExternalId();
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
