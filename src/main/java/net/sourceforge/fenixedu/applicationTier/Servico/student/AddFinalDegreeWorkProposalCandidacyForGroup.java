/*
 * Created on 2004/04/19
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
public class AddFinalDegreeWorkProposalCandidacyForGroup {

    @Atomic
    public static Boolean run(final FinalDegreeWorkGroup group, String proposalOID) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        Proposal proposal = FenixFramework.getDomainObject(proposalOID);
        if (group != null && group.getGroupProposals() != null
        /* && !CollectionUtils.exists(group.getStudents(), ) */) {
            Scheduleing scheduleing = group.getExecutionDegree().getScheduling();
            if (scheduleing == null || scheduleing.getMaximumNumberOfProposalCandidaciesPerGroup() == null) {
                throw new MaximumNumberOfProposalCandidaciesPerGroupUndefinedException();
            } else if (scheduleing.getMaximumNumberOfProposalCandidaciesPerGroup().intValue() <= group.getGroupProposals().size()) {
                throw new MaximumNumberOfProposalCandidaciesPerGroupReachedException(scheduleing
                        .getMaximumNumberOfProposalCandidaciesPerGroup().toString());
            }
            if (scheduleing == null || scheduleing.getMinimumNumberOfStudents() == null) {
                throw new MinimumNumberOfStudentsUndefinedException();
            } else if (scheduleing.getMinimumNumberOfStudents().intValue() > group.getGroupStudents().size()) {
                throw new MinimumNumberOfStudentsNotReachedException(scheduleing.getMinimumNumberOfStudents().toString());
            }

            GroupProposal groupProposal = new GroupProposal();
            groupProposal.setFinalDegreeWorkProposal(proposal);
            groupProposal.setFinalDegreeDegreeWorkGroup(group);
            groupProposal.setOrderOfPreference(Integer.valueOf(group.getGroupProposals().size()));
            return true;
        }

        return false;

    }

    public static class MaximumNumberOfProposalCandidaciesPerGroupUndefinedException extends FenixServiceException {

        public MaximumNumberOfProposalCandidaciesPerGroupUndefinedException() {
            super();
        }

        public MaximumNumberOfProposalCandidaciesPerGroupUndefinedException(int errorType) {
            super(errorType);
        }

        public MaximumNumberOfProposalCandidaciesPerGroupUndefinedException(String s) {
            super(s);
        }

        public MaximumNumberOfProposalCandidaciesPerGroupUndefinedException(Throwable cause) {
            super(cause);
        }

        public MaximumNumberOfProposalCandidaciesPerGroupUndefinedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MaximumNumberOfProposalCandidaciesPerGroupReachedException extends FenixServiceException {

        public MaximumNumberOfProposalCandidaciesPerGroupReachedException() {
            super();
        }

        public MaximumNumberOfProposalCandidaciesPerGroupReachedException(int errorType) {
            super(errorType);
        }

        public MaximumNumberOfProposalCandidaciesPerGroupReachedException(String s) {
            super(s);
        }

        public MaximumNumberOfProposalCandidaciesPerGroupReachedException(Throwable cause) {
            super(cause);
        }

        public MaximumNumberOfProposalCandidaciesPerGroupReachedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MinimumNumberOfStudentsUndefinedException extends FenixServiceException {

        public MinimumNumberOfStudentsUndefinedException() {
            super();
        }

        public MinimumNumberOfStudentsUndefinedException(int errorType) {
            super(errorType);
        }

        public MinimumNumberOfStudentsUndefinedException(String s) {
            super(s);
        }

        public MinimumNumberOfStudentsUndefinedException(Throwable cause) {
            super(cause);
        }

        public MinimumNumberOfStudentsUndefinedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MinimumNumberOfStudentsNotReachedException extends FenixServiceException {

        public MinimumNumberOfStudentsNotReachedException() {
            super();
        }

        public MinimumNumberOfStudentsNotReachedException(int errorType) {
            super(errorType);
        }

        public MinimumNumberOfStudentsNotReachedException(String s) {
            super(s);
        }

        public MinimumNumberOfStudentsNotReachedException(Throwable cause) {
            super(cause);
        }

        public MinimumNumberOfStudentsNotReachedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}