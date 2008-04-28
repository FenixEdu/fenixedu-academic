package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.Over23IndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Over23IndividualCandidacy extends Over23IndividualCandidacy_Base {

    private Over23IndividualCandidacy() {
	super();
    }

    Over23IndividualCandidacy(final Over23IndividualCandidacyProcess process, final Person person, final List<Degree> degrees) {
	this();
	checkParameters(person, process, degrees);
	setPerson(person);
	setCandidacyProcess(process);
	setState(IndividualCandidacyState.STAND_BY);
	createDegreeEntries(degrees);
	createDebt(person);
    }

    private void checkParameters(final Person person, final Over23IndividualCandidacyProcess process, final List<Degree> degrees) {
	if (person == null || person.hasStudent()) {
	    throw new DomainException("error.Over23IndividualCandidacy.invalid.person");
	}
	if (process == null) {
	    throw new DomainException("error.Over23IndividualCandidacy.invalid.process");
	}
	if (degrees == null || degrees.isEmpty()) {
	    throw new DomainException("error.Over23IndividualCandidacy.invalid.degrees");
	}
    }

    private void createDegreeEntries(final List<Degree> degrees) {
	for (int index = 0; index < degrees.size(); index++) {
	    new Over23IndividualCandidacyDegreeEntry(this, degrees.get(index), index + 1);
	}
    }

    private void removeExistingDegreeEntries() {
	while (hasAnyOver23IndividualCandidacyDegreeEntries()) {
	    getOver23IndividualCandidacyDegreeEntries().get(0).delete();
	}
    }

    private void createDebt(final Person person) {
	new Over23IndividualCandidacyEvent(this, person);
    }

    void editPersonalCandidacyInformation(final PersonBean personBean) {
	getPerson().edit(personBean);
    }

    boolean hasAnyPayment() {
	return getEvent().hasAnyPayments();
    }

    boolean isDebtPayed() {
	return getEvent().isClosed();
    }

    boolean isAccepted() {
	return getState() == IndividualCandidacyState.ACCEPTED;
    }

    void cancel() {
	checkRulesToCancel();
	setState(IndividualCandidacyState.CANCELLED);

	//TODO: check this
	throw new DomainException("error.must.add.responsible.person");
    }

    private void checkRulesToCancel() {
	if (hasAnyPayment()) {
	    throw new DomainException("error.Over23IndividualCandidacy.cannot.cancel.candidacy.with.payments");
	}
    }

    void modifyChoosenDegrees(final List<Degree> degrees) {
	if (degrees == null || degrees.isEmpty()) {
	    throw new DomainException("error.Over23IndividualCandidacy.invalid.degrees");
	}
	removeExistingDegreeEntries();
	createDegreeEntries(degrees);
    }

    public List<Degree> getSelectedDegreesSortedByOrder() {
	final Set<Over23IndividualCandidacyDegreeEntry> entries = new TreeSet<Over23IndividualCandidacyDegreeEntry>(
		Over23IndividualCandidacyDegreeEntry.COMPARATOR_BY_ORDER);
	entries.addAll(getOver23IndividualCandidacyDegreeEntries());

	final List<Degree> result = new ArrayList<Degree>();
	for (Over23IndividualCandidacyDegreeEntry entry : entries) {
	    result.add(entry.getDegree());
	}
	return result;
    }
}
