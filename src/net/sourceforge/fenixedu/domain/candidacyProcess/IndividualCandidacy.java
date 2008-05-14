package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

abstract public class IndividualCandidacy extends IndividualCandidacy_Base {

    protected IndividualCandidacy() {
	super();
	super.setWhenCreated(new DateTime());
	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
	throw new DomainException("error.IndividualCandidacy.cannot.modify.when.created");
    }

    public boolean hasAnyPayment() {
	return hasEvent() && getEvent().hasAnyPayments();
    }

    public void editPersonalCandidacyInformation(final PersonBean personBean) {
	getPerson().edit(personBean);
    }

    abstract public void cancel(final Person person);

    public boolean isInStandBy() {
	return getState() == IndividualCandidacyState.STAND_BY;
    }

    public boolean isAccepted() {
	return getState() == IndividualCandidacyState.ACCEPTED;
    }

    public boolean isCancelled() {
	return getState() == IndividualCandidacyState.CANCELLED;
    }

    public boolean isRejected() {
	return getState() == IndividualCandidacyState.REJECTED;
    }

    public boolean isDebtPayed() {
	return hasEvent() && getEvent().isClosed();
    }

    public boolean isFor(final ExecutionInterval executionInterval) {
	return hasCandidacyProcess() && getCandidacyProcess().isFor(executionInterval);
    }
}
