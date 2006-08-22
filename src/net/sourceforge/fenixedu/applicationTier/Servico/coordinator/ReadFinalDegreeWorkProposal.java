/*
 * Created on 2004/03/09
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class ReadFinalDegreeWorkProposal extends Service {

    public InfoProposal run(Integer finalDegreeWorkProposalOID) throws FenixServiceException, ExcepcaoPersistencia {
        InfoProposal infoProposal = null;

        Proposal proposal = rootDomainObject.readProposalByOID(
                finalDegreeWorkProposalOID);

        if (proposal != null) {
            infoProposal = new InfoProposal();
            infoProposal.setIdInternal(proposal.getIdInternal());
            infoProposal.setTitle(proposal.getTitle());
            infoProposal.setProposalNumber(proposal.getProposalNumber());

            if (proposal.getOrientator() != null) {
                infoProposal.setOrientator(new InfoTeacher(proposal.getOrientator()));
                if (proposal.getOrientator().getPerson() != null) {
                    Department department = proposal.getOrientator().getCurrentWorkingDepartment();
                    if (department != null) {
                        infoProposal.setOrientatorsDepartment(new InfoDepartment());
                        infoProposal.getOrientatorsDepartment()
                                .setIdInternal(department.getIdInternal());
                        infoProposal.getOrientatorsDepartment().setName(department.getName());
                    }
                }
            }

            if (proposal.getCoorientator() != null) {
                infoProposal.setCoorientator(new InfoTeacher(proposal.getCoorientator()));
                if (proposal.getCoorientator().getPerson() != null) {
                    Department department = proposal.getCoorientator().getCurrentWorkingDepartment();
                    if (department != null) {
                        infoProposal.setCoorientatorsDepartment(new InfoDepartment());
                        infoProposal.getCoorientatorsDepartment().setIdInternal(
                                department.getIdInternal());
                        infoProposal.getCoorientatorsDepartment().setName(department.getName());
                    }
                }
            }

            infoProposal.setCompanionName(proposal.getCompanionName());
            infoProposal.setCompanionMail(proposal.getCompanionMail());
            infoProposal.setCompanionPhone(proposal.getCompanionPhone());
            infoProposal.setOrientatorsCreditsPercentage(proposal.getOrientatorsCreditsPercentage());
            infoProposal.setCoorientatorsCreditsPercentage(proposal.getCoorientatorsCreditsPercentage());
            infoProposal.setFraming(proposal.getFraming());
            infoProposal.setObjectives(proposal.getObjectives());
            infoProposal.setDescription(proposal.getDescription());
            infoProposal.setRequirements(proposal.getRequirements());
            infoProposal.setDeliverable(proposal.getDeliverable());
            infoProposal.setUrl(proposal.getUrl());
            infoProposal.setMinimumNumberOfGroupElements(proposal.getMinimumNumberOfGroupElements());
            infoProposal.setMaximumNumberOfGroupElements(proposal.getMaximumNumberOfGroupElements());
            infoProposal.setLocation(proposal.getLocation());
            infoProposal.setDegreeType(proposal.getDegreeType());
            infoProposal.setObservations(proposal.getObservations());
            infoProposal.setCompanyName(proposal.getCompanyName());
            infoProposal.setCompanyAdress(proposal.getCompanyAdress());
            infoProposal.setStatus(proposal.getStatus());

            if (proposal.getBranches() != null && !proposal.getBranches().isEmpty()) {
                infoProposal.setBranches(new ArrayList());
                for (int i = 0; i < proposal.getBranches().size(); i++) {
                    Branch branch = proposal.getBranches().get(i);
                    if (branch != null) {
                        InfoBranch infoBranch = new InfoBranch(branch);
                        infoProposal.getBranches().add(infoBranch);
                    }
                }
            }

            if (!proposal.getScheduleing().getExecutionDegreesSet().isEmpty()) {
            	final ExecutionDegree executionDegree = proposal.getScheduleing().getExecutionDegrees().iterator().next();
                infoProposal.setExecutionDegree(new InfoExecutionDegree(executionDegree));
            }
        }

        return infoProposal;
    }
}