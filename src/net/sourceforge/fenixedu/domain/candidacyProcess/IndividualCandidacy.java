package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

abstract public class IndividualCandidacy extends IndividualCandidacy_Base {

    protected IndividualCandidacy() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
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

    public boolean isDebtPayed() {
	return hasEvent() && getEvent().isClosed();
    }
}
