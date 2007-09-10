package net.sourceforge.fenixedu.applicationTier.Servico.student.elections;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVote;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class VoteYearDelegateElections extends Service  {
	
	public void run(YearDelegateElection yearDelegateElection, Student student, Student votedStudent) throws FenixServiceException, ExcepcaoPersistencia {
		
		try {
			if(!yearDelegateElection.getVotingStudents().contains(student)) {
				DelegateElectionVote vote = new DelegateElectionVote(yearDelegateElection, votedStudent);
				yearDelegateElection.addVotingStudents(student);
				yearDelegateElection.addVotes(vote);
			}
			else {
				throw new FenixServiceException("error.student.elections.voting.studentAlreadyVoted");
			}
		} catch (DomainException ex) {
			throw new FenixServiceException(ex.getMessage(), ex.getArgs());
		}
	}

}
