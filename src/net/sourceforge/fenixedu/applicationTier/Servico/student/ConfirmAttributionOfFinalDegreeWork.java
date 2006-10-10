/*
 * Created on 2004/04/25
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class ConfirmAttributionOfFinalDegreeWork extends Service {

    public ConfirmAttributionOfFinalDegreeWork() {
        super();
    }

    public boolean run(String username, Integer selectedGroupProposalOID) throws ExcepcaoPersistencia,
            FenixServiceException {
        GroupProposal groupProposal = rootDomainObject.readGroupProposalByOID(selectedGroupProposalOID);

        if (groupProposal != null) {
            Group groupAttributed = groupProposal.getFinalDegreeWorkProposal()
                    .getGroupAttributedByTeacher();

            if (groupAttributed == null) {
                throw new NoAttributionToConfirmException();
            }

            Group group = groupProposal.getFinalDegreeDegreeWorkGroup();
            if (group != null) {
                if (!group.getIdInternal().equals(groupAttributed.getIdInternal())) {
                    throw new NoAttributionToConfirmException();
                }

                List groupStudents = group.getGroupStudents();
                if (groupStudents != null && !groupStudents.isEmpty()) {
                    for (int i = 0; i < groupStudents.size(); i++) {
                        GroupStudent groupStudent = (GroupStudent) groupStudents.get(i);
                        if (groupStudent != null
                                && groupStudent.getStudent().getPerson().hasUsername(username)) {
                            groupStudent.setFinalDegreeWorkProposalConfirmation(groupProposal
                                    .getFinalDegreeWorkProposal());
                        }
                    }
                }
            }
        }

        return true;
    }

    public class NoAttributionToConfirmException extends FenixServiceException {

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