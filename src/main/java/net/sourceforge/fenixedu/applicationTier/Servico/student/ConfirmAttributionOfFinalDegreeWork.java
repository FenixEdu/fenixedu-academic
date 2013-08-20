/*
 * Created on 2004/04/25
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Luis Cruz
 */
public class ConfirmAttributionOfFinalDegreeWork {

    public ConfirmAttributionOfFinalDegreeWork() {
        super();
    }

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static Boolean run(String username, String selectedGroupProposalOID) throws FenixServiceException {
        GroupProposal groupProposal = AbstractDomainObject.fromExternalId(selectedGroupProposalOID);

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

                List groupStudents = group.getGroupStudents();
                if (groupStudents != null && !groupStudents.isEmpty()) {
                    for (int i = 0; i < groupStudents.size(); i++) {
                        GroupStudent groupStudent = (GroupStudent) groupStudents.get(i);
                        if (groupStudent != null && groupStudent.getRegistration().getPerson().hasUsername(username)) {
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