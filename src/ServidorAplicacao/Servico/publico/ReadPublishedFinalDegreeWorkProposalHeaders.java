/*
 * Created on 2004/04/04
 *  
 */
package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoBranch;
import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import DataBeans.finalDegreeWork.FinalDegreeWorkProposalHeader;
import DataBeans.finalDegreeWork.InfoGroup;
import DataBeans.finalDegreeWork.InfoGroupStudent;
import Dominio.IBranch;
import Dominio.finalDegreeWork.IGroupStudent;
import Dominio.finalDegreeWork.IProposal;
import Dominio.finalDegreeWork.Proposal;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class ReadPublishedFinalDegreeWorkProposalHeaders implements IService {

    public ReadPublishedFinalDegreeWorkProposalHeaders() {
        super();
    }

    public List run(Integer executionDegreeOID) throws FenixServiceException {
        List finalDegreeWorkProposalHeaders = new ArrayList();

        try {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
            IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                    .getIPersistentFinalDegreeWork();
            List finalDegreeWorkProposals = persistentFinalDegreeWork
                    .readPublishedFinalDegreeWorkProposalsByExecutionDegree(executionDegreeOID);
            if (finalDegreeWorkProposals != null) {
                finalDegreeWorkProposalHeaders = new ArrayList();
                for (int i = 0; i < finalDegreeWorkProposals.size(); i++) {
                    IProposal proposal = (Proposal) finalDegreeWorkProposals.get(i);

                    if (proposal != null) {
                        FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader = new FinalDegreeWorkProposalHeader();

                        finalDegreeWorkProposalHeader.setIdInternal(proposal.getIdInternal());
                        finalDegreeWorkProposalHeader.setProposalNumber(proposal.getProposalNumber());
                        finalDegreeWorkProposalHeader.setTitle(proposal.getTitle());
                        finalDegreeWorkProposalHeader.setExecutionYear(proposal.getExecutionDegree().getExecutionYear().getYear());
                        if (proposal.getOrientator() != null) {
                            finalDegreeWorkProposalHeader.setOrientatorOID(proposal.getOrientator()
                                    .getIdInternal());
                            finalDegreeWorkProposalHeader.setOrientatorName(proposal.getOrientator()
                                    .getPerson().getNome());
                        }
                        if (proposal.getCoorientator() != null) {
                            finalDegreeWorkProposalHeader.setCoorientatorOID(proposal.getCoorientator()
                                    .getIdInternal());
                            finalDegreeWorkProposalHeader.setCoorientatorName(proposal.getCoorientator()
                                    .getPerson().getNome());
                        }
                        finalDegreeWorkProposalHeader.setCompanyLink(proposal.getCompanionName());
                        finalDegreeWorkProposalHeader.setStatus(proposal.getStatus());

                        if (proposal.getBranches() != null) {
                            finalDegreeWorkProposalHeader.setBranches(new ArrayList());
                            for (int j = 0; j < proposal.getBranches().size(); j++) {
                                IBranch branch = (IBranch) proposal.getBranches().get(j);

                                if (branch != null) {
                                    InfoBranch infoBranch = new InfoBranch();
                                    infoBranch.setIdInternal(branch.getIdInternal());
                                    infoBranch.setName(branch.getName());
                                    finalDegreeWorkProposalHeader.getBranches().add(infoBranch);
                                }
                            }
                        }

                        if (proposal.getGroupAttributedByTeacher() != null) {
                            InfoGroup infoGroup = new InfoGroup();
                            infoGroup.setGroupStudents(new ArrayList());
                            for (int j = 0; j < proposal.getGroupAttributedByTeacher()
                                    .getGroupStudents().size(); j++) {
                                IGroupStudent groupStudent = (IGroupStudent) proposal
                                        .getGroupAttributedByTeacher().getGroupStudents().get(j);
                                InfoGroupStudent infoGroupStudent = new InfoGroupStudent();
                                InfoStudent infoStudent = new InfoStudent();
                                InfoPerson infoPerson = new InfoPerson();
                                infoPerson.setUsername(groupStudent.getStudent().getPerson()
                                        .getUsername());
                                infoStudent.setInfoPerson(infoPerson);
                                infoStudent.setNumber(groupStudent.getStudent().getNumber());
                                infoGroupStudent.setStudent(infoStudent);
                                infoGroup.getGroupStudents().add(infoGroupStudent);
                            }
                            finalDegreeWorkProposalHeader.setGroupAttributedByTeacher(infoGroup);
                        }

                        if (proposal.getGroupAttributed() != null) {
                            InfoGroup infoGroup = new InfoGroup();
                            infoGroup.setGroupStudents(new ArrayList());
                            for (int j = 0; j < proposal.getGroupAttributed().getGroupStudents().size(); j++) {
                                IGroupStudent groupStudent = (IGroupStudent) proposal
                                        .getGroupAttributed().getGroupStudents().get(j);
                                InfoGroupStudent infoGroupStudent = new InfoGroupStudent();
                                InfoStudent infoStudent = new InfoStudent();
                                InfoPerson infoPerson = new InfoPerson();
                                infoPerson.setUsername(groupStudent.getStudent().getPerson()
                                        .getUsername());
                                infoStudent.setInfoPerson(infoPerson);
                                infoStudent.setNumber(groupStudent.getStudent().getNumber());
                                infoGroupStudent.setStudent(infoStudent);
                                infoGroup.getGroupStudents().add(infoGroupStudent);
                            }
                            finalDegreeWorkProposalHeader.setGroupAttributed(infoGroup);
                        }

                        finalDegreeWorkProposalHeaders.add(finalDegreeWorkProposalHeader);
                    }
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return finalDegreeWorkProposalHeaders;
    }
}