/*
 * Created on 2004/04/25
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class ConfirmAttributionOfFinalDegreeWork implements IService {

    public ConfirmAttributionOfFinalDegreeWork() {
        super();
    }

    public boolean run(String username, Integer selectedGroupProposalOID) throws ExcepcaoPersistencia,
            FenixServiceException {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        IGroupProposal groupProposal = (IGroupProposal) persistentFinalDegreeWork.readByOID(
                GroupProposal.class, selectedGroupProposalOID);

        if (groupProposal != null) {
            IGroup groupAttributed = groupProposal.getFinalDegreeWorkProposal()
                    .getGroupAttributedByTeacher();

            if (groupAttributed == null) {
                throw new NoAttributionToConfirmException();
            }

            IGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
            if (group != null) {
                if (!group.getIdInternal().equals(groupAttributed.getIdInternal())) {
                    throw new NoAttributionToConfirmException();
                }

                List groupStudents = group.getGroupStudents();
                if (groupStudents != null && !groupStudents.isEmpty()) {
                    for (int i = 0; i < groupStudents.size(); i++) {
                        IGroupStudent groupStudent = (IGroupStudent) groupStudents.get(i);
                        if (groupStudent != null
                                && groupStudent.getStudent().getPerson().getUsername().equals(username)) {
                            persistentFinalDegreeWork.simpleLockWrite(groupStudent);
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