package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeCandidacyForGraduatedPersonEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.LocalDate;

public class DegreeCandidacyForGraduatedPerson extends DegreeCandidacyForGraduatedPerson_Base {

    private DegreeCandidacyForGraduatedPerson() {
	super();
    }

    DegreeCandidacyForGraduatedPerson(final DegreeCandidacyForGraduatedPersonIndividualProcess process,
	    final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {

	this();

	final Person person = bean.getOrCreatePersonFromBean();
	checkParameters(person, process, bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	init(person, process, bean.getCandidacyDate());
	setSelectedDegree(bean.getSelectedDegree());

	createPrecedentDegreeInformation(bean);
	createDebt();
    }

    private void checkParameters(final Person person, final DegreeCandidacyForGraduatedPersonIndividualProcess process,
	    final LocalDate candidacyDate, final Degree selectedDegree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(person, process, candidacyDate);

	if (person.hasValidDegreeCandidacyForGraduatedPerson(process.getCandidacyExecutionInterval())) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.person.already.has.candidacy", process
		    .getCandidacyExecutionInterval().getName());
	}

	if (selectedDegree == null) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.degree");
	}

	if (personHasDegree(person, selectedDegree)) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.existing.degree", selectedDegree.getName());
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.precedentDegreeInformation");
	}
    }

    private void createDebt() {
	new DegreeCandidacyForGraduatedPersonEvent(this, getPerson());
    }

    @Override
    public DegreeCandidacyForGraduatedPersonIndividualProcess getCandidacyProcess() {
	return (DegreeCandidacyForGraduatedPersonIndividualProcess) super.getCandidacyProcess();
    }

    public void editCandidacyInformation(final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean) {
	checkParameters(bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	setCandidacyDate(bean.getCandidacyDate());
	setSelectedDegree(bean.getSelectedDegree());

	if (getPrecedentDegreeInformation().isExternal()) {
	    getPrecedentDegreeInformation().edit(bean.getPrecedentDegreeInformation());
	}
    }

    private void checkParameters(final LocalDate candidacyDate, final Degree selectedDegree,
	    CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(getPerson(), getCandidacyProcess(), candidacyDate);
	if (selectedDegree == null || personHasDegree(getPerson(), selectedDegree)) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.degrees");
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.invalid.precedentDegreeInformation");
	}
    }

    void editCandidacyResult(final DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean bean) {
	checkParameters(bean);

	setAffinity(bean.getAffinity());
	setDegreeNature(bean.getDegreeNature());
	setCandidacyGrade(bean.getGrade());

	if (isCandidacyResultStateValid(bean.getState())) {
	    setState(bean.getState());
	}
    }

    private void checkParameters(final DegreeCandidacyForGraduatedPersonIndividualCandidacyResultBean bean) {
	if (isAccepted() && bean.getState() != IndividualCandidacyState.ACCEPTED && hasRegistration()) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPerson.cannot.change.state.from.accepted.candidacies");
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
