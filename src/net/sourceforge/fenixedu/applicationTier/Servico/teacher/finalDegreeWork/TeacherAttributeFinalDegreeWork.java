/*
 * Created on 2004/04/24
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;

/**
 * @author Luis Cruz
 */
public class TeacherAttributeFinalDegreeWork extends Service {

    public Boolean run(Integer selectedGroupProposalOID) throws FenixServiceException {
	final GroupProposal groupProposal = rootDomainObject.readGroupProposalByOID(selectedGroupProposalOID);

	if (groupProposal != null) {
	    final Proposal proposal = groupProposal.getFinalDegreeWorkProposal();
	    final FinalDegreeWorkGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();

	    if (proposal != null && group != null) {
		final Proposal proposalAttributedToGroup = group.getProposalAttributedByTeacher();
		if (proposalAttributedToGroup != null && proposalAttributedToGroup != proposal) {
		    throw new GroupAlreadyAttributed(proposalAttributedToGroup.getProposalNumber().toString());
		}

		if (proposal.getGroupAttributedByTeacher() == null || proposal.getGroupAttributedByTeacher() != group) {
		    proposal.setGroupAttributedByTeacher(group);
		    for (int i = 0; i < group.getGroupProposals().size(); i++) {
			GroupProposal otherGroupProposal = group.getGroupProposals().get(i);
			Proposal otherProposal = otherGroupProposal.getFinalDegreeWorkProposal();
			if (otherProposal != proposal && group == otherProposal.getGroupAttributedByTeacher()) {
			    otherProposal.setGroupAttributedByTeacher(null);
			}
		    }
		} else {
		    proposal.setGroupAttributedByTeacher(null);
		}
	    }
	}
	return Boolean.TRUE;
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