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
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SubmitFinalWorkProposal {

    protected void run(InfoProposalEditor infoProposal) throws FenixServiceException {
        Integer executionDegreeId = infoProposal.getExecutionDegree().getExternalId();
        ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);

        Scheduleing scheduleing = executionDegree.getScheduling();
        if (scheduleing == null) {
            throw new OutOfPeriodException(null, null, null);
        }

        Proposal proposal = null;
        if (infoProposal.getExternalId() != null) {
            proposal = RootDomainObject.getInstance().readProposalByOID(infoProposal.getExternalId());
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
                    Branch branch = RootDomainObject.getInstance().readBranchByOID(infoBranch.getExternalId());
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

    @Service
    public static void runSubmitFinalWorkProposal(InfoProposalEditor infoProposal) throws FenixServiceException,
            NotAuthorizedException {
        SubmitFinalWorkProposalAuthorization.instance.execute(infoProposal);
        serviceInstance.run(infoProposal);
    }

}