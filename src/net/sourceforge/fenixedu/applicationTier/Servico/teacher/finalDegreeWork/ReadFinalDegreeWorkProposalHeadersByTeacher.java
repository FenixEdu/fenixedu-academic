/*
 * Created on 2004/03/13
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupStudent;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkProposalHeadersByTeacher extends Service {

	public List run(Integer teacherOID) throws FenixServiceException, ExcepcaoPersistencia {
		List finalDegreeWorkProposalHeaders = new ArrayList();

		final Teacher teacher = rootDomainObject.readTeacherByOID(teacherOID);
		Set<Proposal> finalDegreeWorkProposals = teacher.findFinalDegreeWorkProposals();

		if (finalDegreeWorkProposals != null) {
			finalDegreeWorkProposalHeaders = new ArrayList();
			for (final Proposal proposal : finalDegreeWorkProposals) {
				final Scheduleing scheduleing = proposal.getScheduleing();
				for (final ExecutionDegree executionDegree : scheduleing.getExecutionDegrees()) {

				if (proposal != null) {
					FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader = new FinalDegreeWorkProposalHeader();

					finalDegreeWorkProposalHeader.setIdInternal(proposal.getIdInternal());
					finalDegreeWorkProposalHeader.setExecutionDegreeOID(executionDegree.getIdInternal());
					finalDegreeWorkProposalHeader.setTitle(proposal.getTitle());
					finalDegreeWorkProposalHeader.setExecutionYear(executionDegree.getExecutionYear().getYear());
					finalDegreeWorkProposalHeader.setProposalNumber(proposal.getProposalNumber());
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
					finalDegreeWorkProposalHeader.setDegreeCode(executionDegree
							.getDegreeCurricularPlan().getDegree().getSigla());

					if (scheduleing != null
							&& scheduleing.getStartOfProposalPeriod() != null
							&& scheduleing.getEndOfProposalPeriod() != null
							&& scheduleing.getStartOfProposalPeriod().before(
									Calendar.getInstance().getTime())
							&& scheduleing.getEndOfProposalPeriod().after(
									Calendar.getInstance().getTime())) {
						finalDegreeWorkProposalHeader.setEditable(new Boolean(true));
					} else {
						finalDegreeWorkProposalHeader.setEditable(new Boolean(false));
					}

					if (scheduleing != null && scheduleing.getAttributionByTeachers() != null && scheduleing.getAttributionByTeachers().booleanValue() 
							&& proposal.getGroupProposals() != null && !proposal.getGroupProposals().isEmpty()) {
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
													InfoStudent infoStudent = new InfoStudent(student);
													infoGroupStudent.setStudent(infoStudent);
												}
												infoGroup.getGroupStudents().add(infoGroupStudent);
											}
										}
									}
									infoGroupProposal.setInfoGroup(infoGroup);

									if (proposal.getGroupAttributedByTeacher() != null
											&& proposal.getGroupAttributedByTeacher().getIdInternal()
													.equals(group.getIdInternal())) {
										finalDegreeWorkProposalHeader
												.setGroupAttributedByTeacher(infoGroup);
									}

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
		}

		return finalDegreeWorkProposalHeaders;
	}
}