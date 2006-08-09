/*
 * Created on Mar 11, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfPeriodException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SubmitFinalWorkProposal extends Service {

    public void run(InfoProposal infoProposal) throws FenixServiceException, ExcepcaoPersistencia {
        Integer executionDegreeId = infoProposal.getExecutionDegree().getIdInternal();
        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);

        Scheduleing scheduleing = executionDegree.getScheduling();
        if (scheduleing == null) {
            throw new OutOfPeriodException(null, null, null);
        }

        Proposal proposal = null;
        if (infoProposal.getIdInternal() != null) {
            proposal = rootDomainObject.readProposalByOID(infoProposal.getIdInternal());
        }
        if (proposal == null) {
            proposal = new Proposal();
            int proposalNumber = scheduleing.getCurrentProposalNumber().intValue();
            proposal.setProposalNumber(proposalNumber);
            scheduleing.setCurrentProposalNumber(proposalNumber + 1);
        }

        proposal.setCompanionName(infoProposal.getCompanionName());
        proposal.setCompanionMail(infoProposal.getCompanionMail());
        proposal.setCompanionPhone(infoProposal.getCompanionPhone());
        proposal.setCompanyAdress(infoProposal.getCompanyAdress());
        proposal.setCompanyName(infoProposal.getCompanyName());

        if (infoProposal.getCoorientator() != null) {
            Integer coorientatorId = infoProposal.getCoorientator().getIdInternal();
            Teacher coorientator = rootDomainObject.readTeacherByOID(coorientatorId);
            proposal.setCoorientator(coorientator);
        } else {
            proposal.setCoorientator(null);
        }

        proposal.setCoorientatorsCreditsPercentage(infoProposal.getCoorientatorsCreditsPercentage());
        proposal.setDegreeType(infoProposal.getDegreeType());
        proposal.setDeliverable(infoProposal.getDeliverable());
        proposal.setDescription(infoProposal.getDescription());

        proposal.setScheduleing(executionDegree.getScheduling());
        proposal.setFraming(infoProposal.getFraming());
        proposal.setLocation(infoProposal.getLocation());

        proposal.setMaximumNumberOfGroupElements(infoProposal.getMaximumNumberOfGroupElements());
        proposal.setMinimumNumberOfGroupElements(infoProposal.getMinimumNumberOfGroupElements());
        proposal.setObjectives(infoProposal.getObjectives());
        proposal.setObservations(infoProposal.getObservations());

        Integer orientatorId = infoProposal.getOrientator().getIdInternal();
        Teacher orientator = rootDomainObject.readTeacherByOID(orientatorId);

        proposal.setOrientator(orientator);
        proposal.setOrientatorsCreditsPercentage(infoProposal.getOrientatorsCreditsPercentage());
        proposal.setRequirements(infoProposal.getRequirements());
        proposal.setTitle(infoProposal.getTitle());
        proposal.setUrl(infoProposal.getUrl());

        proposal.getBranches().clear();
        if (infoProposal.getBranches() != null && !infoProposal.getBranches().isEmpty()) {
            for (int i = 0; i < infoProposal.getBranches().size(); i++) {
                InfoBranch infoBranch = (InfoBranch) infoProposal.getBranches().get(i);
                if (infoBranch != null && infoBranch.getIdInternal() != null) {
                    Branch branch = rootDomainObject.readBranchByOID(infoBranch.getIdInternal());
                    if (branch != null) {
                        proposal.getBranches().add(branch);
                    }
                }
            }
        }

        proposal.setStatus(infoProposal.getStatus());
    }
}
