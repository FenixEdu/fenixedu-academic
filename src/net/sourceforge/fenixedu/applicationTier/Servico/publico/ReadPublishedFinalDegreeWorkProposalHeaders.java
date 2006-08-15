/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupStudent;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class ReadPublishedFinalDegreeWorkProposalHeaders extends Service {

    public List run(Integer executionDegreeOID) throws ExcepcaoPersistencia {
        final List finalDegreeWorkProposalHeaders = new ArrayList();

        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeOID);
        if (executionDegree != null && executionDegree.getScheduling() != null) {
        Set<Proposal> finalDegreeWorkProposals = executionDegree.getScheduling().findPublishedProposals();
        if (finalDegreeWorkProposals != null) {
            for (final Proposal proposal : finalDegreeWorkProposals) {
                if (proposal != null) {
                    FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader = new FinalDegreeWorkProposalHeader();

                    finalDegreeWorkProposalHeader.setIdInternal(proposal.getIdInternal());
                    finalDegreeWorkProposalHeader.setProposalNumber(proposal.getProposalNumber());
                    finalDegreeWorkProposalHeader.setTitle(proposal.getTitle());
                    finalDegreeWorkProposalHeader.setExecutionYear(proposal.getScheduleing().getExecutionDegrees().iterator().next()
                            .getExecutionYear().getYear());
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
                            Branch branch = proposal.getBranches().get(j);

                            if (branch != null) {
                                InfoBranch infoBranch = new InfoBranch(branch);
                                finalDegreeWorkProposalHeader.getBranches().add(infoBranch);
                            }
                        }
                    }

                    if (proposal.getGroupAttributedByTeacher() != null) {
                        InfoGroup infoGroup = new InfoGroup();
                        infoGroup.setGroupStudents(new ArrayList());
                        for (int j = 0; j < proposal.getGroupAttributedByTeacher().getGroupStudents()
                                .size(); j++) {
                            GroupStudent groupStudent = proposal
                                    .getGroupAttributedByTeacher().getGroupStudents().get(j);
                            InfoGroupStudent infoGroupStudent = new InfoGroupStudent();
                            InfoStudent infoStudent = new InfoStudent();
                            InfoPerson infoPerson = new InfoPerson();
                            infoPerson.setUsername(groupStudent.getStudent().getPerson().getUsername());
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
                            GroupStudent groupStudent = proposal.getGroupAttributed()
                                    .getGroupStudents().get(j);
                            InfoGroupStudent infoGroupStudent = new InfoGroupStudent();
                            InfoStudent infoStudent = new InfoStudent();
                            InfoPerson infoPerson = new InfoPerson();
                            infoPerson.setUsername(groupStudent.getStudent().getPerson().getUsername());
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
        }

        return finalDegreeWorkProposalHeaders;
    }
}