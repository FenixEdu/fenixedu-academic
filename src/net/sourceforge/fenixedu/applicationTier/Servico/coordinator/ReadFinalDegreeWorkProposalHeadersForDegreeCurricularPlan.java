package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupStudent;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan extends Service {

    public List run(Integer executionDegreeOID) throws ExcepcaoPersistencia {
    	final List finalDegreeWorkProposalHeaders = new ArrayList();

    	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeOID);
    	final Scheduleing scheduleing = executionDegree.get$scheduling();
    	if (scheduleing != null) {
    		final List finalDegreeWorkProposals = scheduleing.getProposals();
            for (int i = 0; i < finalDegreeWorkProposals.size(); i++) {
                Proposal proposal = (Proposal) finalDegreeWorkProposals.get(i);

                if (proposal != null) {
                    FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader = new FinalDegreeWorkProposalHeader();

                    finalDegreeWorkProposalHeader.setIdInternal(proposal.getIdInternal());
                    finalDegreeWorkProposalHeader.setProposalNumber(proposal.getProposalNumber());
                    finalDegreeWorkProposalHeader.setTitle(proposal.getTitle());
                    finalDegreeWorkProposalHeader.setExecutionYear(proposal.getScheduleing().getExecutionDegrees()
                            .iterator().next().getExecutionYear().getYear());
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

                    if (proposal.getGroupProposals() != null && !proposal.getGroupProposals().isEmpty()) {
                        finalDegreeWorkProposalHeader.setGroupProposals(new ArrayList());
                        for (int j = 0; j < proposal.getGroupProposals().size(); j++) {
                            GroupProposal groupProposal = proposal.getGroupProposals().get(j);
                            if (groupProposal != null) {
                                InfoGroupProposal infoGroupProposal = new InfoGroupProposal();
                                infoGroupProposal.setIdInternal(groupProposal.getIdInternal());
                                infoGroupProposal.setOrderOfPreference(groupProposal
                                        .getOrderOfPreference());
                                Group group = groupProposal.getFinalDegreeDegreeWorkGroup();
                                if (group != null) {
                                    InfoGroup infoGroup = new InfoGroup();
                                    infoGroup.setIdInternal(group.getIdInternal());
                                    if (group.getGroupStudents() != null) {
                                        infoGroup.setGroupStudents(new ArrayList());
                                        for (int k = 0; k < group.getGroupStudents().size(); k++) {
                                            GroupStudent groupStudent = group.getGroupStudents().get(k);
                                            if (groupStudent != null) {
                                                InfoGroupStudent infoGroupStudent = new InfoGroupStudent();
                                                infoGroupStudent.setIdInternal(groupStudent
                                                        .getIdInternal());
                                                infoGroupStudent
                                                        .setFinalDegreeDegreeWorkGroup(infoGroup);

                                                Registration student = groupStudent.getStudent();
                                                if (student != null) {
                                                    InfoStudent infoStudent = new InfoStudent();
                                                    infoStudent.setIdInternal(student.getIdInternal());
                                                    infoStudent.setNumber(student.getNumber());
                                                    Person person = student.getPerson();
                                                    if (person != null) {
                                                        InfoPerson infoPerson = new InfoPerson();
                                                        infoPerson.setIdInternal(person.getIdInternal());
                                                        infoPerson.setUsername(person.getUsername());
                                                        infoPerson.setNome(person.getNome());
                                                        infoPerson.setEmail(person.getEmail());
                                                        infoPerson.setTelefone(person.getTelefone());
                                                        infoStudent.setInfoPerson(infoPerson);
                                                    }
                                                    infoGroupStudent.setStudent(infoStudent);
                                                }

                                                if (groupStudent
                                                        .getFinalDegreeWorkProposalConfirmation() != null) {
                                                    Proposal proposalConfirmation = groupStudent
                                                            .getFinalDegreeWorkProposalConfirmation();

                                                    if (proposal != null) {
                                                        InfoProposal infoProposal = new InfoProposal();
                                                        infoProposal.setIdInternal(proposalConfirmation
                                                                .getIdInternal());
                                                        infoGroupStudent
                                                                .setFinalDegreeWorkProposalConfirmation(infoProposal);
                                                    }
                                                }

                                                Group attributedGroupByTeacher = proposal
                                                        .getGroupAttributedByTeacher();
                                                if (attributedGroupByTeacher != null
                                                        && attributedGroupByTeacher.getIdInternal()
                                                                .equals(group.getIdInternal())) {
                                                    finalDegreeWorkProposalHeader
                                                            .setGroupAttributedByTeacher(infoGroup);
                                                }
                                                Group attributedGroup = proposal.getGroupAttributed();
                                                if (attributedGroup != null
                                                        && attributedGroup.getIdInternal().equals(
                                                                group.getIdInternal())) {
                                                    finalDegreeWorkProposalHeader
                                                            .setGroupAttributed(infoGroup);
                                                }

                                                infoGroup.getGroupStudents().add(infoGroupStudent);
                                            }
                                        }
                                    }
                                    infoGroupProposal.setInfoGroup(infoGroup);
                                    finalDegreeWorkProposalHeader.getGroupProposals().add(
                                            infoGroupProposal);
                                }
                            }
                        }
                    }

                    finalDegreeWorkProposalHeaders.add(finalDegreeWorkProposalHeader);
                }
            }
        }

        return finalDegreeWorkProposalHeaders;
    }
}