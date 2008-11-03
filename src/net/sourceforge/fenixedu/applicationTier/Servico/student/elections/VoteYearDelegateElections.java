package net.sourceforge.fenixedu.applicationTier.Servico.student.elections;

import java.util.Collections;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVote;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.Email;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class VoteYearDelegateElections extends FenixService {

    public void run(YearDelegateElection yearDelegateElection, Student student, Student votedStudent)
	    throws FenixServiceException {

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.DelegateResources", Language.getLocale());
	try {
	    if (!yearDelegateElection.getVotingStudents().contains(student)) {
		final String fromName = bundle.getString("VoteYearDelegateElections.email.fromName");
		final String fromAddress = bundle.getString("VoteYearDelegateElections.email.fromAddrees");
		final String subject = bundle.getString("VoteYearDelegateElections.email.subject");
		final String msg = bundle.getString("VoteYearDelegateElections.email.message");
		final Person person = student.getPerson();
		DelegateElectionVote vote = new DelegateElectionVote(yearDelegateElection, votedStudent);
		yearDelegateElection.addVotingStudents(student);
		yearDelegateElection.addVotes(vote);
		new Email(fromName, fromAddress, null, Collections.singletonList(person.getEmail()), null, null, subject, msg);
	    } else {
		throw new FenixServiceException("error.student.elections.voting.studentAlreadyVoted");
	    }
	} catch (DomainException ex) {
	    throw new FenixServiceException(ex.getMessage(), ex.getArgs());
	}
    }

}
