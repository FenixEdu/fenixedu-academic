/*
 * Created on 2004/04/19
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IScheduleing;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 *  
 */
public class AddFinalDegreeWorkProposalCandidacyForGroup implements IService {

    public AddFinalDegreeWorkProposalCandidacyForGroup() {
        super();
    }

    public boolean run(Integer groupOID, Integer proposalOID) throws ExcepcaoPersistencia,
            FenixServiceException {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        IGroup group = (IGroup) persistentFinalDegreeWork.readByOID(Group.class, groupOID);
        IProposal proposal = (IProposal) persistentFinalDegreeWork
                .readByOID(Proposal.class, proposalOID);
        if (group != null && group.getGroupProposals() != null
        /* && !CollectionUtils.exists(group.getStudents(), ) */) {
            IScheduleing scheduleing = persistentFinalDegreeWork.readFinalDegreeWorkScheduleing(group
                    .getExecutionDegree().getIdInternal());
            if (scheduleing == null
                    || scheduleing.getMaximumNumberOfProposalCandidaciesPerGroup() == null) {
                throw new MaximumNumberOfProposalCandidaciesPerGroupUndefinedException();
            } else if (scheduleing.getMaximumNumberOfProposalCandidaciesPerGroup().intValue() <= group
                    .getGroupProposals().size()) {
                throw new MaximumNumberOfProposalCandidaciesPerGroupReachedException(scheduleing
                        .getMaximumNumberOfProposalCandidaciesPerGroup().toString());
            }
            if (scheduleing == null || scheduleing.getMinimumNumberOfStudents() == null) {
                throw new MinimumNumberOfStudentsUndefinedException();
            } else if (scheduleing.getMinimumNumberOfStudents().intValue() > group.getGroupStudents()
                    .size()) {
                throw new MinimumNumberOfStudentsNotReachedException(scheduleing
                        .getMinimumNumberOfStudents().toString());
            }

            GroupProposal groupProposal = new GroupProposal();
            persistentFinalDegreeWork.simpleLockWrite(groupProposal);
            groupProposal.setFinalDegreeWorkProposal(proposal);
            groupProposal.setFinalDegreeDegreeWorkGroup(group);
            groupProposal.setOrderOfPreference(new Integer(group.getGroupProposals().size() + 1));
            return true;
        }

        return false;

    }

    public class MaximumNumberOfProposalCandidaciesPerGroupUndefinedException extends
            FenixServiceException {

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

        public MaximumNumberOfProposalCandidaciesPerGroupUndefinedException(String message,
                Throwable cause) {
            super(message, cause);
        }
    }

    public class MaximumNumberOfProposalCandidaciesPerGroupReachedException extends
            FenixServiceException {

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

    public class MinimumNumberOfStudentsUndefinedException extends FenixServiceException {

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

    public class MinimumNumberOfStudentsNotReachedException extends FenixServiceException {

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