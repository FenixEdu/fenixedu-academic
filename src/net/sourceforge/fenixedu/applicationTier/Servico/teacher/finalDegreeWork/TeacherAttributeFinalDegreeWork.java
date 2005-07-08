/*
 * Created on 2004/04/24
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class TeacherAttributeFinalDegreeWork implements IService {

    public TeacherAttributeFinalDegreeWork() {
        super();
    }

    public Boolean run(Integer selectedGroupProposalOID) throws FenixServiceException {
        try {
            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentFinalDegreeWork persistentFinalWork = persistentSupport
                    .getIPersistentFinalDegreeWork();

            IGroupProposal groupProposal = (IGroupProposal) persistentFinalWork.readByOID(
                    GroupProposal.class, selectedGroupProposalOID);
            if (groupProposal != null) {
                IProposal proposal = groupProposal.getFinalDegreeWorkProposal();
                IGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
                if (proposal != null && group != null) {
                    IProposal proposalAttributedToGroup = persistentFinalWork
                            .readFinalDegreeWorkAttributedToGroupByTeacher(group.getIdInternal());
                    if (proposalAttributedToGroup != null
                            && !proposalAttributedToGroup.getIdInternal().equals(
                                    proposal.getIdInternal())) {
                        throw new GroupAlreadyAttributed(proposalAttributedToGroup.getProposalNumber()
                                .toString());
                    }

                    persistentFinalWork.simpleLockWrite(proposal);
                    if (proposal.getGroupAttributedByTeacher() == null
                            || !proposal.getGroupAttributedByTeacher().equals(group)) {
                        proposal.setGroupAttributedByTeacher(group);
                        for (int i = 0; i < group.getGroupProposals().size(); i++) {
                            IGroupProposal otherGroupProposal = group
                                    .getGroupProposals().get(i);
                            IProposal otherProposal = otherGroupProposal.getFinalDegreeWorkProposal();
                            if (!otherProposal.getIdInternal().equals(proposal.getIdInternal())
                                    && group.equals(otherProposal.getGroupAttributedByTeacher())) {
                                persistentFinalWork.simpleLockWrite(otherProposal);
                                otherProposal.setGroupAttributedByTeacher(null);
                            }
                        }
                    } else {
                        proposal.setGroupAttributedByTeacher(null);
                    }
                }
            }
            return new Boolean(true);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
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