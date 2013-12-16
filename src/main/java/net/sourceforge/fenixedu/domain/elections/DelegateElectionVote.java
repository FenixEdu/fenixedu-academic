package net.sourceforge.fenixedu.domain.elections;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

public class DelegateElectionVote extends DelegateElectionVote_Base {

    protected DelegateElectionVote() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public DelegateElectionVote(DelegateElectionVotingPeriod votingPeriod, Student student) {
        this();
        checkParameters(student, votingPeriod);
        setDelegateElection(votingPeriod);
        setStudent(student);
    }

    public void delete() {
        setStudent(null);
        setDelegateElection(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    private void checkParameters(final Student student, final DelegateElectionVotingPeriod votingPeriod) {
        if (student == null) {
            throw new DomainException("error.student.cannot.be.null");
        }
        if (votingPeriod == null) {
            throw new DomainException("error.votingPeriod.cannot.be.null");
        }
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDelegateElection() {
        return getDelegateElection() != null;
    }

}
