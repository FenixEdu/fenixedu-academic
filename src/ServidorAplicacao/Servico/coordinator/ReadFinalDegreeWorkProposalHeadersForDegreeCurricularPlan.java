/*
 * Created on 2004/03/09
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import DataBeans.finalDegreeWork.FinalDegreeWorkProposalHeader;
import DataBeans.finalDegreeWork.InfoGroup;
import DataBeans.finalDegreeWork.InfoGroupProposal;
import DataBeans.finalDegreeWork.InfoGroupStudent;
import DataBeans.finalDegreeWork.InfoProposal;
import Dominio.IPerson;
import Dominio.IStudent;
import Dominio.finalDegreeWork.IGroup;
import Dominio.finalDegreeWork.IGroupProposal;
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
 */
public class ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan implements IService {

    public ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan() {
        super();
    }

    public List run(Integer executionDegreeOID) throws FenixServiceException {
        List finalDegreeWorkProposalHeaders = new ArrayList();

        try {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
            IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                    .getIPersistentFinalDegreeWork();
            List finalDegreeWorkProposals = persistentFinalDegreeWork
                    .readFinalDegreeWorkProposalsByExecutionDegree(executionDegreeOID);
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

                        if (proposal.getGroupProposals() != null
                                && !proposal.getGroupProposals().isEmpty()) {
                            finalDegreeWorkProposalHeader.setGroupProposals(new ArrayList());
                            for (int j = 0; j < proposal.getGroupProposals().size(); j++) {
                                IGroupProposal groupProposal = (IGroupProposal) proposal
                                        .getGroupProposals().get(j);
                                if (groupProposal != null) {
                                    InfoGroupProposal infoGroupProposal = new InfoGroupProposal();
                                    infoGroupProposal.setIdInternal(groupProposal.getIdInternal());
                                    infoGroupProposal.setOrderOfPreference(groupProposal
                                            .getOrderOfPreference());
                                    IGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
                                    if (group != null) {
                                        InfoGroup infoGroup = new InfoGroup();
                                        infoGroup.setIdInternal(group.getIdInternal());
                                        if (group.getGroupStudents() != null) {
                                            infoGroup.setGroupStudents(new ArrayList());
                                            for (int k = 0; k < group.getGroupStudents().size(); k++) {
                                                IGroupStudent groupStudent = (IGroupStudent) group
                                                        .getGroupStudents().get(k);
                                                if (groupStudent != null) {
                                                    InfoGroupStudent infoGroupStudent = new InfoGroupStudent();
                                                    infoGroupStudent.setIdInternal(groupStudent
                                                            .getIdInternal());
                                                    infoGroupStudent
                                                            .setFinalDegreeDegreeWorkGroup(infoGroup);

                                                    IStudent student = groupStudent.getStudent();
                                                    if (student != null) {
                                                        InfoStudent infoStudent = new InfoStudent();
                                                        infoStudent.setIdInternal(student
                                                                .getIdInternal());
                                                        infoStudent.setNumber(student.getNumber());
                                                        IPerson person = student.getPerson();
                                                        if (person != null) {
                                                            InfoPerson infoPerson = new InfoPerson();
                                                            infoPerson.setIdInternal(person
                                                                    .getIdInternal());
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
                                                        IProposal proposalConfirmation = groupStudent
                                                                .getFinalDegreeWorkProposalConfirmation();

                                                        if (proposal != null) {
                                                            InfoProposal infoProposal = new InfoProposal();
                                                            infoProposal
                                                                    .setIdInternal(proposalConfirmation
                                                                            .getIdInternal());
                                                            infoGroupStudent
                                                                    .setFinalDegreeWorkProposalConfirmation(infoProposal);
                                                        }
                                                    }

                                                    IGroup attributedGroupByTeacher = proposal
                                                            .getGroupAttributedByTeacher();
                                                    if (attributedGroupByTeacher != null
                                                            && attributedGroupByTeacher.getIdInternal()
                                                                    .equals(group.getIdInternal())) {
                                                        finalDegreeWorkProposalHeader
                                                                .setGroupAttributedByTeacher(infoGroup);
                                                    }
                                                    IGroup attributedGroup = proposal
                                                            .getGroupAttributed();
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
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return finalDegreeWorkProposalHeaders;
    }
}