package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class RemoveProposalFromFinalDegreeWorkStudentGroup extends FenixService {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static Boolean run(final FinalDegreeWorkGroup group, Integer groupProposalOID) throws FenixServiceException {
        final GroupProposal groupProposal = findGroupProposal(group, groupProposalOID);
        if (groupProposal != null) {
            if (group.getProposalAttributed() == groupProposal.getFinalDegreeWorkProposal()) {
                throw new GroupProposalAttributedException();
            } else if (group.getProposalAttributedByTeacher() == groupProposal.getFinalDegreeWorkProposal()) {
                throw new GroupProposalAttributedByTeacherException();
            }

            for (int i = 0; i < group.getGroupProposals().size(); i++) {
                final GroupProposal otherGroupProposal = group.getGroupProposals().get(i);
                if (!groupProposal.equals(otherGroupProposal)
                        && groupProposal.getOrderOfPreference().intValue() < otherGroupProposal.getOrderOfPreference().intValue()) {
                    otherGroupProposal
                            .setOrderOfPreference(new Integer(otherGroupProposal.getOrderOfPreference().intValue() - 1));
                }
            }

            groupProposal.delete();
            return true;
        }
        return false;

    }

    private static GroupProposal findGroupProposal(final FinalDegreeWorkGroup group, final Integer groupProposalOID) {
        for (final GroupProposal groupProposal : group.getGroupProposalsSet()) {
            if (groupProposal.getIdInternal().equals(groupProposalOID)) {
                return groupProposal;
            }
        }
        return null;
    }

    public static class GroupProposalAttributedException extends FenixServiceException {
    }

    public static class GroupProposalAttributedByTeacherException extends FenixServiceException {
    }

}