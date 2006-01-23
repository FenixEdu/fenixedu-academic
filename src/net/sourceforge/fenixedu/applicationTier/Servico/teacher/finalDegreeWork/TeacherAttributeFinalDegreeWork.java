/*
 * Created on 2004/04/24
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;

/**
 * @author Luis Cruz
 */
public class TeacherAttributeFinalDegreeWork extends Service {

	public TeacherAttributeFinalDegreeWork() {
		super();
	}

	public Boolean run(Integer selectedGroupProposalOID) throws FenixServiceException,
			ExcepcaoPersistencia {
		IPersistentFinalDegreeWork persistentFinalWork = persistentSupport
				.getIPersistentFinalDegreeWork();

		GroupProposal groupProposal = (GroupProposal) persistentObject.readByOID(
				GroupProposal.class, selectedGroupProposalOID);
		if (groupProposal != null) {
			Proposal proposal = groupProposal.getFinalDegreeWorkProposal();
			Group group = groupProposal.getFinalDegreeDegreeWorkGroup();
			if (proposal != null && group != null) {
				Proposal proposalAttributedToGroup = persistentFinalWork
						.readFinalDegreeWorkAttributedToGroupByTeacher(group.getIdInternal());
				if (proposalAttributedToGroup != null
						&& !proposalAttributedToGroup.getIdInternal().equals(proposal.getIdInternal())) {
					throw new GroupAlreadyAttributed(proposalAttributedToGroup.getProposalNumber()
							.toString());
				}

				if (proposal.getGroupAttributedByTeacher() == null
						|| !proposal.getGroupAttributedByTeacher().equals(group)) {
					proposal.setGroupAttributedByTeacher(group);
					for (int i = 0; i < group.getGroupProposals().size(); i++) {
						GroupProposal otherGroupProposal = group.getGroupProposals().get(i);
						Proposal otherProposal = otherGroupProposal.getFinalDegreeWorkProposal();
						if (!otherProposal.getIdInternal().equals(proposal.getIdInternal())
								&& group.equals(otherProposal.getGroupAttributedByTeacher())) {
							otherProposal.setGroupAttributedByTeacher(null);
						}
					}
				} else {
					proposal.setGroupAttributedByTeacher(null);
				}
			}
		}
		return new Boolean(true);
	}

	public class GroupAlreadyAttributed extends FenixServiceException {

		public GroupAlreadyAttributed() {
			super();
		}

		public GroupAlreadyAttributed(int errorType) {
			super(errorType);
		}

		public GroupAlreadyAttributed(String s) {
			super(s);
		}

		public GroupAlreadyAttributed(Throwable cause) {
			super(cause);
		}

		public GroupAlreadyAttributed(String message, Throwable cause) {
			super(message, cause);
		}

	}

}