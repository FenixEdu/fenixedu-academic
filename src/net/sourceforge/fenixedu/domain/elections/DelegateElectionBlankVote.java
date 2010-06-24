package net.sourceforge.fenixedu.domain.elections;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

public class DelegateElectionBlankVote extends DelegateElectionBlankVote_Base {

    public DelegateElectionBlankVote(DelegateElection election) {
	super();
	checkParameters(election);
	setDelegateElection(election);
    }

    @Override
    public void delete() {
	removeDelegateElection();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    private void checkParameters(final DelegateElection election) {
	if (election == null) {
	    throw new DomainException("error.delegateElection.cannot.be.null");
	}
    }

    public static boolean isBlankVote(Student votedStudent) {
	return votedStudent == null;
    }
}
