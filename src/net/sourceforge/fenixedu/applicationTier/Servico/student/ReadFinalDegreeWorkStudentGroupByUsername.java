/*
 * Created on 2004/04/15
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupStudent;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkStudentGroupByUsername implements IService {

    public ReadFinalDegreeWorkStudentGroupByUsername() {
        super();
    }

    public InfoGroup run(String username) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        IGroup group = persistentFinalDegreeWork.readFinalDegreeWorkGroupByUsername(username);
        InfoGroup infoGroup = null;
        if (group != null) {
            infoGroup = new InfoGroup();
            infoGroup.setIdInternal(group.getIdInternal());
            infoGroup.setExecutionDegree(new InfoExecutionDegree());
            infoGroup.getExecutionDegree().setIdInternal(group.getExecutionDegree().getIdInternal());
            if (group.getGroupStudents() != null) {
                infoGroup.setGroupStudents(new ArrayList());

                for (int i = 0; i < group.getGroupStudents().size(); i++) {
                    IGroupStudent groupStudent = (IGroupStudent) group.getGroupStudents().get(i);
                    if (groupStudent != null) {
                        InfoGroupStudent infoGroupStudent = new InfoGroupStudent();
                        infoGroupStudent.setIdInternal(groupStudent.getIdInternal());

                        IStudent student = groupStudent.getStudent();
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
                            IProposal proposal = groupStudent.getFinalDegreeWorkProposalConfirmation();

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
                    IGroupProposal groupProposal = (IGroupProposal) group.getGroupProposals().get(i);
                    if (groupProposal != null) {
                        try {
                            groupProposal.getIdInternal();

                            InfoGroupProposal infoGroupProposal = new InfoGroupProposal();
                            infoGroupProposal.setIdInternal(groupProposal.getIdInternal());
                            infoGroupProposal.setOrderOfPreference(groupProposal.getOrderOfPreference());
                            IProposal proposal = groupProposal.getFinalDegreeWorkProposal();
                            if (proposal != null) {
                                InfoProposal infoProposal = new InfoProposal();
                                infoProposal.setIdInternal(proposal.getIdInternal());
                                infoProposal.setProposalNumber(proposal.getProposalNumber());
                                infoProposal.setTitle(proposal.getTitle());
                                ITeacher orientator = proposal.getOrientator();
                                if (orientator != null) {
                                    InfoTeacher infoTeacher = new InfoTeacher();
                                    IPerson person = orientator.getPerson();
                                    if (person != null) {
                                        InfoPerson infoPerson = new InfoPerson();
                                        infoPerson.setNome(person.getNome());
                                        infoTeacher.setInfoPerson(infoPerson);
                                    }
                                    infoProposal.setOrientator(infoTeacher);
                                }
                                ITeacher coOrientator = proposal.getCoorientator();
                                if (coOrientator != null) {
                                    InfoTeacher infoTeacher = new InfoTeacher();
                                    IPerson person = coOrientator.getPerson();
                                    if (person != null) {
                                        InfoPerson infoPerson = new InfoPerson();
                                        infoPerson.setNome(person.getNome());
                                        infoTeacher.setInfoPerson(infoPerson);
                                    }
                                    infoProposal.setCoorientator(infoTeacher);
                                }
                                infoProposal.setCompanionName(proposal.getCompanionName());
                                IGroup attributedGroup = proposal.getGroupAttributedByTeacher();
                                if (attributedGroup != null
                                        && attributedGroup.getIdInternal().equals(group.getIdInternal())) {
                                    infoProposal.setGroupAttributedByTeacher(infoGroup);
                                }

                                infoGroupProposal.setFinalDegreeWorkProposal(infoProposal);
                            }
                            infoGroup.getGroupProposals().add(infoGroupProposal);
                        } catch (Exception e) {
                            // proxy to groupProposal is null
                        }
                    }
                }
            }
        }
        return infoGroup;
    }

}