package net.sourceforge.fenixedu.domain.elections;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

public class DelegateElectionVote extends DelegateElectionVote_Base {

	protected DelegateElectionVote() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public DelegateElectionVote(DelegateElectionVotingPeriod votingPeriod, Student student) {
		this();
		checkParameters(student, votingPeriod);
		setDelegateElection(votingPeriod);
		setStudent(student);
	}

	public void delete() {
		removeStudent();
		removeDelegateElection();
		removeRootDomainObject();
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

}
