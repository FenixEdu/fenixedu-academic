package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

abstract public class IndividualCandidacyProcess extends IndividualCandidacyProcess_Base {

    protected IndividualCandidacyProcess() {
	super();
    }

    public ExecutionInterval getCandidacyExecutionInterval() {
	return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyExecutionInterval() : null;
    }

    public boolean isFor(final ExecutionInterval executionInterval) {
	return getCandidacyExecutionInterval() == executionInterval;
    }

    public DateTime getCandidacyStart() {
	return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyStart() : null;
    }

    public DateTime getCandidacyEnd() {
	return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyEnd() : null;
    }

    public YearMonthDay getCandidacyDate() {
	return getCandidacy().getCandidacyDate();
    }

    public boolean hasOpenCandidacyPeriod() {
	return hasCandidacyProcess() && getCandidacyProcess().hasOpenCandidacyPeriod();
    }

    public boolean hasOpenCandidacyPeriod(final DateTime date) {
	return hasCandidacyProcess() && getCandidacyProcess().hasOpenCandidacyPeriod(date);
    }

    public boolean isInStandBy() {
	return hasCandidacyProcess() && getCandidacyProcess().isInStandBy();
    }

    public boolean isSentToJury() {
	return hasCandidacyProcess() && getCandidacyProcess().isSentToJury();
    }

    public boolean isSentToCoordinator() {
	return hasCandidacyProcess() && getCandidacyProcess().isSentToCoordinator();
    }

    public boolean isSentToScientificCouncil() {
	return hasCandidacyProcess() && getCandidacyProcess().isSentToScientificCouncil();
    }

    public boolean isPublished() {
	return hasCandidacyProcess() && getCandidacyProcess().isPublished();
    }

    protected boolean hasAnyPaymentForCandidacy() {
	return getCandidacy().hasAnyPayment();
    }

    protected void cancelCandidacy(final Person person) {
	getCandidacy().cancel(person);
    }

    public IndividualCandidacyState getCandidacyState() {
	return getCandidacy().getState();
    }

    public boolean isCandidacyInStandBy() {
	return getCandidacy().isInStandBy();
    }

    public boolean isCandidacyAccepted() {
	return getCandidacy().isAccepted();
    }

    public boolean isCandidacyRejected() {
	return getCandidacy().isRejected();
    }

    public boolean isCandidacyCancelled() {
	return getCandidacy().isCancelled();
    }

    protected boolean isCandidacyDebtPayed() {
	return getCandidacy().isDebtPayed();
    }

    public Person getCandidacyPerson() {
	return getCandidacy().getPerson();
    }

    public boolean hasRegistrationForCandidacy() {
	return getCandidacy().hasRegistration();
    }
}
