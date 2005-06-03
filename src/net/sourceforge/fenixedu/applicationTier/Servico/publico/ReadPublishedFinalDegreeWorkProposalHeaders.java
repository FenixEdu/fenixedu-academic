/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupStudent;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                    .getIPersistentFinalDegreeWork();
            List finalDegreeWorkProposals = persistentFinalDegreeWork
                    .readPublishedFinalDegreeWorkProposalsByExecutionDegree(executionDegreeOID);
            if (finalDegreeWorkProposals != null) {
                finalDegreeWorkProposalHeaders = new ArrayList();
                for (int i = 0; i < finalDegreeWorkProposals.size(); i++) {
                    IProposal proposal = (IProposal) finalDegreeWorkProposals.get(i);

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