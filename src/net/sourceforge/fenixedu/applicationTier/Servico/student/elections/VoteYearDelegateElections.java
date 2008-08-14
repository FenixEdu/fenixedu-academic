package net.sourceforge.fenixedu.applicationTier.Servico.student.elections;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVote;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class VoteYearDelegateElections extends Service {

    public void run(YearDelegateElection yearDelegateElection, Student student, Student votedStudent)
	    throws FenixServiceException {

	try {
	    if (!yearDelegateElection.getVotingStudents().contains(student)) {
		final String msg = "A sua votação para a eleição de Delegado de Ano encontra-se registada. Obrigado pela sua participação.";
		final Person person = student.getPerson();
		DelegateElectionVote vote = new DelegateElectionVote(yearDelegateElection, votedStudent);
		yearDelegateElection.addVotingStudents(student);
		yearDelegateElection.addVotes(vote);
		new Email("Comissão Eleitoral de Delegados de Ano", "ce-delegados-ano@mlists.ist.utl.pt", null, Collections
			.singletonList(person.getEmail()), null, null, "Eleição de Delegado de Ano", msg);
	    } else {
		throw new FenixServiceException("error.student.elections.voting.studentAlreadyVoted");
	    }
	} catch (DomainException ex) {
	    throw new FenixServiceException(ex.getMessage(), ex.getArgs());
	}
    }

}
