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
 * Created on Mar 11, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import net.sourceforge.fenixedu.applicationTier.Filtro.SubmitFinalWorkProposalAuthorization;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfPeriodException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposalEditor;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SubmitFinalWorkProposal {

    protected void run(InfoProposalEditor infoProposal) throws FenixServiceException {
        String executionDegreeId = infoProposal.getExecutionDegree().getExternalId();
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);

        Scheduleing scheduleing = executionDegree.getScheduling();
        if (scheduleing == null) {
            throw new OutOfPeriodException(null, null, null);
        }

        Proposal proposal = null;
        if (infoProposal.getExternalId() != null) {
            proposal = FenixFramework.getDomainObject(infoProposal.getExternalId());
        }
        if (proposal == null) {
            proposal = new Proposal();
            int proposalNumber = scheduleing.getCurrentProposalNumber().intValue();
            proposal.setProposalNumber(proposalNumber);
            scheduleing.setCurrentProposalNumber(proposalNumber + 1);
        }

        proposal.setScheduleing(scheduleing);

        infoProposal.setProposalOID(proposal.getExternalId());
        proposal.setCompanionName(infoProposal.getCompanionName());
        proposal.setCompanionMail(infoProposal.getCompanionMail());
        proposal.setCompanionPhone(infoProposal.getCompanionPhone());
        proposal.setCompanyAdress(infoProposal.getCompanyAdress());
        proposal.setCompanyName(infoProposal.getCompanyName());

        if (infoProposal.getCoorientator() != null) {
            Person coorientator = infoProposal.getCoorientator().getPerson();
            proposal.setCoorientator(coorientator);
        } else {
            proposal.setCoorientator(null);
        }

        proposal.setCoorientatorsCreditsPercentage(infoProposal.getCoorientatorsCreditsPercentage());
        proposal.setDegreeType(infoProposal.getDegreeType());
        proposal.setDeliverable(infoProposal.getDeliverable());
        proposal.setDescription(infoProposal.getDescription());

        proposal.setFraming(infoProposal.getFraming());
        proposal.setLocation(infoProposal.getLocation());

        proposal.setMaximumNumberOfGroupElements(infoProposal.getMaximumNumberOfGroupElements());
        proposal.setMinimumNumberOfGroupElements(infoProposal.getMinimumNumberOfGroupElements());
        proposal.setObjectives(infoProposal.getObjectives());
        proposal.setObservations(infoProposal.getObservations());

        Person orientator = infoProposal.getOrientator().getPerson();

        proposal.setOrientator(orientator);
        proposal.setOrientatorsCreditsPercentage(infoProposal.getOrientatorsCreditsPercentage());
        proposal.setRequirements(infoProposal.getRequirements());
        proposal.setTitle(infoProposal.getTitle());
        proposal.setUrl(infoProposal.getUrl());

        proposal.getBranches().clear();
        if (infoProposal.getBranches() != null && !infoProposal.getBranches().isEmpty()) {
            for (int i = 0; i < infoProposal.getBranches().size(); i++) {
                InfoBranch infoBranch = (InfoBranch) infoProposal.getBranches().get(i);
                if (infoBranch != null && infoBranch.getExternalId() != null) {
                    Branch branch = FenixFramework.getDomainObject(infoBranch.getExternalId());
                    if (branch != null) {
                        proposal.getBranches().add(branch);
                    }
                }
            }
        }

        proposal.setStatus(infoProposal.getStatus());
    }

    // Service Invokers migrated from Berserk

    private static final SubmitFinalWorkProposal serviceInstance = new SubmitFinalWorkProposal();

    @Atomic
    public static void runSubmitFinalWorkProposal(InfoProposalEditor infoProposal) throws FenixServiceException,
            NotAuthorizedException {
        SubmitFinalWorkProposalAuthorization.instance.execute(infoProposal);
        serviceInstance.run(infoProposal);
    }

}