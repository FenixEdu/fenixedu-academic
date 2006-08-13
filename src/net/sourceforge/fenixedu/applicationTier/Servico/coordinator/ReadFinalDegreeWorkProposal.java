/*
 * Created on 2004/03/09
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
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
                infoProposal.setOrientator(new InfoTeacher());
                infoProposal.getOrientator().setIdInternal(proposal.getOrientator().getIdInternal());
                infoProposal.getOrientator().setTeacherNumber(
                        proposal.getOrientator().getTeacherNumber());
                if (proposal.getOrientator().getPerson() != null) {
                    infoProposal.getOrientator().setInfoPerson(new InfoPerson());
                    infoProposal.getOrientator().getInfoPerson().setIdInternal(
                            proposal.getOrientator().getPerson().getIdInternal());
                    infoProposal.getOrientator().getInfoPerson().setNome(
                            proposal.getOrientator().getPerson().getNome());
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
                infoProposal.setCoorientator(new InfoTeacher());
                infoProposal.getCoorientator().setIdInternal(proposal.getCoorientator().getIdInternal());
                infoProposal.getCoorientator().setTeacherNumber(
                        proposal.getCoorientator().getTeacherNumber());
                if (proposal.getCoorientator().getPerson() != null) {
                    infoProposal.getCoorientator().setInfoPerson(new InfoPerson());
                    infoProposal.getCoorientator().getInfoPerson().setIdInternal(
                            proposal.getCoorientator().getPerson().getIdInternal());
                    infoProposal.getCoorientator().getInfoPerson().setNome(
                            proposal.getCoorientator().getPerson().getNome());
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
                        InfoBranch infoBranch = new InfoBranch();
                        infoBranch.setIdInternal(branch.getIdInternal());
                        infoBranch.setName(branch.getName());
                        infoProposal.getBranches().add(infoBranch);
                    }
                }
            }

            if (!proposal.getScheduleing().getExecutionDegreesSet().isEmpty()) {
            	final ExecutionDegree executionDegree = proposal.getScheduleing().getExecutionDegrees().iterator().next();
                infoProposal.setExecutionDegree(new InfoExecutionDegree());
                infoProposal.getExecutionDegree().setIdInternal(
                        proposal.getScheduleing().getExecutionDegrees().iterator().next().getIdInternal());

                if (executionDegree.getExecutionYear() != null) {
                    infoProposal.getExecutionDegree().setInfoExecutionYear(
                            InfoExecutionYear.newInfoFromDomain(executionDegree.getExecutionYear()));
                }

                if (executionDegree.getDegreeCurricularPlan() != null
                        && executionDegree.getDegreeCurricularPlan().getDegree() != null) {
                	final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                	infoProposal.getExecutionDegree().setInfoDegreeCurricularPlan(new InfoDegreeCurricularPlan(degreeCurricularPlan));
                }
            }
        }

        return infoProposal;
    }
}