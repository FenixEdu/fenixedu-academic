package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.Over23IndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.LocalDate;

public class Over23IndividualCandidacy extends Over23IndividualCandidacy_Base {

    private Over23IndividualCandidacy() {
	super();
    }

    Over23IndividualCandidacy(final Over23IndividualCandidacyProcess process, final Over23IndividualCandidacyProcessBean bean) {
	this();

	final Person person = bean.getOrCreatePersonFromBean();
	checkParameters(person, process, bean.getCandidacyDate(), bean.getSelectedDegrees());

	init(person, process, bean.getCandidacyDate());

	setDisabilities(bean.getDisabilities());
	setEducation(bean.getEducation());
	setLanguages(bean.getLanguages());

	createDegreeEntries(bean.getSelectedDegrees());
	createDebt(person);
    }

    private void checkParameters(final Person person, final Over23IndividualCandidacyProcess process,
	    final LocalDate candidacyDate, final List<Degree> degrees) {

	checkParameters(person, process, candidacyDate);

	if (person.hasStudent()) {
	    throw new DomainException("error.Over23IndividualCandidacy.invalid.person");
	}

	if (person.hasValidOver23IndividualCandidacy(process.getCandidacyExecutionInterval())) {
	    throw new DomainException("error.Over23IndividualCandidacy.person.already.has.candidacy", process
		    .getCandidacyExecutionInterval().getName());
	}

	checkDegrees(degrees);
    }

    private void checkDegrees(final List<Degree> degrees) {
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

    void saveChoosedDegrees(final List<Degree> degrees) {
	if (!degrees.isEmpty()) {
	    removeExistingDegreeEntries();
	    createDegreeEntries(degrees);
	}
    }

    @Override
    public Over23IndividualCandidacyProcess getCandidacyProcess() {
	return (Over23IndividualCandidacyProcess) super.getCandidacyProcess();
    }

    void editCandidacyInformation(final LocalDate candidacyDate, final List<Degree> degrees, final String disabilities,
	    final String education, final String languages) {

	checkParameters(getPerson(), getCandidacyProcess(), candidacyDate);
	checkDegrees(degrees);

	setCandidacyDate(candidacyDate);
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

    void editCandidacyResult(final IndividualCandidacyState state, final Degree acceptedDegree) {
	checkParameters(state, acceptedDegree);
	setAcceptedDegree(acceptedDegree);
	if (isCandidacyResultStateValid(state)) {
	    setState(state);
	}
    }

    private void checkParameters(final IndividualCandidacyState state, final Degree acceptedDegree) {
	if (state != null) {
	    if (state == IndividualCandidacyState.ACCEPTED
		    && (acceptedDegree == null || !getSelectedDegrees().contains(acceptedDegree))) {
		throw new DomainException("error.Over23IndividualCandidacy.invalid.acceptedDegree");
	    }

	    if (isAccepted() && state != IndividualCandidacyState.ACCEPTED && hasRegistration()) {
		throw new DomainException("error.Over23IndividualCandidacy.cannot.change.state.from.accepted.candidacies");
	    }
	}
    }
    
    @Override
    protected ExecutionYear getCandidacyExecutionInterval() {
        return (ExecutionYear) super.getCandidacyExecutionInterval();
    }
    
    @Override
    protected Registration createRegistration(Person person, DegreeCurricularPlan degreeCurricularPlan, CycleType cycleType,
	    Ingression ingression) {
	final Registration registration = super.createRegistration(person, degreeCurricularPlan, cycleType, ingression);
	registration.setRegistrationYear(getCandidacyExecutionInterval().hasNextExecutionYear() ? getCandidacyExecutionInterval()
		.getNextExecutionYear() : getCandidacyExecutionInterval());
	return registration;
    }

}
