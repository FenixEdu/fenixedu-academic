package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.Over23IndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Over23IndividualCandidacy extends Over23IndividualCandidacy_Base {

    private Over23IndividualCandidacy() {
	super();
    }

    Over23IndividualCandidacy(final Over23IndividualCandidacyProcess process, final Person person, final List<Degree> degrees,
	    final String disabilities, final String education, final String languages) {
	this();
	checkParameters(person, process, degrees);

	setPerson(person);
	setCandidacyProcess(process);
	setDisabilities(disabilities);
	setEducation(education);
	setLanguages(languages);
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

    public void cancel(final Person person) {
	checkRulesToCancel();
	setState(IndividualCandidacyState.CANCELLED);
	setResponsible(person.getUsername());
    }

    private void checkRulesToCancel() {
	if (hasAnyPayment()) {
	    throw new DomainException("error.Over23IndividualCandidacy.cannot.cancel.candidacy.with.payments");
	}
    }

    void saveChoosedDegrees(final List<Degree> degrees) {
	if (!degrees.isEmpty()) {
	    removeExistingDegreeEntries();
	    createDegreeEntries(degrees);
	}
    }

    void editCandidacyInformation(final List<Degree> degrees, final String disabilities, final String education,
	    final String languages) {
	saveChoosedDegrees(degrees);
	setDisabilities(disabilities);
	setEducation(education);
	setLanguages(languages);
    }

    List<Degree> getSelectedDegrees() {
	final List<Degree> result = new ArrayList<Degree>(getOver23IndividualCandidacyDegreeEntriesCount());
	for (final Over23IndividualCandidacyDegreeEntry entry : getOver23IndividualCandidacyDegreeEntries()) {
	    result.add(entry.getDegree());
	}
	return result;
    }

    List<Degree> getSelectedDegreesSortedByOrder() {
	final Set<Over23IndividualCandidacyDegreeEntry> entries = new TreeSet<Over23IndividualCandidacyDegreeEntry>(
		Over23IndividualCandidacyDegreeEntry.COMPARATOR_BY_ORDER);
	entries.addAll(getOver23IndividualCandidacyDegreeEntries());

	final List<Degree> result = new ArrayList<Degree>(entries.size());
	for (final Over23IndividualCandidacyDegreeEntry entry : entries) {
	    result.add(entry.getDegree());
	}
	return result;
    }

    Person getResponsiblePerson() {
	return Person.readPersonByUsername(getResponsible());
    }

    void setCandidacyResult(final IndividualCandidacyState state, final Degree acceptedDegree) {
	checkParameters(state, acceptedDegree);
	setState(state);
	setAcceptedDegree(acceptedDegree);
    }

    private void checkParameters(final IndividualCandidacyState state, final Degree acceptedDegree) {
	if (state == null) {
	    throw new DomainException("error.Over23IndividualCandidacy.invalid.state");
	}
	if (state == IndividualCandidacyState.ACCEPTED
		&& (acceptedDegree == null || !getSelectedDegrees().contains(acceptedDegree))) {
	    throw new DomainException("error.Over23IndividualCandidacy.invalid.acceptedDegree");
	}
    }
}
