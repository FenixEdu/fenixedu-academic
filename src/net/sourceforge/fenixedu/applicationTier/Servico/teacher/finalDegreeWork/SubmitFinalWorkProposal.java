/*
 * Created on Mar 11, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import java.util.ArrayList;

import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IScheduleing;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfPeriodException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SubmitFinalWorkProposal implements IService {

    /**
     *  
     */
    public SubmitFinalWorkProposal() {
        super();
    }

    public void run(InfoProposal infoProposal) throws FenixServiceException {
        try {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

            IPersistentFinalDegreeWork persistentFinalWork = persistentSupport
                    .getIPersistentFinalDegreeWork();
            IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                    .getIPersistentExecutionCourse();
            IPersistentBranch persistentBranch = persistentSupport.getIPersistentBranch();

            Integer executionDegreeId = infoProposal.getExecutionDegree().getIdInternal();
            IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionCourse.readByOID(
                    ExecutionDegree.class, executionDegreeId);

            IScheduleing scheduleing = persistentFinalWork
                    .readFinalDegreeWorkScheduleing(executionDegreeId);
            if(scheduleing == null){
                throw new OutOfPeriodException(null, null, null);
            }

            IProposal proposal = null;
            if (infoProposal.getIdInternal() != null) {
                proposal = (IProposal) persistentFinalWork.readByOID(Proposal.class, infoProposal
                        .getIdInternal());
            }
            if (proposal == null) {
                proposal = new Proposal();
                persistentFinalWork.simpleLockWrite(proposal);
                int proposalNumber = scheduleing.getCurrentProposalNumber().intValue();
                proposal.setProposalNumber(new Integer(proposalNumber));
                persistentFinalWork.simpleLockWrite(scheduleing);
                scheduleing.setCurrentProposalNumber(new Integer(proposalNumber + 1));
            } else {
                persistentFinalWork.simpleLockWrite(proposal);
            }

            proposal.setCompanionName(infoProposal.getCompanionName());
            proposal.setCompanionMail(infoProposal.getCompanionMail());
            proposal.setCompanionPhone(infoProposal.getCompanionPhone());
            proposal.setCompanyAdress(infoProposal.getCompanyAdress());
            proposal.setCompanyName(infoProposal.getCompanyName());

            if (infoProposal.getCoorientator() != null) {
                Integer coorientatorId = infoProposal.getCoorientator().getIdInternal();
                ITeacher coorientator = (ITeacher) persistentTeacher.readByOID(Teacher.class,
                        coorientatorId);
                proposal.setCoorientator(coorientator);
            } else {
                proposal.setCoorientator(null);
            }

            proposal.setCoorientatorsCreditsPercentage(infoProposal.getCoorientatorsCreditsPercentage());
            proposal.setDegreeType(infoProposal.getDegreeType());
            proposal.setDeliverable(infoProposal.getDeliverable());
            proposal.setDescription(infoProposal.getDescription());

            proposal.setExecutionDegree(executionDegree);
            proposal.setFraming(infoProposal.getFraming());
            proposal.setLocation(infoProposal.getLocation());

            proposal.setMaximumNumberOfGroupElements(infoProposal.getMaximumNumberOfGroupElements());
            proposal.setMinimumNumberOfGroupElements(infoProposal.getMinimumNumberOfGroupElements());
            proposal.setObjectives(infoProposal.getObjectives());
            proposal.setObservations(infoProposal.getObservations());

            Integer orientatorId = infoProposal.getOrientator().getIdInternal();
            ITeacher orientator = (ITeacher) persistentTeacher.readByOID(Teacher.class, orientatorId);

            proposal.setOrientator(orientator);
            proposal.setOrientatorsCreditsPercentage(infoProposal.getOrientatorsCreditsPercentage());
            proposal.setRequirements(infoProposal.getRequirements());
            proposal.setTitle(infoProposal.getTitle());
            proposal.setUrl(infoProposal.getUrl());

            proposal.setBranches(new ArrayList());
            if (infoProposal.getBranches() != null && !infoProposal.getBranches().isEmpty()) {
                for (int i = 0; i < infoProposal.getBranches().size(); i++) {
                    InfoBranch infoBranch = (InfoBranch) infoProposal.getBranches().get(i);
                    if (infoBranch != null && infoBranch.getIdInternal() != null) {
                        IBranch branch = (IBranch) persistentBranch.readByOID(Branch.class, infoBranch
                                .getIdInternal());
                        if (branch != null) {
                            proposal.getBranches().add(branch);
                        }
                    }
                }
            }

            proposal.setStatus(infoProposal.getStatus());
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}