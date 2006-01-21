/*
 * Created on 2004/04/15
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupStudent;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkStudentGroupByUsername extends Service {

    public InfoGroup run(String username) throws ExcepcaoPersistencia {
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        Group group = persistentFinalDegreeWork.readFinalDegreeWorkGroupByUsername(username);
        InfoGroup infoGroup = null;
        if (group != null) {
            infoGroup = new InfoGroup();
            infoGroup.setIdInternal(group.getIdInternal());
            infoGroup.setExecutionDegree(new InfoExecutionDegree());
            infoGroup.getExecutionDegree().setIdInternal(group.getExecutionDegree().getIdInternal());
            if (group.getGroupStudents() != null) {
                infoGroup.setGroupStudents(new ArrayList());

                for (int i = 0; i < group.getGroupStudents().size(); i++) {
                    GroupStudent groupStudent = group.getGroupStudents().get(i);
                    if (groupStudent != null) {
                        InfoGroupStudent infoGroupStudent = new InfoGroupStudent();
                        infoGroupStudent.setIdInternal(groupStudent.getIdInternal());

                        Student student = groupStudent.getStudent();
                        if (student != null) {
                            InfoStudent infoStudent = new InfoStudent();
                            infoStudent.setIdInternal(student.getIdInternal());
                            infoStudent.setNumber(student.getNumber());
                            if (student.getPerson() != null) {
                                InfoPerson infoPerson = new InfoPerson();
                                infoPerson.setIdInternal(student.getPerson().getIdInternal());
                                infoPerson.setNome(student.getPerson().getNome());
                                infoPerson.setUsername(student.getPerson().getUsername());
                                infoStudent.setInfoPerson(infoPerson);
                            }
                            infoGroupStudent.setStudent(infoStudent);
                        }

                        if (groupStudent.getFinalDegreeWorkProposalConfirmation() != null) {
                            Proposal proposal = groupStudent.getFinalDegreeWorkProposalConfirmation();

                            if (proposal != null) {
                                InfoProposal infoProposal = new InfoProposal();
                                infoProposal.setIdInternal(proposal.getIdInternal());
                                infoGroupStudent.setFinalDegreeWorkProposalConfirmation(infoProposal);
                            }
                        }

                        infoGroup.getGroupStudents().add(infoGroupStudent);
                    }
                }
            }
            if (group.getGroupProposals() != null) {
                infoGroup.setGroupProposals(new ArrayList());
                for (int i = 0; i < group.getGroupProposals().size(); i++) {
                    GroupProposal groupProposal = group.getGroupProposals().get(i);
                    if (groupProposal != null) {
                        InfoGroupProposal infoGroupProposal = new InfoGroupProposal();
                        infoGroupProposal.setIdInternal(groupProposal.getIdInternal());
                        infoGroupProposal.setOrderOfPreference(groupProposal.getOrderOfPreference());
                        Proposal proposal = groupProposal.getFinalDegreeWorkProposal();
                        if (proposal != null) {
                            InfoProposal infoProposal = new InfoProposal();
                            infoProposal.setIdInternal(proposal.getIdInternal());
                            infoProposal.setProposalNumber(proposal.getProposalNumber());
                            infoProposal.setTitle(proposal.getTitle());
                            Teacher orientator = proposal.getOrientator();
                            if (orientator != null) {
                                InfoTeacher infoTeacher = new InfoTeacher();
                                Person person = orientator.getPerson();
                                if (person != null) {
                                    InfoPerson infoPerson = new InfoPerson();
                                    infoPerson.setNome(person.getNome());
                                    infoTeacher.setInfoPerson(infoPerson);
                                }
                                infoProposal.setOrientator(infoTeacher);
                            }
                            Teacher coOrientator = proposal.getCoorientator();
                            if (coOrientator != null) {
                                InfoTeacher infoTeacher = new InfoTeacher();
                                Person person = coOrientator.getPerson();
                                if (person != null) {
                                    InfoPerson infoPerson = new InfoPerson();
                                    infoPerson.setNome(person.getNome());
                                    infoTeacher.setInfoPerson(infoPerson);
                                }
                                infoProposal.setCoorientator(infoTeacher);
                            }
                            infoProposal.setCompanionName(proposal.getCompanionName());
                            Group attributedGroup = proposal.getGroupAttributedByTeacher();
                            if (attributedGroup != null
                                    && attributedGroup.getIdInternal().equals(group.getIdInternal())) {
                                infoProposal.setGroupAttributedByTeacher(infoGroup);
                            }

                            infoGroupProposal.setFinalDegreeWorkProposal(infoProposal);
                        }
                        infoGroup.getGroupProposals().add(infoGroupProposal);
                    }
                }
            }
        }
        return infoGroup;
    }

}