/*
 * Created on Mar 11, 2004
 *  
 */
package ServidorAplicacao.Servico.teacher.finalDegreeWork;

import java.util.ArrayList;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoBranch;
import DataBeans.finalDegreeWork.InfoProposal;
import Dominio.Branch;
import Dominio.CursoExecucao;
import Dominio.IBranch;
import Dominio.ICursoExecucao;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.finalDegreeWork.IProposal;
import Dominio.finalDegreeWork.IScheduleing;
import Dominio.finalDegreeWork.Proposal;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.OutOfPeriodException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionCourse.readByOID(
                    CursoExecucao.class, executionDegreeId);

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
                //System.out.println("Read proposal number= " +
                // proposalNumber);
                proposal.setProposalNumber(new Integer(proposalNumber));
                persistentFinalWork.simpleLockWrite(scheduleing);
                scheduleing.setCurrentProposalNumber(new Integer(proposalNumber + 1));
                //System.out.println("Read next proposal number= " +
                // scheduleing.getCurrentProposalNumber());
                //System.out.println("Read proposal number= " +
                // proposal.getProposalNumber());
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