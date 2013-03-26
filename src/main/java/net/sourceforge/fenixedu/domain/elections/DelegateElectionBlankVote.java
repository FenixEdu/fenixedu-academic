package net.sourceforge.fenixedu.domain.elections;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

public class DelegateElectionBlankVote extends DelegateElectionBlankVote_Base {

    public DelegateElectionBlankVote(DelegateElectionVotingPeriod votingPeriod) {
        super();
        checkParameters(votingPeriod);
        setDelegateElection(votingPeriod);
    }

    @Override
    public void delete() {
        removeDelegateElection();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    private void checkParameters(final DelegateElectionVotingPeriod votingPeriod) {
        if (votingPeriod == null) {
            throw new DomainException("error.votingPeriod.cannot.be.null");
        }
    }

    public static boolean isBlankVote(Student votedStudent) {
        return votedStudent == null;
    }
}
