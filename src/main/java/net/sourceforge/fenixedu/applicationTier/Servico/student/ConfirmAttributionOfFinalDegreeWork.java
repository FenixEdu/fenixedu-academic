/*
 * Created on 2004/04/25
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
public class ConfirmAttributionOfFinalDegreeWork {

    public ConfirmAttributionOfFinalDegreeWork() {
        super();
    }

    @Atomic
    public static Boolean run(String username, String selectedGroupProposalOID) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        GroupProposal groupProposal = FenixFramework.getDomainObject(selectedGroupProposalOID);

        if (groupProposal != null) {
            FinalDegreeWorkGroup groupAttributed = groupProposal.getFinalDegreeWorkProposal().getGroupAttributedByTeacher();

            if (groupAttributed == null) {
                throw new NoAttributionToConfirmException();
            }

            FinalDegreeWorkGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
            if (group != null) {
                if (!group.getExternalId().equals(groupAttributed.getExternalId())) {
                    throw new NoAttributionToConfirmException();
                }

                Collection<GroupStudent> groupStudents = group.getGroupStudents();
                if (groupStudents != null && !groupStudents.isEmpty()) {
                    for (GroupStudent groupStudent : groupStudents) {
                        if (groupStudent != null && groupStudent.getRegistration().getPerson().getUsername().equals(username)) {
                            groupStudent.setFinalDegreeWorkProposalConfirmation(groupProposal.getFinalDegreeWorkProposal());
                        }
                    }
                }
            }
        }

        return true;
    }

    public static class NoAttributionToConfirmException extends FenixServiceException {

        public NoAttributionToConfirmException() {
            super();
        }

        public NoAttributionToConfirmException(int errorType) {
            super(errorType);
        }

        public NoAttributionToConfirmException(String s) {
            super(s);
        }

        public NoAttributionToConfirmException(Throwable cause) {
            super(cause);
        }

        public NoAttributionToConfirmException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}